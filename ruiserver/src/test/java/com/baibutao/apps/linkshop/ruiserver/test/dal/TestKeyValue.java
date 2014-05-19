package com.baibutao.apps.linkshop.ruiserver.test.dal;

import java.util.List;

import wint.lang.utils.CollectionUtil;

import junit.framework.Assert;

import com.baibutao.apps.linkshop.ruiserver.test.BaseImageserver;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.KeyValueDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.KeyValueDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.KeyValueTypeEnum;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  8:35:04 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestKeyValue extends BaseImageserver {
	
	private KeyValueDAO keyValueDAO;
	
	public void testCreate() {
		KeyValueDO keyValue = new KeyValueDO();
		keyValue.setKeyName("keyname");
		long id = keyValueDAO.create(keyValue);
		Assert.assertTrue(id > 0);
	}
	
	public void testUpdate() {
		KeyValueDO keyValue = new KeyValueDO();
		keyValue.setKeyName("keyname");
		long id = keyValueDAO.create(keyValue);
		keyValue.setId(id);
		
		keyValueDAO.update(keyValue);
	}
	
	public void testDelete() {
		KeyValueDO keyValue = new KeyValueDO();
		keyValue.setKeyName("keyname");
		long id = keyValueDAO.create(keyValue);
		keyValueDAO.delete(id);
	}
	
	public void testQueryById() {
		KeyValueDO keyValue = new KeyValueDO();
		keyValue.setKeyName("keyname");
		long id = keyValueDAO.create(keyValue);
		KeyValueDO fromDB = keyValueDAO.queryById(id);
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(keyValue.getKeyName(), fromDB.getKeyName());
	}
	
	public void testQueryByType() {
		List<KeyValueDO> list = keyValueDAO.queryByType(KeyValueTypeEnum.PERMINSSION_MAP);
		Assert.assertTrue(!CollectionUtil.isEmpty(list));
	}
	
	public void testQueryByKey() {
		String key = "android.permission.ACCESS_WIFI_STATE";
		KeyValueDO fromDB = keyValueDAO.queryByKey(key);
		Assert.assertTrue(fromDB != null);
		Assert.assertEquals(KeyValueTypeEnum.PERMINSSION_MAP.getId(), fromDB.getType());
	}

	public void setKeyValueDAO(KeyValueDAO keyValueDAO) {
		this.keyValueDAO = keyValueDAO;
	}
	
}

