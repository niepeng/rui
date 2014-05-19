package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.JsonAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query.AppQuery;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 12, 2014  9:15:32 PM</p>
 * <p>作者：聂鹏</p>
 */
public class Json extends BaseAction {
	
	private JsonAO jsonAO;
	
	public void version(FlowData flowData, Context context) {
		Result result = jsonAO.version();
		handleJsonResult(result, flowData, context);
	}
	
	/**
	 * @param flowData
	 * @param context
	 */
	public void index(FlowData flowData, Context context) {
		// type=1 游戏精选， type=2 精品推荐， type=3 下载排行
		int type = flowData.getParameters().getInt("type");
		int page = flowData.getParameters().getInt("page");
		Result result = jsonAO.index(type, page);
		handleJsonResult(result, flowData, context);
	}
	
	public void search(FlowData flowData, Context context) {
		String keyword = flowData.getParameters().getString("keyword");
		int page = flowData.getParameters().getInt("page");
		
		AppQuery query = new AppQuery();
		query.setKeyword(keyword);
		query.setPageNo(page);
		Result result = jsonAO.search(query);
		handleJsonResult(result, flowData, context);
	}

	public void setJsonAO(JsonAO jsonAO) {
		this.jsonAO = jsonAO;
	}
	
}

