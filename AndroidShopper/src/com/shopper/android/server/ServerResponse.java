package com.shopper.android.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import android.content.Context;

import com.shopper.android.ApplicationModel;

public class ServerResponse {
		
	private int status;
	private String content;
	
	public ServerResponse(HttpResponse response,Context ctx) throws IllegalStateException, IOException {
		super();
		this.status = response.getStatusLine().getStatusCode();;
		System.out.println("Response status: "+ status);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        response.getEntity().writeTo(out);
        out.close();
		this.content = out.toString();
		System.out.println("Response content: "+ content);
		parseHeaders(response.getAllHeaders(), ctx);
	}

	private void parseHeaders(Header[] headers, Context ctx) {
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			String headerName = header.getName();		
			String headerValue = header.getValue();
			System.out.println(headerName + ": " + headerValue);
			if (headerName.equalsIgnoreCase(Constants.SET_COOKIE)) {				
				if (headerValue.startsWith(Constants.COOKIE_SESSION_ID)) {
					ApplicationModel.getInstance(ctx).setSessionId(headerValue);
				}else if (headerValue.startsWith(Constants.COOKIE_TOKEN)) {
					ApplicationModel.getInstance(ctx).setToken(headerValue);
				}
			}
		}
		System.out.println(ApplicationModel.getInstance(ctx).toString());
	}

	public int getStatus() {
		return status;
	}

	public String getContent() {
		return content;
	}
}
