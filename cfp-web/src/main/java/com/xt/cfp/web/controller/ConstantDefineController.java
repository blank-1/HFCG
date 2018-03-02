package com.xt.cfp.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.pojo.CityInfo;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.ProvinceInfo;
import com.xt.cfp.core.service.CityInfoService;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.ProvinceInfoService;

@Controller
@RequestMapping("/constant")
public class ConstantDefineController {
	
	@Autowired
	private ConstantDefineService constantDefineService;
	
	@Autowired
	private ProvinceInfoService provinceInfoService;
	
	@Autowired
	private CityInfoService cityInfoService;
	
	/**
	 * 加载下拉列表。
	 * @param constantTypeCode 常量类型
	 * @param parentConstant 常量父编号
	 */
	@RequestMapping(value = "/loadSelect")
	@ResponseBody
	public Object loadSelect(@RequestParam(value = "constantTypeCode", required = false) String constantTypeCode,
			@RequestParam(value = "parentConstant", required = false) Long parentConstant){
		List<ConstantDefine> list = constantDefineService.findByTypeCodeAndParentConstant(constantTypeCode, parentConstant);
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map;
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				ConstantDefine constantDefine = list.get(i);
				map = new HashMap<Object, Object>();
				map.put("text", constantDefine.getConstantName());
				map.put("value", constantDefine.getConstantValue());
				map.put("id", constantDefine.getConstantDefineId());
				maps.add(map);
			}
		}
		return maps;
	}
	
	/**
	 * 加载省份下拉列表。
	 */
	@RequestMapping(value = "/loadProvince")
	@ResponseBody
	public Object loadProvince(){
		List<ProvinceInfo> list = provinceInfoService.getAllProvinceInfo();
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map;
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				ProvinceInfo provinceInfo = list.get(i);
				map = new HashMap<Object, Object>();
				map.put("text", provinceInfo.getProvinceName());
				map.put("value", provinceInfo.getProvinceId());
				maps.add(map);
			}
		}
		return maps;
	}
	
	/**
	 * 加载市或区下拉列表。
	 * @param provinceId 省份ID
	 * @param pCityId 父级城市ID
	 */
	@RequestMapping(value = "/loadCity")
	@ResponseBody
	public Object loadCity(@RequestParam(value = "provinceId", required = false) Long provinceId,
			@RequestParam(value = "pCityId", required = false) Long pCityId){
		List<CityInfo> list = cityInfoService.getCityByProvinceIdAndPId(provinceId, pCityId);
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map;
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				CityInfo cityInfo = list.get(i);
				map = new HashMap<Object, Object>();
				map.put("text", cityInfo.getCityName());
				map.put("value", cityInfo.getCityId());
				maps.add(map);
			}
		}
		return maps;
	}
	
}
