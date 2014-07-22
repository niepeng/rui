package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao;

import org.json.JSONObject;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common.JsonUtil;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.BaseAction;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Apr 17, 2014  8:15:55 PM</p>
 * <p>作者：聂鹏</p>
 */
public class BaseAO extends BaseAction {

	protected int default_version_code = 1;
	
	protected String default_version_url = "http://img.uxiang.com/ruiserver/files/bairui.apk";
	
	protected String parseDownloadUrl(String jsonValue) {
		JSONObject dataJson = JsonUtil.getJsonObject(jsonValue);
		if (dataJson != null) {
			return JsonUtil.getString(dataJson, "lastAndroidUrl", default_version_url);
		}
		return default_version_url;
	}
}

