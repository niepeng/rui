package com.rui.android_client.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rui.android_client.R;
import com.rui.android_client.component.LinePageIndicator;
import com.rui.android_client.component.TextViewWithLabelLayout;
import com.rui.android_client.model.AppInfo;
import com.rui.android_client.model.DownloadInfo;
import com.rui.android_client.parse.AppInfoParser;
import com.rui.android_client.utils.CollectionUtil;
import com.rui.android_client.utils.DownloadUtils;
import com.rui.android_client.utils.JsonUtil;
import com.rui.android_client.utils.NumberUtils;
import com.rui.android_client.utils.StringUtil;
import com.rui.android_client.utils.ThreadAid;
import com.rui.android_client.utils.ThreadListener;
import com.rui.http.Config;
import com.rui.http.RemoteManager;
import com.rui.http.Request;
import com.rui.http.Response;

public class AppDetailActivity extends BaseActivity {

	private File mRootFile;

	private long appId;

	private AppInfo mAppInfo;

	private ImageView iconView;
	private TextView mainTitleView, subTitleView, detailInfoView;
	private TextViewWithLabelLayout fileSizeView;

	private Button downloadBtn;
	private Button updateBtn;
	private Button cancelBtn;
	private Button installBtn;
	private ProgressBar downloadProgress;

