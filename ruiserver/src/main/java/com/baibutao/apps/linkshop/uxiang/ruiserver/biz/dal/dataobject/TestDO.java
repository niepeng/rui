package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject;

import java.io.Serializable;

public class TestDO extends BaseDO implements Serializable {
	
	private static final long serialVersionUID = -8211677597972645535L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
