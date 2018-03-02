package com.xt.cfp.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.util.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.RoleInfo;
import com.xt.cfp.core.service.RoleInfoService;
import com.xt.cfp.core.util.Pagination;

@Service
public class RoleInfoServiceImpl implements RoleInfoService {

	@Autowired
	private MyBatisDao myBatisDao;

	private List<Long> customerServiceRoleIds;

	@Property(name = "CUSTOMER_SERVICE_ROLE_IDS")
	public void setCustomerServiceRoleIds(String customerServiceRoleIds) {
		this.customerServiceRoleIds = new ArrayList<>();
		if (customerServiceRoleIds != null) {
			String[] split = customerServiceRoleIds.split(",");
			for (String ss : split) {
				this.customerServiceRoleIds.add(Long.valueOf(ss));
			}
		}
	}

	/**
	 * 获取角色分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
	@Override
	public Pagination<RoleInfo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<RoleInfo> pagination = new Pagination<RoleInfo>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<RoleInfo> roleInfos = myBatisDao.getListForPaging("findAllRoleInfoByPage", params, pageNo, pageSize);
        pagination.setRows(roleInfos);
        pagination.setTotal(myBatisDao.count("findAllRoleInfoByPage", params));
        return pagination;
	}

    /**
     * 添加角色
     * @param roleInfo 角色对象
     */
	@Override
	public RoleInfo addRole(RoleInfo roleInfo) {
		myBatisDao.insert("ROLE_INFO.insert", roleInfo);
		return roleInfo;
	}
	
    /**
     * 根据角色ID加载一条数据
     * @param roleId 角色ID
     */
	@Override
	public RoleInfo getRoleById(Long roleId){
		return myBatisDao.get("ROLE_INFO.selectByPrimaryKey", roleId);
	}

	/**
	 * 编辑角色
	 * @param roleInfo 角色ID
	 */
	@Override
	public RoleInfo editRole(RoleInfo roleInfo) {
		myBatisDao.update("ROLE_INFO.updateByPrimaryKey", roleInfo);
		return roleInfo;
	}
	
	/**
	 * 根据员工ID获取角色列表
	 * @param adminId 员工ID
	 */
	@Override
	public List<RoleInfo> getRoleByAdminId(Long adminId){
		return myBatisDao.getList("ROLE_INFO.findRoleByAdminId", adminId);
	}
	
	/**
	 * 获取角色列表
	 */
	@Override
	public List<RoleInfo> getAllRole(){
		return myBatisDao.getList("ROLE_INFO.findAllRole");
	}

	@Override
	public boolean isCustomerServiceRole(Long adminId) {
		boolean result = false;

		List<RoleInfo> roleInfoList = getRoleByAdminId(adminId);
		for (RoleInfo roleInfo : roleInfoList) {
			if (customerServiceRoleIds.contains(roleInfo.getRoleId())) {
				result = true;
				break;
			}
		}

		return result;
	}

}
