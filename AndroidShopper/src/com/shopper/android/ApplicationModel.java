package com.shopper.android;

import com.shopper.android.util.LocalStorage;

import android.content.Context;

public class ApplicationModel {

	private static final ApplicationModel instance = new ApplicationModel();
	
	private Context context;
	private String sessionId;
	private String cookie;

	 
    private ApplicationModel() {
    	// singleton class
    }
 
    public static ApplicationModel getInstance(Context context) {
    	instance.context = context;
        return instance;
    }
    
    public void load(){
    	this.sessionId = LocalStorage.getProperty(Constants.PREFERENCE_SESSION_ID, context);
    	this.cookie = LocalStorage.getProperty(Constants.PREFERENCE_COOKIE, context);
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

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		if ((this.cookie == null && cookie != null) || !this.cookie.equals(cookie)) {
			LocalStorage.setProperty(Constants.PREFERENCE_COOKIE, cookie, context);
		}
		this.cookie = cookie;
	}
}
