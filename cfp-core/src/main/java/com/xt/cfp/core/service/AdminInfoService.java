package com.xt.cfp.core.service;

import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.util.Pagination;

public interface AdminInfoService {
	
	/**
	 * 员工分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
    Pagination<AdminInfo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params);
    
    /**
     * 添加员工
     * @param adminInfo 员工对象
     */
    AdminInfo addAdmin(AdminInfo adminInfo);
    
    /**
     * 根据员工ID加载一条数据 
     * @param adminId 员工ID
     */
    AdminInfo getAdminById(Long adminId);
	
    /**
     * 编辑员工信息
     * @param adminInfo 员工ID
     */
    AdminInfo editAdmin(AdminInfo adminInfo);
	
    /**
     * 添加员工信息
     * @param adminInfo 员工对象
     * @param roleId 角色ID
     */
    void addAdminInfo(AdminInfo adminInfo, Long roleId);
    
    /**
     * 更改员工信息
     * @param adminInfo 员工对象
     */
	void updateAdminInfo(AdminInfo adminInfo);
	
	/**
	 * 更改员工信息
	 * @param adminInfo 员工对象
	 * @param userRoleId 员工角色ID
	 * @param roleId 角色ID
	 */
	void updateRoleInfo(AdminInfo adminInfo, Long userRoleId, Long roleId);
	
	/**
	 * 根据登录名获取员工信息
	 * @param loginName 登录名
	 */
	List<AdminInfo> getAdminByLoginName(String loginName);
}
