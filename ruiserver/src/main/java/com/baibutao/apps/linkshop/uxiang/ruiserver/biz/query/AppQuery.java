package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.query;

import wint.help.biz.query.BaseQuery;
import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Apr 20, 2014  3:17:18 PM</p>
 * <p>作者：聂鹏</p>
 */
public class AppQuery  extends BaseQuery { 

	private static final long serialVersionUID = 4965505745349554661L;
	
	// 用户id
	private long userId;
	
	// 一级类目
	private long firstCatId;
	
	//二级类目
	private long secondCatId;
	
	// app状态
	private int status;
	
	// 主标题搜索
	private String keyword;
	
	// 0 默认， 3 是下载值排序高到低
	private int sort;
	

	// -------------- extend attribute -----------------------

	// -------------- normal moethod -------------------------
	
	public void addWildcardChar() {
		if (!StringUtil.isBlank(keyword)) {
			keyword = "%" + keyword + "%" ;
		} else {
			keyword = null;
		}
	}
	
	public void reduceWildcardChar() {
		if (!StringUtil.isBlank(keyword)) {
			keyword = keyword.substring(1, keyword.length() - 1);
		}
	}

	// -------------- setter/getter --------------------------


	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getFirstCatId() {
		return firstCatId;
	}

	public void setFirstCatId(long firstCatId) {
		this.firstCatId = firstCatId;
	}

	public long getSecondCatId() {
		return secondCatId;
	}

	public void setSecondCatId(long secondCatId) {
		this.secondCatId = secondCatId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
}

