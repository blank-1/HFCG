package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.CustomerCarSnapshot;

public interface CustomerCarSnapshotService {

	/**
	 * 添加车贷抵押信息
	 */
	
	CustomerCarSnapshot addCar(CustomerCarSnapshot customerCarSnapshot);
	
	/**
	 * 修改车贷抵押信息
	 */
	CustomerCarSnapshot updateCar(CustomerCarSnapshot customerCarSnapshot);
	
	/**
	 * 根据ID加载一条车贷抵押信息
	 * @param snapshotId 抵押信息ID
	 */
	CustomerCarSnapshot getCarById(Long snapshotId);
	
	/**
	 * 根据申请ID加载一条抵押信息
	 * @param loanApplicationId 借款申请ID
	 */
	CustomerCarSnapshot getCarByLoanApplicationId(Long loanApplicationId);
	
	// by mainid
	CustomerCarSnapshot getCarByMainLoanApplicationId(Long mainLoanApplicationId);
}
