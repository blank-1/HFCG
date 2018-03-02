package com.xt.cfp.wechat.controller;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.OpenAccount4ApiPersonalDataSource;
import com.external.deposites.model.response.CommonOpenAccount4ApiResponse;
import com.external.deposites.utils.HfUtils;
import com.external.llpay.BankInfo;
import com.external.llpay.LLPayUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.Exception.code.ext.WithDrawErrorCode;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.LendOrderConstants.FinanceOrderStatusEnum;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.*;
import com.xt.cfp.core.security.util.Des3;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.financePlan.FinancePlanProcessModule;
import com.xt.cfp.core.service.impl.CgBizService;
import com.xt.cfp.core.service.impl.SundryDataQuery;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.*;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
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
    private AddressService addressService;

    @Autowired
    private CityInfoService cityInfoService;

    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private RedisCacheManger redisCacheManger;
    @Autowired
    private CommiProfitService commiProfitService;
    @Autowired
    private CommisionService commisionService;
    @Autowired
    private DistributionInviteService distributionInviteService;
    @Autowired
    private WhiteTabsService whiteTabsService;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private LoanPublishService loanPublicService;
    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    @Autowired
    private IhfApi ihfApi;
    @Autowired
    private SundryDataQuery apiDataQuery;
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

        //用户邀请码
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", currentUser.getUserId());
        param.put("type", 0);
        InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
        request.setAttribute("invite_code", invite.getInvitationCode());

        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        request.setAttribute("userExt", userExt);
        //已获收益、利息、奖励
        BigDecimal totalProfit = lendOrderService.getAllProfit(currentUser.getUserId());
        //==> 新增奖励方法
        BigDecimal totalAward = userAccountService.getUserTotalAward(currentUser.getUserId());
        //获取用户减少的奖励（如：取消）
        BigDecimal totalReduceAward = userAccountService.getUserTotalReduceAward(currentUser.getUserId());
        totalAward = totalAward.subtract(totalReduceAward);//实际得奖=励总的奖励-减少的奖励
        request.setAttribute("totalAward", totalAward);
        request.setAttribute("allProfit", totalProfit.add(totalAward));
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
        //可用的财富卷张数
        Pagination<VoucherVO> voucherPaging = (Pagination<VoucherVO>) this.voucherList(request, "0", "-1", 10, "", "", 1);
        request.setAttribute("voucherTotal", voucherPaging.getTotal());
        //总推广收益
        BigDecimal disProfit = commiProfitService.calUserAccountProfit(currentUser.getUserId());
        request.setAttribute("salesDistributionProfit", disProfit);
        //是否是白名单用户
        int white = whiteTabsService.countUserId(currentUser.getUserId());
        if (white == 0) {
            request.setAttribute("userFlag", "0");
        } else {
            request.setAttribute("userFlag", "1");
        }
        return "/person/accountOverview";
    }

    /**
     * 我的推荐
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/account/myRecommend")
    public String myRecommend(HttpServletRequest request) {
        //---组织页面参数
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        //总推广收益
        BigDecimal disProfit = commiProfitService.calUserAccountProfit(currentUser.getUserId());
        request.setAttribute("salesDistributionProfit", disProfit);
        BigDecimal sProfit = commiProfitService.calUserAccountShouldProfit(currentUser.getUserId());
        request.setAttribute("salesDistributionsholdProfit", sProfit.subtract(disProfit));
        //级别佣金
        /*BigDecimal first=commisionService.calUserLevelProfit(currentUser.getUserId(), DisActivityEnums.DistribututionLevelEnum.LEVEL_FIRST.getValue());
        BigDecimal second=commisionService.calUserLevelProfit(currentUser.getUserId(), DisActivityEnums.DistribututionLevelEnum.LEVEL_SECOND.getValue());
        BigDecimal third=commisionService.calUserLevelProfit(currentUser.getUserId(), DisActivityEnums.DistribututionLevelEnum.LEVEL_THIRD.getValue());
        request.setAttribute("firstProfit",first);
        request.setAttribute("secondProfit",second);
        request.setAttribute("thirdProfit",third);*/
        //推荐人数
        Integer inviteNum = distributionInviteService.countUserCustomerByUserId(currentUser.getUserId());
        request.setAttribute("inviteNum", inviteNum);
        //用户邀请码
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", currentUser.getUserId());
        param.put("type", 0);
        InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
        request.setAttribute("incode", invite.getInvitationCode());
        return "/person/myRecommend";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/recommend/code")
    public String recommendCode(HttpServletRequest request, String invite_code) {
        if (!StringUtils.isNull(invite_code)) {
            request.setAttribute("invitecode", invite_code);
        } else {
            UserInfo currentUser = SecurityUtil.getCurrentUser(false);
            if (null != currentUser) {
                Integer inviteNum = distributionInviteService.countUserCustomerByUserId(currentUser.getUserId());
                request.setAttribute("inviteNum", inviteNum);
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("userId", currentUser.getUserId());
                param.put("type", 0);
                InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
                if (invite != null) {
                    String invitecode = invite.getInvitationCode();
                    request.setAttribute("invitecode", invitecode);
                }
            }

        }
        return "/person/recommendCode";
    }

    @RequestMapping(value = "/inviteFriends")
    public String inviteFriends(HttpServletRequest request) {

        return "/person/inviteFriends";
    }

    @RequestMapping(value = "/fiendsList")
    @ResponseBody
    public Object fiendsList(HttpServletRequest request, @RequestParam(value = "rows", defaultValue = "20") int pageSize, @RequestParam(value = "page", defaultValue = "1") int pageNo) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", currentUser.getUserId());
        return distributionInviteService.findAllByPage(pageNo, pageSize, params);
    }

    @RequestMapping(value = "/commision")
    public String commision(HttpServletRequest request, String userLevel) {
        request.setAttribute("userLevel", userLevel);
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        if (currentUser != null) {
            // 用户邀请码
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", currentUser.getUserId());
            param.put("type", 0);
            InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
            request.setAttribute("invite_code", invite.getInvitationCode());
        }

        return "/person/commisionList";
    }

    @RequestMapping(value = "/commisionList")
    @ResponseBody
    public Object commisionList(HttpServletRequest request, @RequestParam(value = "rows", defaultValue = "20") int pageSize,
                                @RequestParam(value = "page", defaultValue = "1") int pageNo, String userLevel) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", currentUser.getUserId());
        if (!StringUtils.isNull(userLevel)) {
            if (!userLevel.equals("0")) {
                params.put("userLevel", userLevel);
            }
        }
        return commisionService.findAllByPage(pageNo, pageSize, params);
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
        Map<String, Pair<BigDecimal, BigDecimal>> licaiTrend = lendOrderReceiveService.getTrendByUserIdNew(userInfo.getUserId(), LendProductTypeEnum.FINANCING.getValue());//理财
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
     * 跳转到提现页面前，验证完善银行卡信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toChoiceBranch")
    public String toChoiceBranch(HttpServletRequest request) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        //账户余额
        UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
        request.setAttribute("cashAccount", cashAccount);
        //提现卡
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
        if (null == customerCard) {// 如果连连无卡，查询易宝
            customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.YB);
        }
        request.setAttribute("customerCard", customerCard);

        //所属银行及卡号
        ConstantDefine bankInfo = constantDefineService.findById(customerCard.getBankCode());
        request.setAttribute("bankName", bankInfo.getConstantName());
        request.setAttribute("encryptCardNo", StringUtils.getPreStr(customerCard.getCardCode(), 4) + "********" + StringUtils.getLastStr(customerCard.getCardCode(), 4));

        //返回查询条件
        if (null != customerCard) {
            LianLianProvinceCity cityInfo = cityInfoService.getLianLianProvinceCityById(customerCard.getProvinceCityId());
            if (null != cityInfo) {
                request.setAttribute("w_seachPid", cityInfo.getpProvinceCityId());
                request.setAttribute("w_seachCid", cityInfo.getProvinceCityId());
                request.setAttribute("w_seachMag", customerCard.getRegisteredBank());
                request.setAttribute("w_seachPidStr", cityInfo.getProvinceName());
                request.setAttribute("w_seachCidStr", cityInfo.getCityName());
            }
        }

        return "/person/choiceBranch";
    }

    /**
     * 执行查询支行列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/choiceBranchQueryResult")
    @ResponseBody
    public Object choiceBranchQueryResult(HttpServletRequest request,
                                          @RequestParam(value = "w_seachCid", required = false) String w_seachCid,
                                          @RequestParam(value = "w_seachMag", required = false) String w_seachMag) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);

        String cityCode = "";
        if (null != w_seachCid && !"".equals(w_seachCid)) {
            LianLianProvinceCity cInfo = cityInfoService.getLianLianProvinceCityById(Long.valueOf(w_seachCid));
            cityCode = cInfo.getProvinceCityCode();
        }

        //提现卡
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
        if (null == customerCard) {// 如果连连无卡，查询易宝
            customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.YB);
        }
        List<BankInfo> bankInfoList = null;
        //查询支行信息
        if (null != cityCode && !"".equals(cityCode) && null != w_seachMag && !"".equals(w_seachMag.trim())) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("card_no", customerCard.getCardCode());
            params.put("city_code", cityCode);
            params.put("brabank_name", w_seachMag);
            bankInfoList = LLPayUtil.getCNAPSCode(params);
        }

        return bankInfoList;
    }

    /**
     * 处理添加支行信息
     *
     * @param request
     * @param prcptcd      支行编号
     * @param brabank_name 支行名称
     * @param brabank_city 所在城市
     * @return
     */
    @RequestMapping(value = "/choiceBranch")
    public String choiceBranch(HttpServletRequest request,
                               @RequestParam(value = "prcptcd", required = false) String prcptcd,
                               @RequestParam(value = "brabank_name", required = false) String brabank_name,
                               @RequestParam(value = "brabank_city", required = false) String brabank_city) {

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);

        //验证参数
        if (null == prcptcd || "".equals(prcptcd)) {
            request.setAttribute("errorMsg", "支行信息参数错误");
            return "/error";
        }

        if (null == brabank_name || "".equals(brabank_name)) {
            request.setAttribute("errorMsg", "支行信息参数错误");
            return "/error";
        }

        if (null == brabank_city || "".equals(brabank_city)) {
            request.setAttribute("errorMsg", "支行信息参数错误");
            return "/error";
        }

        //更改用户支行信息
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
        if (null != customerCard) {
            customerCard.setRegisteredBank(brabank_name);
            customerCard.setBankLineNumber(prcptcd);
            customerCard.setProvinceCityId(Long.valueOf(brabank_city));
            customerCardService.saveOrUpdateCustomerCard(customerCard);
        } else {
            // 添加新的银行卡，连连的
            customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.YB);
            if (null != customerCard) {
                CustomerCard lianLianCustomerCard = new CustomerCard();
                lianLianCustomerCard.setUserId(customerCard.getUserId());//用户ID
                lianLianCustomerCard.setBankCode(customerCard.getBankCode());//所属银行
                lianLianCustomerCard.setProvinceCityId(Long.valueOf(brabank_city));//省市id【页面获取】
                lianLianCustomerCard.setRegisteredBank(brabank_name);//开户行详细地址【页面获取】
                lianLianCustomerCard.setMobile(customerCard.getMobile());//银行预留手机号
                lianLianCustomerCard.setCardType(customerCard.getCardType());//卡类型
                lianLianCustomerCard.setCardCode(customerCard.getCardCode());//卡号
                lianLianCustomerCard.setCardCustomerName(customerCard.getCardCustomerName());//开户名
                lianLianCustomerCard.setStatus(customerCard.getStatus());//卡状态
                lianLianCustomerCard.setBindStatus(customerCard.getBindStatus());//绑卡状态
                lianLianCustomerCard.setUpdateTime(new Date());//最后更改时间【新赋值】
                lianLianCustomerCard.setAgreeNo(customerCard.getAgreeNo());//签约协议号
                lianLianCustomerCard.setBelongChannel(PayConstants.PayChannel.LL.getValue());//所属渠道【新赋值】
                lianLianCustomerCard.setBankLineNumber(prcptcd);//大额行号【页面获取】
                lianLianCustomerCard.setBranchName(customerCard.getBranchName());//支行名字

                customerCardService.addCustomerCard(lianLianCustomerCard);
            }
        }

        //转发到提现页面
        return this.toWithDraw(request);
    }

    /**
     * 记载连连城市下拉框。（连连提现专用）
     *
     * @param pProvinceCityId 父ID
     * @return
     */
    @RequestMapping(value = "/loadLianLianCitySelect")
    @ResponseBody
    public Object loadSelect(@RequestParam(value = "pProvinceCityId", required = false) Long pProvinceCityId) {
        List<LianLianProvinceCity> list = cityInfoService.getLianLianProvinceCityByPID(pProvinceCityId);
        List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
        Map<Object, Object> map;
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LianLianProvinceCity lianLianProvinceCity = list.get(i);
                map = new HashMap<Object, Object>();
                map.put("text", lianLianProvinceCity.getCityName());
                map.put("value", lianLianProvinceCity.getProvinceCityCode());
                map.put("id", lianLianProvinceCity.getProvinceCityId());
                maps.add(map);
            }
        }
        return maps;
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
        //验证是否实名
        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        if (!userExt.getIsVerified().equals(UserIsVerifiedEnum.BIND.getValue())) {
            request.setAttribute("url", "/person/toWithDraw");
            return "forward:/finance/toRealName";
        }
        //账户余额
        UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
        request.setAttribute("cashAccount", cashAccount);
        BigDecimal withDrawAmount = withDrawService.getWithDrawAmountByUserId(currentUser.getUserId());
        request.setAttribute("surplusAmount", new BigDecimal(500000).subtract(withDrawAmount).intValue() > 0 ? new BigDecimal(500000).subtract(withDrawAmount) : 0);
        //提现卡
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);
        if (customerCard == null) {
            return "redirect:/bankcard/to_bidding";
        }
        request.setAttribute("customerCard", customerCard);
        //是否需要补录银行卡信息
        //所属银行及卡号
        CgBank cgBank = cgBizService.getBankInfo(customerCard.getBankNum(), CgBank.IdTypeEnum.PERSON);
        request.setAttribute("bankName", cgBank.getName());
        request.setAttribute("encryptCardNo", StringUtils.getPreStr(customerCard.getCardCode(), 4) + "********" + StringUtils.getLastStr(customerCard.getCardCode(), 4));//银行卡号
