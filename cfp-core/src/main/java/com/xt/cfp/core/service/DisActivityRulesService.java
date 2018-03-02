package com.xt.cfp.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.DisActivityRules;
import com.xt.cfp.core.pojo.ext.CommitProfitVO;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.pojo.ext.DisActivityRulesExt;


public interface DisActivityRulesService {

	void addDisActivityRules(DisActivityRules disActivityRules);


	/**
	 * 根据出借产品ID，查询规则列表
	 * 
	 * @param lendProductId
	 *            出借产品ID
	 * @return 规则列表
	 */
	List<DisActivityRulesExt> getDisActivityRulesByLendProductId(Long lendProductId);

	/**
	 * 
	 * @param productId
	 * @author wangyadong
	 * @return
	 */

	Pagination<CommitProfitVO> getDisActivityRulesByProductIds(int pageNo, int pageSize, Map<String, Object> customParams);


	/**
	 * 根据分销活动ID，查询规则列表
	 * 
	 * @param disId
	 *            分销活动ID
	 * @return 规则列表
	 */
	List<DisActivityRulesExt> getDisActivityRulesByDisId(Long disId);

	/**
	 * 校验活动下是否有相同出借产品
	 * 
	 * @param disId
	 * @return
	 */
	List<DisActivityRules> checkLendProIdCountByDisId(Long disId);

	/**
	 * 根据出借产品ID，查询规则列表
	 * 
	 * @param lendProductId
	 *            出借产品ID
	 * @return 规则列表
	 */
	List<DisActivityRules> getDisActivityRulesByLendProductIdAndTime(
			Long lendProductId, Date startTime, Date endTime);

}
