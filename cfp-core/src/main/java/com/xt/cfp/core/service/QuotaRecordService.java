package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.QuotaRecord;

public interface QuotaRecordService {

	/**
     * 添加
     */
	QuotaRecord addQuotaRecord(QuotaRecord quotaRecord);

    /**
     * 根据ID加载一条数据 
     */
	QuotaRecord getById(Long quotaRecordId);

    /**
     * 编辑
     */
	QuotaRecord editQuotaRecord(QuotaRecord quotaRecord);
	
	/**
	 * 根据企业ID，获取所有
	 */
	List<QuotaRecord> getAllByEnterpriseId(Long enterpriseId);
	
}
