
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.ibatis;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.UserAppOptInfoDAO; 
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.UserAppOptInfoDO; 


/*
 * @user lsb
 */
public class  UserAppOptInfoDAOIbatis extends BaseIbatisDAO implements UserAppOptInfoDAO {

	@Override
    public long create(UserAppOptInfoDO userAppOptInfo) {
    	return (Long)this.getSqlMapClientTemplate().insert("UserAppOptInfoDAO.create", userAppOptInfo);
    }
    
	@Override
    public void delete(long id) {
    	this.getSqlMapClientTemplate().update("UserAppOptInfoDAO.delete", id);
    }
    
	@Override
    public void update(UserAppOptInfoDO userAppOptInfo) {
    	this.getSqlMapClientTemplate().update("UserAppOptInfoDAO.update", userAppOptInfo);
    }
    
	@Override
    public UserAppOptInfoDO queryById(long id) {
    	return (UserAppOptInfoDO)this.getSqlMapClientTemplate().queryForObject("UserAppOptInfoDAO.queryById", id);
    }

}
