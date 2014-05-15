package com.rui.android_client.utils;

import com.rui.http.Response;

public interface ThreadListener {
	
	void onPostExecute(Response response);
}