package com.baibutao.apps.linkshop.ruiserver.test;

import wint.help.codec.MD5;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Apr 20, 2014  1:13:51 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestMain {
	public static void main(String[] args) {
		String value = "uxiang.com";
		String md5Value = MD5.encrypt(value);
		System.out.println("value=" + value + ",md5Value=" + md5Value);
	
		String visitUrl = "http://a.com/fef/sdf.apk";
		String fileName = visitUrl.substring(visitUrl.lastIndexOf("/")+ 1);
		System.out.println(fileName);
	}
}

