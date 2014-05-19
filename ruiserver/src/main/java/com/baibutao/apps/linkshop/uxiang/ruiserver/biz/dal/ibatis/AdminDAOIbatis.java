
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.ibatis;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.AdminDAO; 
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AdminDO; 


/*
 * @user lsb
 */
public class AdminDAOIbatis extends BaseIbatisDAO implements AdminDAO {

	@Override
	public long create(AdminDO admin) {
		return (Long) this.getSqlMapClientTemplate().insert("AdminDAO.create", admin);
	}

	@Override
	public void delete(long id) {
		this.getSqlMapClientTemplate().update("AdminDAO.delete", id);
	}

	@Override
	public void update(AdminDO admin) {
		this.getSqlMapClientTemplate().update("AdminDAO.update", admin);
	}

	@Override
	public AdminDO queryById(long id) {
		return (AdminDO) this.getSqlMapClientTemplate().queryForObject("AdminDAO.queryById", id);
	}

	@Override
	public AdminDO queryByName(String userName) {
		return (AdminDO) this.getSqlMapClientTemplate().queryForObject("AdminDAO.queryByName", userName);
	}
}
