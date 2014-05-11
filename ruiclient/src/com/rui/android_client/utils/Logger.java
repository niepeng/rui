package com.rui.android_client.utils;

import android.util.Log;

public class Logger {

	private static boolean ENABLE_LOG = true;

	private final static String LOG_TAG = "DOITIM";

	public static void d(final Object logMe) {
		if (ENABLE_LOG) {
			Log.d(LOG_TAG, "" + logMe);
		}
	}

	public static void e(final Object logMe) {
		Log.e(LOG_TAG, "" + logMe);
	}

}
