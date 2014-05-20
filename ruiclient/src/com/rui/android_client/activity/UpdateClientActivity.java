package com.rui.android_client.activity;

import java.io.File;
import java.util.Date;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rui.android_client.R;
import com.rui.android_client.utils.DateUtil;
import com.rui.http.ProgressCallback;

/**
 * @author lsb
 *
 * @date 2012-5-30 上午10:33:21
 */
public class UpdateClientActivity extends BaseActivity {

	private ProgressBar progressBar;

	private Handler handler;

	private String lastAndroidClientUrl;

	private TextView downloadApkGrogress;

	private File installFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_update_client);
		handler = new Handler();
		lastAndroidClientUrl = this.getIntent().getExtras().getString("lastAndroidClientUrl");
		progressBar = (ProgressBar) this.findViewById(R.id.update_client_download_progress_bar);
		downloadApkGrogress = (TextView) this.findViewById(R.id.download_apk_grogress);
		startDownLoad();
	}

	private void startDownLoad() {
		try {
//			installFile = File.createTempFile(genName(), ".apk");
			installFile = new File(Environment.getExternalStorageDirectory(), genName());
			
		} catch (Exception e) {
			Log.e("update", e.getMessage(), e);
			this.alert(getString(R.string.update_client_sd_no_space_msg));
			return;
		}
		DownLoader downLoader = new DownLoader(lastAndroidClientUrl, installFile.getAbsolutePath(), new UpdateDownLoadCallback());
		downLoader.asyDownload((RuiApp) this.getApplication());

	}

	private void startInstall() {
		String type = "application/vnd.android.package-archive";
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(installFile), type);
		startActivity(intent);
		endMe();
	}

	private void endMe() {
		try {
			Log.d("update", "finishAllActivities!!");
			((RuiApp)this.getApplication()).finishAllActivities();
			Log.d("update", "done!!");
		} catch (Throwable e) {
			Log.e("update", e.getMessage(), e);
		}
	}

	private class UpdateDownLoadCallback implements ProgressCallback {

		private int max;

		@Override
		public void onFinish() {
			startInstall();
		}

		@Override
		public void onSetMaxSize(int maxSize) {
			int targetMax = maxSize;
			if (maxSize < 0) {
				targetMax = 100;
			}
			this.max = targetMax;
			final int temp = targetMax;
			handler.post(new Runnable() {

				@Override
				public void run() {
					progressBar.setMax(temp);
					downloadApkGrogress.setText("0%");
				}
			});
		}

		@Override
		public void onProgress(final int downloadSize) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					progressBar.setProgress(downloadSize);
					if (max > 0) {
						int percent = (int) downloadSize * 100 / max;
						downloadApkGrogress.setText(percent + "%");
					}
				}
			});
		}

		@Override
		public void onException(Exception e) {
			alert(getString(R.string.app_label_conect_network_fail));
		}

	}
	
	private static String genName() {
		return "rui_install_file_" + DateUtil.format(new Date(), "yyyy_MM_dd_HH_mm") + ".apk";
	}

}

