package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.AdminRole;

public interface AdminRoleService {
	
	/**
	 * 添加员工角色关联
	 * @param adminRole 员工角色对象
	 */
	AdminRole addAdminRole(AdminRole adminRole);
	
	/**
	 * 根据员工ID获取员工角色列表
	 * @param adminId 员工ID
	 */
	List<AdminRole> getAdminRoleByAdminId(Long adminId);
	
	/**
	 * 删除员工角色
	 * @param adminRole 员工角色ID
	 */
	void deleteAdminRole(AdminRole adminRole);
}
