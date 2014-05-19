package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.admin;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.form.Form;
import wint.mvc.module.annotations.Action;
import wint.mvc.template.Context;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.AdminAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.BaseAction;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 11, 2014  5:58:49 PM</p>
 * <p>作者：聂鹏</p>
 */
public class Cat extends BaseAction {

	private AdminAO adminAO;
	
	public void list(FlowData flowData, Context context) {
		Result result = adminAO.catList(flowData);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "类目管理");
		handleResult(result, flowData, context);
	}
	
	public void add(FlowData flowData, Context context) {
		long catId = getLongByParam(flowData, context, "pid");
		Result result = adminAO.viewAddCat(flowData, catId);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "类目添加");
		handleResult(result, flowData, context);
	}
	
	@Action(defaultTarget="/admin/cat/add") 
	public void doAddCat(FlowData flowData, Context context) {
		long parentId = getLongByParam(flowData, context, "parentId");
		context.put("pid", parentId);

		Form form = flowData.getForm("cat.add");
		CatDO cat = new CatDO();
		cat.setParentId(parentId);
		if (!form.apply(cat)) {
			return;
		}

		Result result = adminAO.addCat(flowData, cat);
		if (result.isSuccess()) {
			flowData.redirectTo("adminModule", "cat/list");
			return;
		}
		handleResult(result, flowData, context);
	}
	
	public void edit(FlowData flowData, Context context) {
		long catId = getLongByParam(flowData, context, "id");
		Result result = adminAO.viewEditCat(flowData, catId);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "类目修改");
		handleResult(result, flowData, context);
	}
	
	@Action(defaultTarget="/admin/cat/add") 
	public void doEditCat(FlowData flowData, Context context) {
		long id = getLongByParam(flowData, context, "id");
		context.put("id", id);

		Form form = flowData.getForm("cat.edit");
		CatDO cat = new CatDO();
		cat.setId(id);
		if (!form.apply(cat)) {
			return;
		}

		Result result = adminAO.editCat(flowData, cat);
		if (result.isSuccess()) {
			flowData.redirectTo("adminModule", "cat/list");
			return;
		}
		handleResult(result, flowData, context);
	}
	
	public void delete(FlowData flowData, Context context) {
		long id = flowData.getParameters().getLong("id");
		adminAO.deleteCat(flowData, id);
		flowData.redirectTo("adminModule", "cat/list");
	}
	

	public void setAdminAO(AdminAO adminAO) {
		this.adminAO = adminAO;
	}
	
}

