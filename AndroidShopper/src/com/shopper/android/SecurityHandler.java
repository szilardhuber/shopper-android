package com.shopper.android;

import com.shopper.android.server.ServerRequest;
import com.shopper.android.server.ServerResponse;

public class SecurityHandler {
	public static final int OK = 0;
	public static final int FAIL = 1;
	public static final int INVALID_EMAIL = 2;
	public static final int INVALID_PASSWORD = 3;
	
	private SecurityHandler() {
		// don't instance me
	}

	public static int login(User user) {
		// do we need to validate email and pass?
		ServerResponse response = ServerRequest.loginRequest(user.getEmail(), user.getPassword());
		if (response != null && response.isOk()) {
			return OK;
		}
		return FAIL;
	}
	
	public static int register(User user) {
		int ret = validateUser(user); // perhaps we don't need this, if we validate only on server side
		if (ret < 0) {
			ServerResponse response = ServerRequest.loginRequest(user.getEmail(), user.getPassword());
			if(response != null && response.isOk()){
				return OK;
			}
			return FAIL;
		}
		return ret;
	}
	
	private static int validateUser(User user){
		if (!user.isValidEmail()) {
			return INVALID_EMAIL;			
		} else if ( user.isValidPassword()){
			return INVALID_PASSWORD;
		}
		return -1;
	}
}
