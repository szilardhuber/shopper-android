package com.shopper.android.util;

import com.shopper.android.Constants;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {

	private static SharedPreferences.Editor getStorageEditor(Context ctx) {
		SharedPreferences settings = getStorage(ctx);
		return settings.edit();
	}

	private static SharedPreferences getStorage(Context ctx) {
		return ctx.getSharedPreferences(Constants.PREFERENCES_NAME, 0);
	}

	public static String getProperty(String propertyName, Context ctx) {
		return getStorage(ctx).getString(propertyName, null);
	}

	public static void setProperty(String propertyName,String propertyValue, Context ctx) {
		SharedPreferences.Editor editor = getStorageEditor(ctx);
		editor.putString(propertyName, propertyValue);
	}
}
