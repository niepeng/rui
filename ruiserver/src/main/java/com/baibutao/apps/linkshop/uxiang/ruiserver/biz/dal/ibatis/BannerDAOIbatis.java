
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.ibatis;

import java.util.List;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.BannerDAO; 
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.BannerDO; 


/*
 * @user lsb
 */
public class BannerDAOIbatis extends BaseIbatisDAO implements BannerDAO {

	@Override
	public long create(BannerDO banner) {
		return (Long) this.getSqlMapClientTemplate().insert("BannerDAO.create", banner);
	}

	@Override
	public void delete(long id) {
		this.getSqlMapClientTemplate().update("BannerDAO.delete", id);
	}

	@Override
	public void update(BannerDO banner) {
		this.getSqlMapClientTemplate().update("BannerDAO.update", banner);
	}

	@Override
	public BannerDO queryById(long id) {
		return (BannerDO) this.getSqlMapClientTemplate().queryForObject("BannerDAO.queryById", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BannerDO> listAll() {
		return this.getSqlMapClientTemplate().queryForList("BannerDAO.listAll");
	}

}
