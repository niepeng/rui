package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.ibatis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class BaseIbatisDAO extends SqlMapClientDaoSupport {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected Logger getLog() {
		return log;
	}
}