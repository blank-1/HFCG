package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.constants.RateEnum.RateUsageHisStateEnum;
import com.xt.cfp.core.pojo.RateUsageHistory;
import com.xt.cfp.core.pojo.RateUser;
import com.xt.cfp.core.pojo.ext.RateUsageHistoryVO;

public interface RateUsageHistoryService {

	/**
	 * 更新加息历史数据
	 * */
	public void updateRateUsageHistory(RateUsageHistory rateUsageHistory);

	/**
	 * 查询加息历史数据-根据订单ID和加息用户券ID，有效状态
	 * */
	public List<RateUsageHistory> findByParams(Long lendOrderId, Long rateUserId, String status);

	/**
	 * insert
	 */
	public RateUsageHistory insert(RateUsageHistory rateUsageHistory);

	/**
	 * insert
	 */
	public void insertRateUsageHis(RateUser rateUser, Long userId, Long lendOrderId, Long loanApplicationId, RateUsageHisStateEnum state);
	
	/**
	 * 根据发放ID，查询使用历史
	 * @param rateUserId
	 * @return
	 */
	List<RateUsageHistoryVO> getHistoryByRateUserId(Long rateUserId);
	
}
