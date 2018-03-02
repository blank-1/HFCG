package com.xt.cfp.core.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.pojo.RepaymentRecord;

import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.constants.ChannelTypeEnum;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.LoanPublish;
import com.xt.cfp.core.pojo.RepaymentPlan;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.pojo.ext.RepaymentPlanVO;
import com.xt.cfp.core.pojo.ext.RepaymentVO;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.Pagination;

public interface RepaymentPlanService {
    void create(RepaymentPlan plan) throws Exception;
    void update(RepaymentPlan plan) throws Exception;
    void update(Map repaymentPlanMap);
    void remove(RepaymentPlan plan) throws Exception;

    List<RepaymentPlan> getRepaymentPlansByloanApplicationId(long loanApplicationId);
    List<RepaymentPlan> getRepaymentPlansByloanApplicationId(long loanApplicationId,ChannelTypeEnum channelTypeEnum);

    List<RepaymentPlan> getRepaymentPlanByLoanAppIdAndState(long loanApplicationId, char planState);

    List<RepaymentPlan> getRepaymentPlanByDate(long loanApplicationId, Date repaymentDate, char planState);


    RepaymentPlan getFirstUnCompletePlan(long loanApplicationId);

    List<RepaymentPlan> getByLoanApplicationIdAtLast(Map parameters) throws Exception;
    List<RepaymentPlan> getByLoanApplicationIdAtBefore(Map parameters) throws Exception;
    BigDecimal calcRepaymentBalance(long loanApplicationId, String repaymentDay, boolean isInterest) throws Exception;
    BigDecimal calcBreachBalance(long loanApplicationId, BigDecimal feesRate, int radicesType, long feeItemId) throws Exception;
//   BigDecimal calcFeesDetailBalance(long loanApplicationId, LoanProductFeesItem productFeeItem, BigDecimal shouldCapital, BigDecimal shouldInterest) throws Exception;

    void updateOverdueStatus(Map map);
    List<RepaymentPlan> findBy(Map parameters);
    List<LoanApplication> findLoansBy(Map parameters);

    List<RepaymentPlan> generateRepaymentPlan(long loanProductId, BigDecimal confirmBalance) throws Exception;

     RepaymentPlan findById(long repaymentPlanId);



    /**
     * 添加-还款计划。
     */
    RepaymentPlan addRepaymentPlan(RepaymentPlan repaymentPlan);

    /**
     *待还款金额总额
     * @return
     */
    BigDecimal getAllRepaymentAmountByUserId(Long userId);
    /**
     *待还本金
     * @return
     */
    BigDecimal getRepaymentCapitalByUserId(Long userId);
    /**
     *待还本金
     * @return
     */
    BigDecimal getRepaymentCapitalByLoanApplicationId(Long loanApplicationId);
    /**
     *待还利息
     * @return
     */
    BigDecimal getRepaymentInterestByUserId(Long userId);
    /**
     *待还利息
     * @return
     */
    BigDecimal getRepaymentInterestByLoanApplicationId(Long loanApplicationId);

    /**
     * 通过借款申请ID和指定日期查询未还的还款计划
     * @param loanApplicationId
     * @param repaymentDate
     * @return
     */
    List<RepaymentPlan> getRepaymentPlanList(long loanApplicationId,Date repaymentDate);

    @Transactional
    Map<Long,Map<Long,Map<String,BigDecimal>>> repayment(long loanApplicationId,long replaymentPlanId, Date repaymentDate, BigDecimal balance,BigDecimal paidBalance, long adminId, boolean ignoreDelay,String desc ,boolean isAhead,List<Long> repaymentIdList) throws Exception;
    
    /**
     * 查询还款list
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     */
	Pagination<RepaymentVO> getRepaymentList(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 查询借款订单还款明细
	 * @param pageNo
	 * @param pageSize
	 * @param loanApplicationId
	 * @return
	 */
	Pagination<RepaymentVO> getRepaymentDetailListByLoanAppId(int pageNo, int pageSize, Long loanApplicationId);

    /**
     * 发送还款成功短信(to 借款人)
     */
    void sendRepaymentSuccessMsg(BigDecimal balance,LoanApplication loanApplication,RepaymentRecord repaymentRecord,RepaymentPlan repaymentPlan);
    /**
     * 发送还款结束短信
     */
    void sendRepaymentEndMsg(String title,String mobileNo);
    
    /**
     * 逾期罚息任务
     * */
	void processOverduePenalty();
	
	/**
	 * 根据id查询还款记录信息
	 * @param repaymentPlanId
	 * @param lock
	 * */
	RepaymentPlan findByIdBLock(long repaymentPlanId, boolean lock);
	/**
	 * 不带事物的更新操作
	 * */
	void updateNTX(RepaymentPlan plan) throws Exception;
	
	BigDecimal getDefaultBalance(BigDecimal rate, int radicesType,
			BigDecimal currentCalital, BigDecimal currentInterest,
			BigDecimal allCalital, BigDecimal allInterest,
			BigDecimal currentPaidCalital, BigDecimal currentPaidInterest,
			BigDecimal currentProfit, BigDecimal allProfit);
	
	/**
	 * 最近一期还款时间与当前时间相差天数
	 * @param detailRightsList
	 * @return
	 */
	Long getTermDay(List<RightsRepaymentDetail> detailRightsList);
	/**
	 * 重新生成债权转让
	 * */
	void reCreateTurnRight(Long loanApplicationId);
	/**
     * 进行佣金分配和更新
     * @param int sectionCode 期号
     * @param int allCode 总分期数
     * @param Map lendsOrderMap 订单信息
     * */
	void repayCommision(int sectionCode, int allCode,
			Map<Long, Long> lendsOrderMap, AccountValueChangedQueue avcq,
			LoanPublish loanPublish);
	
	
	/**
	 * 获取债权对应最早的还款计划期号（用于查询中间转让接收的债权的有效开始）
	 * */
	int getTheMinRepaymentForCreditorRights(long creditorRightsId);
	
	/**
	 * 处理省心计划子订单返现操作（购买时勾选收益提取可用余额）
	 * */
	void handleFinanceChildOrder(Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap);
	
	/**
	 * 根据借款申请ID，获取指定还款计划信息
	 * @param loanApplicationId
	 * @return
	 */
	RepaymentPlanVO getRepaymentPlanByLoanApplicationId(Long loanApplicationId);

}

