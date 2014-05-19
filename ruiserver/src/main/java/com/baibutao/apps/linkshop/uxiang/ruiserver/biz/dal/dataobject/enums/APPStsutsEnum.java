package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  10:52:58 AM</p>
 * <p>作者：聂鹏</p>
 */
public enum APPStsutsEnum {

	ONLINE(1,"发布"),
	READY_SAVE(2, "已保存，未提交审核"),
	READY_CHECK(3, "待审核"),
	CHECK_FIAL(4,"审核失败，等待修改"),
	// 但是为了方便操作，这个部分的数据暂时逻辑删除了
	SUCCESS_VERSION_OUT(100,"审核通过，但版本过期");
	
	private final int value;
	
	private final String name;

	private APPStsutsEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public static APPStsutsEnum valueOf(int value) {
		for (APPStsutsEnum e : values()) {
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

