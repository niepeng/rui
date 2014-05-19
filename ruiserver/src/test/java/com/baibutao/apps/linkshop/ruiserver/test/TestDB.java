package com.baibutao.apps.linkshop.ruiserver.test;

import java.util.List;

import wint.lang.utils.CollectionUtil;

import junit.framework.Assert;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.TestDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.TestDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 4, 2014  12:00:20 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestDB extends BaseImageserver {

	private TestDAO testDAO;

	public void testCreate() {
		TestDO testDO = new TestDO();
		testDO.setName("sdfds");
		long id = testDAO.create(testDO);
		Assert.assertTrue(id > 0);
	}
	
	public void testUpdate() {
		TestDO testDO = new TestDO();
		testDO.setName("sdfds");
		long id = testDAO.create(testDO);
		
		testDO.setId(id);
		testDO.setName("sdfew");
		testDAO.update(testDO);
	}
	
	public void testQueryById() {
		TestDO testDO = new TestDO();
		testDO.setName("sdfds");
		long id = testDAO.create(testDO);
		TestDO fromDB = testDAO.queryById(id);

		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(testDO.getName(), fromDB.getName());
	}
	
	public void testQueryAll() {
		TestDO testDO1 = new TestDO();
		testDO1.setName("sdfds");
		testDAO.create(testDO1);

		TestDO testDO2 = new TestDO();
		testDO2.setName("sdfds222");
		testDAO.create(testDO2);

		List<TestDO> list = testDAO.queryAll();
		Assert.assertTrue(!CollectionUtil.isEmpty(list));

	}
	
	
	public void setTestDAO(TestDAO testDAO) {
		this.testDAO = testDAO;
	}
}

