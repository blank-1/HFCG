package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.FinancePlanAgreement;

/**
 * Created by ren yulin on 15-8-1.
 */
public interface FinancePlanAgreementService {

    public void insert(FinancePlanAgreement agreement);

    public void update(FinancePlanAgreement agreement);

    public List<FinancePlanAgreement> findByLendOrderId(long lendOrderId);
    /**
     * 根据订单id返回省心计划合同
     * */
	public FinancePlanAgreement findVersionByLendOrderId(long lendOrderId);

    /**
     * 根据订单id和状态查询省心计划合同
     * */
	public FinancePlanAgreement findAvalidByLendOrderId(Long lendOrderId,char status);

	/**
	 * 讲对应省心计划下所有合同都置为失效
	 * */
	public void cancelAllAvalidAgreement(Long lendOrderId,char status);
	
	
}
