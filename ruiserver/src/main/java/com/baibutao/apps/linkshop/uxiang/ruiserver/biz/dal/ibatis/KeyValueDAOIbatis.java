
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.ibatis;

import java.util.List;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.KeyValueDAO; 
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.KeyValueDO; 
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.KeyValueTypeEnum;


/*
 * @user lsb
 */
public class KeyValueDAOIbatis extends BaseIbatisDAO implements KeyValueDAO {

	@Override
	public long create(KeyValueDO keyValue) {
		return (Long) this.getSqlMapClientTemplate().insert("KeyValueDAO.create", keyValue);
	}

	@Override
	public void delete(long id) {
		this.getSqlMapClientTemplate().update("KeyValueDAO.delete", id);
	}

	@Override
	public void update(KeyValueDO keyValue) {
		this.getSqlMapClientTemplate().update("KeyValueDAO.update", keyValue);
	}

	@Override
	public KeyValueDO queryById(long id) {
		return (KeyValueDO) this.getSqlMapClientTemplate().queryForObject("KeyValueDAO.queryById", id);
	}
	
	@Override
	public KeyValueDO queryByKey(String key) {
		return (KeyValueDO) this.getSqlMapClientTemplate().queryForObject("KeyValueDAO.queryByKey", key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KeyValueDO> queryByType(KeyValueTypeEnum type) {
		return (List<KeyValueDO> ) this.getSqlMapClientTemplate().queryForList("KeyValueDAO.queryByType", type.getId());

	}

}
