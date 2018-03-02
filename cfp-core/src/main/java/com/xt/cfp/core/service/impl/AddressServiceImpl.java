package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.pojo.ext.AddressVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.Address;
import com.xt.cfp.core.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private MyBatisDao myBatisDao;

	/**
	 * 添加地址
	 */
	@Override
	public Address addAddress(Address address) {
		myBatisDao.insert("ADDRESS.insert", address);
		return address;
	}

	/**
	 * 修改地址
	 */
	@Override
	public Address updateAddress(Address address) {
		myBatisDao.update("ADDRESS.updateByPrimaryKey", address);
		return address;
	}

	/**
	 * 根据ID加载一条地址信息
	 * @param addressId 地址ID
	 */
	@Override
	public Address getAddressById(Long addressId) {
		return myBatisDao.get("ADDRESS.selectByPrimaryKey", addressId);
	}

	@Override
	public AddressVO getAddressVOById(Long addressId) {
		return myBatisDao.get("getAddressVOById", addressId);
	}

}
