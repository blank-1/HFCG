package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.CustomerBasicSnapshot;

public interface CustomerBasicSnapshotService {
	/**
	 * 添加客户基本信息
	 */
	CustomerBasicSnapshot addBasic(CustomerBasicSnapshot customerBasicSnapshot);
	
	/**
	 * 修改客户基本信息
	 */
	CustomerBasicSnapshot updateBasic(CustomerBasicSnapshot customerBasicSnapshot);
	
	/**
	 * 根据ID获取一条基本信息
	 * @param snapshotId 基本信息ID
	 */
	CustomerBasicSnapshot getBasicById(Long snapshotId);
	
	/**
	 * 根据ID获取一条基本信息
	 * @param snapshotId 基本信息ID
	 */
	
	/**
	 * 根据借款ID加载一条基础数据
	 * @param loanApplicationId 借款申请ID
	 */
	CustomerBasicSnapshot getBasicByLoanApplicationId(Long loanApplicationId);
	
	//by mainid
	CustomerBasicSnapshot getBasicByMainLoanApplicationId(Long mainLoanApplicationId);
}
