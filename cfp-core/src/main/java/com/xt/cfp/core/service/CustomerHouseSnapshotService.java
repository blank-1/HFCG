package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.CustomerHouseSnapshot;

public interface CustomerHouseSnapshotService {
	/**
	 * 添加房产抵押信息
	 */
	CustomerHouseSnapshot addHouse(CustomerHouseSnapshot customerHouseSnapshot);

	/**
	 * 修改房产抵押信息
	 */
	CustomerHouseSnapshot updateHouse(CustomerHouseSnapshot customerHouseSnapshot);
	
	/**
	 * 根据ID加载一条房产抵押信息
	 * @param snapshotId 抵押信息ID
	 */
	CustomerHouseSnapshot getHouseById(Long snapshotId);
	
	/**
	 * 根据申请ID加载一条抵押信息
	 * @param loanApplicationId 借款申请ID
	 */
	CustomerHouseSnapshot getHouseByLoanApplicationId(Long loanApplicationId);
	
	// by mainid
	CustomerHouseSnapshot getHouseByMainLoanApplicationId(Long mainLoanApplicationId);
}
