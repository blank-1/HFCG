package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.EnterpriseCarLoanSnapshot;
import com.xt.cfp.core.pojo.EnterpriseInfo;
import com.xt.cfp.core.pojo.MainLoanApplication;

public interface EnterpriseCarLoanSnapshotService {
    /**
     * 添加车贷
     */
	EnterpriseCarLoanSnapshot addEnterpriseCarLoanSnapshot(EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot);

    /**
     * 根据车贷ID加载一条数据 
     */
	EnterpriseCarLoanSnapshot getEnterpriseCarLoanSnapshotById(Long enterpriseCarLoanId);

    /**
     * 编辑车贷
     */
	EnterpriseCarLoanSnapshot editEnterpriseCarLoanSnapshot(EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot);
	
	/**
	 * 保存企业车贷，第一步
	 * @param loanApplication 借款申请
	 * @param enterpriseInfo 企业信息
	 * @param enterpriseCarLoanSnapshot 企业车贷快照
	 * @return
	 */
	MainLoanApplication saveEnterpriseLoan(MainLoanApplication mainLoanApplication, EnterpriseInfo enterpriseInfo, EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot);
	
	/**
	 * 根据借款申请ID，加载一条车贷快照信息
	 */
	EnterpriseCarLoanSnapshot getByloanApplicationId(Long loanApplicationId);
	
	/**
	 * 根据借款申请【主】ID，加载一条车贷快照信息
	 */
	EnterpriseCarLoanSnapshot getByMainLoanApplicationId(Long mainLoanApplicationId);
	
	/**
	 * 保存企业车贷，第一步
	 * @param loanApplication 借款申请
	 * @param enterpriseCarLoanSnapshot 企业车贷快照
	 * @return
	 */
	MainLoanApplication saveEnterpriseLoanCar(MainLoanApplication mainLoanApplication, EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot);

}
