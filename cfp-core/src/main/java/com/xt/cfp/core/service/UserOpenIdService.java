package com.xt.cfp.core.service;


import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserOpenId;

import java.util.Map;


public interface UserOpenIdService {

	/**
	 * 添加openid和登录时间
	 */
	UserOpenId addUserOpenId(UserInfo userInfo, UserOpenId userOpenId);
	/**
	 * 添加openid
	 * @param userInfo
	 * @param userOpenId
	 * @return
	 */
	UserOpenId addUserOpenId(UserOpenId userOpenId);
	
	/**
	 * 查找openid
	 */
	UserOpenId getUserOpenId(String openId);
	/**
	 * 修改
	 * @param userInfo
	 */
	void updateUserOpenId(UserOpenId oldUserOpenId, UserInfo userInfo, UserOpenId newUserOpenId);
	/**
	 * 通过userid查找openid
	 * @param userId
	 * @return
	 */
	UserOpenId getUserOpenIdByUserId(Long userId);
	/**
	 * 查找openid
	 * @param userId
	 * @return
	 */
	UserOpenId getOpenIdByCondition(Long userId, String openId, String type);
	
	Map<String,String> getConfig(String url);

	void deleteOpenId(UserOpenId userOpenId);

}
