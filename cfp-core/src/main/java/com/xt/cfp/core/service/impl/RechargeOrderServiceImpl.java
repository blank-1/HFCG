package com.xt.cfp.core.service.impl;

import com.external.deposites.utils.HfUtils;
import com.external.llpay.LLPayRequest;
import com.external.llpay.LLPayUtil;
import com.external.llpay.PayDataBean;
import com.external.yeepay.QueryResult;
import com.external.yeepay.TZTService;
import com.external.yeepay.YeePayUtil;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.PayErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderPointEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderStatusEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.context.ParaContext;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.PayResult;
import com.xt.cfp.core.pojo.ext.VoucherVO;
import com.xt.cfp.core.pojo.ext.phonesell.PrepaidVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.financePlan.FinancePlanProcessModule;
import com.xt.cfp.core.service.financePlan.FinancePlanService;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by luqinglin on 2015/6/25.
 */
@Service
public class RechargeOrderServiceImpl implements RechargeOrderService {

    private static Logger logger = Logger.getLogger(RechargeOrderServiceImpl.class);

    @Autowired
    private MyBatisDao myBatisDao;
    @Autowired
    private UserAccountOperateService userAccountOperateService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private PayService payService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private OrderResourceService orderResourceService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private LendProductService lendProductService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private RateLendOrderService rateLendOrderService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    @Autowired
    private FinancePlanService financePlanService;
    @Autowired
    private RedisCacheManger redisCacheManger;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private CapitalFlowService capitalFlowService;
    @Autowired
    private CgBizService cgBizService;

