package com.xt.cfp.core.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.constants.LendOrderConstants;
import com.xt.cfp.core.pojo.CreditorRightsTransferApplication;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendProduct;
import com.xt.cfp.core.pojo.LendProductPublish;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.ext.CreditorRightsExtVo;
import com.xt.cfp.core.pojo.ext.LendOrderDetailVO;
import com.xt.cfp.core.pojo.ext.LendOrderExtProduct;
import com.xt.cfp.core.pojo.ext.PayResult;
import com.xt.cfp.core.pojo.ext.phonesell.LendOrderVO;
import com.xt.cfp.core.util.Pagination;

import javax.servlet.http.HttpServletResponse;

public interface LendOrderService {

	/**
	 * 省心计划列表
	 * @param pageSize
	 * @param pageNo
	 * @param searchFinancialName 省心计划名称
	 * @param searchPeriods 期数
	 * @param searchLeanUserName 购买人姓名
	 * @param searchT 时间区间
	 * @param searchBeginTime 购买时间开始
	 * @param searchEndTime 购买时间结束
	 * @param searchState 理财状态
	 * @param userId 购买人ID
	 * @return
	 */
    Pagination<LendOrderExtProduct> getFinancialPlanList(int pageSize, int pageNo, String searchFinancialName, String searchPeriods, String searchLeanUserName,
                                                       String searchT, String searchBeginTime, String searchEndTime, String searchState, Long userId);
    /**
	 * 电销投资记录
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
    Pagination<LendOrderVO> getPhonesellOrder( int pageNo,int pageSize, Map<String, Object> customParams);
    
    /**
     * 电销统计数据
     * @param customParams
     * @return
     */
    List<String> getPhonesellOrdersAccount(Map<String, Object> customParams);
    
    /**
     * 我的理财-省心计划列表
     * @param pageSize
     * @param pageNo
     * @param userId
     * @param queryState 省心计划状态
     * @param queryType 省心类型
     * @return
     */
    Pagination<LendOrderExtProduct>	getAllMyFinanceList(int pageSize, int pageNo, Long userId, String queryState, String queryType); 
    /**
     * 根据id查找出借订单
     * @param lendOrderId
     * @return
     */
    LendOrder findById(long lendOrderId);
    /**
     *  根据出借id 查找省心计划详情
     * @param lendOrderId
     * @return
     */
    LendOrderExtProduct findFinancialPlanById(Long lendOrderId);
    /**
     * 获得实换利息
     */
    BigDecimal getRealChangeInterest(Long lendOrderId);
    /**
     * 债券明细列表  根据出借id
     * @param pageSize
     * @param pageNo
     * @param lendOrderId
     * @return
     */
    Pagination<CreditorRightsExtVo> findCreditorRightsByDetailList(int pageSize, int pageNo, Long lendOrderId);

    LendOrder newRightsLendOrder(LendProduct rightLendProduct, LendProductPublish lendPublish, LoanApplication loanApplication,
                            UserAccount lendAccount, long customerCardId);

    /**
     * 得到投标的出借人信息列表  根据借款申请id
     * @param loanApplicationId
     * @return
     */
    Pagination<LendOrderExtProduct> getLenderInformationById(int pageSize, int pageNo, long loanApplicationId);

    /**
     * 按条件查询所有待理财金额大于0的订单
     * @param lendMap
     *  orderStateList：订单状态列表
     *  productType：出借产品类型
     * @return
     */
    List<LendOrder> findHaveBalanceOrder(Map<String, Object> lendMap);

    /**
     * 出借用户,同一标 ，出借的总金额
     * @param userId
     * @param loanApplicationId
     * @return
     */
	BigDecimal getTotalLendAmount(Long userId, Long loanApplicationId);

	/**
	 * 出借用户在平台累计出借总金额
	 * 
	 * @param userId
	 * @return
	 */
	BigDecimal getTotalLendAmount(Long userId);

    /**
     * 出借用户在平台累计出借总金额 (单月)
     * @param userId
     * @param month   201504
     * @return
     */
    BigDecimal getTotalLendAmount(Long userId,String month);

    void update(Map map);

    /**
     * 新建出借订单
     * @param userId
     * @param lendOrderName
     * @param productPublishId
     * @param amount
     * @param now
     * @param lendProduct
     * @return
     */
    LendOrder addLendOrder(Long userId, String lendOrderName, Long productPublishId, BigDecimal amount, Date now, LendProduct lendProduct,String resource, String productType,String isUseVoucher,String profitReturnConfig);

