package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action;

import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 24, 2014  2:20:27 PM</p>
 * <p>作者：聂鹏</p>
 */
public class AgreeInfo extends BaseAction {
	
	public void execute(FlowData flowData, Context context) {
		context.put("title", "同意条款");
	}


}

