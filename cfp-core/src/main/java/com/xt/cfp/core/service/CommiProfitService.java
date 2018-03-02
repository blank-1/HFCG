package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.CommiProfit;
import com.xt.cfp.core.pojo.ext.CommitProfitVO;

public interface CommiProfitService {
	
	/**
	 * 创建一条佣金收益统计
	 * @param commiProfit
	 * @return
	 */
	CommiProfit addCommiProfit(CommiProfit commiProfit);
	
	/**
	 * 修改一条佣金收益统计
	 * @param commiProfit
	 * @return
	 */
	CommiProfit updateCommiProfit(CommiProfit commiProfit);
	
	/**
	 * 投标创建佣金收益统计（一笔最多三条）
	 * @param lendOrderId 出借产品订单ID
	 */
	void createCommiProfit(Long lendOrderId) throws Exception;
	
	/**
	 * 根据订单id查询收益信息
	 * */
	List<CommitProfitVO> getCommiProfitByLendOrderId(Long lendOrderId, Long lendUserId);

	/**
	 * 根据主键查询
	 * */
	CommiProfit selectByPrimaryKey(Long primaryId);
	
	/***
	 * 计算用户三级分销中的总已获收益
	 * @author hu
	 * @param userId
	 * @return 收益总额
	 */
	BigDecimal calUserAccountProfit(Long userId);
	
	/**
	 * 计算用户三级分销中的总应得收益
	 * @param userId
	 * @return
	 */
	BigDecimal calUserAccountShouldProfit(Long userId);
	
	/**
	 * 投标创建佣金收益统计（一笔最多三条）
	 * @param lendOrderId 出借产品订单ID
	 */
	void createCommiProfit(Long lendOrderId, Date startTime, Date endTime) throws Exception;

	/**
	 * 根据map中的参数查询佣金信息
	 * */
	List<CommiProfit> getCommiProfitByParams(Map<String, Object> paramMap);
}
