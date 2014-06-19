package com.rui.android_client.component;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rui.android_client.R;
import com.rui.android_client.activity.RuiApp;
import com.rui.android_client.model.AppInfo;
import com.rui.android_client.model.BaseModel;
import com.rui.android_client.model.DownloadInfo;
import com.rui.android_client.utils.CollectionUtil;
import com.rui.android_client.utils.DownloadUtils;

public class AppInfoViewHolder extends ViewHolder {

	private Context mContext;
	private File mRootFile;

	public ImageView iconView;
	public TextView titleView;
	public Button openBtn;
	public Button updateBtn;
	public Button cancelBtn;
	public Button installBtn;
	public ProgressBar downloadProgress;

	private OnAppInfoDetailClickListener mOnAppInfoDetailClickListener;
	private OnUpdateAppInfoClickListener mOnUpdateAppInfoClickListener;
	private OnCancelUpdateClickListener mOnCancelUpdateClickListener;
	private OnInstallApkClickListener mOnInstallApkClickListener;

	public interface OnAppInfoDetailClickListener {
		public void onClick(View v);
	}

	public interface OnUpdateAppInfoClickListener {
		public void onClick(View v);
	}

	public interface OnCancelUpdateClickListener {
		public void onClick(View v);
	}

	public interface OnInstallApkClickListener {
		public void onClick(View v);
	}

	public void setOnAppInfoDetailClickListener(OnAppInfoDetailClickListener l) {
		mOnAppInfoDetailClickListener = l;
	}

	public void setOnUpdateAppInfoClickListener(OnUpdateAppInfoClickListener l) {
		mOnUpdateAppInfoClickListener = l;
	}

	public void setOnCancelUpdateClickListener(OnCancelUpdateClickListener l) {
		mOnCancelUpdateClickListener = l;
	}

	public void setOnInstallApkClickListener(OnInstallApkClickListener l) {
		mOnInstallApkClickListener = l;
	}

