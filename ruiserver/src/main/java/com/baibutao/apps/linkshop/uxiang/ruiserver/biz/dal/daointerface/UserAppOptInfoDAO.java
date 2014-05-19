
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.UserAppOptInfoDO; 


/*
 * @user lsb
 */
public interface UserAppOptInfoDAO {

    public long create(UserAppOptInfoDO userAppOptInfo);
    
    public void delete(long id);
    
    public void update(UserAppOptInfoDO userAppOptInfo);
    
    public UserAppOptInfoDO queryById(long id);

}
