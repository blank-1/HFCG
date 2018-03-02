package com.xt.cfp.web.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.OpenAccount4PCPersonalDataSource;
import com.external.deposites.model.datasource.OpenAccount4ApiPersonalDataSource;
import com.external.deposites.model.fydatasource.LegalPersonDataSource;
import com.external.deposites.model.response.CommonOpenAccount4ApiResponse;
import com.external.deposites.model.response.OpenAccount4PCEnterpriseResponse;
import com.external.deposites.model.response.OpenAccount4PCPersonResponse;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.*;
import com.xt.cfp.core.service.*;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.external.llpay.BankInfo;
import com.external.llpay.LLPayUtil;
import com.external.yeepay.TZTService;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.service.financePlan.FinancePlanProcessModule;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.util.TemplateUtil;

@Controller
@RequestMapping("/jsp/custom/customer")
public class CustomerController extends BaseController {

    private static Logger logger = Logger.getLogger(CustomerController.class);

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private RightsRepaymentDetailService rightsRepaymentDetailService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private CityInfoService cityInfoService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private BondSourceService bondSourceService;
    @Autowired
    private LendOrderReceiveService lendOrderReceiveService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private LoanFeesDetailService loanFeesDetailService;
    @Autowired
    private DefaultInterestDetailService defaultInterestDetailService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private IDCardVerifiedService idCardVerifiedService;
    @Autowired
    private LendProductService lendProductService;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private LendLoanBindingService lendLoanBindingService;
    @Autowired
    private FeesItemService feesItemService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    @Autowired
    private IhfApi hfApi;
    @Autowired
    private TradeService tradeService;

    /**
     * 个人自助开户
     */
    @ResponseBody
    @RequestMapping(value = "personalOpenAccountAsync", params = {"mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code",
            "mobile_no", "cust_nm", "certif_id", "city_id", "parent_bank_id", "capAcntNo"})
    public Object personalOpenAccountAsync(OpenAccount4PCPersonResponse response) {
        Map<String, Object> a = new HashMap<>(1);
        a.put("personalOpenAccount", response);
        System.out.println(this.getRequest().getParameterMap());
        if (response.isSuccess()) {
            CustomerCard customerCard = customerCardService.findById(Long.parseLong(response.getUser_id_from()));
            customerCard.setBindStatus(CustomerCardBindStatus.BINDED.getValue());
            customerCardService.saveOrUpdateCustomerCard(customerCard);
            UserInfoExt infoExt = userInfoExtService.getUserInfoExtById(customerCard.getUserId());
            infoExt.setIsVerified(UserIsVerifiedEnum.BIND.getValue());
            userInfoExtService.updateUserInfoExt(infoExt);
            getTradeUpdate(response);
        }

        return a;
    }

    /**
     * 个人自助开户
     */
    @ResponseBody
    @RequestMapping(value = "personalOpenAccount", params = {"mchnt_cd", "mchnt_txn_ssn", "signature", "resp_code",
            "mobile_no", "cust_nm", "certif_id", "city_id", "parent_bank_id", "capAcntNo"})
    public Object personalOpenAccount(OpenAccount4PCPersonResponse response) {
        Map<String, Object> a = new HashMap<>(1);
        System.out.println(this.getRequest().getParameterMap());
        if (response.isSuccess()) {
            a.put("开户成功", "");
            CustomerCard customerCard = customerCardService.findById(Long.parseLong(response.getUser_id_from()));
            customerCard.setBindStatus(CustomerCardBindStatus.BINDED.getValue());
            customerCardService.saveOrUpdateCustomerCard(customerCard);
            UserInfoExt infoExt = userInfoExtService.getUserInfoExtById(customerCard.getUserId());
            infoExt.setIsVerified(UserIsVerifiedEnum.BIND.getValue());
            userInfoExtService.updateUserInfoExt(infoExt);
            getTradeUpdate(response);
        }else {
            a.put("开户不成功", "请联系网站管理员");
        }

        return a;
    }

    @ResponseBody
    @RequestMapping(value = "signAccount")
    public Object signAccount(String mobile, String userId, String cardNo, String realName, String idCard, Model model) {
        OpenAccount4PCPersonalDataSource dataSource = new OpenAccount4PCPersonalDataSource();
        try {
            dataSource.setUser_id_from(userId);
            dataSource.setCertif_tp(HfUtils.CertifTpEnum.ID_CARD.code());
            dataSource.setCertif_id(idCard);
            dataSource.setMobile_no(mobile);
            dataSource.setCust_nm(realName);
            dataSource.setCapAcntNo(cardNo);
            dataSource.setBack_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.open_account.pc.person.self.back_notify_url_admin"));
            dataSource.setPage_notify_url(PropertiesUtils.property("hf-config", "cg.hf.api.open_account.pc.person.self.page_notify_url_admin"));
            dataSource = hfApi.openAccountBySelf(dataSource, false);
            addTrade(dataSource);

        } catch (final HfApiException e) {
            logger.error(e.getMessage(), e);
        }
        return dataSource;
    }

