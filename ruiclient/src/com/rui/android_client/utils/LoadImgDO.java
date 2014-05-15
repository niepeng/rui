package com.rui.android_client.utils;

import android.os.Handler;
import android.widget.ImageView;

/**
 * @author lsb
 *
 */
public class LoadImgDO {

	private ImageView imageView;
	
	private String picName;
	
	private String picUrl;

	private Handler handler;

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
}