//        ConstantDefine bankInfo = constantDefineService.findById(customerCard.getBankCode());
//        request.setAttribute("bankName", bankInfo.getConstantName());//银行名称

        //省、市、支行地址信息
//        if (null != customerCard.getProvinceCityId()) {
//            LianLianProvinceCity cityInfo = cityInfoService.getLianLianProvinceCityById(customerCard.getProvinceCityId());
//            request.setAttribute("cityName", cityInfo.getCityName());
//            request.setAttribute("provinceName", cityInfo.getProvinceName());
//            request.setAttribute("registeredBank", customerCard.getRegisteredBank());
//        }

        //支付密码和登录密码是否一致
        boolean isBidEqualLoginPass = userInfoService.isBidPassEqualLoginPass(currentUser.getUserId());
        request.setAttribute("isBidEqualLoginPass", isBidEqualLoginPass);
        //用户扩展信息
        request.setAttribute("userExt", userExt);
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
        return "/person/withDraw";
    }

    @RequestMapping(value = "/withDrawResult")
    public String withDrawResult(HttpServletRequest request, Long withdrawId) {
        WithDraw withdraw = withDrawService.getWithDrawByWithDrawId(withdrawId);
        request.setAttribute("createTime", withdraw.getCreateTime());
        request.setAttribute("auditTime", DateUtil.addDate(withdraw.getCreateTime(), 1));
        return "/person/withDrawResult";
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
     * 跳转到财富券支付选取页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toVoucherPag")
    public String toVoucherPag(HttpServletRequest request) {
        return "/person/voucherPag";
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
        //验证是否实名认证
        if (!userExt.getIsVerified().equals(UserIsVerifiedEnum.BIND.getValue())) {
            request.setAttribute("url", "/person/toIncome");
            return "forward:/finance/toRealName";
        }

        // 提现卡
        CustomerCard customerCard = cgBizService.findCurrentCard(currentUser.getUserId());
        if (null != customerCard) {
            // 连连没有绑定，查询易宝绑定情况
            if (PayConstants.PayChannel.YB.getValue().equals(customerCard.getBelongChannel())) {
                boolean support = LLPayUtil.checkLLCardSupport(customerCard.getCardCode());
                request.setAttribute("support", support);
                request.setAttribute("ybBindCard", customerCard);
            }
        }

        boolean isBidEqualLoginPass = userInfoService.isBidPassEqualLoginPass(currentUser.getUserId());
        request.setAttribute("isBidEqualLoginPass", isBidEqualLoginPass);
        request.setAttribute("customerCard", customerCard);
        return "/person/recharge";
    }


    /**
     * 充值列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toIncomeList")
    public String toIncomeList(HttpServletRequest request) {

        return "/person/newIncomeList";
    }

    /**
     * 提现列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toWithdrawList")
    public String toWithdrawList(HttpServletRequest request) {

        return "/person/newWithdrawList";
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
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        //后端验证注册信息
        Map<String, String> validate = (Map<String, String>) validate(moneyp, valid, rankm, true);
        for (String key : validate.keySet()) {
            String result = validate.get(key);
            Map<String, Object> jsonMap = JsonUtil.fromJson(result);
            boolean isSuccess = (Boolean) jsonMap.get("isSuccess");
            if (!isSuccess) {
                return validate;
            }
        }
        try {
            WithDraw d = withDrawService.withDraw(voucher, getWithDraw(moneyp), AccountConstants.AccountChangedTypeEnum.PLATFORM_USER, ClientEnum.from(currentUser.getAppSource()));
            return "success," + d.getWithdrawId();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
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
    public Object validate(BigDecimal amount, String validCode, String bidPass, boolean flag) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        HashMap<String, String> map = new HashMap<String, String>();
        //验证金额
        boolean amountResult = false;
        map.put("amount", validatAmount(amount, amountResult));
        //交易密码
        map.put("bidpass", validatBidPass(bidPass));
        //验证短信码,并返回
        map.put("valid", validate(currentUser.getMobileNo(), validCode, flag));
        //校验提现次数
//        if (amountResult){
        //由于提现次数和金额错误提示显示位置在一块所以需要先验证金额后验证次数
        map.put("times", validatTimes());
//        }
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
    private String validatBidPass(String bidPass) {
        boolean b = passwordValidate(bidPass);
        if (!b) {
            return JsonView.JsonViewFactory.create().success(false).info("请输入6~16位字符，字母加数字组合，字母区分大小写").put("id", "rankm")
                    .toJson();
        }
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        String password = MD5Util.MD5Encode(bidPass, "utf-8");
        if (!currentUser.getBidPass().equals(password)) {
            return JsonView.JsonViewFactory.create().success(false).info("交易密码不正确，请重新输入").put("id", "rankm")
                    .toJson();
        }
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
        if (new BigDecimal("500000").compareTo(withDrawAmount.add(amount)) < 0) {
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
        result = true;// 验证通过，告知下面据需验证，提现次数
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
        CustomerCard customerCard = cgBizService.findCurrentCard(currentUser.getUserId());
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
        request.setAttribute("w_value", request.getParameter("flag"));
        return "/person/fundManage";
    }

    /**
     * 跳到资金管理页面详情
     * 公众平台
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toFundManageInfo")
    public String toFundManageInfo(HttpServletRequest request) {
        //UserInfo currentUser = SecurityUtil.getCurrentUser(true);

        return "/person/fundManageInfo";
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
                                 @RequestParam(value = "flowType[]", required = false) String[] flowType,
                                 @RequestParam(value = "searchDate[]", required = false) String[] searchDate) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
        /*System.out.println("变更类型" + Arrays.toString(flowType));
        System.out.println("变更类型" + Arrays.toString(searchDate));*/
        return userAccountService.getUserAccountHisByAccId(pageNo, pageSize, cashAccount.getAccId(), flowType, searchDate, AccountConstants.VisiableEnum.DISPLAY);
    }

    @RequestMapping(value = "/fundManageInfoDetail", method = RequestMethod.POST)
    public String fundManageInfoDetail(HttpServletRequest request, String id) {
        UserAccountHis his = userAccountService.getUserAccountHisById(Long.parseLong(id));
        if (null == his) {
            request.setAttribute("errorMsg", "非法参数");
            return "/error";
        }
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
        UserAccount ua = userAccountService.getUserAccountByAccId(his.getAccId());
        if (userInfo.getUserId().compareTo(ua.getUserId()) != 0) {
            request.setAttribute("errorMsg", "非法参数");
            return "/error";
        }

        request.setAttribute("userHis", his);
        return "/person/fundManageDetail";
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
        }
        LendOrder order = lendOrderService.findById(lendOrderId);
        if (order == null) {
            // todo 没有订单 跳至错误页
        }
        LendProduct lendProduct = lendProductService.findById(order.getLendProductId());
        request.setAttribute("order", order);
        request.setAttribute("lendProduct", lendProduct);

        //预期收益
        BigDecimal expectedInteresting = InterestCalculation.getExpectedInteresting(order.getBuyBalance(), order.getProfitRate(), order.getTimeLimitType().charAt(0), order.getTimeLimit());
        request.setAttribute("expectedInteresting", expectedInteresting);
        List<PayOrderDetail> paymentOrderDetail = payService.getPaymentOrderDetail(order.getLendOrderId());
        request.setAttribute("paymentOrderDetail", paymentOrderDetail);
        if (order.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
            //投标详情页
            //借款申请描述
            List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(order.getLendOrderId());
            LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
            LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(lendOrderBidDetail.getLoanApplicationId());
            request.setAttribute("loanApplication", loanApplication);
        }
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
        List<UserInfoVO> inviteUserList = userInfoExtService.getUserAllInvite(user.getUserId());
        req.setAttribute("users", inviteUserList);
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
        if (invite_code == null) {
            return "/register/sharePage";
        }
        InvitationCode invitationCode = userInfoService.getInvitationCodeByCode(invite_code);
        if (invitationCode == null) {
            return "/register/sharePage";
        }
        InvitationCode param = new InvitationCode();
        param.setInvitationId(invitationCode.getInvitationId());
        param.setType("0");
        InvitationCode result = userInfoExtService.getUserInfoByInviteId(param);
        if (null != result) {
            req.setAttribute("inviteUserId", result.getUserId());
            req.setAttribute("invite_code", invite_code);
        }
        req.setAttribute("pastUrl", "/");
        return "/register/sharePage";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/distribution")
    public String distribution(HttpServletRequest req, String invite_code) {
        UserInfo currentUser = null;
        try {
            currentUser = SecurityUtil.getCurrentUser(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.setAttribute("sameCode", "2");
        if (currentUser != null) {
            req.setAttribute("islogin", "1");
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", currentUser.getUserId());
            param.put("type", 0);
            InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
            if (invite != null) {
                if (invite.getInvitationCode().equals(invite_code)) {
                    req.setAttribute("sameCode", "1");
                }
            }
        }
        req.setAttribute("invite_code", invite_code);
        return "/register/shareFenXiao";
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

        String keycode = "http://m.caifupad.com/person/beinvite?invite_code=" + req.getParameter("invitecode");
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
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }

    }

    @DoNotNeedLogin
    @RequestMapping(value = "/getFenXiaoErWeiMa")
    @ResponseBody
    public void getFenXiaoErWeiMa(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String keycode = "http://m.caifupad.com/user/regist/share?invite_code=" + req.getParameter("invite_code");
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
    public Pagination<UserInfoVO> invite_friends_list(@RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
                                                      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        UserInfo user = SecurityUtil.getCurrentUser(false);
        Pagination<UserInfoVO> inviteUserList = userInfoExtService.getUserInviteFriends(pageNo, pageSize, user.getUserId());
        return inviteUserList;
    }

    /**
     * 跳转到：密码管理页
     */
    @RequestMapping(value = "/to_pass_manage")
    public ModelAndView to_pass_manage() {
        ModelAndView mv = new ModelAndView();
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
    @RequestMapping("/savePass")
    @ResponseBody
    public Object savePass(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "pass_type", required = false) String pass_type,
                           @RequestParam(value = "login_old", required = false) String login_old,
                           @RequestParam(value = "login_new", required = false) String login_new,
                           @RequestParam(value = "trade_old", required = false) String trade_old,
                           @RequestParam(value = "trade_new", required = false) String trade_new) {
        try {
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
    public ModelAndView toAuthentication(HttpServletRequest request) {
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
        mv.setViewName("person/infoAuthentication");
        return mv;
    }

    @ModelAttribute(value = "currentUser")
    public UserInfo getUserInfo() {
        return SecurityUtil.getCurrentUser(false);
    }

    /**
     * 所有实名认证的东西都来开户这里
     */
    @RequestMapping(value = "/identityAuthenticationBy")
    public String identityAuthentication(Model model, String url, String userToken, String source) {
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExt4currentUser();
        if (UserIsVerifiedEnum.BIND.getValue().equals(userInfoExt.getIsVerified())) {
            return "forward:/apiCombine/openSuccess";
        }
        CustomerCard card = customerCardService.getCustomerBindCardUseful(userInfoExt.getUserId());
        model.addAttribute("userInfoExt", userInfoExt);
        model.addAttribute("card", card);
        model.addAttribute("_u", url);
        model.addAttribute("source", source);
        model.addAttribute("userToken", userToken);
        return "cg/w_openBank";
    }

    @DoNotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/queryBranch")
    public Object queryBranch(String key) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);

        List<CgBankBranch> branches = apiDataQuery.queryBankBranch(key);
        result.put("data", branches);
        result.put("success", !(branches == null || branches.isEmpty()));
        return result;
    }


    /**
     * 进行开户
     */
    @RequestMapping(value = "identityAuthentication")
    @ResponseBody
    public Object doOpenAccount(OpenAccount4ApiPersonalDataSource dataSource) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);

        try {
            UserInfoExt currentUser = userInfoExtService.getUserInfoExt4currentUser();
            if (Objects.equals(UserIsVerifiedEnum.BIND.getValue(), currentUser.getIsVerified())) {
                result.put("success", false);
                result.put("error", "用户已开户");
                return result;
            }
            if (!Objects.equals(currentUser.getMobileNo(), dataSource.getMobile_no())) {
                result.put("error", "用户手机号有异常");
                return result;
            }
            dataSource.setMobile_no(currentUser.getMobileNo());
            dataSource.setCertif_tp(HfUtils.CertifTpEnum.ID_CARD.code());
            String password = dataSource.getPassword();
            if (!StringUtils.isNull(password)) {
                password = password.trim();
                dataSource.setPassword(MD5Util.MD5Encode(password, "utf-8"));
            }
            if (!StringUtils.isNull(dataSource.getLpassword())) {
                String lpasswd = dataSource.getLpassword().trim();
                dataSource.setLpassword(MD5Util.MD5Encode(lpasswd, "utf-8"));
            }
            CommonOpenAccount4ApiResponse response = ihfApi.openAccount(dataSource);
            if (!response.isSuccess()) {
                logger.error(response.getResp_desc(), new Exception());
                result.put("error", response.getResp_desc());
            } else {
                logger.info("开户成功：{}", response.toString());
                dataSource.setPassword(password);
                cgBizService.openAccount(dataSource, currentUser);
            }
            result.put("success", response.isSuccess());
        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
            result.put("error", "开户失败");
        }
        return result;
    }

    /**
     * 身份证认证
     *
     * @param idCard
     * @param trueName
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/old/identityAuthentication")
    @ResponseBody
    public Object identityAuthentication(String idCard, String trueName) throws UnsupportedEncodingException {

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
        TokenUtils.validateToken(request);
        try {
            boolean bool = smsService.validateMsg(phone, valid, TemplateType.SMS_MOBILEAUTHENTICATION_VM, true);
            if (bool) {
                UserInfo user = SecurityUtil.getCurrentUser(true);
                //修改手机验证
                user = userInfoService.mobileAuthentValidate(user, phone, UserSource.WECHAT);
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


    @RequestMapping(value = "old/identityAuthenticationBy")
    public String identityAuthenticationBy(String url, Model model) {
        UserInfo user = SecurityUtil.getCurrentUser(true);
        if (user == null) {
            throw new SystemException(UserErrorCode.LONGIN_EXIST);
        }
        //记录验证来源页面标记
        if (!StringUtils.isNull(url)) {
            model.addAttribute("url", url);
        }
        return "realName/realName";
    }

    /**
     * @param
     * @return /person/moreInformatio.jsp
     * @method moreInformation  跳转到个人页面
     * @author:wangyadong
     * @date:2015/09/08 11:24
     */
    @RequestMapping(value = "/moreInformation")
    public String moreInformation(HttpServletRequest request) {
        UserInfo user = SecurityUtil.getCurrentUser(true);
        if (user != null) {
            String mobile = user.getMobileNo();
            user.setMobileNo(mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4, mobile.length()));
            UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
            request.setAttribute("userExt", userExt);
            request.setAttribute("user", user);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", user.getUserId());
            param.put("type", 0);
            InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
            if (invite != null) {
                request.setAttribute("invitecode", invite.getInvitationCode());
                request.setAttribute("name", userExt.getLoginName());
                if (UserIsVerifiedEnum.YES.getValue().equals(userExt.getIsVerified())) {
                    request.setAttribute("name", userExt.getRealName());
                }
            }
            return "person/moreInformation";
        }
        throw new SystemException(UserErrorCode.LONGIN_EXIST);
    }

    /**
     * @param
     * @return /person/personaIlnformation.jsp
     * @method singlePersonInformation  个人详细页面
     * @author:wangyadong
     * @date:2015/09/08 11:46
     */
    @RequestMapping(value = "/singlePersonInformation")
    public String singlePersonInformation(HttpServletRequest request) {
        UserInfo user = SecurityUtil.getCurrentUser(true);
        if (user != null) {
            UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
            String mobile = userExt.getMobileNo();
            userExt.setMobileNo(mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4, mobile.length()));
            request.setAttribute("loginName", user.getLoginName());
            request.setAttribute("userExt", userExt);
            return "person/personInformation";
        }
        throw new SystemException(UserErrorCode.LONGIN_EXIST);
    }

    @RequestMapping(value = "/personInformationVerified")
    public String personInformationVerified(HttpServletRequest request) {
        UserInfo user = SecurityUtil.getCurrentUser(true);
        if (user != null) {
            UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
            String mobile = userExt.getMobileNo();
            userExt.setMobileNo(mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4, mobile.length()));
            request.setAttribute("loginName", user.getLoginName());
            request.setAttribute("userExt", userExt);
            return "person/personInformation2";
        }
        throw new SystemException(UserErrorCode.LONGIN_EXIST);
    }

    /**
     * @param
     * @return /person/aboutus.jsp
     * @method aboutus  关于我们
     * @author:wangyadong
     * @date:2015/09/08 11:49
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/aboutus")
    public String aboutUs(HttpServletRequest requst) {
        return "person/aboutus";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/toEntityShop")
    public String entityShop(HttpServletRequest requst) {
        return "person/entityShop";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/toAppEntityShop")
    public String toAppEntityShop(HttpServletRequest requst) {
        return "person/appEntityShop";
    }

    /**
     * to 我的二维码页面
     *
     * @return
     */
    @RequestMapping(value = "/to_myErweima")
    public String to_myErWeiMa(HttpServletRequest req) {
        UserInfo user = SecurityUtil.getCurrentUser(true);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", user.getUserId());
        param.put("type", 0);
        InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
        if (invite != null) {
            String invitecode = invite.getInvitationCode();
            req.setAttribute("invitecode", invitecode);
            UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
            req.setAttribute("name", userExt.getLoginName());
            if (UserIsVerifiedEnum.YES.getValue().equals(userExt.getIsVerified())) {
                req.setAttribute("name", userExt.getRealName());
            }

        }
        return "/person/myErweima";
    }

    /**
     * 跳转到：忘记登录密码
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/forgetLoginPass")
    public String forgetLoginPass(HttpServletRequest request) {
        return this.toForgetPswFinal(request, "nologin");
    }

    /**
     * 跳转到：重置登录密码
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/resetLoginPass")
    public String resetLoginPass(HttpServletRequest request) {
        return this.toForgetPswFinal(request, "login");
    }

    /**
     * 跳转到：重置交易密码
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/resetTransPass")
    public String resetTransPass(HttpServletRequest request) {
        return this.toForgetPswFinal(request, "trans");
    }


    /**
     * 跳转到：找回密码第一页，验证短信
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/toForgetPswFinal")
    public String toForgetPswFinal(HttpServletRequest request,
                                   @RequestParam(value = "pass_type", required = false) String pass_type) {

        if ("login".equals(pass_type) || "trans".equals(pass_type)) {
            UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
            UserInfo userInfo = userInfoService.getUserByUserId(currentUserInfo.getUserId());
            request.setAttribute("mobile_no", userInfo.getEncryptMobileNo());
            request.setAttribute("mobile", userInfo.getMobileNo());
        }

        request.setAttribute("pass_type", pass_type);
        TokenUtils.setNewToken(request);//【创建Token值】
        return "person/forgetPswFinal";
    }

    /**
     * 执行：找回密码，发送手机验证码
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/forgetPswFinal_sendMsg", method = RequestMethod.POST)
    @ResponseBody
    public Object forgetPswFinal_sendMsg(HttpServletRequest request, HttpSession session,
                                         @RequestParam(value = "pass_type", required = false) String pass_type,
                                         @RequestParam(value = "telNum", required = false) String telNum) {
        try {
            if (null == pass_type || "".equals(pass_type)) {
                return returnResultMap(false, null, null, "缺少参数，非法请求");
            }

            if ("nologin".equals(pass_type)) {
                if (null == telNum || "".equals(telNum)) {
                    return returnResultMap(false, null, null, "手机号码不能为空");
                }
                if (!mobileNoValidate(telNum)) {
                    return returnResultMap(false, null, null, "手机号码格式不正确");
                }
                UserInfo userInfo = userInfoService.getUserByMobileNo(telNum);
                if (null == userInfo) {
                    return returnResultMap(false, null, null, "手机号码不存在");
                }
                userInfoService.sendRetrievePassword(userInfo.getMobileNo());//找回登录密码，短信发送
            } else {
                UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
                UserInfo userInfo = userInfoService.getUserByUserId(currentUserInfo.getUserId());
                if ("login".equals(pass_type)) {
                    userInfoService.sendRetrievePassword(userInfo.getMobileNo());//找回登录密码，短信发送
                } else if ("trans".equals(pass_type)) {
                    userInfoService.sendResetTradePassMsg(userInfo.getMobileNo());//找回交易密码，密码发送
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 执行：找回密码，校验手机验证码
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/forgetPswFinal_checkMsg", method = RequestMethod.POST)
    @ResponseBody
    public Object forgetPswFinal_checkMsg(HttpServletRequest request, HttpSession session,
                                          @RequestParam(value = "pass_type", required = false) String pass_type,
                                          @RequestParam(value = "telNum", required = false) String telNum,
                                          @RequestParam(value = "checkCode", required = false) String checkCode) {
        try {
            if (null == pass_type || "".equals(pass_type)) {
                return returnResultMap(false, null, null, "缺少参数，非法请求");
            }
            if (null == checkCode || "".equals(checkCode)) {
                return returnResultMap(false, null, null, "验证码不能为空");
            }
            if (checkCode.length() != 6 || !StringUtils.isPattern("^([0-9]+)$", checkCode)) {
                return returnResultMap(false, null, null, "验证码格式错误");
            }

            if ("nologin".equals(pass_type)) {
                if (null == telNum || "".equals(telNum)) {
                    return returnResultMap(false, null, null, "手机号码不能为空");
                }
                if (!mobileNoValidate(telNum)) {
                    return returnResultMap(false, null, null, "手机号码格式不正确");
                }
                boolean checkResult = this.checkMsgNum(telNum);//记录短信校验次数
                if (!checkResult) {
                    return returnResultMap(false, null, null, "验证次数过多，请24小时后再操作");
                }
                boolean bool = smsService.validateMsg(telNum, checkCode, TemplateType.SMS_RETRIEVEPASSWORD_VM, true);
                if (!bool) {
                    return returnResultMap(false, null, null, "验证码不正确");
                }
                this.destroyCheckMsgNum(telNum);//销毁短信校验次数记录

                //dtoken【开始】（1存）
                String desc_mobile_no = Des3.encode(telNum);
                return returnResultMap(true, desc_mobile_no, null, null);
                //dtoken【结束】
            } else {
                UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
                UserInfo userInfo = userInfoService.getUserByUserId(currentUserInfo.getUserId());
                boolean checkResult = this.checkMsgNum(userInfo.getMobileNo());//记录短信校验次数
                if (!checkResult) {
                    return returnResultMap(false, null, null, "验证次数过多，请24小时后再操作");
                }
                if ("login".equals(pass_type)) {
                    boolean bool = smsService.validateMsg(userInfo.getMobileNo(), checkCode, TemplateType.SMS_RETRIEVEPASSWORD_VM, true);
                    if (!bool) {
                        return returnResultMap(false, null, null, "验证码不正确");
                    }
                } else if ("trans".equals(pass_type)) {
                    boolean bool = smsService.validateMsg(userInfo.getMobileNo(), checkCode, TemplateType.SMS_RESETTRADEPASSMSG_VM, true);
                    if (!bool) {
                        return returnResultMap(false, null, null, "验证码不正确");
                    }
                }
                this.destroyCheckMsgNum(userInfo.getMobileNo());//销毁短信校验次数记录
            }

        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 记录短信校验次数
     */
    private boolean checkMsgNum(String mobile_no) throws ParseException {
        int seconds = DateUtil.getLeftTimesToday();//当天剩余时间，秒
        Integer num = 0;
        String numStr = redisCacheManger.getRedisCacheInfo("wechat_forgetPsw_" + mobile_no);
        if (null != numStr) {
            num = Integer.valueOf(numStr);
        }
        num = num + 1;
        redisCacheManger.setRedisCacheInfo("wechat_forgetPsw_" + mobile_no, String.valueOf(num), seconds);//key=mobile_no;value=checkMsgNum
        if (num >= 10) {
            return false;
        }
        return true;
    }

    /**
     * 销毁短信校验次数记录
     */
    private void destroyCheckMsgNum(String mobile_no) {
        redisCacheManger.destroyRedisCacheInfo("wechat_forgetPsw_" + mobile_no);
    }

    /**
     * 跳转到：找回密码第二页，身份验证
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/toIdentityProving")
    public String toIdentityProving(HttpServletRequest request,
                                    @RequestParam(value = "pass_type", required = false) String pass_type,
                                    @RequestParam(value = "mobile_no", required = false) String mobile_no,
                                    @RequestParam(value = "dtoken", required = false) String dtoken) {

        TokenUtils.validateToken(request);//【验证Token值】
        request.setAttribute("pass_type", pass_type);
        request.setAttribute("mobile_no", mobile_no);
        TokenUtils.setNewToken(request);//【创建Token值】

        UserInfo userInfo = null;
        if ("nologin".equals(pass_type)) {
            //dtoken【开始】（1验2存）
            try {
                if (null == mobile_no || "".equals(mobile_no) || null == dtoken || "".equals(dtoken)) {
                    throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
                }
                String des3_mobile_no = Des3.encode(mobile_no);
                if (!des3_mobile_no.equals(dtoken)) {
                    throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
                }
                request.setAttribute("dtoken", des3_mobile_no);
            } catch (Exception e) {
                throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
            }
            //dtoken【结束】
            userInfo = userInfoService.getUserByMobileNo(mobile_no);
        } else {
            userInfo = SecurityUtil.getCurrentUser(true);
        }

        UserInfoExt userExt = userInfoExtService.getUserInfoExtById(userInfo.getUserId());
        if (!UserIsVerifiedEnum.YES.getValue().equals(userExt.getIsVerified())) {
            return "person/installNewPassword";//未身份认证，直接进入密码重置页
        }
        return "person/identityProving";
    }

    /**
     * 执行：找回密码，身份验证
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/identityProving_authentication")
    @ResponseBody
    public Object identityProving_authentication(HttpServletRequest request, HttpSession session,
                                                 @RequestParam(value = "pass_type", required = false) String pass_type,
                                                 @RequestParam(value = "idcard", required = false) String idcard,
                                                 @RequestParam(value = "mobile_no", required = false) String mobile_no,
                                                 @RequestParam(value = "dtoken", required = false) String dtoken) {
        try {
            if (null == pass_type || "".equals(pass_type)) {
                return returnResultMap(false, null, null, "缺少参数，非法请求");
            }
            if (null == idcard || "".equals(idcard)) {
                return returnResultMap(false, null, null, "身份证为空");
            }
            if (!idCardValidate(idcard)) {
                return returnResultMap(false, null, null, "身份证格式不正确");
            }

            if ("nologin".equals(pass_type)) {
                if (null == mobile_no || "".equals(mobile_no)) {
                    return returnResultMap(false, null, null, "手机号码不能为空");
                }
                if (!mobileNoValidate(mobile_no)) {
                    return returnResultMap(false, null, null, "手机号码格式不正确");
                }
                //dtoken【开始】（2验）
                try {
                    if (null == dtoken || "".equals(dtoken)) {
                        throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
                    }
                    String des3_mobile_no = Des3.encode(mobile_no);
                    if (!des3_mobile_no.equals(dtoken)) {
                        throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
                    }
                } catch (Exception e) {
                    throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
                }
                //dtoken【结束】
                UserInfo userInfo = userInfoService.getUserByMobileNo(mobile_no);
                if (null == userInfo) {
                    return returnResultMap(false, null, null, "手机号码不存在");
                }
                UserInfoExt userExt = userInfoExtService.getUserInfoExtById(userInfo.getUserId());
                if (!UserIsVerifiedEnum.YES.getValue().equals(userExt.getIsVerified())) {
                    return returnResultMap(false, null, null, "还未身份认证");
                }
                if (!userExt.getIdCard().equals(idcard)) {
                    return returnResultMap(false, null, null, "身份证号未通过验证");
                }
            } else {
                UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
                UserInfoExt userExt = userInfoExtService.getUserInfoExtById(currentUserInfo.getUserId());
                if (!UserIsVerifiedEnum.YES.getValue().equals(userExt.getIsVerified())) {
                    return returnResultMap(false, null, null, "还未身份认证");
                }
                if (!userExt.getIdCard().equals(idcard)) {
                    return returnResultMap(false, null, null, "身份证号未通过验证");
                }
            }
        } catch (SystemException se) {
            se.printStackTrace();
            return returnResultMap(false, null, null, se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 跳转到：找回密码第三页，新密码设置
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/toInstallNewPassword")
    public String toInstallNewPassword(HttpServletRequest request,
                                       @RequestParam(value = "pass_type", required = false) String pass_type,
                                       @RequestParam(value = "mobile_no", required = false) String mobile_no,
                                       @RequestParam(value = "dtoken", required = false) String dtoken) {

        TokenUtils.validateToken(request);//【验证Token值】
        request.setAttribute("pass_type", pass_type);
        request.setAttribute("mobile_no", mobile_no);
        TokenUtils.setNewToken(request);//【创建Token值】

        if ("nologin".equals(pass_type)) {
            //dtoken【开始】（2.2验3存）
            try {
                if (null == mobile_no || "".equals(mobile_no) || null == dtoken || "".equals(dtoken)) {
                    throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
                }
                String des3_mobile_no = Des3.encode(mobile_no);
                if (!des3_mobile_no.equals(dtoken)) {
                    throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
                }
                request.setAttribute("dtoken", des3_mobile_no);
            } catch (Exception e) {
                throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
            }
            //dtoken【结束】
        } else {
            SecurityUtil.getCurrentUser(true);
        }

        return "person/installNewPassword";
    }

    /**
     * 执行：找回密码，保存新密码
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/installNewPassword_savePass", method = RequestMethod.POST)
    @ResponseBody
    public Object installNewPassword_savePass(HttpServletRequest request, HttpSession session,
                                              @RequestParam(value = "pass_type", required = false) String pass_type,
                                              @RequestParam(value = "mobile_no", required = false) String mobile_no,
                                              @RequestParam(value = "dtoken", required = false) String dtoken,
                                              @RequestParam(value = "newPass", required = false) String newPass) {
        try {
            if (null == pass_type || "".equals(pass_type)) {
                return returnResultMap(false, null, null, "缺少参数，非法请求");
            }
            if (null == newPass || "".equals(newPass)) {
                return returnResultMap(false, null, null, "密码不能为空");
            }
            if (!passwordValidate(newPass)) {
                return returnResultMap(false, null, null, "密码为6-16位字符，支持字母及数字,字母区分大小写");
            }

            if ("nologin".equals(pass_type)) {
                if (null == mobile_no || "".equals(mobile_no)) {
                    return returnResultMap(false, null, null, "手机号码不能为空");
                }
                if (!mobileNoValidate(mobile_no)) {
                    return returnResultMap(false, null, null, "手机号码格式不正确");
                }

                //dtoken【开始】（3验）
                try {
                    if (null == mobile_no || "".equals(mobile_no) || null == dtoken || "".equals(dtoken)) {
                        throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
                    }
                    String des3_mobile_no = Des3.encode(mobile_no);
                    if (!des3_mobile_no.equals(dtoken)) {
                        throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
                    }
                } catch (Exception e) {
                    throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
                }
                //dtoken【结束】

                UserInfo userInfo = userInfoService.getUserByMobileNo(mobile_no);
                if (null == userInfo) {
                    return returnResultMap(false, null, null, "手机号码不存在");
                }
                userInfo = userInfoService.getUserByUserId(userInfo.getUserId());
                String md5_login_new = MD5Util.MD5Encode(newPass.trim(), "utf-8");
                userInfo.setLoginPass(md5_login_new);
                userInfoService.updateUser(userInfo);
            } else {
                UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
                UserInfo userInfo = userInfoService.getUserByUserId(currentUserInfo.getUserId());
                if ("login".equals(pass_type)) {
                    String md5_login_new = MD5Util.MD5Encode(newPass.trim(), "utf-8");
                    userInfo.setLoginPass(md5_login_new);
                } else if ("trans".equals(pass_type)) {
                    String md5_trade_new = MD5Util.MD5Encode(newPass.trim(), "utf-8");
                    userInfo.setBidPass(md5_trade_new);
                }
                userInfoService.updateUser(userInfo);
                request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, userInfo);//覆盖用户登录信息
            }
        } catch (SystemException se) {
            se.printStackTrace();
            return returnResultMap(false, null, null, se.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }

    /**
     * 跳转到：找回密码完成，返回来源页面
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/finishInstallNewPassword")
    public String finishInstallNewPassword(HttpServletRequest request,
                                           @RequestParam(value = "pass_type", required = false) String pass_type) {
        TokenUtils.validateToken(request);//【验证Token值】
        if ("login".equals(pass_type) || "trans".equals(pass_type)) {
            SecurityUtil.getCurrentUser(true);
            return "redirect:/person/moreInformation";
        } else {
            return "redirect:/";
        }
    }


    /**
     * 跳转到:我的理财-省心计划列表页
     *
     * @return
     */
    @RequestMapping(value = "/toAllMyFinanceList")
    public String toAllMyFinanceList(HttpServletRequest request) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);

        // 提供“计划类型”查询条件，即选择值
        List<Integer> timeLimitList = lendProductService.getLendProductTimeLimitByLendUserId(currentUser.getUserId());
        request.setAttribute("timeLimitList", timeLimitList);

        BigDecimal totalValue = BigDecimal.ZERO;//【存】省心账户总资产
        BigDecimal forLendBalance = BigDecimal.ZERO;//【存】待出借金额
        BigDecimal waitInterest = BigDecimal.ZERO;//【存】在投预期收益
        BigDecimal currentProfit = BigDecimal.ZERO;//【存】已获收益
        Integer allOrderSize = 0;//【存】累计参与计划总数
        Integer financingOrderSize = 0;//【存】理财中计划总数
        BigDecimal awardBalance = BigDecimal.ZERO;//【存】已获奖励
        BigDecimal financingValue = userAccountService.getUserFinancingAccountValueByUserId(currentUser.getUserId());//【存】理财中金额

        // 获取用户所有[省心订单]列表
        List<LendOrder> financeOrderList = lendOrderService.getFinancialPlanListByUserId(currentUser.getUserId());
        for (LendOrder finance : financeOrderList) {
            totalValue = totalValue.add(finance.getBuyBalance().add(finance.getCurrentProfit()));
            currentProfit = currentProfit.add(finance.getCurrentProfit());
            // 状态为理财中或者授权期结束的
            if (FinanceOrderStatusEnum.REPAYMENTING.getValue().equals(finance.getOrderState())
                    || FinanceOrderStatusEnum.QUITING.getValue().equals(finance.getOrderState())) {
                financingOrderSize++;
            }

            // 查询某条省心计划，已经匹配到子标的已获奖励
            awardBalance = awardBalance.add(userAccountService.getUserTotalFinanceAwardByLendOrderId(finance.getLendOrderId()));
        }
        //获取用户所有省心账户-计算省心账户子标所有扣费
        BigDecimal financeFees = financePlanProcessModule.getAllFeesByUserId(currentUser.getUserId());
        // 获取用户省心订单的待回利息  需要加预期奖励
        waitInterest = lendOrderService.getFinancialWaitInterestByUserId(currentUser.getUserId());
        BigDecimal totalAward = creditorRightsService.getTotalExpectAwardByUserId(currentUser.getUserId());
        BigDecimal waitAward = totalAward.subtract(awardBalance);
        //理财中金额需要加奖励
        financingValue = financingValue.add(waitAward);

        allOrderSize = financeOrderList.size();
        // 获取用户所有[省心账户]列表
        List<UserAccount> userAccountList = userAccountService.getUserFinanceAccount(currentUser.getUserId());
        for (UserAccount userAccount : userAccountList) {
            forLendBalance = forLendBalance.add(userAccount.getAvailValue2()).add(userAccount.getFrozeValue2());
        }
        //用户省心账户转出资金（即退出转账）
        BigDecimal financeAccountTurnValue = userAccountService.getFinanceAccountPayValue(currentUser.getUserId());
        // [统计值]省心账户总资产
        request.setAttribute("totalValue", BigDecimalUtil.down(totalValue.add(waitInterest).add(totalAward).subtract(financeAccountTurnValue).subtract(financeFees), 2));
        // [统计值]理财中金额
        request.setAttribute("financingValue", BigDecimalUtil.down(financingValue, 2));
        // [统计值]待出借金额
        request.setAttribute("forLendBalance", BigDecimalUtil.down(forLendBalance, 2));
        //增加奖励
        waitInterest = waitInterest.add(waitAward);
        // [统计值]在投预期收益
        request.setAttribute("waitInterest", BigDecimalUtil.down(waitInterest, 2));
        // [统计值]已获收益 + 已获奖励
        request.setAttribute("currentProfit", BigDecimalUtil.down(currentProfit.add(awardBalance), 2));
        // [统计值]理财中计划总数
        request.setAttribute("financingOrderSize", financingOrderSize);
        // [统计值]累计参与计划总数
        request.setAttribute("allOrderSize", allOrderSize);

        return "/person/mysx";
    }

    /**
     * 我的理财-省心计划的列表数据
     *
     * @param request
     * @param pageSize
     * @param pageNo
     * @param queryState 省心计划状态（已支付0，理财中1，已结束2）
     * @param queryType  计划类型（省心期数值）
     * @return
     */
    @RequestMapping(value = "/getAllMyFinanceList")
    @ResponseBody
    public Object getAllMyFinanceList(HttpServletRequest request,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                      @RequestParam(value = "queryState", defaultValue = "") String queryState,
                                      @RequestParam(value = "queryType", defaultValue = "") String queryType) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        return lendOrderService.getAllMyFinanceList(pageSize, pageNo, currentUser.getUserId(), queryState, queryType);
    }

    /**
     * 修改手机号
     *
     * @param request
     * @return
     */
