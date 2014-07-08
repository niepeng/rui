package com.rui.android_client.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rui.android_client.model.AppCategory;
import com.rui.android_client.model.SubCategory;
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
		JSONArray jsonArray = JsonUtil.getJsonArray(json, "subList");
		ArrayList<SubCategory> subCategories = new ArrayList<SubCategory>();
		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				subCategories.add(SubCategoryParser.getInstance().parse(jsonObject));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		item.setSubCategories(subCategories);
		return item;
	}

}