    @Override
    @Transactional
    public RechargeOrder recharge(RechargeOrder rechargeOrder, long accId, String ownerType, long ownerId) {
        //判断参数是否为null
        if (rechargeOrder == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("rechargeOrder", "null");
        if (accId == 0l)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("accId", "0");

        //记录充值单(目前只支持转账、现金，不支持划扣)
        String code = UUID.randomUUID().toString().replaceAll("-", "");
        rechargeOrder.setRechargeCode(code);
        rechargeOrder.setCreateTime(new Date());
        rechargeOrder.setStatus(RechargeStatus.SUCCESS.getValue());
        myBatisDao.insert("RECHARGE_ORDER.insertSelective", rechargeOrder);


        AccountValueChangedQueue accountValueChangedQueue = new AccountValueChangedQueue();
        AccountValueChanged accountValueChanged = new AccountValueChanged(accId, rechargeOrder.getAmount(), rechargeOrder.getAmount(), AccountConstants.AccountOperateEnum.INCOM.getValue(),
                AccountConstants.BusinessTypeEnum.FEESTYPE_RECHARGE.getValue(), AccountConstants.AccountChangedTypeEnum.RECHARGE.getValue(),
                "1", rechargeOrder.getRechargeId(), ownerType, ownerId, new Date(), "充值" + rechargeOrder.getAmount() + "元", false);
        accountValueChangedQueue.addAccountValueChanged(accountValueChanged);
        userAccountOperateService.execute(accountValueChangedQueue);
        return rechargeOrder;
    }

    @Override
    @Transactional
    public RechargeOrder recharge(RechargeOrder rechargeOrder, AccountConstants.AccountChangedTypeEnum ownerType) {
        //判断参数是否为null
        if (rechargeOrder == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("rechargeOrder", "null");
        if (ownerType == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("ownerType", "null");

        //获取用户的出借账户
        UserAccount userAccount = userAccountService.getCashAccount(rechargeOrder.getUserId());

        rechargeOrder = recharge(rechargeOrder, userAccount.getAccId(), ownerType.getValue(), rechargeOrder.getUserId());
        return rechargeOrder;
    }

    @Override
    public Pagination<RechargeOrder> getRechargeOrderPaging(int pageNo, int pageSize, RechargeOrder rechargeOrder, Map<String, Object> customParams) {
        Pagination<RechargeOrder> re = new Pagination<RechargeOrder>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("rechargeOrder", rechargeOrder);
        params.put("customParams", customParams);

        int totalCount = this.myBatisDao.count("getIncomePaging", params);
        List<RechargeOrder> uah = this.myBatisDao.getListForPaging("getIncomePaging", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
    }

    @Override
    public Pagination<PrepaidVO> getPrepaidPaging(int pageNo, int pageSize, Map<String, Object> customParams) {
        Pagination<PrepaidVO> re = new Pagination<PrepaidVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        if (customParams.size() > 0) {
            Set<String> keys = customParams.keySet();
            for (String key : keys) {
                params.put(key, customParams.get(key));
            }
        }
        int totalCount = this.myBatisDao.count("getPrepaidList", params);
        List<PrepaidVO> uah = this.myBatisDao.getListForPaging("getPrepaidList", params, pageNo, pageSize);
        List<String> nums = getAccountAllByStatus(customParams);
        for (PrepaidVO p : uah) {
            p.setNums(nums);
        }
        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
    }

    @Override
    public List<String> getAccountAllByStatus(Map<String, Object> customParams) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (customParams.size() > 0) {
            Set<String> keys = customParams.keySet();
            for (String key : keys) {
                params.put(key, customParams.get(key));
            }
        }
        List<String> nums = this.myBatisDao.getList("getAccountAllByStatus", params);
        return nums;
    }

    @Override
    @Transactional
    public RechargeOrder addRechargeOrder(BigDecimal amount, Long bankCardId, Long payId, Long userId, String channelCode, String resource) {
        RechargeOrder rechargeOrder = new RechargeOrder();
        CustomerCard bankCard = null;
        if (bankCardId != null)
            bankCard = customerCardService.findById(bankCardId);
        rechargeOrder.setAmount(amount);
        rechargeOrder.setBankCode(bankCard != null ? bankCard.getBankCode().toString() : null);
        rechargeOrder.setCardNo(bankCard != null ? bankCard.getCardCode() : null);
        rechargeOrder.setChannelCode(channelCode);
        rechargeOrder.setCreateTime(new Date());
        rechargeOrder.setCustomerCardId(bankCardId);
        rechargeOrder.setRechargeCode(UUID.randomUUID().toString().replace("-", ""));
        rechargeOrder.setStatus(RechargeStatus.UN_RECHARGE.getValue());
        rechargeOrder.setUserId(userId);
        if (payId != null) {
            PayOrder payOrder = payService.getPayOrderById(payId, true);
            PayOrderDetail orderDetail = payService.getPayOrderDetailByPayIdAndAmountType(payId, PayConstants.AmountType.RECHARGE.getValue());
            if (orderDetail == null)
                throw new SystemException(PayErrorCode.PAYORDER_NOT_INCLUD_RECHARGE_DETAIL).set("payId", payId);

            if (orderDetail.getAmount().compareTo(amount) != 0)
                throw new SystemException(PayErrorCode.RECHARGE_AMOUNT_NOT_EQUALS_PAYORDER_DEATIL_RECHARGE_AMOUNT).set("rechargeAmount", amount)
                        .set("payOrderDetailAmount", orderDetail.getAmount()).set("payId", payId);

            if (!payOrder.getUserId().equals(userId))
                throw new SystemException(PayErrorCode.ERROR_RELATION).set("payUserId", payOrder.getUserId()).set("rechargeUserId", userId).set("payId", payId);

            rechargeOrder.setPayId(payId);
            rechargeOrder.setDetailId(orderDetail.getDetailId());
        }

        this.myBatisDao.insert("RECHARGE_ORDER.insertSelective", rechargeOrder);
        //记录订单来源
        OrderResource or = orderResourceService.selectBYDesc(resource);
        orderResourceService.addResourceFrom(rechargeOrder.getRechargeId(), Long.parseLong(OrderResourceEnum.MAPPING_TYPE_RECHARGE.getValue()), or.getResourceId(), rechargeOrder.getCreateTime());
        return rechargeOrder;
    }

    @Override
    public RechargeOrder addRechargeOrderForLL(BigDecimal amount, CustomerCard card, Long payId, Long userId, String channelCode, String resource) {
        RechargeOrder rechargeOrder = new RechargeOrder();

        rechargeOrder.setAmount(amount);
        rechargeOrder.setBankCode(card != null && card.getBankCode() != null ? card.getBankCode().toString() : null);
        rechargeOrder.setCardNo(card != null && card.getCardCode() != null ? card.getCardCode() : null);
        rechargeOrder.setChannelCode(channelCode);
        rechargeOrder.setCreateTime(new Date());
        rechargeOrder.setCustomerCardId(card != null ? card.getCustomerCardId() : null);
        rechargeOrder.setRechargeCode(UUID.randomUUID().toString().replace("-", ""));
        rechargeOrder.setStatus(RechargeStatus.UN_RECHARGE.getValue());
        rechargeOrder.setUserId(userId);
        if (payId != null) {
            PayOrder payOrder = payService.getPayOrderById(payId, true);
            PayOrderDetail orderDetail = payService.getPayOrderDetailByPayIdAndAmountType(payId, PayConstants.AmountType.RECHARGE.getValue());
            if (orderDetail == null)
                throw new SystemException(PayErrorCode.PAYORDER_NOT_INCLUD_RECHARGE_DETAIL).set("payId", payId);

            if (orderDetail.getAmount().compareTo(amount) != 0)
                throw new SystemException(PayErrorCode.RECHARGE_AMOUNT_NOT_EQUALS_PAYORDER_DEATIL_RECHARGE_AMOUNT).set("rechargeAmount", amount)
                        .set("payOrderDetailAmount", orderDetail.getAmount()).set("payId", payId);

            if (!payOrder.getUserId().equals(userId))
                throw new SystemException(PayErrorCode.ERROR_RELATION).set("payUserId", payOrder.getUserId()).set("rechargeUserId", userId).set("payId", payId);

            rechargeOrder.setPayId(payId);
            rechargeOrder.setDetailId(orderDetail.getDetailId());
        }

        this.myBatisDao.insert("RECHARGE_ORDER.insertSelective", rechargeOrder);
        //记录订单来源
        OrderResource or = orderResourceService.selectBYDesc(resource);
        orderResourceService.addResourceFrom(rechargeOrder.getRechargeId(), Long.parseLong(OrderResourceEnum.MAPPING_TYPE_RECHARGE.getValue()), or.getResourceId(), rechargeOrder.getCreateTime());
        return rechargeOrder;
    }

    @Override
    public RechargeOrder addRechargeOrderForHF(BigDecimal amount, CustomerCard card, Long payId, Long userId, String channelCode, String resource,String code) {
        RechargeOrder rechargeOrder = new RechargeOrder();

        rechargeOrder.setAmount(amount);
        rechargeOrder.setBankCode(card != null && card.getBankCode() != null ? card.getBankCode().toString() : null);
        rechargeOrder.setCardNo(card != null && card.getCardCode() != null ? card.getCardCode() : null);
        rechargeOrder.setChannelCode(channelCode);
        rechargeOrder.setCreateTime(new Date());
        rechargeOrder.setCustomerCardId(card != null ? card.getCustomerCardId() : null);
        rechargeOrder.setRechargeCode(code);
        rechargeOrder.setStatus(RechargeStatus.UN_RECHARGE.getValue());
        rechargeOrder.setUserId(userId);
        if (payId != null) {
            PayOrder payOrder = payService.getPayOrderById(payId, true);
            PayOrderDetail orderDetail = payService.getPayOrderDetailByPayIdAndAmountType(payId, PayConstants.AmountType.RECHARGE.getValue());
            if (orderDetail == null)
                throw new SystemException(PayErrorCode.PAYORDER_NOT_INCLUD_RECHARGE_DETAIL).set("payId", payId);

            if (orderDetail.getAmount().compareTo(amount) != 0)
                throw new SystemException(PayErrorCode.RECHARGE_AMOUNT_NOT_EQUALS_PAYORDER_DEATIL_RECHARGE_AMOUNT).set("rechargeAmount", amount)
                        .set("payOrderDetailAmount", orderDetail.getAmount()).set("payId", payId);

            if (!payOrder.getUserId().equals(userId))
                throw new SystemException(PayErrorCode.ERROR_RELATION).set("payUserId", payOrder.getUserId()).set("rechargeUserId", userId).set("payId", payId);

            rechargeOrder.setPayId(payId);
            rechargeOrder.setDetailId(orderDetail.getDetailId());
        }

        this.myBatisDao.insert("RECHARGE_ORDER.insertSelective", rechargeOrder);
        //记录订单来源
        OrderResource or = orderResourceService.selectBYDesc(resource);
        orderResourceService.addResourceFrom(rechargeOrder.getRechargeId(), Long.parseLong(OrderResourceEnum.MAPPING_TYPE_RECHARGE.getValue()), or.getResourceId(), rechargeOrder.getCreateTime());
        return rechargeOrder;
    }

    @Override
    public RechargeOrder addRechargeOrderForCg(BigDecimal amount, CustomerCard card,  Long userId, String resource) {
        RechargeOrder rechargeOrder = new RechargeOrder();

        rechargeOrder.setAmount(amount);
        rechargeOrder.setBankCode(card != null && card.getBankCode() != null ? card.getBankCode().toString() : null);
        rechargeOrder.setCardNo(card != null && card.getCardCode() != null ? card.getCardCode() : null);
//        rechargeOrder.setChannelCode(channelCode);
        rechargeOrder.setCreateTime(new Date());
        rechargeOrder.setCustomerCardId(card != null ? card.getCustomerCardId() : null);
        rechargeOrder.setRechargeCode(HfUtils.getUniqueSerialNum());
        rechargeOrder.setStatus(RechargeStatus.UN_RECHARGE.getValue());
        rechargeOrder.setUserId(userId);

        this.myBatisDao.insert("RECHARGE_ORDER.insertSelective", rechargeOrder);
        //记录订单来源
        OrderResource or = orderResourceService.selectBYDesc(resource);
        orderResourceService.addResourceFrom(rechargeOrder.getRechargeId(), Long.parseLong(OrderResourceEnum.MAPPING_TYPE_RECHARGE.getValue()), or.getResourceId(), rechargeOrder.getCreateTime());
        return rechargeOrder;
    }

    @Override
    public RechargeOrder getRechargeOrderByPayId(Long payId) {
        return myBatisDao.get("RECHARGE_ORDER.getRechargeOrderByPayId", payId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RechargeOrder confirmRecharge(String rechargeCode, String externalNo, String rechargeResult) {
        Date now = new Date();
        RechargeOrder rechargeOrder = this.getRechargeOrderByOrderCode(rechargeCode, true);
        rechargeOrder.setResultTime(now);
        //如果订单还未被处理，更新充值订单的状态
        if (rechargeResult.equals(RechargeStatus.SUCCESS.getValue())
                && rechargeOrder.getStatus().equals(RechargeStatus.UN_RECHARGE.getValue())) {
            rechargeOrder.setStatus(RechargeStatus.SUCCESS.getValue());
            rechargeOrder.setExternalNo(externalNo);
            UserAccount userAccount = this.userAccountService.getCashAccount(rechargeOrder.getUserId());
            //充值现金流
            AccountValueChangedQueue avcq = new AccountValueChangedQueue();
            AccountValueChanged income = new AccountValueChanged(userAccount.getAccId(), rechargeOrder.getAmount(), rechargeOrder.getAmount()
                    , AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_RECHARGE.getValue()
                    , AccountConstants.AccountChangedTypeEnum.RECHARGE.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue()
                    , rechargeOrder.getRechargeId(), AccountConstants.OwnerTypeEnum.USER.getValue(), rechargeOrder.getUserId()
                    , now, StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECHARGE_IN, rechargeOrder.getAmount()), false);
            avcq.addAccountValueChanged(income);
            this.userAccountOperateService.execute(avcq);
            this.myBatisDao.update("RECHARGE_ORDER.updateByPrimaryKeySelective", rechargeOrder);

            //更新任务调度
            Map map=new HashMap();
            map.put("businessId",rechargeOrder.getRechargeId());
            map.put("businessFlow",rechargeCode);
            CapitalFlow cap=capitalFlowService.findByCondition(map).get(0);
            cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_SUCCESS.getValue()));
            cap.setEndTime(new Date());
            capitalFlowService.updateCapital(cap);
            map.clear();
            map.put("businessId",rechargeOrder.getRechargeId());
            Schedule sch=scheduleService.findByCondition(map).get(0);
            sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_SUCCESS.getValue()));
            sch.setEndTime(new Date());
            scheduleService.updateSchedule(sch);

            //如果更新充值订单为支付成功则发送充值成功短信
            try {
                if (rechargeOrder.getStatus().equals(RechargeStatus.SUCCESS.getValue())) {
                    UserInfo user = userInfoService.getUserByUserId(rechargeOrder.getUserId());
                    VelocityContext context = new VelocityContext();
                    context.put("amount", rechargeOrder.getAmount());
                    try {
                        context.put("date", DateUtil.getDateLongMD(new Date()));
                    } catch (Exception e) {
                        logger.error("生成充值成功短信失败", e);
                        //e.printStackTrace();
                    }
                    String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_RECHARGE_SUCCESS_VM, context);
                    smsService.sendMsg(user.getMobileNo(), content);
                }
            } catch (Exception e) {
                logger.error("发送充值成功短信失败", e);
                //e.printStackTrace();
            }
        } else if (rechargeResult.equals(RechargeStatus.FAILE.getValue())) {
            rechargeOrder.setStatus(RechargeStatus.FAILE.getValue());
            this.myBatisDao.update("RECHARGE_ORDER.updateByPrimaryKeySelective", rechargeOrder);
        }

