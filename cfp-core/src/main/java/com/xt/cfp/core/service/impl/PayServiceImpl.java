package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.PayErrorCode;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.PayResult;
import com.xt.cfp.core.security.util.RandomUtil;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.financePlan.FinancePlanService;
import com.xt.cfp.core.util.*;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by yulei on 2015/7/7.
 */
@Service
public class PayServiceImpl implements PayService {

    private static final Logger LOGGER = Logger.getLogger(PayServiceImpl.class);

    //易宝----提现请求url
    private static String WITHDRAW_URL;
    //易宝----提现查询请求url
    private static String WITHDRAW_QUERY_URL;

    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountOperateService userAccountOperateService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private WithDrawService withDrawService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private OrderResourceService orderResourceService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private CreditorRightsTransferAppService creditorRightsTransferAppService;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private FinancePlanService financePlanService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private CapitalFlowService capitalFlowService;

    @Override
    @Transactional
    public PayOrder addPayOrder(BigDecimal amount, Date now, Long userId, PayConstants.BusTypeEnum busTypeEnum, Pair<String, BigDecimal>... amountDetails) {
        PayOrder payOrder = new PayOrder();
        payOrder.setAmount(amount);
        payOrder.setBusType(busTypeEnum.getValue());
        payOrder.setCreateTime(now);
        payOrder.setStatus(PayConstants.OrderStatus.UNPAY.getValue());
        payOrder.setUserId(userId);
        payOrder.setProcessStatus(PayConstants.ProcessStatus.UN_PROCESS.getValue());
        this.myBatisDao.insert("PAY_ORDER.insert", payOrder);
        payOrder.setPayOrderDetails(new ArrayList<PayOrderDetail>());

        for (Pair<String, BigDecimal> amountDetail : amountDetails) {
            if (amountDetail.getV().compareTo(BigDecimal.ZERO) > 0) {
                PayOrderDetail payOrderDetail = new PayOrderDetail();
                payOrderDetail.setPayId(payOrder.getPayId());
                payOrderDetail.setAmount(amountDetail.getV());
                payOrderDetail.setAmountType(amountDetail.getK());
                myBatisDao.insert("PAY_ORDER_DETAIL.insert", payOrderDetail);
                payOrder.getPayOrderDetails().add(payOrderDetail);
            }
        }
        return payOrder;
    }

