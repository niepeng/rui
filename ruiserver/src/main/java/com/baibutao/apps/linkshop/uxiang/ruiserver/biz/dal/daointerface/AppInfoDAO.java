
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface;

import java.util.List;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AppInfoDO; 
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.AppQuery;


/*
 * @user lsb
 */
public interface AppInfoDAO {

    public long create(AppInfoDO appInfo);
    
    public void delete(long id);
    
    public void update(AppInfoDO appInfo);
    
    public AppInfoDO queryById(long id);
    
    public List<AppInfoDO> query(AppQuery query);
    
    public AppInfoDO queryByPackageName(String packageName);
    
    public List<AppInfoDO> queryByIds(String ids);
    
}
