package com.xt.cfp.core.service;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.pojo.FunctionInfo;
import com.xt.cfp.core.pojo.FunctionUrl;
import com.xt.cfp.core.pojo.SalesFunctionInfo;

import java.util.List;

public interface SalesFunctionInfoService {
	
	/**
	 * 获取权限信息列表
	 */
	public List<SalesFunctionInfo> findAllFunction();
	
	/**
	 * 根据权限ID加载一条数据
	 * @param functionId 权限ID
	 */
	public SalesFunctionInfo getFunctionById(Long functionId);
	
	/**
	 * 根据权限父ID获取权限列表
	 * @param pFunctionId 权限父ID
	 */
	public List<SalesFunctionInfo> getFunctionByPId(Long pFunctionId);
	
	/**
	 * 根据权限编码加载一条权限信息
	 * @param code 权限编码
	 */
	public SalesFunctionInfo getFunctionByCode(String code);
	
	/**
	 * 根据角色ID获取权限列表
	 * @param roleId 角色ID
	 */
	public List<SalesFunctionInfo> getFunctionByRoleId(Long roleId);
	
	/**
	 * 添加权限
	 * @param functionInfo 权限对象
	 * @param urlJsonObjects url集合
	 */
	public void addFunctionInfo(SalesFunctionInfo functionInfo, List<JSONObject> urlJsonObjects);
	
	/**
	 * 编辑权限
	 * @param functionInfo 权限对象
	 * @param urlJsonObjects url集合
	 */
	public void updateFunctionInfo(SalesFunctionInfo functionInfo, List<JSONObject> urlJsonObjects);

	/**
	 * 根据权限ID获取url列表
	 * @param functionId 权限ID
	 */
	public List<SalesFunctionInfo> getUrlByFunctionId(Long functionId);
}
