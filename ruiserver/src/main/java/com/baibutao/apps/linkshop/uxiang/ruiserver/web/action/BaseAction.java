package com.baibutao.apps.linkshop.uxiang.ruiserver.web.action;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultCode;
import wint.help.biz.result.results.CommonResultCodes;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.template.Context;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.RoleEnum;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.common.AdminResultCodes;
import com.baibutao.apps.linkshop.uxiang.ruiserver.web.common.SessionKeys;


/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 20, 2014  11:02:59 PM</p>
 * <p>作者：聂鹏</p>
 */
public class BaseAction {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	public static final String JSON_TYPE = "application/json;charset=UTF-8";

	
	protected boolean checkSessionNeedRedrect(FlowData flowData, Context context) {
		boolean isSuccess = addLoginInfo(flowData, context);
		if(!isSuccess) {
			flowData.redirectTo("adminModule", "quit");
			return false;
		}
		return true;
	}

	protected boolean addLoginInfo(FlowData flowData, Context context) {
		HttpSession session = flowData.getSession();
		String userName = (String) session.getAttribute(SessionKeys.USER_NAME);
		if (StringUtil.isBlank(userName)) {
			return false;
		}
		context.put(SessionKeys.USER_NAME, userName);
		context.put(SessionKeys.USER_ID, session.getAttribute(SessionKeys.USER_ID));
		context.put(SessionKeys.ROLE_ID, session.getAttribute(SessionKeys.ROLE_ID));
		return true;
	}
	
	
	protected void handleResult(Result result, FlowData flowData, Context context) {
		if(!result.isSuccess()) {
		    result2Context(result, context);
			handleError(result, flowData, context);
			return;
		}
		result2Context(result, context);
	}
	
	protected void handleJsonResult(Result result, FlowData flowData, Context context) {
		flowData.setContentType(JSON_TYPE);
		if(!result.isSuccess()) {
			handleJsonError(result, flowData, context);
			return;
		}
		result2Context(result, context);
	}
	
	protected void handleJsonError(Result result, FlowData flowData, Context context) {
		ResultCode resultCode = result.getResultCode();
		if (resultCode == null) {
			resultCode = CommonResultCodes.SYSTEM_ERROR;
		}
		context.put("code", resultCode.getCode());
		context.put("errorMessage", resultCode.getMessage());
		flowData.forwardTo("/json/error");
	}
	
	protected boolean ensureUserLogin(Result result, FlowData flowData) {
		if (!isUserLogin(flowData)) {
			result.setResultCode(AdminResultCodes.USER_NOT_LOGIN);
			return false;
		}
		return true;
	}
	
	protected boolean isAdmin(FlowData flowData) {
		HttpSession session = flowData.getSession();
		Integer roleId = (Integer) session.getAttribute(SessionKeys.ROLE_ID);
		return roleId != null && roleId == RoleEnum.ADMIN.getValue();
	}
	
	protected long getLoginUserId(FlowData flowData) {
		HttpSession session = flowData.getSession();
		Long userId = (Long) session.getAttribute(SessionKeys.USER_ID);
		return userId != null ? userId : 0L;
	}
	
	protected boolean isUserLogin(FlowData flowData) {
		return (getLoginNick(flowData) != null);
	}
	
	protected String getLoginNick(FlowData flowData) {
		HttpSession session = flowData.getSession();
		return (String) session.getAttribute(SessionKeys.USER_NAME);
	}
	protected void result2Context(Result result, Context context) {
		for (Map.Entry<String, Object> entry : result.getModels().entrySet()) {
			context.put(entry.getKey(), entry.getValue());
		}
	}
	
	protected void handleError(Result result, FlowData flowData, Context context) {
		ResultCode resultCode = result.getResultCode();

		if (resultCode == null) {
			resultCode = CommonResultCodes.SYSTEM_ERROR;
		} else if (AdminResultCodes.USER_NOT_LOGIN == resultCode) {
			flowData.redirectTo("adminModule", "login");
			return;
		}
		/*else if(WebResultCodes.USER_NOT_LOGIN == resultCode) {
		    String referurl = flowData.getTarget();
		    UrlBroker urlBroker = flowData.redirectTo("baseModule", "login");
		    setUrlForRefer(flowData, urlBroker);
		    urlBroker.parameter("referurl", referurl);//设置登录成功后的跳转的请求路径。
		    
		    Map<String, Object> paramMap = new HashMap<String, Object>();
		    Set<String> setParam = flowData.getParameters().getNames();
		    Iterator<String> iterator = setParam.iterator();
		    while(iterator.hasNext()){
		        String name = iterator.next();
		        String value = flowData.getParameters().getString(name);
		        paramMap.put(name, value);
		    }
		    
		    flowData.getSession().setAttribute("requestParam", paramMap);
		    
			return;
		} */
		context.put("resultmessage", resultCode);
	}

	protected long getLongByParam(FlowData flowData, Context context , String key) {
		long value = flowData.getParameters().getLong(key);
		if(value > 0) {
			return value;
		}
		Long valueLong =  (Long) context.get(key);
		if(valueLong != null) {
			return valueLong.longValue();
		}
		return 0L;
	}
	
	protected String getStringByParam(FlowData flowData, Context context , String key) {
		String value = flowData.getParameters().getString(key);
		if(!StringUtil.isBlank(value)) {
			return value;
		}
		String value1  =  (String) context.get(key);
		if(!StringUtil.isBlank(value1)) {
			return value1;
		}
		return "";
	}

	
	
	
}

