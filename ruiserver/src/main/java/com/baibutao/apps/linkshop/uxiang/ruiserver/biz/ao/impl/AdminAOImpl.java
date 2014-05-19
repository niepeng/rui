package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

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
	public Result addAppFirst(FlowData flowData) {
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
			/*
			 * 1. 解析出包名以及版本 
			 * 2. 原有系统中不存在的，app
			 */
			if(!StringUtil.isBlank(visitUrl)) {
				String fileName = visitUrl.substring(visitUrl.lastIndexOf("/")+ 1);
				File apkFile = fileService.getFileByName(fileName);
				if (apkFile.exists()) {
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
					AppInfoDO fromDB = appInfoDAO.queryByPackageName(appInfoDO.getPackageName());
					if (fromDB != null) {
						result.setResultCode(new StringResultCode("该app已经存在"));
						return result;
					}
				}
				appInfoDO.setDownUrl(visitUrl);
			}
			
			long id = appInfoDAO.create(appInfoDO);
			
			result.getModels().put("appId", id);
			result.setSuccess(true);

		} catch (Exception e) {
			log.error("addAppFirst error", e);
		}
		return result;
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
			if (fromDB == null || (!isAdmin(flowData) && getLoginUserId(flowData) != fromDB.getUploadUserId())) {
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

