package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.RoleFunction;
import com.xt.cfp.core.pojo.SalesRoleFunction;

import java.util.List;

public interface SalesRoleFunctionService {
	
	/**
	 * 根据角色ID获取角色权限列表
	 * @param roleId 角色ID
	 */
	public List<SalesRoleFunction> getRoleFunctionByRoleId(Long roleId);
	
	/**
	 * 添加角色权限
	 * @param roleFunction 角色权限对象
	 * @param functionIdStr 权限集合
	 */
	public void addRoleFunction(SalesRoleFunction roleFunction, String functionIdStr);
}
