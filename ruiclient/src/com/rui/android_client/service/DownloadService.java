package com.rui.android_client.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.rui.android_client.activity.RuiApp;
import com.rui.android_client.download.Downloader;

public class DownloadService extends Service {
	
	public static final String ACTION_UPDATE_PROGRESS = "com.rui.android_client.service.DownloadService.UPDATE_PROGRESS";

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
		String flag = intent.getStringExtra("flag");
		if ("start_download".equals(flag)) {
			startDownload(intent);
		} else if ("cancel".equals(flag)) {
			String downloadUrl = intent.getStringExtra("downloadUrl");
			Downloader downloader = RuiApp.downloaders.get(downloadUrl);
			if (downloader != null) {
				downloader.delete(downloadUrl);
				RuiApp.downloaders.remove(downloader);
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void startDownload(Intent intent) {
		final String packageName = intent.getStringExtra("packageName");
		final String downloadUrl = intent.getStringExtra("downloadUrl");
		final String fileName = intent.getStringExtra("fileName");
		
		new StartDownload().execute(packageName, downloadUrl, fileName);
	}
	
	private class StartDownload extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			String packageName = params[0];
			String downloadUrl = params[1];
			String fileName = params[2];
			int threadcount = 1;
			// 初始化一个downloader下载器
			Downloader downloader = RuiApp.downloaders.get(downloadUrl);
			if (downloader == null) {
				downloader = new Downloader(packageName, downloadUrl, fileName, threadcount,
						DownloadService.this, mHandler);
				RuiApp.downloaders.put(downloadUrl, downloader);
			}
			if (downloader.isDownloading())
				return null;
			downloader.getDownloaderInfors();
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
				int fileSize = msg.arg1;
				int completedSize = msg.arg2;
				
				Downloader downloader = RuiApp.downloaders.get(url);
				if (fileSize == completedSize) {
					downloader.reset();
					RuiApp.downloaders.remove(downloader);
				}
				
				// 发送广播更新
				Intent intent = new Intent(ACTION_UPDATE_PROGRESS);
				intent.putExtra("downloadUrl", url);
				intent.putExtra("fileSize", fileSize);
				intent.putExtra("completedSize", completedSize);
				sendBroadcast(intent);
			}
		}
	};

}
