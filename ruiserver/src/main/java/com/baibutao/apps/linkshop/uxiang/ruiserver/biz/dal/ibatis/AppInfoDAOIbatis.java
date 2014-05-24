
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.AppInfoDAO; 
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AppInfoDO; 
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.AppQuery;


/*
 * @user lsb
 */
public class  AppInfoDAOIbatis extends BaseIbatisDAO implements AppInfoDAO {

	@Override
    public long create(AppInfoDO appInfo) {
    	return (Long)this.getSqlMapClientTemplate().insert("AppInfoDAO.create", appInfo);
    }
    
	@Override
    public void delete(long id) {
    	this.getSqlMapClientTemplate().update("AppInfoDAO.delete", id);
    }
    
	@Override
    public void update(AppInfoDO appInfo) {
    	this.getSqlMapClientTemplate().update("AppInfoDAO.update", appInfo);
    }
    
	@Override
    public AppInfoDO queryById(long id) {
    	return (AppInfoDO)this.getSqlMapClientTemplate().queryForObject("AppInfoDAO.queryById", id);
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AppInfoDO> query(AppQuery query) {
		query.addWildcardChar();
		Integer count = (Integer) this.getSqlMapClientTemplate().queryForObject("AppInfoDAO.queryCount", query);
		if (count != null) {
			query.setTotalResultCount(count);
		}
		List<AppInfoDO> list = (List<AppInfoDO>) this.getSqlMapClientTemplate().queryForList("AppInfoDAO.query", query);
		query.reduceWildcardChar();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AppInfoDO> queryByPackageName(String packageName) {
		List<AppInfoDO> appinfoList = (List<AppInfoDO>) this.getSqlMapClientTemplate().queryForList("AppInfoDAO.queryByPackageName", packageName);
		return appinfoList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AppInfoDO> queryByPackageNameStatus(String packageName, int status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("packageName", packageName);
		map.put("status", status);
		return (List<AppInfoDO>)this.getSqlMapClientTemplate().queryForList("AppInfoDAO.queryByPackageNameStatus", map);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AppInfoDO> queryByIds(String ids) {
		return (List<AppInfoDO>) this.getSqlMapClientTemplate().queryForList("AppInfoDAO.queryByIds", ids);
	}

}
