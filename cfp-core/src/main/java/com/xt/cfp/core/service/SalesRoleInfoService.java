package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.RoleInfo;
import com.xt.cfp.core.pojo.SalesRoleInfo;
import com.xt.cfp.core.util.Pagination;

import java.util.List;
import java.util.Map;

public interface SalesRoleInfoService {
	
	/**
	 * 获取角色分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
    public Pagination<SalesRoleInfo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params);
    
    /**
     * 添加角色
     * @param roleInfo 角色对象
     */
    public SalesRoleInfo addRole(SalesRoleInfo roleInfo);
    
    /**
     * 根据角色ID加载一条数据
     * @param roleId 角色ID
     */
	public SalesRoleInfo getRoleById(Long roleId);
	
	/**
	 * 编辑角色
	 * @param roleInfo 角色ID
	 */
	public SalesRoleInfo editRole(SalesRoleInfo roleInfo);
	
	/**
	 * 根据员工ID获取角色列表
	 * @param adminId 员工ID
	 */
	public List<SalesRoleInfo> getRoleByAdminId(Long adminId);
	
	/**
	 * 获取角色列表
	 */
	public List<SalesRoleInfo> getAllRole();
}
