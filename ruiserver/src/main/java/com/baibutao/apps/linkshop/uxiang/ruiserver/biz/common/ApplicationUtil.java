package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common;

import java.text.DecimalFormat;
import java.util.List;

import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.CatDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.APPStsutsEnum;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Apr 23, 2014  10:59:34 PM</p>
 * <p>作者：聂鹏</p>
 */
public class ApplicationUtil {

	public static String div(int a, int b) {
		return String.valueOf(ArithUtil.div(Double.valueOf(a),Double.valueOf(b)));
	}
	
	public static String showSizeM(int size) {
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(size/((double)(1024)));
	}
	
	public static String showAPPStatus(int status) {
		return APPStsutsEnum.valueOf(status).getName();
	}
	
	public static String showIndexImgs(String imgs, int index) {
		if (StringUtil.isBlank(imgs)) {
			return null;
		}
		String[] array = imgs.split(",");
		if (array.length > index) {
			return array[index];
		}
		return null;
	}
	
	/*
	 * ["a","b","c","d"],   
        ["e","f","g","h"]
        
        type = 0, 那么展示名称
        type = 1, 那么展示id
	 */
	public static String showSecondKeys(List<CatDO> catList, int type) {
		if (CollectionUtil.isEmpty(catList)) {
			return StringUtil.EMPTY;
		}
		StringBuilder sb = new StringBuilder();
		int firstSize = catList.size();
		for (int i = 0; i < firstSize; i++) {
			if (i != 0) {
				sb.append(",");
			}
			List<CatDO> secondList = catList.get(i).getChildrenList();
			sb.append("[");
			for (int k = 0, secondSize = secondList.size(); k < secondSize; k++) {
				if (k != 0) {
					sb.append(",");
				}
				if(type == 0) {
					sb.append("\"");
					sb.append(secondList.get(k).getName());
					sb.append("\"");
				} else {
					sb.append(secondList.get(k).getId());
				}
				
			}
			sb.append("]");
		}
		return sb.toString();
	}
	
	
	
	
	public static void main(String[] args) {
//		int size = 75651024;
//		System.out.println(showSizeM(size));
		
		List<CatDO> catList = CollectionUtil.newArrayList();
		CatDO c1 = new CatDO();
		c1.setName("c1");
		c1.setId(1);
		CatDO c2 = new CatDO();
		c2.setName("c2");
		c2.setId(2);
		
		catList.add(c1);
		catList.add(c2);
		
		List<CatDO> c1List = CollectionUtil.newArrayList();
		List<CatDO> c2List = CollectionUtil.newArrayList();
		c1.setChildrenList(c1List);
		c2.setChildrenList(c2List);
		
		CatDO c11 = new CatDO();
		c11.setName("c11");
		c11.setId(11);
		CatDO c12 = new CatDO();
		c12.setName("c12");
		c12.setId(12);
		c1List.add(c11);
		c1List.add(c12);
		
		CatDO c21 = new CatDO();
		c21.setName("c21");
		c21.setId(21);
		CatDO c22 = new CatDO();
		c22.setName("c22");
		c22.setId(22);
		c2List.add(c21);
		c2List.add(c22);
		
		System.out.println(showSecondKeys(catList,1));
	}
	
}

