package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.EnterprisePledgeSnapshot;
import com.xt.cfp.core.pojo.MainLoanApplication;

public interface EnterprisePledgeSnapshotService {
    /**
     * 添加信贷
     */
	EnterprisePledgeSnapshot addEnterprisePledgeSnapshot(EnterprisePledgeSnapshot enterprisePledgeSnapshot);

    /**
     * 根据信贷ID加载一条数据 
     */
	EnterprisePledgeSnapshot getEnterprisePledgeSnapshotById(Long enterprisePledgeId);

    /**
     * 编辑信贷
     */
	EnterprisePledgeSnapshot editEnterprisePledgeSnapshot(EnterprisePledgeSnapshot enterprisePledgeSnapshot);
	
	/**
	 * 保存企业信贷，第一步
	 * @param loanApplication 借款申请
	 * @param enterpriseInfo 企业信息
	 * @param enterprisePledgeSnapshot 企业信贷快照
	 * @return
	 */
	MainLoanApplication saveEnterpriseLoan(MainLoanApplication mainLoanApplication, EnterpriseInfo enterpriseInfo, EnterprisePledgeSnapshot enterprisePledgeSnapshot);
	
	/**
	 * 根据借款申请ID，加载一条信贷信息
	 */
	EnterprisePledgeSnapshot getByloanApplicationId(Long loanApplicationId);
	
	/**
	 * 根据借款申请【主】ID，加载一条信贷信息
	 */
	EnterprisePledgeSnapshot getByMainLoanApplicationId(Long mainLoanApplicationId);
	
	/**
	 * 保存企业信贷，第二步
	 * @param loanApplication 借款申请
	 * @param enterprisePledgeSnapshot 企业信贷快照
	 * @return
	 */
	MainLoanApplication saveEnterpriseLoanPledge(MainLoanApplication mainLoanApplication, EnterprisePledgeSnapshot enterprisePledgeSnapshot);

}
