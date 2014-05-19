
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.UserDO; 


/*
 * @user lsb
 */
public interface UserDAO {

    public long create(UserDO user);
    
    public void delete(long id);
    
    public void update(UserDO user);
    
    public UserDO queryById(long id);
    
    public UserDO queryByDeviceId(String deviceId);
    
}
