package com.baibutao.apps.linkshop.ruiserver.test.dal;

import junit.framework.TestCase;
import wint.help.tools.ibatis.IbatisGenUtil;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AppInfoDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.BannerDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.KeyValueDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.TagDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.UserAppOptInfoDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.UserDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 20, 2014  11:27:05 PM</p>
 * <p>作者：聂鹏</p>
 */
public class GenSql extends TestCase {
	
	private String sqlPrefix = "/Volumes/util/code/rui/ruiserver/src/main/resources/sqlmaps/";
	
	private String daoPrefix = "/Volumes/util/code/rui/ruiserver/src/main/java/com/baibutao/apps/linkshop/uxiang/ruiserver/biz/dal/";
	
	private String DB_PREFIX = "rui_db_";
	
//	public void testAdmin() {
//		IbatisGenUtil.genIbatisDaoToFile(daoPrefix, DB_PREFIX, AdminDO.class);
//		IbatisGenUtil.genSqlMapToConsole(sqlPrefix + "sqlmap-admin.xml", DB_PREFIX, AdminDO.class);
//		IbatisGenUtil.genCreateTableSqlToConsole(DB_PREFIX, AdminDO.class);
//	}
	
	public void testAppInfo() {
		IbatisGenUtil.genIbatisDaoToFile(daoPrefix, DB_PREFIX, AppInfoDO.class);
		IbatisGenUtil.genSqlMapToConsole(sqlPrefix + "sqlmap-app-info.xml", DB_PREFIX, AppInfoDO.class);
		IbatisGenUtil.genCreateTableSqlToConsole(DB_PREFIX, AppInfoDO.class);
	}
	
	public void testBanner() {
		IbatisGenUtil.genIbatisDaoToFile(daoPrefix, DB_PREFIX, BannerDO.class);
		IbatisGenUtil.genSqlMapToConsole(sqlPrefix + "sqlmap-banner.xml", DB_PREFIX, BannerDO.class);
		IbatisGenUtil.genCreateTableSqlToConsole(DB_PREFIX, BannerDO.class);
	}
	
	public void testCat() {
		IbatisGenUtil.genIbatisDaoToFile(daoPrefix, DB_PREFIX, CatDO.class);
		IbatisGenUtil.genSqlMapToConsole(sqlPrefix + "sqlmap-cat.xml", DB_PREFIX, CatDO.class);
		IbatisGenUtil.genCreateTableSqlToConsole(DB_PREFIX, CatDO.class);
	}
	
	public void testKeyValue() {
		IbatisGenUtil.genIbatisDaoToFile(daoPrefix, DB_PREFIX, KeyValueDO.class);
		IbatisGenUtil.genSqlMapToConsole(sqlPrefix + "sqlmap-key-value.xml", DB_PREFIX, KeyValueDO.class);
		IbatisGenUtil.genCreateTableSqlToConsole(DB_PREFIX, KeyValueDO.class);
	}
	
	public void testTag() {
		IbatisGenUtil.genIbatisDaoToFile(daoPrefix, DB_PREFIX, TagDO.class);
		IbatisGenUtil.genSqlMapToConsole(sqlPrefix + "sqlmap-tag.xml", DB_PREFIX, TagDO.class);
		IbatisGenUtil.genCreateTableSqlToConsole(DB_PREFIX, TagDO.class);
	}
	
	public void testUserAppOptInfo() {
		IbatisGenUtil.genIbatisDaoToFile(daoPrefix, DB_PREFIX, UserAppOptInfoDO.class);
		IbatisGenUtil.genSqlMapToConsole(sqlPrefix + "sqlmap-user-app-opt-info.xml", DB_PREFIX, UserAppOptInfoDO.class);
		IbatisGenUtil.genCreateTableSqlToConsole(DB_PREFIX, UserAppOptInfoDO.class);
	}
	
	public void testUser() {
		IbatisGenUtil.genIbatisDaoToFile(daoPrefix, DB_PREFIX, UserDO.class);
		IbatisGenUtil.genSqlMapToConsole(sqlPrefix + "sqlmap-user.xml", DB_PREFIX, UserDO.class);
		IbatisGenUtil.genCreateTableSqlToConsole(DB_PREFIX, UserDO.class);
	}
	
}

