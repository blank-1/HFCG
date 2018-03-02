package com.xt.cfp.core.service;

import com.xt.cfp.core.constants.LendProductPublishStateEnum;
import com.xt.cfp.core.context.ParaContext;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.*;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.Pair;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LendProductService {

	/**
	 * 计算费用
	 * @param lendProductFeesItem
	 * @param allCalital
	 * @param allInterest
	 * @param currentCalital
	 * @param currentInterest
	 * @param allProfit
	 * @param currentProfit
	 * @return
	 */
	BigDecimal calculateLendProductFeesItemBalance(LendProductFeesItem lendProductFeesItem, BigDecimal allCalital, BigDecimal allInterest,
												   BigDecimal currentCalital, BigDecimal currentInterest, BigDecimal allProfit, BigDecimal currentProfit);

	void addLendProduct(LendProduct lendProduct, List<LendProductFeesItem> lendProductFeesItems,
						List<LendProductLadderDiscount> ladderDiscounts, List<LendLoanBinding> lendLoanBindings);

	Pagination<LendProductVO> findAllByPage(int page, int limit, LendProductVO parameter);

	Pagination<LendProductPublish> findAllPublishByPage(int page, int limit, Map map);

	LendProduct findById(long lendProductId);

	List<LendProductPublish> findLendProductPublishsByProductId(long lendProductId);

	// 处理出借产品查看详情
	List<ConstantDefine> findLendProductAndFees(long lendProductId);

	void updateProductState(LendProduct lendProduct);

	int getMaxPublishCodeByLendProductId(long lendProductId);

	// 处理初始化列表时期限的回显查询
	List<LendProduct> findTimeLimit(LendProduct lendProduct);

	List<LendProduct> findProfitRate();

	void addProductPublish(LendProductPublish lendProductPublish, List<LPPublishChannelDetail> lpPublishChannelDetails);

	void updateLendProduct(LendProduct lendProduct, List<LendProductFeesItem> lendProductFeesItems,
						   List<LendProductLadderDiscount> ladderDiscounts, List<LendLoanBinding> lendLoanBindings);

	List<LendProduct> findProductVersionByName(LendProduct lendProduct);

	List<LendProductFeesItem> findAllProductFeesItems();

	/**
	 *通过出借产品订单id查询出借人应缴费用
	 * @return
	 */
	List<LendProductFeesItem> findAllProductFeesItemsByLendOrderId(long lendOrderId);

	List<LendProductLadderDiscountFees> findByLendProductLDFId(long lendProductId);

	/**
	 * 根据出借产品Id查询该产品当前的发布信息
	 * 
	 * @param productId
	 * @return
	 */
	LendProductPublish findCurrentProductPublishByProductId(Long productId);

	/**
	 * 为我要理财页面提供省心计划列表
	 * 
	 * @return
	 */
	List<LproductWithBalanceStatus> findFinanceProductListForWeb();

	List<LendProductPublish> findLendProductPublishBy(Map map);

	/**
	 * 根据发布明细id查询出借产品发布明细信息
	 * @param publishId
	 * @return
	 */
	LendProductPublish getLendProductPublishByPublishId(Long publishId);

	/**
	 * 用余额购买省心计划
	 * @param userId
	 * @param financePublishId
	 * @param amount
	 * @return
	 */
	PayResult buyFinanceByAccountBalance(Long userId, Long financePublishId, BigDecimal amount, String source,String isUseVoucher,String profitReturnConfig);

	LproductWithBalanceStatus findFinanceProductDetailForWeb(long lendProductPublishId);

	/**
	 * 查找并锁定省心计划发布记录
	 * @param financePublishId
	 * @return
	 */
	LendProductPublish getAndLockPublishById(Long financePublishId);
	
	/**
	 * 根据发布状态和借款产品ID，查询出借产品发布明细
	 */
	List<LendProductPublish> getByPublishStateAndLendProductId(char publishState, Long lendProductId);

	/**
	 * 用余额投标
	 * @param loanApplicationId
	 * @param userId
	 * @param financePublishId
	 * @param amount
	 * @return
	 */
	PayResult bidLoanByAccountBalance(Long loanApplicationId, Long userId, Long financePublishId, BigDecimal amount, String resource );
	/**
	 * 用余额买债权
	 * @param crta
	 * @param loanApplicationId
	 * @param userId
	 * @param financePublishId
	 * @param amount
	 * @param resource
	 * @return
	 */
	PayResult creditorrightsByAccountBalance(CreditorRightsTransferApplication crta, Long loanApplicationId, Long userId, Long financePublishId, BigDecimal amount, String resource);
	/**
	 * 用余额支付省心计划
	 * @param userId
	 * @param lendOrderId
	 * @return
	 */
	PayResult payFinanceByAccountBalance(Long userId, Long lendOrderId);

	/**
	 * 用余额支付省心计划
	 * @param userId
	 * @param lendOrderId
	 * @return
	 */
	PayResult payFinanceByAccountBalanceWeb(Long userId, Long lendOrderId, RateUser rateUser, RateProduct rateProduct, String... voucherIds);

	PayResult addPayOrderForBuyProductWeb(Long userId, LendOrder lendOrder, ParaContext paraContext, Date now, List<VoucherVO> vouchers, RateUser rateUser, RateProduct rateProduct, Pair<String, BigDecimal>... amountDetails) ;

	PayOrder addPayOrderForBuyHF(Long userId, LendOrder lendOrder, ParaContext paraContext, Date now, List<VoucherVO> vouchers, RateUser rateUser, RateProduct rateProduct, Pair<String, BigDecimal>... amountDetails) ;

	Pagination<LendAndLoanVO> findLendListByUserId(int pageNo, int pageSize, Map<String, Object> params);
 /**
  * 查询省心计划列表
  * @param pageSize
  * @param pageNo
  * @param lendProductPublish
  * @author wangyadong
  * @return
  */
	Pagination<LendProductPublishVO> findFinanceProductListForWebCondition(int pageSize, int pageNo, LendProductPublishVO lendProductPublish);

	List<LendProduct> findLendProductByPublishState(LendProductPublishStateEnum stateEnum);

	/**
	 * 刷新省心计划状态
	 * */
	void refreshFinanceBidStatus(); 
	
	/**
	 * 获取出借产品期限去重值列表，根据出借用户ID
	 * @param lendUserId 出借用户ID
	 * @return 出借产品期限去重值列表（如：[1,3,6]）
	 */
	List<Integer> getLendProductTimeLimitByLendUserId(Long lendUserId);
	/**
 * 新手用户
 * @param pageSize
 * @param pageNo
 * @param lendProductPublish
 * @author wangyadong
 * @return
 */
	Pagination<LproductWithBalanceStatus> findSpecialFinanceProductListForWebCondition(
			int pageSize, int pageNo, LendProductPublish lendProductPublish);
	
}
