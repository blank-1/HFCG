/**
 * 
 */
package com.xt.cfp.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.FinancePlanAgreement;
import com.xt.cfp.core.service.FinancePlanAgreementService;

/**
 * @author 1
 *
 */
@Service
@Transactional
public class FinancePlanAgreementServiceImpl implements
		FinancePlanAgreementService {

	 @Autowired
	    private MyBatisDao myBatisDao;
	 
	/* (non-Javadoc)
	 * @see com.xt.cfp.core.service.FinancePlanAgreementService#insert(com.xt.cfp.core.pojo.FinancePlanAgreement)
	 */
	@Override
	public void insert(FinancePlanAgreement agreement) {
		myBatisDao.insert("FINANCE_PLAN_AGREEMENT.insert",agreement);
	}

	/* (non-Javadoc)
	 * @see com.xt.cfp.core.service.FinancePlanAgreementService#update(com.xt.cfp.core.pojo.FinancePlanAgreement)
	 */
	@Override
	public void update(FinancePlanAgreement agreement) {
		myBatisDao.update("FINANCE_PLAN_AGREEMENT.updateByPrimaryKeySelective",agreement);
	}

	/* (non-Javadoc)
	 * @see com.xt.cfp.core.service.FinancePlanAgreementService#findByLendOrderId(long)
	 */
	@Override
	public List<FinancePlanAgreement> findByLendOrderId(long lendOrderId) {
		return myBatisDao.getList("FINANCE_PLAN_AGREEMENT.findByLendOrderId",lendOrderId);
	}

	/* (non-Javadoc)
	 * @see com.xt.cfp.core.service.FinancePlanAgreementService#findVersionByLendOrderId(long)
	 */
	@Override
	public FinancePlanAgreement findVersionByLendOrderId(long lendOrderId) {
		return myBatisDao.get("FINANCE_PLAN_AGREEMENT.findVersionByCreditorRightsIdAgreeType",lendOrderId);
	}

	@Override
	public FinancePlanAgreement findAvalidByLendOrderId(Long lendOrderId,char status) {
		Map<String, Object> param = new HashMap<>();
		param.put("lendOrderId", lendOrderId) ;
		param.put("status",status);
		return myBatisDao.get("FINANCE_PLAN_AGREEMENT.findAvalidByLendOrderId", param);
	}

	@Override
	public void cancelAllAvalidAgreement(Long lendOrderId,char status) {
		FinancePlanAgreement  agreement = new FinancePlanAgreement();
		agreement.setLendOrderId(lendOrderId);
		agreement.setFinanceAgreementStatus(status);
		myBatisDao.update("FINANCE_PLAN_AGREEMENT.updateByLendOrderId",agreement);
	}
	

}
