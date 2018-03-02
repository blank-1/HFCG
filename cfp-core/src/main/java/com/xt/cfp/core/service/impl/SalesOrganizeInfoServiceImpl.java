package com.xt.cfp.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.SalesFunctionInfoService;
import com.xt.cfp.core.service.SalesOrganizeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalesOrganizeInfoServiceImpl implements SalesOrganizeInfoService {
	
	@Autowired
	private MyBatisDao myBatisDao;

	/**
	 * 获取权限信息列表
	 */
	@Override
	public List<SalesOrganizationInfo> findAllSalesOrganizetionInfo(){
		return myBatisDao.getList("SL_ORGANIZATION_INFO.findAllSalesOrganizetionInfo");
	}
	
	/**
	 * 根据权限ID加载一条数据
	 * @param organizeId 权限ID
	 */
	@Override
	public SalesOrganizationInfo getOrganizeById(Long organizeId){
		return myBatisDao.get("SL_ORGANIZATION_INFO.selectByPrimaryKey", organizeId);
	}
	
	/**
	 * 根据角色ID获取权限列表
	 * @param roleId 角色ID
	 */
	@Override
	public List<SalesFunctionInfo> getFunctionByRoleId(Long roleId){
		return myBatisDao.getList("SL_FUNCTION_INFO.findFunctionByRoleId", roleId);
	}
	
	/**
	 * 根据权限父ID获取权限列表
	 * @param parentId 权限父ID
	 */
	@Override
	public List<SalesOrganizationInfo> getOrganizeByPId(Long parentId){
		return myBatisDao.getList("SL_ORGANIZATION_INFO.getOrganizeByPId", parentId);
	}
	
	/**
	 * 根据权限编码加载一条权限信息
	 * @param code 权限编码
	 */
	@Override
	public SalesOrganizationInfo getOrganizeByCode(String code){
		return myBatisDao.get("SL_ORGANIZATION_INFO.getOrganizeByCode", code);
	}
	
	/**
	 * 添加权限
	 * @param functionInfo 权限对象
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addFunctionInfo(SalesOrganizationInfo functionInfo) {
		myBatisDao.insert("SL_ORGANIZATION_INFO.insert",functionInfo);
	}
	
	/**
	 * 编辑权限
	 * @param functionInfo 权限对象
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateOrganizeInfo(SalesOrganizationInfo functionInfo) {
		myBatisDao.update("SL_ORGANIZATION_INFO.updateByPrimaryKey", functionInfo);
	}

	/**
	 * 根据权限ID获取url列表
	 * @param functionId 权限ID
	 */
	@Override
	public List<SalesFunctionInfo> getUrlByFunctionId(Long functionId){
		return myBatisDao.getList("SL_FUNCTION_INFO.getUrlByFunctionId", functionId);
	}

	@Override
	public SalesAdminOrganizetion getOrganizetionInfobyAdminId(Long adminId) {
		return myBatisDao.get("SL_ADMIN_ORGANIZATION.getOrganizetionInfobyAdminId", adminId);
	}

	@Override
	public void updateAdminOrganizetion(SalesAdminOrganizetion adminOrganizetion) {
		myBatisDao.update("SL_ADMIN_ORGANIZATION.updateByPrimaryKey", adminOrganizetion);
	}

	@Override
	public int getUserCountByOrganizeId(Long organizeId) {
		return myBatisDao.get("SL_ADMIN_ORGANIZATION.getUserCountByOrganizeId", organizeId);
	}


	@Override
	public SalesOrganizationInfo getSupOrganizetionByAdminId(Long adminId) {
		SalesAdminOrganizetion sao = getOrganizetionInfobyAdminId(adminId);
		if (sao!=null){

			SalesOrganizationInfo soi = getOrganizeById(sao.getOrganizeId());
			return getOrganizeById(soi.getParentId());
		}
		return null;
	}


}