    /**
     * 跳转到客户列表页
     *
     * @return
     */
    @RequestMapping(value = "/customerInfoList")
    public ModelAndView customerInfoList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/custom/customer/customer_list");
        return mv;
    }

    // 列表页数据加载
    @RequestMapping(value = "/customerPage", method = RequestMethod.POST)
    @ResponseBody
    public Object customerList(HttpServletRequest request, @ModelAttribute("userVO") UserInfoVO userVO) {
        int pageSize = Integer.parseInt(request.getParameter("rows"));
        int pageNo = Integer.parseInt(request.getParameter("page"));
        Pagination<UserInfoVO> customers = userInfoService.getUserExtPaging(pageNo, pageSize, userVO);
        return customers;
    }

    /**
     * 修改用户状态
     *
     * @param userId
     * @param status
     * @return
     */
    @RequestMapping(value = "/toEditCustomer")
    @ResponseBody
    public String toEditCustomer(@RequestParam("userId") long userId, @RequestParam("status") String status) {
        if (!StringUtils.isNull(status) && !StringUtils.isNull(String.valueOf(userId))) {
            userInfoService.changeUserStatus(userId, status.equals(UserStatus.NORMAL.getValue()) ? UserStatus.NORMAL : UserStatus.FREEZE);
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 查看出借产品详情
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/toViewCustomer")
    public ModelAndView toViewCustomer(@RequestParam("userId") Long userId) {
        UserInfoVO user = userInfoService.getUserExtByUserId(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("jsp/custom/customer/customer_account_info");
        return modelAndView;
    }

    /**
     * 账户总览
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "showAccount")
    public String showAccount(HttpServletRequest req, Long userId) {
        // todo 数据为查询
        // 可用金额 冻结金额
        UserAccount userAccount = userAccountService.getCashAccount(userId);
        // 累计出借金额
        BigDecimal buyBalance = lendOrderService.getTotalLendAmount(userId);
        // 累计收益
        BigDecimal profit = lendOrderService.getTotalProfit(userId);
        // 累计借款
        BigDecimal loanBalance = loanApplicationService.getTotalLoanAmount(userId);

        //待回本金（投标）
        BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(null, userId, LendProductTypeEnum.RIGHTING, LendProductTypeEnum.CREDITOR_RIGHTS);
        req.setAttribute("capitalRecive", capitalRecive);
        //待回收益（投标）
        BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(null, userId, LendProductTypeEnum.RIGHTING, LendProductTypeEnum.CREDITOR_RIGHTS);
        req.setAttribute("interestRecive", interestRecive);
        //持有全部省心计划
        BigDecimal totalHoldFinancePlan = lendOrderService.getTotalHoldFinancePlan(userId);
        req.setAttribute("totalHoldFinancePlan", totalHoldFinancePlan);
        //待还本金
        BigDecimal replaymentCapital = repaymentPlanService.getRepaymentCapitalByUserId(userId);
        req.setAttribute("replaymentCapital", replaymentCapital);
        //待还利息
        BigDecimal replaymentInterest = repaymentPlanService.getRepaymentInterestByUserId(userId);
        req.setAttribute("replaymentInterest", replaymentInterest);
        //代缴费用
        BigDecimal loanFeeNopaied = loanFeesDetailService.getUserLoanFeeNoPaied(userId);
        req.setAttribute("loanFeeNopaied", loanFeeNopaied);
        //待还罚息
        BigDecimal interestPaid = defaultInterestDetailService.getDefaultInterestByUserId(userId);
        req.setAttribute("interestPaid", interestPaid);

        //账户总资产
        BigDecimal sumMoney = BigDecimalUtil.down(this.getNetAsset(userId), 2);

        //财富券
        BigDecimal voucherValue = voucherService.getAllVoucherValue(userId);
        req.setAttribute("sumMoney", sumMoney);
        req.setAttribute("voucherValue", voucherValue);
        req.setAttribute("userAccount", userAccount);
        req.setAttribute("buyBalance", buyBalance);
        req.setAttribute("profit", profit);
        req.setAttribute("loanBalance", loanBalance);
        return "jsp/custom/customer/customer_account_detail";
    }

    /**
     * 获取用户净资产
     *
     * @param currentUser
     * @return
     */
    private BigDecimal getNetAsset(Long userId) {
        //净资产=账户余额+（待回本金+待回利息+持有省心计划）-（待还本金+待还利息+待缴费用+待还罚息）
        UserAccount cashAccount = userAccountService.getCashAccount(userId);
        //待回本金（投标）
        BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(null, userId, LendProductTypeEnum.RIGHTING, LendProductTypeEnum.CREDITOR_RIGHTS);
        //待回收益（投标）
        BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(null, userId, LendProductTypeEnum.RIGHTING, LendProductTypeEnum.CREDITOR_RIGHTS);
        //持有全部省心计划
        BigDecimal totalHoldFinancePlan = BigDecimal.ZERO;
        List<LendOrder> financeOrderList = lendOrderService.getFinancialPlanListByUserId(userId);
        BigDecimal financeAccountTurnValue = userAccountService.getFinanceAccountPayValue(userId);
        //用户省心计划已获但未回账户的奖励
        BigDecimal totalFinanceAward = BigDecimal.ZERO;
        for (LendOrder financeOrder : financeOrderList) {
            totalHoldFinancePlan = totalHoldFinancePlan.add(financeOrder.getBuyBalance()).add(financeOrder.getCurrentProfit());
            totalFinanceAward = userAccountService.getUserTotalFinanceAwardByLendOrderId(financeOrder.getLendOrderId());
        }
        //获取用户所有省心账户-计算省心账户子标所有扣费
        BigDecimal financeFees = financePlanProcessModule.getAllFeesByUserId(userId);
        //用户所有省心计划获取的奖励
        totalHoldFinancePlan = totalHoldFinancePlan.add(totalFinanceAward);
        totalHoldFinancePlan = totalHoldFinancePlan.subtract(financeAccountTurnValue).subtract(financeFees);
        //省心计划带回款利息
        BigDecimal waitInterest = lendOrderService.getFinancialWaitInterestByUserId(userId);
        totalHoldFinancePlan = totalHoldFinancePlan.add(waitInterest);
        //待还本金
        BigDecimal replaymentCapital = repaymentPlanService.getRepaymentCapitalByUserId(userId);
        //待还利息
        BigDecimal replaymentInterest = repaymentPlanService.getRepaymentInterestByUserId(userId);
        //待缴费用
        BigDecimal loanFeeNopaied = loanFeesDetailService.getLoanFeeNoPaied(userId);
        //待还罚息
        BigDecimal interestPaid = defaultInterestDetailService.getDefaultInterestByUserId(userId);

        return cashAccount.getValue2().add(totalHoldFinancePlan).add(capitalRecive).add(interestRecive).subtract(replaymentCapital).subtract(replaymentInterest).subtract(loanFeeNopaied).subtract(interestPaid);
    }

    /**
     * 借款总览
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "showLoan")
    public String showLoan(HttpServletRequest request, Long userId) {
        request.setAttribute("userId", userId);
        return "jsp/custom/customer/customer_loan_detail";
    }

    /**
     * 客户借款list
     *
     * @param request
     * @param loanApplication
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/getLoanList")
    @ResponseBody
    public Pagination<LoanApplicationExtOne> getLoanList(HttpServletRequest request, LoanApplication loanApplication, String startDate, String endDate) {
        int pageSize = Integer.parseInt(request.getParameter("rows"));
        int pageNo = Integer.parseInt(request.getParameter("page"));
        return loanApplicationService.getAllLoanApplicationList(pageNo, pageSize, loanApplication, startDate, endDate);
    }

    /**
     * to还款明细
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "showRepayment")
    public String showRepayment(HttpServletRequest request, Long userId) {
        request.setAttribute("userId", userId);
        return "jsp/custom/customer/customer_repayment_list";
    }

    /**
     * 查询还款明细list
     *
     * @param request
     * @param userId
     * @param channelCode
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/getRepaymentList")
    @ResponseBody
    public Pagination<RepaymentVO> getRepaymentList(HttpServletRequest request, String userId, String channelCode, String startDate,
                                                    String endDate) {
        int pageSize = Integer.parseInt(request.getParameter("rows"));
        int pageNo = Integer.parseInt(request.getParameter("page"));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("channelCode", channelCode);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return repaymentPlanService.getRepaymentList(pageNo, pageSize, params);
    }

    /**
     * to还款明细detail
     *
     * @param request
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "toRepaymentDetail")
    public String toRepaymentDetail(HttpServletRequest request, Long loanApplicationId) {
        request.setAttribute("loanApplicationId", loanApplicationId);
        return "jsp/custom/customer/toRepaymentDetail";
    }

    /**
     * 查询还款明细不同期数list
     *
     * @param request
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "/getRepaymentDetailList")
    @ResponseBody
    public Pagination<RepaymentVO> getRepaymentDetailList(HttpServletRequest request, Long loanApplicationId) {
        int pageSize = Integer.parseInt(request.getParameter("rows"));
        int pageNo = Integer.parseInt(request.getParameter("page"));
        return repaymentPlanService.getRepaymentDetailListByLoanAppId(pageNo, pageSize, loanApplicationId);
    }

    /**
     * 出借总览
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "showLend")
    public String showLend(HttpServletRequest request, Long userId) {
        request.setAttribute("userId", userId);
        return "jsp/custom/customer/customer_lend_detail";
    }

    /**
     * 加载出借列表datagrid数据
     *
     * @param request
     * @param userId
     * @param orderState
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/getLendList")
    @ResponseBody
    public Pagination<LendAndLoanVO> getLendList(HttpServletRequest request, String userId, String orderState, String startDate, String endDate) {
        int pageSize = Integer.parseInt(request.getParameter("rows"));
        int pageNo = Integer.parseInt(request.getParameter("page"));
        Map<String, Object> params = new HashMap<String, Object>();
        if (!"-1".equals(orderState)) {
            params.put("type", orderState);
        }
        params.put("userId", userId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return lendProductService.findLendListByUserId(pageNo, pageSize, params);
    }

    /**
     * 加载出借列表下拉详情
     *
     * @param creditorRightsId
     * @param lendOrderId
     * @param loanProductId
     * @param lendUserId
     * @return
     */
    @RequestMapping(value = "/getLendDetail")
    @ResponseBody
    public List<RightsRepaymentDetail> getLendDetail(Long creditorRightsId, Long lendOrderId, Long loanProductId, Long lendUserId) {
        List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRightsId);
        // 获得出借产品所有费用项
        List<LendProductFeesItem> feeitems = lendProductService.findAllProductFeesItemsByLendOrderId(lendOrderId);
        // 需要计算回款时的费用项
        List<LendProductFeesItem> atcycleFeeitems = new ArrayList<LendProductFeesItem>();
        for (LendProductFeesItem feesItem : feeitems) {
            if (FeesPointEnum.ATCYCLE.value2Char() == feesItem.getChargePoint()) {
                atcycleFeeitems.add(feesItem);
            }
        }
        // 获得费用
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        List<LendLoanBinding> byLendAndLoan = lendLoanBindingService.findByLendAndLoan(lendOrder.getLendProductId(), loanProductId);
        List<FeesItem> fis = new ArrayList<FeesItem>();
        if (byLendAndLoan != null) {
            for (LendLoanBinding llb : byLendAndLoan) {
                if (llb.getFeesItemId() != null && llb.getFeesItemId() > 0) {
                    fis.add(feesItemService.findById(llb.getFeesItemId()));
                }
            }
        }

        // 处理一下明细，添加待缴费用
        BigDecimal exceptProfit = lendOrderReceiveService.getExceptProfitByUserId(lendOrderId, lendUserId);
        for (RightsRepaymentDetail detail : detailRightsList) {
            // 计算费用
            BigDecimal fees = BigDecimal.ZERO;
            if (atcycleFeeitems != null && atcycleFeeitems.size() > 0) {
                for (LendProductFeesItem item : atcycleFeeitems) {
                    BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(item, BigDecimal.ZERO, BigDecimal.ZERO,
                            detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
                    fees = fees.add(fee);
                }
            }
            if (fis != null && fis.size() > 0) {
                for (FeesItem fi : fis) {
                    BigDecimal fee = feesItemService.calculateFeesBalance(fi.getFeesItemId(), BigDecimal.ZERO, BigDecimal.ZERO,
                            detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
                    fees = fees.add(fee);
                }
            }
            detail.setShouldFee(fees);
        }
        return detailRightsList;
    }

    /**
     * 加载出借总览数据（预期收益,最近回款日）
     *
     * @param creVo
     * @return
     */
    @RequestMapping(value = "/getLoanInfo")
    @ResponseBody
    public CreditorRightsExtVo getLoanInfo(CreditorRightsExtVo creVo) {
        BigDecimal expectProfit = lendOrderReceiveService.getExceptProfitByUserId(creVo.getLendOrderId(), creVo.getLendUserId());
        List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(creVo.getCreditorRightsId());
        //最近还款日期
        try {
            creVo.setCurrentPayDate(creditorRightsService.getRecentPayDate(detailRightsList));
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("最近还款日期计算出错", e);
        }
        creVo.setExpectProfit(expectProfit);
        return creVo;
    }

    /**
     * 显示银行卡信息
     *
     * @param req
     * @param userId
     * @return
     */
    @RequestMapping(value = "showBankCard")
    public String showBankCard(HttpServletRequest req, Long userId) {
        List<CustomerCard> cards = customerCardService.getCustomerCardsByUserId(userId);
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(userId);
        UserInfo userInfo = userInfoService.getUserByUserId(userInfoExt.getUserId());
        req.setAttribute("cards", cards);
        req.setAttribute("userId", userId);
        req.setAttribute("cardsize", cards.size());
        req.setAttribute("userInfoExt", userInfoExt);
        req.setAttribute("userInfo", userInfo);
        if (!cards.isEmpty())
            req.setAttribute("card", cards.get(cards.size() - 1));
        else
            req.setAttribute("card", new CustomerCard());
        return "jsp/custom/customer/customer_card_list";
    }

    /**
     * 恒丰存管修改银行卡信息
     *
     * @param req
     * @param cardId
     * @return
     */
    @RequestMapping(value = "showBankCardforCarId")
    public String showBankCardforCarId(HttpServletRequest req, Long cardId) {
        CustomerCardVO customerCard = customerCardService.getCustomerCardVOById(cardId);
        UserInfoExt userInfoExtById = userInfoExtService.getUserInfoExtById(customerCard.getUserId());
        req.setAttribute("card", customerCard);
        req.setAttribute("user", userInfoExtById);
        return "jsp/custom/customer/customer_card_edit";
    }


    /**
     * 用户银行卡解绑操作
     *
     * @param request
     * @param customerCardId 卡ID
     * @return
     */
    @RequestMapping(value = "unbundling")
    @ResponseBody
    public Object unbundling(HttpServletRequest request, Long customerCardId) {
        try {
            if (null == customerCardId) {
                return returnResultMap(false, null, "check", "缺少参数！");
            }
            CustomerCard card = customerCardService.findById(customerCardId);
            if (null == card) {
                return returnResultMap(false, null, "check", "参数异常，获取数据失败！");
            }
            //更改银行卡信息
            card.setStatus(CustomerCardStatus.DISABLED.getValue());
            card.setBindStatus(CustomerCardBindStatus.UNBINDING.getValue());
            card.setUpdateTime(new Date());
            customerCardService.saveOrUpdateCustomerCard(card);
            // 发送短信
//			UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(card.getUserId());
//			this.sendBankCardUnbundlingMsg(userInfoExt.getMobileNo(), card.getEncryptCardNo());
        } catch (SystemException se) {
            logger.error(se.getDetailDesc(), se);
            se.printStackTrace();
            return returnResultMap(false, null, null, se.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 后台银行卡解绑给用户发送短信通知
     *
     * @param mobileNo      手机号码
     * @param encryptCardNo 银行卡后四位
     */
    private void sendBankCardUnbundlingMsg(String mobileNo, String encryptCardNo) {
        VelocityContext context = new VelocityContext();
        try {
            try {
                context.put("date", DateUtil.getDateLongMD(new Date()));
            } catch (Exception e) {
                logger.error("生成银行卡解绑短信失败", e);
            }
            context.put("card", encryptCardNo);
            String content = TemplateUtil.getStringFromTemplate(TemplateType.SMS_BANKCARDUNBUNDLING_VM, context);
            smsService.sendMsg(mobileNo, content);
        } catch (Exception e) {
            logger.error("发送银行卡解绑短信失败", e);
        }
    }

    /**
     * 绑定银行卡 信息
     *
     * @param req
     * @param customerCardId
     * @return
     */
    @RequestMapping(value = "toBindingCard")
    @ResponseBody
    public String toBindingCard(HttpServletRequest req, Long customerCardId) {
        ///////////////////////////一分钱充值
        CustomerCard card = customerCardService.findById(customerCardId);
        if (CustomerCardBindStatus.BINDED.getValue().equals(card.getBindStatus())) {
            return JsonView.JsonViewFactory.create().success(false).info("此卡已绑定！").toJson();
        }
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(card.getUserId());
        UserInfo user = userInfoService.getUserByUserId(userInfoExt.getUserId());
//		//发送请求
//		CommonOpenAccountResponse commonOpenAccountResponse = openAccountByLoan(card, userInfoExt);
//
        boolean flag = false;
        String str = "绑卡失败";
        try {
            //		if(commonOpenAccountResponse.isSuccess()){
            card.setBindStatus(CustomerCardBindStatus.BINDED.getValue());
            customerCardService.saveOrUpdateCustomerCard(card);
            flag = true;
            str = "绑卡成功";
//		}
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonView.JsonViewFactory.create().success(flag).info(str).put("rechargeCode", "").toJson();

    }

    /**
     * 绑定银行卡
     *
     * @param req
     * @param requestId
     * @return
     */
    @RequestMapping(value = "bindingCard")
    @ResponseBody
    public String bindingCard(HttpServletRequest req, Long customerCardId, String smscode, String requestId) {
        if (org.apache.commons.lang.StringUtils.isEmpty(smscode)) {
            return "短信验证码不能为空";
        }
        try {
            Map<String, String> map = new TreeMap<String, String>();
            map.put("requestid", requestId);
            map.put("validatecode", smscode);

            Map<String, String> result = TZTService.confirmBindBankcard(map);
            if (result.get("error_code") != null) {
                return result.get("error_msg");
            } else {
                CustomerCard card = customerCardService.findById(customerCardId);
                card.setBindStatus(CustomerCardBindStatus.BINDED.getValue());
                customerCardService.saveOrUpdateCustomerCard(card);
                return "success";
            }
        } catch (SystemException se) {
            logger.error(se.getDetailDesc(), se);
            se.printStackTrace();
            return se.getMessage();
        }
    }

    @RequestMapping(value = "bankCards")
    @ResponseBody
    public Object bankCards(HttpServletRequest req, Long userId) {
        List<CustomerCard> cards = customerCardService.getCustomerCardsByUserId(userId);
        return cards;
    }

    /**
     * 导出用户
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("userVO") UserInfoVO userVO) {
        //withDrawService.exportExcel(response);
        userInfoService.exportExcel(response, userVO);
    }

    /**
     * 获取省市信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getProvince")
    @ResponseBody
    public Object getProvince(HttpServletRequest request, HttpServletResponse response) {

        List<LianLianProvinceCity> lianLianProvinceCities = cityInfoService.getLianLianProvinceCityByPID(0L);
        List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
        Map<Object, Object> map;
        map = new HashMap<Object, Object>();
        map.put("PROVINCENAME", "请选择");
        map.put("PROVINCEID", "");
        map.put("selected", true);
        maps.add(map);
        if (null != lianLianProvinceCities && lianLianProvinceCities.size() > 0) {
            for (int i = 0; i < lianLianProvinceCities.size(); i++) {
                LianLianProvinceCity provinceInfo = lianLianProvinceCities.get(i);
                map = new HashMap<Object, Object>();
                map.put("PROVINCENAME", provinceInfo.getProvinceName());
                map.put("PROVINCEID", provinceInfo.getProvinceCityId());
                maps.add(map);
            }
        }
        return maps;
    }

    /**
     * 获取城市信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getCity")
    @ResponseBody
    public Object getCity(HttpServletRequest request, HttpServletResponse response, Long provinceId) {

        if (provinceId == null)
            return returnResultMap(false, null, "check", "请先选择省份！");
        List<LianLianProvinceCity> lianLianProvinceCities = cityInfoService.getLianLianProvinceCityByPID(provinceId);
        List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
        Map<Object, Object> map;
        map = new HashMap<Object, Object>();
        map.put("CITYNAME", "请选择");
        map.put("CITYID", "");
        map.put("selected", true);
        maps.add(map);
        if (null != lianLianProvinceCities && lianLianProvinceCities.size() > 0) {
            for (int i = 0; i < lianLianProvinceCities.size(); i++) {
                LianLianProvinceCity cityInfo = lianLianProvinceCities.get(i);
                map = new HashMap<Object, Object>();
                map.put("CITYNAME", cityInfo.getCityName());
                map.put("CITYID", cityInfo.getProvinceCityId());
                maps.add(map);
            }
        }
        return maps;
    }

    /**
     * 获取开户支行信息
     *
     * @param request
     */
    @RequestMapping(value = "getBankName")
    @ResponseBody
    public Object getBankName(HttpServletRequest request, String cardNo, Long cityId, String bankName) {

        if (cityId == null)
            return returnResultMap(false, null, "check", "请先选择城市！");
        try {
            bankName = new String(bankName.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        LianLianProvinceCity city = cityInfoService.getLianLianProvinceCityById(cityId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("card_no", cardNo);
        params.put("city_code", city.getProvinceCityCode());
        params.put("brabank_name", bankName);
        List<BankInfo> cnapsCode = LLPayUtil.getCNAPSCode(params);

        List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
        Map<Object, Object> map;
        map = new HashMap<Object, Object>();
        map.put("brabank_name", "请选择");
        map.put("prcptcd", "");
        map.put("selected", true);
        maps.add(map);
        if (null != cnapsCode && cnapsCode.size() > 0) {
            for (int i = 0; i < cnapsCode.size(); i++) {
                BankInfo cityInfo = cnapsCode.get(i);
                map = new HashMap<Object, Object>();
                map.put("brabank_name", cityInfo.getBrabank_name());
                map.put("prcptcd", cityInfo.getPrcptcd());
                maps.add(map);
            }
        }
        return maps;
    }

    /**
     * to添加银行卡
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/toAddCustomerCard")
    public ModelAndView toAddCustomerCard(Long userId, Long customCardId) {
        ModelAndView modelAndView = new ModelAndView();
        if (customCardId != null) {
            CustomerCard card = customerCardService.findById(customCardId);
            modelAndView.addObject("card", card);
            userId = card.getUserId();
            if (null != card.getProvinceCityId()) {
                //省市
                LianLianProvinceCity city = cityInfoService.getLianLianProvinceCityById(card.getProvinceCityId());
                LianLianProvinceCity province = cityInfoService.getLianLianProvinceCityById(city.getpProvinceCityId());
                modelAndView.addObject("province", province);
            }

        }

        modelAndView.addObject("needEdit", LLPayUtil.getNotNeedCityInfoBankIds());
        modelAndView.addObject("user", userInfoService.getUserExtByUserId(userId));
        modelAndView.setViewName("jsp/custom/customer/customer_card_add");
        return modelAndView;
    }

    /**
     * 保存银行卡
     *
     * @param customerCard
     * @return
     */
    @RequestMapping("/saveCustomerCard")
    @ResponseBody
    public Object saveCustomerCard(CustomerCard customerCard, String cardCustomerName, String cityCodeHF) {

        if (null == cityCodeHF || "".equals(cityCodeHF))
            return returnResultMap(false, null, "check", "cityCode不一致");
        if (customerCard.getCustomerCardId() == null) {
            customerCard.setStatus(CustomerCardStatus.NORMAL.getValue());
            customerCard.setBindStatus(CustomerCardBindStatus.UNBINDING.getValue());
            customerCard.setBelongChannel(PayConstants.PayChannel.HF.getValue());
            customerCard.setCardcustomerName(cardCustomerName);
            customerCard.setBranchName(cityCodeHF);
        }
        UserInfo user = userInfoService.getUserByUserId(customerCard.getUserId());
        // 普通用户只能拥有一张银行卡
        if (user.getType().equals(UserType.COMMON.getValue())) {
            CustomerCard old = customerCardService.getCustomerBindCardByUserId(customerCard.getUserId(), PayConstants.PayChannel.LL);
            if (null == old || customerCard.getCustomerCardId() != null) {
                customerCardService.saveOrUpdateCustomerCard(customerCard);
            } else {
                return "-1";
            }
        } else {
            customerCardService.saveOrUpdateCustomerCard(customerCard);
        }
        return "success";
    }

    /**
     * 查询银行卡号
     *
     * @return
     */
    @RequestMapping(value = "/searchBank")
    @ResponseBody
    public JSONArray searchBank(@RequestParam("typeCode") String typeCode) {
        JSONArray array = new JSONArray();
        List<ConstantDefine> bankList = customerCardService.searchBank(typeCode);
        for (ConstantDefine bank : bankList) {
            JSONObject obj = new JSONObject();
            obj.put("bankId", bank.getConstantDefineId());
            obj.put("bankValue", bank.getConstantName());
            array.add(obj);
        }
        return array;
    }

    /**
     * 获取用户卡下拉框
     *
     * @param selectedDisplay 默认显示值
     */
    @RequestMapping(value = "/loadCustomerCard")
    @ResponseBody
    public Object loadCustomerCard(@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay,
                                   @RequestParam(value = "originalUserId", required = false) String originalUserId) {
        if ("selected".equals(selectedDisplay)) {
            selectedDisplay = "请选择";
        } else {
            selectedDisplay = "全部";
        }
        CustomerCard card = new CustomerCard();
        card.setStatus(CustomerCardStatus.NORMAL.getValue());
        card.setBindStatus(CustomerCardBindStatus.BINDED.getValue());
        // 根据原始债权人id，获取对应的用户id
        BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserById(Long.valueOf(originalUserId));
        card.setUserId(bondSourceUser.getUserId());
        // 获取用户卡信息
        List<CustomerCard> list = customerCardService.getAllCustomerCard(card);
        List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
        Map<Object, Object> map;
        map = new HashMap<Object, Object>();
        map.put("CARDCUSTOMERNAME", selectedDisplay);
        map.put("CUSTOMERCARDID", "");
        map.put("selected", true);
        maps.add(map);
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                CustomerCard customerCard = list.get(i);
                map = new HashMap<Object, Object>();
                map.put("CARDCUSTOMERNAME", customerCard.getCardcustomerName());
                map.put("CUSTOMERCARDID", customerCard.getCustomerCardId());
                maps.add(map);
            }
        }
        return maps;
    }

    /**
     * to客户认证
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/toAuthentication")
    public ModelAndView toAuthentication(@RequestParam("userId") long userId) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("userId", userId);
        mv.setViewName("jsp/custom/customer/toAuthentication");
        return mv;
    }

    /**
     * 客户认证
     *
     * @param userId
     * @param idCard
     * @param realName
     * @return
     */
    @RequestMapping(value = "/customerAuthentication")
    @ResponseBody
    public String customerAuthentication(@RequestParam("userId") long userId, @RequestParam("idCard") String idCard,
                                         @RequestParam("realName") String realName) {
        if (StringUtils.isNull(idCard) || StringUtils.isNull(String.valueOf(userId)) || StringUtils.isNull(String.valueOf(realName))) {
            return JsonView.JsonViewFactory.create().success(false).info("请求参数不全！").toJson();
        }
        return idCardVerifiedService.updateUserVerified(userId, idCard, realName);
    }

    /**
     * 客户列表-详情-出借-省心计划出借列表
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "toSxjhLendList")
    public String toSxjhLendList(HttpServletRequest request, Long userId) {
        request.setAttribute("userId", userId);
        return "jsp/custom/customer/customer_sxjh_list";
    }

    private CommonOpenAccount4ApiResponse openAccountByLoan(CustomerCard customerCard, UserInfoExt userInfoExt) {
        OpenAccount4ApiPersonalDataSource dataSource = new OpenAccount4ApiPersonalDataSource();
        dataSource.setCapAcntNo(customerCard.getCardCode());//银行卡号
        dataSource.setCertif_tp(HfUtils.CertifTpEnum.ID_CARD.code());//证件类型为身份证
        dataSource.setMobile_no(userInfoExt.getMobileNo());//手机号
        dataSource.setCertif_id(userInfoExt.getIdCard());//身份证号
        dataSource.setCity_id(customerCard.getBranchName());//银行cityid
        String custom = "0" + customerCard.getBankCode().toString();
        dataSource.setParent_bank_id(custom); // 支行名称
        dataSource.setCust_nm(userInfoExt.getRealName());
        CommonOpenAccount4ApiResponse commonOpenAccount4ApiResponse = null;
        try {
            commonOpenAccount4ApiResponse = hfApi.openAccount(dataSource);
        } catch (HfApiException e) {
            e.printStackTrace();
        }
        return commonOpenAccount4ApiResponse;
    }

    /**
     * 报备表中的数据设置为已开户成功
     *
     * @param rechargeResponse
     */
    private void getTradeUpdate(OpenAccount4PCPersonResponse rechargeResponse) {
        Trade trade = new Trade();
        rechargeResponse.getMchnt_txn_ssn();
        rechargeResponse.getSignature();
        String inputStr = rechargeResponse.regSignVal();
        trade.setResponse_message(inputStr);
        trade.setSerial_number(rechargeResponse.getMchnt_txn_ssn());
        trade.setTrade_status(ResponseStatusEnum.Success.getValue());
        trade.setResponse_message(inputStr);
        tradeService.updateByPrimaryKeySelective(trade);
    }

    /**
     * 向报备表中增加数据
     *
     * @param dataSource
     */
    private void addTrade(OpenAccount4PCPersonalDataSource dataSource) {
        Trade trade = new Trade();
        String outputStr = dataSource.regSignVal();
        trade.setMessage_id(TradeTypeEnum.CorpRegist.getValue());
        trade.setTrade_status(ResponseStatusEnum.Unresponsive.getValue());
        trade.setRequest_message(outputStr);
        trade.setTrade_date(new Date());
        trade.setSerial_number(dataSource.getMchnt_txn_ssn());
        trade.setUser_id(Long.parseLong(dataSource.getUser_id_from()));
        tradeService.addTrade(trade);
    }

}
