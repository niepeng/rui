package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.form.Form;
import wint.mvc.module.annotations.Action;
import wint.mvc.template.Context;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.AdminAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.bean.UserBean;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AdminDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AppInfoDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.APPStsutsEnum;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.AppQuery;

public class Admin extends BaseAction {
	
	private AdminAO adminAO;
	
	/**
	 * admin index page
	 * 
	 * @param flowData
	 * @param context
	 */
	public void index(FlowData flowData, Context context) {
		if (!checkSessionNeedRedrect(flowData, context)) {
			return;
		}
		int page = flowData.getParameters().getInt("page");
		long userId = flowData.getParameters().getLong("userId");
		AppQuery query = new AppQuery();
		query.setPageNo(page);
		query.setUserId(userId);
		Result result = adminAO.index(flowData, query);
		handleResult(result, flowData, context);
	}
	
	public void myAppList(FlowData flowData, Context context) {
		int page = flowData.getParameters().getInt("page");
		AppQuery query = new AppQuery();
		query.setPageNo(page);

		Result result = adminAO.myAppList(flowData, query);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "我的应用");
		handleResult(result, flowData, context);
	}
	
	public void viewAddAppFirst(FlowData flowData, Context context) {
		if (!checkSessionNeedRedrect(flowData, context)) {
			return;
		}
		context.put("title", "上传安装包");
		flowData.setLayout("/admin/addApp");
	}
	
	@Action(defaultTarget="/admin/viewAddAppFirst") 
	public void addAppFirst(FlowData flowData, Context context) {
		Result result = adminAO.addAppFirst(flowData);
		if (result.isSuccess()) {
			flowData.redirectTo("adminModule", "viewAppUpdateInfo").param("appId", result.getModels().get("appId"));
			return;
		}
		handleError(result, flowData, context);
	}
	
	public void viewAppUpdateInfo(FlowData flowData, Context context) {
		long appId = getLongByParam(flowData, context, "appId");
		Result result = adminAO.viewAddUpdateInfo(flowData, appId);
		context.put("title", "处理应用信息");
		flowData.setLayout("/admin/addApp");
		handleResult(result, flowData, context);
	}
	
	@Action(defaultTarget="/admin/viewAppUpdateInfo") 
	public void doAppUpdateInfo(FlowData flowData, Context context) {
		long appId = getLongByParam(flowData, context, "appId");
		context.put("appId", appId);

		Form form = flowData.getForm("appinfo");
		AppInfoDO appInfoDO = new AppInfoDO();
		appInfoDO.setId(appId);
		if (!form.apply(appInfoDO)) {
			return;
		}

		Result result = adminAO.updateAPPInfo(flowData, appInfoDO);
		if (result.isSuccess()) {
			flowData.redirectTo("adminModule", "index");
			return;
		}
		handleResult(result, flowData, context);
	}
	
	/**
	 * 后台登陆
	 */
	@Action(defaultTarget="admin/login") 
	public void doLogin(FlowData flowData, Context context) {
		Form form = flowData.getForm("user.login");
		UserBean userBean = new UserBean();
		if (!form.apply(userBean)) {
			return;
		}

		// 检查用户名密码是否正确并跳转到对应的页面首页
		Result result = adminAO.handleLogin(userBean, flowData);
		if (result.isSuccess()) {
			AdminDO adminDO = (AdminDO) result.getModels().get("adminDO");
			if (adminDO.isAdmin()) {
				flowData.redirectTo("adminModule", "app/list");
			} else {
				flowData.redirectTo("adminModule", "index");
			}
			return;
		}

		handleError(result, flowData, context);
	}
	
	public void register(FlowData flowData, Context context) {
		flowData.setLayout("/admin/login");
	}
	
	
	/**
	 * 注册
	 */
	@Action(defaultTarget="admin/register") 
	public void doRegister(FlowData flowData, Context context) {
		Form form = flowData.getForm("admin.register");
		AdminDO adminDO = new AdminDO();
		if (!form.apply(adminDO)) {
			return;
		}

		Result result = adminAO.register(flowData, adminDO);
		if (result.isSuccess()) {
			flowData.redirectTo("adminModule", "index");
			return;
		}
		handleResult(result, flowData, context);
	}
	
	
	
	public void quit(FlowData flowData, Context context) {
		adminAO.loginOut(flowData);
		flowData.redirectTo("adminModule", "login");
	}
	
	public void setAdminAO(AdminAO adminAO) {
		this.adminAO = adminAO;
	}
	
}