
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.ibatis;

import java.util.List;

import wint.lang.utils.CollectionUtil;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.CatDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO;


/*
 * @user lsb
 */
public class  CatDAOIbatis extends BaseIbatisDAO implements CatDAO {

	@Override
    public long create(CatDO cat) {
    	return (Long)this.getSqlMapClientTemplate().insert("CatDAO.create", cat);
    }
    
	@Override
    public void delete(long id) {
    	this.getSqlMapClientTemplate().update("CatDAO.delete", id);
    }
    
	@Override
    public void update(CatDO cat) {
    	this.getSqlMapClientTemplate().update("CatDAO.update", cat);
    }
    
	@Override
    public CatDO queryById(long id) {
    	return (CatDO)this.getSqlMapClientTemplate().queryForObject("CatDAO.queryById", id);
    }
	
	@Override
	public List<CatDO> listAll() {
		List<CatDO> firstCatList = queryFirstLevel();
		if (CollectionUtil.isEmpty(firstCatList)) {
			return firstCatList;
		}
		for (CatDO cat : firstCatList) {
			cat.setChildrenList(queryByParentId(cat.getId()));
		}
		return firstCatList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CatDO> queryFirstLevel() {
		return (List<CatDO>) this.getSqlMapClientTemplate().queryForList("CatDAO.queryFirstLevel");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CatDO> queryByParentId(long id) {
		return (List<CatDO>) this.getSqlMapClientTemplate().queryForList("CatDAO.queryByParentId", id);
	}

}
