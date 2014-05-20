package com.baibutao.apps.linkshop.ruiserver.test.dal;

import java.util.List;

import wint.lang.utils.CollectionUtil;

import junit.framework.Assert;

import com.baibutao.apps.linkshop.ruiserver.test.BaseImageserver;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.AppInfoDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AppInfoDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.AppQuery;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  8:34:40 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestAppInfo  extends BaseImageserver {

	private AppInfoDAO appInfoDAO;
	
	public void testCreate() {
		AppInfoDO appInfo = new AppInfoDO();
		appInfo.setMainTitle("appname");
		long id = appInfoDAO.create(appInfo);
		Assert.assertTrue(id > 0);
	}
	
	public void testUpdate() {
		AppInfoDO appInfo = new AppInfoDO();
		appInfo.setMainTitle("appname");
		long id = appInfoDAO.create(appInfo);
		appInfo.setId(id);
		appInfoDAO.update(appInfo);
	}
	
	public void testDelete() {
		AppInfoDO appInfo = new AppInfoDO();
		appInfo.setMainTitle("appname");
		long id = appInfoDAO.create(appInfo);
		appInfoDAO.delete(id);
	}
	
	public void testQueryById() {
		AppInfoDO appInfo = new AppInfoDO();
		appInfo.setMainTitle("appname");
		long id = appInfoDAO.create(appInfo);
		
		AppInfoDO fromDB = appInfoDAO.queryById(id);
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(appInfo.getMainTitle(), fromDB.getMainTitle());
	}
	
	public void testQuery() {
		AppInfoDO appInfo = new AppInfoDO();
		appInfo.setMainTitle("appname");
		appInfo.setUploadUserId(1L);
		appInfoDAO.create(appInfo);
		
		AppQuery query = new AppQuery();
		query.setUserId(appInfo.getUploadUserId());
		List<AppInfoDO> list = appInfoDAO.query(query);
		Assert.assertTrue(!CollectionUtil.isEmpty(list));
	}
	
	public void testQueryByPackageName() {
		AppInfoDO appInfoDO = new AppInfoDO();
		appInfoDO.setPackageName("a.b.c");
		long id = appInfoDAO.create(appInfoDO);
		AppInfoDO fromDB = appInfoDAO.queryByPackageName(appInfoDO.getPackageName());
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(fromDB.getId(), id);
	}

	public void setAppInfoDAO(AppInfoDAO appInfoDAO) {
		this.appInfoDAO = appInfoDAO;
	}
	
}
