package com.xt.cfp.core.service.cfh;

import com.xt.cfp.core.pojo.ext.cfh.CfhUserRelation;

public interface CfhRelationService {

	CfhUserRelation getCfpUserByCfhUserId(Long userId);

	CfhUserRelation getCfpUserById(Long relationId);

	CfhUserRelation addCfhUserRelation(CfhUserRelation cfhUserRelation);

	void updateCfhUserRelation(CfhUserRelation cfhUserRelation);

	/**
	 * 发送当前用户关联的认证信息
	 */
	void sendRelationVerified(Long relationId);

	void sendSms(String mobileNo);

	/**
	 * 财富汇用户有效绑定数据
	 * 
	 * @param relationId
	 * @return
	 */
	CfhUserRelation getEffectiveRelationCfhBind(Long relationId);

	/**
	 * 财富派用户有效绑定数据
	 * 
	 * @param relationId
	 * @return
	 */
	CfhUserRelation getEffectiveRelationCfpBind(Long relationId);

}