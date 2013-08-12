package com.shopzenion.android.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import com.shopzenion.android.model.ApplicationModel;
import com.shopzenion.android.util.Logger;

public class ServerRequest {
	private static final String SERVER_ADDRESS = "https://szilardhuber.appspot.com"; 
	private static final String LOGIN_URL = "/User/Login/api";
	private static final String REGISTER_URL = "/User/Register/api";
	private static final String CREATE_SHOPPING_LIST_URL = "/api/v1/Lists";
	private static final String CREATE_ADD_ITEM_URL = "/api/v1/Lists/";
	
	private Context context;
	ServerResponseCallback callback;
	
	public void sendGet(String url,  Context ctx, ServerResponseCallback callback){
		this.context = ctx;
		this.callback = callback;
		if (!isOnline(ctx)) {
			callback.offline();
			return;
		}
		
		Logger.debug("Get request URI: " + SERVER_ADDRESS + url);
		HttpGet request = new HttpGet(SERVER_ADDRESS + url);
		addCommonHeaders(request, ctx);
		AsyncHttpGet get = new AsyncHttpGet();
		get.execute(request);
	}
	
	public static ServerResponse loginRequest(String email, String password, Context ctx) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    nameValuePairs.add(new BasicNameValuePair("email", email));
	    nameValuePairs.add(new BasicNameValuePair("password", password));
	    nameValuePairs.add(new BasicNameValuePair("remember", "True")); // always remember if mobile client
		return sendPost(LOGIN_URL,nameValuePairs,ctx);
	}
	
	public static ServerResponse registerRequest(String email, String password, Context ctx) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    nameValuePairs.add(new BasicNameValuePair("email", email));
	    nameValuePairs.add(new BasicNameValuePair("password", password));	    
		return sendPost(REGISTER_URL,nameValuePairs,ctx);
	}

	public static ServerResponse createShoppingListRequest(String name, Context ctx) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	    nameValuePairs.add(new BasicNameValuePair("name", name));
		return sendPost(CREATE_SHOPPING_LIST_URL,nameValuePairs,ctx);
	}
	
	public static ServerResponse addItemRequest(String listId ,String description, String quantity, Context ctx) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	    nameValuePairs.add(new BasicNameValuePair("description", description));
	    nameValuePairs.add(new BasicNameValuePair("quantity", quantity));
		return sendPost(CREATE_ADD_ITEM_URL + listId ,nameValuePairs,ctx);
	}

	private static ServerResponse sendPost(String url, List<NameValuePair> nameValuePairs, Context ctx){
		if (!isOnline(ctx)) {			
			Logger.debug("Phone is offline");
			return null;
		}
		HttpClient httpclient = getClient();
	    HttpResponse response;
		try {
			Logger.debug("Post request URI: " + SERVER_ADDRESS + url);
			HttpPost request = new HttpPost(SERVER_ADDRESS + url);
			if (nameValuePairs != null) {				
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}
			addCommonHeaders(request, ctx);
			response = httpclient.execute(request);
			return new ServerResponse(response, ctx);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();			
			return null;
		}
	}
	
	private static HttpClient getClient(){
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, Constants.SOCKET_TIMEOUT);
		
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));
		SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
		return new DefaultHttpClient(mgr, client.getParams());
	}


	private static void addCommonHeaders(AbstractHttpMessage request, Context ctx) {
		Map<String, String> commonHeaders =getCommonHeaders(ctx);		
		addHeaderValues(request, commonHeaders);
	}
	

	private static void addHeaderValues(AbstractHttpMessage request,
			Map<String, String> commonHEaders) {
		for (Map.Entry<String,String> entry : commonHEaders.entrySet()) {
			request.addHeader(entry.getKey(), entry.getValue());
		}
	}
		
	private static Map<String, String> getCommonHeaders(Context ctx){
		Map<String, String> ret = new HashMap<String, String>();
		ret.put(Constants.HEADER_SHOPPER_CLIENT_TYPE, "ANDROID");
		ret.put(Constants.HEADER_SHOPPER_CLIENT_VERSION, "" + Build.VERSION.SDK_INT);
		ApplicationModel appmodel = ApplicationModel.getInstance(ctx);
		String token = appmodel.getToken();
		if (token != null) {			
			ret.put(Constants.HEADER_TOKEN, token);			
		}
		String sid = appmodel.getSessionId();
		if (sid != null) {			
			ret.put(Constants.HEADER_SESSION_ID, sid);			
		}
		for (Map.Entry<String, String> entry : ret.entrySet()) {
			Logger.debug("Request header: "+entry.getKey() + ": " + entry.getValue());
		}
		return ret;		
	}
	
	private class AsyncHttpGet extends AsyncTask<HttpGet, Void, ServerResponse>{
		@Override
		protected ServerResponse doInBackground(HttpGet... params) {
			if (params != null && params.length > 0) {
				HttpClient httpclient = getClient();
				HttpResponse response;
				try {
					response = httpclient.execute(params[0]);
					return new ServerResponse(response, context);				
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(ServerResponse response) {
			callback.gotResponse(response);
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			callback.cancelled();
		}
	}
	
	private static boolean isOnline(Context ctx) {
	    ConnectivityManager cm =
	        (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
}