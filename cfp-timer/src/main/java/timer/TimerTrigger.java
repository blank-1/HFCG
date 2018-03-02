package timer;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.QueryRechargeWithdrawDataSource;
import com.external.deposites.model.response.QueryRechargeWithdrawResponse;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.crm.StaffService;
import com.xt.cfp.core.service.financePlan.FinancePlanService;
import com.xt.cfp.core.service.task.LoanReportTask;
import com.xt.cfp.core.service.task.TradeReportTask;
import com.xt.cfp.core.service.task.UserReportTask;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.LogUtils;
import com.xt.cfp.core.util.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulei on 2015/8/20 0020.
 */
@Component
@Configuration
@EnableAsync
@EnableScheduling
public class TimerTrigger {
    private static final long serialVersionUID = 1L;


    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ScheduleService scheduleService;
    @Resource
    private CapitalFlowService capitalFlowService;
    @Resource
    private LendProductService lendProductService;
    @Resource
    private LendOrderService lendOrderService;
    @Resource
    private MainLoanApplicationService mainLoanApplicationService;
    @Resource
    private FinancePlanService financePlanService;
    @Resource
    private RateUserService rateUserService;
    @Resource
    private RateProductService rateProductService;
    @Resource
    private StaffService staffService;
    @Resource
    private RepaymentPlanService repaymentPlanService;
    @Resource
    private VoucherService voucherService;
    @Resource
    private MyBatisDao myBatisDao;
    @Resource
    private WithDrawService withDrawService;
    @Resource
    private RechargeOrderService rechargeOrderService;
    @Resource
    private PayService payService;
    @Resource
    private UserAccountService userAccountService;
    @Resource
    private UserAccountOperateService userAccountOperateService;
    @Resource
    private IhfApi ihfApi;
    @Resource
    private UserReportTask userReportTask;
    @Resource
    private LoanReportTask loanReportTask;
    @Resource
    private TradeReportTask tradeReportTask;

    private void failureRechange(RechargeOrder ro) {
        ro.setStatus(RechargeStatus.FAILE.getValue());
        rechargeOrderService.updateRecharge(ro);
        if (ro.getPayId() != null) {
            PayOrder payOrder = payService.getPayOrderById(ro.getPayId(), true);
            payOrder.setStatus(PayConstants.OrderStatus.FAIL.getValue());
            payOrder.setProcessStatus(PayConstants.ProcessStatus.FAIL.getValue());
            payService.update(payOrder);
        }
    }

