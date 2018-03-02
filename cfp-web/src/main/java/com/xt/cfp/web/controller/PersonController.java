package com.xt.cfp.web.controller;

import com.external.deposites.api.IhfApi;
import com.external.yongyou.entity.http.YongYouBean;
import com.external.yongyou.util.YongYouUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.PayErrorCode;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.Exception.code.ext.WithDrawErrorCode;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.constants.RateEnum.RateUserStatusEnum;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.financePlan.FinancePlanProcessModule;
import com.xt.cfp.core.service.impl.CgBizService;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.*;
import com.xt.cfp.web.annotation.DoNotNeedLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by luqinglin on 2015/7/13.
 */
@Controller
@RequestMapping(value = "/person")
public class PersonController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private LendOrderReceiveService lendOrderReceiveService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private LoanFeesDetailService loanFeesDetailService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private DefaultInterestDetailService defaultInterestDetailService;
    @Autowired
    private WithDrawService withDrawService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private PayService payService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private LendProductService lendProductService;

    @Autowired
    private LoanProductService loanProductService;
    @Autowired
    private CityInfoService cityInfoService;
    @Autowired
    private RedisCacheManger redisCacheManger;


    @Autowired
    private AddressService addressService;
    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private CreditorRightsTransferAppService creditorRightsTransferAppService;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private LoanPublishService loanPublishService;

    @Autowired
    private RateLendOrderService rateLendOrderService;
    @Autowired
    private RateUserService rateUserService;
    @Autowired
    private LendLoanBindingService lendLoanBindingService;
    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    @Autowired
    private IhfApi ihfApi;
    @Autowired
    private CgBizService cgBizService;

    /**
     * 跳转至账户总览页面
     *
     * @return
     */
    @RequestMapping(value = "/account/overview")
    public String accountOverview(HttpServletRequest request) {
        //---组织页面参数
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        request.setAttribute("userExt", userExt);
        //已获收益、利息、奖励
        BigDecimal totalProfit = lendOrderService.getAllProfit(currentUser.getUserId());
        //==> 新增奖励方法
        BigDecimal totalAward = userAccountService.getUserTotalAward(currentUser.getUserId());
        //获取用户减少的奖励（如：取消）
        BigDecimal totalReduceAward = userAccountService.getUserTotalReduceAward(currentUser.getUserId());
        totalAward = totalAward.subtract(totalReduceAward);//实际得奖=励总的奖励-减少的奖励
        request.setAttribute("totalProfit", totalProfit);//已获利息
        request.setAttribute("totalAward", totalAward);//已获奖励
        request.setAttribute("allProfit", totalProfit.add(totalAward));//已获收益=已获利息+已获奖励
        //净资产
        request.setAttribute("netAsset", getNetAsset(request, currentUser));
        //出借走势
        request.setAttribute("categories", getCategories(currentUser));
        //累计出借
        BigDecimal totalLendAmount = lendOrderService.getTotalLendAmount(currentUser.getUserId());
        request.setAttribute("totalLendAmount", totalLendAmount);
        //累计借款
        BigDecimal totalLoanAmount = loanApplicationService.getTotalLoanAmount(currentUser.getUserId());
        request.setAttribute("totalLoanAmount", totalLoanAmount);
        //近期5条交易记录
        List<LendOrder> lendOrderRecent = lendOrderService.getLendOrderRecent(currentUser.getUserId(), 5);
        request.setAttribute("lendOrderRecent", lendOrderRecent);
        //出借利率
        List<LendProduct> profitRate = lendProductService.findProfitRate();
        request.setAttribute("profitRate", profitRate);
        //出借期数
        LendProduct lendProduct = new LendProduct();
        lendProduct.setTimeLimitType(LendProductTimeLimitType.TIMELIMITTYPE_MONTH.getValue());
        List<LendProduct> timeLimits = lendProductService.findTimeLimit(lendProduct);
        request.setAttribute("timeLimits", timeLimits);
        //借款期数
        List<LoanProduct> dueTimeMonth = loanProductService.findDueTimeMonth();
        request.setAttribute("dueTimeMonth", dueTimeMonth);
        //省心计划展示数据
        setFinanceAccountData(request, currentUser);

        //加息券数量
        List<RateUserVO> rateList = rateUserService.findRateUsersByUserId(currentUser.getUserId(), RateUserStatusEnum.UNUSED, RateUserStatusEnum.USING);
        int rateNumber = 0;
        if (rateList != null && rateList.size() > 0) {
            rateNumber = rateList.size();
        }
        request.setAttribute("rateNumber", rateNumber);

        return "/person/accountOverview";
    }

    /**
     * 省心计划展示数据
     */
    private void setFinanceAccountData(HttpServletRequest request,
                                       UserInfo currentUser) {
        List<LendOrder> financeOrderList = lendOrderService.getFinancialPlanListByUserId(currentUser.getUserId());
        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal totalInterest = BigDecimal.ZERO;
        BigDecimal awardBalance = BigDecimal.ZERO;
        BigDecimal financeAccountTurnValue = userAccountService.getFinanceAccountPayValue(currentUser.getUserId());
        BigDecimal totalFinanceAward = creditorRightsService.getTotalExpectAwardByUserId(currentUser.getUserId());
        for (LendOrder finance : financeOrderList) {
            totalValue = totalValue.add(finance.getBuyBalance().add(finance.getCurrentProfit()));
            totalInterest = totalInterest.add(finance.getCurrentProfit());
        }
        //获取用户所有省心账户-计算省心账户子标所有扣费
        BigDecimal financeFees = financePlanProcessModule.getAllFeesByUserId(currentUser.getUserId());
        awardBalance = userAccountService.getUserTotalFinanceAwardByUserId(currentUser.getUserId());
        BigDecimal waitInterest = lendOrderService.getFinancialWaitInterestByUserId(currentUser.getUserId());
        request.setAttribute("financeTotalValue", BigDecimalUtil.down(totalValue.add(waitInterest).add(totalFinanceAward).subtract(financeAccountTurnValue).subtract(financeFees), 2));
        request.setAttribute("financeTotalCounts", financeOrderList.size());
        request.setAttribute("financeTotalInterest", BigDecimalUtil.down(totalInterest.add(awardBalance), 2));
        request.setAttribute("financeWaitInterest", BigDecimalUtil.down(waitInterest, 2));
    }

    /**
     * 获取用户净资产
     *
     * @param currentUser
     * @return
     */
    private BigDecimal getNetAsset(HttpServletRequest request, UserInfo currentUser) {
        //净资产=账户余额+（待回本金+待回利息+持有省心计划）-（待还本金+待还利息+待缴费用+待还罚息）
        UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
        request.setAttribute("cashAccount", cashAccount);
        //财富券
        BigDecimal voucherValue = voucherService.getAllVoucherValue(currentUser.getUserId());
        //账户饼图数据组织
        request.setAttribute("voucherValue", voucherValue);
        request.setAttribute("dataPie", pieData(cashAccount));
        //待回本金（投标）
        BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(null, currentUser.getUserId(), LendProductTypeEnum.RIGHTING, LendProductTypeEnum.CREDITOR_RIGHTS);
        request.setAttribute("capitalRecive", capitalRecive);
        //待回收益（投标）
        BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(null, currentUser.getUserId(), LendProductTypeEnum.RIGHTING, LendProductTypeEnum.CREDITOR_RIGHTS);
        request.setAttribute("interestRecive", interestRecive);
        //持有全部省心计划
        BigDecimal totalHoldFinancePlan = BigDecimal.ZERO;
        List<LendOrder> financeOrderList = lendOrderService.getFinancialPlanListByUserId(currentUser.getUserId());
        BigDecimal financeAccountTurnValue = userAccountService.getFinanceAccountPayValue(currentUser.getUserId());
        //用户省心计划已获但未回账户的奖励
        BigDecimal totalFinanceAward = BigDecimal.ZERO;
        for (LendOrder financeOrder : financeOrderList) {
            totalHoldFinancePlan = totalHoldFinancePlan.add(financeOrder.getBuyBalance()).add(financeOrder.getCurrentProfit());
            totalFinanceAward = userAccountService.getUserTotalFinanceAwardByLendOrderId(financeOrder.getLendOrderId());
        }
        //获取用户所有省心账户-计算省心账户子标所有扣费
        BigDecimal financeFees = financePlanProcessModule.getAllFeesByUserId(currentUser.getUserId());
        //用户所有省心计划获取的奖励
        totalHoldFinancePlan = totalHoldFinancePlan.add(totalFinanceAward);
        totalHoldFinancePlan = totalHoldFinancePlan.subtract(financeAccountTurnValue).subtract(financeFees);
        //省心计划带回款利息
        BigDecimal waitInterest = lendOrderService.getFinancialWaitInterestByUserId(currentUser.getUserId());
        totalHoldFinancePlan = totalHoldFinancePlan.add(waitInterest);
        request.setAttribute("totalHoldFinancePlan", BigDecimalUtil.up(totalHoldFinancePlan, 2));
        //待还本金
        BigDecimal replaymentCapital = repaymentPlanService.getRepaymentCapitalByUserId(currentUser.getUserId());
        request.setAttribute("replaymentCapital", replaymentCapital);
        //待还利息
        BigDecimal replaymentInterest = repaymentPlanService.getRepaymentInterestByUserId(currentUser.getUserId());
        request.setAttribute("replaymentInterest", replaymentInterest);
        //待缴费用
        BigDecimal loanFeeNopaied = loanFeesDetailService.getLoanFeeNoPaied(currentUser.getUserId());
        request.setAttribute("loanFeeNopaied", loanFeeNopaied);
        //待还罚息
        BigDecimal interestPaid = defaultInterestDetailService.getDefaultInterestByUserId(currentUser.getUserId());
        request.setAttribute("interestPaid", interestPaid);

        return cashAccount.getValue2().add(totalHoldFinancePlan).add(capitalRecive).add(interestRecive).subtract(replaymentCapital).subtract(replaymentInterest).subtract(loanFeeNopaied).subtract(interestPaid);
    }

    /**
     * 组织饼图数据
     *
     * @param cashAccount
     * @return
     */
    private String pieData(UserAccount cashAccount) {
        if (cashAccount.getValue2().compareTo(BigDecimal.ZERO) == 0) {
            return "[['账户余额  0元',   100]]";
        }
        //可用财富券
        BigDecimal avalibleVoucherValue = voucherService.getAllVoucherValue(cashAccount.getUserId(), VoucherConstants.VoucherStatus.UN_USAGE);
        String result = "[";
        result += "['可用余额  " + StringUtils.parseMoney("#,##0.00#", cashAccount.getAvailValue2()) + "元'," + cashAccount.getAvailValue2() + "],";
//        result += "['冻结金额',"+cashAccount.getFrozeValue2()+"],";
//        result += "['可用财富券  "+StringUtils.parseMoney("#,##0.00#", avalibleVoucherValue)+"元',"+avalibleVoucherValue+"],";
//        result += "['冻结财富券  "+StringUtils.parseMoney("#,##0.00#", voucherValue.subtract(avalibleVoucherValue))+"元',"+voucherValue.subtract(avalibleVoucherValue)+"],";
        result += "['冻结金额  " + StringUtils.parseMoney("#,##0.00#", cashAccount.getFrozeValue2()) + "元'," + cashAccount.getFrozeValue2() + "]";
        result += "]";
        return result;
    }

    /**
     * 出借走势数据组织
     *
     * @param userInfo
     * @return
     */
    public String[] getCategories(UserInfo userInfo) {

        Map<String, Pair<BigDecimal, BigDecimal>> bidTrend = lendOrderReceiveService.getTrendByUserIdNew(userInfo.getUserId(), LendProductTypeEnum.RIGHTING.getValue());//投标
        Map<String, Pair<BigDecimal, BigDecimal>> licaiTrend = lendOrderReceiveService.getTrendByUserIdNew(userInfo.getUserId(), LendProductTypeEnum.FINANCING.getValue());//省心计划
        String toubiao = "[";
        String licai = "[";
        String monthStr = "[";
        for (String month : bidTrend.keySet()) {
            monthStr += "'" + month + "',";
            Pair<BigDecimal, BigDecimal> p1 = bidTrend.get(month);
            Pair<BigDecimal, BigDecimal> p2 = licaiTrend.get(month);
            toubiao += p1.getK().subtract(p1.getV()) + ",";
            licai += p2.getK().subtract(p2.getV()) + ",";
        }
        monthStr = monthStr.substring(0, monthStr.length() - 1) + "]";
        licai = licai.substring(0, licai.length() - 1) + "]";
        toubiao = toubiao.substring(0, toubiao.length() - 1) + "]";

        String[] str = {monthStr, licai, toubiao};
        return str;
    }


    /**
     * 跳转到提现页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toWithDraw")
    public String toWithDraw(HttpServletRequest request) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfo userInfo = userInfoService.getUserByUserId(currentUser.getUserId());
        request.setAttribute("userInfo", userInfo);
        //账户余额
        UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
        request.setAttribute("cashAccount", cashAccount);
        //提现卡
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);
        if(customerCard!=null){
            CgBank bankInfo = cgBizService.getBankInfo(customerCard.getBankNum(), CgBank.IdTypeEnum.PERSON);
            customerCard.setBankCode(bankInfo.getIconCode());
        }
        request.setAttribute("customerCard", customerCard);
        //是否需要补录银行卡信息
        /*if (customerCard != null) {
            String bankIds = LLPayUtil.getNotNeedCityInfoBankIds();
            boolean needEdit = true;

            if (bankIds.indexOf(customerCard.getBankCode().toString()) != -1) {
                needEdit = false;
            }

            if (needEdit && null != customerCard.getProvinceCityId()) {
                needEdit = false;
            }
            request.setAttribute("needEdit", needEdit);
        }*/
        boolean isBidEqualLoginPass = userInfoService.isBidPassEqualLoginPass(currentUser.getUserId());
        request.setAttribute("isBidEqualLoginPass", isBidEqualLoginPass);
        //当前手续费写死
        request.setAttribute("fee", InterestCalculation.getWithDrawFee(null));
        //获取当日提现次数上限
        int times = withDrawService.getWithDrawTimesDue();
        request.setAttribute("times", times);
        //获取用户使用次数
        int doTimes = withDrawService.getWithDrawTimesByUserId(currentUser.getUserId());
        request.setAttribute("doTimes", doTimes);
        //获取有效提现券个数
        int count = withDrawService.getVoucherWithDrawCount(currentUser.getUserId());
        request.setAttribute("voucherCount", count);
        //清空历史提现验证码(非数字验证失败，清空)
        redisCacheManger.destroyRedisCacheInfo(TemplateType.SMS_WITHDRAW_APPLY_VM.getPrekey() + userInfo.getMobileNo());


        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        request.setAttribute("userExt", userExt);
        List<LianLianProvinceCity> lianLianProvinceCities = cityInfoService.getLianLianProvinceCityByPID(0L);
        request.setAttribute("province", lianLianProvinceCities);
        return "/person/withDraw";
    }

    /**
     * 跳转到财富券页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toVoucher")
    public String toVoucher(HttpServletRequest request) {
        return "/person/voucher";
    }

    /**
     * 跳转到财富券页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/voucherList")
    @ResponseBody
    public Object voucherList(HttpServletRequest request,
                              @RequestParam(value = "state", defaultValue = "-1") String state,
                              @RequestParam(value = "couponType", defaultValue = "-1") String couponType,
                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                              @RequestParam(value = "startDate", defaultValue = "", required = false) String startDate,
                              @RequestParam(value = "endDate", defaultValue = "", required = false) String endDate,
                              @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));


        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);
        //财富券
        VoucherVO voucherVO = new VoucherVO();
        switch (state) {
            case "-1":
                break;
            case "0":
                //可用
                customParams.put("fstatus", VoucherConstants.VoucherFrontStatus.CAN_USAGE.getValue());
                break;
            case "1":
                //不可用
                customParams.put("fstatus", VoucherConstants.VoucherFrontStatus.CAN_NOT_USAGE.getValue());
                break;
        }
        voucherVO.setVoucherCouponType(couponType);
        voucherVO.setUserId(currentUser.getUserId());
        if (StringUtils.isNull(voucherVO.getCouponType())) {
            voucherVO.setCouponType("2");//加息券
        }
        Pagination<VoucherVO> voucherPaging = voucherService.getVoucherPaging(pageNo, pageSize, voucherVO, customParams);
        return voucherPaging;
    }


    /**
     * 跳转到充值页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toIncome")
    public String toIncome(HttpServletRequest request) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        TokenUtils.setNewToken(request);
        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        request.setAttribute("userExt", userExt);
        //历史卡信息
        /*CustomerCard hisCustomerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(),PayConstants.PayChannel.HF);
        if(hisCustomerCard!=null){
            boolean isSupport = true;
            ConstantDefine bankInfo = constantDefineService.findById(hisCustomerCard.getBankCode());
            if (bankInfo.getConstantStatus().equals("1")){//无效银行卡
                isSupport = false;
            }
            request.setAttribute("isSupport", isSupport);
        }*/
        //提现卡
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);
        CgBank bankInfo = cgBizService.getBankInfo(customerCard.getBankNum(), CgBank.IdTypeEnum.PERSON);
        customerCard.setBankCode(bankInfo.getIconCode());
        request.setAttribute("customerCard", customerCard);
        //request.setAttribute("hisCustomerCard", hisCustomerCard);
        //获取网关充值银行卡列表
        /*List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByTypeNeedSort("gateway_bank");
        request.setAttribute("bankList", constantDefines);*/
        //return "/person/incomeWithoutCard";
        return "/person/incomeNew";
    }


    /**
     * 提交提现单
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/withDraw")
    @ResponseBody
    public Object withDraw(HttpServletRequest request, BigDecimal moneyp, String rankm, String valid, boolean voucher) {

        //后端验证注册信息
        Map<String, String> validate = (Map<String, String>) validate(voucher, moneyp, valid, rankm, true);
        for (String key : validate.keySet()) {
            String result = validate.get(key);
            Map<String, Object> jsonMap = JsonUtil.fromJson(result);
            boolean isSuccess = (Boolean) jsonMap.get("isSuccess");
            if (!isSuccess)
                // 跳转错误页面
                return validate;
        }
        //完成生成提现申请操作
        try {
            withDrawService.withDraw(voucher, getWithDraw(moneyp), AccountConstants.AccountChangedTypeEnum.PLATFORM_USER, ClientEnum.WEB_CLIENT);
            return "success";
        } catch (SystemException e) {
            logger.error(e.getMessage(), e);
            return "error" + ":" + e.getMessage();
        }
    }


    /**
     * 提现列表
     *
     * @param request
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/withDrawList")
    @ResponseBody
    public Object withDrawList(HttpServletRequest request,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        //提现记录
        WithDraw withDraw = new WithDraw();
        withDraw.setUserId(currentUser.getUserId());
        Pagination<WithDrawExt> withDrawPaging = withDrawService.getWithDrawPaging(pageNo, pageSize, withDraw, null);
        return withDrawPaging;
    }

    /**
     * 发送短信验证码
     *
     * @return
     */
    @RequestMapping(value = "/withDraw/getMsg")
    @ResponseBody
    public Object getMsg() {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        try {
            withDrawService.sendWithDrawApplyMsg(currentUser.getMobileNo());
            return JsonView.JsonViewFactory.create().success(true).info("")
                    .toJson();
        } catch (SystemException se) {
            logger.error(se.getMessage(), se);
            return JsonView.JsonViewFactory.create().success(false).info("")
                    .toJson();
        }
    }

    /**
     * 提现后端验证
     *
     * @return
     */
    @RequestMapping(value = "/withDraw/validate")
    @ResponseBody
    public Object validate(boolean voucher, BigDecimal amount, String validCode, String bidPass, boolean flag) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        HashMap<String, String> map = new HashMap<String, String>();
        //验证提现券
        if (voucher) {
            int voucherWithDrawCount = withDrawService.getVoucherWithDrawCount(currentUser.getUserId());
            if (voucherWithDrawCount < 1) {
                throw new SystemException(WithDrawErrorCode.WITHDRAW_VOUCHER_NULL).set("voucherWithDrawCount", voucherWithDrawCount);
            }
        }
        //验证金额
        boolean amountResult = false;
        boolean bidPassResult = false;
        map.put("amount", validatAmount(amount, amountResult));
        //交易密码
        map.put("bidpass", validatBidPass(bidPass, bidPassResult));
        //校验提现次数
        if (amountResult) {
            //由于提现次数和金额错误提示显示位置在一块所以需要先验证金额后验证次数
            map.put("times", validatTimes());
        }
        if (!amountResult || !bidPassResult) {
            flag = false;//不清除提现验证码
        }
        //验证短信码,并返回
        map.put("valid", validate(currentUser.getMobileNo(), validCode, flag));
        return map;

    }

    private String validatTimes() {
        int times = withDrawService.getWithDrawTimesDue();
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        //获取用户使用次数
        int doTimes = withDrawService.getWithDrawTimesByUserId(currentUser.getUserId());
        if (times > doTimes)
            return JsonView.JsonViewFactory.create().success(true).info("").put("id", "moneyp")
                    .toJson();
        else
            return JsonView.JsonViewFactory.create().success(false).info(WithDrawErrorCode.WITHDRAW_TIMES_OUT.getDesc()).put("id", "moneyp")
                    .toJson();

    }

    /**
     * 验证交易密码
     *
     * @param bidPass
     * @return
     */
    private String validatBidPass(String bidPass, boolean bidPassResult) {
        boolean b = passwordValidate(bidPass);
        if (!b) {
            return JsonView.JsonViewFactory.create().success(false).info("请输入6~16位字符，支持字母及数字,字母区分大小写").put("id", "rankm")
                    .toJson();
        }
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        String password = MD5Util.MD5Encode(bidPass, "utf-8");
        if (!currentUser.getBidPass().equals(password)) {
            return JsonView.JsonViewFactory.create().success(false).info("交易密码不正确，请重新输入").put("id", "rankm")
                    .toJson();
        }
        bidPassResult = true;
        return JsonView.JsonViewFactory.create().success(true).info("").put("id", "rankm")
                .toJson();
    }

    /**
     * 验证短信验证码
     *
     * @param mobileNo
     * @param validCode
     * @return
     */
    private String validate(String mobileNo, String validCode, boolean flag) {
        try {
            boolean bool = smsService.validateMsg(mobileNo, validCode, TemplateType.SMS_WITHDRAW_APPLY_VM, flag);
            if (bool)
                return JsonView.JsonViewFactory.create().success(true).info("").put("id", "valid")
                        .toJson();
            else
                return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.VALIDATE_CODE_ERROR.getDesc()).put("id", "valid")
                        .toJson();
        } catch (SystemException se) {
            logger.error(se.getMessage(), se);
            return JsonView.JsonViewFactory.create().success(false).info(se.getMessage()).put("id", "valid")
                    .toJson();
        }
    }

    /**
     * 验证提现金额
     *
     * @param amount
     * @return
     */
    private String validatAmount(BigDecimal amount, boolean result) {
        if (amount == null) {
            result = false;
            return JsonView.JsonViewFactory.create().success(false).info(WithDrawErrorCode.ERROR_AMOUNT.getDesc()).put("id", "moneyp")
                    .toJson();
        }
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
        if (amount.compareTo(new BigDecimal("100")) < 0) {
            result = false;
            return JsonView.JsonViewFactory.create().success(false).info(WithDrawErrorCode.MORE_THAN_HUNDRED.getDesc()).put("id", "moneyp")
                    .toJson();
        }
        BigDecimal withDrawAmount = withDrawService.getWithDrawAmountByUserId(currentUser.getUserId());
        if (new BigDecimal("500000").compareTo(amount.add(withDrawAmount)) < 0) {
            result = false;
            return JsonView.JsonViewFactory.create().success(false).info(WithDrawErrorCode.WITHDRAW_AMOUNT_OVERLIMIT.getDesc()).put("id", "moneyp")
                    .toJson();
        }
        if (amount.compareTo(new BigDecimal("500000")) > 0) {
            result = false;
            return JsonView.JsonViewFactory.create().success(false).info(WithDrawErrorCode.WITHDRAW_AMOUNT_OVERLIMIT.getDesc()).put("id", "moneyp")
                    .toJson();
        }
        if (cashAccount.getAvailValue2().compareTo(amount) < 0) {
            result = false;
            return JsonView.JsonViewFactory.create().success(false).info(WithDrawErrorCode.WITHDRAW_AMOUNT_NOT_ENOUGH.getDesc()).put("id", "moneyp")
                    .toJson();
        }
        result = true;
        return JsonView.JsonViewFactory.create().success(true).info("").put("id", "moneyp")
                .toJson();
    }


    /**
     * 组织提现单
     *
     * @param amount
     * @return
     */
    public WithDraw getWithDraw(BigDecimal amount) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);
        if (customerCard == null) {
            throw new SystemException(UserErrorCode.HAS_NO_CARD_AVALIBLE).set("currentUser", currentUser.getLoginName());
        }
        WithDraw withDraw = new WithDraw();
        withDraw.setWithdrawAmount(amount);
        withDraw.setHappenType(WithDrawSource.USER_WITHDRAW.getValue());
        withDraw.setUserId(currentUser.getUserId());
        withDraw.setCustomerCardId(customerCard.getCustomerCardId());
        withDraw.setCreateTime(new Date());
        return withDraw;
    }

    /**
     * 跳到资金管理页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toFundManage")
    public String toFundManage(HttpServletRequest request) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
        request.setAttribute("cashAccount", cashAccount);
        //获取当前可用财富券
        BigDecimal voucherValue = voucherService.getAllVoucherValue(currentUser.getUserId());
        request.setAttribute("voucherValue", voucherValue);
        return "/person/fundManage";
    }

    /**
     * 资金流水列表
     *
     * @param request
     * @param session
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/fundManageList")
    @ResponseBody
    public Object fundManageList(HttpServletRequest request, HttpSession session,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                 @RequestParam(value = "accId", required = false) Long accId,
                                 @RequestParam(value = "flowType[]", required = false) String[] flowType,
                                 @RequestParam(value = "searchDate[]", required = false) String[] searchDate) {
        System.out.println("变更类型" + Arrays.toString(flowType));
        return userAccountService.getUserAccountHisByAccId(pageNo, pageSize, accId, flowType, searchDate, AccountConstants.VisiableEnum.DISPLAY);
    }

    /**
     * to 资产管理-订单查询
     *
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/to_lendorder_list")
    public ModelAndView to_lendorder_query(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                           @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("person/lendorder_list");
        return mv;
    }

    /**
     * 订单查询
     *
     * @param request
     * @param loanApplicationListVO
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/lendorder_list")
    @ResponseBody
    public Pagination<LendOrderExtProduct> lendorder_list(HttpServletRequest request, LoanApplicationListVO loanApplicationListVO,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderCode", request.getParameter("orderCode"));
        paramMap.put("orderState", request.getParameter("orderState"));
        paramMap.put("startDate", request.getParameter("startDate"));
        paramMap.put("endDate", request.getParameter("endDate"));
        paramMap.put("lendUserId", SecurityUtil.getCurrentUser(true).getUserId());
        return lendOrderService.getLendOrderListBy(pageNo, pageSize, paramMap);
    }

    /**
     * to 资产管理-订单查询-订单详情
     *
     * @param request
     * @param lendOrderId
     * @return
     */
    @RequestMapping(value = "/to_lendorder_detail")
    public String to_lendorder_detail(HttpServletRequest request, Long lendOrderId) {
        if (lendOrderId == null) {
            // todo 没有订单id 跳至错误页
            throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderUserId", lendOrderId);
        }
        LendOrder order = lendOrderService.findById(lendOrderId);
        if (order == null) {
            // todo 没有订单 跳至错误页
            throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderUserId", lendOrderId);
        }
        // 校验-订单用户和当前用户必须相同
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        if (!order.getLendUserId().equals(currentUser.getUserId())) {
            throw new SystemException(PayErrorCode.NOT_BELONG_PAY_ORDER).set("lendOrderUserId", order.getLendUserId()).set("user",
                    currentUser.getUserId());
        }

        List<PayOrderDetail> paymentOrderDetail = payService.getPaymentOrderDetail(order.getLendOrderId());
        LendProduct lendProduct = lendProductService.findById(order.getLendProductId());

        // 如果是否为 省心计划 的出借订单【开始】
        if (LendProductTypeEnum.FINANCING.getValue().equals(order.getProductType())) {
            request.setAttribute("order", order);
            request.setAttribute("lendProduct", lendProduct);
            request.setAttribute("paymentOrderDetail", paymentOrderDetail);
            String dueTimeScope = lendLoanBindingService.getLoanProductDueTimeByLendProductId(lendProduct.getLendProductId());
            request.setAttribute("dueTimeScope", dueTimeScope);
            return "/order/orderDetail";
        }
        // 如果是否为 省心计划 的出借订单【结束】

        //查询加息券和奖励
        RateLendOrder rateOrder = rateLendOrderService.findByLendOrderId(lendOrderId, RateLendOrderTypeEnum.RATE_COUPON.getValue(), null);
        if (rateOrder != null) {
            request.setAttribute("rateOrder", rateOrder);
        }
        RateLendOrder activityOrder = rateLendOrderService.findByLendOrderId(lendOrderId, RateLendOrderTypeEnum.ACTIVITY.getValue(), null);
        if (activityOrder != null) {
            request.setAttribute("activityOrder", activityOrder);
        }

        // 预期收益
        BigDecimal expectedInteresting = BigDecimal.ZERO;
        List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(order.getLendOrderId());
        LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(lendOrderBidDetail.getLoanApplicationId());
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());

        LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());

        if (LendProductTypeEnum.CREDITOR_RIGHTS.getValue().equals(order.getProductType())) {
            // 预计收益提前部分还款
            CreditorRightsTransferApplication crta = creditorRightsTransferAppService.getTransferApplicationByLendOrderId(order.getLendOrderId());
            boolean isLookProduct = false;
            if (crta.getBusStatus().equals(CreditorRightsTransferAppStatus.TRANSFERRING.getValue())) {
                isLookProduct = true;
            }
            request.setAttribute("isLookProduct", isLookProduct);
            String[] temp_expectedInteresting = creditorRightsService.getExpectRightProfit(crta.getCreditorRightsApplyId(), order.getBuyBalance()).toString().split(",");
            expectedInteresting = new BigDecimal(String.valueOf(temp_expectedInteresting[0]));
            if (temp_expectedInteresting.length == 2) {
                BigDecimal awardProfit = new BigDecimal(String.valueOf(temp_expectedInteresting[1]));
                request.setAttribute("awardProfit", awardProfit);
            }
            loanApplicationListVO
                    .setAwardRate(!StringUtils.isNull(loanApplicationListVO.getAwardRate()) && !loanApplicationListVO.getAwardRate().equals("0")
                            && null != loanApplicationListVO.getAwardPoint() && !loanApplicationListVO.getAwardPoint().equals(AwardPointEnum.ATMAKELOAN.getValue()) ? loanApplicationListVO.getAwardRate() + "%" : "");

            request.setAttribute("creditorRightsApplyId", crta.getCreditorRightsApplyId());
        } else if (order.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
            // 计算奖励金额
            if (loanApplicationListVO.getAwardRate() != null && !loanApplicationListVO.getAwardRate().equals("")
                    && !loanApplicationListVO.getAwardRate().equals("0")) {

                BigDecimal amount = order.getBuyBalance();
                BigDecimal profit = BigDecimal.ZERO;
                try {
                    profit = InterestCalculation.getAllInterest(amount, new BigDecimal(loanApplicationListVO.getAwardRate()),
                            loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(),
                            loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                profit = BigDecimalUtil.down(profit, 2);
                request.setAttribute("awardProfit", profit);
            }
            loanApplicationListVO.setAwardRate(loanApplicationListVO.getAwardRate() != null && !loanApplicationListVO.getAwardRate().equals("")
                    && !loanApplicationListVO.getAwardRate().equals("0") ? loanApplicationListVO.getAwardRate() + "%" : "");

            // 预期收益
            try {
                expectedInteresting = InterestCalculation.getAllInterest(order.getBuyBalance(), order.getProfitRate(), order.getTimeLimitType()
                        .charAt(0), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct
                        .getDueTime(), loanProduct.getCycleValue());
                expectedInteresting = BigDecimalUtil.down(expectedInteresting, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 奖励发放时机
        String point = StringUtils.isNull(loanApplicationListVO.getAwardPoint()) ? "" : null;
        if (point == null) {
            if (loanApplicationListVO.getAwardPoint().equals("1")) {
                point = AwardPointEnum.ATMAKELOAN.getDesc();
            } else if (loanApplicationListVO.getAwardPoint().equals("2")) {
                point = AwardPointEnum.ATREPAYMENT.getDesc();
            } else {
                point = AwardPointEnum.ATCOMPLETE.getDesc();
            }
        }
        loanApplicationListVO.setAwardPoint(point);
        request.setAttribute("order", order);
        request.setAttribute("lendProduct", lendProduct);
        request.setAttribute("loanApplication", loanApplicationListVO);
        request.setAttribute("paymentOrderDetail", paymentOrderDetail);
        request.setAttribute("expectedInteresting", expectedInteresting);
        return "/order/orderDetail";

    }

    /**
     * to 好友邀请
     *
     * @return
     */
    @RequestMapping(value = "/to_invite_friends")
    public String to_invite_friends(HttpServletRequest req) {
        UserInfo user = SecurityUtil.getCurrentUser(true);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", user.getUserId());
        param.put("type", 0);
        InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
        if (invite != null) {
            String invitecode = invite.getInvitationCode();
            req.setAttribute("invitecode", invitecode);
            req.setAttribute("inviteURL", PropertiesUtils.getInstance().get("INVITE_FRIENDS_VISIT_PATH") + "?invite_code=" + invitecode);
        }
        return "/person/invite_friends";
    }

    @RequestMapping(value = "/user_qrcode")
    @ResponseBody
    public String user_qrcode(HttpServletRequest req) {
        UserInfo user = SecurityUtil.getCurrentUser(true);
        return userInfoService.getQrImg(user.getUserId());
    }


    @DoNotNeedLogin
    @RequestMapping(value = "/beinvite")
    public String beinvite(HttpServletRequest req, String invite_code, String type) {
        InvitationCode invitationCode = userInfoService.getInvitationCodeByCode(invite_code);
        if (invitationCode == null) {
            throw new SystemException(UserErrorCode.INVITECODE_NOT_EXIST).set("invite_code", invite_code);
        }
        InvitationCode param = new InvitationCode();
        param.setInvitationId(invitationCode.getInvitationId());
        param.setType(type);
        InvitationCode result = userInfoExtService.getUserInfoByInviteId(param);
        if (null != result) {
            req.setAttribute("inviteUserId", result.getUserId());
            req.setAttribute("invite_code", invite_code);
        }

        //获取ua，用来判断是否为移动端访问
        String userAgent = req.getHeader("USER-AGENT").toLowerCase();
        if (null == userAgent) {
            userAgent = "";
        }
        boolean isFromMobile = CheckMobile.check(userAgent);
        //判断是否为移动端访问
        if (isFromMobile) {
            return "/register/mobileRegister";
        } else {
            //用于记录页面登陆成功后的跳转页面
            req.setAttribute("pastUrl", "/");
            return userInfoService.getRegisterPath();
        }

    }


    /**
     * 获取二维码
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @RequestMapping(value = "/getErWeiMa")
    @ResponseBody
    public void getErWeiMa(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String invitecode = ShareCodeUtil.toSerialCode(SecurityUtil.getCurrentUser(true).getUserId());
        String keycode = PropertiesUtils.getInstance().get("INVITE_FRIENDS_VISIT_PATH") + "?invite_code=" + invitecode;
        if (keycode != null && !"".equals(keycode)) {
            OutputStream stream = null;
            try {
                int size = 280;
                String msize = req.getParameter("msize");
                if (msize != null && !"".equals(msize.trim())) {
                    size = Integer.valueOf(msize);
                }
                stream = resp.getOutputStream();
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix m = writer.encode(keycode, BarcodeFormat.QR_CODE, size, size);
                MatrixToImageWriter.writeToStream(m, "png", stream);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }

    }

    /**
     * 获取邀请好友list
     *
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/invite_friends_list")
    @ResponseBody
    public Pagination<UserInfoVO> invite_friends_list(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        UserInfo user = SecurityUtil.getCurrentUser(false);
        Pagination<UserInfoVO> inviteUserList = userInfoExtService.getUserInviteFriends(pageNo, pageSize, user.getUserId());
        return inviteUserList;
    }

    /**
     * 跳转到：密码管理页
     */
    @RequestMapping(value = "/to_pass_manage")
    public ModelAndView to_pass_manage(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        TokenUtils.setNewToken(request);//创建Token值
        mv.setViewName("person/passManage");
        return mv;
    }

    /**
     * 执行：修改密码
     *
     * @param pass_type 密码类型（登录，交易）
     * @param login_old 登录原密码
     * @param login_new 登录新密码
     * @param trade_old 交易原密码
     * @param trade_new 交易新密码
     * @return
     */
    @RequestMapping(value = "/savePass", method = RequestMethod.POST)
    @ResponseBody
    public Object savePass(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "pass_type", required = false) String pass_type,
                           @RequestParam(value = "login_old", required = false) String login_old,
                           @RequestParam(value = "login_new", required = false) String login_new,
                           @RequestParam(value = "trade_old", required = false) String trade_old,
                           @RequestParam(value = "trade_new", required = false) String trade_new) {
        try {
            TokenUtils.validateToken(request);//验证Token值
            // 合法性验证
            if (null == pass_type || "".equals(pass_type)) {
                return returnResultMap(false, null, "check", "密码类型不能为空");
            } else {
                if ("login".equals(pass_type)) {
                    if (null == login_old || "".equals(login_old)) {
                        return returnResultMap(false, null, "check", "原密码不能为空");
                    }
                    if (null == login_new || "".equals(login_new)) {
                        return returnResultMap(false, null, "check", "新密码不能为空");
                    }
                    if (!passwordValidate(login_new)) {
                        return returnResultMap(false, null, "check", "密码为6-16位字符，支持字母及数字,字母区分大小写");
                    }
                } else if ("trade".equals(pass_type)) {
                    if (null == trade_old || "".equals(trade_old)) {
                        return returnResultMap(false, null, "check", "原密码不能为空");
                    }
                    if (null == trade_new || "".equals(trade_new)) {
                        return returnResultMap(false, null, "check", "新密码不能为空");
                    }
                    if (!passwordValidate(trade_new)) {
                        return returnResultMap(false, null, "check", "密码为6-16位字符，支持字母及数字,字母区分大小写");
                    }
                } else {
                    return returnResultMap(false, null, "check", "密码类型无效");
                }
            }
            // 获取当前登录用户
            UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
            // 根据用户ID，加载一条数据
            UserInfo userInfo = userInfoService.getUserByUserId(currentUserInfo.getUserId());
            // 赋值
            if ("login".equals(pass_type)) {
                String md5_login_old = MD5Util.MD5Encode(login_old.trim(), "utf-8");
                if (!userInfo.getLoginPass().equals(md5_login_old)) {
                    return returnResultMap(false, null, "check", "原密码不正确");
                }
                String md5_login_new = MD5Util.MD5Encode(login_new.trim(), "utf-8");
                userInfo.setLoginPass(md5_login_new);
            } else if ("trade".equals(pass_type)) {
                String md5_trade_old = MD5Util.MD5Encode(trade_old.trim(), "utf-8");
                if (!userInfo.getBidPass().equals(md5_trade_old)) {
                    return returnResultMap(false, null, "check", "原密码不正确");
                }
                String md5_trade_new = MD5Util.MD5Encode(trade_new.trim(), "utf-8");
                userInfo.setBidPass(md5_trade_new);
            }
            // 更改操作
            userInfoService.updateUser(userInfo);
            // 覆盖用户登录信息
            request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 跳转到：个人信息页
     */
    @RequestMapping(value = "/to_userInfo")
    public ModelAndView to_userInfo() {
        ModelAndView mv = new ModelAndView();

        // 获取当前登录用户
        UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
        // 根据用户ID，加载一条数据
        UserInfo userInfo = userInfoService.getUserByUserId(currentUserInfo.getUserId());
        // 根据用户ID，加载一条扩展数据
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(currentUserInfo.getUserId());

        mv.addObject("username", userInfo.getLoginName());
        mv.addObject("education", userInfoExt.getEducationLevel());
        mv.addObject("isVerified", userInfoExt.getIsVerified());
        mv.addObject("realName", userInfoExt.getRealName());
        mv.addObject("sex", userInfoExt.getSexStr());
        mv.addObject("birthday", userInfoExt.getBirthday());
        mv.addObject("mobileNo", StringUtils.getEncryptMobileNo(userInfoExt.getMobileNo()));

        Address address = addressService.getAddressById(userInfoExt.getResidentAddress());
        if (null != address) {
            mv.addObject("province", address.getProvince());
            mv.addObject("city", address.getCity());
            mv.addObject("detail", address.getDetail());
        }

        mv.setViewName("person/userInfo");
        return mv;
    }

    /**
     * 执行：修改用户个人信息
     */
    @RequestMapping("/saveUserInfo")
    @ResponseBody
    public Object saveUserInfo(HttpServletRequest request, HttpSession session,
                               @RequestParam(value = "username", required = false) String username,
                               @RequestParam(value = "education", required = false) String education,
                               @RequestParam(value = "province", required = false) String province,
                               @RequestParam(value = "city", required = false) String city,
                               @RequestParam(value = "address", required = false) String address) {
        try {
            // 合法性验证
            if (null == username || "".equals(username)) {
                return returnResultMap(false, null, "check", "用户名不能为空");
            } else if (!loginNameValidate(username)) {
                return returnResultMap(false, null, "check", "用户名含非法字符");
            }
//			if(null == education || "".equals(education)){
//				return returnResultMap(false, null, "check", "学历不能为空");
//			}
//			if(null == province || "".equals(province)){
//				return returnResultMap(false, null, "check", "常住地省份不能为空");
//			}
//			if(null == city || "".equals(city)){
//				return returnResultMap(false, null, "check", "常住地城市不能为空");
//			}
//			if(null == address || "".equals(address)){
//				return returnResultMap(false, null, "check", "详细地址不能为空");
//			}

            // 获取当前登录用户
            UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
            // 根据用户ID，加载一条数据
            UserInfo userInfo = userInfoService.getUserByUserId(currentUserInfo.getUserId());

            // 判断用户名是否已经存在，非本人
            UserInfo info = userInfoService.getUserByLoginName(username);
            if (null != info && !userInfo.getLoginName().equals(info.getLoginName())) {
                return returnResultMap(false, null, "check", "用户名已经存在");
            }

            // 赋值
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setUserId(userInfo.getUserId());
            userInfoVO.setLoginName(username);
            userInfoVO.setEducationLevel(education);
            if (null != province && !"".equals(province)) {
                userInfoVO.setProvince(Long.valueOf(province));
            }
            if (null != city && !"".equals(city)) {
                userInfoVO.setCity(Long.valueOf(city));
            }
            userInfoVO.setDetail(address);

            // 更改操作
            userInfoService.updateUserInfo(userInfoVO);
            // 覆盖用户登录信息
            request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 跳转到：用户认证页面
     */
    @RequestMapping(value = "/toAuthentication")
    public ModelAndView toAuthentication(HttpServletRequest request) throws SystemException {
        String mobileNo = "";
        String idCard = "";
        UserInfo user = SecurityUtil.getCurrentUser(true);
        UserInfoExt userExt = new UserInfoExt();
        userExt = userInfoExtService.getUserInfoExtById(user.getUserId());
        mobileNo = plusXing(userExt.getMobileNo(), 3, 3);
        if (!"".equals(userExt.getIdCard()) && userExt.getIdCard() != null) {
            idCard = plusXing(userExt.getIdCard(), 3, 4);
        }
        TokenUtils.setNewToken(request);
        ModelAndView mv = new ModelAndView();
        mv.addObject("mobileNo", mobileNo);
        mv.addObject("idCard", idCard);
        mv.addObject("bind", userExt.getIsVerified());
        mv.addObject("userExt", userExt);
        CustomerCard cc = customerCardService.getCustomerBindCardUseful(userExt.getUserId());
        if (null != cc) {
            mv.addObject("cardCode", cc.getCardCode());
        }
        mv.setViewName("person/infoAuthentication");
        return mv;
    }

    /**
     * 身份证认证
     *
     * @param request
     * @param session
     * @param idCard
     * @param trueName
     * @return
     */
    @RequestMapping(value = "/identityAuthentication")
    @ResponseBody
    public Object identityAuthentication(HttpServletRequest request, HttpSession session,
                                         @RequestParam(value = "idCard", required = false) String idCard,
                                         @RequestParam(value = "trueName", required = false) String trueName) {

        if ("".equals(idCard) || idCard == null) {
            return returnResultMap(false, null, "idCardEmpty", "身份证为空");
        }
        if ("".equals(trueName) || trueName == null) {
            return returnResultMap(false, null, "trueNameEmpty", "用户名字为空");
        }
        if (!idCardValidate(idCard)) {
            return returnResultMap(false, null, "idCardCheck", "身份证格式不正确");
        }
        int userExist = userInfoExtService.identityBindingExist(idCard, trueName);
        if (userExist > 0) {
            return returnResultMap(false, null, "idCardCheck", "该身份证已被占用");
        }
        UserInfo user = SecurityUtil.getCurrentUser(true);
        //验证是否已经认证过
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(user.getUserId());
        if (UserIsVerifiedEnum.YES.getValue().equals(userInfoExt.getIsVerified())) {
            return returnResultMap(false, null, "idCardCheck", "当前用户已经认证过");
        }
        boolean resultFlag = userInfoExtService.identityAuthentication(idCard, trueName, user.getUserId());
        if (resultFlag) {
            return returnResultMap(true, null, null, null);
        }
        return returnResultMap(false, null, "identityError", "身份证和姓名验证失败");
    }

    /**
     * 修改手机验证发送验证码
     *
     * @param mobileNo
     * @return
     */
    @RequestMapping(value = "/mobileAuthentication")
    @ResponseBody
    public String mobileAuthentication(String mobileNo) {
        try {
            //验证手机号格式
            if (!mobileNoValidate(mobileNo)) {
                return JsonView.JsonViewFactory.create().success(false).info("手机号码格式错误").put("id", "mobileNo")
                        .toJson();
            }
            boolean mobileExist = userInfoService.isMobileExist(mobileNo);
            if (mobileExist) {
                return JsonView.JsonViewFactory.create().success(false).info("与原手机号相同或手机号已被使用").put("id", "mobileNo")
                        .toJson();
            }
            userInfoService.sendmobileAuthenticationMsg(mobileNo);
            return JsonView.JsonViewFactory.create().success(true).info("")
                    .toJson();
        } catch (SystemException se) {
            logger.error(se.getMessage(), se);
            return JsonView.JsonViewFactory.create().success(false).info("")
                    .toJson();
        }
    }

    /**
     * 修改手机验证  校验验证码
     *
     * @param valid
     * @param phone
     * @return
     */
    @RequestMapping(value = "/mobileAuthentValidate")
    @ResponseBody
    public String mobileAuthentValidate(HttpServletRequest request, HttpSession session, String valid, String phone) {
        try {
            boolean bool = smsService.validateMsg(phone, valid, TemplateType.SMS_MOBILEAUTHENTICATION_VM, true);
            if (bool) {
                UserInfo user = SecurityUtil.getCurrentUser(true);
                UserInfo userNew = userInfoService.getUserByUserId(user.getUserId());
                if (!user.getMobileNo().equals(userNew.getMobileNo())) {
                    request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, userNew);
                    return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.USER_NOT_EXIST.getDesc()).put("id", "valid")
                            .toJson();
                }
                boolean mobileExist = userInfoService.isMobileExist(phone);
                if (mobileExist) {
                    return JsonView.JsonViewFactory.create().success(false).info("与原手机号相同或手机号已被使用").put("id", "mobileNo")
                            .toJson();
                }
                TokenUtils.validateToken(request);
                //修改手机验证
                user = userInfoService.mobileAuthentValidate(user, phone, UserSource.WEB);
                //更新一下session的数据
                request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, user);
                return JsonView.JsonViewFactory.create().success(true).info("").put("id", "valid")
                        .toJson();
            } else
                return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.VALIDATE_CODE_ERROR.getDesc()).put("id", "valid")
                        .toJson();
        } catch (SystemException se) {
            logger.error(se.getMessage(), se);
            return JsonView.JsonViewFactory.create().success(false).info(se.getMessage()).put("id", "valid")
                    .toJson();
        }
    }

    /**
     * 隐藏信息
     *
     * @param str
     * @param frontLen
     * @param endLen
     * @return
     */
    public String plusXing(String str, int frontLen, int endLen) {
        int len = str.length() - frontLen - endLen;
        String xing = "";
        for (int i = 0; i < len; i++) {
            xing += "*";
        }
        return str.substring(0, frontLen) + xing + str.substring(str.length() - endLen);
    }

    /**
     * 执行：发送手机验证码（找回交易密码）
     */
    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
    @ResponseBody
    public Object sendMsg(HttpServletRequest request, HttpSession session) {
        try {
            // 获取当前登录用户
            UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
            // 根据用户ID，加载一条数据
            UserInfo userInfo = userInfoService.getUserByUserId(currentUserInfo.getUserId());
            // 验证手机号
            if (!mobileNoValidate(userInfo.getMobileNo())) {
                return returnResultMap(false, null, null, "手机号码格式错误");
            }
            // 发送验证码
            userInfoService.sendResetTradePassMsg(userInfo.getMobileNo());
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, TokenUtils.setNewToken(request), null, null);//创建Token值
    }

    /**
     * 执行：校验手机验证码（找回交易密码）
     *
     * @param valid 短信验证码
     */
    @RequestMapping(value = "/checkMsg", method = RequestMethod.POST)
    @ResponseBody
    public Object checkMsg(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "valid", required = false) String valid) {
        try {
            // 合法性验证
            if (null == valid || "".equals(valid)) {
                return returnResultMap(false, null, "check", "验证码不能为空！");
            } else if (valid.length() != 6 || !StringUtils.isPattern("^([0-9]+)$", valid)) {
                return returnResultMap(false, null, "check", "验证码格式不正确！");
            }
            // 获取当前登录用户
            UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
            // 根据用户ID，加载一条数据
            UserInfo userInfo = userInfoService.getUserByUserId(currentUserInfo.getUserId());
            // 验证验证码
            boolean bool = smsService.validateMsg(userInfo.getMobileNo(), valid, TemplateType.SMS_RESETTRADEPASSMSG_VM, true);
            if (!bool) {
                return returnResultMap(false, null, null, "短信验证码不正确，请重新操作！");
            }
            TokenUtils.validateToken(request);//验证Token值(注：如果一个获取验证码，只能校验一次，则把Token校验放到验证码校验前面)
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, TokenUtils.setNewToken(request), null, null);//创建Token值
    }

    /**
     * 执行：保存交易密码（找回交易密码）
     *
     * @param trade_pass 新交易密码
     */
    @RequestMapping(value = "/saveTradePass", method = RequestMethod.POST)
    @ResponseBody
    public Object saveTradePass(HttpServletRequest request, HttpSession session,
                                @RequestParam(value = "trade_pass", required = false) String trade_pass) {
        try {
            TokenUtils.validateToken(request);//验证Token值
            // 合法性验证
            if (null == trade_pass || "".equals(trade_pass)) {
                return returnResultMap(false, null, "check", "交易密码不能为空");
            }
            if (!passwordValidate(trade_pass)) {
                return returnResultMap(false, null, "check", "密码为6-16位字符，支持字母及数字,字母区分大小写");
            }
            // 获取当前登录用户
            UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
            // 根据用户ID，加载一条数据
            UserInfo userInfo = userInfoService.getUserByUserId(currentUserInfo.getUserId());
            // 赋值
            String md5_trade_new = MD5Util.MD5Encode(trade_pass.trim(), "utf-8");
            userInfo.setBidPass(md5_trade_new);
            // 更改操作
            userInfoService.updateUser(userInfo);
            // 覆盖用户登录信息
            request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 用友报表流水对外开放接口
     */
    @RequestMapping(value = "/getAccountHisForYongYou")
    @DoNotNeedLogin
    public void getUserAccountHisForYongYou(HttpServletRequest request, HttpServletResponse response) {
        try {
            YongYouBean reqBean = YongYouUtil.resposneForYongYou(request);
            YongYouBean respBean = userAccountService.getUserAccountHisForYongYou(reqBean.getStartTime(), reqBean.getEndTime());
            YongYouUtil.responseHttp(respBean, response);
        } catch (Exception e) {
            logger.error("处理用友流水请求异常，异常原因：", e);
        }
    }

    /**
     * 获取用户基本信息
     */
    @RequestMapping(value = "/getUserCommonInfo")
    public String getUserCommonInfo(HttpServletRequest request) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        request.setAttribute("userExt", userExt);
        return "person/userLeftCommon";
    }

}
