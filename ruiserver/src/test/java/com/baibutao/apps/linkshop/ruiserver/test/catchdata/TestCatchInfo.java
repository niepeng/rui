package com.baibutao.apps.linkshop.ruiserver.test.catchdata;

import java.util.List;

import junit.framework.TestCase;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common.CatchDataUtil;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common.ContentUtil;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AppInfoDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Jul 7, 2014  3:08:00 PM</p>
 * <p>作者：聂鹏</p>
 */
public class TestCatchInfo extends TestCase {

	public void testCat() {
		String url = "http://www.wandoujia.com/tag/app";
//		String url = "http://www.wandoujia.com/tag/game";
		List<CatDO> list = CatchDataUtil.catchCats(url);
		for(CatDO cat : list) {
			System.out.println("title="+cat.getName());
			System.out.println("linkUrl="+cat.getCatchUrl());
		}
	}
	
	/**
	 * title=便捷生活
	 * linkUrl=http://www.wandoujia.com/tag/便捷生活
	 */
	public void testAppList() {
		String url = "http://www.wandoujia.com/tag/便捷生活";
		List<AppInfoDO> catAppList = CatchDataUtil.catAppList(url);
		for (AppInfoDO app : catAppList) {
			System.out.println("packageName = " + app.getPackageName());
			System.out.println("detailUrl = " + app.getCatchDetailUrl());
			System.out.println("img = " + app.getIconUrl());
		}
	}
	
	/**
	 * packageName = com.taobao.taobao
	 * detailUrl = http://www.wandoujia.com/apps/com.taobao.taobao
	 */
	public void testAppDetail() {
		String url = "http://www.wandoujia.com/apps/com.taobao.taobao";
//		String refer = "http://www.wandoujia.com/tag/便捷生活";
		String refer = null;
		AppInfoDO app = CatchDataUtil.catAppDetail(url, refer);
		System.out.println(app.getMainTitle());
		System.out.println(app.getCatchDataBean().downloadUrl);
		System.out.println(app.getCatchDataBean().iconUrl);
		System.out.println(app.getSubTitle());
		System.out.println(app.getFavNum());
		System.out.println(app.getDownloadNum());
		System.out.println(app.getInfo());
		System.out.println(app.getUpdateInfo());
	}
	
	public void testDownloadApp() {
		String downloadUrl = "http://apps.wandoujia.com/apps/com.taobao.taobao/download";
		String filepathAndName = "/Volumes/util/jhl_project/taobao.apk";
		long start = System.currentTimeMillis();
		System.out.println("start->" + start);
		boolean isSuccess = ContentUtil.saveFile(filepathAndName, downloadUrl);
		long end = System.currentTimeMillis();
		System.out.println("end->" + end + ",isSuccess = " + isSuccess);
		System.out.println("distance->" + (end - start));
	}

}