    /**
     * 新建省心计划
     * @param userId
     * @param productPublishId
     * @param amount
     * @param now
     * @param lendProduct
     * @return
     */
    LendOrder addFinanceOrder(Long userId, Long productPublishId, BigDecimal amount, Date now, LendProduct lendProduct,String resource,String isUseVoucher,String profitReturnConfig);

    /**
     * 新建投标订单
     * @param userId
     * @param loanApplicationId
     * @param productPublishId
     * @param amount
     * @param now
     * @param lendProduct
     * @return
     */
    LendOrder addLoanOrder(Long userId, Long loanApplicationId, Long productPublishId, BigDecimal amount, Date now, LendProduct lendProduct,String resource);

    /**
     * 新建债券订单
     * @param userId
     * @param loanApplicationId
     * @param productPublishId
     * @param amount
     * @param now
     * @param lendProduct
     * @param resource
     * @param crta
     * @return
     */
	LendOrder addCreditorRightsOrder(Long userId, Long loanApplicationId, Long productPublishId, BigDecimal amount, Date now,
			LendProduct lendProduct, String resource, CreditorRightsTransferApplication crta);
    
    /**
     * 确认省心计划支付结果
     * @param orderId
     * @param now
     * @param payResult
     */
    void confirmFinanceOrderHasPaid(Long orderId, Date now, PayResult payResult);

    /**
     * 累计收益
     * @param userId
     * @return
     */
	BigDecimal getTotalProfit(Long userId);

	/**
	 * 累计持有省心计划
	 * @param userId
	 * @return
	 */
	BigDecimal getTotalHoldFinancePlan(Long userId);

    /**
     * 根据支付订单id获取出借订单
     * @param payId
     * @param assertExist
     * @return
     */
    LendOrder getLendOrderByPayId(Long payId, boolean assertExist);

	/**
	 * 本期购买情况
	 * 
	 * @param lendProductPublishId
	 * @return
	 */
	List<LendOrderExtProduct> getCycleBuySituation(Long lendProductPublishId);
	/**
	 * 往期购买情况
	 * @param lpp
	 * @return
	 */
	List<LendProductPublish> getCycleBuySituationHistory(LendProductPublish lpp);

    BigDecimal getNewestProfit(long lendOrderId);

    void updateNewestProfit(long lendOrderId, BigDecimal profit);

    /**
     * 获取当前用户近期交易
     * @param userId
     * @return
     */
    List<LendOrder> getLendOrderRecent(Long userId,int rows);

    /**
     * 获取分页订单信息
     *
     * @param pageNum
     * @param pageSize
     * @param lendOrder
     * @param customParams
     * @return
     */
    Pagination<LendOrder> getLendOrderPaging(int pageNum, int pageSize, LendOrder lendOrder, Map<String, Object> customParams);

    /**
     * 生成出借订单编号
     * @param productType
     * @return
     */
    String genertOrderCode(String productType);

    /**
     * 生成出借订单名称
     * @param lendOrder
     * @return
     */
    String genertOrderName(LendOrder lendOrder);


    /**
     * 根据userid查询订单（债权/投标）
     * @param pageNum
     * @param pageSize
     * @param paramMap
     * @return
     */
	Pagination<LendOrderExtProduct> getLendOrderListBy(int pageNum, int pageSize, Map<String, Object> paramMap);

    /**
     * 确认出借订单支付结果
     * @param lendOrderId
     * @param date
     * @param payResult
     */
    void confirmBidLoanOrderHasPaid(Long lendOrderId, Date date, PayResult payResult);
	
	/**
	 * 获得累计出借总额
	 * @param userId 也可以指定某个用户的购买金额
	 */
	BigDecimal getAllBuyBalance(Long userId);
	
	/**
	 * 查询符合特定条件的用户订单数量
	 * @param userId
	 * @param map
	 * @return
	 */
	Integer getUserOrderNumByCondition(Map map);
	
	/**
	 * 查询符合特定条件的用户订单总金额
	 * @param map
	 * @return
	 */
	BigDecimal getUserAllBuyBalanceByCondition(Map map);
	
    /**
     * 获得累计奖励
     * @param userId
     * @return
     */
    BigDecimal getTotalAward(Long userId);
	
	/**
	 * 获得累计收益
	 * @param userId 也可以指定某个用户的收益
	 */
	BigDecimal getAllProfit(Long userId);

    /**
     * 更改处接订单的状态
     * @param lendOrderId
     * @param financeOrderStatusEnum
     */
    void updateLendOrderStatus(Long lendOrderId, LendOrderConstants.FinanceOrderStatusEnum financeOrderStatusEnum);

