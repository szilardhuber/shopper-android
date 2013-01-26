package com.shopper.android.server;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import android.content.Context;

import com.shopper.android.util.LocalStorage;

public class ServerResponse {
		
	private int status;
	private Header[] headers;
	private String error;
	private String sessionId;
	private String cookie;
	
	public ServerResponse(HttpResponse response,Context ctx) {
		super();
		this.status = response.getStatusLine().getStatusCode();;
		this.headers = response.getAllHeaders();
		parseHeaders();
		saveCommonHeaders(ctx);
	}
	private void saveCommonHeaders(Context ctx) {
		if (sessionId != null) {
			LocalStorage.setProperty(com.shopper.android.Constants.PREFERENCE_SESSION_ID, sessionId, ctx);
		}
		if (cookie != null) {
			LocalStorage.setProperty(com.shopper.android.Constants.PREFERENCE_COOKIE, cookie, ctx);
		}		
	}
	private void parseHeaders() {
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			String headerName = header.getName();		
			String headerValue = header.getValue();
			if (headerName.equals(Constants.HEADER_SHOPPER_ERROR)) {
				error = headerValue;
			} else if (headerName.equals(Constants.HEADER_SHOPPER_SESSION_ID)) {
				sessionId = headerValue;
			}else if (headerName.equals(Constants.HEADER_SHOPPER_COOKIE)) {
				cookie = headerValue;
			}
		}
	}

	public int getStatus() {
		return status;
	}

	public boolean isOk() {
		return getStatus() == HttpStatus.SC_OK;
	}
	
	public String getError() {
		return error;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public String getCookie() {
		return cookie;
	}
}
