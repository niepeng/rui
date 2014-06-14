package com.rui.android_client.service;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.rui.android_client.activity.RuiApp;
import com.rui.android_client.utils.StringUtil;

public class BootService extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
			installOrUpdateComplete(context, intent);
		} else if (Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
			installOrUpdateComplete(context, intent);
		}
	}

	private void installOrUpdateComplete(Context context, Intent intent) {
		String packageName = intent.getData().getSchemeSpecificPart();
		if (StringUtil.isNotBlank(packageName)) {
			RuiApp.mPersist.downloadInfoDao.deleteByPackageName(packageName);

			File root = new File(Environment.getExternalStorageDirectory(),
					"rui");
			if (root.exists()) {
				File file = new File(root.getAbsoluteFile() + File.separator
						+ packageName + ".apk");
				if (file.exists()) {
					file.delete();
				}
			}
			
			updateDownloadView(context, packageName);
			
		}
	}
	
	private void updateDownloadView(Context context, String packageName) {
		// 发送广播更新
		Intent intent = new Intent(DownloadService.ACTION_UPDATE_PROGRESS);
		intent.putExtra("packageName", packageName);
		context.sendBroadcast(intent);
	}
}
