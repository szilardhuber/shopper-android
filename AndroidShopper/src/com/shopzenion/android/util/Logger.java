package com.shopzenion.android.util;

import android.util.Log;

public class Logger {
	private Logger() {
		// Utility class should not have a public or default constructor.
	}

	/**
	 * Logging tag.
	 */
	public static final String LOG_TAG = "AndroidShopzenion";

	public static void debug(String msg) {
		Log.d(LOG_TAG, msg);
	}

	public static void error(String msg) {
		Log.e(LOG_TAG, msg);
	}

	public static void info(String msg) {
		Log.i(LOG_TAG, msg);
	}

	public static void warn(String msg) {
		Log.w(LOG_TAG, msg);
	}
}
