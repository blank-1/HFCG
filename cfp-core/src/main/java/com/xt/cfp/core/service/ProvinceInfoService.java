package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.ProvinceInfo;

public interface ProvinceInfoService {
	
	/**
	 * 获取所有省份信息
	 */
	List<ProvinceInfo> getAllProvinceInfo();

    ProvinceInfo findById(Long provinceId);
}
