package com.shopzenion.android.server;

public interface ServerResponseCallback {
	void gotResponse(ServerResponse response);
	void cancelled();
	void offline();
}
