package com.baibutao.apps.linkshop.ruiserver.test.dal;

import junit.framework.Assert;

import com.baibutao.apps.linkshop.ruiserver.test.BaseImageserver;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.AdminDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AdminDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  9:38:05 AM</p>
 * <p>作者：聂鹏</p>
 */
public class TestAdmin extends BaseImageserver {

	private AdminDAO adminDAO;

	public void testCreate() {
		AdminDO admin = new AdminDO();
		admin.setUserName("niepeng");
		long id = adminDAO.create(admin);
		Assert.assertTrue(id > 0);
	}

	public void testUpdate() {
		AdminDO admin = new AdminDO();
		admin.setUserName("niepeng");
		long id = adminDAO.create(admin);
		admin.setId(id);

		admin.setNick("sdf");
		adminDAO.update(admin);
	}

	public void testDelete() {
		AdminDO admin = new AdminDO();
		admin.setUserName("niepeng");
		long id = adminDAO.create(admin);
		adminDAO.delete(id);
	}

	public void testQueryById() {
		AdminDO admin = new AdminDO();
		admin.setUserName("niepeng");
		long id = adminDAO.create(admin);
		AdminDO fromDB = adminDAO.queryById(id);
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(admin.getUserName(), fromDB.getUserName());
	}

	public void testQueryByName() {
		AdminDO admin = new AdminDO();
		admin.setUserName("niepeng");
		long id = adminDAO.create(admin);
		AdminDO fromDB = adminDAO.queryByName(admin.getUserName());
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(id, fromDB.getId());
	}
	
	public void setAdminDAO(AdminDAO adminDAO) {
		this.adminDAO = adminDAO;
	}

}

