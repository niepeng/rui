package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  10:05:44 AM</p>
 * <p>作者：聂鹏</p>
 */
public class TagDO extends BaseDO {

	private String name;
	
	private String info;
	
	private String iconUrl;
	
	// 排序值
	private int sortValue;

	// -------------- extend attribute -----------------------

	// -------------- normal moethod -------------------------

	// -------------- setter/getter --------------------------

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}
	
}

