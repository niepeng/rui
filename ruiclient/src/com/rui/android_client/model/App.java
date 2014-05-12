package com.rui.android_client.model;

import java.util.Calendar;

public class App {

	private long id;
	
	// 链接地址
	private String downUrl;

	// 包名
	private String packageName;

	// 当前版本号
	private String versionName;

	// 版本值，int
	private String versionValue;

	// 文件大小,单位 k
	private int fileSize;

	// 上传时间
	private Calendar uploadDate;

	// 发布时间
	private Calendar publishDate;

	// icon地址
	private String iconUrl;

	// 主标题
	private String mainTitle;

	// 副标题
	private String subTitle;

	// 一级类目
	private long firstCatId;

	// 二级类目
	private long secondCatId;

	// 描述
	private String info;

	// 本次更新内容
	private String updateInfo;

	// 截图s
	private String[] screenshots;

	// 本表自身关联，为了实现更新审核，关联的父appId
	private long referMainAppId;

	// // 本表自身关联，为了实现查看历史审核通过的版本
	// private String historyAppIds;

	// ---- 提交用户操作相关 ----
	private long uploadUserId;

	// ---- 客户端用户操作相关 ----
	// 下载安装数量
	private int downloadNum;

	// 喜欢收藏数量
	private int favNum;

	// 推荐数量
	private int recommendNum;

	// 评论数量
	private int commentNum;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionValue() {
		return versionValue;
	}

	public void setVersionValue(String versionValue) {
		this.versionValue = versionValue;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public Calendar getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Calendar uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Calendar getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Calendar publishDate) {
		this.publishDate = publishDate;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public long getFirstCatId() {
		return firstCatId;
	}

	public void setFirstCatId(long firstCatId) {
		this.firstCatId = firstCatId;
	}

	public long getSecondCatId() {
		return secondCatId;
	}

	public void setSecondCatId(long secondCatId) {
		this.secondCatId = secondCatId;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}

	public String[] getScreenshots() {
		return screenshots;
	}

	public void setScreenshots(String[] screenshots) {
		this.screenshots = screenshots;
	}

	public long getReferMainAppId() {
		return referMainAppId;
	}

	public void setReferMainAppId(long referMainAppId) {
		this.referMainAppId = referMainAppId;
	}

	public long getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(long uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	public int getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(int downloadNum) {
		this.downloadNum = downloadNum;
	}

	public int getFavNum() {
		return favNum;
	}

	public void setFavNum(int favNum) {
		this.favNum = favNum;
	}

	public int getRecommendNum() {
		return recommendNum;
	}

	public void setRecommendNum(int recommendNum) {
		this.recommendNum = recommendNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

}
