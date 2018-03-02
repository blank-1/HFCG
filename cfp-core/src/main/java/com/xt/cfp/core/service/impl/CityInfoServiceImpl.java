package com.xt.cfp.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.CityInfo;
import com.xt.cfp.core.pojo.LianLianProvinceCity;
import com.xt.cfp.core.pojo.ProvinceInfo;
import com.xt.cfp.core.service.CityInfoService;

@Service
public class CityInfoServiceImpl implements CityInfoService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 根据省ID和城市父ID获取城市或者地区列表
	 */
	@Override
	public List<CityInfo> getCityByProvinceIdAndPId(Long provinceId, Long pCityId) {
		CityInfo cityInfo = new CityInfo();
		cityInfo.setProvinceId(provinceId);
		cityInfo.setpCityId(pCityId);
		return myBatisDao.getList("CITY_INFO.getCityByProvinceIdAndPId", cityInfo);
	}

    @Override
    public CityInfo findById(long cityId) {
        return myBatisDao.get("CITY_INFO.findById",cityId);
    }

    /**
     * 根据父ID获取省市列表【连连支付专用】
     * @param pProvinceCityId 父ID
     * @return
     */
	@Override
	public List<LianLianProvinceCity> getLianLianProvinceCityByPID(Long pProvinceCityId) {
		return myBatisDao.getList("LIANLIAN_PROVINCE_CITY.getLianLianProvinceCityByPID", pProvinceCityId);
	}

	@Override
	public LianLianProvinceCity getLianLianProvinceCityById(Long cityId) {
		return myBatisDao.get("LIANLIAN_PROVINCE_CITY.selectByPrimaryKey",cityId);
	}


	@Override
	public List<CityInfo> findCitysByPid(Long provinceId) {
		return myBatisDao.getList("CITY_INFO.findCityByProvinceId",provinceId);
	}

	@Override
	public List<CityInfo> findAreaByCid(Long cityId) {
		return myBatisDao.getList("CITY_INFO.findAreaByCityId",cityId);
	}

}
