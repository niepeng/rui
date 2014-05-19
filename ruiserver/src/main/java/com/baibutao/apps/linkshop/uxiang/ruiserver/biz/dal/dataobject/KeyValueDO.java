package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  10:08:53 AM</p>
 * <p>作者：聂鹏</p>
 */
public class KeyValueDO extends BaseDO {

	private String keyName;
	
	private String value;
	
	// KeyValueTypeEnum
	private int type;
	
	// -------------- extend attribute -----------------------

	// -------------- normal moethod -------------------------

	// -------------- setter/getter --------------------------


	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}

