package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.admin;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.form.Form;
import wint.mvc.module.annotations.Action;
import wint.mvc.template.Context;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.AdminAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.KeyValueDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.BaseAction;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 11, 2014  9:59:56 PM</p>
 * <p>作者：聂鹏</p>
 */
public class Permission extends BaseAction {

	private AdminAO adminAO;
	
	public void list(FlowData flowData, Context context) {
		Result result = adminAO.permissionList(flowData);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "权限翻译列表");
		handleResult(result, flowData, context);
	}
	
	public void add(FlowData flowData, Context context) {
		Result result = adminAO.viewAddPermission(flowData);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "权限翻译添加");
		handleResult(result, flowData, context);
	}

	@Action(defaultTarget="/admin/permission/add") 
	public void doAddPermission(FlowData flowData, Context context) {
		Form form = flowData.getForm("permission.add");
		KeyValueDO keyValueDO = new KeyValueDO();
		if (!form.apply(keyValueDO)) {
			return;
		}

		Result result = adminAO.addPermission(flowData, keyValueDO);
		if (result.isSuccess()) {
			flowData.redirectTo("adminModule", "permission/list");
			return;
		}
		handleResult(result, flowData, context);
	}

	public void delete(FlowData flowData, Context context) {
		long id = flowData.getParameters().getLong("id");
		adminAO.deletePermission(flowData, id);
		flowData.redirectTo("adminModule", "permission/list");
	}
	

	public void setAdminAO(AdminAO adminAO) {
		this.adminAO = adminAO;
	}
	
}

