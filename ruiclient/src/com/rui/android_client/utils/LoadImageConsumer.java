package com.rui.android_client.utils;

import android.graphics.Bitmap;

/**
 * @author lsb
 *
 * @date 2012-5-29 下午11:04:48
 */
public class LoadImageConsumer implements Runnable {

	private MyQueue<LoadImgDO> myQueue;

	public LoadImageConsumer(MyQueue<LoadImgDO> queue) {
		this.myQueue = queue;
	}

	@Override
	public void run() {
		while (true) {
			final LoadImgDO loadImgDO = myQueue.take();
			final Bitmap bitmap = tryLoadImage(loadImgDO);
			if (bitmap == null) {
				continue;
			}
			loadImgDO.getHandler().post(new Runnable() {
				@Override
				public void run() {
					loadImgDO.getImageView().setImageBitmap(bitmap);
				}
			});
		}
	}

	private Bitmap tryLoadImage(LoadImgDO loadImgDO) {
		int tryTimes = 5;
		for (int i = 0; i < tryTimes; ++i) {
			try {
				return null;
//				return ImageUtil.getBitmap(loadImgDO.getPicName(), loadImgDO.getPicUrl());
//				return ImageUtil.getBitmap(loadImgDO.getPicName(), 100);
//				return ImageUtil.getBitmap(loadImgDO.getPicName(), loadImgDO.getPicUrl(), loadImgDO.getSize());
			} catch (Exception e) {
				// 如果失败再重新取几次
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					Thread.currentThread().interrupt();
				}
			}
		}
		return null;
	}

}