	private ArrayList<View> bannerImageViewList = new ArrayList<View>();
	private BannerPagerAdapter mBannerPageAdapter;
	private ViewPager mBannerViewPager;
	private LinePageIndicator mBannerViewIndicator;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_detail);
		init();
	}

	private void init() {
		initView();
		initData();
		initListener();
	}

	private void initView() {
		initActionBar();
		iconView = (ImageView) findViewById(R.id.icon);
		mainTitleView = (TextView) findViewById(R.id.main_title);
		subTitleView = (TextView) findViewById(R.id.sub_title);
		fileSizeView = (TextViewWithLabelLayout) findViewById(R.id.file_size);
		detailInfoView = (TextView) findViewById(R.id.detail_info);
		downloadBtn = (Button) findViewById(R.id.download_btn);
		updateBtn = (Button) findViewById(R.id.update_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		installBtn = (Button) findViewById(R.id.install_btn);
		downloadProgress = (ProgressBar) findViewById(R.id.download_progress);

		mBannerPageAdapter = new BannerPagerAdapter();
		mBannerViewPager = (ViewPager) findViewById(R.id.banner_viewpage);
		mBannerViewPager.setAdapter(mBannerPageAdapter);
		mBannerViewIndicator = (LinePageIndicator) findViewById(R.id.indicator);
		mBannerViewIndicator.setViewPager(mBannerViewPager);
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(R.string.detail);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
	}

	private void initData() {
		mRootFile = DownloadUtils.createDirIfNotExists();
		appId = getIntent().getLongExtra("ID", 0);
		RemoteManager remoteManager = RemoteManager.getPostOnceRemoteManager();
		Request request = remoteManager.createPostRequest(Config.getConfig()
				.getProperty(Config.Names.APP_DETAIL_URL));
		request.addParameter("id", appId);
		mApp.asyInvoke(new ThreadAid(new GetAppInfoCallbackListener(), request));
	}

	private void initViewContent() {
		if (mAppInfo == null) {
			return;
		}
		RuiApp.fb.display(iconView, mAppInfo.getIconUrl());
		mainTitleView.setText(mAppInfo.getMainTitle());

		fileSizeView
				.setContentText(NumberUtils.setScale(mAppInfo.getFileSize() / 1024f)
						+ "MB");

		setBannerViewContent();

		updateBtn.setTag(mAppInfo);
		cancelBtn.setTag(mAppInfo);
		installBtn.setTag(mAppInfo);

		updateBtn.setVisibility(View.GONE);
		cancelBtn.setVisibility(View.GONE);
		installBtn.setVisibility(View.GONE);

		if (CollectionUtil.isEmpty(mAppInfo.getDownloadInfos())) {
			if (mAppInfo.isNeedUpdate()) {
				updateBtn.setVisibility(View.VISIBLE);
			} else {
				downloadBtn.setVisibility(View.VISIBLE);
			}
			downloadProgress.setVisibility(View.GONE);
		} else {
			int fileSize = 0;
			int completedSize = 0;
			for (DownloadInfo info : mAppInfo.getDownloadInfos()) {
				completedSize += info.getCompeleteSize();
				fileSize += info.getEndPos() - info.getStartPos();
			}
			if (DownloadUtils.isReadyInstalled(mAppInfo.getPackageName(),
					fileSize, completedSize)) {
				installBtn.setVisibility(View.VISIBLE);
			} else if (RuiApp.downloaders.get(mAppInfo.getDownUrl()) != null
					&& DownloadUtils.isDownloading(fileSize, completedSize)) {
				cancelBtn.setVisibility(View.VISIBLE);
			} else {
				fileSize = 0;
				completedSize = 0;
				if (mAppInfo.isNeedUpdate()) {
					updateBtn.setVisibility(View.VISIBLE);
				} else {
					downloadBtn.setVisibility(View.VISIBLE);
				}
			}
			downloadProgress.setVisibility(View.VISIBLE);
			downloadProgress.setMax(fileSize);
			downloadProgress.setProgress(completedSize);
		}
	}

	public void setBannerViewContent() {
		if (mAppInfo == null || mAppInfo.getScreenshots() == null) {
			return;
		}
		bannerImageViewList.clear();
		for (String item : mAppInfo.getScreenshots()) {
			ImageView view = new ImageView(this);
			view.setScaleType(ScaleType.FIT_XY);
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			RuiApp.fb.display(view, item);
			bannerImageViewList.add(view);
		}
		mBannerPageAdapter.notifyDataSetChanged();
	}

	private class GetAppInfoCallbackListener implements ThreadListener {

		@Override
		public void onPostExecute(Response response) {
			if (response != null && response.isSuccess()) {
				Object result = response.getModel();
				JSONObject jsonRoot = JsonUtil.getJsonObject(result);
				JSONObject jsonAppDetail = JsonUtil.getJSONObject(
						JsonUtil.getJSONObject(jsonRoot, "data"), "appDetail");
				if (jsonAppDetail != null) {
					mAppInfo = AppInfoParser.getInstance().parse(jsonAppDetail);
					if (mAppInfo != null && StringUtil.isNotBlank(mAppInfo.getDownUrl())) {
						List<DownloadInfo> downloadInfos = RuiApp.mPersist.downloadInfoDao
								.getInfos(mAppInfo.getDownUrl());
						mAppInfo.setDownloadInfos(downloadInfos);
					}
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							initViewContent();
						}
					});
				}
			} else {
				// TODO
			}
		}

	}

	class BannerPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (bannerImageViewList == null) {
				return 0;
			}
			return bannerImageViewList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (position < getCount()) {
				container.removeView(bannerImageViewList.get(position));
			}
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(bannerImageViewList.get(position));
			return bannerImageViewList.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	private void initListener() {
		updateBtn.setOnClickListener(onUpdateClick);
		cancelBtn.setOnClickListener(onCancelClick);
		installBtn.setOnClickListener(onInstallClick);
	}

	private View.OnClickListener onUpdateClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
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
		intent.setClass(this,
				com.rui.android_client.service.DownloadService.class);
		intent.putExtra("packageName", appInfo.getPackageName());
		intent.putExtra("fileName", fileName);
		intent.putExtra("downloadUrl", downloadUrl);
		intent.putExtra("flag", "start_download");// 标志着数据从localdownactivi
		startService(intent);// 这里启动service

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
			AppInfo appInfo = (AppInfo) v.getTag();
			Intent intent = new Intent();
			intent.setClass(AppDetailActivity.this,
					com.rui.android_client.service.DownloadService.class);
			intent.putExtra("downloadUrl", appInfo.getDownUrl());
			intent.putExtra("flag", "cancel");// 标志着数据从localdownactivi
			startService(intent);// 这里启动service
		}
	};

	private View.OnClickListener onInstallClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
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
			startActivity(intent);
		}
	};

}
