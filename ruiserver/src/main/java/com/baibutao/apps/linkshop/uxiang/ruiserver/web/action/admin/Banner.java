package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.admin;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.form.Form;
import wint.mvc.module.annotations.Action;
import wint.mvc.template.Context;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.AdminAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.BannerDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.BaseAction;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 11, 2014  6:00:40 PM</p>
 * <p>作者：聂鹏</p>
 */
public class Banner extends BaseAction {

	private AdminAO adminAO;

	public void list(FlowData flowData, Context context) {
		Result result = adminAO.bannerList(flowData);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "banner列表");
		handleResult(result, flowData, context);
	}

	public void add(FlowData flowData, Context context) {
		Result result = adminAO.viewAddBanner(flowData);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "banner添加");
		handleResult(result, flowData, context);
	}

	@Action(defaultTarget = "/admin/cat/add")
	public void doAddBanner(FlowData flowData, Context context) {
		Form form = flowData.getForm("banner.add");
		BannerDO bannerDO = new BannerDO();
		if (!form.apply(bannerDO)) {
			return;
		}

		Result result = adminAO.addBanner(flowData, bannerDO);
		if (result.isSuccess()) {
			flowData.redirectTo("adminModule", "banner/list");
			return;
		}
		handleResult(result, flowData, context);
	}

	public void edit(FlowData flowData, Context context) {
		long bannerId = getLongByParam(flowData, context, "id");
		Result result = adminAO.viewEditBanner(flowData, bannerId);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "banner修改");
		handleResult(result, flowData, context);
	}

	@Action(defaultTarget = "/admin/banner/add")
	public void doEditBanner(FlowData flowData, Context context) {
		long id = getLongByParam(flowData, context, "id");
		context.put("id", id);

		Form form = flowData.getForm("banner.edit");
		BannerDO bannerDO = new BannerDO();
		bannerDO.setId(id);
		if (!form.apply(bannerDO)) {
			return;
		}

		Result result = adminAO.editBanner(flowData, bannerDO);
		if (result.isSuccess()) {
			flowData.redirectTo("adminModule", "banner/list");
			return;
		}
		handleResult(result, flowData, context);
	}

	public void delete(FlowData flowData, Context context) {
		long bannerId = flowData.getParameters().getLong("id");
		adminAO.deleteBanner(flowData, bannerId);
		flowData.redirectTo("adminModule", "banner/list");
	}

	public void setAdminAO(AdminAO adminAO) {
		this.adminAO = adminAO;
	}

}



