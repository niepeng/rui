package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.bo.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.lang.utils.IoUtil;
import wint.lang.utils.StringUtil;
import wint.mvc.form.fileupload.UploadFile;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.bo.FileService;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common.ContentUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Apr 27, 2014  4:03:52 PM</p>
 * <p>作者：聂鹏</p>
 */
public class FileServiceImpl implements FileService {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	// 文件存放路径
	private String filePath;
	
	// url访问前缀
	private String imagePerfix;
	
	public static final String PNG = ".png";
	
	public static final String APK = ".apk";
	
	@Override
	public String uploadFile(UploadFile uploadFile, String suffix) {
		if (uploadFile == null || uploadFile.getSize() < 1) {
			return null;
		}
		
		String fileName = String.valueOf(System.currentTimeMillis()) + suffix;
		File file = new File(filePath + fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			log.error("创建文件失败", e);
			return null;
		}
		
		uploadFile.writeTo(file);
		return imagePerfix + fileName;
	}
	
	@Override
	public String uploadFile(String url, String suffix) {
		if (StringUtil.isBlank(url)) {
			return null;
		}
		String fileName = String.valueOf(System.currentTimeMillis()) + suffix;
		try {
//			if (writeFile(ImageUtil.getDataByUrl(url), filePath + fileName)) {
//				return imagePerfix + fileName;
//			}
			if(ContentUtil.saveFile(filePath + fileName, url)) {
				return imagePerfix + fileName;
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	@Override
	public String uploadFile(UploadFile uploadFile) {
		if (uploadFile == null || uploadFile.getSize() < 1) {
			return null;
		}
		String name = uploadFile.getName();
		int index = name.lastIndexOf('.');
		if (index < 0) {
			return null;
		}
		return uploadFile(uploadFile, name.substring(index));
	}
	
	@Override
	public File getFileByName(String fileName) {
		return new File(filePath + fileName);
	}
	
	@Override
	public boolean checkFileWidthHeigthEqueals(File file, int width, int height) {
		if (file == null || !file.exists()) {
			return false;
		}
		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			return bufferedImage.getWidth() == width && bufferedImage.getHeight() == height;
		} catch (Exception e) {
		}
		return false;
	}
	
	public static boolean isSuffix(String name, String suffix) {
		int index = -1;
		if (StringUtil.isBlank(name) || (index = name.lastIndexOf('.')) < 0) {
			return false;
		}
		String currentSuffix = name.substring(index);
		return currentSuffix.equalsIgnoreCase(suffix);
	}
	
	public static boolean writeFile(byte[] data, String filePathName) throws Exception {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePathName, false);
			fos.write(data);
			fos.flush();
		} finally {
			IoUtil.close(fos);
		}
		return true;
	}


	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public String getImagePerfix() {
		return imagePerfix;
	}


	public void setImagePerfix(String imagePerfix) {
		this.imagePerfix = imagePerfix;
	}
	
}

