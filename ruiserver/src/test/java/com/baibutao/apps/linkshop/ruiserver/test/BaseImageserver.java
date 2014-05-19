package com.baibutao.apps.linkshop.ruiserver.test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Mar 4, 2014  10:08:08 AM</p>
 * <p>作者：聂鹏</p>
 */
public class BaseImageserver extends AbstractTransactionalDataSourceSpringContextTests {
	
	@Override
	protected String[] getConfigLocations() {
		// 当配置多个dataSource的时候，该行解决：No unique bean of type [javax.sql.DataSource] 
		super.setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] {"applicationContext.xml"};
	}
	
}