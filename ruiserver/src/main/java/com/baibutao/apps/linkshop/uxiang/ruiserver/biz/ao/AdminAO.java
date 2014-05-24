package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.bean.UserBean;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AdminDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AppInfoDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.BannerDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.KeyValueDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.AppQuery;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Apr 17, 2014  7:47:54 PM</p>
 * <p>作者：聂鹏</p>
 */
public interface AdminAO {

	Result handleLogin(UserBean userBean, FlowData flowData);
	
	Result loginOut(FlowData flowData);
	
	Result register(FlowData flowData, AdminDO adminDO);
	
	Result updatePsw(FlowData flowData, AdminDO adminDO);
	
	// --------------------------------------------------
	// -------------  普通用户相关操作 --------------------
	// --------------------------------------------------

	// 普通用户登陆首页
	Result index(FlowData flowData, AppQuery appQuery);
	
	Result myAppList(FlowData flowData, AppQuery appQuery);

	// 添加apk文件
	Result addAppFirst(FlowData flowData, long fatherAppId);
	
	
	Result viewAddUpdateInfo(FlowData flowData, long appId);
	
	// 添加或修改app基本信息
	Result updateAPPInfo(FlowData flowData, AppInfoDO appInfoDO);
	
	
	// --------------------------------------------------
	// -------------  管理员相关操作 ----------------------
	// --------------------------------------------------
	// 管理员用户登陆首页, 管理app
	Result adminAppList(FlowData flowData, AppQuery query);
	
	// app审核通过操作
	Result appCheckSuccess(FlowData flowData, long appId);
	
	// --------------------------------------------------
	// ------------- 类目相关操作 -------------------------
	// --------------------------------------------------

	Result catList(FlowData flowData);
	
	Result viewAddCat(FlowData flowData, long catId);
	
	Result addCat(FlowData flowData, CatDO catDO);
	
	Result viewEditCat(FlowData flowData, long catId);
	
	Result editCat(FlowData flowData, CatDO catDO);
	
	Result deleteCat(FlowData flowData, long catId);
	
	// --------------------------------------------------
	// ------------- 类目相关操作 -------------------------
	// --------------------------------------------------

	Result bannerList(FlowData flowData);
	
	Result viewAddBanner(FlowData flowData);
	
	Result addBanner(FlowData flowData, BannerDO bannerDO);
	
	Result viewEditBanner(FlowData flowData, long bannerId);
	
	Result editBanner(FlowData flowData, BannerDO bannerDO);
	
	Result deleteBanner(FlowData flowData, long bannerId);
	
	// --------------------------------------------------
	// ------------- app权限翻译相关操作 -------------------
	// --------------------------------------------------

	Result permissionList(FlowData flowData);
	
	Result viewAddPermission(FlowData flowData);
	
	Result addPermission(FlowData flowData, KeyValueDO keyValueDO);
	
	Result deletePermission(FlowData flowData, long id);
	
	// --------------------------------------------------
	// ------------- app推荐管理相关操作 -------------------
	// --------------------------------------------------

	Result recommendAppList(FlowData flowData, int type, int page);
	
	Result managerAppList(FlowData flowData, int type, int page);
	
	Result updateRecommendApps(FlowData flowData, int type, long[] nonRecommendAppIds, long[] recommendAppIds);
}

