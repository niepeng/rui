package com.rui.android_client.utils;

import java.io.File;
import java.util.List;

import android.os.Environment;

import com.rui.android_client.activity.RuiApp;
import com.rui.android_client.model.DownloadInfo;

public class DownloadUtils {
	
	public static final String DOWNLOAD_DIR_NAME = "rui";
	
	public static boolean isReadyInstalled(String downloadUrl, String packageName) {
		List<DownloadInfo> infos = RuiApp.mPersist.downloadInfoDao.getInfos(downloadUrl);
		if (CollectionUtil.isEmpty(infos)) {
			return false;
		}
		return isReadyInstalled(packageName, infos);
	}

	public static boolean isReadyInstalled(String packageName,
			List<DownloadInfo> infos) {
		int fileSize = 0;
		int completedSize = 0;
		for (DownloadInfo item : infos) {
			fileSize += item.getEndPos() - item.getStartPos();
			completedSize += item.getCompeleteSize();
		}
		return isReadyInstalled(packageName, fileSize, completedSize);
	}

	public static boolean isReadyInstalled(String packageName, int fileSize,
			int completedSize) {
		if (fileSize > 0 && fileSize == completedSize) {
			return isApkExist(packageName);
		}
		return false;
	}
	

	public static boolean isDownloading(List<DownloadInfo> infos) {
		if (CollectionUtil.isEmpty(infos)) {
			return false;
		}
		int fileSize = 0;
		int completedSize = 0;
		for (DownloadInfo item : infos) {
			fileSize += item.getEndPos() - item.getStartPos();
			completedSize += item.getCompeleteSize();
		}
		return isDownloading(fileSize, completedSize);
	}

	public static boolean isDownloading(int fileSize, int completedSize) {
		if (fileSize > 0 && fileSize > completedSize) {
			return true;
		}
		return false;
	}

	public static boolean isApkExist(String packageName) {
		File root = new File(Environment.getExternalStorageDirectory(), DOWNLOAD_DIR_NAME);
		if (!root.exists()) {
			return false;
		}
		File file = new File(root.getAbsoluteFile() + File.separator + packageName + ".apk");
		if (file.exists()) {
			return true;
		}
		return false;
	}
	
	public static File createDirIfNotExists() {
		boolean success = true;

		File file = new File(Environment.getExternalStorageDirectory(), DOWNLOAD_DIR_NAME);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				success = false;
			}
		}
		if (success) {
			return file;
		}
		return null;
	}
	
}
