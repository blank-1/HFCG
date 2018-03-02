package com.xt.cfp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CustomerWorkSnapshot;
import com.xt.cfp.core.service.CustomerWorkSnapshotService;

@Service
public class CustomerWorkSnapshotServiceImpl implements CustomerWorkSnapshotService {

	@Autowired
	private MyBatisDao myBatisDao;

	/**
	 * 添加工作信息
	 */
	@Override
	public CustomerWorkSnapshot addWork(CustomerWorkSnapshot customerWorkSnapshot) {
		myBatisDao.insert("CUSTOMER_WORK_SNAPSHOT.insert", customerWorkSnapshot);
		return customerWorkSnapshot;
	}

	/**
	 * 修改工作信息
	 */
	@Override
	public CustomerWorkSnapshot updateWork(CustomerWorkSnapshot customerWorkSnapshot) {
		myBatisDao.update("CUSTOMER_WORK_SNAPSHOT.updateByPrimaryKey", customerWorkSnapshot);
		return customerWorkSnapshot;
	}

	/**
	 * 根据ID加载一条工作信息
	 * @param snapshotId 工作信息ID
	 */
	@Override
	public CustomerWorkSnapshot getWorkById(Long snapshotId) {
		return myBatisDao.get("CUSTOMER_WORK_SNAPSHOT.selectByPrimaryKey", snapshotId);
	}

	/**
	 * 根据借款申请ID加载一条工作信息
	 * @param loanApplicationId 借款申请ID
	 */
	@Override
	public CustomerWorkSnapshot getWorkByLoanApplicationId(Long loanApplicationId) {
		return myBatisDao.get("CUSTOMER_WORK_SNAPSHOT.getWorkByLoanApplicationId", loanApplicationId);
	}
	
	//by mainid
	@Override
	public CustomerWorkSnapshot getWorkByMainLoanApplicationId(Long mainLoanApplicationId) {
		return myBatisDao.get("CUSTOMER_WORK_SNAPSHOT.getWorkByMainLoanApplicationId", mainLoanApplicationId);
	}
	
}
