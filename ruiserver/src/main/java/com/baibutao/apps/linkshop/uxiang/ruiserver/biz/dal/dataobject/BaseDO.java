package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject;

import java.util.Date;

public class BaseDO {
	
	private long id;
	
	private Date gmtCreate;
	
	private Date gmtModified;

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
