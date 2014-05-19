package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.json;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.JsonAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.APPStsutsEnum;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.AppQuery;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.BaseAction;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 12, 2014  9:52:26 PM</p>
 * <p>作者：聂鹏</p>
 */
public class App extends BaseAction {
	
	private JsonAO jsonAO;
	
	public void appList(FlowData flowData, Context context) {
		long catId = flowData.getParameters().getLong("catId");
		int page = flowData.getParameters().getInt("page");

		AppQuery query = new AppQuery();
		query.setStatus(APPStsutsEnum.ONLINE.getValue());
		query.setSecondCatId(catId);
		query.setPageNo(page);

		Result result = jsonAO.appList(query);
		handleJsonResult(result, flowData, context);
	}
	
	
	public void appDetail(FlowData flowData, Context context) {
		long id = flowData.getParameters().getLong("id");
		Result result = jsonAO.appDetail(id);
		handleJsonResult(result, flowData, context);
	}
	
	public void getApps(FlowData flowData, Context context) {
		String[] packages = flowData.getParameters().getStringArray("packages");
		Result result = jsonAO.getApps(packages);
		handleJsonResult(result, flowData, context);
	}

	public void setJsonAO(JsonAO jsonAO) {
		this.jsonAO = jsonAO;
	}
	
}

