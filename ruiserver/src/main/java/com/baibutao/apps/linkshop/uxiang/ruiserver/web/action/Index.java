package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultSupport;
import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 20, 2014  11:01:36 PM</p>
 * <p>作者：聂鹏</p>
 */
public class Index extends BaseAction {
	
	public void execute(FlowData flowData, Context context) {
		Result result = new ResultSupport(true);
		addLoginInfo(result, flowData);
		handleResult(result, flowData, context);
	}

}

