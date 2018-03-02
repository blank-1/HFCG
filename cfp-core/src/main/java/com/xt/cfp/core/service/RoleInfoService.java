package com.xt.cfp.core.service;

import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.RoleInfo;
import com.xt.cfp.core.util.Pagination;

public interface RoleInfoService {
	
	/**
	 * 获取角色分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
    public Pagination<RoleInfo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params);
    
    /**
     * 添加角色
     * @param roleInfo 角色对象
     */
    public RoleInfo addRole(RoleInfo roleInfo);
    
    /**
     * 根据角色ID加载一条数据
     * @param roleId 角色ID
     */
	public RoleInfo getRoleById(Long roleId);
	
	/**
	 * 编辑角色
	 * @param roleInfo 角色ID
	 */
	public RoleInfo editRole(RoleInfo roleInfo);
	
	/**
	 * 根据员工ID获取角色列表
	 * @param adminId 员工ID
	 */
	public List<RoleInfo> getRoleByAdminId(Long adminId);
	
	/**
	 * 获取角色列表
	 */
	public List<RoleInfo> getAllRole();

	/**
	 * 是否是客服人员
	 * @param adminId
	 * @return
	 */
	boolean isCustomerServiceRole(Long adminId);
}
