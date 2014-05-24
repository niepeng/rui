package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.impl;

import java.util.Date;
import java.util.List;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultSupport;
import wint.help.biz.result.StringResultCode;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.BaseAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.JsonAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.AppInfoDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.BannerDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.CatDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.KeyValueDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.UserDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AppInfoDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.BannerDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.KeyValueDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.UserDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.KeyValueTypeEnum;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.AppQuery;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.KeyValueQuery;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 12, 2014  9:17:11 PM</p>
 * <p>作者：聂鹏</p>
 */
public class JsonAOImpl extends BaseAO implements JsonAO {
	
	private CatDAO catDAO;
	
	private AppInfoDAO appInfoDAO;
	
	private BannerDAO bannerDAO;
	
	private UserDAO userDAO;
	
	private KeyValueDAO keyValueDAO;
	
	private int default_version_code = 1;
	
	private String default_version_url = "http://static.uxiang.com/uxiang/uxiang-v2.0.0.apk";
	
	public static final int APP_LIST_NUM = 10;

	@Override
	public Result index(int type, int page) {
		Result result = new ResultSupport(false);
		try {
			List<AppInfoDO> appList = getAppListByType(type, page);
			
			// 只有当page=1的时候，获取bannerList
			if(page <= 1) {
				List<BannerDO> bannerList = bannerDAO.listAll();
				result.getModels().put("bannerList", bannerList);
			}
			
			result.getModels().put("appList", appList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("index error", e);
		}
		return result;
	}

	/*
	 * 1是游戏精选，2是精品推荐，3下载排行
	 */
	private List<AppInfoDO> getAppListByType(int type, int page) {
		List<AppInfoDO> appList = null;

		AppQuery query = new AppQuery();
		query.setPageNo(page);
		query.setPageSize(APP_LIST_NUM);

		switch (type) {
		case 1:
			KeyValueQuery keyValueQuery = new KeyValueQuery();
			keyValueQuery.setPageNo(page);
			keyValueQuery.setPageSize(APP_LIST_NUM);
			keyValueQuery.setKeyName(KeyValueTypeEnum.RECOMMEND_APP_GAME.getKeyName());
			appList = AdminAOImpl.getAppListFByKeyValueQuery(keyValueDAO, appInfoDAO, keyValueQuery);
			break;
		case 2:
			KeyValueQuery valueQuery = new KeyValueQuery();
			valueQuery.setPageNo(page);
			valueQuery.setPageSize(APP_LIST_NUM);
			valueQuery.setKeyName(KeyValueTypeEnum.RECOMMEND_APP_GOOD.getKeyName());
			appList = AdminAOImpl.getAppListFByKeyValueQuery(keyValueDAO, appInfoDAO, valueQuery);
			break;
		case 3:
			query.setSort(3);
			appList = appInfoDAO.query(query);
			break;
		default:
			appList = appInfoDAO.query(query);
		}

		return appList;
	}
	
	@Override
	public Result appDetail(long appId) {
		Result result = new ResultSupport(false);
		try {
			AppInfoDO appInfoDO = appInfoDAO.queryById(appId);
			if(appInfoDO == null || !appInfoDO.isOnLine()) {
				result.setResultCode(new StringResultCode("app参数错误:" + appId));
				return result;
			}

			// 根据随机，1/4概率命中，认为是下载数量+1
			if ((int) (Math.random() * 4) == 1) {
				appInfoDO.setDownloadNum(appInfoDO.getDownloadNum() + 1);
				appInfoDAO.update(appInfoDO);
			}
			
			result.getModels().put("appInfoDO", appInfoDO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("appDetail error", e);
		}
		return result;
	}

	@Override
	public Result catList() {
		Result result = new ResultSupport(false);
		try {
			List<CatDO> catList = catDAO.listAll();
			result.getModels().put("catList", catList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("catList error", e);
		}
		return result;
	}
	
	@Override
	public Result appList(AppQuery query) {
		Result result = new ResultSupport(false);
		try {
			CatDO cat = catDAO.queryById(query.getSecondCatId());
			if(cat == null) {
				result.setResultCode(new StringResultCode("参数错误"));
				return result;
			}
			
			query.setPageSize(APP_LIST_NUM);
			List<AppInfoDO> appList = appInfoDAO.query(query);
			result.getModels().put("appList", appList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("appList error", e);
		}
		return result;
	}
	
	@Override
	public Result getApps(String[] packages) {
		Result result = new ResultSupport(true);
		try {
			List<AppInfoDO> appList = CollectionUtil.newArrayList();
			result.getModels().put("appList", appList);

			if (packages == null || packages.length < 1) {
				return result;
			}

			for (String packageName : packages) {
				AppInfoDO appInfoDO = appInfoDAO.queryByPackageName(packageName);
				if (appInfoDO != null) {
					appList.add(appInfoDO);
				}
			}
		} catch (Exception e) {
			log.error("getApps error", e);
		}
		return result;
	}
	
	@Override
	public Result search(AppQuery query) {
		Result result = new ResultSupport(false);
		try {
			query.setPageSize(APP_LIST_NUM);
			List<AppInfoDO> appList = appInfoDAO.query(query);
			result.getModels().put("appList", appList);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("search error", e);
		}
		return result;
	}
	
	@Override
	public Result login(UserDO userDO) {
		Result result = new ResultSupport(false);
		try {
			String deviceId = userDO.getDeviceId();
			if (StringUtil.isBlank(deviceId)) {
				result.setResultCode(new StringResultCode("登陆参数错误"));
				return result;
			}
			
			UserDO user = userDAO.queryByDeviceId(deviceId);
			if (user == null) {
				userDO.setLastVisitTime(new Date());
				long id = userDAO.create(userDO);
				userDO.setId(id);
				result.getModels().put("user", userDO);
			} else {
				user.setLastVisitTime(new Date());
				if (!StringUtil.isBlank(userDO.getPhone())) {
					user.setPhone(userDO.getPhone());
					userDAO.update(user);
				}
				result.getModels().put("user", user);
			}

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("login error", e);
		}
		return result;
	}
	
	@Override
	public Result version() {
		Result result = new ResultSupport(false);
		try {

			KeyValueDO keyValueDO = keyValueDAO.queryByKey(KeyValueTypeEnum.VERSION_UPDATE.getKeyName());
			if (keyValueDO == null) {
				StringBuilder sb = new StringBuilder();
				sb.append("{\"lastAndroidVersion\":");
				sb.append(default_version_code);
				sb.append(",");
				sb.append("\"lastAndroidUrl\":");
				sb.append("\"" + default_version_url + "\"}");
				result.getModels().put("value", sb.toString());
				result.setSuccess(true);
				return result;
			}

			result.getModels().put("value", keyValueDO.getValue());
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("version error", e);
		}
		return result;
	}
	
	@Override
	public Result feedback(UserDO userDO, String content) {
		Result result = new ResultSupport(false);
		try {
			UserDO fromDB = getUser(userDO);
			if (fromDB == null || StringUtil.isBlank(content)) {
				result.setResultCode(new StringResultCode("当前参数错误"));
				return result;
			}

			KeyValueDO keyValue = new KeyValueDO();
			keyValue.setType(KeyValueTypeEnum.FEED_BACK.getId());
			// keyName是用户id
			keyValue.setKeyName(String.valueOf(fromDB.getId()));
			keyValue.setValue(content);
			keyValueDAO.create(keyValue);

			result.setSuccess(true);
		} catch (Exception e) {
			log.error("feedback error", e);
		}
		return result;
	}
	
	
	private UserDO getUser(UserDO userDO) {
		if (userDO.getId() < 1) {
			return null;
		}

		UserDO user = userDAO.queryById(userDO.getId());
		if (user != null && user.getDeviceId().equals(userDO.getDeviceId())) {
			return user;
		}
		return null;
	}

	public void setCatDAO(CatDAO catDAO) {
		this.catDAO = catDAO;
	}

	public void setAppInfoDAO(AppInfoDAO appInfoDAO) {
		this.appInfoDAO = appInfoDAO;
	}

	public void setBannerDAO(BannerDAO bannerDAO) {
		this.bannerDAO = bannerDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setKeyValueDAO(KeyValueDAO keyValueDAO) {
		this.keyValueDAO = keyValueDAO;
	}
	
}

