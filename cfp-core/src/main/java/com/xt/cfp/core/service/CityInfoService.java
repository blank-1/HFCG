package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.CityInfo;
import com.xt.cfp.core.pojo.LianLianProvinceCity;

public interface CityInfoService {
	
	/**
	 * 根据省ID和城市父ID获取城市或者地区列表
	 */
	List<CityInfo> getCityByProvinceIdAndPId(Long provinceId, Long pCityId);

    CityInfo findById(long cityId);
    
    /**
     * 根据父ID获取省市列表【连连支付专用】
     * @param pProvinceCityId 父ID
     * @return
     */
    List<LianLianProvinceCity> getLianLianProvinceCityByPID(Long pProvinceCityId);
    
    LianLianProvinceCity getLianLianProvinceCityById(Long cityId);
    
    List<CityInfo> findCitysByPid(Long provinceId);
    
    List<CityInfo> findAreaByCid(Long cityId);
}
