package com.xt.cfp.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.FunctionInfo;
import com.xt.cfp.core.pojo.FunctionUrl;
import com.xt.cfp.core.pojo.SalesFunctionInfo;
import com.xt.cfp.core.service.FunctionInfoService;
import com.xt.cfp.core.service.SalesFunctionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalesFunctionInfoServiceImpl implements SalesFunctionInfoService {
	
	@Autowired
	private MyBatisDao myBatisDao;

	/**
	 * 获取权限信息列表
	 */
	@Override
	public List<SalesFunctionInfo> findAllFunction(){
		return myBatisDao.getList("SL_FUNCTION_INFO.findAllFunction");
	}
	
	/**
	 * 根据权限ID加载一条数据
	 * @param functionId 权限ID
	 */
	@Override
	public SalesFunctionInfo getFunctionById(Long functionId){
		return myBatisDao.get("SL_FUNCTION_INFO.selectByPrimaryKey", functionId);
	}
	
	/**
	 * 根据角色ID获取权限列表
	 * @param roleId 角色ID
	 */
	@Override
	public List<SalesFunctionInfo> getFunctionByRoleId(Long roleId){
		return myBatisDao.getList("SL_FUNCTION_INFO.findFunctionByRoleId", roleId);
	}
	
	/**
	 * 根据权限父ID获取权限列表
	 * @param pFunctionId 权限父ID
	 */
	@Override
	public List<SalesFunctionInfo> getFunctionByPId(Long pFunctionId){
		return myBatisDao.getList("SL_FUNCTION_INFO.findFunctionByPid", pFunctionId);
	}
	
	/**
	 * 根据权限编码加载一条权限信息
	 * @param code 权限编码
	 */
	@Override
	public SalesFunctionInfo getFunctionByCode(String code){
		return myBatisDao.get("SL_FUNCTION_INFO.findFunctionByCode", code);
	}
	
	/**
	 * 添加权限
	 * @param functionInfo 权限对象
	 * @param urlJsonObjects url集合
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addFunctionInfo(SalesFunctionInfo functionInfo, List<JSONObject> urlJsonObjects) {
		myBatisDao.insert("SL_FUNCTION_INFO.insert",functionInfo);
		if(!urlJsonObjects.isEmpty()) {	//如果有Url，保存Url
			for(JSONObject jsonObject : urlJsonObjects) {
				FunctionUrl functionUrl = new FunctionUrl();
				functionUrl.setFunctionId(functionInfo.getFunctionId());
				functionUrl.setUrlInfo(jsonObject.getString("urlInfo"));
				myBatisDao.insert("SL_FUNCTION_INFO.insert",functionUrl);
			}
		}
	}
	
	/**
	 * 编辑权限
	 * @param functionInfo 权限对象
	 * @param urlJsonObjects url集合
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateFunctionInfo(SalesFunctionInfo functionInfo, List<JSONObject> urlJsonObjects) {
		myBatisDao.update("SL_FUNCTION_INFO.updateByPrimaryKey", functionInfo);
	}

	/**
	 * 根据权限ID获取url列表
	 * @param functionId 权限ID
	 */
	@Override
	public List<SalesFunctionInfo> getUrlByFunctionId(Long functionId){
		return myBatisDao.getList("SL_FUNCTION_INFO.getUrlByFunctionId", functionId);
	}
	
}
