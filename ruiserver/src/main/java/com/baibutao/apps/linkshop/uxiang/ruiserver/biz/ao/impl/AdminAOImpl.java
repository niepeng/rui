package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import javax.servlet.http.HttpSession;

import net.erdfelt.android.apk.AndroidApk;
import wint.help.biz.result.Result;
import wint.help.biz.result.ResultSupport;
import wint.help.biz.result.StringResultCode;
import wint.help.codec.MD5;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.form.fileupload.UploadFile;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.AdminAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.BaseAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.bean.UserBean;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.bo.FileService;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.bo.impl.FileServiceImpl;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common.CatchDataUtil;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common.ContentUtil;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.AdminDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.AppInfoDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.BannerDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.CatDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.KeyValueDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AdminDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AppInfoDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.BannerDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.KeyValueDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.APPStsutsEnum;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.KeyValueTypeEnum;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.RoleEnum;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.AppQuery;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.KeyValueQuery;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.common.AdminResultCodes;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.common.SessionKeys;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>创建时间: Apr 17, 2014  7:53:49 PM</p>
 * <p>作者：聂鹏</p>
 */
public class AdminAOImpl extends BaseAO implements AdminAO {

	private AdminDAO adminDAO;

	private KeyValueDAO keyValueDAO;

	private CatDAO catDAO;

	private AppInfoDAO appInfoDAO;

	private BannerDAO bannerDAO;

	private FileService fileService;

	// app的icon的大小为 512 * 512，其他大小不允许
	private int ICON_WIDTH = 512;

	private int ICON_HEIGHT = 512;

