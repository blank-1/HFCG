package com.xt.cfp.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.EnterpriseConstants;
import com.xt.cfp.core.constants.WithDrawSource;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.AddressService;
import com.xt.cfp.core.service.CityInfoService;
import com.xt.cfp.core.service.ProvinceInfoService;
import com.xt.cfp.core.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.service.CoLtdService;

@Service
public class CoLtdServiceImpl implements CoLtdService{

	@Autowired
	private MyBatisDao myBatisDao;

	@Autowired
	private ProvinceInfoService provinceInfoService;

	@Autowired
	private CityInfoService cityInfoService;

	@Override
	public List<CoLtd> getAllCoLtd(Long enterpriseId) {
		return myBatisDao.getList("CO_LTD.getAllCoLtd", enterpriseId);
	}

	@Override
	public Pagination<CoLtd> getCoLtdPaging(int pageNo, int pageSize, CoLtd coltd, Map<String, Object> customParams) {
		Pagination<CoLtd> re = new Pagination<CoLtd>();
		re.setCurrentPage(pageNo);
		re.setPageSize(pageSize);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("coltd", coltd);
		params.put("customParams", customParams);

		int totalCount = this.myBatisDao.count("getCoLtdPaging", params);
		List<CoLtd> uah = this.myBatisDao.getListForPaging("getCoLtdPaging", params, pageNo, pageSize);

		//组装合作公司地址
		for (CoLtd col:uah){
			try{
				ProvinceInfo proviceInfo = provinceInfoService.findById(col.getProvince());
				CityInfo city = cityInfoService.findById(col.getCity());
				col.setAddressStr(proviceInfo.getProvinceName()+city.getCityName());
			}catch (SystemException se){
				//do nothing
			}
		}

		re.setTotal(totalCount);
		re.setRows(uah);

		return re;
	}

	@Override
	public void addCoLtd(CoLtd coLtd) {
		//判断参数是否为null
		if (coLtd == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("coLtd", "null");
		myBatisDao.insert("CO_LTD.insertSelective",coLtd);
	}

	@Override
	public void updateCoLtd(CoLtd coLtd) {
		//判断参数是否为null
		if (coLtd == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("coLtd", "null");

		//判断id属性是否为空
		if (coLtd.getCoId() == null)
			throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_FIELD).set("coId", "null");

		coLtd.setLastUpdateTime(new Date());
		myBatisDao.update("CO_LTD.updateByPrimaryKeySelective", coLtd);
	}

	@Override
	public CoLtd getCoLtdById(Long colId) {
		return myBatisDao.get("CO_LTD.selectByPrimaryKey",colId);
	}

	@Override
	public void changeStatus(Long colId, EnterpriseConstants.CoLtdStatusEnum unUsage) {
		CoLtd coLtd = new CoLtd();
		coLtd.setCoId(colId);
		coLtd.setState(unUsage.getValue());
		this.updateCoLtd(coLtd);
	}

}
