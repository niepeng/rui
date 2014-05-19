package com.baibutao.apps.linkshop.uxiang.ruiserver.web.common;

import wint.help.biz.result.ResultCode;
import wint.help.biz.result.StringResultCode;

public class AdminResultCodes extends ResultCode {

	private static final long serialVersionUID = 4900053242692056748L;

	public static final ResultCode USER_NOT_LOGIN = create();
	
	public static final ResultCode USER_NOT_PERMISSION = new StringResultCode("当前用户没有权限访问或操作本次请求");
	
	public static final ResultCode ARGS_ERROR = new StringResultCode("参数错误");
	
	public static final ResultCode APP_ARGS_ERROR = new StringResultCode("app参数错误");
	
}