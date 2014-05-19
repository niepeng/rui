package com.baibutao.apps.linkshop.ruiserver.test.dal;

import junit.framework.Assert;

import com.baibutao.apps.linkshop.ruiserver.test.BaseImageserver;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.BannerDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.BannerDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  8:45:02 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestBanner extends BaseImageserver {
	
	private BannerDAO bannerDAO;
	
	public void testCreate() {
		BannerDO bannerDO = new BannerDO();
		bannerDO.setName("banner");
		
		long id = bannerDAO.create(bannerDO);
		Assert.assertTrue(id > 0);
	}
	
	public void testUpdate() {
		BannerDO bannerDO = new BannerDO();
		bannerDO.setName("banner");
		long id = bannerDAO.create(bannerDO);
		bannerDO.setId(id);
		
		bannerDAO.update(bannerDO);
	}
	
	public void testDelete() {
		BannerDO bannerDO = new BannerDO();
		bannerDO.setName("banner");
		long id = bannerDAO.create(bannerDO);
		bannerDAO.delete(id);
	}
	
	public void testQueryById() {
		BannerDO bannerDO = new BannerDO();
		bannerDO.setName("banner");
		long id = bannerDAO.create(bannerDO);
		
		BannerDO fromDB = bannerDAO.queryById(id);
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(bannerDO.getName(), fromDB.getName());
	}

	public void setBannerDAO(BannerDAO bannerDAO) {
		this.bannerDAO = bannerDAO;
	}
	
}

