package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject;

/**
 * <p>标题: 用户与app之间有操作过的信息</p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  11:00:06 AM</p>
 * <p>作者：聂鹏</p>
 */
public class UserAppOptInfoDO extends BaseDO {

	private long appId;
	
	// 客户端用户id
	private long userId;
	
	// 是否添加了喜欢, 0未操作，1喜欢
	private int isFav;
	
	// 是否已经下载, 0未操作，1已下载
	private int isDownload;
	
	// 是否添加了推荐, 0未操作，1推荐
	private int isRecommend;
	
	// 评论内容
	private String comment;

	// -------------- extend attribute -----------------------

	// -------------- normal moethod -------------------------

	// -------------- setter/getter --------------------------

	public long getAppId() {
		return appId;
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getIsFav() {
		return isFav;
	}

	public void setIsFav(int isFav) {
		this.isFav = isFav;
	}

	public int getIsDownload() {
		return isDownload;
	}

	public void setIsDownload(int isDownload) {
		this.isDownload = isDownload;
	}

	public int getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(int isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}

