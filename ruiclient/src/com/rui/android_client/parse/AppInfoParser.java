package com.rui.android_client.parse;

import org.json.JSONObject;

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
		return app;
	}

}
