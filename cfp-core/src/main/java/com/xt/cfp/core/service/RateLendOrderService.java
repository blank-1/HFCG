package com.xt.cfp.core.service;

import com.xt.cfp.core.constants.RateEnum.RateLendOrderStatusEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.RateLendOrder;
import com.xt.cfp.core.pojo.RateUser;
import com.xt.cfp.core.pojo.ext.RateLendOrderVO;

import java.math.BigDecimal;
import java.util.List;

public interface RateLendOrderService {

	/**
	 * 根据订单号查询加息或奖励的关联数据（加息或奖励唯一）
	 * */
	public RateLendOrder findByLendOrderId(Long lendOrderId, String rateType, String status);

	/**
	 * 根据订单号找出所有奖励
	 * 
	 * @param lendOrderId
	 * @return
	 */
	public List<RateLendOrder> findAllByLendOrderId(Long lendOrderId, String status);

	/**
	 * 更新一条加息-订单中间表数据
	 * */
	public void updateRateLendOrder(RateLendOrder rateLendOrder);

	/**
	 * 根据借款标的查询对应的奖励信息和加息信息（有效状态）
	 * */
	public List<RateLendOrder> findByLoanApplicationIdAndRepaymentPlanId(Long loanApplicationId, Long repaymentPlanId);

	/**
	 * insert
	 * */
	public RateLendOrder insert(RateLendOrder rateLendOrder);

	/**
	 * 创建rate_lend_order
	 * 
	 * @param lendOrderId
	 * @param loanApplicationId
	 * @param typeEnum
	 * @param awardPoint
	 * @param rateValue
	 * @param statuEnum
	 */
	public void createRateLendOrder(RateUser rateUser, Long lendOrderId, Long loanApplicationId, RateLendOrderTypeEnum typeEnum, String awardPoint, BigDecimal rateValue,
			RateLendOrderStatusEnum statuEnum);

	/**
	 * 8月活动加息券奖励
	 * 
	 * @param loanApplicationId
	 */
	public void createActivity(Long userId, LendOrder lendOrder, Long loanApplicationId);

	/**
	 * 根据用户ID获取带有债权ID的中间表
	 * */
	public List<RateLendOrderVO> getRateLendOrderCreditByUserId(Long userId);


	List<RateLendOrder> findByLendOrderIdAndType(Long lendOrderId, String rateType);
}
