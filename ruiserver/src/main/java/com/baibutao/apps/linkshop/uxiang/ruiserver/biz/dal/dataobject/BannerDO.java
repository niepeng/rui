package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  10:27:45 AM</p>
 * <p>作者：聂鹏</p>
 */
public class BannerDO extends BaseDO {

	private String name;
	
	// 图片地址
	private String imageUrl;
	
	// 关联app
	private long relationAppId;
	
	// 链接地址
	private String linkUrl;
	
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public long getRelationAppId() {
		return relationAppId;
	}

	public void setRelationAppId(long relationAppId) {
		this.relationAppId = relationAppId;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}
	
}

