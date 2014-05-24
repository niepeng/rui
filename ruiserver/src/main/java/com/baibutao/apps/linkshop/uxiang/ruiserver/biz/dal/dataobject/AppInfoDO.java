package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject;

import java.util.Date;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.APPStsutsEnum;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  10:36:07 AM</p>
 * <p>作者：聂鹏</p>
 */
public class AppInfoDO extends BaseDO implements Cloneable {
	
	/*
	 * 当前状态：
	 * com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.APPStsutsEnum
	 */
	private int status;

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
	
	private String permissionValue;
	
	// 上传时间
	private Date uploadDate;
	
	// 发布时间
	private Date publishDate;
	
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
	private String screenshots;
	
	// 本表自身关联，父appId
	private long referMainAppId;
	
	// 本表自身关联，下一个版本的appId
	private long nextAppId;
	
//	// 本表自身关联，为了实现查看历史审核通过的版本
//	private String historyAppIds;
	
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
	
	// -------------- extend attribute -----------------------

	// 当前是否为推荐app
	private boolean recommendApp;
	
	// -------------- normal moethod -------------------------
	
	public boolean isOnLine() {
		return status == APPStsutsEnum.ONLINE.getValue();
	}
	
	public boolean isTimeOut() {
		return status == APPStsutsEnum.SUCCESS_VERSION_OUT.getValue();
	}
	
	/*
	 * 该app是否有下一个版本，已经提交
	 */
	public boolean hasNextAppId() {
		return nextAppId > 0;
	}
	
	
	public AppInfoDO clone() {
		try {
			return (AppInfoDO) super.clone();
		} catch (Exception e) {
			return null;
		}
	}
	
	// -------------- setter/getter --------------------------

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

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
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

	public String getScreenshots() {
		return screenshots;
	}

	public void setScreenshots(String screenshots) {
		this.screenshots = screenshots;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getReferMainAppId() {
		return referMainAppId;
	}

	public void setReferMainAppId(long referMainAppId) {
		this.referMainAppId = referMainAppId;
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

	public long getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(long uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public String getPermissionValue() {
		return permissionValue;
	}

	public void setPermissionValue(String permissionValue) {
		this.permissionValue = permissionValue;
	}

	public boolean isRecommendApp() {
		return recommendApp;
	}

	public void setRecommendApp(boolean recommendApp) {
		this.recommendApp = recommendApp;
	}

	public long getNextAppId() {
		return nextAppId;
	}

	public void setNextAppId(long nextAppId) {
		this.nextAppId = nextAppId;
	}
	
}

