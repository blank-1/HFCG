package com.xt.cfp.core.service;

import java.util.List;
import java.util.Map;

import com.xt.cfp.core.constants.DisActivityEnums.DisActivityStateEnum;
import com.xt.cfp.core.pojo.DisActivity;
import com.xt.cfp.core.pojo.DisActivityRules;
import com.xt.cfp.core.pojo.ext.DisActivityVO;
import com.xt.cfp.core.util.Pagination;

public interface DisActivityService {

	Pagination<DisActivityVO> getDisActivityListPaging(int pageNo, int pageSize, DisActivityVO disActivityVO, Map<String, Object> customParams);

	void addDisActivity(DisActivity disActivity);

	void addDisActivityInfo(DisActivity disActivity, List<DisActivityRules> rulesList);

	/**
	 * 根据分销活动ID，加载一条信息
	 * 
	 * @param disId
	 *            分销活动ID
	 * @return 一条分销活动
	 */
	DisActivity getDisActivityById(Long disId);

	DisActivityVO getDisActivityVoById(Long disId);

	void updateDisActivityInfo(DisActivity disActivity, List<DisActivityRules> rulesList);

	/**
	 * 查询出借产品已发布的活动
	 * 
	 * @param lendProductId
	 * @param statePublish
	 * @param targetUser
	 * @return
	 */
	List<DisActivityVO> getDisActByStateAndLendProId(Long lendProductId, DisActivityStateEnum statePublish,List<String> targetUser);
	
	/**
	 * 获取分销产品所有涉及的出借产品信息
	 * */
	List<DisActivityVO> getAllDisActivityProducts();
	
	void updateDisActivityState(DisActivity disActivity);

}
