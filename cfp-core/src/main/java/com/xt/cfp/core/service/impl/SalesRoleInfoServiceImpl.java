package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.RoleInfo;
import com.xt.cfp.core.pojo.SalesRoleInfo;
import com.xt.cfp.core.service.RoleInfoService;
import com.xt.cfp.core.service.SalesRoleInfoService;
import com.xt.cfp.core.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SalesRoleInfoServiceImpl implements SalesRoleInfoService {

	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 获取角色分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
	@Override
	public Pagination<SalesRoleInfo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<SalesRoleInfo> pagination = new Pagination<SalesRoleInfo>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<SalesRoleInfo> roleInfos = myBatisDao.getListForPaging("findAllSalesRoleInfoByPage", params, pageNo, pageSize);
        pagination.setRows(roleInfos);
        pagination.setTotal(myBatisDao.count("findAllSalesRoleInfoByPage", params));
        return pagination;
	}

    /**
     * 添加角色
     * @param roleInfo 角色对象
     */
	@Override
	public SalesRoleInfo addRole(SalesRoleInfo roleInfo) {
		myBatisDao.insert("SL_ROLE_INFO.insert", roleInfo);
		return roleInfo;
	}
	
    /**
     * 根据角色ID加载一条数据
     * @param roleId 角色ID
     */
	@Override
	public SalesRoleInfo getRoleById(Long roleId){
		return myBatisDao.get("SL_ROLE_INFO.selectByPrimaryKey", roleId);
	}

	/**
	 * 编辑角色
	 * @param roleInfo 角色ID
	 */
	@Override
	public SalesRoleInfo editRole(SalesRoleInfo roleInfo) {
		myBatisDao.update("SL_ROLE_INFO.updateByPrimaryKey", roleInfo);
		return roleInfo;
	}
	
	/**
	 * 根据员工ID获取角色列表
	 * @param adminId 员工ID
	 */
	@Override
	public List<SalesRoleInfo> getRoleByAdminId(Long adminId){
		return myBatisDao.getList("SL_ROLE_INFO.findRoleByAdminId", adminId);
	}
	
	/**
	 * 获取角色列表
	 */
	@Override
	public List<SalesRoleInfo> getAllRole(){
		return myBatisDao.getList("SL_ROLE_INFO.findAllRole");
	}

}
