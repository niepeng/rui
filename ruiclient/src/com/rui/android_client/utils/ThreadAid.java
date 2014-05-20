package com.rui.android_client.utils;

import java.util.concurrent.Callable;

import android.util.Log;

import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class ThreadAid implements Callable<Response> {
	
	private ThreadListener threadListener;

	private Request request;
	
	
	public ThreadAid(ThreadListener threadListener, Request request) {
		super();
		this.threadListener = threadListener;
		this.request = request;
	}

	@Override
	public Response call() throws Exception {
		Response response = null;
		try {
			RemoteManager remoteManager = RemoteManager.getFullFeatureRemoteManager();
			response = remoteManager.execute(request);
			return response;
		} catch (Exception e) {
			Log.e("ThreadAid", e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			threadListener.onPostExecute(response);
		}
	}


}
