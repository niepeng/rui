package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.admin;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.AdminAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.APPStsutsEnum;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.AppQuery;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.BaseAction;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 11, 2014  6:00:48 PM</p>
 * <p>作者：聂鹏</p>
 */
public class App extends BaseAction {

	private AdminAO adminAO;
	
	public void list(FlowData flowData, Context context) {
		int status = flowData.getParameters().getInt("status", APPStsutsEnum.ONLINE.getValue());
		int page = flowData.getParameters().getInt("page");
		AppQuery query = new AppQuery();
		query.setPageNo(page);
		query.setStatus(status);
		
		Result result = adminAO.adminAppList(flowData, query);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "app管理");
		handleResult(result, flowData, context);
	}
	
	public void checkSuccess(FlowData flowData, Context context) {
		long appId = flowData.getParameters().getLong("appId");
		adminAO.appCheckSuccess(flowData, appId);
		flowData.redirectTo("adminModule", "app/list");
	}

	public void setAdminAO(AdminAO adminAO) {
		this.adminAO = adminAO;
	}
	
}