	public AppInfoViewHolder(Context context) {
		super(context);
		mContext = context;
		mRootFile = DownloadUtils.createDirIfNotExists();
		View.inflate(context, R.layout.layout_app_listview_item, this);
		iconView = (ImageView) findViewById(R.id.icon);
		titleView = (TextView) findViewById(R.id.title);
		openBtn = (Button) findViewById(R.id.open_btn);
		updateBtn = (Button) findViewById(R.id.update_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		installBtn = (Button) findViewById(R.id.install_btn);
		downloadProgress = (ProgressBar) findViewById(R.id.download_progress);

		openBtn.setOnClickListener(onOpenClick);
		updateBtn.setOnClickListener(onUpdateClick);
		cancelBtn.setOnClickListener(onCancelClick);
		installBtn.setOnClickListener(onInstallClick);
	}

	@Override
	public void setViewContent(int position, BaseModel item) {
		AppInfo appInfo = (AppInfo) item;
		// 异步加载图片
		if (appInfo.getIcon() != null) {
			iconView.setImageDrawable(appInfo.getIcon());
		} else {
			RuiApp.fb.display(iconView, appInfo.getIconUrl());
		}
		titleView.setText(appInfo.getMainTitle());

		openBtn.setTag(appInfo);
		updateBtn.setTag(appInfo);
		cancelBtn.setTag(appInfo);
		installBtn.setTag(appInfo);

		openBtn.setVisibility(View.GONE);
		updateBtn.setVisibility(View.GONE);
		cancelBtn.setVisibility(View.GONE);
		installBtn.setVisibility(View.GONE);

		if (CollectionUtil.isEmpty(appInfo.getDownloadInfos())) {
			if (appInfo.isNeedUpdate()) {
				updateBtn.setVisibility(View.VISIBLE);
			} else {
				openBtn.setVisibility(View.VISIBLE);
			}
			downloadProgress.setVisibility(View.GONE);
		} else {
			int fileSize = 0;
			int completedSize = 0;
			for (DownloadInfo info : appInfo.getDownloadInfos()) {
				completedSize += info.getCompeleteSize();
				fileSize += info.getEndPos() - info.getStartPos();
			}
			if (DownloadUtils.isReadyInstalled(appInfo.getPackageName(),
					fileSize, completedSize)) {
				installBtn.setVisibility(View.VISIBLE);
			} else if (RuiApp.downloaders.get(appInfo.getDownUrl()) != null
					&& DownloadUtils.isDownloading(fileSize, completedSize)) {
				cancelBtn.setVisibility(View.VISIBLE);
			} else {
				fileSize = 0;
				completedSize = 0;
				if (appInfo.isNeedUpdate()) {
					updateBtn.setVisibility(View.VISIBLE);
				} else {
					openBtn.setVisibility(View.VISIBLE);
				}
			}
			downloadProgress.setVisibility(View.VISIBLE);
			downloadProgress.setMax(fileSize);
			downloadProgress.setProgress(completedSize);
		}
	}

	private View.OnClickListener onOpenClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mOnAppInfoDetailClickListener != null) {
				mOnAppInfoDetailClickListener.onClick(v);
				return;
			}
			AppInfo appInfo = (AppInfo) v.getTag();
			PackageManager pm = mContext.getPackageManager();
			Intent appStartIntent = pm.getLaunchIntentForPackage(appInfo
					.getPackageName());
			if (null != appStartIntent) {
				mContext.startActivity(appStartIntent);
			}
		}
	};

	private View.OnClickListener onUpdateClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mOnUpdateAppInfoClickListener != null) {
				mOnUpdateAppInfoClickListener.onClick(v);
				return;
			}
			startDownload(v);
		}

	};

	/**
	 * 83 * 响应开始下载按钮的点击事件 84
	 */
	private void startDownload(View v) {
		AppInfo appInfo = (AppInfo) v.getTag();
		String downloadUrl = appInfo.getDownUrl();
		String fileName = mRootFile.getAbsolutePath() + File.separator
				+ appInfo.getPackageName() + ".apk";

		Intent intent = new Intent();
		intent.setClass(mContext,
				com.rui.android_client.service.DownloadService.class);
		intent.putExtra("packageName", appInfo.getPackageName());
		intent.putExtra("fileName", fileName);
		intent.putExtra("downloadUrl", downloadUrl);
		intent.putExtra("flag", "start_download");// 标志着数据从localdownactivi
		mContext.startService(intent);// 这里启动service

		showProgressBar(v, downloadUrl);
	}

	private void showProgressBar(View v, String downloadUrl) {
		View root = (View) v.getParent().getParent();
		ProgressBar bar = (ProgressBar) root
				.findViewById(R.id.download_progress);
		v.setVisibility(View.GONE);
		Button cancelBtn = (Button) root.findViewById(R.id.cancel_btn);
		cancelBtn.setVisibility(View.VISIBLE);
		bar.setVisibility(View.VISIBLE);
	}

	private View.OnClickListener onCancelClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mOnCancelUpdateClickListener != null) {
				mOnCancelUpdateClickListener.onClick(v);
				return;
			}
			AppInfo appInfo = (AppInfo) v.getTag();
			Intent intent = new Intent();
			intent.setClass(mContext,
					com.rui.android_client.service.DownloadService.class);
			intent.putExtra("downloadUrl", appInfo.getDownUrl());
			intent.putExtra("flag", "cancel");// 标志着数据从localdownactivi
			mContext.startService(intent);// 这里启动service
		}
	};

	private View.OnClickListener onInstallClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mOnInstallApkClickListener != null) {
				mOnInstallApkClickListener.onClick(v);
				return;
			}
			AppInfo appInfo = (AppInfo) v.getTag();
			String path = mRootFile.getAbsolutePath() + File.separator
					+ appInfo.getPackageName() + ".apk";
			File installFile = new File(path);
			if (!installFile.exists()) {
				return;
			}
			String type = "application/vnd.android.package-archive";
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(installFile), type);
			mContext.startActivity(intent);
		}
	};

}