    @Override
    @Transactional
    public PayOrder addPayOrderForLendOrder(LendOrder lendOrder, Date now, PayConstants.BusTypeEnum busTypeEnum, Pair<String, BigDecimal>... amountDetails) {
        PayOrder payOrder = addPayOrder(lendOrder.getBuyBalance(), now, lendOrder.getLendUserId(), busTypeEnum, amountDetails);

        //关联订单和支付单
        OrderPayRelations orderPayRelations = new OrderPayRelations();
        orderPayRelations.setLendOrderId(lendOrder.getLendOrderId());
        orderPayRelations.setPayId(payOrder.getPayId());
        myBatisDao.insert("ORDER_PAY_RELATIONS.insert", orderPayRelations);

        return payOrder;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayResult doPay(Long payId, Date date) {
        PayResult payResult = new PayResult(payId);
        PayServiceImpl bean = ApplicationContextUtil.getBean(PayServiceImpl.class);
        PayOrder payOrder2 = getPayOrderById(payId, false);
        RechargeOrder order = null;
        if (payOrder2 != null) {
            order = rechargeOrderService.getRechargeOrderByPayId(payOrder2.getPayId());
        }
        //预处理支付订单，如果确认所有支付明细均成功，就会锁定账户金额，并将支付订单状态改为支付中
        PayOrder payOrder = bean.pretreatmentForDoPay(payId, date, payResult, order == null ? null : order.getAmount());
        if (!payResult.isPayResult())
            return payResult;
        //继续进行业务处理
        if (payResult.isPayResult()) {
            try {
                bean.handleRoute(payOrder, date, payResult);
            } catch (SystemException se) {
                LOGGER.info(se.getDetailDesc());
            } finally {
                //根据业务处理结果，更新支付订单状态
                bean.refreshPayStatusForDoPay(payId, date, payResult);
            }
        }

        return payResult;
    }

    @Override
    @Transactional
    public void handleUndonePayOrderForTimer() {
        List<PayOrder> payOrders = this.myBatisDao.getList("findNeedCancelPayOrder");
        PayServiceImpl bean = ApplicationContextUtil.getBean(PayServiceImpl.class);
        PayResult payResult = null;
        Date now = new Date();
        for (PayOrder payOrder : payOrders) {
            payResult = new PayResult(payOrder.getPayId());
            payResult.setPayResult(true);
            if (payResult.isPayResult()) {
                try {
                    bean.handleRoute(payOrder, now, payResult);
                } catch (SystemException se) {
                    LOGGER.info(se.getDetailDesc());
                } finally {
                    bean.refreshPayStatusForDoPay(payOrder.getPayId(), payOrder.getCreateTime(), payResult);
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleRoute(PayOrder payOrder, Date date, PayResult payResult) {
        try {
            //购买理财
            if (payOrder.getBusType().equals(PayConstants.BusTypeEnum.BUY_FINANCE.getValue())) {
                handleBuyFinance(payOrder, date, payResult);
                //投标借款
            } else if (payOrder.getBusType().equals(PayConstants.BusTypeEnum.BID_LOAN.getValue())) {
                handleBidLoan(payOrder, date, payResult);
                //购买成功时
                if (payResult.isProcessResult()) {
                    //冻结财富券
                    voucherService.frozeVoucher(payOrder);
                }
            } else if (payOrder.getBusType().equals(PayConstants.BusTypeEnum.BUY_RIGHTS.getValue())) {
                //to do 转让
                handleBidCireditorRights(payOrder, date, payResult);
                //购买成功时
                //if (payResult.isProcessResult()){
                //冻结财富券
//                    voucherService.frozeVoucher(payOrder);
                //}
            }
        } catch (Exception e) {
            payResult.setProcessResult(false);
            if (SystemException.class.isInstance(e)) {
                payResult.setFailDesc(((SystemException) e).getBasicDesc());
                throw (SystemException) e;
            } else {
                LOGGER.error("未知异常：", e);
                payResult.setFailDesc("未知异常：" + e.getMessage());
                throw new SystemException(SystemErrorCode.UNKNOW_ERROR).set("errorMsg", e.getMessage());
            }
        }

    }

    /**
     * 执行：投标借款
     *
     * @param payOrder
     * @param date
     * @param payResult
     */
    @Transactional
    public void handleBidLoan(PayOrder payOrder, Date date, PayResult payResult) {
        LendOrder lendOrder = lendOrderService.getLendOrderByPayId(payOrder.getPayId(), true);
        payResult.setLendOrderId(lendOrder.getLendOrderId());
        lendOrderService.confirmBidLoanOrderHasPaid(lendOrder.getLendOrderId(), date, payResult);
        //根据处理结果发送投标短信
        LendOrder pOrder = null;
        if (lendOrder.getLendOrderPId() != null) {
            pOrder = lendOrderService.findById(lendOrder.getLendOrderPId());
        }
        //发短投标借款短信，需要屏蔽省心计划订单和省心计划子订单的购买短信
        if (!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
                && (pOrder == null || !pOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))) {
            sendBidResultMsg(lendOrder, payResult);
        }
    }

    /**
     * 执行：债权转让
     */
    @Transactional
    public void handleBidCireditorRights(PayOrder payOrder, Date date, PayResult payResult) {
        LendOrder lendOrder = lendOrderService.getLendOrderByPayId(payOrder.getPayId(), true);
        payResult.setLendOrderId(lendOrder.getLendOrderId());
        lendOrderService.turnCreditorRights(lendOrder.getLendOrderId(), date, payResult);
    }

    /**
     * 发送投标类短信
     *
     * @param lendOrder
     * @param payResult
     */
    private void sendBidResultMsg(LendOrder lendOrder, PayResult payResult) {
        UserInfo user = userInfoService.getUserByUserId(lendOrder.getLendUserId());
        VelocityContext context = new VelocityContext();
        try {
            try {
                context.put("date", DateUtil.getDateLongMD(new Date()));
            } catch (Exception e) {
                LOGGER.error("生成投标类短信失败", e);
            }

            String content = "";
            context.put("amount", lendOrder.getBuyBalance());
            if (payResult.isProcessResult()) {
                //投标成功后发送投标成功短信
                content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_LOANBID_SUCCESS_VM, context);
            } else {
                //投标失败后发送投标失败短信
                content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_LOANBID_FAILED_VM, context);
            }
            smsService.sendMsg(user.getMobileNo(), content);
        } catch (Exception e) {
            LOGGER.error("发送投标类短信失败", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void refreshPayStatusForDoPay(Long payId, Date date, PayResult processResult) {
        PayOrder payOrder = this.getAndLockById(payId);
        if (!payOrder.getStatus().equals(PayConstants.OrderStatus.SUCCESS.getValue())
                && !payOrder.getProcessStatus().equals(PayConstants.ProcessStatus.PROCESSING.getValue()))
            throw new SystemException(PayErrorCode.WRONG_PAY_ORDER_STATUS).set("payId", payId).set("payOrderStatus", payOrder.getStatus())
                    .set("processStatus", payOrder.getProcessStatus());

        if (processResult.isProcessResult()) {
            payOrder.setStatus(PayConstants.OrderStatus.SUCCESS.getValue());
            payOrder.setProcessStatus(PayConstants.ProcessStatus.SUCCESS.getValue());
            payOrder.setResultTime(date);
            payOrder.setDoneTime(date);
        } else {
//            UserAccount cashAccount = userAccountService.getCashAccount(payOrder.getUserId());
            LendOrder lendOrder = lendOrderService.getLendOrderByPayId(payId, false);
            UserAccount cashAccount = null;
            if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue()) || lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())) {
                cashAccount = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
            } else {
                cashAccount = userAccountService.getCashAccount(payOrder.getUserId());
            }
            //如果明细中有财富券，修改财富券使用状态(失败时代金券未冻结)
//            BigDecimal voucherValue = voucherService.calcVoucherValue(payOrder, VoucherConstants.VoucherStatus.UN_USAGE);
            BigDecimal voucherValue = voucherService.calcVoucherValue(payOrder);
            if (voucherValue.compareTo(BigDecimal.ZERO) > 0) {
                //如果存在财富劵支付明细，先判断本支付单是否已锁定财富劵，如果有，进行解绑解冻等处理。如果没有，可能是因为该财富劵已被别的订单冻结、绑定或使用，不用理会
                Voucher voucher = voucherService.getVoucherByPayId(payOrder.getPayId());
                if (voucher != null) {
                    //如果有当前订单绑定的财富劵，先判断财富劵是否被冻结，如果是，就将财富劵解冻
                    if (voucher.getStatus().equals(VoucherConstants.VoucherStatus.FREEZE.getValue()))
                        voucherService.unFreezeVoucher(voucher.getVoucherId());

                    //如果财富劵已被使用，就将财富劵的钱扣除，并将财富劵解冻
                    if (voucher.getStatus().equals(VoucherConstants.VoucherStatus.USAGE.getValue()))
                        voucherService.rollbackVoucherUse(voucher.getVoucherId());
                }
            }

            AccountValueChangedQueue queue = new AccountValueChangedQueue();
            AccountValueChanged changed = new AccountValueChanged(cashAccount.getAccId(),
                    payOrder.getAmount().subtract(voucherValue), payOrder.getAmount().subtract(voucherValue),
                    AccountConstants.AccountOperateEnum.UNFREEZE.getValue(),
                    PayConstants.BusTypeEnum.cValueOf(payOrder.getBusType()).getAccUnFrozenBusTypeEnum().getValue(),
                    AccountConstants.AccountChangedTypeEnum.PAYORDER.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(),
                    payId, AccountConstants.OwnerTypeEnum.USER.getValue(), payOrder.getUserId(), date,
                    StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.UNFREEZE_FOR_BUS_HANDLING_FAIL,
                            PayConstants.BusTypeEnum.cValueOf(payOrder.getBusType()).getAccUnFrozenBusTypeEnum().getDesc(),
                            payOrder.getAmount().subtract(voucherValue)), true);
            queue.addAccountValueChanged(changed);
            userAccountOperateService.execute(queue);

            payOrder.setStatus(PayConstants.OrderStatus.FAIL.getValue());
            payOrder.setProcessStatus(PayConstants.ProcessStatus.FAIL.getValue());
            payOrder.setResultTime(date);
            payOrder.setDoneTime(date);
        }

        this.myBatisDao.update("PAY_ORDER.updateByPrimaryKey", payOrder);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayOrder pretreatmentForDoPay(Long payId, Date date, PayResult payResult, BigDecimal fAmount) {
        LOGGER.info(LogUtils.createSimpleLog("开始支付预处理", "订单编号：" + payId));
        PayOrder payOrder = this.getAndLockById(payId);
        if (!payOrder.getStatus().equals(PayConstants.OrderStatus.UNPAY.getValue())) {
            throw new SystemException(PayErrorCode.ORDER_HAS_HANDLED).set("payId", payId).set("status", payOrder.getStatus());
        }

        LendOrder lendOrder = lendOrderService.getLendOrderByPayId(payId, false);
        UserAccount cashAccount = null;

        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue()) || lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())) {
            cashAccount = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
        } else {
            cashAccount = userAccountService.getCashAccount(payOrder.getUserId());
        }
        AccountValueChangedQueue queue = new AccountValueChangedQueue();
        payResult.setPayResult(true);

