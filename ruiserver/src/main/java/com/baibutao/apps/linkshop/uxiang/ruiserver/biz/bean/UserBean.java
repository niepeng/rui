package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.bean;

import java.io.Serializable;

public class UserBean implements Serializable {

	private static final long serialVersionUID = -1976628058640345526L;

	private String nick;
	
	private String password;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}