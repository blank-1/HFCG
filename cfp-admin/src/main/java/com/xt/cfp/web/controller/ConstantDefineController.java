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

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.pojo.CityInfo;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.ProvinceInfo;
import com.xt.cfp.core.pojo.Store;
import com.xt.cfp.core.service.CityInfoService;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.ProvinceInfoService;
import com.xt.cfp.core.service.StoreService;

@Controller
@RequestMapping("/jsp/constant/")
public class ConstantDefineController {
	
	@Autowired
	private ConstantDefineService constantDefineService;
	
	@Autowired
	private ProvinceInfoService provinceInfoService;
	
	@Autowired
	private CityInfoService cityInfoService;
	
	@Autowired
	private StoreService storeService;
	
	/**
	 * 加载下拉列表。
	 * @param constantTypeCode 常量类型
	 * @param parentConstant 常量父编号
	 * @param selectedDisplay 默认显示值
	 */
	@RequestMapping(value = "/loadSelect")
	@ResponseBody
	public Object loadSelect(@RequestParam(value = "constantTypeCode", required = false) String constantTypeCode,
			@RequestParam(value = "parentConstant", required = false) Long parentConstant,
			@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay){
		if("selected".equals(selectedDisplay)){
			selectedDisplay = "请选择";
		}else {
			selectedDisplay = "全部";
		}
		List<ConstantDefine> list = constantDefineService.findByTypeCodeAndParentConstant(constantTypeCode, parentConstant);
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map;
		map = new HashMap<Object, Object>();
		map.put("CONSTANTNAME", selectedDisplay);
		map.put("CONSTANTVALUE", "");
		map.put("CONSTANTID", "");
		map.put("selected", true);
		maps.add(map);
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				ConstantDefine constantDefine = list.get(i);
				map = new HashMap<Object, Object>();
				map.put("CONSTANTNAME", constantDefine.getConstantName());
				map.put("CONSTANTVALUE", constantDefine.getConstantValue());
				map.put("CONSTANTID", constantDefine.getConstantDefineId());
				maps.add(map);
			}
		}
		return maps;
	}
	
	/**
	 * 获取常量ID。（根据下面三个条件锁定一条数据）
	 * @param constantTypeCode 常量类型
	 * @param parentConstant 常量父编号
	 * @param constantValue 常量值
	 */
	@RequestMapping(value = "/getConstantDefineId")
	@ResponseBody
	public Object getConstantDefineId(@RequestParam(value = "constantTypeCode", required = false) String constantTypeCode,
			@RequestParam(value = "parentConstant", required = false) Long parentConstant,
			@RequestParam(value = "constantValue", required = false) String constantValue){
		ConstantDefine define = new ConstantDefine();
		define.setConstantTypeCode(constantTypeCode);
		define.setConstantValue(constantValue);
		define.setParentConstant(parentConstant);
		ConstantDefine constantDefine = constantDefineService.findConstantByTypeCodeAndValue(define);
		return JSONObject.toJSON(constantDefine);
	}
	
	/**
	 * 加载省份下拉列表。
	 * @param selectedDisplay 默认显示值
	 */
	@RequestMapping(value = "/loadProvince")
	@ResponseBody
	public Object loadProvince(@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay){
		if("selected".equals(selectedDisplay)){
			selectedDisplay = "请选择";
		}else {
			selectedDisplay = "全部";
		}
		List<ProvinceInfo> list = provinceInfoService.getAllProvinceInfo();
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map;
		map = new HashMap<Object, Object>();
		map.put("PROVINCENAME", selectedDisplay);
		map.put("PROVINCEID", "");
		map.put("selected", true);
		maps.add(map);
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				ProvinceInfo provinceInfo = list.get(i);
				map = new HashMap<Object, Object>();
				map.put("PROVINCENAME", provinceInfo.getProvinceName());
				map.put("PROVINCEID", provinceInfo.getProvinceId());
				maps.add(map);
			}
		}
		return maps;
	}
	
	/**
	 * 加载市或区下拉列表。
	 * @param selectedDisplay 默认显示值
	 * @param provinceId 省份ID
	 * @param pCityId 父级城市ID
	 */
	@RequestMapping(value = "/loadCity")
	@ResponseBody
	public Object loadCity(@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay,
			@RequestParam(value = "provinceId", required = false) Long provinceId,
			@RequestParam(value = "pCityId", required = false) Long pCityId){
		if("selected".equals(selectedDisplay)){
			selectedDisplay = "请选择";
		}else {
			selectedDisplay = "全部";
		}
		List<CityInfo> list = cityInfoService.getCityByProvinceIdAndPId(provinceId, pCityId);
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map;
		map = new HashMap<Object, Object>();
		map.put("CITYNAME", selectedDisplay);
		map.put("CITYID", "");
		map.put("selected", true);
		maps.add(map);
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				CityInfo cityInfo = list.get(i);
				map = new HashMap<Object, Object>();
				map.put("CITYNAME", cityInfo.getCityName());
				map.put("CITYID", cityInfo.getCityId());
				maps.add(map);
			}
		}
		return maps;
	}
	
	/**
	 * 加载门店下拉列表。
	 * @param selectedDisplay 默认显示值
	 */
	@RequestMapping(value = "/loadStore")
	@ResponseBody
	public Object loadStore(@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay){
		if("selected".equals(selectedDisplay)){
			selectedDisplay = "选择门店";
		}else {
			selectedDisplay = "全部";
		}
		List<Store> list = storeService.findAllStore(); 
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map;
		map = new HashMap<Object, Object>();
		map.put("STORENAME", selectedDisplay);
		map.put("STOREID", "");
		map.put("selected", true);
		maps.add(map);
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				Store store = list.get(i);
				map = new HashMap<Object, Object>();
				map.put("STORENAME", store.getStoreName());
				map.put("STOREID", store.getStoreId());
				maps.add(map);
			}
		}
		return maps;
	}
}
