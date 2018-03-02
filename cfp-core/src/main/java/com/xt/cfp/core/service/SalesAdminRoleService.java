package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.AdminRole;
import com.xt.cfp.core.pojo.SalesAdminRole;

import java.util.List;

public interface SalesAdminRoleService {
	
	/**
	 * 添加员工角色关联
	 * @param adminRole 员工角色对象
	 */
	SalesAdminRole addAdminRole(SalesAdminRole adminRole);
	
	/**
	 * 根据员工ID获取员工角色列表
	 * @param adminId 员工ID
	 */
	List<SalesAdminRole> getAdminRoleByAdminId(Long adminId);
	
	/**
	 * 删除员工角色
	 * @param adminRole 员工角色ID
	 */
	void deleteAdminRole(SalesAdminRole adminRole);
}
