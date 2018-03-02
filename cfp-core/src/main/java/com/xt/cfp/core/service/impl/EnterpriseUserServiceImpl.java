package com.xt.cfp.core.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.constants.UserType;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.EnterpriseUser;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.service.EnterpriseUserService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.Pagination;

@Service
public class EnterpriseUserServiceImpl implements EnterpriseUserService {
	
    @Autowired
    private MyBatisDao myBatisDao;
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private UserInfoExtService userInfoExtService;

	@Override
	public Pagination<EnterpriseUser> findAllByPage(int pageNo, int pageSize,
			Map<String, Object> params) {
		Pagination<EnterpriseUser> pagination = new Pagination<EnterpriseUser>();
        pagination.setCurrentPage(pageNo);
        pagination.setPageSize(pageSize);
        List<EnterpriseUser> enterpriseUsers = myBatisDao.getListForPaging("findAllEnterpriseUserByPage", params, pageNo, pageSize);
        pagination.setRows(enterpriseUsers);
        pagination.setTotal(myBatisDao.count("findAllEnterpriseUserByPage", params));
        return pagination;
	}

	@Override
	public EnterpriseUser addEnterpriseUser(EnterpriseUser enterpriseUser) {
		myBatisDao.insert("ENTERPRISE_USER.insert", enterpriseUser);
		return enterpriseUser;
	}

	@Override
	public EnterpriseUser getById(Long enterpriseId) {
		return myBatisDao.get("ENTERPRISE_USER.selectByPrimaryKey", enterpriseId);
	}

	@Override
	public EnterpriseUser editEnterpriseUser(EnterpriseUser enterpriseUser) {
		myBatisDao.update("ENTERPRISE_USER.updateByPrimaryKey", enterpriseUser);
		return enterpriseUser;
	}

	@Override
	@Transactional
	public EnterpriseUser addEnterpriseUser(Long enterpriseId, UserInfo userInfo, UserInfoExt userInfoExt) {
		
        // 用户信息
		userInfo.setType(UserType.ENTERPRISE_USER.getValue());// 设置为企业人员用户
		userInfo = userInfoService.regist(userInfo, UserSource.PLATFORM);

        // 用户扩展
		userInfoExt.setUserId(userInfo.getUserId());
        userInfoExtService.updateUserInfoExt(userInfoExt);
        
        // 企业人员关联表
        EnterpriseUser enterpriseUser = new EnterpriseUser();
        enterpriseUser.setEnterpriseId(enterpriseId);
        enterpriseUser.setUserId(userInfo.getUserId());
        enterpriseUser.setCreateTime(new Date());
        enterpriseUser = this.addEnterpriseUser(enterpriseUser);
		
		return enterpriseUser;
	}

	@Override
	@Transactional
	public void editEnterpriseUser(UserInfo userInfo,
			UserInfoExt userInfoExt) {
		userInfoService.updateUser(userInfo);
		userInfoExtService.updateUserInfoExt(userInfoExt);
	}
	
}
