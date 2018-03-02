package com.xt.cfp.core.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.AdminRole;
import com.xt.cfp.core.service.AdminInfoService;
import com.xt.cfp.core.util.Pagination;

@Service
public class AdminInfoServiceImpl implements AdminInfoService {
	
	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 员工分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
	@Override
	public Pagination<AdminInfo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<AdminInfo> pagination = new Pagination<AdminInfo>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<AdminInfo> adminInfos = myBatisDao.getListForPaging("findAllAdminInfoByPage", params, pageNo, pageSize);
        pagination.setRows(adminInfos);
        pagination.setTotal(myBatisDao.count("findAllAdminInfoByPage", params));
        return pagination;
	}
	
    /**
     * 添加员工
     * @param adminInfo 员工对象
     */
	@Override
	public AdminInfo addAdmin(AdminInfo adminInfo){
		myBatisDao.insert("ADMIN_INFO.insert", adminInfo);
		return adminInfo;
	}
	
    /**
     * 根据员工ID加载一条数据 
     * @param adminId 员工ID
     */
	@Override
	public AdminInfo getAdminById(Long adminId){
		return myBatisDao.get("ADMIN_INFO.selectByPrimaryKey", adminId);
	}
	
    /**
     * 编辑员工信息
     * @param adminInfo 员工ID
     */
	@Override
	public AdminInfo editAdmin(AdminInfo adminInfo){
		myBatisDao.update("ADMIN_INFO.updateByPrimaryKey", adminInfo);
		return adminInfo;
	}
	
    /**
     * 添加员工信息
     * @param adminInfo 员工对象
     * @param roleId 角色ID
     */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addAdminInfo(AdminInfo adminInfo, Long roleId) {
		
		//添加AdminInfo
		if(adminInfo.getAdminId() != null && adminInfo.getAdminId() == -1){
			adminInfo.setLoginName(adminInfo.getAdminCode());
			adminInfo.setCreateTime(new Date());
			adminInfo.setUpdateTime(new Date());
			myBatisDao.insert("ADMIN_INFO.insert", adminInfo);
		}
		
		if(null != roleId){
			//保存关联到AdminRole
			AdminRole adminRole = new AdminRole();
			adminRole.setAdminId(adminInfo.getAdminId());
			adminRole.setRoleId(roleId);
			adminRole.setRoleState(AdminRole.STATE_ENABLE);
			myBatisDao.insert("ADMIN_ROLE.insert", adminRole);
		}
	}
	
    /**
     * 更改员工信息
     * @param adminInfo 员工对象
     */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateAdminInfo(AdminInfo adminInfo) {
		myBatisDao.update("ADMIN_INFO.updateByPrimaryKey", adminInfo);
	}
	
	/**
	 * 更改员工信息
	 * @param adminInfo 员工对象
	 * @param userRoleId 员工角色ID
	 * @param roleId 角色ID
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateRoleInfo(AdminInfo adminInfo, Long userRoleId, Long roleId) {
		//保存关联到AdminRole
		AdminRole adminRole = new AdminRole();
		adminRole.setAdminRoleId(userRoleId);
		adminRole.setAdminId(adminInfo.getAdminId());
		adminRole.setRoleId(roleId);
		adminRole.setRoleState(AdminRole.STATE_ENABLE);
		myBatisDao.update("ADMIN_ROLE.updateByPrimaryKey", adminRole);
	}
	
	/**
	 * 根据登录名获取员工信息
	 * @param loginName 登录名
	 */
	public List<AdminInfo> getAdminByLoginName(String loginName){
		return myBatisDao.getList("ADMIN_INFO.findAdminByLoginName", loginName);
	}

}
