package com.xt.cfp.core.service;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.pojo.SalesAdminInfo;
import com.xt.cfp.core.pojo.SalesAdminOrganizetion;
import com.xt.cfp.core.pojo.SalesFunctionInfo;
import com.xt.cfp.core.pojo.SalesOrganizationInfo;

import java.util.List;

public interface SalesOrganizeInfoService {
	
	/**
	 * 获取权限信息列表
	 */
	public List<SalesOrganizationInfo> findAllSalesOrganizetionInfo();
	
	/**
	 * 根据权限ID加载一条数据
	 * @param organizeId 权限ID
	 */
	public SalesOrganizationInfo getOrganizeById(Long organizeId);
	
	/**
	 * 根据权限父ID获取权限列表
	 * @param parentId 权限父ID
	 */
	public List<SalesOrganizationInfo> getOrganizeByPId(Long parentId);
	
	/**
	 * 根据权限编码加载一条权限信息
	 * @param code 权限编码
	 */
	public SalesOrganizationInfo getOrganizeByCode(String code);
	
	/**
	 * 根据角色ID获取权限列表
	 * @param roleId 角色ID
	 */
	public List<SalesFunctionInfo> getFunctionByRoleId(Long roleId);
	
	/**
	 * 添加权限
	 * @param functionInfo 权限对象
	 */
	public void addFunctionInfo(SalesOrganizationInfo functionInfo);
	
	/**
	 * 编辑权限
	 */
	public void updateOrganizeInfo(SalesOrganizationInfo functionInfo);

	/**
	 * 根据权限ID获取url列表
	 * @param functionId 权限ID
	 */
	public List<SalesFunctionInfo> getUrlByFunctionId(Long functionId);

	/**
	 * 根据用户获取组织机构
	 * @param adminId
	 * @return
	 */
	public SalesAdminOrganizetion getOrganizetionInfobyAdminId(Long adminId);


	/**
	 * 更新组织机构和人员的关系
	 * @param adminOrganizetion
	 */
	void updateAdminOrganizetion(SalesAdminOrganizetion adminOrganizetion);

	/**
	 * 获取组织用户数
	 * @param organizeId
	 * @return
	 */
	int getUserCountByOrganizeId(Long organizeId);


	/**
	 * 获取所属组织节点
	 * @param adminId
	 * @return
	 */
	SalesOrganizationInfo getSupOrganizetionByAdminId(Long adminId);


}
