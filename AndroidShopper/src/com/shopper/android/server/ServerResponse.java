package com.shopper.android.server;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import android.content.Context;

import com.shopper.android.ApplicationModel;

public class ServerResponse {
		
	private int status;
	private Header[] headers;
	private String error;
	
	public ServerResponse(HttpResponse response,Context ctx) {
		super();
		this.status = response.getStatusLine().getStatusCode();;
		System.out.println("Response status: "+ status);
		this.headers = response.getAllHeaders();
		parseHeaders(ctx);
	}

	private void parseHeaders(Context ctx) {
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			String headerName = header.getName();		
			String headerValue = header.getValue();
			System.out.println(headerName + ": " + headerValue); 
			if (headerName.equals(Constants.HEADER_SHOPPER_ERROR)) {
				error = headerValue;
			} else if (headerName.equals(Constants.HEADER_SHOPPER_SESSION_ID)) {
				ApplicationModel.getInstance(ctx).setSessionId(headerValue);
			}else if (headerName.equals(Constants.HEADER_SHOPPER_COOKIE)) {
				ApplicationModel.getInstance(ctx).setCookie(headerValue);
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
}
