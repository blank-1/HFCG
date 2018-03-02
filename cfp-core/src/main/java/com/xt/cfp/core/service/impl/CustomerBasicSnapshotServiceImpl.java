package com.xt.cfp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CustomerBasicSnapshot;
import com.xt.cfp.core.service.CustomerBasicSnapshotService;

@Service
public class CustomerBasicSnapshotServiceImpl implements CustomerBasicSnapshotService {
	
	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 添加客户基本信息
	 */
	@Override
	public CustomerBasicSnapshot addBasic(CustomerBasicSnapshot customerBasicSnapshot) {
		myBatisDao.insert("CUSTOMER_BASIC_SNAPSHOT.insert", customerBasicSnapshot);
		return customerBasicSnapshot;
	}
	
	/**
	 * 修改客户基本信息
	 */
	@Override
	public CustomerBasicSnapshot updateBasic(CustomerBasicSnapshot customerBasicSnapshot) {
		myBatisDao.update("CUSTOMER_BASIC_SNAPSHOT.updateByPrimaryKey", customerBasicSnapshot);
		return customerBasicSnapshot;
	}

	/**
	 * 根据ID获取一条基本信息
	 * @param snapshotId 基本信息ID
	 */
	@Override
	public CustomerBasicSnapshot getBasicById(Long snapshotId) {
		return myBatisDao.get("CUSTOMER_BASIC_SNAPSHOT.selectByPrimaryKey", snapshotId);
	}

	/**
	 * 根据借款ID加载一条基础数据
	 * @param loanApplicationId 借款申请ID
	 */
	@Override
	public CustomerBasicSnapshot getBasicByLoanApplicationId(Long loanApplicationId) {
		return myBatisDao.get("CUSTOMER_BASIC_SNAPSHOT.getBasicByLoanApplicationId", loanApplicationId);
	}
	
	/**
	 * 根据借款（主）ID加载一条基础数据
	 * @param mainLoanApplicationId 借款申请（主）ID
	 */
	@Override
	public CustomerBasicSnapshot getBasicByMainLoanApplicationId(Long mainLoanApplicationId) {
		return myBatisDao.get("CUSTOMER_BASIC_SNAPSHOT.getBasicByMainLoanApplicationId", mainLoanApplicationId);
	}
}
