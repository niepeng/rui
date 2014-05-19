
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AdminDO; 


/*
 * @user lsb
 */
public interface AdminDAO {

    public long create(AdminDO admin);
    
    public void delete(long id);
    
    public void update(AdminDO admin);
    
    public AdminDO queryById(long id);
    
    public AdminDO queryByName(String userName);

}
