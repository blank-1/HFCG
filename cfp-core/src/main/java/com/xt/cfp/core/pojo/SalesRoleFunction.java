package com.xt.cfp.core.pojo;
/**
 * 角色权限
 */
public class SalesRoleFunction {
	private Long roleFunctionId;//角色权限ID[主键]
	private Long functionId;//权限ID[外键]
	private Long roleId;//角色ID[外键]
	
	public Long getRoleFunctionId() {
		return roleFunctionId;
	}
	public void setRoleFunctionId(Long roleFunctionId) {
		this.roleFunctionId = roleFunctionId;
	}
	public Long getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
