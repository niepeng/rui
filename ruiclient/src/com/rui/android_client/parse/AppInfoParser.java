package com.rui.android_client.parse;

import org.json.JSONObject;

import com.rui.android_client.activity.RuiApp;
import com.rui.android_client.model.AppInfo;
import com.rui.android_client.utils.JsonUtil;

public class AppInfoParser {

	private static AppInfoParser mParser;

	public static AppInfoParser getInstance() {
		if (mParser == null) {
			mParser = new AppInfoParser();
		}
		return mParser;
	}

	public AppInfo parse(JSONObject json) {
		AppInfo app = new AppInfo();
		app.setId(JsonUtil.getLong(json, "id", 0));
		app.setMainTitle(JsonUtil.getString(json, "mainTitle", null));
		app.setIconUrl(JsonUtil.getString(json, "iconUrl", null));
		app.setDownUrl(JsonUtil.getString(json, "downUrl", null));
		app.setVersionValue(JsonUtil.getString(json, "versionValue", null));
		app.setVersionName(JsonUtil.getString(json, "versionName", null));
		app.setInfo(JsonUtil.getString(json, "info", null));
		app.setPackageName(JsonUtil.getString(json, "packageName", null));
		app.setFileSize(JsonUtil.getInt(json, "fileSize", 0));
		app.setFirstCatId(JsonUtil.getLong(json, "firstCatId", 0));
		app.setUpdateInfo(JsonUtil.getString(json, "updateInfo", null));

		RuiApp ruiApp = (RuiApp) RuiApp.context.getApplicationContext();
		if (ruiApp.isAppInstalled(app.getPackageName())) {
			app.setInstalled(true);
			AppInfo installedApp = ruiApp.getInstalledAppInfo(app
					.getPackageName());
			if (installedApp.getLocalVersionCode() < Long.parseLong(app
					.getVersionValue())) {
				app.setNeedUpdate(true);
			}
		}

		return app;
	}

}
