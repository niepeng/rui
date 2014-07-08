package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.bo;

import java.io.File;

import wint.mvc.form.fileupload.UploadFile;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Apr 27, 2014  4:02:22 PM</p>
 * <p>作者：聂鹏</p>
 */
public interface FileService {

	public String uploadFile(UploadFile uploadFile, String suffix);
	
	public String uploadFile(String url, String suffix);
	
	public String uploadFile(UploadFile uploadFile);
	
	public File getFileByName(String fileName); 
	
	public boolean checkFileWidthHeigthEqueals(File file, int width, int height);
}