    /**
     * 获取并锁定订单
     * @param lendOrderId
     * @return
     */
    LendOrder findAndLockById(Long lendOrderId);

    void refreshLendOrderReceive();

    /**
     * 获得已购买标的总额
     * @param loanApplicationId
     * @return
     */
    BigDecimal getTotalLendAmountByLoanApplicationId(Long loanApplicationId);

    /**
     * 省心计划起息
     * @param financeOrderId
     * @param date
     */
    void startCalculateFinanceOrderInterest(Long financeOrderId, Date date);

    /**
     * 更新订单
     * @param lendOrder
     */
    void update(LendOrder lendOrder);
    
    /**
     * 第二部分（上） 理财情况
     */
    LendOrderDetailVO getOrderDetail2ALC(Long lendOrderId);
    
    /**
     * 第二部分（下） 理财情况
     */
    LendOrderDetailVO getOrderDetail2BLC(Long lendOrderId);
    
    /**
     * 第二部分（上） 债权情况
     */
    LendOrderDetailVO getOrderDetail2AZQ(Long lendOrderId);
    
    /**
     * 第二部分（下） 债权情况
     */
    LendOrderDetailVO getOrderDetail2BZQ(Long lendOrderId);
    
    /**
     * 第三部分（上）
     */
    LendOrderDetailVO getOrderDetail3A(Long lendOrderId);
    
    /**
     * 第三部分（下）
     */
    List<LendOrderDetailVO> getOrderDetail3B(Long lendOrderId);

    /**
     * 导出出借人数据报表
     * @param response
     * @param customParams
     */
    void exportLenderExcel(HttpServletResponse response, Map<String, Object> customParams);

    /**
     * 是否是首次投标
     * @param userId
     * @param lendOrder
     * @return
     */
    boolean isFirstLend(Long userId, LendOrder lendOrder);
    
    /**
     * 根据出借产品id得到费用
     * @param loanProductId
     * @return
     */
    FeesItem getFeesByLendProId(long lendProId);

    /**
     * 扫描可以退出的省心计划,并执行退出操作
     */
    void scanFinanceOrderAndDoQuit();
    /**
     * 处理债权转让订单
     * @throws Exception
     */
    void turnCreditorRights(Long lendOrderId, Date date, PayResult payResult);
    
    /**
     * 根据父级id获取订单
     * */
    LendOrder getLendOrderByPid(Long lendOrderPid);
    
    /**
     * 获取省心计划订单中退出中状态并且所有债权都是已结清的
     * */
	List<LendOrder> findFinanceClearForQuit();
	/**
     * 根据订单id获取省心计划退出中的订单
     * */
	List<Long> findQuitFinanceOrderByIds(List<Long> lendOrderIdList);
	/**
	 * 根据省心计划订单找出未还款的子订单
	 * */
	List<LendOrder> findUnRepayOrdersByFinanceOrder(LendOrder lendOrder);
	/**
	 * 获取用户所有理财订单
	 * */
	List<LendOrder> getFinancialPlanListByUserId(Long userId);
	/**
	 * 获取用户省心计划订单的待回利息
	 * */
	BigDecimal getFinancialWaitInterestByUserId(Long userId);
	/**
	 * 【省心计划】查询某条省心计划，在投资金预期收益 
	 * @param lendOrderId 出借订单
	 * @return
	 */
	BigDecimal getFinancialWaitInterestByLendOrderId(Long lendOrderId);
	/**
	 * 【省心计划】根据省心计划主出借订单ID，查询已经匹配到子标的出借总金额
	 * @param lendOrderPId 省心计划，出借订单ID
	 * @return
	 */
	BigDecimal getAllBuyBalanceByLendOrderPid(Long lendOrderPId);
	/**
	 * 【省心计划】根据省心计划主出借订单ID，查询已经匹配到子标的已获奖励
	 * @param lendOrderPId 省心计划，出借订单ID
	 * @return
	 */
	BigDecimal getAwardBalanceByLendOrderPid(Long lendOrderPId);
	/**
	 * 根据子出借订单ID，查询已经子标的已获奖励
	 * @param lendOrderId 子出借订单ID
	 * @return
	 */
	BigDecimal getAwardBalanceByLendOrderId(Long lendOrderId);
    
	/* * 是否为新手用户
	 * @param userId
	 * @param querys 
	 * @return
	 */
	int countMakedLoan(Long userId, String[] querys);
	
	/**
	 * 查询全部有效的省心计划（已支付，理财中，授权期到期，已结清）
	 * */
	List<LendOrder> findAllValidFinanceOrder(Map<String,Date> param);
}
