package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums;


/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  9:46:36 AM</p>
 * <p>作者：聂鹏</p>
 */
public enum RoleEnum {

	ADMIN(1,"系统管理员"),
	CHECK_OPERATOR(2, "审核操作员"),
	USER(10,"普通用户");
	
	private final int value;
	
	private final String name;

	private RoleEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public static RoleEnum valueOf(int value) {
		for (RoleEnum e : values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
	
	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

}

