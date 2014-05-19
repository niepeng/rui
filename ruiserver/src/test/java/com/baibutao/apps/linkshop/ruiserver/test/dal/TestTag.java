package com.baibutao.apps.linkshop.ruiserver.test.dal;

import junit.framework.Assert;

import com.baibutao.apps.linkshop.ruiserver.test.BaseImageserver;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.TagDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.TagDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  8:35:04 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestTag extends BaseImageserver {
	
	private TagDAO tagDAO;
	
	public void testCreate() {
		TagDO tag = new TagDO();
		tag.setName("tagName");
		long id = tagDAO.create(tag);
		Assert.assertTrue(id > 0);
	}
	
	public void testUpdate() {
		TagDO tag = new TagDO();
		tag.setName("tagName");
		long id = tagDAO.create(tag);
		tag.setId(id);
		tagDAO.update(tag);
	}
	
	public void testDelete() {
		TagDO tag = new TagDO();
		tag.setName("tagName");
		long id = tagDAO.create(tag);
		tagDAO.delete(id);
	}
	
	public void testQueryById() {
		TagDO tag = new TagDO();
		tag.setName("tagName");
		long id = tagDAO.create(tag);
		TagDO fromDB = tagDAO.queryById(id);
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(tag.getName(), fromDB.getName());
	}

	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}
	
}

