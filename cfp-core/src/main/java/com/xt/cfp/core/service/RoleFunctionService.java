package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.RoleFunction;

public interface RoleFunctionService {
	
	/**
	 * 根据角色ID获取角色权限列表
	 * @param roleId 角色ID
	 */
	public List<RoleFunction> getRoleFunctionByRoleId(Long roleId);
	
	/**
	 * 添加角色权限
	 * @param roleFunction 角色权限对象
	 * @param functionIdStr 权限集合
	 */
	public void addRoleFunction(RoleFunction roleFunction,String functionIdStr);
}
