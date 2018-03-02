package com.xt.cfp.core.service.impl;

import java.util.List;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.ProvinceInfo;
import com.xt.cfp.core.service.ProvinceInfoService;

@Service
public class ProvinceInfoServiceImpl implements ProvinceInfoService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 获取所有省份信息
	 */
	@Override
	public List<ProvinceInfo> getAllProvinceInfo() {
		return myBatisDao.getList("PROVINCE_INFO.getAllProvinceInfo");
	}

    @Override
    public ProvinceInfo findById(Long provinceId) {
		if (provinceId==null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("provinceId", "null");
        return  myBatisDao.get("PROVINCE_INFO.findById",provinceId);
    }

}
