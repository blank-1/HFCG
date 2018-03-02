package com.xt.cfp.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.AdminRole;
import com.xt.cfp.core.service.AdminRoleService;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {
	
	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 添加员工角色关联
	 * @param adminRole 员工角色对象
	 */
	@Override
	public AdminRole addAdminRole(AdminRole adminRole) {
		myBatisDao.insert("ADMIN_ROLE.insert", adminRole);
		return adminRole;
	}
	
	/**
	 * 根据员工ID获取员工角色列表
	 * @param adminId 员工ID
	 */
	@Override
	public List<AdminRole> getAdminRoleByAdminId(Long adminId){
		return myBatisDao.getList("ADMIN_ROLE.getAdminRoleByAdminId", adminId);
	}
	
	/**
	 * 删除员工角色
	 * @param adminRole 员工角色ID
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void deleteAdminRole(AdminRole adminRole) {
		myBatisDao.delete("ADMIN_ROLE.deleteByPrimaryKey", adminRole.getAdminRoleId());
	}
}
