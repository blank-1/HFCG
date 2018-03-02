package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.Store;

public interface StoreService {
	
	/**
	 * 获取所有门店
	 */
	List<Store> findAllStore();

	/**
	 * 根据id获取门店
	 */
	Store getStoreById(Long storeId);
}
