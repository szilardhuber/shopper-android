package com.shopzenion.android.model;

import com.shopzenion.android.Constants;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {

	private LocalStorage(){
		// utility class
	}
	
	private static SharedPreferences.Editor getStorageEditor(Context ctx) {
		SharedPreferences settings = getStorage(ctx);
		return settings.edit();
	}

	private static SharedPreferences getStorage(Context ctx) {
		return ctx.getSharedPreferences(Constants.PREFERENCES_NAME, 0);
	}

	public static String getProperty(String propertyName, Context ctx, String defaultValue) {
		return getStorage(ctx).getString(propertyName, defaultValue);
	}
	
	public static String getProperty(String propertyName, Context ctx) {
		return getProperty(propertyName, ctx, null);
	}

	public static void setProperty(String propertyName,String propertyValue, Context ctx) {
		SharedPreferences.Editor editor = getStorageEditor(ctx);
		editor.putString(propertyName, propertyValue);
		editor.commit();
	}
}