        //（1）确认充值状态
        if (payResult.isPayResult()) {
            RechargeOrder rechargeOrder = rechargeOrderService.getRechargeOrderByPayId(payId);
            if (rechargeOrder != null) {
                LOGGER.info(LogUtils.createSimpleLog("支付预处理-确认充值单状态", "支付单ID：" + payId + "充值单ID：" + rechargeOrder.getRechargeId()) + ", 充值状态：" + rechargeOrder.getStatus());
                if (!rechargeOrder.getStatus().equals(RechargeStatus.SUCCESS.getValue())) {
                    payResult.setPayResult(false);
                    payResult.setFailDesc("对应的充值订单充值失败了");
                }
            }
        }

        // (2) 判断当前代金券是否可用
        if (payResult.isPayResult()) {
            boolean voucherAvaliable = voucherService.isVoucherAvaliable(payOrder);
            if (!voucherAvaliable) {
                payResult.setPayResult(false);
                payResult.setFailDesc("购买失败，该订单使用的财富券已被使用");
            }
        }

        BigDecimal voucherValue = voucherService.calcVoucherValue(payOrder, VoucherConstants.VoucherStatus.UN_USAGE);
        BigDecimal frozeAmount = payOrder.getAmount().subtract(voucherValue);
        frozeAmount = fAmount == null ? frozeAmount : fAmount;
        //（3）确认账户余额足够
        if (payResult.isPayResult()) {
            if (frozeAmount.compareTo(cashAccount.getAvailValue2()) > 0) {
                LOGGER.info(LogUtils.createSimpleLog("支付预处理-确认账户余额", "支付单ID：" + payId + "购买金额：" + payOrder.getAmount() + ", 财富券：" + voucherValue + ", 余额：" + cashAccount.getAvailValue2()));
                payResult.setPayResult(false);
                payResult.setFailDesc("账户余额不足，购买失败");
            }
        }