//    @RequestMapping(value = "/toUpdateMobile")
    public String toUpdateMobile(HttpServletRequest request) {

        return "/person/updateMobile";
    }

//    @RequestMapping(value = "/updateMobile")
    @ResponseBody
    public String updateMobile(HttpServletRequest request, String valid, String phone) {
        try {
            boolean bool = smsService.validateMsg(phone, valid, TemplateType.SMS_MOBILEAUTHENTICATION_VM, true);
            if (bool) {
                UserInfo user = SecurityUtil.getCurrentUser(true);
                /*UserInfo userNew = userInfoService.getUserByUserId(user.getUserId());
                if(!user.getMobileNo().equals(userNew.getMobileNo())){
                    request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, userNew);
                    return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.USER_NOT_EXIST.getDesc()).put("id", "valid").toJson();
                }*/
                boolean mobileExist = userInfoService.isMobileExist(phone);
                if (mobileExist) {
                    return JsonView.JsonViewFactory.create().success(false).info("与原手机号相同或手机号已被使用").put("id", "mobileNo").toJson();
                }
                //修改手机验证
                user = userInfoService.mobileAuthentValidate(user, phone, UserSource.WECHAT);
                //更新一下session的数据
                request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, user);
                return JsonView.JsonViewFactory.create().success(true).info("").put("id", "valid").toJson();
            } else
                return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.VALIDATE_CODE_ERROR.getDesc()).put("id", "valid").toJson();
        } catch (SystemException se) {
            logger.error(se.getMessage(), se);
            return JsonView.JsonViewFactory.create().success(false).info(se.getMessage()).put("id", "valid").toJson();
        }
    }

    /**
     * 修改手机验证发送验证码
     *
     * @param mobileNo
     * @return
     */
    @RequestMapping(value = "/updateMobileMsgSend")
    @ResponseBody
    public String updateMobileMsgSend(String mobileNo) {
        try {
            //验证手机号格式
            if (!mobileNoValidate(mobileNo)) {
                return JsonView.JsonViewFactory.create().success(false).info("手机号码格式错误").put("id", "mobileNo").toJson();
            }
            boolean mobileExist = userInfoService.isMobileExist(mobileNo);
            if (mobileExist) {
                return JsonView.JsonViewFactory.create().success(false).info("与原手机号相同或手机号已被使用").put("id", "mobileNo").toJson();
            }
            userInfoService.sendmobileAuthenticationMsg(mobileNo);
            return JsonView.JsonViewFactory.create().success(true).info("").toJson();
        } catch (SystemException se) {
            logger.error(se.getMessage(), se);
            return JsonView.JsonViewFactory.create().success(false).info("").toJson();
        }
    }

    @RequestMapping(value = "/toUpdateLoginName")
    public String toUpdateLoginName(HttpServletRequest request) {

        return "/person/updateLoginName";
    }

    @RequestMapping(value = "/updateLoginName")
    @ResponseBody
    public String updateLoginName(HttpServletRequest request, String loginName) {
        // 获取当前登录用户
        UserInfo currentUserInfo = SecurityUtil.getCurrentUser(true);
        // 根据用户ID，加载一条数据
        UserInfo userInfo = userInfoService.getUserByUserId(currentUserInfo.getUserId());

        // 判断用户名是否已经存在，非本人
        UserInfo info = userInfoService.getUserByLoginName(loginName);
        if (null != info && !userInfo.getLoginName().equals(info.getLoginName())) {
            return JsonView.JsonViewFactory.create().success(false).info("用户名已经存在").put("id", "loginName").toJson();
        }

        userInfo.setLoginName(loginName);
        userInfoService.updateUser(userInfo);
        // 覆盖用户登录信息
        request.getSession().setAttribute(Constants.USER_ID_IN_SESSION, userInfo);
        return JsonView.JsonViewFactory.create().success(true).info("").toJson();
    }
}
