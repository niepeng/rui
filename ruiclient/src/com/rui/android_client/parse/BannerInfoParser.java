package com.rui.android_client.parse;

import org.json.JSONObject;

import com.rui.android_client.model.BannerInfo;
import com.rui.android_client.utils.JsonUtil;


public class BannerInfoParser {
	
	private static BannerInfoParser mParser;

	public static BannerInfoParser getInstance() {
		if (mParser == null) {
			mParser = new BannerInfoParser();
		}
		return mParser;
	}
	
	public BannerInfo parse(JSONObject json) {
		BannerInfo app = new BannerInfo();
		app.setId(JsonUtil.getLong(json, "id", 0));
		app.setName(JsonUtil.getString(json, "name", null));
		app.setImageUrl(JsonUtil.getString(json, "imageUrl", null));
		return app;
	}

}
