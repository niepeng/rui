
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface;

import java.util.List;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO; 


/*
 * @user lsb
 */
public interface CatDAO {

    public long create(CatDO cat);
    
    public void delete(long id);
    
    public void update(CatDO cat);
    
    public CatDO queryById(long id);
    
    public List<CatDO> listAll();
    
    public List<CatDO> queryFirstLevel();
    
    public List<CatDO> queryByParentId(long id);

}
