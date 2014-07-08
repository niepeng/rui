package com.baibutao.apps.linkshop.ruiserver.test.dal;

import java.util.List;

import wint.lang.utils.CollectionUtil;

import junit.framework.Assert;

import com.baibutao.apps.linkshop.ruiserver.test.BaseImageserver;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.CatDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  8:35:04 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestCat extends BaseImageserver {
	
	private CatDAO catDAO;
	
	public void testCreate() {
		CatDO cat = new CatDO();
		cat.setName("sdf");
		long id = catDAO.create(cat);
		Assert.assertTrue(id > 0);
	}
	
	public void testUpdate() {
		CatDO cat = new CatDO();
		cat.setName("sdf");
		long id = catDAO.create(cat);
		cat.setId(id);
		
		catDAO.update(cat);
		
	}
	
	public void testDelete() {
		CatDO cat = new CatDO();
		cat.setName("sdf");
		long id = catDAO.create(cat);
		catDAO.delete(id);
	}
	
	public void testQueryById() {
		CatDO cat = new CatDO();
		cat.setName("sdf");
		long id = catDAO.create(cat);
		CatDO fromDB = catDAO.queryById(id);
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(cat.getName(), fromDB.getName());
	}
	
	public void testQueryByName() {
		String name = "应用";
		List<CatDO> list = catDAO.queryByName(name);
		Assert.assertTrue(!CollectionUtil.isEmpty(list));
	}
	
	public void testQueryFirstLevel() {
		List<CatDO> catList = catDAO.queryFirstLevel();
		Assert.assertTrue(!CollectionUtil.isEmpty(catList));
	}
	
	public void testQueryByParentId() {
		List<CatDO> catList = catDAO.queryByParentId(1L);
		Assert.assertTrue(!CollectionUtil.isEmpty(catList));
	}
	
	public void testListAll() {
		List<CatDO> catList = catDAO.listAll();
		Assert.assertTrue(!CollectionUtil.isEmpty(catList));
	}

	public void setCatDAO(CatDAO catDAO) {
		this.catDAO = catDAO;
	}
	
}