        //（4）确认资金可用情况，并锁定相关资金
        if (payResult.isPayResult()) {
            LOGGER.info(LogUtils.createSimpleLog("支付预处理-确认资金可用情况", "账户ID：" + cashAccount.getUserId()));
            AccountValueChanged changed = new AccountValueChanged(cashAccount.getAccId(), frozeAmount, frozeAmount,
                    AccountConstants.AccountOperateEnum.FREEZE.getValue(),
                    PayConstants.BusTypeEnum.cValueOf(payOrder.getBusType()).getAccFrozenBusTypeEnum().getValue(),
                    AccountConstants.AccountChangedTypeEnum.PAYORDER.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(),
                    payId, AccountConstants.OwnerTypeEnum.USER.getValue(), payOrder.getUserId(), date,
                    StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.FREEZE_FOR_BUS_HANDLING,
                            PayConstants.BusTypeEnum.cValueOf(payOrder.getBusType()).getAccFrozenBusTypeEnum().getDesc(),
                            payOrder.getAmount().subtract(voucherValue)), true);
            queue.addAccountValueChanged(changed);
            try {
                //执行现金流
                userAccountOperateService.execute(queue);
            } catch (SystemException se) {
                //如果执行锁定现金流失败，说明支付失败
                LOGGER.info(se.getDetailDesc(), se);
                payResult.setPayResult(false);
                payResult.setFailDesc("账户可用余额不足");
                LOGGER.info(LogUtils.createSimpleLog("支付预处理-确认资金可用情况", "支付单ID：" + payId + "结果：失败，账户ID：" + cashAccount.getAccId()));
            }
        }

        if (payResult.isPayResult()) {
            payOrder.setStatus(PayConstants.OrderStatus.SUCCESS.getValue());
            payOrder.setProcessStatus(PayConstants.ProcessStatus.PROCESSING.getValue());
        } else {
            payOrder.setStatus(PayConstants.OrderStatus.FAIL.getValue());
            payOrder.setProcessStatus(PayConstants.ProcessStatus.UN_PROCESS.getValue());
        }

        this.myBatisDao.update("PAY_ORDER.updatePayOrderStatusByPayOrderId", payOrder);
        return payOrder;
    }

    @Override
    public PayOrder getPayOrderById(Long payId, boolean assertTrue) {
        PayOrder payOrder = this.myBatisDao.get("PAY_ORDER.selectByPrimaryKey", payId);
        if (assertTrue)
            if (payOrder == null)
                throw new SystemException(PayErrorCode.ORDER_NOT_FOUND_FOR_ORDERID).set("payId", payId);
        return payOrder;
    }

    @Override
    public List<PayOrderDetail> findPayOrderDetailByPayId(Long payId) {
        return this.myBatisDao.getList("PAY_ORDER_DETAIL.findPayOrderDetailByPayId", payId);
    }

    private PayOrder getAndLockById(Long payId) {
        return this.myBatisDao.get("PAY_ORDER.getAndLockById", payId);
    }

    @Transactional
    public void handleBuyFinance(PayOrder payOrder, Date date, PayResult payResult) {
        try {
            LendOrder lendOrder = lendOrderService.getLendOrderByPayId(payOrder.getPayId(), true);
            payResult.setLendOrderId(lendOrder.getLendOrderId());
            lendOrderService.confirmFinanceOrderHasPaid(lendOrder.getLendOrderId(), date, payResult);
            sendFinanceResultMsg(lendOrder, TemplateType.SMS_FINANCEPLAN_SUCCESS_VM);
        } catch (Exception se) {
            payResult.setProcessResult(false);
            if (SystemException.class.isInstance(se)) {
                LOGGER.info(((SystemException) se).getDetailDesc());
                payResult.setFailDesc(((SystemException) se).getBasicDesc());
            } else {
                LOGGER.info("未知异常", se);
                payResult.setFailDesc("未知异常：" + se.getMessage());
            }
        }
    }

    /**
     * 发送省心计划短信
     *
     * @param lendOrder
     * @param templateVM
     */
    @Override
    public void sendFinanceResultMsg(LendOrder lendOrder, TemplateType templateVM) {
        UserInfo user = userInfoService.getUserByUserId(lendOrder.getLendUserId());
        VelocityContext context = new VelocityContext();
        try {
            try {
                context.put("date", DateUtil.getDateLongMD(new Date()));
//                context.put("amount", lendOrder.getBuyBalance());
                context.put("financePlan", lendOrder.getLendOrderName());
            } catch (Exception e) {
                LOGGER.error("生成省心计划短信失败", e);
            }

            String content = "";
            context.put("amount", lendOrder.getBuyBalance());
            //购买省心计划成功后发送投标成功短信
            content = TemplateUtil.getStringFromTemplate(templateVM, context);
//            else{
//                //购买省心计划失败后发送投标失败短信
//                content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_LOANBID_FAILED_VM,context);
//            }
            smsService.sendMsg(user.getMobileNo(), content);
        } catch (Exception e) {
            LOGGER.error("发送投标类短信失败", e);
        }
    }


    /**
     * 添加-支付订单。
     */
    @Override
    public PayOrder addPayOrder(PayOrder payOrder) {
        myBatisDao.insert("PAY_ORDER.insert", payOrder);
        return payOrder;
    }

    /**
     * 添加-支付订单明细。
     */
    @Override
    public PayOrderDetail addPayOrderDetail(PayOrderDetail payOrderDetail) {
        myBatisDao.insert("PAY_ORDER_DETAIL.insert", payOrderDetail);
        return payOrderDetail;
    }

    @Override
    public PayOrderDetail getPayOrderDetailByPayIdAndAmountType(Long payId, String amountType) {
        List<PayOrderDetail> list = myBatisDao.getList("PAY_ORDER_DETAIL.findPayOrderDetailByPayId", payId);
        for (PayOrderDetail orderDetail : list) {
            if (orderDetail.getAmountType().equals(amountType))
                return orderDetail;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(RandomUtil.getRandom(16));
    }


    @Override
    public void withDraw(Long withDrawId) {
        //获得提现单详情
        WithDraw detail = withDrawService.detail(withDrawId);

    }

    @Override
    public PayOrder getCurrentUnpayPayOrderByLendOrderId(Long lendOrderId) {
        List<Object> list = myBatisDao.getList("PAY_ORDER.getCurrentUnpayPayOrderByLendOrderId", lendOrderId);
        return list.size() > 0 ? (PayOrder) list.get(0) : null;
    }

    @Override
    public PayOrder getPaidPayOrderByLendOrderId(Long lendOrderId) {
        List<Object> list = myBatisDao.getList("PAY_ORDER.getPaidPayOrderByLendOrderId", lendOrderId);
        return list.size() > 0 ? (PayOrder) list.get(0) : null;
    }

    @Override
    public LendOrder getLendOrderByPayOrderDetailId(Long detailId) {
        PayOrderDetail payOrderDetail = myBatisDao.get("PAY_ORDER_DETAIL.selectByPrimaryKey", detailId);
        if (payOrderDetail != null) {
            LendOrder lendOrder = lendOrderService.getLendOrderByPayId(payOrderDetail.getPayId(), true);
            return lendOrder;
        }
        return null;
    }

    @Override
    public PayOrder createPayOrder(BigDecimal accountPayValue, BigDecimal rechargePayValue, BigDecimal voucherPayValue, UserInfo currentUser, LendOrder lendOrder) {
        PayConstants.BusTypeEnum busTypeEnum = null;
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
            busTypeEnum = PayConstants.BusTypeEnum.BUY_FINANCE;

        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue()))
            busTypeEnum = PayConstants.BusTypeEnum.BID_LOAN;

        if (lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue()))
            busTypeEnum = PayConstants.BusTypeEnum.BUY_RIGHTS;
        Pair<String, BigDecimal> accountAmount = new Pair<String, BigDecimal>(PayConstants.AmountType.ACCOUNT.getValue(), accountPayValue);
        Pair<String, BigDecimal> rechargeAmount = new Pair<String, BigDecimal>(PayConstants.AmountType.RECHARGE.getValue(), rechargePayValue);
        //todo 财富券
        Pair<String, BigDecimal> voucherAmount = new Pair<String, BigDecimal>(PayConstants.AmountType.VOUCHERS.getValue(), voucherPayValue);
        return addPayOrderForLendOrder(lendOrder, new Date(), busTypeEnum, accountAmount, rechargeAmount, voucherAmount);
    }

    @Override
    public void update(PayOrder payOrder) {
        myBatisDao.update("PAY_ORDER.updateByPrimaryKey", payOrder);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayOrder createBidPayOrderForFinanceLendOrder(LendOrder lendOrder, Long loanApplicationId, Long productPublishId, BigDecimal amount, Date now, LendProduct lendProduct, String resource) {
        LoanPublish loanPublish = loanApplicationService.getLoanPublishByAppId(loanApplicationId);
        //新建购买订单
        LendOrder newLendOrder = new LendOrder();
        newLendOrder.setBuyBalance(amount);
        newLendOrder.setBuyTime(now);
        newLendOrder.setCurrentProfit2(BigDecimal.ZERO);
        newLendOrder.setCurrentProfit(BigDecimal.ZERO);
        newLendOrder.setClosingDate(lendProduct.getClosingDate());
        newLendOrder.setClosingType(lendProduct.getClosingType());
        newLendOrder.setCustomerAccountId(lendOrder.getCustomerAccountId());
        newLendOrder.setForLendBalance(BigDecimal.ZERO);
        newLendOrder.setDisplayState(DisplayEnum.HIDDEN.getValue());
        newLendOrder.setLendUserId(lendOrder.getLendUserId());
        newLendOrder.setLendProductPublishId(productPublishId);
        newLendOrder.setLendOrderName(loanPublish.getLoanTitle());
        newLendOrder.setLendProductId(lendProduct.getLendProductId());
        //todo 确认订单编号的生成规则
        String prefix = "L";
        newLendOrder.setOrderCode(prefix + DateUtil.getFormattedDateUtil(now, "yyyyMMddHHmmssSSS"));
        newLendOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue());
        newLendOrder.setProductType(LendProductTypeEnum.RIGHTING.getValue());
        newLendOrder.setProfitRate(lendProduct.getProfitRate());
        newLendOrder.setRenewal(lendProduct.getRenewal());
        newLendOrder.setRenewalCycleType(lendProduct.getRenewalCycleType());
        newLendOrder.setRenewalType(lendProduct.getRenewalType());
        newLendOrder.setTheReturnMethod("0");
        newLendOrder.setTimeLimit(lendProduct.getTimeLimit());
        newLendOrder.setTimeLimitType(lendProduct.getTimeLimitType());
        newLendOrder.setToInterestPoint(lendProduct.getToInterestPoint());

        newLendOrder.setLendOrderPId(lendOrder.getLendOrderId());
        myBatisDao.insert("LENDORDER.insert", newLendOrder);
        //记录订单来源
        OrderResource or = orderResourceService.selectBYDesc(resource);
        orderResourceService.addResourceFrom(newLendOrder.getLendOrderId(), Long.parseLong(OrderResourceEnum.MAPPING_TYPE_LEND.getValue()), or.getResourceId(), lendOrder.getBuyTime());

        //订单明细
        LendOrderBidDetail lendOrderBidDetail = new LendOrderBidDetail();
        lendOrderBidDetail.setBuyBalance(newLendOrder.getBuyBalance());
        lendOrderBidDetail.setBuyDate(newLendOrder.getBuyTime());
        lendOrderBidDetail.setLendOrderId(newLendOrder.getLendOrderId());
        lendOrderBidDetail.setLoanApplicationId(loanApplicationId);
        lendOrderBidDetail.setStatus(LendOrderBidStatusEnum.WAITING_PAY.getValue().toCharArray()[0]);
        lendOrderBidDetailService.insert(lendOrderBidDetail);
        //新建支付单
        Pair<String, BigDecimal> amountDetail = new Pair<String, BigDecimal>(PayConstants.AmountType.FINANCEACCOUNT.getValue(), amount);
        PayOrder payOrder = addPayOrder(amount, now, newLendOrder.getLendUserId(), PayConstants.BusTypeEnum.BID_LOAN, amountDetail);
        //关联订单和支付单
        OrderPayRelations orderPayRelations = new OrderPayRelations();
        orderPayRelations.setLendOrderId(newLendOrder.getLendOrderId());
        orderPayRelations.setPayId(payOrder.getPayId());
        myBatisDao.insert("ORDER_PAY_RELATIONS.insert", orderPayRelations);

        return payOrder;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayOrder createRightsPayOrderForFinanceLendOrder(LendOrder lendOrder, Long loanApplicationId, Long productPublishId, BigDecimal amount, Date now, LendProduct lendProduct, String resource, CreditorRightsTransferApplication crta) {
        LoanPublish loanPublish = loanApplicationService.getLoanPublishByAppId(loanApplicationId);

        //新建购买订单
        LendOrder newLendOrder = new LendOrder();
        newLendOrder.setBuyBalance(amount);
        newLendOrder.setBuyTime(now);
        newLendOrder.setCurrentProfit2(BigDecimal.ZERO);
        newLendOrder.setCurrentProfit(BigDecimal.ZERO);
        newLendOrder.setClosingDate(lendProduct.getClosingDate());
        newLendOrder.setClosingType(lendProduct.getClosingType());
        newLendOrder.setCustomerAccountId(lendOrder.getCustomerAccountId());
        newLendOrder.setForLendBalance(BigDecimal.ZERO);
        newLendOrder.setDisplayState(DisplayEnum.HIDDEN.getValue());
        newLendOrder.setLendUserId(lendOrder.getLendUserId());
        newLendOrder.setLendProductPublishId(productPublishId);
        newLendOrder.setLendOrderName(loanPublish.getLoanTitle());
        newLendOrder.setLendProductId(lendProduct.getLendProductId());
        //todo 确认订单编号的生成规则
        String prefix = "L";
        newLendOrder.setOrderCode(prefix + DateUtil.getFormattedDateUtil(now, "yyyyMMddHHmmssSSS"));
        newLendOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue());
        newLendOrder.setProductType(LendProductTypeEnum.CREDITOR_RIGHTS.getValue());
        newLendOrder.setProfitRate(lendProduct.getProfitRate());
        newLendOrder.setRenewal(lendProduct.getRenewal());
        newLendOrder.setRenewalCycleType(lendProduct.getRenewalCycleType());
        newLendOrder.setRenewalType(lendProduct.getRenewalType());
        newLendOrder.setTheReturnMethod("0");
        //todo
        newLendOrder.setTimeLimit(crta.getTimeLimit());
        newLendOrder.setTimeLimitType(lendProduct.getTimeLimitType());
        newLendOrder.setToInterestPoint(lendProduct.getToInterestPoint());

        newLendOrder.setLendOrderPId(lendOrder.getLendOrderId());
        myBatisDao.insert("LENDORDER.insert", newLendOrder);
        //记录订单来源
        OrderResource or = orderResourceService.selectBYDesc(resource);
        orderResourceService.addResourceFrom(newLendOrder.getLendOrderId(), Long.parseLong(OrderResourceEnum.MAPPING_TYPE_LEND.getValue()), or.getResourceId(), newLendOrder.getBuyTime());

        //订单明细
        LendOrderBidDetail lendOrderBidDetail = new LendOrderBidDetail();
        lendOrderBidDetail.setBuyBalance(newLendOrder.getBuyBalance());
        lendOrderBidDetail.setBuyDate(newLendOrder.getBuyTime());
        lendOrderBidDetail.setLendOrderId(newLendOrder.getLendOrderId());
        lendOrderBidDetail.setLoanApplicationId(loanApplicationId);
        lendOrderBidDetail.setStatus(LendOrderBidStatusEnum.WAITING_PAY.getValue().toCharArray()[0]);
        lendOrderBidDetailService.insert(lendOrderBidDetail);

        //转让申请明细
        creditorRightsTransferAppService.addCreditorRightsDealDetail(crta, newLendOrder);

        //新建支付单
        Pair<String, BigDecimal> amountDetail = new Pair<String, BigDecimal>(PayConstants.AmountType.FINANCEACCOUNT.getValue(), amount);
        PayOrder payOrder = addPayOrder(amount, now, newLendOrder.getLendUserId(), PayConstants.BusTypeEnum.BUY_RIGHTS, amountDetail);
        //关联订单和支付单
        OrderPayRelations orderPayRelations = new OrderPayRelations();
        orderPayRelations.setLendOrderId(newLendOrder.getLendOrderId());
        orderPayRelations.setPayId(payOrder.getPayId());
        myBatisDao.insert("ORDER_PAY_RELATIONS.insert", orderPayRelations);

        return payOrder;
    }


    @Override
    public List<PayOrderDetail> getPaymentOrderDetail(long lendOrderId) {

        return myBatisDao.getList("PAY_ORDER_DETAIL.getPaymentOrderDetail", lendOrderId);
    }
}
