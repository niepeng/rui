package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.json;

import wint.help.biz.result.Result;
import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.JsonAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.UserDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.action.BaseAction;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: May 15, 2014  5:47:50 PM</p>
 * <p>作者：聂鹏</p>
 */
public class User extends BaseAction {

	private JsonAO jsonAO;

	public void login(FlowData flowData, Context context) {
		String deviceId = flowData.getParameters().getString("deviceId");
		String phone = flowData.getParameters().getString("phone");
		String phoneModule = flowData.getParameters().getString("phoneModule");
		int width = flowData.getParameters().getInt("width");
		int height = flowData.getParameters().getInt("height");
		
		UserDO userDO = new UserDO();
		userDO.setDeviceId(deviceId);
		userDO.setPhone(phone);
		userDO.setPhoneModule(phoneModule);
		userDO.setWidth(width);
		userDO.setHeight(height);
		
		Result result = jsonAO.login(userDO);
		handleJsonResult(result, flowData, context);
	}
	
	public void feedback(FlowData flowData, Context context) {
		String deviceId = flowData.getParameters().getString("deviceId");
		long userId = flowData.getParameters().getLong("userId");
		String content = flowData.getParameters().getString("content");
		
		UserDO userDO = new UserDO();
		userDO.setDeviceId(deviceId);
		userDO.setId(userId);
		
		Result result = jsonAO.feedback(userDO, content);
		handleJsonResult(result, flowData, context);
	}
	
	

	
	public void setJsonAO(JsonAO jsonAO) {
		this.jsonAO = jsonAO;
	}

}

