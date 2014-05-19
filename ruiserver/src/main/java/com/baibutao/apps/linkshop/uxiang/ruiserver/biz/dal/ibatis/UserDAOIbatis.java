package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.ibatis;

import java.util.List;

import wint.lang.utils.CollectionUtil;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.UserDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.UserDO;

/*
 * @user lsb
 */
public class UserDAOIbatis extends BaseIbatisDAO implements UserDAO {

	@Override
	public long create(UserDO user) {
		return (Long) this.getSqlMapClientTemplate().insert("UserDAO.create", user);
	}

	@Override
	public void delete(long id) {
		this.getSqlMapClientTemplate().update("UserDAO.delete", id);
	}

	@Override
	public void update(UserDO user) {
		this.getSqlMapClientTemplate().update("UserDAO.update", user);
	}

	@Override
	public UserDO queryById(long id) {
		return (UserDO) this.getSqlMapClientTemplate().queryForObject("UserDAO.queryById", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserDO queryByDeviceId(String deviceId) {
		List<UserDO> list = (List<UserDO>) this.getSqlMapClientTemplate().queryForList("UserDAO.queryByDeviceId", deviceId);
		if (!CollectionUtil.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
}
