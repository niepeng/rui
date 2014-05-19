
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.ibatis;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.TagDAO; 
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.TagDO; 


/*
 * @user lsb
 */
public class  TagDAOIbatis extends BaseIbatisDAO implements TagDAO {

	@Override
    public long create(TagDO tag) {
    	return (Long)this.getSqlMapClientTemplate().insert("TagDAO.create", tag);
    }
    
	@Override
    public void delete(long id) {
    	this.getSqlMapClientTemplate().update("TagDAO.delete", id);
    }
    
	@Override
    public void update(TagDO tag) {
    	this.getSqlMapClientTemplate().update("TagDAO.update", tag);
    }
    
	@Override
    public TagDO queryById(long id) {
    	return (TagDO)this.getSqlMapClientTemplate().queryForObject("TagDAO.queryById", id);
    }

}
