package com.xt.cfp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CustomerCarSnapshot;
import com.xt.cfp.core.service.CustomerCarSnapshotService;
@Service
public class CustomerCarSnapshotServiceImpl implements
		CustomerCarSnapshotService {

	@Autowired
	private MyBatisDao myBatisDao;

	@Override
	public CustomerCarSnapshot addCar(CustomerCarSnapshot customerCarSnapshot) {
		myBatisDao.insert("CUSTOMER_CAR_SNAPSHOT.insert", customerCarSnapshot);
		return customerCarSnapshot;
	}

	@Override
	public CustomerCarSnapshot updateCar(CustomerCarSnapshot customerCarSnapshot) {
		myBatisDao.update("CUSTOMER_CAR_SNAPSHOT.updateByPrimaryKey", customerCarSnapshot);
		return customerCarSnapshot;
	}

	@Override
	public CustomerCarSnapshot getCarById(Long snapshotId) {
		return myBatisDao.get("CUSTOMER_CAR_SNAPSHOT.selectByPrimaryKey", snapshotId);
	}

	@Override
	public CustomerCarSnapshot getCarByLoanApplicationId(Long loanApplicationId) {
		return myBatisDao.get("CUSTOMER_CAR_SNAPSHOT.getCarByLoanApplicationId", loanApplicationId);
	}

	@Override
	public CustomerCarSnapshot getCarByMainLoanApplicationId(
			Long mainLoanApplicationId) {
		return myBatisDao.get("CUSTOMER_CAR_SNAPSHOT.getCarByMainLoanApplicationId", mainLoanApplicationId);
	}
}
