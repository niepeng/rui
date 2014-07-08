package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.bean.CatchDataBean;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.AppInfoDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Jul 7, 2014  10:48:04 PM</p>
 * <p>作者：聂鹏</p>
 */
public class CatchDataUtil {

	/**
	 * http://www.wandoujia.com/tag/app
	 */
	public static List<CatDO> catchCats(String url) {
		List<CatDO> result = CollectionUtil.newArrayList();
		String content = ContentUtil.getContent(url, 2000);
		Document doc = Jsoup.parse(content);
		Elements elements = doc.getElementsByClass("tag-box");
		if (elements == null || elements.size() > 1) {
			return result;
		}
		Element tagBoxElement = elements.get(0);
		Elements catsElement = tagBoxElement.children();
		for (Element catElement : catsElement) {
			CatDO cat = new CatDO();
			Element mainCatsElement = catElement.getElementsByTag("a").get(0);
			String title = mainCatsElement.attr("title");
			String linkUrl = mainCatsElement.attr("href");
			cat.setName(title);
			cat.setCatchUrl(linkUrl);
			result.add(cat);
		}
		return result;
	}


	/**
	 * http://www.wandoujia.com/tag/便捷生活
	 */
	public static String catchCatImg(String name) {
		List<AppInfoDO> list = catAppList("http://www.wandoujia.com/tag/" + encode(name));
		if (CollectionUtil.isEmpty(list)) {
			return StringUtil.EMPTY;
		}
		for (AppInfoDO appInfoDO : list) {
			if (!StringUtil.isBlank(appInfoDO.getIconUrl())) {
				return appInfoDO.getIconUrl();
			}
		}
		return StringUtil.EMPTY;
	}


	public static String encode(String name) {
		try {
			return URLEncoder.encode(name, "utf-8");
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * http://www.wandoujia.com/tag/便捷生活
	 */
	public static List<AppInfoDO> catAppList(String url) {
		List<AppInfoDO> result = CollectionUtil.newArrayList();
		String content = ContentUtil.getContent(url, 2000);
		Document doc = Jsoup.parse(content);
		Element appListElement = doc.getElementById("j-tag-list");
		for (Element appElement : appListElement.children()) {
			AppInfoDO appInfo = new AppInfoDO();
			String packageName = appElement.attr("data-pn");
			Element aElement = appElement.getElementsByClass("icon-wrap").get(0).getElementsByTag("a").get(0);
			String detailUrl = aElement.attr("href");
			String logoImg = aElement.getElementsByTag("img").get(0).attr("src");
			appInfo.setPackageName(packageName);
			appInfo.setIconUrl(logoImg);
			appInfo.setCatchDetailUrl(detailUrl);
			result.add(appInfo);
		}
		return result;
	}
	
	/**
	 * 
	 * @param detailUrl
	 * 		http://www.wandoujia.com/apps/com.taobao.taobao
	 * @param referUrl
	 * 		http://www.wandoujia.com/tag/便捷生活
	 * @return
	 */
	public static AppInfoDO catAppDetail(String detailUrl, String referUrl) {
		AppInfoDO appInfoDO = new AppInfoDO();
		CatchDataBean bean = new CatchDataBean();
		appInfoDO.setCatchDataBean(bean);

		String content = ContentUtil.getContent(detailUrl, referUrl, 2000);
		Document doc = Jsoup.parse(content);
		Element detailElement = doc.getElementsByClass("detail-wrap").get(0);

		Element iconElement = detailElement.getElementsByClass("app-icon").get(0);
		Element imgElement = iconElement.getElementsByTag("img").get(0);
		String icon = imgElement.attr("src");
		bean.iconUrl = icon;

		Element appInfoElement = detailElement.getElementsByClass("app-info").get(0);
		String title = appInfoElement.getElementsByClass("app-name").get(0).getElementsByTag("span").get(0).html();
		appInfoDO.setMainTitle(title);
		String subTitle = appInfoElement.getElementsByClass("tagline").get(0).html();
		appInfoDO.setSubTitle(subTitle);
		Element installBtnElement = detailElement.getElementsByClass("install-btn").get(0);
		String downloadUrl = installBtnElement.attr("href");
		String dataLikeNum = installBtnElement.attr("data-like");
		String dataInstallNum = installBtnElement.attr("data-install");
		bean.downloadUrl = downloadUrl;
		appInfoDO.setFavNum(change2Num(dataLikeNum));
		appInfoDO.setDownloadNum(change2Num(dataInstallNum));

		Element imgsElement = detailElement.getElementsByClass("view-box").get(0);
		StringBuilder sb = new StringBuilder();
		for (Element imgElemnt : imgsElement.getElementsByTag("img")) {
			String imgUrl = imgElemnt.attr("src");
			if (!StringUtil.isBlank(imgUrl)) {
				sb.append(imgUrl);
				sb.append(",");
			}
		}
		String imgs = sb.toString();
		if (!StringUtil.isBlank(imgs)) {
			appInfoDO.setScreenshots(imgs.substring(0, imgs.length() - 1));
		}

		String info = detailElement.getElementsByClass("desc-info").get(0).getElementsByClass("con").get(0).html();
		appInfoDO.setInfo(info);

		String updateInfo = detailElement.getElementsByClass("change-info").get(0).getElementsByClass("con").get(0).html();
		appInfoDO.setUpdateInfo(updateInfo);
		return appInfoDO;
	}
	
	/**
	 * 1.1 亿
	 */
	private static int change2Num(String value) {
		if (StringUtil.isBlank(value)) {
			return 0;
		}

		int index = -1;
		if ((index = value.indexOf("万")) > 0) {
			value = value.substring(0, index).trim();
			return parse2Int(value, 10000);
		}

		if ((index = value.indexOf("亿")) > 0) {
			value = value.substring(0, index).trim();
			return parse2Int(value, 100000000);
		}

		return parse2Int(value.trim(), 1);
	}
	
	private static int parse2Int(String value, int i) {
		try {
			return (int)(Double.valueOf(value.trim()).doubleValue() * i);
		} catch (Exception e) {
		}
		return 0;
	}

	public static void main(String[] args) {
		System.out.println(change2Num("1.2 万"));
		System.out.println(change2Num("1.2 亿"));
		System.out.println(change2Num("5612"));
	}
	
}

