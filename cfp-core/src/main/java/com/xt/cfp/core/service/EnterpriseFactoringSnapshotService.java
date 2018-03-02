package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.EnterpriseFactoringSnapshot;
import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.MainLoanApplication;

public interface EnterpriseFactoringSnapshotService {
    /**
     * 添加保理
     */
	EnterpriseFactoringSnapshot addEnterpriseFactoringSnapshot(EnterpriseFactoringSnapshot enterpriseFactoringSnapshot);

    /**
     * 根据保理ID加载一条数据
     */
	EnterpriseFactoringSnapshot getEnterpriseFactoringSnapshotById(Long enterpriseFactoringId);

    /**
     * 编辑保理
     */
	EnterpriseFactoringSnapshot editEnterpriseFactoringSnapshot(EnterpriseFactoringSnapshot enterpriseFactoringSnapshot);
	
	/**
	 * 保存企业保理，第一步
	 * @param loanApplication 借款申请
	 * @param enterpriseInfo 企业信息
	 * @param enterpriseFactoringSnapshot 企业保理快照
	 * @return
	 */
	MainLoanApplication saveEnterpriseLoan(MainLoanApplication mainLoanApplication, EnterpriseInfo enterpriseInfo, EnterpriseFactoringSnapshot enterpriseFactoringSnapshot);
	
	/**
	 * 根据借款申请ID，加载一条保理信息
	 */
	EnterpriseFactoringSnapshot getByloanApplicationId(Long loanApplicationId);
	
	/**
	 * 根据借款申请【主】ID，加载一条保理信息
	 */
	EnterpriseFactoringSnapshot getByMainLoanApplicationId(Long mainLoanApplicationId);
	
	/**
	 * 保存企业保理，第二步
	 * @param loanApplication 借款申请
	 * @param enterpriseFactoringSnapshot 企业保理信息
	 * @return
	 */
	MainLoanApplication saveEnterpriseLoanFactoring(MainLoanApplication mainLoanApplication, EnterpriseFactoringSnapshot enterpriseFactoringSnapshot);

}
