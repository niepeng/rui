package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.json;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.JsonAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.BaseAction;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 12, 2014  9:19:45 PM</p>
 * <p>作者：聂鹏</p>
 */
public class Cat extends BaseAction {
	
	private JsonAO jsonAO;
	
	public void catList(FlowData flowData, Context context) {
		Result result = jsonAO.catList();
		handleJsonResult(result, flowData, context);
	}

	public void setJsonAO(JsonAO jsonAO) {
		this.jsonAO = jsonAO;
	}
	
}

