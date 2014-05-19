package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject;

import java.util.List;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  10:26:35 AM</p>
 * <p>作者：聂鹏</p>
 */
public class CatDO extends BaseDO {
	
	private String name;

	private int level;

	private long parentId;

	private int sortValue;

	private String iconUrl;
	
	// -------------- extend attribute -----------------------
	
	private List<CatDO> childrenList;

	// -------------- normal moethod -------------------------

	// -------------- setter/getter --------------------------

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public List<CatDO> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(List<CatDO> childrenList) {
		this.childrenList = childrenList;
	}
	
	
}

