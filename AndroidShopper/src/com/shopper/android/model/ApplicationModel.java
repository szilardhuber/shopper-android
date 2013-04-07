package com.shopper.android.model;

import com.shopper.android.Constants;
import com.shopper.android.util.LocalStorage;

import android.content.Context;

public class ApplicationModel {

	private static final ApplicationModel instance = new ApplicationModel();
	
	private Context context;
	private String sessionId;
	private String token;

	 
    private ApplicationModel() {
    	// singleton class
    }
 
    public static ApplicationModel getInstance(Context context) {
    	instance.context = context;
        return instance;
    }
    
    public void load(){
    	this.sessionId = LocalStorage.getProperty(Constants.PREFERENCE_SESSION_ID, context);
    	this.token = LocalStorage.getProperty(Constants.PREFERENCE_COOKIE, context);
    }

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		if ((this.sessionId == null && sessionId != null) || !this.sessionId.equals(sessionId)) {
			LocalStorage.setProperty(Constants.PREFERENCE_SESSION_ID, sessionId, context);
		}
		this.sessionId = sessionId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String cookie) {
		if ((this.token == null && cookie != null) || !this.token.equals(cookie)) {
			LocalStorage.setProperty(Constants.PREFERENCE_COOKIE, cookie, context);
		}
		this.token = cookie;
	}

	public boolean isLogedIn() {
		return getSessionId() != null;
	}

	@Override
	public String toString() {
		return "ApplicationModel [sessionId=" + sessionId + ", cookie="
				+ token + "]";
	}
}
