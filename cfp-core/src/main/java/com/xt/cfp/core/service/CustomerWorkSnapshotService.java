package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.CustomerWorkSnapshot;

public interface CustomerWorkSnapshotService {
	/**
	 * 添加工作信息
	 */
	CustomerWorkSnapshot addWork(CustomerWorkSnapshot customerWorkSnapshot);
	
	/**
	 * 修改工作信息
	 */
	CustomerWorkSnapshot updateWork(CustomerWorkSnapshot customerWorkSnapshot);
	
	/**
	 * 根据ID加载一条工作信息
	 * @param snapshotId 工作信息ID
	 */
	CustomerWorkSnapshot getWorkById(Long snapshotId);
	
	/**
	 * 根据借款申请ID加载一条工作信息
	 * @param loanApplicationId 借款申请ID
	 */
	CustomerWorkSnapshot getWorkByLoanApplicationId(Long loanApplicationId);
	
	//by mainid
	CustomerWorkSnapshot getWorkByMainLoanApplicationId(Long mainLoanApplicationId);
}
