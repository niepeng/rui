package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.impl;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultSupport;

import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.BaseAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.ao.WebAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.daointerface.KeyValueDAO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.KeyValueDO;
import com.baibutao.apps.linkshop.uxiang.ruiserver.biz.dal.dataobject.enums.KeyValueTypeEnum;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: U箱</p>
 * <p>创建时间: Jul 22, 2014  5:42:40 PM</p>
 * <p>作者：聂鹏</p>
 */
public class WebAOImpl extends BaseAO implements WebAO {
	
	private KeyValueDAO keyValueDAO;

	@Override
	public Result index() {
		Result result = new ResultSupport(true);
		try {
			KeyValueDO keyValueDO = keyValueDAO.queryByKey(KeyValueTypeEnum.VERSION_UPDATE.getKeyName());
			if (keyValueDO == null) {
				result.getModels().put("downloadUrl", default_version_url);
				return result;
			}
			result.getModels().put("downloadUrl", parseDownloadUrl(keyValueDO.getValue()));
		} catch (Exception e) {
			log.error("index error", e);
		}
		return result;
	}

	public void setKeyValueDAO(KeyValueDAO keyValueDAO) {
		this.keyValueDAO = keyValueDAO;
	}
	
	
}

