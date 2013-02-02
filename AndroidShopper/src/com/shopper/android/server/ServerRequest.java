package com.shopper.android.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.Build;

import com.shopper.android.util.LocalStorage;

public class ServerRequest {
	private static final String SERVER_ADDRESS = "https://szilardhuber.appspot.com"; 
	private static final String LOGIN_URL = "/user/login/api?email=%s&password=%s";
	private static final String REGISTER_URL = "/User/Register?email=%s&password=%s";

	private static ServerResponse send(String url, Context ctx){
		HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response;
		try {
			System.out.println("Requeest URI: " + SERVER_ADDRESS + url);
			HttpPost request = new HttpPost(SERVER_ADDRESS + url);
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


	private static void addCommonHeaders(HttpPost request, Context ctx) {
		Map<String, String> commonHeaders =getCommonHeaders(ctx);		
		addHeaderValues(request, commonHeaders);
	}

	private static void addHeaderValues(HttpPost request,
			Map<String, String> commonHEaders) {
		for (Map.Entry<String,String> entry : commonHEaders.entrySet()) {
			request.addHeader(entry.getKey(), entry.getValue());
		}
	}

	public static ServerResponse loginRequest(String email, String password, Context ctx) {
		return send(String.format(LOGIN_URL, new Object[]{email, password}),ctx);
	}
	
	public static ServerResponse registerRequest(String email, String password, Context ctx) {
		return send(String.format(REGISTER_URL, new Object[]{email, password}),ctx);
	}
	
	private static Map<String, String> getCommonHeaders(Context ctx){
		Map<String, String> ret = new HashMap<String, String>();
		ret.put(Constants.HEADER_SHOPPER_CLIENT_TYPE, "ANDROID");
		ret.put(Constants.HEADER_SHOPPER_CLIENT_VERSION, "" + Build.VERSION.SDK_INT);
		String sessionId = LocalStorage.getProperty(com.shopper.android.Constants.PREFERENCE_SESSION_ID, ctx);
		if (sessionId != null) {			
			ret.put(Constants.HEADER_SHOPPER_SESSION_ID, sessionId);
		}
		String cookie = LocalStorage.getProperty(com.shopper.android.Constants.PREFERENCE_COOKIE, ctx);
		if (sessionId != null) {			
			ret.put(Constants.HEADER_SHOPPER_COOKIE, cookie);			
		}
		return ret;		
	}
}