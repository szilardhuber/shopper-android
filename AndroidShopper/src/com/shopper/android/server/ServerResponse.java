package com.shopper.android.server;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

public class ServerResponse {
	
	private int status;
	private Header[] headers;
	
	public ServerResponse(int status, Header[] headers2) {
		super();
		this.status = status;
		this.headers = headers2;
	}
	public int getStatus() {
		return status;
	}
	public Header[] getHeaders() {
		return headers;
	}

	public boolean isOk() {
		return getStatus() == HttpStatus.SC_OK;
	}
}
