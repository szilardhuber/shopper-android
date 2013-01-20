package com.shopper.android.server;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class ServerRequest {
	private static final String SERVER_ADDRESS = "https://szilardhuber.appspot.com"; 
	private static final String LOGIN_URL = "/User/Login?email{0}&password{1}";
	private static final String REGISTER_URL = "/User/Register?email{0}&password{1}";

	private static ServerResponse send(String url){
		HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response;
		try {
			response = httpclient.execute(new HttpPost(SERVER_ADDRESS + url));
			int responseStatus = response.getStatusLine().getStatusCode();
			return new ServerResponse(responseStatus, response.getAllHeaders());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();			
			return null;
		}
	}


	public static ServerResponse loginRequest(String email, String password) {
		return send(String.format(LOGIN_URL, new Object[]{email, password}));
	}
	
	public static ServerResponse registerRequest(String email, String password) {
		return send(String.format(REGISTER_URL, new Object[]{email, password}));
	}
}