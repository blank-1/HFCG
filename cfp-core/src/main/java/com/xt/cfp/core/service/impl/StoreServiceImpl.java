package com.xt.cfp.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.Store;
import com.xt.cfp.core.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 获取所有门店
	 */
	@Override
	public List<Store> findAllStore() {
		return myBatisDao.getList("STORE.findAllStore");
	}

	@Override
	public Store getStoreById(Long storeId) {
		return myBatisDao.get("STORE.selectByPrimaryKey", storeId);
	}

}
