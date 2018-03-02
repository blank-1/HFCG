package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.EnterpriseCreditSnapshot;
import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.MainLoanApplication;

public interface EnterpriseCreditSnapshotService {
    /**
     * 添加信贷
     */
	EnterpriseCreditSnapshot addEnterpriseCreditSnapshot(EnterpriseCreditSnapshot enterpriseCreditSnapshot);

    /**
     * 根据信贷ID加载一条数据 
     */
	EnterpriseCreditSnapshot getEnterpriseCreditSnapshotById(Long enterpriseCreditId);

    /**
     * 编辑信贷
     */
	EnterpriseCreditSnapshot editEnterpriseCreditSnapshot(EnterpriseCreditSnapshot enterpriseCreditSnapshot);
	
	/**
	 * 保存企业信贷，第一步
	 * @param loanApplication 借款申请
	 * @param enterpriseInfo 企业信息
	 * @param enterpriseCreditSnapshot 企业信贷快照
	 * @return
	 */
	MainLoanApplication saveEnterpriseLoan(MainLoanApplication mainLoanApplication, EnterpriseInfo enterpriseInfo, EnterpriseCreditSnapshot enterpriseCreditSnapshot);
	
	/**
	 * 根据借款申请ID，加载一条信贷信息
	 */
	EnterpriseCreditSnapshot getByloanApplicationId(Long loanApplicationId);
	
	/**
	 * 根据借款申请【主】ID，加载一条信贷信息
	 */
	EnterpriseCreditSnapshot getByMainLoanApplicationId(Long mainLoanApplicationId);
	
	/**
	 * 保存企业信贷，第二步
	 * @param loanApplication 借款申请
	 * @param enterpriseCreditSnapshot 企业信贷快照
	 * @return
	 */
	MainLoanApplication saveEnterpriseLoanCredit(MainLoanApplication mainLoanApplication, EnterpriseCreditSnapshot enterpriseCreditSnapshot);

}
