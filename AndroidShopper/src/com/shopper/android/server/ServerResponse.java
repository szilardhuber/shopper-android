package com.shopper.android.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import android.content.Context;

import com.shopper.android.model.ApplicationModel;
import com.shopper.android.util.Logger;

public class ServerResponse {
		
	private int status;
	private String content;
	
	public ServerResponse(HttpResponse response,Context ctx) throws IllegalStateException, IOException {
		super();
		this.status = response.getStatusLine().getStatusCode();;
		Logger.debug("Response status: "+ status);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        response.getEntity().writeTo(out);
        out.close();
		this.content = out.toString();
		Logger.debug("Response content: "+ content);
		parseHeaders(response.getAllHeaders(), ctx);
	}

	private void parseHeaders(Header[] headers, Context ctx) {
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			String headerName = header.getName();		
			String headerValue = header.getValue();
			Logger.debug(headerName + ": " + headerValue);
			if (headerName.equalsIgnoreCase(Constants.SET_COOKIE)) {				
				if (headerValue.startsWith(Constants.HEADER_SESSION_ID)) {
					ApplicationModel.getInstance(ctx).setSessionId(headerValue);
				}else if (headerValue.startsWith(Constants.HEADER_TOKEN)) {
					ApplicationModel.getInstance(ctx).setToken(headerValue);
				}
			}
		}
		Logger.debug(ApplicationModel.getInstance(ctx).toString());
	}

	public int getStatus() {
		return status;
	}

	public String getContent() {
		return content;
	}
}
