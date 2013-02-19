package com.shopper.android;

import android.content.Context;

import com.shopper.android.server.ServerRequest;
import com.shopper.android.server.ServerResponse;

public class SecurityHandler {
	public static final int OK = 200;
	public static final int FAIL = 400;	
	public static final int LOGIN_REQUIRED = 401;
	public static final int UNVERIFIED = 403;
	public static final int IO_ERROR = 1;
	public static final int INVALID_EMAIL = 2;
	public static final int INVALID_PASSWORD = 3;
	public static final int EMPTY_EMAIL = 4;
	public static final int EMPTY_PASSWORD = 5;
	
	private SecurityHandler() {
		// don't instance me
	}

	public static int login(User user, Context ctx) {
		ServerResponse response = ServerRequest.loginRequest(user.getEmail(), user.getPassword(), ctx);
		if (response == null) {
			return IO_ERROR;
		}
		return response.getStatus();
	}
	
	public static int register(User user, Context ctx) {
		ServerResponse response = ServerRequest.registerRequest(user.getEmail(), user.getPassword(), ctx);
		if (response == null) {
			return IO_ERROR;
		}
		return response.getStatus();
	}
	
	public static int validateUser(User user){
		if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
			return EMPTY_EMAIL;
		} else if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
			return EMPTY_PASSWORD;
		} else  if (!user.isValidEmail()) {
			return INVALID_EMAIL;			
		} else if ( !user.isValidPassword()){
			return INVALID_PASSWORD;
		}
		return -1;
	}
}
