package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.Address;
import com.xt.cfp.core.pojo.ext.AddressVO;

public interface AddressService {
	
	/**
	 * 添加地址
	 */
	Address addAddress(Address address);
	
	/**
	 * 修改地址
	 */
	Address updateAddress(Address address);
	
	/**
	 * 根据ID加载一条地址信息
	 * @param addressId 地址ID
	 */
	Address getAddressById(Long addressId);

	/**
	 * 根据ID加载一条地址信息
	 * @param addressId 地址ID
	 */
	AddressVO getAddressVOById(Long addressId);
}
