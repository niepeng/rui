package com.baibutao.apps.linkshop.ruiserver.test.dal;

import java.util.Date;

import junit.framework.Assert;

import com.baibutao.apps.linkshop.ruiserver.test.BaseImageserver;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.UserDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.UserDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  8:35:04 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestUser extends BaseImageserver {

	private UserDAO userDAO;
	
	private Date date = new Date();

	public void testCreate() {
		UserDO user = new UserDO();
		user.setPhone("sdfsd");
		user.setLastVisitTime(date);
		long id = userDAO.create(user);
		Assert.assertTrue(id > 0);
	}

	public void testUpdate() {
		UserDO user = new UserDO();
		user.setPhone("sdfsd");
		user.setLastVisitTime(date);
		long id = userDAO.create(user);
		user.setId(id);
		userDAO.update(user);
	}

	public void testDelete() {
		UserDO user = new UserDO();
		user.setPhone("sdfsd");
		user.setLastVisitTime(date);
		long id = userDAO.create(user);
		userDAO.delete(id);
	}

	public void testQueryById() {
		UserDO user = new UserDO();
		user.setPhone("sdfsd");
		user.setLastVisitTime(date);
		long id = userDAO.create(user);
		UserDO fromDB = userDAO.queryById(id);
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(user.getPhone(), fromDB.getPhone());
	}
	
	public void testQueryByDeviceId() {
		UserDO user = new UserDO();
		user.setLastVisitTime(date);
		user.setDeviceId("12");
		long id = userDAO.create(user);
		UserDO fromDB = userDAO.queryByDeviceId(user.getDeviceId());
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(id, fromDB.getId());
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}

