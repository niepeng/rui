package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao;

import wint.help.biz.result.Result;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.UserDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.AppQuery;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 12, 2014  9:16:07 PM</p>
 * <p>作者：聂鹏</p>
 */
public interface JsonAO {

	public Result index(int type, int page);
	
	public Result catList();
	
	public Result appDetail(long appId);
	
	public Result appList(AppQuery query);
	
	/**
	 * 根据app包名，获取app信息
	 */
	public Result getApps(String[] packages);
	
	public Result search(AppQuery query);
	
	public Result login(UserDO userDO);
	
	public Result version();
	
	public Result feedback(UserDO userDO, String content);
}

