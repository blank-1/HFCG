package com.xt.cfp.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CustomerHouseSnapshot;
import com.xt.cfp.core.service.CustomerHouseSnapshotService;

@Service
public class CustomerHouseSnapshotServiceImpl implements CustomerHouseSnapshotService {

	@Autowired
	private MyBatisDao myBatisDao;

	/**
	 * 添加房产抵押信息
	 */
	@Override
	public CustomerHouseSnapshot addHouse(CustomerHouseSnapshot customerHouseSnapshot) {
		myBatisDao.insert("CUSTOMER_HOUSE_SNAPSHOT.insert", customerHouseSnapshot);
		return customerHouseSnapshot;
	}

	/**
	 * 修改房产抵押信息
	 */
	@Override
	public CustomerHouseSnapshot updateHouse(CustomerHouseSnapshot customerHouseSnapshot) {
		myBatisDao.update("CUSTOMER_HOUSE_SNAPSHOT.updateByPrimaryKey", customerHouseSnapshot);
		return customerHouseSnapshot;
	}

	/**
	 * 根据ID加载一条房产抵押信息
	 * @param snapshotId 抵押信息ID
	 */
	@Override
	public CustomerHouseSnapshot getHouseById(Long snapshotId) {
		return myBatisDao.get("CUSTOMER_HOUSE_SNAPSHOT.selectByPrimaryKey", snapshotId);
	}

	/**
	 * 根据申请ID加载一条抵押信息
	 * @param loanApplicationId 借款申请ID
	 */
	@Override
	public CustomerHouseSnapshot getHouseByLoanApplicationId(Long loanApplicationId) {
		return myBatisDao.get("CUSTOMER_HOUSE_SNAPSHOT.getHouseByLoanApplicationId", loanApplicationId);
	}
	
	// by mainid
	@Override
	public CustomerHouseSnapshot getHouseByMainLoanApplicationId(Long mainLoanApplicationId) {
		return myBatisDao.get("CUSTOMER_HOUSE_SNAPSHOT.getHouseByMainLoanApplicationId", mainLoanApplicationId);
	}
	
}
