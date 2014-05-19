package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.ibatis;

import java.util.List;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.TestDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.TestDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 4, 2014  12:05:31 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestDAOIbatis extends BaseIbatisDAO implements TestDAO {

	@Override
	public long create(TestDO testDO) {
		return (Long) this.getSqlMapClientTemplate().insert("TestDAO.create", testDO);
	}

	@Override
	public TestDO queryById(long id) {
		return (TestDO) this.getSqlMapClientTemplate().queryForObject("TestDAO.queryById", id);
	}

	@Override
	public void update(TestDO testDO) {
		this.getSqlMapClientTemplate().update("TestDAO.update", testDO);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TestDO> queryAll() {
		return (List<TestDO>) this.getSqlMapClientTemplate().queryForList("TestDAO.queryAll");

	}

}