        cgBizService.flowRecord();

        return rechargeOrder;
    }

    @Override
    public RechargeOrder getRechargeOrderByOrderCode(String rechargeOrderCode, boolean lock) {
        if (!lock)
            return myBatisDao.get("RECHARGE_ORDER.getRechargeOrderByOrderCode", rechargeOrderCode);

        return myBatisDao.get("RECHARGE_ORDER.getAndLockRechargeOrderByOrderCode", rechargeOrderCode);
    }

    @Override
    public RechargeOrder getRechargeOrderByOrderCodeChannel(Map<String, String> params, boolean lock) {
        if (!lock)
            return myBatisDao.get("RECHARGE_ORDER.getRechargeOrderByOrderCodeChannel", params);

        return myBatisDao.get("RECHARGE_ORDER.getAndLockRechargeOrderByOrderCodeChannel", params);
    }

    @Override
    public void updateRecharge(RechargeOrder rechargeOrder) {
        this.myBatisDao.update("RECHARGE_ORDER.updateByPrimaryKeySelective", rechargeOrder);
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public void handleCallbackNotification(Map<String, String> result) {
        String rechargeOrderCode = result.get("orderid");
        String externalNo = result.get("yborderid");
        //确定支付结果
        String rechargeResult = getRechargeResult(result);

        RechargeOrderServiceImpl bean = ApplicationContextUtil.getBean(RechargeOrderServiceImpl.class);

        //更新充值订单状态，并记录现金流
        if (!rechargeResult.equals(RechargeStatus.UN_RECHARGE.getValue())) {
            RechargeOrder rechargeOrder = bean.confirmRecharge(
                    rechargeOrderCode, externalNo, rechargeResult);

            // 如果充值后需要走业务处理，择立刻处理
            if (rechargeOrder.getPayId() != null) {
                PayResult payResult = payService
                        .doPay(rechargeOrder.getPayId(),
                                rechargeOrder.getResultTime());
                // 根据执行结果跳转不同页面
                if (payResult.isPayResult() && payResult.isProcessResult()) {
                    LendOrder lendOrder = lendOrderService.getLendOrderByPayId(
                            payResult.getPayId(), false);
                    if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) {
                        // 生成省息计划回款列表和合同
                        financePlanProcessModule.beginCalcInterest(lendOrder, new Date());
                        // 匹配
                        try {
                            financePlanService.match(lendOrder);
                        } catch (Exception e) {
                            logger.error("匹配省心计划失败，失败原因：", e);
                        }
                    }
                }
            }

        }
    }

    private String getRechargeResult(Map<String, String> result) {
        String rechargeResult = RechargeStatus.UN_RECHARGE.getValue();
        if (result.get("errorcode") != null && !result.get("errorcode").equals("")) {//出现异常
            rechargeResult = RechargeStatus.FAILE.getValue();
        } else {//正常处理，但未确定结果
            if (result.get("status").equals("1")) {
                rechargeResult = RechargeStatus.SUCCESS.getValue();
            } else if (result.get("status").equals("0")) {
                rechargeResult = RechargeStatus.FAILE.getValue();
            }
        }
        return rechargeResult;
    }

    /**
     * 获取充值记录列表
     *
     * @param pageNo       页码
     * @param pageSize     条数
     * @param customParams 查询条件
     */
    @Override
    public Pagination<RechargeOrder> findAllRechargeOrderByPage(int pageNo, int pageSize, Map<String, Object> customParams) {
        Pagination<RechargeOrder> re = new Pagination<RechargeOrder>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        int totalCount = this.myBatisDao.count("findAllRechargeOrderByPage", customParams);
        List<RechargeOrder> uah = this.myBatisDao.getListForPaging("findAllRechargeOrderByPage", customParams, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
    }

    /**
     * 更改支付订单信息
     */
    @Override
    public RechargeOrder updateRechargeOrder(RechargeOrder rechargeOrder) {
        myBatisDao.update("RECHARGE_ORDER.updateByPrimaryKey", rechargeOrder);
        return rechargeOrder;
    }

    /**
     * 根据ID加载一条数据
     *
     * @param rechargeId 充值订单ID
     */
    @Override
    public RechargeOrder findRechargeOrderById(Long rechargeId) {
        return myBatisDao.get("RECHARGE_ORDER.selectByPrimaryKey", rechargeId);
    }

    @Override
    public List<RechargeOrder> findRechargeOrdersByCardId(Long customerCardId) {
        return myBatisDao.get("RECHARGE_ORDER.findRechargeOrdersByCardId", customerCardId);
    }

    @Override
    public RechargeOrder createRechargeOrderByAdminOperation(RechargeOrder rechargeOrder) {
        this.myBatisDao.insert("RECHARGE_ORDER.insertSelective", rechargeOrder);
        return rechargeOrder;
    }

    @Override
    public void exportExcel(HttpServletResponse response, Map<String, Object> params, HttpServletRequest req) {
        List<LinkedHashMap<String, Object>> list = this.myBatisDao.getList("RECHARGE_ORDER.exportExcel", params);
        try {
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            String fileName = "充值单列表";
            if (list.size() > 0 && list.size() > Integer.parseInt(PropertiesUtils.getInstance().get("EXCEL_SUM"))) {
                response.setContentType("application/x-msdownload;");
                response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
//				response.setHeader("Content-Length", String.valueOf(fileLength));
                ExcelUtil.createExcelBath(list, req, os, fileName, response);
            } else {
                response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".xls");// 设定输出文件头
                response.setContentType("application/msexcel");// 定义输出类型
                HSSFWorkbook wb = ExcelUtil.createExcel(list, "充值单列表");
                wb.write(os);
                os.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void exportExcelByLoanAppId(HttpServletResponse response, Long loanApplicationId) {
        List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
        list = this.myBatisDao.getList("RECHARGE_ORDER.exportExcelByLoanAppId", loanApplicationId);
        try {
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            String fileName = "投标明细";
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".xls");// 设定输出文件头
            response.setContentType("application/msexcel");// 定义输出类型
            HSSFWorkbook wb = ExcelUtil.createExcel(list, "投标明细");
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<RechargeOrder> findBy(RechargeOrder rechargeOrder) {
        return this.myBatisDao.getList("RECHARGE_ORDER.findBy", rechargeOrder);
    }

    @Override
    public BigDecimal getAllRechargeValueByUserId(Long userId) {
        return getAllRechargeValueByUserId(userId, "");
    }

    @Override
    public BigDecimal getAllRechargeValueByUserId(Long userId, String month) {
        if (userId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", "null");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(month)) {
            params.put("month", month);
        }
        return this.myBatisDao.get("RECHARGE_ORDER.getAllRechargeValueByUserId", params);
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public void handleCallbackPaymentOnlineNotification(Map<String, String> result) {
        String rechargeOrderCode = result.get("r6_Order");
        String externalNo = result.get("r2_TrxId");
        //确定支付结果
        String rechargeResult = getRechargeResultOnline(result);
        //
        RechargeOrderServiceImpl bean = ApplicationContextUtil.getBean(RechargeOrderServiceImpl.class);

        //更新充值订单状态，并记录现金流
        if (!rechargeResult.equals(RechargeStatus.UN_RECHARGE.getValue())) {
            RechargeOrder rechargeOrder = bean.confirmRecharge(rechargeOrderCode, externalNo, rechargeResult);

            //如果充值后需要走业务处理，择立刻处理
            if (rechargeOrder.getPayId() != null)
                payService.doPay(rechargeOrder.getPayId(), rechargeOrder.getResultTime());

        }
    }

    private String getRechargeResultOnline(Map<String, String> result) {
        String rechargeResult = RechargeStatus.UN_RECHARGE.getValue();
        String rCode = result.get("r1_Code");
        if (rCode == null || rCode.equals("")) {//出现异常
            rechargeResult = RechargeStatus.FAILE.getValue();
        } else {//正常处理，但未确定结果
            if (rCode.equals("1")) {
                rechargeResult = RechargeStatus.SUCCESS.getValue();
            } else {
                rechargeResult = RechargeStatus.FAILE.getValue();
            }
        }
        return rechargeResult;
    }

    @Override
    public String updateOrderStatus(RechargeOrder rechargeOrder) {
        Map<String, String> map = new HashMap<String, String>();
        if (rechargeOrder.getChannelCode() != null && rechargeOrder.getChannelCode().equals("YB_EBK")) {
            //处理易宝交易的订单
            if (rechargeOrder.getRechargeCode() == null) {
                return "不存在的充值单号";
            }
            QueryResult result = YeePayUtil.selectYeePayOrder(rechargeOrder.getRechargeCode());
            if (result == null) {
                return "订单接口查询失败";
            }
            if (result.getR1_Code().equals("50")) {
                map.put("status", "0");
                map.put("orderid", rechargeOrder.getRechargeCode());
                map.put("yborderid", rechargeOrder.getExternalNo());
                handleCallbackNotification(map);
                return "订单不存在";
            }
            if (result.getRb_PayStatus() != null && result.getRb_PayStatus().equals("SUCCESS")) {
                map.put("status", "1");
                map.put("orderid", rechargeOrder.getRechargeCode());
                map.put("yborderid", rechargeOrder.getExternalNo());
                // 处理支付结果回调
                handleCallbackNotification(map);
                return "success";
            } else {
                //处理未支付和已取消的订单
                long diff = new Date().getTime() - rechargeOrder.getCreateTime().getTime() - 24 * 3600 * 1000;
                if (diff > 0) {
                    map.put("status", "0");
                    map.put("orderid", rechargeOrder.getRechargeCode());
                    map.put("yborderid", rechargeOrder.getExternalNo());
                    handleCallbackNotification(map);
                }
            }
        } else if (rechargeOrder.getChannelCode().equals("LL_AUTHPAY") || rechargeOrder.getChannelCode().equals("LL_GATEPAY")) {

            Map<String, String> params = new HashMap<String, String>();
            params.put("no_order", rechargeOrder.getRechargeCode());
            params.put("dt_order", DateUtil.getFormattedDateUtil(rechargeOrder.getCreateTime(), "yyyyMMddHHmmss"));
            Map<String, String> respMap = LLPayUtil.orderQuery(params);
            if ("0000".equals(respMap.get("ret_code"))) {
                Map<String, String> handMap = new HashMap<String, String>();
                handMap.put("orderid", rechargeOrder.getRechargeCode());
                handMap.put("yborderid", respMap.get("oid_paybill"));
                if ("SUCCESS".equals(respMap.get("result_pay"))) {
                    PayDataBean payDataBean = new PayDataBean();
                    payDataBean.setNo_order(rechargeOrder.getRechargeCode());
                    if (respMap.get("pay_type").equals(LLPayTypeEnum.LLPAY_AUTH.getValue())) {
                        //认证支付--绑卡
                        customerCardService.bindCustomCard(payDataBean);
                        logger.info("绑卡流程结束......");
                    }
                    logger.info("绑卡流程结束......");
                    handMap.put("status", "1");
                    handleCallbackNotification(handMap);
                    logger.info("对账执行完成，充值结果成功！");
                } else if ("FAILURE".equals(respMap.get("result_pay"))) {
                    handMap.put("status", "0");
                    handleCallbackNotification(handMap);
                    logger.info("对账执行完成，充值结果失败！");
                } else {
                    logger.info("连连支付订单状态等待处理中......");
                }
            } else {
                logger.info("查询连连支付订单响应失败，失败原因:" + respMap.get("ret_message"));
            }

        } else {
            // 调用对账接口
            Map<String, String> result = TZTService.payapiQueryByOrderid(rechargeOrder.getRechargeCode().toString());

            // 状态判断
            String status = result.get("status");
            if (null != status && !"".equals(status)) {
                if (Integer.valueOf(status) == 1 || Integer.valueOf(status) == 0) {
                    // 处理支付结果回调
                    handleCallbackNotification(result);
                }
            }
        }

        return "success";
    }

    @Override
    public Map<String, String> handlePOSRechageNotification(String externalNo,
                                                            String amount, Long userId, String resource) {
        Map<String, String> rsMap = new HashMap<String, String>();
        BigDecimal amount_ = new BigDecimal(amount);
        amount_ = amount_.divide(new BigDecimal(100));
        //校验是否是已经通过人工处理的订单
        String rechargeResult = filterPOSMerIdSsn(externalNo);
        //查询POS流水号产生的充值订单，如果有成功的记录不再进行充值
        Map<String, String> param = new HashMap<String, String>();
        param.put("externalNo", externalNo);
        param.put("channelCode", RechargeChannelEnum.FUIOU_POS.getValue());
        RechargeOrder ro = getRechargeOrderByOrderCodeChannel(param, false);
        if (ro != null && ro.getStatus().equals(RechargeStatus.SUCCESS.getValue())) {
            rsMap.put("code", "0001");
            rsMap.put("rechargeCode", ro.getRechargeCode());
            return rsMap;
        } else if (ro == null) {
            ro = addRechargeOrderForFuiou(amount_, null, null,
                    userId, "FUIOU_POS", resource, externalNo);
        }
        RechargeOrderServiceImpl bean = ApplicationContextUtil
                .getBean(RechargeOrderServiceImpl.class);
        // 更新充值订单状态，并记录现金流
        RechargeOrder recharge = bean.confirmRecharge(ro.getRechargeCode(),
                externalNo, rechargeResult);
        if (rechargeResult.equals(RechargeStatus.FAILE.getValue())) {
            rsMap.put("code", "0002");
        } else {
            rsMap.put("code", "0000");
        }
        rsMap.put("rechargeCode", recharge.getRechargeCode());
        rsMap.put("rechargeResult", recharge.getStatus());
        return rsMap;
    }

    @Override
    public boolean isPOSssnLock(String externalNo) {
        long r = redisCacheManger.setRedisCacheInfonx(CommonRedisKeyEnum.POS_RECHARGE.getValue() + externalNo, "true", 60);
        if (r == 1) {
            return false;
        }
        return true;
    }

    @Override
    public void releasePOSssnLock(String externalNo) {
        redisCacheManger.destroyRedisCacheInfo(CommonRedisKeyEnum.POS_RECHARGE.getValue() + externalNo);
    }


    //过滤POS流水号  config.properties 配置
    private String filterPOSMerIdSsn(String externalNo) {
        String filterString = PropertiesUtils.getInstance().get("filterPOSssn");
        if (filterString.indexOf(externalNo) != -1) {
            return RechargeStatus.FAILE.getValue();
        } else {
            return RechargeStatus.SUCCESS.getValue();
        }
    }

    @Override
    @Transactional
    public RechargeOrder addRechargeOrderForFuiou(BigDecimal amount, Long bankCardId,
                                                  Long payId, Long userId, String channelCode, String resource,
                                                  String externalNo) {
        RechargeOrder rechargeOrder = new RechargeOrder();
        CustomerCard bankCard = null;
        if (bankCardId != null)
            bankCard = customerCardService.findById(bankCardId);
        rechargeOrder.setAmount(amount);
        rechargeOrder.setBankCode(bankCard != null ? bankCard.getBankCode()
                .toString() : null);
        rechargeOrder.setCardNo(bankCard != null ? bankCard.getCardCode()
                : null);
        rechargeOrder.setChannelCode(channelCode);
        rechargeOrder.setCreateTime(new Date());
        rechargeOrder.setCustomerCardId(bankCardId);
        rechargeOrder.setRechargeCode(UUID.randomUUID().toString().replace("-", ""));
        rechargeOrder.setStatus(RechargeStatus.UN_RECHARGE.getValue());
        rechargeOrder.setUserId(userId);
        rechargeOrder.setExternalNo(externalNo);

        this.myBatisDao.insert("RECHARGE_ORDER.insertSelective", rechargeOrder);
        // 记录订单来源
        OrderResource or = orderResourceService.selectBYDesc(resource);
        orderResourceService.addResourceFrom(rechargeOrder.getRechargeId(),
                Long.parseLong(OrderResourceEnum.MAPPING_TYPE_RECHARGE
                        .getValue()), or.getResourceId(), rechargeOrder.getCreateTime());
        return rechargeOrder;
    }

    @Override
    @Transactional
    public LLPayRequest createPayRequest(BigDecimal accountPayValue, BigDecimal rechargePayValue, LendOrder lendOrder, UserInfo currentUser,
                                         UserInfoExt userInfoExt, List<VoucherVO> voucherVOs, BigDecimal voucherPayValue, CustomerCard customerCard, ClientEnum client, RateUser rateUser, RateProduct rateProduct) {

        LLPayRequest llr = new LLPayRequest();

        // 创建支付订单
        PayOrder payOrder = payService.createPayOrder(accountPayValue, rechargePayValue, voucherPayValue, currentUser, lendOrder);
        // 支付单明细和财富券保存关系
        voucherService.linkVoucher(payOrder, voucherVOs);
        // 创建加息券和订单关联表
        if (null != rateUser && null != rateProduct) {
            RateLendOrder existsRatelendOrder = rateLendOrderService.findByLendOrderId(lendOrder.getLendOrderId(), RateLendOrderTypeEnum.RATE_COUPON.getValue(), RateLendOrderStatusEnum.UN_VALID.getValue());
            if (null == existsRatelendOrder) {
                List<LendOrderBidDetail> details = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId(), LendOrderBidStatusEnum.WAITING_PAY);
                Long loanApplicationId = details.get(0).getLoanApplicationId();
                rateLendOrderService.createRateLendOrder(rateUser, lendOrder.getLendOrderId(), loanApplicationId, RateLendOrderTypeEnum.RATE_COUPON, RateLendOrderPointEnum.CYCLE_RAPAYMENT.getValue(), rateProduct.getRateValue(),
                        RateLendOrderStatusEnum.UN_VALID);
            }
        }
        // 创建充值订单(卡信息先不入库)
        RechargeOrder rechargeOrder = addRechargeOrderForLL(rechargePayValue, customerCard, payOrder.getPayId(), currentUser.getUserId(),
                "LL_AUTHPAY", PropertiesUtils.getInstance().get(client.getResource()));

        // 确认支付请求
        LendProduct lendProduct = lendProductService.findById(lendOrder.getLendProductId());
        String url = "";
        try {
            if (client.getValue().equals(ClientEnum.WEB_CLIENT.getValue())) {
                url = LLPayUtil.llPay(lendOrder, rechargeOrder, lendProduct.getProductName(), userInfoExt, customerCard);
            } else if (client.getValue().equals(ClientEnum.WAP_CLIENT.getValue())) {
                url = LLPayUtil.llPayWap(rechargeOrder, lendProduct.getProductName(), userInfoExt, customerCard);
            }
            logger.info("发起连连支付请求url：" + url);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            String clientStr = client.getDesc();
            logger.error("客户端:" + clientStr + "userId:" + rechargeOrder.getUserId() + "充值单号：" + rechargeOrder.getRechargeCode() + "充值金额："
                    + rechargeOrder.getAmount() + "发起连连支付请求失败", e);
            throw new SystemException(PayErrorCode.NOT_SC_STATE).set("客户端:" + clientStr + "userId:" + rechargeOrder.getUserId() + "充值单号："
                    + rechargeOrder.getRechargeCode() + "充值金额：" + rechargeOrder.getAmount(), "发起连连支付请求失败");
        }
        llr.setRechargeOrder(rechargeOrder);
        llr.setUrl(url);
        return llr;
    }

    @Override
    @Transactional
    public void createHFPayRequest(BigDecimal accountPayValue, BigDecimal rechargePayValue, LendOrder lendOrder, UserInfo currentUser,
                                   List<VoucherVO> voucherVOs, BigDecimal voucherPayValue, CustomerCard customerCard, ClientEnum client,
                                   RateUser rateUser, RateProduct rateProduct,RechargeChannelEnum rechargeChannelEnum,String businessCode) {

        ParaContext paraContext = new ParaContext();
        Date now = new Date();
        Pair<String, BigDecimal> amountDetail = new Pair<String, BigDecimal>(PayConstants.AmountType.ACCOUNT.getValue(), accountPayValue);
        Pair<String, BigDecimal> voucherDetail = new Pair<String, BigDecimal>(PayConstants.AmountType.VOUCHERS.getValue(),voucherPayValue);
        Pair<String, BigDecimal> rechargeAmount = new Pair<String, BigDecimal>(PayConstants.AmountType.RECHARGE.getValue(), rechargePayValue);


        PayOrder payOrder = lendProductService.addPayOrderForBuyHF(currentUser.getUserId(), lendOrder, paraContext, now,voucherVOs,rateUser,rateProduct ,amountDetail, voucherDetail,rechargeAmount);

        RechargeOrder rechargeOrder = createHFRechargeRequest(rechargePayValue, currentUser, customerCard, rechargeChannelEnum, ClientEnum.WEB_CLIENT, payOrder.getPayId(), businessCode);

        //冻结购买金额
        if(accountPayValue.compareTo(BigDecimal.ZERO) > 0){
            UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
            AccountValueChangedQueue queue = new AccountValueChangedQueue();
            AccountValueChanged changed = new AccountValueChanged(cashAccount.getAccId(),accountPayValue, accountPayValue,
                    AccountConstants.AccountOperateEnum.FREEZE.getValue(),
                    PayConstants.BusTypeEnum.cValueOf(payOrder.getBusType()).getAccFrozenBusTypeEnum().getValue(),
                    AccountConstants.AccountChangedTypeEnum.PAYORDER.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(),
                    payOrder.getPayId(), AccountConstants.OwnerTypeEnum.USER.getValue(), payOrder.getUserId(), new Date(),
                    StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.FREEZE_FOR_BUS_HANDLING,
                            PayConstants.BusTypeEnum.cValueOf(payOrder.getBusType()).getAccFrozenBusTypeEnum().getDesc(),
                            payOrder.getAmount().subtract(voucherPayValue)), true);
            queue.addAccountValueChanged(changed);
            userAccountOperateService.execute(queue);
            Map map = new HashMap();
            map.put("businessId",payOrder.getPayId());
            map.put("businessType",AccountConstants.BusinessTypeEnum.FEESTYPE_TOBUYFREEZEN.getValue());
            Schedule sch = scheduleService.findByCondition(map).get(0);
            CapitalFlow cap=new CapitalFlow();
            cap.setScheduleId(sch.getScheduleId());
            cap.setOperationType(Integer.parseInt(HFOperationEnum.FROZEN_PERSON_TO_PERSON.getValue()));
            cap.setFromUser(currentUser.getUserId());
            cap.setAmount(accountPayValue);
            cap.setStartTime(new Date());
            cap.setBusinessId(sch.getBusinessId());
            cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
            cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
            capitalFlowService.addCapital(cap);
        }

    }

    @Override
    public LLPayRequest createRechargeRequest(BigDecimal rechargeAmount, UserInfo currentUser, UserInfoExt userInfoExt, CustomerCard card, ClientEnum client) {
        LLPayRequest llr = new LLPayRequest();

        // 创建充值订单
        RechargeOrder rechargeOrder = addRechargeOrderForLL(rechargeAmount, card, null, currentUser.getUserId(), "LL_AUTHPAY", PropertiesUtils.getInstance().get(client.getResource()));

        // 确认支付请求
        LendProduct lendProduct = new LendProduct();
        lendProduct.setProductName("财富派充值");

        String url = "";
        try {
            if (client.getValue().equals(ClientEnum.WEB_CLIENT.getValue())) {
                url = LLPayUtil.llPay(null, rechargeOrder, lendProduct.getProductName(), userInfoExt, card);
            } else if (client.getValue().equals(ClientEnum.WAP_CLIENT.getValue())) {
                url = LLPayUtil.llPayWap(rechargeOrder, lendProduct.getProductName(), userInfoExt, card);
            }
            logger.info("发起连连充值请求url：" + url);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            String clientStr = client.getDesc();
            logger.error("客户端:" + clientStr + "userId:" + rechargeOrder.getUserId() + "充值单号：" + rechargeOrder.getRechargeCode() + "充值金额："
                    + rechargeOrder.getAmount() + "发起连连支付请求失败", e);
            throw new SystemException(PayErrorCode.NOT_SC_STATE).set("客户端:" + clientStr + "userId:" + rechargeOrder.getUserId() + "充值单号："
                    + rechargeOrder.getRechargeCode() + "充值金额：" + rechargeOrder.getAmount(), "发起连连支付请求失败");
        }
        llr.setRechargeOrder(rechargeOrder);
        llr.setUrl(url);
        return llr;
    }

    @Override
    public RechargeOrder createHFRechargeRequest(BigDecimal rechargeAmount, UserInfo currentUser,CustomerCard card,RechargeChannelEnum rechargeChannelEnum , ClientEnum client,Long payId,String businessCode) {
        // 创建充值订单
        RechargeOrder rechargeOrder = addRechargeOrderForHF(rechargeAmount, card, payId, currentUser.getUserId(), rechargeChannelEnum.getValue(), PropertiesUtils.getInstance().get(client.getResource()),businessCode);
        Schedule sch=new Schedule();
        sch.setBusinessId(rechargeOrder.getRechargeId());
        sch.setBusinessType(Integer.parseInt(AccountConstants.BusinessTypeEnum.FEESTYPE_RECHARGE.getValue()));
        sch.setDesc(client.getDesc()+AccountConstants.BusinessTypeEnum.FEESTYPE_RECHARGE.getDesc()+rechargeAmount);
        sch.setStartTime(new Date());
        sch.setStatus(Integer.parseInt(ScheduleEnum.BUSINESS_PREPARE.getValue()));
        scheduleService.addSchedule(sch);
        CapitalFlow cap=new CapitalFlow();
        cap.setScheduleId(sch.getScheduleId());
        cap.setOperationType(Integer.parseInt(HFOperationEnum.RECHARGE.getValue()));
        cap.setToUser(currentUser.getUserId());
        cap.setAmount(rechargeAmount);
        cap.setStartTime(new Date());
        cap.setBusinessId(sch.getBusinessId());
        cap.setBusinessFlow(businessCode);
        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
        capitalFlowService.addCapital(cap);
        return rechargeOrder;
    }
    @Override
    public LLPayRequest createGatewayRechargeRequest(BigDecimal rechargeAmount, String bankCode, UserInfo currentUser, UserInfoExt userInfoExt, ClientEnum client) {
        LLPayRequest llr = new LLPayRequest();
        String url = null;
        // 创建充值订单
        RechargeOrder rechargeOrder = addRechargeOrderForLL(rechargeAmount, null, null, currentUser.getUserId(), "LL_GATEPAY", PropertiesUtils.getInstance().get(client.getResource()));
        // 确认支付请求
        try {
            url = LLPayUtil.llGateWayPay(null, rechargeOrder, bankCode, "财富派充值", userInfoExt);
        } catch (Exception e) {
            String clientStr = client.getDesc();
            logger.error("客户端:" + clientStr + "userId:" + rechargeOrder.getUserId() + "充值单号：" + rechargeOrder.getRechargeCode() + "充值金额："
                    + rechargeOrder.getAmount() + "发起连连网关支付请求失败", e);
            throw new SystemException(PayErrorCode.NOT_SC_STATE).set("客户端:" + clientStr + "userId:" + rechargeOrder.getUserId() + "充值单号："
                    + rechargeOrder.getRechargeCode() + "充值金额：" + rechargeOrder.getAmount(), "发起连连网关支付请求失败");
        }

        llr.setRechargeOrder(rechargeOrder);
        llr.setUrl(url);
        return llr;
    }

    @Override
    @Transactional
    public LLPayRequest createPayGatewayRequest(BigDecimal accountPayValue, BigDecimal rechargePayValue, LendOrder lendOrder, UserInfo currentUser,
                                                UserInfoExt userInfoExt, List<VoucherVO> voucherVOs, BigDecimal voucherPayValue, ClientEnum client, String bankCode, RateUser rateUser, RateProduct rateProduct) {

        LLPayRequest llr = new LLPayRequest();

        // 创建支付订单
        PayOrder payOrder = payService.createPayOrder(accountPayValue, rechargePayValue, voucherPayValue, currentUser, lendOrder);
        // 支付单明细和财富券保存关系
        voucherService.linkVoucher(payOrder, voucherVOs);
        // 创建加息券和订单关联表
        if (null != rateUser && null != rateProduct) {
            RateLendOrder existsRatelendOrder = rateLendOrderService.findByLendOrderId(lendOrder.getLendOrderId(), RateLendOrderTypeEnum.RATE_COUPON.getValue(), RateLendOrderStatusEnum.UN_VALID.getValue());
            if (null == existsRatelendOrder) {
                List<LendOrderBidDetail> details = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId(), LendOrderBidStatusEnum.WAITING_PAY);
                Long loanApplicationId = details.get(0).getLoanApplicationId();
                rateLendOrderService.createRateLendOrder(rateUser, lendOrder.getLendOrderId(), loanApplicationId, RateLendOrderTypeEnum.RATE_COUPON, RateLendOrderPointEnum.CYCLE_RAPAYMENT.getValue(), rateProduct.getRateValue(),
                        RateLendOrderStatusEnum.UN_VALID);
            }
        }
        // 创建充值订单
        RechargeOrder rechargeOrder = addRechargeOrderForLL(rechargePayValue, null, payOrder.getPayId(), currentUser.getUserId(), "LL_GATEPAY", PropertiesUtils.getInstance().get(client.getResource()));

        // 确认支付请求
        LendProduct lendProduct = lendProductService.findById(lendOrder.getLendProductId());
        String url = null;
        try {
            url = LLPayUtil.llGateWayPay(lendOrder, rechargeOrder, bankCode, lendProduct.getProductName(), userInfoExt);
        } catch (Exception e) {
            String clientStr = client.getDesc();
            logger.error("客户端:" + clientStr + "userId:" + rechargeOrder.getUserId() + "充值单号：" + rechargeOrder.getRechargeCode() + "充值金额："
                    + rechargeOrder.getAmount() + "发起连连网关支付请求失败", e);
            throw new SystemException(PayErrorCode.NOT_SC_STATE).set("客户端:" + clientStr + "userId:" + rechargeOrder.getUserId() + "充值单号："
                    + rechargeOrder.getRechargeCode() + "充值金额：" + rechargeOrder.getAmount(), "发起连连网关支付请求失败");
        }
        llr.setRechargeOrder(rechargeOrder);
        llr.setUrl(url);
        return llr;
    }
}
