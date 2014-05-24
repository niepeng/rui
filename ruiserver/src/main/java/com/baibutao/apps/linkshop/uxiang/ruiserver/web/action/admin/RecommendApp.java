package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.admin;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.AdminAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.BaseAction;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 19, 2014  10:09:27 PM</p>
 * <p>作者：聂鹏</p>
 */
public class RecommendApp extends BaseAction {

	private AdminAO adminAO;

	public void list(FlowData flowData, Context context) {
		// 默认是游戏精选， 1 游戏精选，2 精品推荐
		int type = flowData.getParameters().getInt("type", 1);
		int page = flowData.getParameters().getInt("page");

		Result result = adminAO.recommendAppList(flowData, type, page);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "推荐app列表");
		handleResult(result, flowData, context);
	}

	public void managerAppList(FlowData flowData, Context context) {
		int type = flowData.getParameters().getInt("type", 1);
		int page = flowData.getParameters().getInt("page");

		Result result = adminAO.managerAppList(flowData, type, page);
		flowData.setLayout("/admin/index");
		result.getModels().put("title", "管理推荐app");
		handleResult(result, flowData, context);

	}
	
	public void updateRecommendAppList(FlowData flowData, Context context) {
		int type = flowData.getParameters().getInt("type");
		long[] checkAppIds = flowData.getParameters().getLongArray("checkedAppIds");
		long[] currentAllAppIds = flowData.getParameters().getLongArray("currentAllAppIds");
		long[] nonRecommendAppIds = getNonRecommendAppIds(checkAppIds, currentAllAppIds);
		adminAO.updateRecommendApps(flowData, type, nonRecommendAppIds, checkAppIds);
		flowData.redirectTo("adminModule", "recommendApp/list").param("type", type);
	}

	private long[] getNonRecommendAppIds(long[] checkAppIds, long[] currentAllAppIds) {
		if (currentAllAppIds == null) {
			return null;
		}
		
		if(checkAppIds == null) {
			return currentAllAppIds;
		}

		// 把当前需要添加推荐的appId，从currentAllAppIds中去除
		for (int i = 0; i < currentAllAppIds.length; i++) {
			for (long checkApp : checkAppIds) {
				if (checkApp == currentAllAppIds[i]) {
					currentAllAppIds[i] = 0;
					break;
				}
			}
		}
		return currentAllAppIds;
	}

	public void setAdminAO(AdminAO adminAO) {
		this.adminAO = adminAO;
	}

}

