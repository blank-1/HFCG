package com.xt.cfp.core.service;

/**
 * Created by yulei on 2015/8/20 0020.
 */
public interface BidTaskForTimerService {

	/**
	 * 流标
	 */
	void failBidTask();

	/**
	 * 财富券有效期定时任务
	 */
	void voucherRefreshTask();

	/**
	 * 充值状态刷新定时任务
	 */
	void contrastRechangeTask();

	/**
	 * 定时刷新（24小时未支付的充值单置为充值失败）
	 */
	void refreshUnRechargeOrderTask();

	/**
	 * 提现刷新定时任务
	 */
	void withDrawRefreshTask();
	
	/**
	 * 财富卷到期提醒
	 */
	void wechatVoucherExpireMsgTask();
	
	/**
	 * 逾期和罚息定时任务
	 * */
	void overDuePenltyTask();

	/**
	 * 退出省心计划
	 */
	void financeQuit();

	/**
	 * 前台债权转让到期
	 */
	void frontCreditorTransOverdue();
	
	/**
	 * 维护crm员工与客户关系
	 */
	void maintainCRMStaffCustomerTask();
	
	/**
	 * 执行自动发标
	 */
	void autoPublishTask();
	
	/**
	 * 执行加息卷状态是否过期
	 */
	void rateProductfreshTask();

	/**
	 * 执行即将到期加息券，提醒消息发送
	 */
	void expireRateMsgTask();

	/**
	 * 处理支付成功但订单状态为处理中的订单金额
	 */
	void handleUndonePayOrder();
			/**
	 * 刷新省心计划状态
	 * */
	void refreshFinanceBidStatus();

	/**
	 * 自动匹配省心计划
	 * */
	void autoMatchFinanceOrder();

	void doCapitalFlowTask();
}
