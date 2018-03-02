package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.AdminRole;
import com.xt.cfp.core.pojo.SalesAdminRole;
import com.xt.cfp.core.service.AdminRoleService;
import com.xt.cfp.core.service.SalesAdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalesAdminRoleServiceImpl implements SalesAdminRoleService {
	
	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 添加员工角色关联
	 * @param adminRole 员工角色对象
	 */
	@Override
	public SalesAdminRole addAdminRole(SalesAdminRole adminRole) {
		myBatisDao.insert("SL_ADMIN_ROLE.insert", adminRole);
		return adminRole;
	}
	
	/**
	 * 根据员工ID获取员工角色列表
	 * @param adminId 员工ID
	 */
	@Override
	public List<SalesAdminRole> getAdminRoleByAdminId(Long adminId){
		return myBatisDao.getList("SL_ADMIN_ROLE.getAdminRoleByAdminId", adminId);
	}
	
	/**
	 * 删除员工角色
	 * @param adminRole 员工角色ID
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void deleteAdminRole(SalesAdminRole adminRole) {
		myBatisDao.delete("SL_ADMIN_ROLE.deleteByPrimaryKey", adminRole.getAdminRoleId());
	}
}
