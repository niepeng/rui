package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject;

import java.util.Date;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  10:31:44 AM</p>
 * <p>作者：聂鹏</p>
 */
public class UserDO extends BaseDO {

	// 设备id
	private String deviceId;
	
	// 手机型号
	private String phoneModule;
	
	private Date lastVisitTime;
	
	private int width;
	
	private int height;
	
	private String phone;
	
	// -------------- extend attribute -----------------------

	// -------------- normal moethod -------------------------

	// -------------- setter/getter --------------------------


	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPhoneModule() {
		return phoneModule;
	}

	public void setPhoneModule(String phoneModule) {
		this.phoneModule = phoneModule;
	}

	public Date getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(Date lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}

