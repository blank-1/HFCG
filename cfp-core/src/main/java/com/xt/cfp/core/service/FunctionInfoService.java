package com.xt.cfp.core.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.pojo.FunctionInfo;
import com.xt.cfp.core.pojo.FunctionUrl;

public interface FunctionInfoService {
	
	/**
	 * 获取权限信息列表
	 */
	public List<FunctionInfo> findAllFunction();
	
	/**
	 * 根据权限ID加载一条数据
	 * @param functionId 权限ID
	 */
	public FunctionInfo getFunctionById(Long functionId);
	
	/**
	 * 根据权限父ID获取权限列表
	 * @param pFunctionId 权限父ID
	 */
	public List<FunctionInfo> getFunctionByPId(Long pFunctionId);
	
	/**
	 * 根据权限编码加载一条权限信息
	 * @param code 权限编码
	 */
	public FunctionInfo getFunctionByCode(String code);
	
	/**
	 * 根据角色ID获取权限列表
	 * @param roleId 角色ID
	 */
	public List<FunctionInfo> getFunctionByRoleId(Long roleId);
	
	/**
	 * 添加权限
	 * @param functionInfo 权限对象
	 * @param urlJsonObjects url集合
	 */
	public void addFunctionInfo(FunctionInfo functionInfo, List<JSONObject> urlJsonObjects);
	
	/**
	 * 编辑权限
	 * @param functionInfo 权限对象
	 * @param urlJsonObjects url集合
	 */
	public void updateFunctionInfo(FunctionInfo functionInfo, List<JSONObject> urlJsonObjects);

	/**
	 * 根据权限ID获取url列表
	 * @param functionId 权限ID
	 */
	public List<FunctionUrl> getUrlByFunctionId(Long functionId);
}
