package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.SalesAdminInfo;
import com.xt.cfp.core.pojo.SalesOrganizationInfo;
import com.xt.cfp.core.pojo.ext.SalesAdminUserInfoVo;
import com.xt.cfp.core.util.Pagination;

import java.util.List;
import java.util.Map;

public interface SalesAdminInfoService {
	
	/**
	 * 员工分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
    Pagination<SalesAdminInfo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params);
    
    /**
     * 添加员工
     * @param adminInfo 员工对象
     */
    SalesAdminInfo addAdmin(SalesAdminInfo adminInfo);
    
    /**
     * 根据员工ID加载一条数据 
     * @param adminId 员工ID
     */
    SalesAdminInfo getAdminById(Long adminId);
	
    /**
     * 编辑员工信息
     * @param adminInfo 员工ID
     */
    SalesAdminInfo editAdmin(SalesAdminInfo adminInfo);
	
    /**
     * 添加员工信息
     * @param adminInfo 员工对象
     * @param roleId 角色ID
     */
    void addAdminInfo(SalesAdminInfo adminInfo, Long roleId,Long organizeId);
    
    /**
     * 更改员工信息
     * @param adminInfo 员工对象
     */
	void updateAdminInfo(SalesAdminInfo adminInfo);
    /**
     * 更改员工信息
     * @param adminInfo 员工对象
     * @param userRoleId 员工角色ID
     * @param roleId 角色ID
     */
    void updateRoleInfo(SalesAdminInfo adminInfo, Long userRoleId, Long roleId);
	
	/**
	 * 根据登录名获取员工信息
	 * @param loginName 登录名
	 */
	List<SalesAdminInfo> getAdminByLoginName(String loginName);

    /**
	 * 根据登录名获取员工信息
	 * @param adminCode 员工编号
	 */
	List<SalesAdminInfo> getAdminByAdminCode(String adminCode);
	
	/**
	 * 根据自己的id得到所有的下级
	 * @param adminId
	 * @return
	 */
	String getAllSubordinateAdminById(Integer adminId,String adminCode);

    /**
     * 获取该节点下负责人
     * @param organizeId
     * @return
     */
    SalesAdminInfo getLeaderByOrganizeId(Long organizeId);

    /**
     * 根据用户id查找所属客服
     * @param userId
     * @return
     */
    SalesAdminInfo getSalesAdminByUserId(Long userId);

    /**
     * 获得所有员工
     * @return
     */
    List<SalesAdminInfo> getAllSalesAdmin();

    /**
     * 变更用户归属
     * @param userId
     * @param adminId
     */
    void updateAdminUserInfo(Long userId, Long adminId);

    /**
     * 变更记录
     * @param userId
     * @return
     */
    List<SalesAdminUserInfoVo> getChangeRecord(Long userId);
}
