package com.xt.cfp.core.service.impl;

import com.external.llpay.LLPayUtil;
import com.external.llpay.PayDataBean;
import com.external.yeepay.QueryResult;
import com.external.yeepay.TZTService;
import com.external.yeepay.YeePayUtil;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.crm.StaffService;
import com.xt.cfp.core.service.financePlan.FinancePlanService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.LogUtils;
import com.xt.cfp.core.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * Created by yulei on 2015/8/20 0020.
 */
@Service(value = "bidTaskForTimerService")
public class BidTaskForTimerServiceImpl implements BidTaskForTimerService, Serializable {
    private static final long serialVersionUID = 1L;

    private final static String TIMER_LOG_NAME_SPACE = "TIMER_EXEC_LOG";

    private final Logger logger = Logger.getLogger("BidTaskForTimerServiceImpl");

    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private LoanPublishService loanPublishService;
    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private VoucherService voucherService;
    @Autowired
	private RechargeOrderService rechargeOrderService;
	@Autowired
	private WithDrawService withDrawService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
	@Autowired
	private LendOrderService lendOrderService;
	@Autowired
	private CreditorRightsTransferAppService creditorRightsTransferAppService;
	@Autowired
	private CreditorRightsService creditorRightsService;
	@Autowired
	private RightsRepaymentDetailService rightsRepaymentDetailService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private FinancePlanService financePlanService;
	@Autowired
	private MainLoanApplicationService mainLoanApplicationService;
	@Autowired
	private RateProductService  rateProductService;
	@Autowired
	private RateUserService rateUserService;
	@Autowired
	private PayService payService;
	@Autowired
	private LendProductService lendProductService;
	@Autowired
	private ScheduleService scheduleService;

	/**
     * 新建定时器执行记录
     * @param typeName
     * @param startTime
     * @return
     */
    private TimerExecLog addTimerExecLog(TimerTypeNameEnum typeName, Date startTime) {
        TimerExecLog timerExecLog = new TimerExecLog();
        timerExecLog.setStartExecTime(startTime);
        timerExecLog.setTimerTypeName(typeName.getDesc());

        this.myBatisDao.insert(TIMER_LOG_NAME_SPACE + ".insert", timerExecLog);
        return timerExecLog;
    }

    /**
     * 更新定时器执行记录
     * @param timerExecLog
     * @param endTime
     */
    private void endTimerExec(TimerExecLog timerExecLog, Date endTime) {
        timerExecLog.setEndExecTime(endTime);

        this.myBatisDao.update(TIMER_LOG_NAME_SPACE + ".updateByPrimaryKeySelective", timerExecLog);
    }

    /**
     * 判断一个借款申请是否可以流标
     * @param loanApplication
     * @param now
     * @return
     */
    private boolean checkLoanApplictionNeedFail(LoanApplication loanApplication, Date now) {
        LoanPublish loanPublish = loanPublishService.findById(loanApplication.getLoanApplicationId());
        int bidingDays = loanPublish.getBidingDays() + 1;
        return DateUtil.addDate(loanPublish.getOpenTime(), Calendar.DATE, bidingDays).compareTo(DateUtil.getShortDateWithZeroTime(now)) <= 0;

    }


