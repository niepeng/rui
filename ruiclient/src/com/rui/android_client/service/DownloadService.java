package com.rui.android_client.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.rui.android_client.download.Downloader;
import com.rui.android_client.download.LoadInfo;

public class DownloadService extends Service {

	// 存放各个下载器
	private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		String flag = intent.getStringExtra("flag");
		if ("setState".equals(flag)) {
			startDownload(intent);
		} else {
			
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void startDownload(Intent intent) {
		final String downloadUrl = intent.getStringExtra("downloadUrl");
		final String fileName = intent.getStringExtra("fileName");
		
		new StartDownload().execute(downloadUrl, fileName);
	}
	
	private class StartDownload extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			String downloadUrl = params[0];
			String fileName = params[1];
			int threadcount = 1;
			// 初始化一个downloader下载器
			Downloader downloader = downloaders.get(downloadUrl);
			if (downloader == null) {
				downloader = new Downloader(downloadUrl, fileName, threadcount,
						DownloadService.this, mHandler);
				downloaders.put(downloadUrl, downloader);
			}
			if (downloader.isDownloading())
				return null;
			LoadInfo loadInfo = downloader.getDownloaderInfors();
			// 调用方法开始下载
			downloader.download();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String url = (String) msg.obj;
				int length = msg.arg1;

				// TODO

				// 发送广播更新
			}
		}
	};

}
