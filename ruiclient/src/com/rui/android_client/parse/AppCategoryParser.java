package com.rui.android_client.parse;

import org.json.JSONObject;

import com.rui.android_client.model.AppCategory;
import com.rui.android_client.utils.JsonUtil;


public class AppCategoryParser {
	
	private static AppCategoryParser mParser;

	public static AppCategoryParser getInstance() {
		if (mParser == null) {
			mParser = new AppCategoryParser();
		}
		return mParser;
	}
	
	public AppCategory parse(JSONObject json) {
		AppCategory item = new AppCategory();
		item.setId(JsonUtil.getLong(json, "id", -1));
		item.setName(JsonUtil.getString(json, "name", null));
		item.setIconUrl(JsonUtil.getString(json, "iconUrl", null));
		return item;
	}

}
