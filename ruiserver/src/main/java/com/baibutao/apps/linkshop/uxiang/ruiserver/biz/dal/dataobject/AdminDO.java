package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.RoleEnum;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  9:31:02 AM</p>
 * <p>作者：聂鹏</p>
 */
public class AdminDO extends BaseDO {

	// 账号
	private String userName;

	// 密码
	private String psw;

	// 昵称
	private String nick;

	// com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.RoleEnum
	private int roleId;

	private String info;
	
	private String email;
	
	private String qq;
	
	// 联系电话
	private String phone;

	// -------------- extend attribute -----------------------
	
	// 新密码
	private String psw1;
	
	// 确认密码
	private String psw2;

	// -------------- normal moethod -------------------------
	
	// 是否是admin用户
	public boolean isAdmin() {
		return RoleEnum.ADMIN.getValue() == roleId;
	}
	
	// -------------- setter/getter --------------------------

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPsw1() {
		return psw1;
	}

	public void setPsw1(String psw1) {
		this.psw1 = psw1;
	}

	public String getPsw2() {
		return psw2;
	}

	public void setPsw2(String psw2) {
		this.psw2 = psw2;
	}
	
}