	@Override
	public Result handleLogin(UserBean userBean, FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			AdminDO adminDO = adminDAO.queryByName(userBean.getNick());
			if (adminDO == null || !adminDO.getPsw().equals(MD5.encrypt(userBean.getPassword()))) {
				result.setResultCode(new StringResultCode("用户名或密码错误!"));
				return result;
			}

			HttpSession session = flowData.getSession();
			session.setAttribute(SessionKeys.USER_NAME, adminDO.getUserName());
			session.setAttribute(SessionKeys.USER_ID, adminDO.getId());
			session.setAttribute(SessionKeys.ROLE_ID, adminDO.getRoleId());

			result.getModels().put("adminDO", adminDO);
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("handleLogin error", e);
		}
		return result;

	}

	@Override
	public Result loginOut(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			HttpSession session = flowData.getSession();
			session.removeAttribute(SessionKeys.USER_NAME);
			session.removeAttribute(SessionKeys.USER_ID);
			session.removeAttribute(SessionKeys.ROLE_ID);
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("loginOut error", e);
		}
		return result;
	}

	@Override
	public Result register(FlowData flowData, AdminDO adminDO) {
		Result result = new ResultSupport(false);
		try {
			AdminDO fromDB = adminDAO.queryByName(adminDO.getUserName());
			if (fromDB != null) {
				result.setResultCode(new StringResultCode("当前的用户名已经存在了"));
				return result;
			}

			adminDO.setRoleId(RoleEnum.USER.getValue());
			adminDO.setPsw(MD5.encrypt(adminDO.getPsw()));

			long id = adminDAO.create(adminDO);
			if (id > 0) {
				HttpSession session = flowData.getSession();
				session.setAttribute(SessionKeys.USER_NAME, adminDO.getUserName());
				session.setAttribute(SessionKeys.USER_ID, id);
				session.setAttribute(SessionKeys.ROLE_ID, adminDO.getRoleId());
				result.setSuccess(true);
				return result;
			}

			result.setResultCode(new StringResultCode("当前参数错误"));

		} catch (Exception e) {
			log.error("register error", e);
		}
		return result;
	}

	@Override
	public Result updatePsw(FlowData flowData, AdminDO adminDO) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData)) {
				return result;
			}

			if (!adminDO.getPsw1().equals(adminDO.getPsw2())) {
				result.setResultCode(new StringResultCode("2次输入的密码不相同"));
				return result;
			}

			long userId = getLoginUserId(flowData);
			AdminDO fromDB = adminDAO.queryById(userId);
			if (fromDB == null) {
				result.setResultCode(new StringResultCode("当前的用户已经不存在了"));
				return result;
			}

			if (!fromDB.getPsw().equals(MD5.encrypt(adminDO.getPsw()))) {
				result.setResultCode(new StringResultCode("原密码错误，请重新输入"));
				return result;
			}

			fromDB.setPsw(MD5.encrypt(adminDO.getPsw1()));
			adminDAO.update(fromDB);
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("updatePsw error", e);
		}
		return result;
	}

	@Override
	public Result index(FlowData flowData, AppQuery appQuery) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData)) {
				return result;
			}

			// 如果当前不是admin用户，那么普通用户只能访问自己的app列表
			if (!isAdmin(flowData)) {
				long loginUserId = getLoginUserId(flowData);
				if (loginUserId != appQuery.getUserId()) {
					appQuery.setUserId(loginUserId);
				}
			}

			if(appQuery.getUserId() == 0 && isAdmin(flowData)) {
				appQuery.setUserId(getLoginUserId(flowData));
			}

			// 获取当前用户提交的所有app列表
			List<AppInfoDO> appInfoList = appInfoDAO.query(appQuery);
			result.getModels().put("appInfoList", appInfoList);
			result.getModels().put("query", appQuery);

			result.setSuccess(true);

		} catch (Exception e) {
			log.error("index error", e);
		}
		return result;
	}

	@Override
	public Result myAppList(FlowData flowData, AppQuery appQuery) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData)) {
				return result;
			}

			// 如果当前不是admin用户，那么普通用户只能访问自己的app列表
			if (!isAdmin(flowData)) {
				long loginUserId = getLoginUserId(flowData);
				if (loginUserId != appQuery.getUserId()) {
					appQuery.setUserId(loginUserId);
				}
			}

			List<AppInfoDO> appInfoList = appInfoDAO.query(appQuery);
			result.getModels().put("appInfoList", appInfoList);
			result.getModels().put("query", appQuery);

			result.setSuccess(true);

		} catch (Exception e) {
			log.error("myAppList error", e);
		}
		return result;
	}

	@Override
	public Result addAppFirst(FlowData flowData, long fatherAppId) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData)) {
				return result;
			}

			// 上传apk，并分析apk
			UploadFile file = flowData.getUploadFiles().get("uploadFile");
			if (file == null || file.getSize() < 1) {
				result.setResultCode(new StringResultCode("请先选择apk文件"));
				return result;
			}

			String name = file.getName();
			if (!FileServiceImpl.isSuffix(name, FileServiceImpl.APK)) {
				result.setResultCode(new StringResultCode("请上传apk文件"));
				return result;
			}

			AppInfoDO appInfoDO = new AppInfoDO();
			appInfoDO.setStatus(APPStsutsEnum.READY_SAVE.getValue());
			appInfoDO.setUploadDate(new Date());
			appInfoDO.setUploadUserId(getLoginUserId(flowData));

			String visitUrl = fileService.uploadFile(file);
			if(StringUtil.isBlank(visitUrl)) {
				result.setResultCode(new StringResultCode("上传apk文件失败"));
				return result;
			}

			// 1. 解析出包名以及版本
			if(!parseAPKPackage(result, appInfoDO, visitUrl)) {
				return result;
			}

			// 2. 新增app:原有系统中不存在的app
			// 	  更新app:原有系统中要有app，并把信息复制过去
			boolean isUpdateApp = false;
			AppInfoDO fatherAppInfoDO = null;
			if(fatherAppId > 0) {
				fatherAppInfoDO = appInfoDAO.queryById(fatherAppId);
				if(isSameApp(appInfoDO, fatherAppInfoDO)) {
					isUpdateApp = true;
				} else {
					// 标记当前参数fatherAppId有问题，设置空
					fatherAppId = 0;
				}

			}


			List<AppInfoDO> dbAppList = appInfoDAO.queryByPackageName(appInfoDO.getPackageName());
			if (!CollectionUtil.isEmpty(dbAppList) && !isUpdateApp) {
				result.setResultCode(new StringResultCode("该app已经存在"));
				return result;
			}

			if(isUpdateApp) {
				copyAppInfo(appInfoDO, fatherAppInfoDO);
			}

			long id = appInfoDAO.create(appInfoDO);

			// 并在原有的app中记录nextAppId
			if (isUpdateApp && fatherAppInfoDO != null) {
				fatherAppInfoDO.setNextAppId(id);
				appInfoDAO.update(fatherAppInfoDO);
			}

			result.getModels().put("appId", id);
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("addAppFirst error", e);
		}
		return result;
	}

	private boolean parseAPKPackage(Result result, AppInfoDO appInfoDO, String visitUrl) throws ZipException, IOException {
		if(StringUtil.isBlank(visitUrl)) {
			return false;
		}
		String fileName = visitUrl.substring(visitUrl.lastIndexOf("/") + 1);
		File apkFile = fileService.getFileByName(fileName);
		if (!apkFile.exists()) {
			if(result != null) {
				result.setResultCode(new StringResultCode("上传apk文件分析文件信息失败"));
			}
			return false;
		}

		AndroidApk apk = new AndroidApk(apkFile);
		long fileSize = apkFile.length();
		appInfoDO.setPackageName(apk.getPackageName());
		appInfoDO.setVersionName(apk.getAppVersion());
		appInfoDO.setVersionValue(apk.getAppVersionCode());
		appInfoDO.setFileSize((int) (fileSize / 1024));
		List<String> permissionList = apk.getUsesPermission();
		String permissoinValues = CollectionUtil.join(permissionList, ",");
		if (!StringUtil.isBlank(permissoinValues)) {
			appInfoDO.setPermissionValue(permissoinValues);
		}
		appInfoDO.setDownUrl(visitUrl);
		return true;
	}

	private void copyAppInfo(AppInfoDO appInfoDO, AppInfoDO fatherAppInfoDO) {
		if(appInfoDO == null || fatherAppInfoDO == null) {
			return;
		}

		appInfoDO.setReferMainAppId(fatherAppInfoDO.getId());
		appInfoDO.setIconUrl(fatherAppInfoDO.getIconUrl());
		appInfoDO.setMainTitle(fatherAppInfoDO.getMainTitle());
		appInfoDO.setSubTitle(fatherAppInfoDO.getSubTitle());
		appInfoDO.setFirstCatId(fatherAppInfoDO.getFirstCatId());
		appInfoDO.setSecondCatId(fatherAppInfoDO.getSecondCatId());
		appInfoDO.setInfo(fatherAppInfoDO.getInfo());
		appInfoDO.setScreenshots(fatherAppInfoDO.getScreenshots());

	}

	private boolean isSameApp(AppInfoDO appInfoDO, AppInfoDO fatherAppInfoDO) {
		if (fatherAppInfoDO == null) {
			return false;
		}
		return appInfoDO.getUploadUserId() == fatherAppInfoDO.getUploadUserId() && appInfoDO.getPackageName().equals(fatherAppInfoDO.getPackageName());
	}

	@Override
	public Result viewAddUpdateInfo(FlowData flowData, long appId) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData)) {
				return result;
			}

			AppInfoDO appInfoDO = appInfoDAO.queryById(appId);
			if (appInfoDO == null) {
				result.setResultCode(AdminResultCodes.APP_ARGS_ERROR);
				return result;
			}

			if (!isAdmin(flowData) && getLoginUserId(flowData) != appInfoDO.getUploadUserId()) {
				result.setResultCode(AdminResultCodes.APP_ARGS_ERROR);
				return result;
			}

			List<CatDO> catList = catDAO.listAll();

			result.getModels().put("appInfoDO", appInfoDO);
			result.getModels().put("catList", catList);
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("viewAddUpdateInfo error", e);
		}
		return result;
	}

	@Override
	public Result updateAPPInfo(FlowData flowData, AppInfoDO appInfoDO) {

		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData)) {
				return result;
			}

			AppInfoDO fromDB = appInfoDAO.queryById(appInfoDO.getId());
			if (fromDB == null || (!isAdmin(flowData) && getLoginUserId(flowData) != fromDB.getUploadUserId()) || fromDB.isTimeOut()) {
				result.setResultCode(AdminResultCodes.APP_ARGS_ERROR);
				return result;
			}

			// 图标上传处理
			UploadFile file = flowData.getUploadFiles().get("iconUrl");
			if (file != null && file.getSize() > 0) {
				String name = file.getName();
				if (!FileServiceImpl.isSuffix(name, FileServiceImpl.PNG)) {
					result.setResultCode(new StringResultCode("图标需要上传png格式的图片"));
					return result;
				}
			}
			String iconUrl = fileService.uploadFile(file, FileServiceImpl.PNG);
			if (!StringUtil.isBlank(iconUrl)) {
				// 判断图片大小是否符合要求
				String fileName = iconUrl.substring(iconUrl.lastIndexOf("/") + 1);
				File imageFile = fileService.getFileByName(fileName);
				boolean checkFileWidthHeight = fileService.checkFileWidthHeigthEqueals(imageFile, ICON_WIDTH, ICON_HEIGHT);
				if (!checkFileWidthHeight) {
					result.setResultCode(new StringResultCode("图标的宽高限制为512*512"));
					return result;
				}
				fromDB.setIconUrl(iconUrl);
			}
			if (StringUtil.isBlank(fromDB.getIconUrl())) {
				result.setResultCode(new StringResultCode("请先上传图标"));
				return result;
			}

			fromDB.setMainTitle(appInfoDO.getMainTitle());
			fromDB.setSubTitle(appInfoDO.getSubTitle());
			fromDB.setFirstCatId(appInfoDO.getFirstCatId());
			fromDB.setSecondCatId(appInfoDO.getSecondCatId());
			fromDB.setInfo(appInfoDO.getInfo());
			fromDB.setUpdateInfo(appInfoDO.getUpdateInfo());

			// 应用截图上传处理：如果原来为空，那么一定要上传5张，原来不为空，那么可以替换上传的部分
			int screenshotsNum = 5;
			boolean screenShotAllUpload = true;
			String[] screenShotUrls = null;
			if(!StringUtil.isBlank(fromDB.getScreenshots())) {
				screenShotUrls = fromDB.getScreenshots().split(",");
			} else {
				screenShotUrls = new String[screenshotsNum];
			}

			for (int i = 1; i <= screenshotsNum; i++) {
				UploadFile uploadFile = flowData.getUploadFiles().get("screenshots" + i);
				String url = fileService.uploadFile(uploadFile);
				if (url == null) {
					screenShotAllUpload = false;
					continue;
				}
				screenShotUrls[i-1] = url;
			}

			if(StringUtil.isBlank(fromDB.getScreenshots()) && !screenShotAllUpload) {
				result.setResultCode(new StringResultCode("必须上传" + screenshotsNum + "张截图"));
				return result;
			}

			fromDB.setScreenshots(mergeString(screenShotUrls, ","));
			if (!isAdmin(flowData) || (!fromDB.isOnLine())) {
				fromDB.setStatus(APPStsutsEnum.READY_CHECK.getValue());
			}

			appInfoDAO.update(fromDB);
			result.getModels().put("appInfoDO", fromDB);
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("updateAPPInfo error", e);
		}
		return result;
	}

	private String mergeString(String[] screenShotUrls, String split) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < screenShotUrls.length; i++) {
			if (i != 0) {
				result.append(split);
			}
			result.append(screenShotUrls[i]);
		}
		return result.toString();
	}

	@Override
	public Result adminAppList(FlowData flowData, AppQuery query) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) && !isAdmin(flowData)) {
				return result;
			}

			List<AppInfoDO> appInfoList = appInfoDAO.query(query);

			result.getModels().put("appInfoList", appInfoList);
			result.getModels().put("query", query);

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("adminAppList error", e);
		}
		return result;
	}

	@Override
	public Result appCheckSuccess(FlowData flowData, long appId) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) && !isAdmin(flowData)) {
				return result;
			}

			/*
			 * 最终上线的appId的值还是老的appId的值，就是当前的referMainAppId对应的app，信息使用新的
			 *
			 * 1. 根据传递过来的appId，获取他的referMainApp，把appId对应的与referMainApp对应的信息互换
			 * 2. appId的修改为过期，referMainApp在线
			 * 3. appId对应的referMainAppId=referMainApp.id,nextAppId=0
			 * 4. referMainApp对应的referMainAppId=0，next=0
			 */

			AppInfoDO appInfoDO = appInfoDAO.queryById(appId);
			if(appInfoDO == null || appInfoDO.isOnLine() || appInfoDO.isTimeOut()) {
				result.setResultCode(new StringResultCode("当前app不存在或已经上线了"));
				return result;
			}

			// app第一次提交审核通过处理
			if(appInfoDO.getReferMainAppId() == 0) {
				appInfoDO.setStatus(APPStsutsEnum.ONLINE.getValue());
				appInfoDAO.update(appInfoDO);
				result.setSuccess(true);
				return result;
			}

			AppInfoDO fatherAppDO = appInfoDAO.queryById(appInfoDO.getReferMainAppId());
			if(fatherAppDO == null || !fatherAppDO.isOnLine()) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}

			AppInfoDO lastOnlineAppInfoDO = appInfoDO.clone();

			// 设置最终上线的信息
			lastOnlineAppInfoDO.setId(fatherAppDO.getId());
			lastOnlineAppInfoDO.setStatus(APPStsutsEnum.ONLINE.getValue());
			lastOnlineAppInfoDO.setNextAppId(0);
			lastOnlineAppInfoDO.setReferMainAppId(0);
			lastOnlineAppInfoDO.setPublishDate(new Date());
			lastOnlineAppInfoDO.setDownloadNum(fatherAppDO.getDownloadNum());
			lastOnlineAppInfoDO.setFavNum(fatherAppDO.getFavNum());
			lastOnlineAppInfoDO.setRecommendNum(fatherAppDO.getRecommendNum());
			lastOnlineAppInfoDO.setCommentNum(fatherAppDO.getCommentNum());
			appInfoDAO.update(lastOnlineAppInfoDO);

			// 设置过期
			fatherAppDO.setId(appInfoDO.getId());
			fatherAppDO.setStatus(APPStsutsEnum.SUCCESS_VERSION_OUT.getValue());
			fatherAppDO.setNextAppId(0);
			fatherAppDO.setReferMainAppId(lastOnlineAppInfoDO.getId());
			appInfoDAO.update(fatherAppDO);

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("appCheckSuccess error", e);
		}
		return result;
	}

	@Override
	public Result catList(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			List<CatDO> catList = catDAO.listAll();
			result.getModels().put("catList", catList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("catList error", e);
		}
		return result;
	}

	@Override
	public Result viewAddCat(FlowData flowData, long catId) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			if (catId > 0) {
				CatDO cat = catDAO.queryById(catId);
				if (cat != null) {
					result.getModels().put("parentCatDO", cat);
				}
			}

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("viewAddCat error", e);
		}
		return result;
	}

	@Override
	public Result addCat(FlowData flowData, CatDO catDO) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			if (catDO.getParentId() < 1) {
				result.setResultCode(new StringResultCode("一级类目缺失"));
				return result;
			}

			CatDO parentCatDO = catDAO.queryById(catDO.getParentId());
			if (parentCatDO == null) {
				result.setResultCode(new StringResultCode("一级类目缺失"));
				return result;
			}

			catDO.setLevel(parentCatDO == null ? 1 : parentCatDO.getLevel() + 1);

			// icon上传
			UploadFile file = flowData.getUploadFiles().get("iconUrl");
			if (file == null || file.getSize() < 1) {
				result.setResultCode(new StringResultCode("请先上传图标"));
				return result;
			}
			String iconUrl = fileService.uploadFile(file);
			catDO.setIconUrl(iconUrl);

			catDAO.create(catDO);

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("addCat error", e);
		}
		return result;
	}

	@Override
	public Result viewEditCat(FlowData flowData, long catId) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			CatDO cat = catDAO.queryById(catId);
			if (cat == null) {
				result.setResultCode(new StringResultCode("当前类目已经不存在了"));
				return result;
			}
			result.getModels().put("catDO", cat);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("viewEditCat error", e);
		}
		return result;
	}

	@Override
	public Result editCat(FlowData flowData, CatDO catDO) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			CatDO fromDB = catDAO.queryById(catDO.getId());
			if (fromDB == null) {
				result.setResultCode(new StringResultCode("参数错误"));
				return result;
			}

			// icon上传
			UploadFile file = flowData.getUploadFiles().get("iconUrl");
			if (file != null && file.getSize() > 0) {
				String iconUrl = fileService.uploadFile(file);
				fromDB.setIconUrl(iconUrl);
			}

			fromDB.setName(catDO.getName());
			fromDB.setSortValue(catDO.getSortValue());

			catDAO.update(fromDB);

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("editCat error", e);
		}
		return result;
	}

	@Override
	public Result deleteCat(FlowData flowData, long catId) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			CatDO fromDB = catDAO.queryById(catId);
			if (fromDB == null) {
				result.setResultCode(new StringResultCode("参数错误"));
				return result;
			}

			AppQuery query = new AppQuery();
			if (fromDB.getLevel() == 1) {
				result.setResultCode(new StringResultCode("一级类目不能删除"));
				return result;
			} else if (fromDB.getLevel() == 2) {
				query.setSecondCatId(fromDB.getId());
			}
			query.setPageSize(1);

			List<AppInfoDO> appInfoList = appInfoDAO.query(query);
			if (!CollectionUtil.isEmpty(appInfoList)) {
				result.setResultCode(new StringResultCode("当期类目下面还有app，不能删除"));
				return result;
			}

			catDAO.delete(fromDB.getId());

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("deleteCat error", e);
		}
		return result;
	}

	@Override
	public Result bannerList(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			List<BannerDO> bannerList = bannerDAO.listAll();
			result.getModels().put("bannerList", bannerList);

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("bannerList error", e);
		}
		return result;
	}

	@Override
	public Result viewAddBanner(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("viewAddBanner error", e);
		}
		return result;
	}

	@Override
	public Result addBanner(FlowData flowData, BannerDO bannerDO) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			// 图片上传
			UploadFile file = flowData.getUploadFiles().get("imageUrl");
			if (file == null || file.getSize() < 1) {
				result.setResultCode(new StringResultCode("请先上传图片"));
				return result;
			}
			String imageUrl = fileService.uploadFile(file);
			bannerDO.setImageUrl(imageUrl);

			bannerDAO.create(bannerDO);

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("addBanner error", e);
		}
		return result;
	}

	@Override
	public Result viewEditBanner(FlowData flowData, long bannerId) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			BannerDO bannerDO = bannerDAO.queryById(bannerId);
			if (bannerDO == null) {
				result.setResultCode(new StringResultCode("当前banner已经不存在了"));
				return result;
			}
			result.getModels().put("bannerDO", bannerDO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("viewEditBanner error", e);
		}
		return result;
	}

	@Override
	public Result editBanner(FlowData flowData, BannerDO bannerDO) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			BannerDO fromDB = bannerDAO.queryById(bannerDO.getId());
			if (fromDB == null) {
				result.setResultCode(new StringResultCode("参数错误"));
				return result;
			}

			// 图片上传
			UploadFile file = flowData.getUploadFiles().get("imageUrl");
			if (file != null && file.getSize() > 0) {
				String imageUrl = fileService.uploadFile(file);
				fromDB.setImageUrl(imageUrl);
			}
			fromDB.setName(bannerDO.getName());
			fromDB.setSortValue(bannerDO.getSortValue());

			bannerDAO.update(fromDB);

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("editCat error", e);
		}
		return result;
	}

	@Override
	public Result deleteBanner(FlowData flowData, long bannerId) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			BannerDO fromDB = bannerDAO.queryById(bannerId);
			if (fromDB == null) {
				result.setResultCode(new StringResultCode("参数错误"));
				return result;
			}

			bannerDAO.delete(bannerId);

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("deleteBanner error", e);
		}
		return result;
	}

	@Override
	public Result permissionList(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			List<KeyValueDO> keyValueList = keyValueDAO.queryByType(KeyValueTypeEnum.PERMINSSION_MAP);
			result.getModels().put("keyValueList", keyValueList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("permissionList error", e);
		}
		return result;
	}

	@Override
	public Result viewAddPermission(FlowData flowData) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("viewAddPermission error", e);
		}
		return result;
	}

	@Override
	public Result addPermission(FlowData flowData, KeyValueDO keyValueDO) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			KeyValueDO fromDB = keyValueDAO.queryByKey(keyValueDO.getKeyName());
			if(fromDB != null) {
				result.setResultCode(new StringResultCode("当前权限翻译已经存在"));
				return result;
			}

			keyValueDO.setType(KeyValueTypeEnum.PERMINSSION_MAP.getId());
			keyValueDAO.create(keyValueDO);

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("addPermission error", e);
		}
		return result;
	}

	@Override
	public Result deletePermission(FlowData flowData, long id) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			KeyValueDO fromDB = keyValueDAO.queryById(id);
			if (fromDB == null) {
				result.setResultCode(new StringResultCode("当前权限翻译已经不存在"));
				return result;
			}

			if (KeyValueTypeEnum.PERMINSSION_MAP.getId() != fromDB.getType()) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}

			keyValueDAO.delete(id);

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("deletePermission error", e);
		}
		return result;
	}

	@Override
	public Result recommendAppList(FlowData flowData, int type, int page) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			if (type != 1 && type != 2) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}
			result.getModels().put("type", type);

			KeyValueQuery query = new KeyValueQuery();
			query.setPageNo(page);
			query.setPageSize(JsonAOImpl.APP_LIST_NUM);
			if (type == 1) {
				query.setKeyName(KeyValueTypeEnum.RECOMMEND_APP_GAME.getKeyName());
			} else {
				query.setKeyName(KeyValueTypeEnum.RECOMMEND_APP_GOOD.getKeyName());
			}

			result.setSuccess(true);
			List<AppInfoDO> appInfoList = getAppListFByKeyValueQuery(keyValueDAO, appInfoDAO, query);

			result.getModels().put("appInfoList", appInfoList);

		} catch (Exception e) {
			log.error("recommendAppList error", e);
		}
		return result;

	}

	public static List<AppInfoDO> getAppListFByKeyValueQuery(KeyValueDAO keyValueDAO, AppInfoDAO appInfoDAO, KeyValueQuery query) {
		List<KeyValueDO> keyList = keyValueDAO.query(query);
		if (CollectionUtil.isEmpty(keyList)) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keyList.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(keyList.get(i).getValue());
		}
		List<AppInfoDO> appInfoList = appInfoDAO.queryByIds(sb.toString());
		for (int i = 0; i < appInfoList.size();) {
			if (!appInfoList.get(i).isOnLine()) {
				appInfoList.remove(i);
				continue;
			}
			i++;
		}
		return appInfoList;
	}

	@Override
	public Result managerAppList(FlowData flowData, int type, int page) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			if (type != 1 && type != 2) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}
			result.getModels().put("type", type);
			AppQuery query = new AppQuery();
			query.setStatus(APPStsutsEnum.ONLINE.getValue());
			query.setPageNo(page);
			query.setPageSize(JsonAOImpl.APP_LIST_NUM);
			List<AppInfoDO> appInfoList = appInfoDAO.query(query);

			setAppInfoListRecommend(appInfoList, type);
			result.getModels().put("appInfoList", appInfoList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("managerAppList error", e);
		}
		return result;

	}

	@Override
	public Result updateRecommendApps(FlowData flowData, int type, long[] nonRecommendAppIds, long[] recommendAppIds) {
		Result result = new ResultSupport(false);
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			if (type != 1 && type != 2) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}

			Map<Long, KeyValueDO> map = getRecommendAppByType(type);


			if (nonRecommendAppIds != null && map != null) {
				for (long non : nonRecommendAppIds) {
					if (non == 0) {
						continue;
					}
					// 如果数据库存在，那么删除改记录
					KeyValueDO keyValueDO = map.get(non);
					if (keyValueDO != null) {
						keyValueDAO.delete(keyValueDO.getId());
					}
				}
			}

			if(recommendAppIds != null) {
				for (long recommendAppId : recommendAppIds) {
					if (recommendAppId == 0) {
						continue;
					}
					// 如果数据库不存在，那么需要添加推荐app
					KeyValueDO keyValueDO = map.get(recommendAppId);
					if (keyValueDO == null) {
						keyValueDO = new KeyValueDO();
						if (type == 1) {
							keyValueDO.setKeyName(KeyValueTypeEnum.RECOMMEND_APP_GAME.getKeyName());
							keyValueDO.setType(KeyValueTypeEnum.RECOMMEND_APP_GAME.getId());
						} else {
							keyValueDO.setKeyName(KeyValueTypeEnum.RECOMMEND_APP_GOOD.getKeyName());
							keyValueDO.setType(KeyValueTypeEnum.RECOMMEND_APP_GOOD.getId());
						}
						keyValueDO.setValue(String.valueOf(recommendAppId));
						keyValueDAO.create(keyValueDO);
					}
				}
			}

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("updateRecommendApps error", e);
		}
		return result;
	}


	@Override
	public Result catchCatsAndApps(FlowData flowData) {
		Result result = new ResultSupport(false);
		log.error("catchCatsAndApps->1");
		try {
			// 登陆和权限判断
			if (!ensureUserLogin(result, flowData) || !isAdmin(flowData)) {
				return result;
			}

			log.error("catchCatsAndApps->2");

			// 应用
			final String startAppUrl = "http://www.wandoujia.com/tag/app";
			// 游戏
			final String startGameUrl = "http://www.wandoujia.com/tag/game";

			final long userId = getLoginUserId(flowData);


			int type = flowData.getParameters().getInt("type");
			final int maxNum = flowData.getParameters().getInt("max");

			/**
			 * type=2 通过类目抓取，max是抓取数量
			 * type=1 默认抓取，如果类目存在就不抓取了
			 */
			if(type == 2 && maxNum > 0) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// 遍历获取二级类目，然后执行抓取
						List<CatDO> catList = catDAO.queryFirstLevel();
						if(CollectionUtil.isEmpty(catList)) {
							return;
						}
						for (CatDO firstCat : catList) {
							List<CatDO> secondCatList = catDAO.queryByParentId(firstCat.getId());
							if (CollectionUtil.isEmpty(secondCatList)) {
								continue;
							}

							for (CatDO secondCat : secondCatList) {
								String name = secondCat.getName();
//								System.out.println("name=(" + secondCat.getName() + "), maxNum=(" + maxNum + ")");
								List<AppInfoDO> appList = ContentUtil.catchApps(name, maxNum);
								if (CollectionUtil.isEmpty(appList)) {
									continue;
								}

								for (int i = 0, size = appList.size(); i < size; i++) {
									List<AppInfoDO> fromDBList = appInfoDAO.queryByPackageName(appList.get(i).getPackageName());
									if (!CollectionUtil.isEmpty(fromDBList)) {
										continue;
									}

									AppInfoDO prepareAppInfoDO = mergeAppInfo(appList.get(i), secondCat, userId);
									if (prepareAppInfoDO != null) {
										prepareAppInfoDO.setUploadDate(new Date());
										prepareAppInfoDO.setPublishDate(new Date());
										appInfoDAO.create(prepareAppInfoDO);
									}
								}

							}
						}

						log.error("catchCatsAndAppsByMaxNumIsEnd");
					}
				}).start();
				result.setSuccess(true);
				return result;
			}

			if(type <=0) {
				result.setSuccess(true);
				return result;
			}


			/* 1. 抓取类目数据
			 * 2. 抓取app数据
			*/
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<CatDO> catList = catDAO.queryFirstLevel();
					if(catList == null || catList.size() != 2) {
						log.error("catchCatsAndApps-> firstCat num is not correct");
						return;
					}

					log.error("catchCatsAndApps->start");
					CatDO appCat = catList.get(0);
					CatDO gameCat = catList.get(1);

					List<CatDO> catchAppCatList = CatchDataUtil.catchCats(startAppUrl);
					optCurrentCats(catchAppCatList, appCat);
					List<CatDO> catchGameCatList = CatchDataUtil.catchCats(startGameUrl);
					optCurrentCats(catchGameCatList, gameCat);
					log.error("catchCatsAndApps->end");
				}

				private void optCurrentCats(List<CatDO> catchCatList, CatDO appCat) {
					if (CollectionUtil.isEmpty(catchCatList)) {
						return;
					}
					List<CatDO> list = null;
					int num = 2;
					for (CatDO cat : catchCatList) {
						list = catDAO.queryByName(cat.getName());
						if (!CollectionUtil.isEmpty(list)) {
							continue;
						}
						// 数据库不存在的类目，需要处理，首先获取类目图片
						String catchIconUrl = CatchDataUtil.catchCatImg(cat.getName());
						String iconUrl = fileService.uploadFile(catchIconUrl, FileServiceImpl.PNG);
						if (StringUtil.isBlank(iconUrl)) {
							continue;
						}
						cat.setIconUrl(iconUrl);
						cat.setLevel(2);
						cat.setParentId(appCat.getId());
						cat.setSortValue(num++);
						long secondCatId = catDAO.create(cat);
						cat.setId(secondCatId);
						uploadApps(cat);
					}

				}

				private void uploadApps(CatDO cat) {
					List<AppInfoDO> appList = CatchDataUtil.catAppList("http://www.wandoujia.com/tag/"+ CatchDataUtil.encode(cat.getName()));
					if (CollectionUtil.isEmpty(appList)) {
						return;
					}
					for (AppInfoDO appInfoDO : appList) {
						List<AppInfoDO> fromDBList = appInfoDAO.queryByPackageName(appInfoDO.getPackageName());
						if (!CollectionUtil.isEmpty(fromDBList)) {
							continue;
						}
						AppInfoDO fromCatchApp = CatchDataUtil.catAppDetail(appInfoDO.getCatchDetailUrl(), null);
						fromCatchApp.setPackageName(appInfoDO.getPackageName());
						AppInfoDO prepareAppInfoDO = mergeAppInfo(fromCatchApp, cat, userId);
						if (prepareAppInfoDO != null) {
							prepareAppInfoDO.setUploadDate(new Date());
							prepareAppInfoDO.setPublishDate(new Date());
							appInfoDAO.create(prepareAppInfoDO);
						}

					}
				}



			}).start();

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("catchCatsAndApps error", e);
		}
		return result;
	}

	private void setAppInfoListRecommend(List<AppInfoDO> appInfoList, int type) {
		if (CollectionUtil.isEmpty(appInfoList)) {
			return;
		}

		// 获取当前的所有的推荐app
		Map<Long, KeyValueDO> map = getRecommendAppByType(type);
		if(map == null) {
			return;
		}

		// 遍历当前appList，设置推荐值
		for (AppInfoDO appInfoDO : appInfoList) {
			if (map.containsKey(appInfoDO.getId())) {
				appInfoDO.setRecommendApp(true);
			}
		}

	}

	private AppInfoDO mergeAppInfo(AppInfoDO fromCatchApp, CatDO cat, long userId) {
		try {
			String apkUrl = fileService.uploadFile(fromCatchApp.getCatchDataBean().downloadUrl, FileServiceImpl.APK);
			fromCatchApp.setDownUrl(apkUrl);
			String iconUrl = fileService.uploadFile(fromCatchApp.getCatchDataBean().iconUrl, FileServiceImpl.PNG);
			fromCatchApp.setIconUrl(iconUrl);
			String packageName = fromCatchApp.getPackageName();
			if (!parseAPKPackage(null, fromCatchApp, apkUrl)) {
				return null;
			}

			if (!packageName.equals(fromCatchApp.getPackageName())) {
				return null;
			}
			uploadScreenshots(fromCatchApp);
			fromCatchApp.setFirstCatId(cat.getParentId());
			fromCatchApp.setSecondCatId(cat.getId());
			fromCatchApp.setUploadUserId(userId);
			fromCatchApp.setStatus(APPStsutsEnum.ONLINE.getValue());
			return fromCatchApp;
		} catch (Exception e) {
			log.error("mergeAppInfoError", e);
		}
		return null;
	}

	private void uploadScreenshots(AppInfoDO fromCatchApp) {
		String[] screenshots = fromCatchApp.getScreenshots().split(",");
		StringBuilder sb = new StringBuilder();
		for (String s : screenshots) {
			String screenshot = fileService.uploadFile(s, FileServiceImpl.PNG);
			if (!StringUtil.isBlank(screenshot)) {
				sb.append(screenshot);
				sb.append(",");
			}
		}
		String result = sb.toString();
		if (!StringUtil.isBlank(result)) {
			fromCatchApp.setScreenshots(result.substring(0, result.length() - 1));
		}
	}


	private Map<Long, KeyValueDO> getRecommendAppByType(int type) {
		KeyValueQuery query = new KeyValueQuery();
		query.setPageSize(200);
		if (type == 1) {
			query.setKeyName(KeyValueTypeEnum.RECOMMEND_APP_GAME.getKeyName());
		} else {
			query.setKeyName(KeyValueTypeEnum.RECOMMEND_APP_GOOD.getKeyName());
		}

		List<KeyValueDO> keyValueList = keyValueDAO.query(query);
		if (keyValueList == null) {
			return null;
		}

		Map<Long, KeyValueDO> map = new HashMap<Long, KeyValueDO>();
		for (KeyValueDO keyValueDO : keyValueList) {
			Long appId = Long.valueOf(keyValueDO.getValue());
			if (appId > 0) {
				map.put(appId, keyValueDO);
			}
		}
		return map;
	}

	public void setAdminDAO(AdminDAO adminDAO) {
		this.adminDAO = adminDAO;
	}

	public void setAppInfoDAO(AppInfoDAO appInfoDAO) {
		this.appInfoDAO = appInfoDAO;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public void setKeyValueDAO(KeyValueDAO keyValueDAO) {
		this.keyValueDAO = keyValueDAO;
	}

	public void setCatDAO(CatDAO catDAO) {
		this.catDAO = catDAO;
	}

	public void setBannerDAO(BannerDAO bannerDAO) {
		this.bannerDAO = bannerDAO;
	}

}

