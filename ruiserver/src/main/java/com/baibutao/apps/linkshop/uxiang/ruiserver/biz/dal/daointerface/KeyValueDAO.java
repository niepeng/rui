
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface;

import java.util.List;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.KeyValueDO; 
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.KeyValueTypeEnum;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.KeyValueQuery;


/*
 * @user lsb
 */
public interface KeyValueDAO {

    public long create(KeyValueDO keyValue);
    
    public void delete(long id);
    
    public void update(KeyValueDO keyValue);
    
    public KeyValueDO queryById(long id);
    
    public KeyValueDO queryByKey(String key);
    
    public List<KeyValueDO> queryByType(KeyValueTypeEnum type);
    
    public List<KeyValueDO> query(KeyValueQuery query);

}
