package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.SalesAdminUserInfoVo;
import com.xt.cfp.core.service.AdminInfoService;
import com.xt.cfp.core.service.SalesAdminInfoService;
import com.xt.cfp.core.util.Pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SalesAdminInfoServiceImpl implements SalesAdminInfoService {
	
	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 员工分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
	@Override
	public Pagination<SalesAdminInfo> findAllByPage(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<SalesAdminInfo> pagination = new Pagination<SalesAdminInfo>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<SalesAdminInfo> adminInfos = myBatisDao.getListForPaging("findAllSalesAdminInfoByPage", params, pageNo, pageSize);
        pagination.setRows(adminInfos);
        pagination.setTotal(myBatisDao.count("findAllSalesAdminInfoByPage", params));
        return pagination;
	}

    /**
     * 添加员工
     * @param adminInfo 员工对象
     */
	@Override
	public SalesAdminInfo addAdmin(SalesAdminInfo adminInfo){
		myBatisDao.insert("SL_ADMIN_INFO.insert", adminInfo);
		return adminInfo;
	}

    /**
     * 根据员工ID加载一条数据
     * @param adminId 员工ID
     */
	@Override
	public SalesAdminInfo getAdminById(Long adminId){
		return myBatisDao.get("SL_ADMIN_INFO.selectByPrimaryKey", adminId);
	}

    /**
     * 编辑员工信息
     * @param adminInfo 员工ID
     */
	@Override
	public SalesAdminInfo editAdmin(SalesAdminInfo adminInfo){
		myBatisDao.update("SL_ADMIN_INFO.updateByPrimaryKey", adminInfo);
		return adminInfo;
	}

    /**
     * 添加员工信息
     * @param adminInfo 员工对象
     * @param roleId 角色ID
     */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addAdminInfo(SalesAdminInfo adminInfo, Long roleId,Long organizeId) {

		//添加AdminInfo
		if(adminInfo.getAdminId() != null && adminInfo.getAdminId() == -1){
			adminInfo.setCreateTime(new Date());
			adminInfo.setUpdateTime(new Date());
			myBatisDao.insert("SL_ADMIN_INFO.insert", adminInfo);
		}

		if(null != roleId){
			//保存关联到AdminRole
			SalesAdminRole adminRole = new SalesAdminRole();
			adminRole.setAdminId(adminInfo.getAdminId());
			adminRole.setRoleId(roleId);
			adminRole.setRoleState(AdminRole.STATE_ENABLE);
			myBatisDao.insert("SL_ADMIN_ROLE.insert", adminRole);
		}

		//组织机构保存
		if(null != organizeId){
			//保存关联到AdminRole
			SalesAdminOrganizetion adminOrganizetion = new SalesAdminOrganizetion();
			adminOrganizetion.setAdminId(adminInfo.getAdminId());
			adminOrganizetion.setOrganizeId(organizeId);
			myBatisDao.insert("SL_ADMIN_ORGANIZATION.insert", adminOrganizetion);
		}
	}

    /**
     * 更改员工信息
     * @param adminInfo 员工对象
     */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateAdminInfo(SalesAdminInfo adminInfo) {
		myBatisDao.update("SL_ADMIN_INFO.updateByPrimaryKey", adminInfo);
	}

	/**
	 * 更改员工信息
	 * @param adminInfo 员工对象
	 * @param userRoleId 员工角色ID
	 * @param roleId 角色ID
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateRoleInfo(SalesAdminInfo adminInfo, Long userRoleId, Long roleId) {
		//保存关联到AdminRole
		SalesAdminRole adminRole = new SalesAdminRole();
		adminRole.setAdminRoleId(userRoleId);
		adminRole.setAdminId(adminInfo.getAdminId());
		adminRole.setRoleId(roleId);
		adminRole.setRoleState(AdminRole.STATE_ENABLE);
		myBatisDao.update("SL_ADMIN_ROLE.updateByPrimaryKey", adminRole);
	}

	/**
	 * 根据登录名获取员工信息
	 * @param loginName 登录名
	 */
	public List<SalesAdminInfo> getAdminByLoginName(String loginName){
		return myBatisDao.getList("SL_ADMIN_INFO.findAdminByLoginName", loginName);
	}

	@Override
	public List<SalesAdminInfo> getAdminByAdminCode(String adminCode) {
		return myBatisDao.getList("SL_ADMIN_INFO.getAdminByAdminCode", adminCode);
	}

	@Override
	public String getAllSubordinateAdminById(Integer adminId,String adminCode) {
		int totalCount = myBatisDao.get("SL_ADMIN_INFO.getAllSubordinateNum", adminId);
		if(totalCount==0){
			return "'"+adminCode+"'";
		}
		StringBuffer sb=new StringBuffer();
		List<String> codes=myBatisDao.getList("SL_ADMIN_INFO.getAllSubordinateCodeAdminById", adminId);
		for (int i = 0; i < codes.size(); i++) {
			if(i==codes.size()-1){
				sb.append("'"+codes.get(i)+"'");
			}else{
				sb.append("'"+codes.get(i)+"'"+",");
			}
		}
		return sb.toString();
	}

	@Override
	public SalesAdminInfo getLeaderByOrganizeId(Long organizeId) {
		List<SalesAdminInfo> adminList = myBatisDao.getList("SL_ADMIN_INFO.getLeaderByOrganizeId", organizeId);
		if (adminList!=null&&adminList.size()>0)
			return adminList.get(0);
		return null;
	}

	@Override
	public SalesAdminInfo getSalesAdminByUserId(Long userId) {
		return myBatisDao.get("SL_ADMIN_INFO.getSalesAdminByUserId",userId);
	}

	@Override
	public List<SalesAdminInfo> getAllSalesAdmin() {
		return myBatisDao.getList("SL_ADMIN_INFO.getAllSalesAdmin", null);
	}

	@Override
	@Transactional
	public void updateAdminUserInfo(Long userId, Long adminId) {
		 // 把历史归属置为无效
		SalesAdminUserInfo adminUserInfo = myBatisDao.get("SL_ADMIN_USER_INFO.getAdminUserInfoByUserId", userId);
		adminUserInfo.setStatus("1");//设置为无效
		adminUserInfo.setEndTime(new Date());
		myBatisDao.update("SL_ADMIN_USER_INFO.updateByPrimaryKeySelective",adminUserInfo);

		//新添加一条记录
		SalesAdminUserInfo newInfo  = new SalesAdminUserInfo();
		newInfo.setUserId(userId);
		newInfo.setStatus("0");//有效
		newInfo.setAdminId(adminId);
		newInfo.setCreateTime(new Date());
		myBatisDao.insert("SL_ADMIN_USER_INFO.insertSelective",newInfo);
	}

	@Override
	public List<SalesAdminUserInfoVo> getChangeRecord(Long userId) {
		List<SalesAdminUserInfoVo> adminList = myBatisDao.getList("SL_ADMIN_USER_INFO.getChangeRecord", userId);
		return adminList;
	}

}