    /**
     * 注意，此方法不受事物管理
     */
    @Override
    public void failBidTask() {
        Date now = new Date();
        TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.FAILBID, now);
        logger.info(LogUtils.createSimpleLog("开始执行流标timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));

        List<LoanApplication> loanApplications = this.loanApplicationService.findByStates(LoanApplicationStateEnum.BIDING);
        for (LoanApplication loanApplication : loanApplications) {
            if (checkLoanApplictionNeedFail(loanApplication, now)) {
                try {
                    this.loanApplicationService.noticeFailLoan(loanApplication, now, timerExecLog);
                } catch (Exception e) {
                    logger.error("处理借款申请id为【" + loanApplication.getLoanApplicationId() + "】的借款流标时出现异常", e);
                }
            }
        }

        Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行流标timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
        endTimerExec(timerExecLog, new Date());
    }

    @Override
    public void voucherRefreshTask() {
        Date now = new Date();
        TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.VOUCHER_REFRESH, now);
        logger.info(LogUtils.createSimpleLog("开始执行刷新财富券有效期timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));

        //刷新财富券产品
        voucherService.refreshProductStatus(now);
        //刷新财富券
        voucherService.refreshStatus(now);

        Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行刷新财富券有效期timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
        endTimerExec(timerExecLog, new Date());
    }
    
    /**
     * 充值状态刷新定时任务
     */
    @Override
	public void contrastRechangeTask() {
    	Date now = new Date();
        TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.CONTRASTRECHANGE_REFRESH, now);
        logger.info(LogUtils.createSimpleLog("开始执行充值状态刷新timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));

		RechargeOrder ro = new RechargeOrder();
		ro.setStatus(RechargeStatus.UN_RECHARGE.getValue());
		List<RechargeOrder> rechargeOrderList = rechargeOrderService.findBy(ro);
		Map<String, String> map=new HashMap<String, String>();
		for (int i = 0; i < rechargeOrderList.size(); i++) {
			RechargeOrder rechargeOrder = rechargeOrderList.get(i);
			if (null == rechargeOrder) {
				logger.error(LogUtils.createSimpleLog("订单接口查询失败,rechargeOrder is null","时间：" + DateUtil.getFormattedDateUtil(new Date(), "yyyy-MM-dd HH:mm:ss")));
				continue;
			}
			if(rechargeOrder.getChannelCode()!=null&&rechargeOrder.getChannelCode().equals("YB_EBK")){
				//处理易宝交易的订单
				if(rechargeOrder.getRechargeCode()==null){
					logger.error(LogUtils.createSimpleLog("不存在的充值单号,rechargeId="+rechargeOrder.getRechargeId(),"时间：" + DateUtil.getFormattedDateUtil(new Date(), "yyyy-MM-dd HH:mm:ss")));
					continue;
				}
				QueryResult result=YeePayUtil.selectYeePayOrder(rechargeOrder.getRechargeCode());
				if(result==null){
					logger.error(LogUtils.createSimpleLog("订单接口查询失败,rechargeId="+rechargeOrder.getRechargeId(),"时间：" + DateUtil.getFormattedDateUtil(new Date(), "yyyy-MM-dd HH:mm:ss")));
					continue;
				}
				map.clear();
				if(result.getR1_Code().equals("50")){
					map.put("status", "0");
					map.put("orderid", rechargeOrder.getRechargeCode());
					map.put("yborderid", rechargeOrder.getExternalNo());
					rechargeOrderService.handleCallbackNotification(map);
					logger.info(LogUtils.createSimpleLog("订单不存在,易宝中没有对应的订单,rechargeId="+rechargeOrder.getRechargeId(),"时间：" + DateUtil.getFormattedDateUtil(new Date(), "yyyy-MM-dd HH:mm:ss")));
					continue;
				}
				if(result.getRb_PayStatus()!=null&&result.getRb_PayStatus().equals("SUCCESS")){
					map.put("status", "1");
					map.put("orderid", rechargeOrder.getRechargeCode());
					map.put("yborderid", rechargeOrder.getExternalNo());
					// 处理支付结果回调
					rechargeOrderService.handleCallbackNotification(map);
				}else{
					//处理未支付和已取消的订单
					long diff=new Date().getTime()-rechargeOrder.getCreateTime().getTime()-24*3600*1000;
					if(diff>0){
						map.put("status", "0");
						map.put("orderid", rechargeOrder.getRechargeCode());
						map.put("yborderid", rechargeOrder.getExternalNo());
						rechargeOrderService.handleCallbackNotification(map);
					}
				}
			} else if (rechargeOrder.getChannelCode().equals("LL_AUTHPAY") || rechargeOrder.getChannelCode().equals("LL_GATEPAY")) {

				Map<String, String> params = new HashMap<String,String>();
		        params.put("no_order", rechargeOrder.getRechargeCode());
		        params.put("dt_order", DateUtil.getFormattedDateUtil(rechargeOrder.getCreateTime(), "yyyyMMddHHmmss"));
		        Map<String, String> respMap = LLPayUtil.orderQuery(params);
		        if("0000".equals(respMap.get("ret_code"))){
		        	Map<String, String> handMap = new HashMap<String,String>();
		        	handMap.put("orderid", rechargeOrder.getRechargeCode());
		        	handMap.put("yborderid", respMap.get("oid_paybill"));
		        	if("SUCCESS".equals(respMap.get("result_pay"))){
		        		PayDataBean payDataBean = new PayDataBean();
		        		payDataBean.setNo_order(rechargeOrder.getRechargeCode());
						if(respMap.get("pay_type").equals(LLPayTypeEnum.LLPAY_AUTH.getValue())){
							//认证支付--绑卡
							customerCardService.bindCustomCard(payDataBean);
							logger.info("绑卡流程结束......");
						}
		        		handMap.put("status", "1");
		        		rechargeOrderService.handleCallbackNotification(handMap);
		        		logger.info("对账执行完成，充值结果成功！");
		        	}else if("FAILURE".equals(respMap.get("result_pay"))){
		        		handMap.put("status", "0");
		        		rechargeOrderService.handleCallbackNotification(handMap);
		        		logger.info("对账执行完成，充值结果失败！");
		        	}else{
		        		logger.info("连连支付订单状态等待处理中......");
		        	}
		        }else{
		        	logger.info("查询连连支付订单响应失败，失败原因:"+ respMap.get("ret_message"));
		        }
		        
			} else{
				// 调用对账接口
				Map<String, String> result = TZTService.payapiQueryByOrderid(rechargeOrder.getRechargeCode().toString());
				// 状态判断
				String status = result.get("status");
				if (null != status && !"".equals(status)) {
					if (Integer.valueOf(status) == 1 || Integer.valueOf(status) == 0) {
						// 处理支付结果回调
						rechargeOrderService.handleCallbackNotification(result);
					}
				}
			}
		}
		Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行充值状态刷新timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
        endTimerExec(timerExecLog, new Date());
	}

    /**
     * 定时刷新（24小时未支付的充值单置为充值失败）
     */
	@Override
	public void refreshUnRechargeOrderTask() {
		Date now = new Date();
        TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.UNRECHARGEORDER_REFRESH, now);
        logger.info(LogUtils.createSimpleLog("开始执行24小时未支付充值单刷新timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));

		myBatisDao.update("RECHARGE_ORDER.refreshUnRechargeOrderTask", null);
		Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行24小时未支付充值单刷新timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
        endTimerExec(timerExecLog, new Date());
	}

	/**
     * 提现刷新定时任务
     */
	@Override
	public void withDrawRefreshTask() {
		Date now = new Date();
        TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.WITHDRAW_REFRESH, now);
        logger.info(LogUtils.createSimpleLog("开始执行提现刷新timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));

		WithDraw withDraw = new WithDraw();
		withDraw.setTransStatus(WithDrawTransferStatus.TRANSFER_ING.getValue());
		List<WithDraw> withDrawList = withDrawService.findBy(withDraw);
		for (int i = 0; i < withDrawList.size(); i++) {
			withDrawService.refreshNew(withDrawList.get(i).getWithdrawId().toString());
		}
		Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行提现刷新timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
        endTimerExec(timerExecLog, new Date());
	}
	
	@Override
	public void wechatVoucherExpireMsgTask() {
		Date now = new Date();
        TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.WECHAT_VOUCHER_EXPIRE_MSG,now);
        logger.info(LogUtils.createSimpleLog("开始执行财富劵到期提醒timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));
        voucherService.wechatVoucherExpireMsg(now);//公众号
        voucherService.sendExpireVoucherMsgForTimer();//短信
        Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行财富劵到期提醒timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
        endTimerExec(timerExecLog, new Date());
	}
	
	@Override
	public void overDuePenltyTask() {
		Date now = new Date();
		TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.OVERDUE_PENLTY, now);
		logger.info(LogUtils.createSimpleLog("开始执行提现刷新timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));
		repaymentPlanService.processOverduePenalty();
		Date endTime = new Date();
		logger.info(LogUtils.createSimpleLog("结束执行提现刷新timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
		endTimerExec(timerExecLog, new Date());
	}

	@Override
	public void financeQuit() {
		Date date = new Date();
		TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.FINANCE_QUIT, date);
		logger.info(LogUtils.createSimpleLog("开始执行省心计划退出timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		lendOrderService.scanFinanceOrderAndDoQuit();
		date = new Date();
		logger.info(LogUtils.createSimpleLog("结束执行省心计划退出timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		endTimerExec(timerExecLog, date);
	}

	@Override
	public void frontCreditorTransOverdue() {
		Date date = new Date();
		TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.FINANCE_QUIT, date);
		logger.info(LogUtils.createSimpleLog("开始执行债权转让到期timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		List<CreditorRightsTransferApplication> creditorRightsTransferApplications = creditorRightsTransferAppService.getByTypeAndApplyTimeAndStatus(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppType.MANUAL, null, CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus.TRANSFERRING);
		Date applyTime = null;
		RightsRepaymentDetail repaymentDetail;
		AccountValueChangedQueue avcq = new AccountValueChangedQueue();
		for (CreditorRightsTransferApplication creditorRightsTransferApplication : creditorRightsTransferApplications) {
			repaymentDetail = this.creditorRightsService.getNearestNeedRepayRpdByCrId(creditorRightsTransferApplication.getApplyCrId());
			applyTime = creditorRightsTransferApplication.getApplyTime();
			if (DateUtil.daysBetween(applyTime, repaymentDetail.getRepaymentDayPlanned()) > 5)
				continue;

			logger.info(StringUtils.t2s(DescTemplate.Log.CreditorTemplate.UNDOING_CREDITORRIGHTSAPPLY, creditorRightsTransferApplication.getCreditorRightsApplyId(), "到期"));
			this.creditorRightsTransferAppService.undoCreditorRightsTransferApplication(creditorRightsTransferApplication, 2);
		}
		date = new Date();
		logger.info(LogUtils.createSimpleLog("结束执行债权转让到期timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		endTimerExec(timerExecLog, date);
	}

	@Override
	public void maintainCRMStaffCustomerTask() {
		Date date = new Date();
		TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.CRM_STAFF_CUSTOMER, date);
		logger.info(LogUtils.createSimpleLog("开始执行CRM客户关系维护timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		
		List<UserInfoExt> users=staffService.findStaffInUEAndHaveRecId();
		if(users!=null&&users.size()>0){
			for (UserInfoExt ue : users) {
				if(ue.getUserId().compareTo(0l)==0){
					continue;
				}
				List<UserInfoExt> customers=staffService.findStaffCustomer(ue.getUserId());
				if(customers!=null&&customers.size()>0){
					for (UserInfoExt c : customers) {
						List<CrmFranCustomer> list=staffService.findStaffCustomers(ue.getUserId(), c.getUserId());
						if(list==null||list.size()==0){
							CrmFranCustomer customer=new CrmFranCustomer();
							customer.setCreateDate(new Date());
							customer.setCustomerId(c.getUserId());
							customer.setpCustomerId(ue.getUserId());
							staffService.saveCustomer(customer);
							continue;
						}
					}
					
				}
			}
		}
		
		logger.info(LogUtils.createSimpleLog("结束执行CRM客户关系维护timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		endTimerExec(timerExecLog, date);
	}


	/**
	 * 执行自动发标
	 */
	@Override
	public void autoPublishTask() {
		Date date = new Date();
		TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.AUTO_PUBLISH, date);
		logger.info(LogUtils.createSimpleLog("开始执行自动发标timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		try {
			mainLoanApplicationService.autoPublish();
		} catch (Exception e) {
			logger.info("【自动发标】发生异常：" + e.getMessage());
		}finally{
			//发标完成后自动匹配理财订单
			autoMatchFinanceOrder();
		}
		date = new Date();
		logger.info(LogUtils.createSimpleLog("结束执行自动发标timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		endTimerExec(timerExecLog, date);
		
	}
	
	/**
	 * 查询加息卷是否过期
	 * 
	 */
	@Override   
    public void rateProductfreshTask() {
        Date now = new Date();
        TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.RATE_PRODUCT_FRESH, now);
        logger.info(LogUtils.createSimpleLog("开始执行刷新加息卷过期状态timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));

        //刷新加息卷产品
        rateProductService.updateRateProductStatus(now);
        //刷新新加息卷
        rateProductService.updateRateUserStatus(now);

        Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行加息卷过期状态timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
        endTimerExec(timerExecLog, new Date());
    }
	
	/**
	 * 执行即将到期加息券，提醒消息发送
	 */
	@Override
	public void expireRateMsgTask() {
		Date date = new Date();
		TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.EXPIRE_RATE_MSG, date);
		logger.info(LogUtils.createSimpleLog("开始执行加息券到期提醒timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		try {
			rateUserService.sendExpireRateMsgForTimer();
			logger.info("【加息券到期提醒】执行结果：成功！");
		} catch (Exception e) {
			logger.info("【加息券到期提醒】发生异常：" + e.getMessage());
		}
		date = new Date();
		logger.info(LogUtils.createSimpleLog("结束执行加息券到期提醒timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		endTimerExec(timerExecLog, date);
		
	}

	@Override
	public void handleUndonePayOrder() {
		Date date = new Date();
		TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.CANCEL_PAYORDER_FOR_FAIL_BID, date);
		logger.info(LogUtils.createSimpleLog("开始处理支付成功但处理失败的支付订单timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		try {
			payService.handleUndonePayOrderForTimer();
			logger.info("【处理支付成功但处理失败的支付订单】执行结果：成功！");
		} catch (Exception e) {
			logger.info("【处理支付成功但处理失败的支付订单】发生异常：" + e.getMessage());
		}
		date = new Date();
		logger.info(LogUtils.createSimpleLog("结束处理支付成功但处理失败的支付订单timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		endTimerExec(timerExecLog, date);
	}
	@Override
	public void refreshFinanceBidStatus() {
		//		select lpp.* from lend_product_publish lpp , lend_product lp where lp.lend_product_id = lpp.lend_product_id and lp.product_type = '2' and lpp.publish_state in ('1','2')
		Date date = new Date();
		TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.FINANCE_REFRESH_STATE, date);
		logger.info(LogUtils.createSimpleLog("开始执行刷新省心计划timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		try {
			lendProductService.refreshFinanceBidStatus();
		} catch (Exception e) {
			logger.info("【刷新省心计划】发生异常：" + e.getMessage());
		}
		date = new Date();
		logger.info(LogUtils.createSimpleLog("结束执行刷新省心计划timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		endTimerExec(timerExecLog, date);
		
	}
	
	@Override
	public void autoMatchFinanceOrder(){
		Date date = new Date();
		TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.FINANCE_AUTO_MATCH, date);
		logger.info(LogUtils.createSimpleLog("开始执行刷新省心计划匹配timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		try {
			financePlanService.creditorRightsAutoMatch();
		} catch (Exception e) {
			logger.info("【刷新省心计划】发生异常：" + e.getMessage());
		}
		date = new Date();
		logger.info(LogUtils.createSimpleLog("结束执行刷新省心计划匹配timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		endTimerExec(timerExecLog, date);
		
	}

	@Override
	public void doCapitalFlowTask() {
		Date date = new Date();
		TimerExecLog timerExecLog = addTimerExecLog(TimerTypeNameEnum.CAPITAL_FLOW_DO, date);
		logger.info(LogUtils.createSimpleLog("开始执行恒丰存管资金流timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
        try {
            scheduleService.doTask();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("【恒丰存管资金流】发生异常：" + e.getMessage());
        }
        date = new Date();
		logger.info(LogUtils.createSimpleLog("结束执行恒丰存管资金流timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
		endTimerExec(timerExecLog, date);
	}
}
