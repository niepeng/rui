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
//		app.setIconUrl(JsonUtil.getString(jsonObject, "iconUrl", null));
		// TODO test
		app.setIconUrl("http://192.168.1.101:8080/static/uploadinfos/1399785753855.png");
		app.setDownUrl(JsonUtil.getString(json, "downUrl", null));
		return app;
	}

}
