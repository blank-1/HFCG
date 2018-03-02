package com.xt.cfp.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.QuotaRecord;
import com.xt.cfp.core.service.QuotaRecordService;

@Service
public class QuotaRecordServiceImpl implements QuotaRecordService {
	
	@Autowired
	private MyBatisDao myBatisDao;

	@Override
	public QuotaRecord addQuotaRecord(QuotaRecord quotaRecord) {
		myBatisDao.insert("QUOTA_RECORD.insert", quotaRecord);
		return quotaRecord;
	}

	@Override
	public QuotaRecord getById(Long quotaRecordId) {
		return myBatisDao.get("QUOTA_RECORD.selectByPrimaryKey", quotaRecordId);
	}

	@Override
	public QuotaRecord editQuotaRecord(QuotaRecord quotaRecord) {
		myBatisDao.update("QUOTA_RECORD.updateByPrimaryKey", quotaRecord);
		return quotaRecord;
	}

	@Override
	public List<QuotaRecord> getAllByEnterpriseId(Long enterpriseId) {
		return myBatisDao.getList("QUOTA_RECORD.getAllByEnterpriseId", enterpriseId);
	}

}
