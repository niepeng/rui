
package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface;

import java.util.List;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.BannerDO; 


/*
 * @user lsb
 */
public interface BannerDAO {

    public long create(BannerDO banner);
    
    public void delete(long id);
    
    public void update(BannerDO banner);
    
    public BannerDO queryById(long id);
    
    public List<BannerDO> listAll();

}
