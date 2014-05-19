package com.rui.android_client.component;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.ListView;

import com.rui.android_client.model.BannerInfo;
import com.rui.android_client.parse.AppInfoParser;
import com.rui.android_client.parse.BannerInfoParser;
import com.rui.android_client.utils.JsonUtil;
import com.rui.http.Response;

public class IndexListView extends AppListView {
	
	private ArrayList<BannerInfo> mBannerInfos = new ArrayList<BannerInfo>();

	public IndexListView(Activity activity, ListView listView) {
		super(activity, listView);
	}
	
	@Override
	protected void parseCallbackBody(Response response) {
		JSONObject json = JsonUtil.getJsonObject(response.getModel());
		JSONObject jsonData = JsonUtil.getJSONObject(json, "data");
		JSONArray bannerList = JsonUtil.getJsonArray(jsonData, "bannerList");
		if (bannerList != null) {
			for (int i = 0, size = bannerList.length(); i < size; i++) {
				try {
					JSONObject jsonObject = bannerList.getJSONObject(i);
					mBannerInfos.add(BannerInfoParser.getInstance().parse(jsonObject));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		JSONArray appList = JsonUtil.getJsonArray(jsonData, "appList");
		if (appList != null) {
			for (int i = 0, size = appList.length(); i < size; i++) {
				try {
					JSONObject jsonObject = appList.getJSONObject(i);
					mAppInfos.add(AppInfoParser.getInstance().parse(jsonObject));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
