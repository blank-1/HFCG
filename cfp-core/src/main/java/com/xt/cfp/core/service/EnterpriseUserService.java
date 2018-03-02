package com.xt.cfp.core.service;

import java.util.Map;

import com.xt.cfp.core.pojo.EnterpriseUser;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.util.Pagination;

public interface EnterpriseUserService {

	/**
	 * 分页列表
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param params 查询条件
	 */
	Pagination<EnterpriseUser> findAllByPage(int pageNo, int pageSize, Map<String, Object> params);

    /**
     * 添加
     */
	EnterpriseUser addEnterpriseUser(EnterpriseUser enterpriseUser);

    /**
     * 根据ID加载一条数据 
     */
	EnterpriseUser getById(Long enterpriseUserId);

    /**
     * 编辑
     */
	EnterpriseUser editEnterpriseUser(EnterpriseUser enterpriseUser);
	
	/**
	 * 添加企业人员
	 */
	EnterpriseUser addEnterpriseUser(Long enterpriseId, UserInfo userInfo, UserInfoExt userInfoExt);
	
	/**
	 * 编辑企业人员
	 */
	void editEnterpriseUser(UserInfo userInfo, UserInfoExt userInfoExt);
	
}
