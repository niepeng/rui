package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 21, 2014  10:11:16 AM</p>
 * <p>作者：聂鹏</p>
 */
public enum KeyValueTypeEnum {
	
	VERSION_UPDATE(1, "version_update","版本升级"),
	FEED_BACK(2, "feddback", "反馈"),
	PERMINSSION_MAP(3, "permission", "权限中文对应"),
	RECOMMEND_APP_GAME(100, "recommend_app_game", "游戏精选"),
	RECOMMEND_APP_GOOD(101, "recommend_app_good", "精品推荐");

	private final int id;
	
	private final String keyName;
	
	private final String meaning;

	private KeyValueTypeEnum(int id, String keyName, String meaning) {
		this.id = id;
		this.keyName = keyName;
		this.meaning = meaning;
	}

	public int getId() {
		return id;
	}
	
	public String getKeyName() {
		return keyName;
	}

	public String getMeaning() {
		return meaning;
	}
}

