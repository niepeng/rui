package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface;

import java.util.List;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.TestDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 4, 2014  12:03:54 PM</p>
 * <p>作者：聂鹏</p>
 */
public interface TestDAO {

	public long create(TestDO testDO);
	
	public TestDO queryById(long id);
	
	public void update(TestDO testDO);
	
	public List<TestDO> queryAll();
}

