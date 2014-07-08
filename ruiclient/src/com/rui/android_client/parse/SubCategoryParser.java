package com.rui.android_client.parse;

import org.json.JSONObject;

import com.rui.android_client.model.SubCategory;
import com.rui.android_client.utils.JsonUtil;


public class SubCategoryParser {
	
	private static SubCategoryParser mParser;

	public static SubCategoryParser getInstance() {
		if (mParser == null) {
			mParser = new SubCategoryParser();
		}
		return mParser;
	}
	
	public SubCategory parse(JSONObject json) {
		SubCategory item = new SubCategory();
		item.setId(JsonUtil.getLong(json, "id", -1));
		item.setName(JsonUtil.getString(json, "name", null));
		item.setIconUrl(JsonUtil.getString(json, "iconUrl", null));
		return item;
	}

}