    @Scheduled(cron = "0 0/2 * * * *")
    public void contrastRechangeTask() {
        Date now = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行充值状态刷新timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));

        RechargeOrder ro = new RechargeOrder();
        ro.setStatus(RechargeStatus.UN_RECHARGE.getValue());
        List<RechargeOrder> rechargeOrderList = rechargeOrderService.findBy(ro);
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < rechargeOrderList.size(); i++) {
            RechargeOrder rechargeOrder = rechargeOrderList.get(i);
            if (null == rechargeOrder) {
                logger.error(LogUtils.createSimpleLog("订单接口查询失败,rechargeOrder is null", "时间：" + DateUtil.getFormattedDateUtil(new Date(), "yyyy-MM-dd HH:mm:ss")));
                continue;
            }
            if (rechargeOrder.getChannelCode() != null && (rechargeOrder.getChannelCode().equals(RechargeChannelEnum.HF_AUTHPAY.getValue()) || rechargeOrder.getChannelCode().equals(RechargeChannelEnum.HF_GATEPAY.getValue()))) {
                if (rechargeOrder.getRechargeCode() == null) {
                    logger.error(LogUtils.createSimpleLog("不存在的充值单号,rechargeId=" + rechargeOrder.getRechargeId(), "时间：" + DateUtil.getFormattedDateUtil(new Date(), "yyyy-MM-dd HH:mm:ss")));
                    failureRechange(rechargeOrder);
                    continue;
                }

                map.clear();
                map.put("businessId", rechargeOrder.getRechargeId());
                map.put("businessType", AccountConstants.BusinessTypeEnum.FEESTYPE_RECHARGE.getValue());
                List<Schedule> schs = scheduleService.findByCondition(map);
                if (null == schs || schs.size() == 0) {
                    logger.error(LogUtils.createSimpleLog("数据异常,rechargeId=" + rechargeOrder.getRechargeId(), "时间：" + DateUtil.getFormattedDateUtil(new Date(), "yyyy-MM-dd HH:mm:ss")));
                    failureRechange(rechargeOrder);
                    continue;
                }
                if (now.getTime() - rechargeOrder.getCreateTime().getTime() > 300 * 1000) {
                    QueryRechargeWithdrawDataSource dataSource = new QueryRechargeWithdrawDataSource();
                    QueryRechargeWithdrawResponse response = null;
                    try {
                        dataSource.setTxn_ssn(rechargeOrder.getRechargeCode());
                        dataSource.setStart_time(DateUtil.getFormattedDateUtil(DateUtil.addDate(now, -1), "yyyy-MM-dd HH:mm:ss"));
                        dataSource.setEnd_time(DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss"));
                        response = ihfApi.queryRecharge(dataSource);
                    } catch (HfApiException e) {
                        e.printStackTrace();
                    }
                    if (!response.isSuccess()) {
                        logger.error("充值单错误=====" + response.getResp_desc() + "===流水号" + rechargeOrder.getRechargeCode(), new Exception());
                        Schedule sch = schs.get(0);
                        sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_FAILD.getValue()));
                        scheduleService.updateSchedule(sch);
                        map.clear();
                        map.put("scheduleId", sch.getScheduleId());
                        List<CapitalFlow> caps = capitalFlowService.findByCondition(map);
                        if (caps != null && caps.size() > 0) {
                            for (CapitalFlow cap : caps) {
                                cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_FAILD.getValue()));
                                capitalFlowService.updateCapital(cap);
                            }
                        }
                        if (null != rechargeOrder.getPayId()) {
                            PayOrder payOrder = payService.getPayOrderById(rechargeOrder.getPayId(), true);
                            map.clear();
                            map.put("businessId", payOrder.getPayId());
                            map.put("businessType", AccountConstants.BusinessTypeEnum.FEESTYPE_TOBUYFREEZEN.getValue());
                            List<Schedule> schs2 = scheduleService.findByCondition(map);
                            if (null != schs2 && schs2.size() > 0) {
                                Schedule sch2 = schs2.get(0);
                                sch2.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_FAILD.getValue()));
                                scheduleService.updateSchedule(sch2);
                                map.clear();
                                map.put("scheduleId", sch2.getScheduleId());
                                List<CapitalFlow> caps2 = capitalFlowService.findByCondition(map);
                                if (caps2 != null && caps2.size() > 0) {
                                    for (CapitalFlow cap : caps2) {
                                        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_FAILD.getValue()));
                                        capitalFlowService.updateCapital(cap);
                                    }
                                }
                            }
                            UserAccount account = userAccountService.getCashAccount(payOrder.getUserId());
                            AccountValueChangedQueue avcq = new AccountValueChangedQueue();
                            AccountValueChanged unfreeze = new AccountValueChanged(account.getAccId(), payOrder.getAmount().subtract(rechargeOrder.getAmount()), payOrder.getAmount().subtract(rechargeOrder.getAmount())
                                    , AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_BALANCE_CHANGE_THAW.getValue()
                                    , AccountConstants.AccountChangedTypeEnum.PAYORDER.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue()
                                    , payOrder.getPayId(), AccountConstants.OwnerTypeEnum.USER.getValue(), payOrder.getUserId()
                                    , now, StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.UNFREEZE_FOR_BUS_HANDLING_SUCCESS, "充值失败", payOrder.getAmount().subtract(rechargeOrder.getAmount())), false);
                            avcq.addAccountValueChanged(unfreeze);
                            userAccountOperateService.execute(avcq);
                        }
                        failureRechange(rechargeOrder);
                    }
                }
            }
        }
        Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行充值状态刷新timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void withDrawRefreshTask() {
        Date now = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行提现刷新timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));

        WithDraw withDraw = new WithDraw();
        withDraw.setTransStatus(WithDrawTransferStatus.TRANSFER_ING.getValue());
        List<WithDraw> withDrawList = withDrawService.findBy(withDraw);
        for (int i = 0; i < withDrawList.size(); i++) {
            withDrawService.refreshNew(withDrawList.get(i).getWithdrawId().toString());
        }
        Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行提现刷新timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void refreshUnRechargeOrderTask() {
        Date now = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行24小时未支付充值单刷新timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));
        myBatisDao.update("RECHARGE_ORDER.refreshUnRechargeOrderTask", null);
        Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行24小时未支付充值单刷新timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void voucherRefreshTask() {
        Date now = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行刷新财富券有效期timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));
        //刷新财富券产品
        voucherService.refreshProductStatus(now);
        //刷新财富券
        voucherService.refreshStatus(now);
        Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行刷新财富券有效期timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void wechatVoucherExpireMsgTask() {
        Date now = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行财富劵到期提醒timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));
        voucherService.wechatVoucherExpireMsg(now);//公众号
        voucherService.sendExpireVoucherMsgForTimer();//短信
        Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行财富劵到期提醒timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void overDuePenltyTask() {
        Date now = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行提现刷新timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));
        repaymentPlanService.processOverduePenalty();
        Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行提现刷新timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 30 0 * * ?")
    public void maintainCRMStaffCustomerTask() {
        Date date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行CRM客户关系维护timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
        List<UserInfoExt> users = staffService.findStaffInUEAndHaveRecId();
        if (users != null && users.size() > 0) {
            for (UserInfoExt ue : users) {
                if (ue.getUserId().compareTo(0l) == 0) {
                    continue;
                }
                List<UserInfoExt> customers = staffService.findStaffCustomer(ue.getUserId());
                if (customers != null && customers.size() > 0) {
                    for (UserInfoExt c : customers) {
                        List<CrmFranCustomer> list = staffService.findStaffCustomers(ue.getUserId(), c.getUserId());
                        if (list == null || list.size() == 0) {
                            CrmFranCustomer customer = new CrmFranCustomer();
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
    }

    @Scheduled(cron = "0 0/6 * * * ?")
    public void autoPublishTask() {
        Date date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行自动发标timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
        try {
            mainLoanApplicationService.autoPublish();
        } catch (Exception e) {
            logger.info("【自动发标】发生异常：" + e.getMessage());
        } finally {
            autoMatchFinanceOrder();
        }
        date = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行自动发标timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void rateProductfreshTask() {
        Date now = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行刷新加息卷过期状态timer", "时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")));
        //刷新加息卷产品
        rateProductService.updateRateProductStatus(now);
        //刷新新加息卷
        rateProductService.updateRateUserStatus(now);
        Date endTime = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行加息卷过期状态timer", "时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void expireRateMsgTask() {
        Date date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行加息券到期提醒timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
        try {
            rateUserService.sendExpireRateMsgForTimer();
            logger.info("【加息券到期提醒】执行结果：成功！");
        } catch (Exception e) {
            logger.info("【加息券到期提醒】发生异常：" + e.getMessage());
        }
        date = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行加息券到期提醒timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void autoMatchFinanceOrder() {
        Date date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行刷新省心计划匹配timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
        try {
            financePlanService.creditorRightsAutoMatch();
        } catch (Exception e) {
            logger.info("【刷新省心计划】发生异常：" + e.getMessage());
        }
        date = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行刷新省心计划匹配timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void refreshFinanceBidStatus() {
        Date date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行刷新省心计划timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
        try {
            lendProductService.refreshFinanceBidStatus();
        } catch (Exception e) {
            logger.info("【刷新省心计划】发生异常：" + e.getMessage());
        }
        date = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行刷新省心计划timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void financeQuit() {
        Date date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行省心计划退出timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
        lendOrderService.scanFinanceOrderAndDoQuit();
        date = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行省心计划退出timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void doCapitalFlowTask() {
        Date date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行恒丰存管资金流timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
        try {
            scheduleService.doTask();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("【恒丰存管资金流】发生异常：" + e.getMessage());
        }
        date = new Date();
        logger.info(LogUtils.createSimpleLog("结束执行恒丰存管资金流timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * 新开用户报备
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void doUserReportTask() {
        Date date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行恒丰存管新用户报备timer", " 时间: " + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
        userReportTask.excute();
        date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行恒丰存管新用户报备timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * 商户交易报备
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void doTradeReportTask() {
        Date date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行恒丰存管新用户报备timer", " 时间: " + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
        tradeReportTask.excute();
        date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行恒丰存管新用户报备timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * 新增项目报备
     */
    @Scheduled(cron = "0 0/15 * * * ?")
    public void doLoanReportTask() {
        Date date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行恒丰存管新增项目报备timer", " 时间: " + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
        loanReportTask.excute();
        date = new Date();
        logger.info(LogUtils.createSimpleLog("开始执行恒丰存管新增项目报备timer", "时间：" + DateUtil.getFormattedDateUtil(date, "yyyy-MM-dd HH:mm:ss")));
    }

}
