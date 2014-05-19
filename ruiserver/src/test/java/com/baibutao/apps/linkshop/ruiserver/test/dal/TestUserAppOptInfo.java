package com.baibutao.apps.linkshop.ruiserver.test.dal;

import junit.framework.Assert;

import com.baibutao.apps.linkshop.ruiserver.test.BaseImageserver;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.UserAppOptInfoDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.UserAppOptInfoDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  8:35:04 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestUserAppOptInfo extends BaseImageserver {

	private UserAppOptInfoDAO userAppOptInfoDAO;

	public void testCreate() {
		UserAppOptInfoDO userAppOptInfo = new UserAppOptInfoDO();
		userAppOptInfo.setComment("comment");
		long id = userAppOptInfoDAO.create(userAppOptInfo);
		Assert.assertTrue(id > 0);
	}

	public void testUpdate() {
		UserAppOptInfoDO userAppOptInfo = new UserAppOptInfoDO();
		userAppOptInfo.setComment("comment");
		long id = userAppOptInfoDAO.create(userAppOptInfo);
		userAppOptInfo.setId(id);
		userAppOptInfoDAO.update(userAppOptInfo);
	}

	public void testDelete() {
		UserAppOptInfoDO userAppOptInfo = new UserAppOptInfoDO();
		userAppOptInfo.setComment("comment");
		long id = userAppOptInfoDAO.create(userAppOptInfo);
		userAppOptInfoDAO.delete(id);
	}

	public void testQueryById() {
		UserAppOptInfoDO userAppOptInfo = new UserAppOptInfoDO();
		userAppOptInfo.setComment("comment");
		long id = userAppOptInfoDAO.create(userAppOptInfo);
		UserAppOptInfoDO fromDB = userAppOptInfoDAO.queryById(id);
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(userAppOptInfo.getComment(), fromDB.getComment());
	}

	public void setUserAppOptInfoDAO(UserAppOptInfoDAO userAppOptInfoDAO) {
		this.userAppOptInfoDAO = userAppOptInfoDAO;
	}

}

