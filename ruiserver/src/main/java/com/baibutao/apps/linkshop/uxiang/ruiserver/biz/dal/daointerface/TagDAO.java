
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.TagDO; 


/*
 * @user lsb
 */
public interface TagDAO {

    public long create(TagDO tag);
    
    public void delete(long id);
    
    public void update(TagDO tag);
    
    public TagDO queryById(long id);

}
