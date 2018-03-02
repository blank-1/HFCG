package com.xt.cfp.wechat.controller;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.impl.CgBizService;
import jodd.util.NameValue;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.external.llpay.LLPayUtil;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.BidErrorCode;
import com.xt.cfp.core.Exception.code.ext.CreditorErrorCode;
import com.xt.cfp.core.Exception.code.ext.PayErrorCode;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.AttachmentIsCode;
import com.xt.cfp.core.constants.ChannelTypeEnum;
import com.xt.cfp.core.constants.ClientEnum;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.constants.CreditorRightsConstants;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.RightsRepaymentDetailIsPayOffEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.RightsRepaymentDetailStateEnum;
import com.xt.cfp.core.constants.CreditorRightsFromWhereEnum;
import com.xt.cfp.core.constants.DisplayEnum;
import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.constants.GuaranteeTypeEnum;
import com.xt.cfp.core.constants.LendOrderBidStatusEnum;
import com.xt.cfp.core.constants.LendOrderConstants;
import com.xt.cfp.core.constants.LendOrderConstants.FinanceProfitReturnEnum;
import com.xt.cfp.core.constants.LendProductPublishStateEnum;
import com.xt.cfp.core.constants.LendProductTimeLimitType;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.constants.LoanTypeEnum;
import com.xt.cfp.core.constants.PayConstants;
import com.xt.cfp.core.constants.PaymentMethodEnum;
import com.xt.cfp.core.constants.PublishRule;
import com.xt.cfp.core.constants.PublishTarget;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.constants.RateEnum.RateProductIsOverlayEnum;
import com.xt.cfp.core.constants.RateEnum.RateProductScenarioEnum;
import com.xt.cfp.core.constants.RateEnum.RateUserStatusEnum;
import com.xt.cfp.core.constants.RepaymentPlanStateEnum;
import com.xt.cfp.core.constants.SpecialBiddingEnum;
import com.xt.cfp.core.constants.UserIsVerifiedEnum;
import com.xt.cfp.core.constants.UserSource;
import com.xt.cfp.core.constants.VoucherConstants;
import com.xt.cfp.core.pojo.ext.CreditorRightsExt;
import com.xt.cfp.core.pojo.ext.CreditorRightsExtVo;
import com.xt.cfp.core.pojo.ext.CustomerUploadSnapshotVO;
import com.xt.cfp.core.pojo.ext.EnterpriseUploadSnapshotVO;
import com.xt.cfp.core.pojo.ext.LendOrderExtProduct;
import com.xt.cfp.core.pojo.ext.LendProductPublishVO;
import com.xt.cfp.core.pojo.ext.LenderRecordVO;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.pojo.ext.LproductWithBalanceStatus;
import com.xt.cfp.core.pojo.ext.PayResult;
import com.xt.cfp.core.pojo.ext.PaymentDayListDataVO;
import com.xt.cfp.core.pojo.ext.PaymentDaySummaryDataVO;
import com.xt.cfp.core.pojo.ext.PaymentMonthListDataVO;
import com.xt.cfp.core.pojo.ext.RateUserVO;
import com.xt.cfp.core.pojo.ext.RightsRepaymentDetailExt;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.pojo.ext.VoucherProductVO;
import com.xt.cfp.core.pojo.ext.VoucherVO;
import com.xt.cfp.core.service.financePlan.FinancePlanProcessModule;
import com.xt.cfp.core.service.financePlan.FinancePlanService;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.InterestCalculation;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.LogUtils;
import com.xt.cfp.core.util.MD5Util;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.PropertiesUtils;
import com.xt.cfp.core.util.RequestUtil;
import com.xt.cfp.core.util.SecurityUtil;
import com.xt.cfp.core.util.TokenUtils;
import com.xt.cfp.core.util.ValidationUtil;
import com.xt.cfp.core.util.WebUtil;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;


/**
 * Created by yulei on 2015/7/2.
 */
@Controller
@RequestMapping(value = "/finance")
public class FinanceController extends BaseController {

    private static Logger logger = Logger.getLogger(FinanceController.class);
    @Autowired
    private LendProductService lendProductService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private CustomerBasicSnapshotService customerBasicSnapshotService;
    @Autowired
    private CustomerHouseSnapshotService customerHouseSnapshotService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private CreditorRightsService creditorRightsService;
    @Autowired
    private LoanPublishService loanPublicService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private LoanProductService loanProductService;
    @Autowired
    private EnterpriseInfoService enterpriseInfoService;
    @Autowired
    private EnterpriseCarLoanSnapshotService enterpriseCarLoanSnapshotService;
    @Autowired
    private ProvinceInfoService provinceInfoService;
    @Autowired
    private CityInfoService cityInfoService;
    @Autowired
    private MortgageCarSnapshotService mortgageCarSnapshotService;
    @Autowired
    private EnterpriseCreditSnapshotService enterpriseCreditSnapshotService;
    @Autowired
    private EnterpriseFoundationSnapshotService enterpriseFoundationSnapshotService;
    @Autowired
    private CoLtdService coLtdService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private EnterpriseLoanApplicationService enterpriseLoanApplicationService;
    @Autowired
    private EnterpriseFactoringSnapshotService enterpriseFactoringSnapshotService;
    @Autowired
    private EnterprisePledgeSnapshotService enterprisePledgeSnapshotService;

    @Autowired
    private ConstantDefineCached constantDefineCached;
    @Autowired
    private LendLoanBindingService lendLoanBindingService;
    @Autowired
    private FeesItemService feesItemService;
    @Autowired
    private RightsRepaymentDetailService rightsRepaymentDetailService;
    @Autowired
    private LendOrderReceiveService lendOrderReceiveService;
    @Autowired
    private DefaultInterestDetailService defaultInterestDetailService;

    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private CreditorRightsTransferAppService creditorRightsTransferAppService;
    @Autowired
    private GuaranteeCompanyService guaranteeCompanyService;
    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private RateLendOrderService rateLendOrderService;
    @Autowired
    private RateUserService rateUserService;
    @Autowired
    private RateProductService rateProductService;
    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    @Autowired
    private FinancePlanService financePlanService;
    @Autowired
    private RepaymentRecordService repaymentRecordService;
    @Autowired
    private LoanFeesDetailService loanFeesDetailService;
    @Autowired
    private CustomerCarSnapshotService customerCarSnapshotService;
    @Autowired
    private CgBizService bizService;

    /**
     * 进入理财产品列表
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/list")
    public String toFinanceList(HttpServletRequest request, HttpServletResponse response) {

        // 设置理财还是出借选项页默认选中
        if ("zhuang".equals(request.getParameter("tab"))) {
            request.setAttribute("tab", "zhuang");//标
        } else if ("shengxin".equals(request.getParameter("tab"))) {//省心计划
            request.setAttribute("tab", "shengxin");//省心计划
        } else {//转让
            request.setAttribute("tab", "");
        }
        return "/finance/financeList";
    }


    /**
     * 进入新手标列表
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/newDiddingList")
    public String newDiddingList(HttpServletRequest request, HttpServletResponse response) {
        //判断是否为新手项目
        request.setAttribute("flagCustomer", "new");
        return "/finance/financeListNew";
    }


    /**
     * 债权的预期收益
     *
     * @param creditorRightsApplyId
     * @param amount
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/getExpectRightProfit")
    @ResponseBody
    public Object getExpectRightProfit(Long creditorRightsApplyId, BigDecimal amount) {
        String result = creditorRightsService.getExpectRightProfit(creditorRightsApplyId, amount);
        String[] split = result.split(",");
        if (split.length > 1) {
            String replaceAll = result.replaceAll(",", "+");
            return replaceAll + "(奖励)";
        }
        return result;
    }

    /**
     * 债权市场列表
     * wangyadong add
     *
     * @return
     * @date
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/creditorRightsList")
    @ResponseBody
    public Object creditorRightsList(HttpServletRequest request,
                                     LoanApplicationListVO loanApplicationListVO,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {

        Pagination<LoanApplicationListVO> loanApplicationListPaging = loanApplicationService.getTurnCreditRightPaging(pageNo, pageSize, loanApplicationListVO, null);
        return loanApplicationListPaging;
    }

    @RequestMapping(value = "toPayFinanceOrder")
    public String toPayFinanceOrder(HttpServletRequest request,
                                    @RequestParam(value = "lendOrderId") Long lendOrderId, String sxFlag) {
        TokenUtils.setNewToken(request);
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        //身份验证
        if (!userExt.getIsVerified().equals("1")) {
            throw new SystemException(UserErrorCode.HAS_NOT_IDVERIFIED).set("userId", currentUser.getUserId());
        }
        //校验-订单是否存在
        if (lendOrder == null) {
            throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderId", lendOrderId);
        }

        //校验-订单用户和当前用户必须相同
        if (!lendOrder.getLendUserId().equals(currentUser.getUserId())) {
            throw new SystemException(PayErrorCode.NOT_BELONG_PAY_ORDER).set("lendOrderUserId", lendOrder.getLendUserId()).set("user", currentUser.getUserId());
        }

        if (!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
            throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
        LendProductPublish lendProductPublish = lendProductService.getLendProductPublishByPublishId(lendOrder.getLendProductPublishId());
        LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());

        //校验业务数据
        checkForToBuyFinanaceByPayAmount(currentUser.getUserId(), lendProductPublish, lendOrder.getBuyBalance());
        //获取理财信息明细
        LproductWithBalanceStatus financeDetail = this.lendProductService.findFinanceProductDetailForWeb(lendProductPublish.getLendProductPublishId());

        //查询用户卡信息
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
        if (customerCard != null && null != customerCard.getMobile())
            customerCard.setMobile(customerCard.getMobile().substring(0, 3) + "*****" + customerCard.getMobile().substring(8));

        //预计收益
        BigDecimal interesting = InterestCalculation.getExpectedInteresting(lendOrder.getBuyBalance(), lendProduct.getProfitRate(), lendProduct.getTimeLimitType().toCharArray()[0], lendProduct.getTimeLimit());

        //财富券
        List<VoucherVO> vouchers = voucherService.getAllVoucherList(currentUser.getUserId(), lendOrder.getBuyBalance(), VoucherConstants.UsageScenario.WHOLE, VoucherConstants.UsageScenario.FINANCE);

        request.setAttribute("sxFlag", sxFlag);
        request.setAttribute("userExtInfo", userExt);
        request.setAttribute("order", lendOrder);
        request.setAttribute("productDetail", financeDetail);
        request.setAttribute("amount", lendOrder.getBuyBalance());
        request.setAttribute("truePayAmount", lendOrder.getBuyBalance());
        request.setAttribute("customerCard", customerCard);
        request.setAttribute("vouchers", vouchers);
        request.setAttribute("interesting", interesting);
        request.setAttribute("lendProduct", lendProduct);
        request.setAttribute("userExtInfo", userExt);
        request.setAttribute("lendProductPublish", lendProductPublish);
        request.setAttribute("userCashBalance", userAccountService.getCashAccount(currentUser.getUserId()).getAvailValue2());
        boolean isBidEqualLoginPass = userInfoService.isBidPassEqualLoginPass(currentUser.getUserId());
        request.setAttribute("isBidEqualLoginPass", isBidEqualLoginPass);
        /*if (customerCard == null)
            return "order/noCardBidLoanPay";
		else
			return "order/hasCardBidLoanPay";*/
        return "order/payForOrder";
    }

    /**
     * 执行：立即支付事件
     *
     * @param loanApplicationId
     * @param amount
     * @param request
     * @return
     */
    @RequestMapping(value = "toBuyBidLoanByPayAmount")
    public String toBuyBidLoanByPayAmount(Long loanApplicationId,
                                          BigDecimal amount,
                                          String targetPass,
                                          HttpServletRequest request) {
        if (loanApplicationId == null) {
            return "/error";
        }
        try {
            //TokenUtils.validateToken(request);
            UserInfo currentUser = SecurityUtil.getCurrentUser(true);

            //【验证是否为定向标-开始】
            int oType = loanApplicationService.getLoanApplicationType(loanApplicationId);//根据借款申请ID，查询该标定向类型（1密码，2用户）
            if (oType == 1) {//定向密码
                request.setAttribute("loanApplicationId", loanApplicationId);
                request.setAttribute("amount", amount);
                if (null == targetPass || "".equals(targetPass)) {
                    return "/finance/financialPsw";
                } else {//服务器端，再次验证定向密码
                    String pass = loanApplicationService.getLoanApplicationPass(loanApplicationId);
                    String passwordCiphertext = MD5Util.MD5Encode(targetPass, "utf-8");
                    if (!pass.equals(passwordCiphertext)) {
                        return "/finance/financialPsw";
                    }
                }
            }
            //【验证是否为定向标-结束】


            //【验证是否为定向标-开始】
//		String Spath = chioceWhichReturnBySpecialLoanApplication(
//				currentUser.getUserId(), loanApplicationId, targetPass,
//				request);
//		if (!"".equals(Spath))
//			return Spath;
            //【验证是否为定向标-结束】

            LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(loanApplicationId);
            LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationId);

            LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());
            //校验业务数据
            checkBidLoanByPay(loanApplicationListVO, amount, currentUser);
            //检查剩余金额
            BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
            if (amount.compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy)) > 0) {
                //订单金额为剩余金额
                if (loanApplicationListVO.getConfirmBalance().compareTo(totalBuy) == 0) {
                    throw new SystemException(BidErrorCode.LENDORDER_AMOUNT_ZONE).set("amount", 0);
                }
                throw new SystemException(BidErrorCode.LENDORDER_AMOUNT_NOT_ENOUGH);
                //			amount = loanApplicationListVO.getConfirmBalance().subtract(totalBuy);
            }

            //100倍数验证
            if (amount.remainder(new BigDecimal("100")).longValue() != 0) {
                throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
            }

            //新建投标订单
            LendOrder lendOrder = lendOrderService.addLoanOrder(currentUser.getUserId(), loanApplicationId, lendProductPublish.getLendProductPublishId(), amount, new Date(), lendProduct, PropertiesUtils.getInstance().get(ClientEnum.from(currentUser.getAppSource()).getResource()));
            //购买理财
            return "redirect:/finance/toPayForLender?productType=1&lendOrderId=" + lendOrder.getLendOrderId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            request.setAttribute("errorMsg", e.getMessage());
        }
        return "/error";
    }


    /**
     * 执行：立即支付事件
     *
     * @param loanApplicationId
     * @param amount
     * @param request
     * @return
     * @author wangyadong
     */
    @RequestMapping(value = "toBuyBidCreditorByPayAmount")
    public String toBuyBidCreditorByPayAmount(Long loanApplicationId,
                                              BigDecimal amount,
                                              String targetPass,
                                              HttpServletRequest request) {
        if (loanApplicationId == null) {
            return "/error";
        }
        //TokenUtils.validateToken(request);
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);

        LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(loanApplicationId);
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationId);

        LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());
        //校验业务数据
        checkBidLoanByPayCredit(loanApplicationListVO, amount, currentUser);
        //检查剩余金额
        BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
        if (amount.compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy)) > 0) {
            //订单金额为剩余金额
            if (loanApplicationListVO.getConfirmBalance().compareTo(totalBuy) == 0) {
                throw new SystemException(BidErrorCode.LENDORDER_AMOUNT_ZONE).set("amount", 0);
            }
            amount = loanApplicationListVO.getConfirmBalance().subtract(totalBuy);
        }

        //100倍数验证
        if (amount.remainder(new BigDecimal("100")).longValue() != 0) {
            throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
        }

        //新建投标订单
        LendOrder lendOrder = lendOrderService.addLoanOrder(currentUser.getUserId(),
                loanApplicationId, lendProductPublish.getLendProductPublishId(), amount,
                new Date(), lendProduct, PropertiesUtils.getInstance().get(ClientEnum.from(currentUser.getAppSource()).getResource()));

        // 新建投标订单
//		LendOrder lendOrder1 = lendOrderService.addCreditorRightsOrder(currentUser.getUserId(), creditorRights.getLoanApplicationId(),
//				lendProductPublish.getLendProductPublishId(), amount, new Date(), lendProduct, PropertiesUtils.getInstance().get("SOURCE_PC"), crta);
        //购买理财

        return "redirect:/finance/toPayRightsOrder?lendOrderId=" + lendOrder.getLendOrderId();
    }


    @RequestMapping(value = "checkForToBuyFinanaceByPayAmount")
    @ResponseBody
    public String checkForToBuyFinanaceByPayAmount(HttpServletRequest request,
                                                   @RequestParam(value = "lendOrderId") Long lendOrderId) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);

        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        if (!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
            throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
        //校验-订单用户和当前用户必须相同
        if (!lendOrder.getLendUserId().equals(currentUser.getUserId())) {
            return JsonView.JsonViewFactory.create().success(false).info(PayErrorCode.NOT_BELONG_PAY_ORDER.getDesc())
                    .toJson();
        }
        LendProductPublish lendProductPublish = lendProductService.getLendProductPublishByPublishId(lendOrder.getLendProductPublishId());
        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        //校验身份认证状态
        if (!this.userInfoService.hasIdentityAuthentication(userByUserId.getUserId()))
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.HAS_NOT_IDVERIFIED.getDesc())
                    .toJson();


        //todo 理财产品状态验证
        if (lendProductPublish.getPublishState() != LendProductPublishStateEnum.SELLING.value2Char()) {
            throw new SystemException(BidErrorCode.FINANACE_STATUS_NOT_SELLING);
        }


        //校验金额是否合法
        BigDecimal waitingBuyBalance = lendProductPublish.getPublishBalance().subtract(lendProductPublish.getSoldBalance());
        if (lendOrder.getBuyBalance().compareTo(waitingBuyBalance) > 0)
            return JsonView.JsonViewFactory.create().success(false).info("购买金额已超出剩余可购买金额")
                    .toJson();

        return JsonView.JsonViewFactory.create().success(true).toJson();
    }

    /**
     * 进入购买省心计划支付页面
     *
     * @param lendProductPublishId
     * @param amount
     * @return
     */
    @RequestMapping(value = "toBuyFinanceByPayAmount")
    public String toBuyFinanceByPayAmount(@RequestParam(value = "lendProductPublishId") Long lendProductPublishId,
                                          @RequestParam(value = "amount") BigDecimal amount,
                                          @RequestParam(value = "isUseVoucher", required = false) String isUseVoucher,
                                          @RequestParam(value = "profitReturnConfig", required = false) String profitReturnConfig,
                                          Model model) {
        try {
            UserInfo currentUser = SecurityUtil.getCurrentUser(true);
            LendProductPublish lendProductPublish = lendProductService.getLendProductPublishByPublishId(lendProductPublishId);
            LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());
            //校验业务数据
            checkForToBuyFinanaceByPayAmount(currentUser.getUserId(), lendProductPublish, lendProduct, amount);
            //确定可购买金额
            if (lendProductPublish.getPublishBalanceType() == LendProductPublish.PUBLISHBALANCETYPE_SPEC) {
                BigDecimal waitingBuyBalance = lendProductPublish.getPublishBalance().subtract(lendProductPublish.getSoldBalance());
                if (waitingBuyBalance.compareTo(amount) < 0)
                    amount = waitingBuyBalance;
            }

            //新建省心计划
            LendOrder lendOrder = lendOrderService.addFinanceOrder(currentUser.getUserId(), lendProductPublishId, amount, new Date(),
                    lendProduct, PropertiesUtils.getInstance().get("SOURCE_WEIXIN"), isUseVoucher, profitReturnConfig);
            return "redirect:/finance/toPayFinanceOrder?sxFlag=sx&lendOrderId=" + lendOrder.getLendOrderId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "/error";
    }

    private void checkForToBuyFinanaceByPayAmount(Long userId, LendProductPublish lendProductPublish, LendProduct lendProduct, BigDecimal amount) {
        UserInfo userByUserId = this.userInfoService.getUserByUserId(userId);
        //校验身份认证状态
        if (!this.userInfoService.hasIdentityAuthentication(userByUserId.getUserId()))
            throw new SystemException(UserErrorCode.HAS_NOT_IDVERIFIED).set("userId", userByUserId.getUserId());

        //校验金额是否合法
        if (amount.subtract(lendProduct.getStartsAt()).remainder(lendProduct.getUpAt()).compareTo(BigDecimal.ZERO) != 0)
            throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("financeId", lendProduct.getLendProductId())
                    .set("financePublishId", lendProductPublish.getLendProductPublishId()).set("amount", amount)
                    .set("startsAt", lendProduct.getStartsAt()).set("upAt", lendProduct.getUpAt());
        //100倍数验证
        if (amount.remainder(new BigDecimal("100")).longValue() != 0) {
            throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("financeId", lendProduct.getLendProductId());
        }
        //校验省心计划产品状态
        if (lendProductPublish.getPublishState() != LendProductPublishStateEnum.SELLING.value2Char())
            throw new SystemException(BidErrorCode.FINANACE_STATUS_NOT_SELLING).set("financePublishId", lendProductPublish.getLendProductPublishId());
    }

    /**
     * 用余额购买理财
     *
     * @param psw
     * @param lendProductPublishId
     * @param amount
     * @return
     */
    @RequestMapping(value = "buyFinanceByAccountAmount")
    @ResponseBody
    public String buyFinanceByAccountAmount(@RequestParam(value = "psw") String psw,
                                            @RequestParam(value = "lendProductPublishId") Long lendProductPublishId,
                                            @RequestParam(value = "amount") BigDecimal amount,
                                            @RequestParam(value = "lendOrderId", required = false) Long lendOrderId,
                                            @RequestParam(value = "accountPayValue", defaultValue = "0") BigDecimal accountPayValue,
                                            @RequestParam(value = "userVoucherId", required = false) String voucherIds,
                                            @RequestParam(value = "rateUserIds", required = false) String rateUserIds,
                                            @RequestParam(value = "profitReturnConfig", required = false) String profitReturnConfig,
                                            HttpServletRequest request) {

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        TokenUtils.validateToken(request);
        logger.info(LogUtils.createLogWithParams("购买理财", DescTemplate.Log.BidLogTemplate.BUY_FINANCE_BY_ACCOUNTVALUE,
                currentUser.getUserId(), lendProductPublishId, amount));

        LendProductPublish lendProductPublish = lendProductService.getLendProductPublishByPublishId(lendProductPublishId);
        //校验业务数据
        checkForBuyFinanaceByAccountAmount(psw, lendProductPublish, amount, currentUser);

        //执行购买
        LendOrder lendOrder = null;
        PayResult payResult = null;
        if (lendOrderId == null) {
            ClientEnum clientEnum = ClientEnum.WAP_CLIENT;//默认微信来源，如果是APP接口来源赋相应的值。
            if (UserSource.ISO.getValue().equals(currentUser.getAppSource())) {
                clientEnum = ClientEnum.IOS_CLIENT;
            } else if (UserSource.ANDROID.getValue().equals(currentUser.getAppSource())) {
                clientEnum = ClientEnum.ANDROID_CLIENT;
            }

            payResult = lendProductService.buyFinanceByAccountBalance(currentUser.getUserId(), lendProductPublishId, amount, clientEnum.getResource(), null, profitReturnConfig);
            lendOrder = lendOrderService.getLendOrderByPayId(payResult.getPayId(), true);
        } else {
            lendOrder = lendOrderService.findById(lendOrderId);
            //todo 财富券校验
            List<VoucherVO> voucherVOs = new ArrayList<VoucherVO>();
            BigDecimal voucherPayValue = BigDecimal.ZERO;
            String[] voucher_Ids = null;
            if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)) {
                voucher_Ids = voucherIds.split(",");
                //先校验财富券
                for (String voucherId : voucher_Ids) {
                    VoucherVO vo = voucherService.getVoucherById(Long.valueOf(voucherId));
                    voucherVOs.add(vo);
                    //计算财富券总额
                    voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
                }
                JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
                if (!jsonView.isSuccess()) {
                    request.setAttribute("errorMsg", jsonView.getInfo());
                    return JsonView.JsonViewFactory.create().success(false).info(jsonView.getInfo()).toJson();
                }
            }
            payResult = lendProductService.payFinanceByAccountBalanceWeb(currentUser.getUserId(), lendOrderId, null, null, voucher_Ids);

        }


        request.setAttribute("payResult", payResult);
        request.setAttribute("lendOrder", lendOrder);
        //根据执行结果跳转不同页面
        if (payResult.isPayResult() && payResult.isProcessResult())
            return JsonView.JsonViewFactory.create().success(true).info(lendOrder.getLendOrderId().toString()).toJson();

        return JsonView.JsonViewFactory.create().success(false).info(payResult.getFailDesc()).toJson();
    }

    /**
     * 用余额投标
     *
     * @param psw
     * @param loanApplicationId
     * @param lendOrderId
     * @param amount
     * @param accountPayValue
     * @param voucherIds
     * @param request
     * @return
     */
    @RequestMapping(value = "bidLoanByAccountBalance")
    @ResponseBody
    public String bidLoanByAccountBalance(@RequestParam(value = "psw", required = false) String psw,
                                          @RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId, Long lendOrderId,
                                          @RequestParam(value = "amount", required = false) BigDecimal amount,
                                          @RequestParam(value = "accountPayValue", defaultValue = "0") BigDecimal accountPayValue,
                                          @RequestParam(value = "userVoucherId", required = false) String voucherIds,
                                          @RequestParam(value = "rateUserIds", required = false) String rateUserIds,
                                          HttpServletRequest request) {
        ValidationUtil.checkRequiredPara(new NameValue<String, Object>("password", psw),
                new NameValue<String, Object>("loanApplicationId", loanApplicationId),
                new NameValue<String, Object>("amount", amount));
        LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(loanApplicationId);
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        currentUser = userInfoService.getUserByUserId(currentUser.getUserId());
        TokenUtils.validateToken(request);
        logger.info(LogUtils.createLogWithParams("投标", DescTemplate.Log.BidLogTemplate.BID_LOAN_BY_ACCOUNTVALUE,
                currentUser.getUserId(), lendProductPublish.getLendProductPublishId(), amount));

        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationId);

        //校验交易密码
        if (currentUser.getBidPass() == null) {
            request.setAttribute("errorMsg", UserErrorCode.NO_BIDPASS.getDesc());
            //return "/order/payError";
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.NO_BIDPASS.getDesc()).toJson();
        }
        if (!MD5Util.MD5Encode(psw, null).equals(currentUser.getBidPass())) {
            request.setAttribute("errorMsg", UserErrorCode.ERROR_BIDPASS.getDesc());
            //return "/order/payError";
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.ERROR_BIDPASS.getDesc()).toJson();
        }

        //校验身份认证状态
        if (!this.userInfoService.hasIdentityAuthentication(currentUser.getUserId())) {
            request.setAttribute("errorMsg", UserErrorCode.HAS_NOT_IDVERIFIED.getDesc());
            //return "/order/payError";
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.HAS_NOT_IDVERIFIED.getDesc()).toJson();
        }

        //标的状态验证
        if (!LoanApplicationStateEnum.BIDING.getValue().equals(loanApplicationListVO.getApplicationState())) {
            request.setAttribute("errorMsg", BidErrorCode.BID_STATIS_NOT_BIDDING.getDesc());
            //return "/order/payError";
            return JsonView.JsonViewFactory.create().success(false).info(BidErrorCode.BID_STATIS_NOT_BIDDING.getDesc()).toJson();
        }

        //验证购买金额是否合法(不大于0的情况)
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            request.setAttribute("errorMsg", BidErrorCode.AMOUNT_ILLEGAL.getDesc());
            return "/order/payError";
        }

        // 定向标验证

        String opass = (String) request.getSession().getAttribute("opass");
        String Spath = chioceWhichReturnBySpecialLoanApplication(
                currentUser.getUserId(), loanApplicationId, opass,
                request);
        if (!"".equals(Spath))
            return JsonView.JsonViewFactory.create().success(false).info((String) request.getAttribute("errorMsg")).toJson();
        //校验金额是否合法
        loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());
        HttpSession session = WebUtil.getHttpServletRequest().getSession();
        List<VoucherVO> voucherVOs = new ArrayList<VoucherVO>();
        BigDecimal voucherPayValue = BigDecimal.ZERO;
        String[] voucher_Ids = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)) {
            voucher_Ids = voucherIds.split(",");
            //财富券
            for (String voucherId : voucher_Ids) {
                VoucherVO vo = voucherService.getVoucherById(Long.valueOf(voucherId));
                voucherVOs.add(vo);
                //计算财富券总额
                voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
            }

        }

        // 加息券校验
        RateUser rateUser = null;
        RateProduct rateProduct = null;
        if (!com.xt.cfp.core.util.StringUtils.isNull(rateUserIds)) {
            String[] arrayRateUserIds = rateUserIds.split(",");
            if (arrayRateUserIds.length > 1) {
                request.setAttribute("errorMsg", "加息券不能叠加使用");
                return JsonView.JsonViewFactory.create().success(false).info("加息券不能叠加使用").toJson();
            }
            rateUser = rateUserService.findByRateUserId(Long.valueOf(arrayRateUserIds[0]));
            if (null == rateUser) {
                request.setAttribute("errorMsg", "该加息券不存在");
                return JsonView.JsonViewFactory.create().success(false).info("该加息券不存在").toJson();
            }
            LendProduct product = lendProductService.findById(lendProductPublish.getLendProductId());
            rateProduct = rateProductService.getRateProductById(rateUser.getRateProductId());
            JsonView checkRateUser = rateUserService.checkRateUser(rateUser, rateProduct, currentUser.getUserId(), product.getTimeLimit(), loanApplicationListVO.getLoanType(), amount);
            if (!checkRateUser.isSuccess()) {
                request.setAttribute("errorMsg", checkRateUser.getInfo());
                return JsonView.JsonViewFactory.create().success(false).info(checkRateUser.getInfo()).toJson();
            }

            if (!rateProduct.getUsageScenario().equals(RateProductScenarioEnum.ALL.getValue()) && !rateProduct.getUsageScenario().equals(RateProductScenarioEnum.BIDDING.getValue())) {
                request.setAttribute("errorMsg", "该加息券不能购买投标产品");
                return JsonView.JsonViewFactory.create().success(false).info("该加息券不能购买投标产品").toJson();
            }
            if (!com.xt.cfp.core.util.StringUtils.isNull(voucherIds)) {
                if (rateProduct.getIsOverlay().equals(RateProductIsOverlayEnum.DISABLED.getValue())) {
                    request.setAttribute("errorMsg", "该加息券不能和财富券叠加使用");
                    return JsonView.JsonViewFactory.create().success(false).info("该加息券不能和财富券叠加使用").toJson();
                }
            }

        }

        UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);
        if (user != null) {
            //获取账户余额
            UserAccount cashAccount = userAccountService.getCashAccount(user.getUserId());
            if (cashAccount.getAvailValue2().compareTo(accountPayValue) < 0) {
                request.setAttribute("errorMsg", UserAccountErrorCode.ACCOUNT_BALANCE_AVAIVALUE_INSUFFICIENT.getDesc());
                //return "/order/payError";
                return JsonView.JsonViewFactory.create().success(false).info(UserAccountErrorCode.ACCOUNT_BALANCE_AVAIVALUE_INSUFFICIENT.getDesc()).toJson();
            }
            BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
            if (amount.compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy)) > 0) {
                //购买金额变为剩余可投金额
                if (loanApplicationListVO.getConfirmBalance().compareTo(totalBuy) == 0) {
                    request.setAttribute("errorMsg", BidErrorCode.LENDORDER_AMOUNT_ZONE.getDesc());
                    //return "/order/payError";
                    return JsonView.JsonViewFactory.create().success(false).info(BidErrorCode.LENDORDER_AMOUNT_ZONE.getDesc()).toJson();
                }
                amount = loanApplicationListVO.getConfirmBalance().subtract(totalBuy);
            }
            //计算账户已投金额
            BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(user.getUserId(), loanApplicationListVO.getLoanApplicationId());
            if (totalLendAmountPerson != null) {
                if (loanApplicationListVO.getMaxBuyBalance() == null) {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
                } else {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
                }
            }

            if (amount.compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy)) > 0) {
                //无法购买剩余标
                throw new SystemException(BidErrorCode.LENDORDER_AMOUNT_NOT_ENOUGH).set("amount", amount).set("remain", loanApplicationListVO.getConfirmBalance().subtract(totalBuy));
            }
        }
        if (amount.compareTo(loanApplicationListVO.getMaxBuyBalanceNow()) > 0) {
            request.setAttribute("errorMsg", BidErrorCode.LIMIT_AMOUNT_NOT_ENOUGH.getDesc());
            //return "/order/payError";
            return JsonView.JsonViewFactory.create().success(false).info(BidErrorCode.LIMIT_AMOUNT_NOT_ENOUGH.getDesc()).toJson();
        }

        //100倍数验证
        if (amount.remainder(new BigDecimal("100")).longValue() != 0) {
            throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
        }

        PayResult payResult = null;
        LendOrder lendOrder = null;
        if (lendOrderId == null) {
            payResult = lendProductService.bidLoanByAccountBalance(loanApplicationId, currentUser.getUserId(), lendProductPublish.getLendProductPublishId(), amount, ClientEnum.from(currentUser.getAppSource()).getResource());
            lendOrder = lendOrderService.getLendOrderByPayId(payResult.getPayId(), true);
        } else {
            //可用余额、财富券支付
            lendOrder = lendOrderService.findById(lendOrderId);

            //todo 财富券校验
            if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)) {
                voucher_Ids = voucherIds.split(",");
                //先校验财富券
                JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
                if (!jsonView.isSuccess()) {
                    request.setAttribute("errorMsg", jsonView.getInfo());
                    //return "/order/payError";
                    return JsonView.JsonViewFactory.create().success(false).info(jsonView.getInfo()).toJson();
                }
            }

            //校验-购买金额加和是否等于订单金额
            if (lendOrder.getBuyBalance().compareTo(accountPayValue.add(voucherPayValue)) != 0) {
                request.setAttribute("errorMsg", BidErrorCode.AMOUNT_ILLEGAL.getDesc());
                return JsonView.JsonViewFactory.create().success(false).info(BidErrorCode.AMOUNT_ILLEGAL.getDesc()).toJson();
            }

            payResult = lendProductService.payFinanceByAccountBalanceWeb(currentUser.getUserId(), lendOrderId, rateUser, rateProduct, voucher_Ids);
        }

        request.setAttribute("payResult", payResult);
        request.setAttribute("lendOrder", lendOrder);
        //根据执行结果跳转不同页面
        if (payResult.isPayResult() && payResult.isProcessResult())
            return JsonView.JsonViewFactory.create().success(true).info(lendOrder.getLendOrderId().toString()).toJson();

        request.setAttribute("errorMsg", payResult.getFailDesc());
        return JsonView.JsonViewFactory.create().success(false).info(payResult.getFailDesc()).toJson();
    }

    @RequestMapping(value = "checkBidLoanByPay")
    @ResponseBody
    public String checkBidLoanByPay(@RequestParam(value = "lendOrderId") Long lendOrderId) {

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        //校验-订单用户和当前用户必须相同
        if (!lendOrder.getLendUserId().equals(currentUser.getUserId())) {
            return JsonView.JsonViewFactory.create().success(false).info(PayErrorCode.NOT_BELONG_PAY_ORDER.getDesc())
                    .toJson();
        }
        List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
        LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());

        //标的状态验证
        if (!LoanApplicationStateEnum.BIDING.getValue().equals(loanApplicationListVO.getApplicationState())) {
            throw new SystemException(BidErrorCode.BID_STATIS_NOT_BIDDING).set("status", loanApplicationListVO.getApplicationState());
        }
        //预热标验证
        if (!loanApplicationListVO.isBegin()) {
            throw new SystemException(BidErrorCode.BID_STATIS_NOT_SELLING).set("begin", loanApplicationListVO.isBegin());
        }

        if (currentUser != null) {
            //获取账户余额
            if (lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getRemain()) > 0) {
                return JsonView.JsonViewFactory.create().success(false).info("金额已超出当前最大可投金额！")
                        .toJson();
            }
            BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
            if (lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy)) > 0) {
                //无法购买剩余标
                JsonView.JsonViewFactory.create().success(false).info("金额已超出当前最大可投金额！")
                        .toJson();
            }
            //计算账户已投金额
            BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(currentUser.getUserId(), loanApplicationListVO.getLoanApplicationId());
            if (totalLendAmountPerson != null) {
                if (loanApplicationListVO.getMaxBuyBalance() == null) {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
                } else {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
                }
            }
        }

        if (lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getMaxBuyBalanceNow()) > 0)
            return JsonView.JsonViewFactory.create().success(false).info("金额已超出当前最大可投金额！")
                    .toJson();


        return JsonView.JsonViewFactory.create().success(true).toJson();

    }

    //wangyadong add
    @SuppressWarnings("unused")
    private void checkBidLoanByPay(LoanApplicationListVO loanApplicationListVO, BigDecimal amount, UserInfo currentUser) {

        HttpServletRequest request = this.getRequest();
        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        //校验身份认证状态
        if (!this.userInfoService.hasIdentityAuthentication(userByUserId.getUserId()))
            throw new SystemException(UserErrorCode.HAS_NOT_IDVERIFIED).set("userId", userByUserId.getUserId());

        //标的状态验证
        if (!LoanApplicationStateEnum.BIDING.getValue().equals(loanApplicationListVO.getApplicationState())) {
            throw new SystemException(BidErrorCode.BID_STATIS_NOT_BIDDING).set("status", loanApplicationListVO.getApplicationState());
        }
        //预热标验证
        if (!loanApplicationListVO.isBegin()) {
            throw new SystemException(BidErrorCode.BID_STATIS_NOT_SELLING).set("begin", loanApplicationListVO.isBegin());
        }

        //验证购买金额是否合法(不大于0的情况)
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
        }

        //校验金额是否合法
        loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());
        HttpSession session = WebUtil.getHttpServletRequest().getSession();

        UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);
        if (user != null) {
            //计算账户已投金额
            BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(user.getUserId(), loanApplicationListVO.getLoanApplicationId());
            if (totalLendAmountPerson != null) {
                if (loanApplicationListVO.getMaxBuyBalance() == null) {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
                } else {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
                }
            }
        }

        if (amount.compareTo(loanApplicationListVO.getMaxBuyBalanceNow()) > 0)
            throw new SystemException(BidErrorCode.LIMIT_AMOUNT_NOT_ENOUGH).set("amount", amount).set("maxBuyBalanceNow", loanApplicationListVO.getMaxBuyBalanceNow());

    }

    //wangyadong add
    @SuppressWarnings("unused")
    private void checkBidLoanByPayCredit(LoanApplicationListVO loanApplicationListVO, BigDecimal amount, UserInfo currentUser) {

        HttpServletRequest request = this.getRequest();
        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        //校验身份认证状态
        if (!this.userInfoService.hasIdentityAuthentication(userByUserId.getUserId()))
            throw new SystemException(UserErrorCode.HAS_NOT_IDVERIFIED).set("userId", userByUserId.getUserId());

//		//标的状态验证
//		if (!LoanApplicationStateEnum.BIDING.getValue().equals(loanApplicationListVO.getApplicationState())){
//			throw new SystemException(BidErrorCode.BID_STATIS_NOT_BIDDING).set("status", loanApplicationListVO.getApplicationState());
//		}
//		//预热标验证
//		if (!loanApplicationListVO.isBegin()){
//			throw new SystemException(BidErrorCode.BID_STATIS_NOT_SELLING).set("begin",loanApplicationListVO.isBegin());
//		}

        //验证购买金额是否合法(不大于0的情况)
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
        }

        //校验金额是否合法
        loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());
        HttpSession session = WebUtil.getHttpServletRequest().getSession();

        UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);
        if (user != null) {
            //计算账户已投金额
            BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(user.getUserId(), loanApplicationListVO.getLoanApplicationId());
            if (totalLendAmountPerson != null) {
                if (loanApplicationListVO.getMaxBuyBalance() == null) {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
                } else {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
                }
            }
        }

        if (amount.compareTo(loanApplicationListVO.getMaxBuyBalanceNow()) > 0)
            throw new SystemException(BidErrorCode.LIMIT_AMOUNT_NOT_ENOUGH).set("amount", amount).set("maxBuyBalanceNow", loanApplicationListVO.getMaxBuyBalanceNow());

    }

    /**
     * 投标列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getLender")
    @ResponseBody
    @DoNotNeedLogin
    public Object getLender(HttpServletRequest request, Long loanApplicationId,
                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        //获取借款申请状态
        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
        if ("345".indexOf(loanApplication.getApplicationState()) != -1) {
            //发标中、放款审核中、待放款
            //查询出借订单明细表中 投标中 的状态数据
            Pagination<LenderRecordVO> result = lendOrderBidDetailService.findLendOrderDetailPaging(pageNo, pageSize, loanApplicationId, LendOrderBidStatusEnum.BIDING, DisplayEnum.DISPLAY);
            return result;
        } else {
            //6、7、8 还款中、已结清、已结清(提前还贷)
            //到债权表查询  已生效，还款中 状态的数据
            Pagination<LenderRecordVO> result_1 = creditorRightsService.findLendOrderDetailPaging(pageNo, pageSize, pageNo, pageSize,
                    loanApplicationId, CreditorRightsStateEnum.EFFECTIVE, CreditorRightsStateEnum.COMPLETE, CreditorRightsStateEnum.EARLYCOMPLETE);
            return result_1;
//			Pagination<LenderRecordVO> result = lendOrderBidDetailService.findLendOrderDetailPaging(pageNo, pageSize,loanApplicationId, LendOrderBidStatusEnum.BIDSUCCESS);
//			return result;
        }
    }

    /**
     * 汽车列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCardList")
    @ResponseBody
    @DoNotNeedLogin
    public Object getCardList(HttpServletRequest request, Long loanApplicationId,
                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                              @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        //获取借款申请状态
        EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot = enterpriseCarLoanSnapshotService.getByloanApplicationId(loanApplicationId);
        if (enterpriseCarLoanSnapshot == null)
            return null;
        MortgageCarSnapshot mortgageCarSnapshot = new MortgageCarSnapshot();
        mortgageCarSnapshot.setCarLoanId(enterpriseCarLoanSnapshot.getEnterpriseCarLoanId());
        return mortgageCarSnapshotService.getMortgageCarSnapshotPaging(pageNo, pageSize, mortgageCarSnapshot, null);

    }

    /**
     * 验证交易密码
     *
     * @param bidPass
     * @return
     */
    @RequestMapping(value = "checkBidLoanByAccountBalance")
    @ResponseBody
    public String checkBidLoanByAccountBalance(HttpServletRequest request, String bidPass) {
        //post请求验证
        RequestUtil.validateRequestMethod(request, RequestMethod.POST);
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        //校验交易密码
        if (userByUserId.getBidPass() == null)
            return JsonView.JsonViewFactory.create().success(false).info("请您输入交易密码！").put("id", "jypassword")
                    .toJson();

        if (!MD5Util.MD5Encode(bidPass, null).equals(userByUserId.getBidPass()))
            return JsonView.JsonViewFactory.create().success(false).info("交易密码错误").put("id", "jypassword")
                    .toJson();
        return JsonView.JsonViewFactory.create().success(true).info("交易密码正确").put("id", "jypassword")
                .toJson();
    }

    private void checkForToBuyFinanaceByPayAmount(Long userId, LendProductPublish lendProductPublish, BigDecimal amount) {
        UserInfo userByUserId = this.userInfoService.getUserByUserId(userId);
        //校验身份认证状态
        if (!this.userInfoService.hasIdentityAuthentication(userByUserId.getUserId()))
            throw new SystemException(UserErrorCode.HAS_NOT_IDVERIFIED).set("userId", userByUserId.getUserId());

        //校验金额是否合法
        LendProduct financeProduct = lendProductService.findById(lendProductPublish.getLendProductId());
        if (!amount.subtract(financeProduct.getStartsAt()).remainder(financeProduct.getUpAt()).equals(BigDecimal.ZERO))
            throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("financeId", financeProduct.getLendProductId())
                    .set("financePublishId", lendProductPublish.getLendProductPublishId()).set("amount", amount)
                    .set("startsAt", financeProduct.getStartsAt()).set("upAt", financeProduct.getUpAt());
        //100倍数验证
        if (amount.remainder(new BigDecimal("100")).longValue() != 0) {
            throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("financeId", financeProduct.getLendProductId());
        }
        //校验理财计划产品状态
        if (lendProductPublish.getPublishState() != LendProductPublishStateEnum.SELLING.value2Char())
            throw new SystemException(BidErrorCode.FINANACE_STATUS_NOT_SELLING).set("financePublishId", lendProductPublish.getLendProductPublishId());
    }

    @SuppressWarnings("unused")
    private void checkForBuyFinanaceByAccountAmount(String bidPass, LendProductPublish lendProductPublish, BigDecimal amount, UserInfo currentUser) {
        HttpServletRequest request = this.getRequest();
        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        //校验交易密码
        if (userByUserId.getBidPass() == null)
            throw new SystemException(UserErrorCode.NO_BIDPASS).set("userId", userByUserId.getUserId());

        if (!MD5Util.MD5Encode(bidPass, null).equals(userByUserId.getBidPass()))
            throw new SystemException(UserErrorCode.ERROR_BIDPASS).set("userId", userByUserId.getUserId());

        //校验身份认证状态
        if (!this.userInfoService.hasIdentityAuthentication(userByUserId.getUserId()))
            throw new SystemException(UserErrorCode.HAS_NOT_IDVERIFIED).set("userId", userByUserId.getUserId());

        //验证购买金额是否合法(不大于0的情况)
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
        }

        //校验金额是否合法
        LendProduct financeProduct = lendProductService.findById(lendProductPublish.getLendProductId());
        if (!amount.subtract(financeProduct.getStartsAt()).remainder(financeProduct.getUpAt()).equals(BigDecimal.ZERO))
            throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("financeId", financeProduct.getLendProductId())
                    .set("financePublishId", lendProductPublish.getLendProductPublishId()).set("amount", amount)
                    .set("startsAt", financeProduct.getStartsAt()).set("upAt", financeProduct.getUpAt());

        //100倍数验证
        if (amount.remainder(new BigDecimal("100")).longValue() != 0) {
            throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
        }

        //校验理财计划产品状态
        if (lendProductPublish.getPublishState() != LendProductPublishStateEnum.SELLING.value2Char())
            throw new SystemException(BidErrorCode.FINANACE_STATUS_NOT_SELLING).set("financePublishId", lendProductPublish.getLendProductPublishId());

        //校验账户余额
        UserAccount cashAccount = userAccountService.getCashAccount(userByUserId.getUserId());
        if (cashAccount.getAvailValue2().compareTo(amount) < 0)
            throw new SystemException(UserAccountErrorCode.ACCOUNT_BALANCE_AVAIVALUE_INSUFFICIENT).set("needAmount", amount).set("userAvailAmount", cashAccount.getAvailValue2()).set("userId", userByUserId.getUserId());
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/toFinanceDetail")
    public String toFinanceDetail(HttpServletRequest req, LendProductPublish lpp) {
        LproductWithBalanceStatus financeDetail = this.lendProductService.findFinanceProductDetailForWeb(lpp.getLendProductPublishId());
        UserInfo user = (UserInfo) req.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
        if (null != user) {
            UserAccount userAccount = userAccountService.getCashAccount(user.getUserId());
            req.setAttribute("userAccount", userAccount);
            UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
            req.setAttribute("userExt", userExt);
        }
        // 本期申购情况
        // todo sql语句状态需要更新
        List<LendOrderExtProduct> buySituation = lendOrderService.getCycleBuySituation(lpp.getLendProductPublishId());
        // 往期申购情况
        List<LendProductPublish> historyBuySituation = lendOrderService.getCycleBuySituationHistory(lpp);
        //token
        String token = TokenUtils.setNewToken(req);
        req.setAttribute("token", token);

        req.setAttribute("financeDetail", financeDetail);
        req.setAttribute("buySituation", buySituation);
        req.setAttribute("historyBuySituation", historyBuySituation);

        return "/finance/financeDetail";
    }

    /**
     * 出借列表
     *
     * @return
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/loanList")
    @ResponseBody
    public Object loanList(HttpServletRequest request,
                           LoanApplicationListVO loanApplicationListVO,
                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                           @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {

        Pagination<LoanApplicationListVO> loanApplicationListPaging = loanApplicationService.getLoanApplicationPaging(pageNo, pageSize, loanApplicationListVO, null);
        return loanApplicationListPaging;
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/getExpectProfit")
    @ResponseBody
    public Object getExpectProfit(HttpServletRequest request,
                                  Long loanApplicationId, BigDecimal amount) {
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);
        LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
        BigDecimal profit = BigDecimal.ZERO;
        BigDecimal award = BigDecimal.ZERO;
        LoanPublish loanPublish = loanPublicService.findById(loanApplication.getLoanApplicationId());
        if (loanPublish.getAwardRate() != null) {
            try {
                award = InterestCalculation.getAllInterest(amount, loanPublish.getAwardRate(), loanProduct.getDueTimeType(),
                        loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
            award = BigDecimalUtil.down(award, 2);
        }
        try {
            profit = InterestCalculation.getAllInterest(amount, loanApplication.getAnnualRate(), loanProduct.getDueTimeType(),
                    loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        profit = BigDecimalUtil.down(profit, 2);
        if (award.compareTo(BigDecimal.ZERO) > 0)
            return profit + "+" + award + "(奖励)";
        else
            return profit.toString();
    }


    @RequestMapping(value = "/toRealName")
    public String toRealName(String url,String userToken, String source,Model model) {
        if (!com.xt.cfp.core.util.StringUtils.isNull(url)) {
            model.addAttribute("url", url);
        }
        model.addAttribute("userToken",userToken);
        model.addAttribute("source",source);
        return "/finance/toRealName";
    }

    /**
     * 投标详情页
     *
     * @param request
     * @param loanApplicationNo
     * @return
     */
    @SuppressWarnings("unchecked")
    @DoNotNeedLogin
    @RequestMapping(value = "/bidding")
    public String bidding(HttpServletRequest request, @RequestParam(value = "loanApplicationNo", required = false) Long loanApplicationNo) {
        if (loanApplicationNo == null) {
            //非法请求
            return "error";
        }
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
        if (loanApplicationListVO == null) {
            //非法请求
            return "error";
        }

        //标的状态验证
        if (LoanApplicationStateEnum.DELETED.getValue().equals(loanApplicationListVO.getApplicationState())
                || LoanApplicationStateEnum.FAILURE.getValue().equals(loanApplicationListVO.getApplicationState())) {
            throw new SystemException(BidErrorCode.BID_STATIS_NOT_BIDDING).set("status", loanApplicationListVO.getApplicationState());
        }

        //发标信息
        LoanPublish loanPublish = loanPublicService.findLoanPublishVO(loanApplicationNo);
        request.setAttribute("loanPublish", loanPublish);
        if (loanPublish == null) {
            //非法请求
            return "error";
        }
        request.setAttribute("creditor_type", "sb");
        request.setAttribute("loanApplicationNo", loanApplicationNo);
//		System.out.println("the loanPublish:"+JsonUtil.json(loanPublish));
//		if (!PublishTarget.FRONT.getValue().equals(loanPublish.getPublishTarget())){
//			//非法请求
//			return "error";
//		}

        Date openTime = loanApplicationListVO.getPopenTime();
        try {
            int secondBetween = 0;
            if (null != openTime) {
                secondBetween = DateUtil.secondBetween(new Date(), openTime);
            }
            request.setAttribute("secondBetwween", secondBetween);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //token
        String token = TokenUtils.setNewToken(request);
        request.setAttribute("token", token);
        //组织页面返回值
        request.setAttribute("repayMentMethod", PaymentMethodEnum.values());
        request.setAttribute("guaranteeType", GuaranteeTypeEnum.values());
        //用户是否登陆
        loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());

        HttpSession session = WebUtil.getHttpServletRequest().getSession();
        UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);

        if (user != null) {
            //获取账户余额
            UserAccount cashAccount = userAccountService.getCashAccount(user.getUserId());
            UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
            request.setAttribute("userExt", userExt);
            request.setAttribute("cashAccount", cashAccount);
            //计算账户已投金额
            BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(user.getUserId(), loanApplicationListVO.getLoanApplicationId());
            if (totalLendAmountPerson != null) {
                if (loanApplicationListVO.getMaxBuyBalance() == null) {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
                } else {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
                }
            }
        }
        request.setAttribute("loanApplicationListVO", loanApplicationListVO);

        Pagination<LenderRecordVO> obj = (Pagination<LenderRecordVO>) getLender(request, loanApplicationListVO.getLoanApplicationId(), 1, 10);
        request.setAttribute("totalNum", obj.getTotal());
        //附件快照
        List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getcustomerUploadAttachment(loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());
        request.setAttribute("customerUploadSnapshots", customerUploadSnapshots);
        request.setAttribute("customerPicNum", customerUploadSnapshots != null ? customerUploadSnapshots.size() : 0);

        //【定向标-开始】
        int oType = loanApplicationService.getLoanApplicationType(loanApplicationNo);//根据借款申请ID，查询该标定向类型（1密码，2用户）
        request.getSession().setAttribute("applicationId", loanApplicationNo);
        request.setAttribute("oType", oType);
        boolean isTargetUser = false;//当前用户，是否为定向用户
        if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_USER.getValue().equals(
                String.valueOf(oType))) {// 定向用户
            if (user != null) {
                int userCount = userInfoService.normalOrOrienta(
                        user.getUserId(), loanApplicationNo);// 判断当前用户，是否为该标的定向用户
                if (userCount > 0) {
                    isTargetUser = true;
                }
            }
        } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_NEWUSER
                .getValue().equals(String.valueOf(oType))) {// 新手用户
            if (user != null) {
                String[] querys = {"1", "2", "5", "6"};
                int mCount = lendOrderService.countMakedLoan(user.getUserId(),
                        querys);
                // 是新手用户
                request.setAttribute("mCount", mCount);

            }

        } else if (SpecialBiddingEnum.SpecialTypeEnum.SPECIAL_PASSWORD
                .getValue().equals(String.valueOf(oType))) {
            request.getSession().setAttribute("opass", userInfoService
                    .getOrientPassLoan(loanApplicationNo));
        }
        request.setAttribute("isTargetUser", isTargetUser);
        //【定向标-结束】

        //省心计划匹配时间计算(开始)
        long time = 60;//倒计时时间 秒
        int second = 0;
        if (PublishRule.FIRST_AUTOMATIC.getValue().equals(loanPublish.getPublishRule())) {//只有省心优先才倒计时
            Long otime = loanApplicationListVO.getOtime().getTime();
            Date now = new Date();
            if (otime.compareTo(now.getTime()) < 0) {
                if ((now.getTime() - otime) / 1000 < time) {
                    second = (int) (time - (now.getTime() - otime) / 1000);
                }
            }
        }
        request.setAttribute("second", second);
        //省心计划匹配时间计算(结束)

        if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(loanApplicationListVO.getLoanType())) {
            //个人投标详情页
            personLoanApplication(request, loanApplicationListVO, loanPublish);
        } else {
            //企业投标详情页
            enterpriseLoanApplication(request, loanApplicationListVO, loanPublish);
        }
        return "/finance/finance";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/detail")
    public String detail(HttpServletRequest request, @RequestParam(value = "loanApplicationNo", required = false) Long loanApplicationNo, String goType, Long creditorRightsId) {
        request.setAttribute("goType", goType);
        request.setAttribute("creditorRightsId", creditorRightsId);
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
        //标的状态验证
        if (LoanApplicationStateEnum.DELETED.getValue().equals(loanApplicationListVO.getApplicationState())
                || LoanApplicationStateEnum.FAILURE.getValue().equals(loanApplicationListVO.getApplicationState())) {
            throw new SystemException(BidErrorCode.BID_STATIS_NOT_BIDDING).set("status", loanApplicationListVO.getApplicationState());
        }

        //发标信息
        LoanPublish loanPublish = loanPublicService.findLoanPublishVO(loanApplicationNo);
        request.setAttribute("loanPublish", loanPublish);
        if (loanPublish == null) {
            //非法请求
            return "error";
        }

        request.setAttribute("loanApplicationListVO", loanApplicationListVO);

        //附件快照
        List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getcustomerUploadAttachment(loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());
        request.setAttribute("customerUploadSnapshots", customerUploadSnapshots);
        request.setAttribute("customerPicNum", customerUploadSnapshots != null ? customerUploadSnapshots.size() : 0);

        if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(loanApplicationListVO.getLoanType())) {
            //个人投标详情
            personLoanApplication(request, loanApplicationListVO, loanPublish);
        } else {
            //企业投标详情
            enterpriseLoanApplication(request, loanApplicationListVO, loanPublish);
        }

        //增加还款信息展示
        //未还款时计算还款信息
        //已还本息
        BigDecimal hasPaidBalance = BigDecimal.ZERO;
        //待还本息
        BigDecimal waitPaidBalance = BigDecimal.ZERO;
        Map<Object, String> stateMap = new HashMap<>();
        for (RepaymentPlanStateEnum r : RepaymentPlanStateEnum.values()) {
            stateMap.put(r.value2Char(), r.getDesc());
        }
        request.setAttribute("stateMap", stateMap);
        List<RepaymentPlan> repaymentPlanList = new ArrayList<RepaymentPlan>();
        if (LoanApplicationStateEnum.REPAYMENTING.getValue().equals(loanApplicationListVO.getApplicationState())
                || LoanApplicationStateEnum.COMPLETED.getValue().equals(loanApplicationListVO.getApplicationState())
                || LoanApplicationStateEnum.EARLYCOMPLETE.getValue().equals(loanApplicationListVO.getApplicationState())) {
            repaymentPlanList = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplicationNo, ChannelTypeEnum.ONLINE);
            if (repaymentPlanList != null && repaymentPlanList.size() > 0) {
                for (RepaymentPlan plan : repaymentPlanList) {
                    hasPaidBalance = hasPaidBalance.add(plan.getFactBalance());
                    if (plan.getPlanState() == RepaymentPlanStateEnum.BEFORE_COMPLETE.value2Char()) {
                        waitPaidBalance = waitPaidBalance.add(plan.getShouldCapital2().subtract(plan.getFactCalital()));
                    } else if (plan.getPlanState() == RepaymentPlanStateEnum.COMPLETE.value2Char()) {

                    } else {
                        waitPaidBalance = waitPaidBalance.add(plan.getShouldBalance2().subtract(plan.getFactBalance()));
                    }
                }
            }
            request.setAttribute("isRepaying", true);
        } else {
            LoanProduct product = loanProductService.findById(loanApplicationListVO.getLoanProductId());
            LoanApplication loanApp = loanApplicationService.findById(loanApplicationListVO.getLoanApplicationId());
            try {
                repaymentPlanList = loanApplicationService.getRepaymentPLanData(product, loanApp);
            } catch (Exception e) {
                logger.error("查询异常", e);
                e.printStackTrace();
            }
            for (RepaymentPlan plan : repaymentPlanList) {
                waitPaidBalance = waitPaidBalance.add(plan.getShouldBalance2());
            }
            request.setAttribute("isRepaying", false);
        }
        request.setAttribute("showRepaypaymentList", repaymentPlanList);
        waitPaidBalance = BigDecimalUtil.up(waitPaidBalance, 2);
        request.setAttribute("waitPaidBalance", waitPaidBalance);
        request.setAttribute("hasPaidBalance", hasPaidBalance);
        return "/finance/financeDesc";
    }

    /**
     * 企业投标详情页
     *
     * @param request
     * @param loanApplicationListVO
     * @param loanPublish
     */
    private void enterpriseLoanApplication(HttpServletRequest request, LoanApplicationListVO loanApplicationListVO, LoanPublish loanPublish) {
        //借款企业
        EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
        EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
        request.setAttribute("enterpriseInfo", enterpriseInfo);
        //企业图
        List<EnterpriseUploadSnapshotVO> enterpriseInfoSnapshots = enterpriseInfoService.getEnterpriseUploadAttachment(enterpriseInfo.getEnterpriseId(), AttachmentIsCode.IS_CODE.getValue());
        request.setAttribute("enterpriseInfoSnapshots", enterpriseInfoSnapshots);
        request.setAttribute("enterprisePicNum", enterpriseInfoSnapshots != null ? enterpriseInfoSnapshots.size() : 0);
        switch (Integer.valueOf(loanApplicationListVO.getLoanType())) {
            case 2:
                //企业车贷
                EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot = enterpriseCarLoanSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
                String address = getAddress(enterpriseCarLoanSnapshot.getProvince(), enterpriseCarLoanSnapshot.getCity());
                BigDecimal totalPrice = mortgageCarSnapshotService.getTotalPriceByCardLoanId(enterpriseCarLoanSnapshot.getEnterpriseCarLoanId());
                request.setAttribute("totalPrice", totalPrice);
                request.setAttribute("address", address);
                request.setAttribute("enterpriseCarLoanSnapshot", enterpriseCarLoanSnapshot);
                break;
            case 3:
                //企业信贷
                EnterpriseCreditSnapshot enterpriseCreditSnapshot = enterpriseCreditSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
                address = getAddress(enterpriseCreditSnapshot.getProvince(), enterpriseCreditSnapshot.getCity());
                request.setAttribute("enterpriseCarLoanSnapshot", enterpriseCreditSnapshot);
                request.setAttribute("address", address);
                break;
            case 4:
                //企业保理
                EnterpriseFactoringSnapshot factoringSnapshot = enterpriseFactoringSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
                request.setAttribute("factoringSnapshot", factoringSnapshot);
                //融资方
                CoLtd financeParty = coLtdService.getCoLtdById(factoringSnapshot.getFinancingParty());
                request.setAttribute("financeParty", financeParty);
                break;
            case 5:
                //企业基金
                EnterpriseFoundationSnapshot foundationSnapshot = enterpriseFoundationSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
                request.setAttribute("foundationSnapshot", foundationSnapshot);
                //托管机构
                CoLtd coltd = coLtdService.getCoLtdById(foundationSnapshot.getCoId());
                request.setAttribute("coltd", coltd);
                //标的详情说明
                Attachment attachment = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getAttachId());
                request.setAttribute("attachment", attachment);
                //交易说明书
                Attachment tradeBook = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getTradeBookId());
                request.setAttribute("tradeBook", tradeBook);
                //风险提示函
                Attachment riskTip = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getRiskTipId());
                request.setAttribute("riskTip", riskTip);
                break;
            case 6:
                //企业标（质押标）
                EnterprisePledgeSnapshot enterprisePledgeSnapshot = enterprisePledgeSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
                address = getAddress(enterprisePledgeSnapshot.getProvince(), enterprisePledgeSnapshot.getCity());
                request.setAttribute("enterpriseCarLoanSnapshot", enterprisePledgeSnapshot);
                request.setAttribute("address", address);
                break;
        }

    }

    private String getAddress(Long provinceId, Long cityId) {
        ProvinceInfo province = null;
        CityInfo city = null;
        String address = "";
        if (provinceId != null) {
            province = provinceInfoService.findById(provinceId);
        }
        if (cityId != null) {
            city = cityInfoService.findById(cityId);
        }
        if (province == null) return null;

        if (city == null)
            return province.getProvinceName();

        if (city.getCityName().equals(province.getProvinceName())) {
            address = city.getCityName();
        } else {
            address = province.getProvinceName() + city.getCityName();
        }
        return address;
    }


    /**
     * 个人投标详情页
     *
     * @param request
     * @param loanApplicationListVO
     * @param loanPublish
     */
    private void personLoanApplication(HttpServletRequest request, LoanApplicationListVO loanApplicationListVO, LoanPublish loanPublish) {
        //借款人基本信息
        CustomerBasicSnapshot basicSnapshot = customerBasicSnapshotService.getBasicByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
        if (basicSnapshot != null) {
            request.setAttribute("basicSnapshot", basicSnapshot);
            //居住地
            Address adress = addressService.getAddressVOById(basicSnapshot.getResidenceAddr());
            request.setAttribute("adress", adress);
        }
        if (LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())) {
            CustomerCarSnapshot basicInfoForPeopleAndCar = customerCarSnapshotService.getCarByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
            request.setAttribute("basicInfoForPeopleAndCar", basicInfoForPeopleAndCar);
           /* request.setAttribute("basicInfoForPeopleAndCarType", basicInfoForPeopleAndCar.getPledgeType().equals("1") ? "一抵" : "二抵");*/
        } else {
            //房产抵押
            CustomerHouseSnapshot house = customerHouseSnapshotService.getHouseByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
            request.setAttribute("house", house);
            request.setAttribute("loanPublish", loanPublish);
            Address houseAdress = addressService.getAddressVOById(loanPublish.getHourseAddress());
            request.setAttribute("houseAdress", houseAdress);
        }

        //认证报告
        String authInfos = loanPublish.getAuthInfos();
        if (StringUtils.isNotEmpty(authInfos)) {
            request.setAttribute("authInfo", authInfos.split(","));
        }
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/front_bidding")
    public String front_bidding(HttpServletRequest request, Long loanApplicationNo) {
        if (loanApplicationNo == null) {
            //非法请求
            return "error";
        }
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
        if (loanApplicationListVO == null) {
            //非法请求
            return "error";
        }

        Date openTime = loanApplicationListVO.getPopenTime();
        try {
            int secondBetween = 0;
            if (null != openTime) {
                secondBetween = DateUtil.secondBetween(new Date(), openTime);
            }
            request.setAttribute("secondBetwween", secondBetween);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //token
        String token = TokenUtils.setNewToken(request);
        request.setAttribute("token", token);
        //组织页面返回值
        request.setAttribute("repayMentMethod", PaymentMethodEnum.values());
        request.setAttribute("guaranteeType", GuaranteeTypeEnum.values());
        //用户是否登陆
        loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());
        HttpSession session = WebUtil.getHttpServletRequest().getSession();

        UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);
        if (user != null) {
            //获取账户余额
            UserAccount cashAccount = userAccountService.getCashAccount(user.getUserId());
            UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
            request.setAttribute("userExt", userExt);
            request.setAttribute("cashAccount", cashAccount);
            //计算账户已投金额
            BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(user.getUserId(), loanApplicationListVO.getLoanApplicationId());
            if (totalLendAmountPerson != null) {
                if (loanApplicationListVO.getMaxBuyBalance() == null) {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
                } else {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
                }
            }
        }
        //发标信息
        LoanPublish loanPublish = loanPublicService.findLoanPublishVO(loanApplicationNo);

        request.setAttribute("loanApplicationListVO", loanApplicationListVO);
        //附件快照
        List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getcustomerUploadAttachment(loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());
        request.setAttribute("customerUploadSnapshots", customerUploadSnapshots);
        if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())) {
            //个人投标详情页
            personLoanApplication(request, loanApplicationListVO, loanPublish);
            return "/finance/front_financeBidding";
        } else {
            //企业投标详情页
            enterpriseLoanApplication(request, loanApplicationListVO, loanPublish);
            return "/finance/front_financeEnterpriseBidding";
        }
    }

    @RequestMapping(value = "checkBuyFinanceByPayAmount")
    @ResponseBody
    public String checkBuyFinanceByPayAmount(@RequestParam(value = "lendProductPublishId", required = false) Long lendProductPublishId,
                                             HttpServletRequest request, @RequestParam(value = "buyAmount", required = false) BigDecimal buyAmount) {
        ValidationUtil.checkRequiredPara(new NameValue<String, Object>("lendProductPublishId", lendProductPublishId), new NameValue<String, Object>(
                "buyAmount", buyAmount));

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        if (null == userExt.getIsVerified() || userExt.getIsVerified().equals(UserIsVerifiedEnum.NO.getValue())) {
            return JsonView.JsonViewFactory.create().success(true).put("isVerified", false).toJson();
        } else {
            String token = TokenUtils.setNewToken(request);
            System.out.println(request.getSession().getAttribute(TokenUtils.TOKEN_INSESSION_ATTRNAME));
            UserAccount cashAccount = userAccountService.getCashAccount(currentUser.getUserId());
            if (cashAccount.getAvailValue2().compareTo(buyAmount) >= 0) {
                return JsonView.JsonViewFactory.create().success(true).put("canPayByAccountAmount", true).put("token", token).toJson();
            } else {
                return JsonView.JsonViewFactory.create().success(true).put("canPayByAccountAmount", false).toJson();
            }
        }
    }

    /**
     * 跳转到我的理财
     *
     * @return
     */
    @RequestMapping(value = "/toAllMyFinanceList")
    public String toAllMyFinanceList() {
        return "/finance/myFinanceList";
    }

    /**
     * 我的理财的详情
     *
     * @param request
     * @param lendOrderId
     * @return
     */
    @RequestMapping(value = "/getAllMyFinanceListDetail")
    public ModelAndView getAllMyFinanceListDetail(HttpServletRequest request, Long lendOrderId) {
        ModelAndView mv = new ModelAndView();
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        LendOrderExtProduct loep = lendOrderService.findFinancialPlanById(lendOrderId);
        //订单总资产
        BigDecimal cashAccount = BigDecimal.ZERO;
        BigDecimal realChangeInterest = lendOrderService.getRealChangeInterest(lendOrderId);
        cashAccount = loep.getBuyBalance().add(realChangeInterest != null ? realChangeInterest : BigDecimal.ZERO);
        //如果订单总资产  大于  购买金额+预期收益   页面的订单总资产就显示  购买金额+预期收益
        if (loep.getBuyBalance().add(loep.getExpectProfit()).compareTo(cashAccount) == -1) {
            mv.addObject("cashAccount", loep.getBuyBalance().add(loep.getExpectProfit()));
        } else {
            mv.addObject("cashAccount", cashAccount);
        }
        mv.addObject("loep", loep);

        mv.setViewName("/finance/myFinanceDetail");
        return mv;
    }

    /**
     * 债券列表
     *
     * @param request
     * @param session
     * @param pageSize
     * @param pageNo
     * @param lendOrderId
     * @return
     */

    @RequestMapping(value = "/findCreditorRightsByDetailList")
    @ResponseBody
    public Object findCreditorRightsByDetailList(HttpServletRequest request, HttpSession session,
                                                 @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                                 @RequestParam(value = "page", defaultValue = "1") int pageNo,
                                                 @RequestParam(value = "lendOrderId", required = false) Long lendOrderId) {

        return lendOrderService.findCreditorRightsByDetailList(pageSize, pageNo, lendOrderId);
    }

    /**
     * 跳转至出借债券页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toMyCreditRightList")
    public String toMyCreditRightList(HttpServletRequest request) {
        String flag = "";
        if (null != request.getParameter("flag") && !"".equals(request.getParameter("flag").trim())) {//flag为空是出借债权
            flag = request.getParameter("flag");
            //返回债权转让
        }
        return "/finance/creditRightList" + flag;//new
    }

    /**
     * 债券转让查询列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getTurnCreditRightList")
    @ResponseBody
    public Object getTurnCreditRightList(HttpServletRequest request, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                         String creditorRightsStatus, String backDate, String buyDate) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        CreditorRightsExtVo vo = new CreditorRightsExtVo();
        vo.setLendUserId(currentUser.getUserId());

        vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上

        //封装参数
        Map<String, Object> customParams = new HashMap<String, Object>();


        if (com.xt.cfp.core.util.StringUtils.isNull(creditorRightsStatus)) {
            String[] queryStatus = {CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.getValue(),
                    CreditorRightsConstants.CreditorRightsStateEnum.TURNOUT.getValue(),
                    CreditorRightsConstants.CreditorRightsStateEnum.COMPLETE.getValue(),
                    CreditorRightsConstants.CreditorRightsStateEnum.APPLYTURNOUT.getValue(),
                    CreditorRightsConstants.CreditorRightsStateEnum.TRANSFERING.getValue()};
            customParams.put("queryStatus", queryStatus);
        } else if (creditorRightsStatus.equals("8")) {
            customParams.put("fromWhere", CreditorRightsFromWhereEnum.TURN.getValue());
        } else {
            String[] queryStatus = getQueryStatus(creditorRightsStatus);
            if (queryStatus != null)
                customParams.put("queryStatus", queryStatus);
        }
        if (creditorRightsStatus.equals("6")) {
            customParams.put("rightsflag", "0");// 转让的标什
        }
        customParams.put("productTypeEnum", "productTypeEnum");
        customParams.put("orderBy", getOrderBy(backDate, buyDate));
        Pagination<CreditorRightsExtVo> creditorRightsPaging = creditorRightsService.getCreditorRightsPaging(pageNo, pageSize, vo, customParams);
        return creditorRightsPaging;
    }

    /**
     * 分页查询列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCreditRightList")
    @ResponseBody
    public Object getCreditRightList(HttpServletRequest request, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                     String creditorRightsStatus, String backDate, String buyDate) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        CreditorRightsExtVo vo = new CreditorRightsExtVo();
        vo.setLendUserId(currentUser.getUserId());
        //vo.setFromWhere(CreditorRightsFromWhereEnum.BUY.value2Char());//购买来源
        vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上


        //封装参数
        Map<String, Object> customParams = new HashMap<String, Object>();

        String[] queryStatus = getQueryStatus(creditorRightsStatus);
        if (queryStatus != null)
            customParams.put("queryStatus", queryStatus);
        customParams.put("orderBy", getOrderBy(backDate, buyDate));
        Pagination<CreditorRightsExtVo> creditorRightsPaging = creditorRightsService.getCreditorRightsPaging(pageNo, pageSize, vo, customParams);
        return creditorRightsPaging;
    }

    /**
     * 投标类支付订单，再次支付时，跳到支付页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toPayForLender")
    public String toPayForLender(HttpServletRequest request, String productType,
                                 @RequestParam(value = "lendOrderId") Long lendOrderId) {
        TokenUtils.setNewToken(request);
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        //身份验证
        if (!UserIsVerifiedEnum.BIND.getValue().equals(userExt.getIsVerified())) {
            throw new SystemException(UserErrorCode.HAS_NOT_IDVERIFIED).set("userId", currentUser.getUserId());
        }
        //校验-订单是否存在
        if (lendOrder == null) {
            throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderId", lendOrderId);
        }
        //校验-订单用户和当前用户必须相同
        if (!lendOrder.getLendUserId().equals(currentUser.getUserId())) {
            throw new SystemException(PayErrorCode.NOT_BELONG_PAY_ORDER).set("lendOrderUserId", lendOrder.getLendUserId()).set("user", currentUser.getUserId());
        }

        List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
        LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
        LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(lendOrderBidDetail.getLoanApplicationId());
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());

        LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());
        //校验业务数据
        checkBidLoanByPayCredit(loanApplicationListVO, lendOrder.getBuyBalance(), currentUser);
        //检查剩余金额
        BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
        if (lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy)) > 0) {
            //无法购买剩余标
            throw new SystemException(BidErrorCode.LENDORDER_AMOUNT_NOT_ENOUGH).set("amount", lendOrder.getBuyBalance()).set("remain", loanApplicationListVO.getConfirmBalance().subtract(totalBuy));
        }
        //预期收益
//        LoanProduct loanProduct = loanProductService.findById(loanApplicationListVO.getLoanProductId());
        //BigDecimal expectedInteresting = InterestCalculation.getExpectedInteresting(lendOrder.getBuyBalance(), lendOrder.getProfitRate(), lendOrder.getTimeLimitType().charAt(0), lendOrder.getTimeLimit());
//		System.out.println(	getExpectProfit(request,loanApplicationListVO.getLoanApplicationId(),lendOrder.getBuyBalance()));
        //getExpectProfit(request,loanApplicationId,new BigDecimal("100"));

//		BigDecimal expectedInteresting = null;
//		try {
//			expectedInteresting = InterestCalculation.getAllInterest(lendOrder.getBuyBalance(), lendOrder.getProfitRate(), lendOrder.getTimeLimitType().charAt(0),
//					loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
//			expectedInteresting = BigDecimalUtil.down(expectedInteresting,2);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        //借款申请描述

        //查询用户卡信息
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);


        //查询用户卡累计提现金额
        BigDecimal withdrawAmount = customerCard != null ? customerCardService.getCardWithdrawAmount(customerCard.getCustomerCardId()) : BigDecimal.ZERO;

        //todo 财富券实现
        List<VoucherVO> vouchers = voucherService.getAllVoucherList(currentUser.getUserId(), lendOrder.getBuyBalance(), VoucherConstants.UsageScenario.LOAN, VoucherConstants.UsageScenario.WHOLE);
        // 加息券
        List<RateUserVO> rateUsers = rateUserService.findRateUsersByUserId(currentUser.getUserId(), loanApplicationListVO.getCycleCount(), loanApplicationListVO.getLoanType(), lendOrder.getBuyBalance(), RateUserStatusEnum.UNUSED,
                RateUserStatusEnum.USING);

        request.setAttribute("productType", productType);
        request.setAttribute("order", lendOrder);
        request.setAttribute("loanApplication", loanApplicationListVO);
        request.setAttribute("withdrawAmount", withdrawAmount);
        request.setAttribute("amount", lendOrder.getBuyBalance());
        request.setAttribute("customerCard", customerCard);
        request.setAttribute("vouchers", vouchers);
        request.setAttribute("rateUsers", rateUsers);
        request.setAttribute("expectedInteresting", getExpectProfit(request, loanApplicationListVO.getLoanApplicationId(), lendOrder.getBuyBalance()));
        request.setAttribute("lendProduct", lendProduct);
        request.setAttribute("userExtInfo", userExt);
        request.setAttribute("lendProductPublish", lendProductPublish);
        request.setAttribute("userCashBalance", userAccountService.getCashAccount(currentUser.getUserId()).getAvailValue2());
        boolean isBidEqualLoginPass = userInfoService.isBidPassEqualLoginPass(currentUser.getUserId());
        request.setAttribute("isBidEqualLoginPass", isBidEqualLoginPass);
        return "order/payForOrder";
    }

    /**
     * 投标类支付订单，再次支付时，跳到支付页面
     *
     * @param request
     * @return wangyadong
     */
    @RequestMapping(value = "/toPayForCreditor")
    public String toPayForCreditor(HttpServletRequest request, @RequestParam(value = "lendOrderId") Long lendOrderId) {
        TokenUtils.setNewToken(request);
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        //身份验证
        if (!userExt.getIsVerified().equals("1")) {
            throw new SystemException(UserErrorCode.HAS_NOT_IDVERIFIED).set("userId", currentUser.getUserId());
        }
        //校验-订单是否存在
        if (lendOrder == null) {
            throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderId", lendOrderId);
        }
        //校验-订单用户和当前用户必须相同
        if (!lendOrder.getLendUserId().equals(currentUser.getUserId())) {
            throw new SystemException(PayErrorCode.NOT_BELONG_PAY_ORDER).set("lendOrderUserId", lendOrder.getLendUserId()).set("user", currentUser.getUserId());
        }

        List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
        LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
        LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(lendOrderBidDetail.getLoanApplicationId());
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());

        LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());
        //校验业务数据
        checkBidLoanByPayCredit(loanApplicationListVO, lendOrder.getBuyBalance(), currentUser);
        //检查剩余金额
        BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
        if (lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy)) > 0) {
            //无法购买剩余标
            throw new SystemException(BidErrorCode.LENDORDER_AMOUNT_NOT_ENOUGH).set("amount", lendOrder.getBuyBalance()).set("remain", loanApplicationListVO.getConfirmBalance().subtract(totalBuy));
        }
        //预期收益
        LoanProduct loanProduct = loanProductService.findById(loanApplicationListVO.getLoanProductId());
        //BigDecimal expectedInteresting = InterestCalculation.getExpectedInteresting(lendOrder.getBuyBalance(), lendOrder.getProfitRate(), lendOrder.getTimeLimitType().charAt(0), lendOrder.getTimeLimit());
        BigDecimal expectedInteresting = null;
        try {
            expectedInteresting = InterestCalculation.getAllInterest(lendOrder.getBuyBalance(), lendOrder.getProfitRate(), lendOrder.getTimeLimitType().charAt(0),
                    loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
            expectedInteresting = BigDecimalUtil.down(expectedInteresting, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //借款申请描述

        //查询用户卡信息
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);

        //查询用户卡累计提现金额
        BigDecimal withdrawAmount = customerCard != null ? customerCardService.getCardWithdrawAmount(customerCard.getCustomerCardId()) : BigDecimal.ZERO;

        //todo 财富券实现
        List<VoucherVO> vouchers = voucherService.getAllVoucherList(currentUser.getUserId(), lendOrder.getBuyBalance(), VoucherConstants.UsageScenario.LOAN, VoucherConstants.UsageScenario.WHOLE);

        request.setAttribute("order", lendOrder);
        request.setAttribute("loanApplication", loanApplicationListVO);
        request.setAttribute("withdrawAmount", withdrawAmount);
        request.setAttribute("amount", lendOrder.getBuyBalance());
        request.setAttribute("customerCard", customerCard);
        request.setAttribute("vouchers", vouchers);
        request.setAttribute("expectedInteresting", expectedInteresting);
        request.setAttribute("lendProduct", lendProduct);
        request.setAttribute("userExtInfo", userExt);
        request.setAttribute("creditorRightsApplyId", loanApplicationListVO.getCreditorRightsApplyId());
        request.setAttribute("lendProductPublish", lendProductPublish);
        request.setAttribute("userCashBalance", userAccountService.getCashAccount(currentUser.getUserId()).getAvailValue2());
        boolean isBidEqualLoginPass = userInfoService.isBidPassEqualLoginPass(currentUser.getUserId());
        request.setAttribute("isBidEqualLoginPass", isBidEqualLoginPass);
        if (customerCard == null) {
            // 连连没有绑定，查询易宝绑定情况
            CustomerCard ybBindCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.YB);
            if (null != ybBindCard) {// 易宝绑定了查看是否支持连连
                boolean support = LLPayUtil.checkLLCardSupport(ybBindCard.getCardCode());
                request.setAttribute("support", support);
                request.setAttribute("ybBindCard", ybBindCard);
                if (support) {// 支持
                    return "CreditroRights/hasCardBidCreditorPay";
                } else {// 不支持
                    return "CreditroRights/noCardBidCreditorPay";
                }
            } else {
                return "CreditroRights/noCardBidCreditorPay";
            }
        } else {
            return "CreditroRights/hasCardBidCreditorPay";
        }
//		if (customerCard == null){
//			return "order/noCardBidLoanPay";
//		else
//			return "order/hasCardBidLoanPay";
    }

    public String getOrderBy(String backDate, String buyDate) {

        String orderBy = "";
        String str1 = StringUtils.isEmpty(backDate) ? null : (backDate.equals("D") ? "CURRENT_PAY_DATE desc" : "CURRENT_PAY_DATE asc");
        String str2 = StringUtils.isEmpty(buyDate) ? null : (buyDate.equals("D") ? " LOBD.BUY_DATE  desc" : " LOBD.BUY_DATE  asc");


        orderBy += str1 == null ? "" : str1;
        orderBy += str2 == null ? "" : (StringUtils.isEmpty(orderBy) ? str2 : "," + str2);
        return orderBy;
    }

    private String[] getQueryStatus(String status) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(status)) {
            if (status.indexOf("0") != -1) {
                return null;
            }
            if (status.indexOf("1-2-3") != -1) {
                return null;
            }
            if (status.indexOf("1") != -1) {
                return status.replace("1", "0-1").split("-");
            }
            if (status.indexOf("2") != -1) {
                return status.replace("2", "8").split("-");
            }

            if (status.indexOf("3") != -1) {
                return status.replace("3", "2-3-7").split("-");
            }
            if (status.indexOf("4") != -1) {
                return status.replace("4", "1-2-5-6-8").split("-");
            }
            if (status.indexOf("7") != -1) {
                return status.replace("7", "2").split("-");
            }
            return null;
        }
        return null;
    }

    /**
     * 理财详情页
     *
     * @param
     * @return 详情页面
     * @Date 2015/08/28 15/54
     * @auhthor wangyadong
     */
    @RequestMapping(value = "/SendCreditRightList")
    public String SendCreditRightList(HttpServletRequest req, String bangDingDetails,
                                      HttpSession session, Model model) {
        System.out.println("==========send值" + bangDingDetails);
//		req.setAttribute("bangDingDetails", bangDingDetails);
//		model.addAttribute("bangDingDetailsByModel", bangDingDetails);
        session.setAttribute("bangDingDetailsBySeesion", bangDingDetails);
        return "";
    }

    /**
     * 理财详情页
     *
     * @param
     * @return 详情页面
     * @Date 2015/08/28 19/15
     * @auhthor wangyadong
     */
    @RequestMapping(value = "/SendCreditRightListByShowPage")
    public String SendCreditRightListByShowPage() {

        return "/finance/FinancingProductmoneyinfo";
    }

    /**
     * 【微信-新】
     * 跳转到：已投债权，分页查询列表
     *
     * @return
     */
    @RequestMapping(value = "/creditRightList")
    public String creditRightList() {
        return "/finance/creditRightList";
    }

    /**
     * 【微信-新】
     * 跳转到：已投债权，查询详情
     *
     * @return
     */
    @RequestMapping(value = "/creditRightDetail", method = RequestMethod.POST)
    public String creditRightDetail(HttpServletRequest request,
                                    @RequestParam(value = "creditorRightsId", required = false) String creditorRightsId) {
        request.setAttribute("creditorRightsId", creditorRightsId);
        CreditorRightsExtVo vo = creditorRightsService.getCreditorRightsDetailById(Long.valueOf(creditorRightsId));
        RateLendOrder activityOrder = rateLendOrderService.findByLendOrderId(vo.getLendOrderId(), RateLendOrderTypeEnum.ACTIVITY.getValue(), null);

        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
        if (userInfo.getUserId().compareTo(vo.getLendUserId()) != 0) {
            request.setAttribute("errorMsg", "非法参数");
            return "/error";
        }

        if (activityOrder != null) {
            request.setAttribute("activityOrder", activityOrder);
        }
        RateLendOrder rateOrder = rateLendOrderService.findByLendOrderId(vo.getLendOrderId(), RateLendOrderTypeEnum.RATE_COUPON.getValue(), null);
        if (rateOrder != null) {
            request.setAttribute("rateOrder", rateOrder);
        }
        return "/finance/creditRightDetail";
    }

    /**
     * 【微信-新】
     * 跳转到：已投债权转让，查询详情
     *
     * @return
     * @author wangyadong
     */
    @RequestMapping(value = "/getTrunCreditRightDetail")
    public String getTrunCreditRightDetail(HttpServletRequest request,
                                           @RequestParam(value = "creditorRightsId", required = false) String creditorRightsId
            , @RequestParam(value = "creditorRightsApplyId", defaultValue = "") String creditorRightsApplyId) {
        request.setAttribute("creditorRightsId", creditorRightsId);
        request.setAttribute("creditorRightsApplyId", creditorRightsApplyId);
        CreditorRightsExtVo vo = creditorRightsService.getCreditorRightsDetailById(Long.valueOf(creditorRightsId));
        RateLendOrder activityOrder = rateLendOrderService.findByLendOrderId(vo.getLendOrderId(), RateLendOrderTypeEnum.ACTIVITY.getValue(), null);
        if (activityOrder != null) {
            request.setAttribute("activityOrder", activityOrder);
        }
        RateLendOrder rateOrder = rateLendOrderService.findByLendOrderId(vo.getLendOrderId(), RateLendOrderTypeEnum.RATE_COUPON.getValue(), null);
        if (rateOrder != null) {
            request.setAttribute("rateOrder", rateOrder);
        }
        return "/CreditroRights/creditRightDetail";
    }


    /**
     * 【微信-新】
     * 已投债权，分页查询列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCreditRightList1", method = RequestMethod.POST)
    @ResponseBody
    public Object getCreditRightList1(HttpServletRequest request,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                      @RequestParam(value = "creditorRightsStatus", required = false) String creditorRightsStatus,
                                      @RequestParam(value = "backDate", defaultValue = "", required = false) String backDate,
                                      @RequestParam(value = "buyDate", defaultValue = "", required = false) String buyDate) {

        try {
            //登录验证
            UserInfo currentUser = SecurityUtil.getCurrentUser(true);

            CreditorRightsExtVo vo = new CreditorRightsExtVo();
            vo.setLendUserId(currentUser.getUserId());
            //vo.setFromWhere(CreditorRightsFromWhereEnum.BUY.value2Char());//购买来源
            vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上

            //封装参数
            Map<String, Object> customParams = new HashMap<String, Object>();
            //判断是否要加入已结清
            String[] queryStatus = getQueryStatus(creditorRightsStatus);
            if (queryStatus != null) {
                customParams.put("queryStatus", queryStatus);
            }


            customParams.put("orderBy", getOrderBy(backDate, buyDate));
            Pagination<CreditorRightsExtVo> pagination = creditorRightsService.getCreditorRightsPaging(pageNo, pageSize, vo, customParams);

            //返回结果
            Map<String, Object> resultMap = new HashMap<String, Object>();
            List<CreditorRightsExt> creVOs = new ArrayList<>();
            CreditorRightsExt creVO = null;
            DecimalFormat df = new DecimalFormat("#,##0.00");

            List<CreditorRightsExtVo> crevList = pagination.getRows();
            if (null != crevList && crevList.size() > 0) {
                for (CreditorRightsExtVo v : crevList) {
                    creVO = new CreditorRightsExt();
                    int agreementDate = DateUtil.daysBetween(v.getCreateTime(), new Date());
                    creVO.setAgreementDays(agreementDate);
                    creVO.setCreditorRightsName(v.getCreditorRightsName());//债权名称(借款标题)
                    creVO.setLoanLoginName(v.getLoanLoginName());//借款人
                    creVO.setBuyPrice(df.format(v.getBuyPrice()));//出借金额（元）
                    creVO.setWaitTotalpayMent(df.format(v.getWaitTotalpayMent()));//待收回款（元）
                    creVO.setFactBalance(df.format(v.getFactBalance()));//已收回款（元）
                    creVO.setCurrentPayDate(DateUtil.getDateLong(v.getCurrentPayDate()));//最近回款日
                    if ("0".equals(String.valueOf(v.getRightsState())) || "1".equals(String.valueOf(v.getRightsState()))) {
                        creVO.setRightsState("回款中");//债权状态:如果值为 0已生效 或 1还款中，则统一显示 回款中
                    } else {
                        creVO.setRightsState(CreditorRightsStateEnum.getCreditorRightsStateEnumByValue(String.valueOf(v.getRightsState())).getDesc());//债权状态
                    }
                    creVO.setLoanType(v.getLoanType());
                    creVO.setBuyDate(DateUtil.getDateLong(v.getBuyDate()));//投标日期
                    creVO.setExpectProfit(df.format(v.getExpectProfit()));//预期收益
                    creVO.setCreditorRightsId(String.valueOf(v.getCreditorRightsId()));//债权ID
                    creVO.setCycleCount(v.getLoanApplicationListVO().getCycleCount() + "个月");//出借时长
                    if (v.getLoanType().equals(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue())) {
                        creVO.setCycleCount(v.getLoanApplicationListVO().getCycleCount() * 14 + "天");
                    }
                    creVO.setAnnualRate(v.getLoanApplicationListVO().getAnnualRate() + "%");//年化利率
                    creVO.setRepayMentMethod(v.getLoanApplicationListVO().getRepayMentMethod());//还款方式
                    creVO.setFromWhere(v.getFromWhere());
                    creVO.setCreditorRightsApplyId(v.getCreditorRightsApplyId());
                    if ("7".equals(String.valueOf(v.getRightsState()))) {// 提前结清
                        // 查询提前结清时间
                        List<RepaymentPlan> repaymentPlans = repaymentPlanService
                                .getRepaymentPlansByloanApplicationId(
                                        v.getLoanApplicationId(),
                                        ChannelTypeEnum.ONLINE);
                        for (int k = 0; k < repaymentPlans.size(); k++) {
                            if (RepaymentPlanStateEnum.BEFORE_COMPLETE.value2Char() ==
                                    repaymentPlans.get(k).getPlanState()) {
                                creVO.setRepaymentRecord(repaymentRecordService
                                        .getRecentRepaymentRecordByRepaymentId(repaymentPlans
                                                .get(k).getRepaymentPlanId()));
                                break;
                            }
                        }
                        // 获取 repaymentPlanId 提前还款时间
                    }
                    creVO.setDetails(v.getRightsRepaymentDetailList());
                    creVOs.add(creVO);
                }
            }

            if ("1".equals(creditorRightsStatus)) {
                resultMap.put("index", "0");
            } else if ("3".equals(creditorRightsStatus)) {
                resultMap.put("index", "1");
            } else {
                resultMap.put("index", "");
            }

            resultMap.put("pageSize", pagination.getPageSize());
            resultMap.put("pageNo", pagination.getCurrentPage());
            resultMap.put("total", pagination.getTotal());
            resultMap.put("totalPage", pagination.getTotalPage());
            resultMap.put("rows", creVOs);
            resultMap.put("newDate", new Date());
            return returnResultMap(true, resultMap, null, null);
        } catch (SystemException se) {
            logger.error(se.getMessage(), se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }

    /**
     * 【微信-新】
     * 已投债权，查询详情
     *
     * @param request
     * @param creditorRightsId
     * @return
     */
    @RequestMapping(value = "/getCreditRightDetail", method = RequestMethod.POST)
    @ResponseBody
    public Object getCreditRightDetail(HttpServletRequest request,
                                       @RequestParam(value = "creditorRightsId", defaultValue = "") String creditorRightsId
            , @RequestParam(value = "creditorRightsApplyId", defaultValue = "") String creditorRightsApplyId
    ) {
        //getCreditRightDetail	 getCreditRightDetail
        try {
            //登录验证
            UserInfo currentUser = SecurityUtil.getCurrentUser(true);
            if (null == currentUser) {
                return returnResultMap(false, null, "needlogin", "请先登录");
            }
            //参数验证
            if (null == creditorRightsId || "".equals(creditorRightsId)) {
                return returnResultMap(false, null, "check", "参数不能为空");
            }


            //根据债权ID加载一条债权数据
            CreditorRightsExtVo vo = creditorRightsService.getCreditorRightsDetailById(Long.valueOf(creditorRightsId));
            if (null == vo) {
                return returnResultMap(false, null, "check", "参数不合法");
            }

            // 【获取数据-开始】
            vo.setCreditorRightsCode(PropertiesUtils.getInstance().get("AGREEMENT_VISIT_PATH") + vo.getCreditorRightsCode());
            //获得借款申请信息
            LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(vo.getLoanApplicationId());
            //计算预期收益
            LoanProduct loanProduct = loanProductService.findById(loanApplicationListVO.getLoanProductId());
            try {
                BigDecimal expectProfit = InterestCalculation.getAllInterest(vo.getBuyPrice(), vo.getAnnualRate(), loanProduct.getDueTimeType(), loanApplicationListVO.getRepayMentMethod().charAt(0), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());

                vo.setExpectProfit(expectProfit);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //处理还款方式
            if (loanApplicationListVO.getRepayMethod().equals("1")) {
                loanApplicationListVO.setRepayMentMethod(PaymentMethodEnum.getPaymentMethod(loanApplicationListVO.getRepayMentMethod()).getDesc());
            } else {
                ConstantDefine repaymentMode = constantDefineCached.getConstantByValue("repaymentMode", loanApplicationListVO.getRepayMethod());
                loanApplicationListVO.setRepayMentMethod(repaymentMode.getConstantName());
            }
            vo.setLoanApplicationListVO(loanApplicationListVO);
            //获得出借产品所有费用项
            List<LendProductFeesItem> feeitems = lendProductService.findAllProductFeesItemsByLendOrderId(vo.getLendOrderId());
            //需要计算回款时的费用项
            List<LendProductFeesItem> atcycleFeeitems = new ArrayList<LendProductFeesItem>();
            for (LendProductFeesItem feesItem : feeitems) {
                if (FeesPointEnum.ATCYCLE.value2Char() == feesItem.getChargePoint()) {
                    atcycleFeeitems.add(feesItem);
                }
            }
            //获得费用
            LendOrder lendOrder = lendOrderService.findById(vo.getLendOrderId());
            List<LendLoanBinding> byLendAndLoan = lendLoanBindingService.findByLendAndLoan(lendOrder.getLendProductId(), loanProduct.getLoanProductId());
            List<FeesItem> fis = new ArrayList<FeesItem>();
            if (byLendAndLoan != null) {
                for (LendLoanBinding llb : byLendAndLoan) {
                    if (llb.getFeesItemId() != null && llb.getFeesItemId() > 0) {
                        fis.add(feesItemService.findById(llb.getFeesItemId()));
                    }

                }
            }
            List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(vo.getCreditorRightsId());
            //最近还款日期
            try {
                vo.setCurrentPayDate(getRecentPayDate(detailRightsList));
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("最近还款日期计算出错", e);
            }
            //处理一下明细，添加待缴费用
            BigDecimal exceptProfit = lendOrderReceiveService.getExceptProfitByUserId(vo.getLendOrderId(), currentUser.getUserId(), LendProductTypeEnum.RIGHTING);
            for (RightsRepaymentDetail detail : detailRightsList) {
                //计算费用
                BigDecimal fees = BigDecimal.ZERO;
                if (atcycleFeeitems != null && atcycleFeeitems.size() > 0) {
                    for (LendProductFeesItem item : atcycleFeeitems) {
                        BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(item, BigDecimal.ZERO, BigDecimal.ZERO, detail.getShouldCapital2(), detail.getShouldInterest2(), exceptProfit, detail.getShouldInterest2());
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
                detail.setShouldFee(BigDecimalUtil.up(fees, 2));
                BigDecimal defaultInterest = defaultInterestDetailService.getDefaultInterestByRepaymentPlanId(detail.getRepaymentPlanId());
                detail.setDefaultInterest(defaultInterest);
            }
            //获取投标类待回款(利息+本金)
            BigDecimal capitalRecive = lendOrderReceiveService.getCapitalReciveByUserId(vo.getLendOrderId(), currentUser.getUserId(), LendProductTypeEnum.RIGHTING);
            BigDecimal interestRecive = lendOrderReceiveService.getInterestReciveByUserId(vo.getLendOrderId(), currentUser.getUserId(), LendProductTypeEnum.RIGHTING);
            vo.setRightsRepaymentDetailList(detailRightsList);
            vo.setWaitTotalpayMent(capitalRecive.add(interestRecive));
            //计算预期收益
            vo.setExpectProfit(exceptProfit);

            // 【获取数据-结束】

            //返回结果
            Map<String, Object> resultMap = new HashMap<String, Object>();
            CreditorRightsExt creVO = new CreditorRightsExt();
            DecimalFormat df = new DecimalFormat("#,##0.00");
            //债权详情
            creVO.setCreditorRightsName(vo.getCreditorRightsName());//债权名称(借款标题)
            creVO.setLoanLoginName(vo.getLoanLoginName());//借款人
            creVO.setBuyPrice(df.format(vo.getBuyPrice()));//出借金额（元）
            creVO.setWaitTotalpayMent(df.format(vo.getWaitTotalpayMent()));//待收回款（元）
            creVO.setFactBalance(df.format(vo.getFactBalance()));//已收回款（元）
            creVO.setCurrentPayDate(DateUtil.getDateLong(vo.getCurrentPayDate()));//最近回款日
            creVO.setRightsState(CreditorRightsStateEnum.getCreditorRightsStateEnumByValue(String.valueOf(vo.getRightsState())).getDesc());//债权状态
            creVO.setBuyDate(DateUtil.getDateLong(vo.getBuyDate()));//投标日期
            creVO.setExpectProfit(df.format(vo.getExpectProfit()));//预期收益
            creVO.setCreditorRightsId(String.valueOf(vo.getCreditorRightsId()));//债权ID
            creVO.setCycleCount(vo.getLoanApplicationListVO().getCycleCount() + "个月");//出借时长
            if (loanApplicationListVO.getLoanType().equals(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue())) {
                creVO.setCycleCount(vo.getLoanApplicationListVO().getCycleCount() * 14 + "天");//出借时长
            }
            creVO.setAnnualRate(vo.getLoanApplicationListVO().getAnnualRate() + "%");//年化利率
            creVO.setRepayMentMethod(vo.getLoanApplicationListVO().getRepayMentMethod());//还款方式
            creVO.setFromWhere(vo.getFromWhere());
            creVO.setAwardPoint(vo.getAwardPoint());
            creVO.setAwardRate(vo.getAwardRate());


            creVO.setCreditorRightsApplyId(vo.getCreditorRightsApplyId());
            resultMap.put("creditorRightsDetail", creVO);
            //回款列表
            List<RightsRepaymentDetailExt> detailVOList = new ArrayList<RightsRepaymentDetailExt>();
            BigDecimal bd = BigDecimal.ZERO;
            RightsRepaymentDetailExt detailVO = null;
            if (null != detailRightsList && detailRightsList.size() > 0) {
                for (RightsRepaymentDetail detail : detailRightsList) {
                    detailVO = new RightsRepaymentDetailExt();

                    detailVO.setSectionCode(String.valueOf(detail.getSectionCode()));//回款期
                    detailVO.setRepaymentDayPlanned(DateUtil.getDateLong(detail.getRepaymentDayPlanned()));//回款日期
                    detailVO.setShouldCapital2(df.format(detail.getShouldCapital2()));//应回本金（元）
                    detailVO.setShouldInterest2(df.format(detail.getShouldInterest2()));//应回利息（元）
                    detailVO.setDefaultInterest(df.format(detail.getDefaultInterest()));//罚息（元）
                    detailVO.setShouldFee(df.format(detail.getShouldFee()));//应缴费用（元）
                    detailVO.setAllBackMoney(df.format(detail.getShouldCapital2().add(detail.getShouldInterest2()).add(detail.getDefaultInterest()).subtract(detail.getShouldFee())));//应回款总额（元）
                    detailVO.setFactMoney(df.format(detail.getFactBalance().add(detail.getDepalFine())));//已回款总额（元）
                    detailVO.setRightsDetailState(RightsRepaymentDetailStateEnum.getRightsRepaymentDetailStateEnumByValue(String.valueOf(detail.getRightsDetailState())).getDesc());//状态

                    if ("已还清".equals(detailVO.getRightsDetailState())) {//已结清状态
                        BigDecimal add2 = detail.getFactBalance().add(detail.getDepalFine());
                        BigDecimal subtract = add2.subtract(detail.getShouldCapital2()).subtract(detail.getShouldFee());
                        bd = bd.add(subtract);
                        //bd=bd.add(add);
                    }
                    detailVOList.add(detailVO);
                }
            }
            creVO.setExpectProfit(df.format(bd));
            resultMap.put("creditorRightsDetail", creVO);
            //CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);   ,@RequestParam(value = "creditorRightsApplyId", defaultValue = "") Long creditorRightsApplyId
            //加载债权名字
            if (creditorRightsApplyId != null && !"".equals(creditorRightsApplyId.trim()) && !"null".equals(creditorRightsApplyId)) {
                CreditorRightsTransferApplication crta = creditorRightsTransferAppService.findById(Long.valueOf(creditorRightsApplyId));
                Long creditorRightId = crta.getApplyCrId();
                CreditorRights creditorRights = creditorRightsService.findById(creditorRightId, false);
                UserInfoExt creditLend = userInfoExtService.getUserInfoExtById(creditorRights.getLendUserId());
                resultMap.put("lendCustomerName", creditLend.getJMRealName());
            }
            resultMap.put("repaymentList", detailVOList);

            return returnResultMap(true, resultMap, null, null);
        } catch (SystemException se) {
            logger.error(se.getMessage(), se);
            return returnResultMap(false, null, "exception", se.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return returnResultMap(false, null, "exception", e.getMessage());
        }
    }

    /**
     * 工具方法，计算最新还款日期
     *
     * @param detailRightsList
     * @return
     * @throws ParseException
     */
    private Date getRecentPayDate(List<RightsRepaymentDetail> detailRightsList) throws ParseException {
        Date date = new Date();
        //一期
        if (detailRightsList.size() == 1)
            return detailRightsList.get(0).getRepaymentDayPlanned();
        //多期
        for (int i = 0; i < detailRightsList.size(); i++) {
            Date repaymentDayPlanned = detailRightsList.get(i).getRepaymentDayPlanned();

            if (i == 0) {
                int i1 = DateUtil.secondBetween(date, repaymentDayPlanned);
                if (i1 > 0) {
                    //当前时间没有到达第一期还款时间
                    return repaymentDayPlanned;
                }
            } else if (i == (detailRightsList.size() - 1)) {
                //最后一次还款
                return repaymentDayPlanned;
            } else {
                int i3 = DateUtil.secondBetween(date, repaymentDayPlanned);
                if (i3 > 0) {
                    //当前时间没有达到当前期
                    return repaymentDayPlanned;
                }
            }
        }
        return null;
    }


    /**
     * 跳转到，定向标，定向密码验证页面
     *
     * @param request
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "/financialPsw")
    public String financialPsw(HttpServletRequest request,
                               @RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId,
                               @RequestParam(value = "amount", required = false) BigDecimal amount) {
        request.setAttribute("loanApplicationId", loanApplicationId);
        request.setAttribute("amount", amount);
        return "/finance/financialPsw";
    }

    /**
     * 执行：验证定向标，定向密码
     *
     * @param pass
     * @param loanApplicationId
     * @return
     */
    @RequestMapping(value = "/getPass")
    @ResponseBody
    public String getPass(String pass, Long loanApplicationId) {
        String pass1 = loanApplicationService.getLoanApplicationPass(loanApplicationId);
        String passwordCiphertext = MD5Util.MD5Encode(pass, "utf-8");
        if (pass1.equals(passwordCiphertext)) {
            return "success";
        }
        return "fail";
    }

    /**
     * 债券转让详情
     *
     * @param request
     * @param creditorRightsApplyId
     * @return
     */
    @SuppressWarnings("unchecked")
    @DoNotNeedLogin
    @RequestMapping(value = "/creditRightBidding")
    public String creditRightBidding(HttpServletRequest request, @RequestParam(value = "creditorRightsApplyId", required = false) Long creditorRightsApplyId) {
        if (creditorRightsApplyId == null) {
            // 非法请求
            return "error";
        }
        CreditorRightsTransferApplication crta = creditorRightsTransferAppService.findById(creditorRightsApplyId);
        if (null == crta) {
            // 非法请求
            return "error";
        }
        Long creditorRightsId = crta.getApplyCrId();
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        long loanApplicationNo = creditorRights.getLoanApplicationId();
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
        if (loanApplicationListVO == null) {
            // 非法请求
            return "error";
        }
        request.setAttribute("creditor_type", "zqzr");
        //发标信息
        LoanPublish loanPublish = loanPublicService.findLoanPublishVO(loanApplicationNo);
        request.setAttribute("loanPublish", loanPublish);
        if (loanPublish == null) {
            //非法请求
            return "error";
        }

        if (PublishTarget.BACKGROUND.getValue().equals(loanPublish.getPublishTarget())) {
            //非法请求
            return "error";
        }
        //担保公司
        if (loanPublish.getCompanyId() != null) {
            GuaranteeCompany company = guaranteeCompanyService.getGuaranteeCompanyByCompanyId(loanPublish.getCompanyId());
            request.setAttribute("guaranteeCompany", company);
        }
        request.setAttribute("loanApplicationNo", loanApplicationNo);
        //token
        String token = TokenUtils.setNewToken(request);
        request.setAttribute("token", token);
        //组织页面返回值
        request.setAttribute("repayMentMethod", PaymentMethodEnum.values());
        request.setAttribute("guaranteeType", GuaranteeTypeEnum.values());
        //用户是否登陆
        loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());

        HttpSession session = WebUtil.getHttpServletRequest().getSession();
        UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);

        if (user != null) {
            //获取账户余额
            UserAccount cashAccount = userAccountService.getCashAccount(user.getUserId());
            UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
            request.setAttribute("userExt", userExt);
            request.setAttribute("cashAccount", cashAccount);
            //计算账户已投金额
            BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(user.getUserId(), loanApplicationListVO.getLoanApplicationId());
            if (totalLendAmountPerson != null) {
                if (loanApplicationListVO.getMaxBuyBalance() == null) {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
                } else {
                    loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
                }
            }

            //个人财富券统计
            List<VoucherVO> useageList = voucherService.getAllVoucherList(user.getUserId(), null, VoucherConstants.UsageScenario.LOAN, VoucherConstants.UsageScenario.WHOLE);
            if (useageList != null) {
                request.setAttribute("useageCount", useageList.size());
                BigDecimal useageSum = BigDecimal.ZERO;
                for (VoucherVO vo : useageList) {
                    useageSum = useageSum.add(vo.getVoucherValue());
                }
                request.setAttribute("useageSum", useageSum);

                List<VoucherProductVO> voucherProductVOs = voucherService.getVoucherStatistics(user.getUserId(), VoucherConstants.UsageScenario.LOAN, VoucherConstants.UsageScenario.WHOLE);
                request.setAttribute("voucherProductVOs", voucherProductVOs);
            }

        }
        List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRightsId);
        BigDecimal shouldCapital = BigDecimal.ZERO;
        int surpMonth = 0;
        Date nextRepaymentDay = null;
        for (RightsRepaymentDetail detailRights : detailRightsList) {
            if (RightsRepaymentDetailIsPayOffEnum.ISPAYOFF_NO.getValue().equals(String.valueOf(detailRights.getIsPayOff()))) {
                shouldCapital = shouldCapital.add(detailRights.getShouldCapital2());
                surpMonth++;
                if (nextRepaymentDay == null) {
                    nextRepaymentDay = detailRights.getRepaymentDayPlanned();
                }
            }
        }

        if (nextRepaymentDay == null) {
            request.setAttribute("nextRepaymentDay", 0);
        } else {
            request.setAttribute("nextRepaymentDay", nextRepaymentDay.getTime() / 1000);
        }

        //已转出债权
        BigDecimal lendRightsBalance = creditorRightsTransferAppService.getRemainingRightsPrice(creditorRightsApplyId);

        // 百分比
        String ratePercent = null;
        if (crta.getBusStatus().equals(CreditorRightsTransferAppStatus.SUCCESS.getValue())) {
            ratePercent = "100";
        } else if (crta.getBusStatus().equals(CreditorRightsTransferAppStatus.OVERDUE.getValue()) || crta.getBusStatus().equals(CreditorRightsTransferAppStatus.CANCEL.getValue())) {
            ratePercent = "0";
        } else {
            ratePercent = lendRightsBalance.multiply(new BigDecimal("100")).divide(crta.getApplyPrice(), 2, BigDecimal.ROUND_HALF_UP) + "";
            ratePercent = ratePercent.replaceAll("\\.00", "");
        }
        //限投
        loanApplicationListVO.setMaxBuyBalanceNow(lendRightsBalance);
        //出售金额
        loanApplicationListVO.setConfirmBalance(crta.getApplyPrice());
        request.setAttribute("lendRightsBalance", crta.getApplyPrice().subtract(lendRightsBalance));
        request.setAttribute("ratePercent", ratePercent);
        request.setAttribute("loanApplicationListVO", loanApplicationListVO);
        request.setAttribute("shouldCapital", creditorRights.getRightsWorth());//剩余本金
        request.setAttribute("surpMonth", surpMonth);//剩余还款月
        request.setAttribute("creditorRightsId", creditorRights.getCreditorRightsId());
        request.setAttribute("creditorRightsApplyId", crta.getCreditorRightsApplyId());

        int residualTime = 0;// 下一个还款日距离当前时间相差秒数
        try {
            residualTime = DateUtil.secondBetween(new Date(), nextRepaymentDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        request.setAttribute("residualTime", residualTime);

        //附件快照
        List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getcustomerUploadAttachment(loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());
        request.setAttribute("customerUploadSnapshots", customerUploadSnapshots);
        UserInfoExt creditLend = userInfoExtService.getUserInfoExtById(creditorRights.getLendUserId());
        request.setAttribute("lendCustomerName", creditLend.getJMRealName());//转让人
        Pagination<LenderRecordVO> obj = (Pagination<LenderRecordVO>) getLender(request, loanApplicationListVO.getLoanApplicationId(), 1, 10);
        request.setAttribute("totalNum", obj.getTotal());
        //附件快照
        request.setAttribute("customerPicNum", customerUploadSnapshots != null ? customerUploadSnapshots.size() : 0);
        if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())) {
            //个人投标详情页
            personLoanApplication(request, loanApplicationListVO, loanPublish);
        } else {
            //企业投标详情页
            enterpriseLoanApplication(request, loanApplicationListVO, loanPublish);
        }
        return "/finance/finance";
    }


    /**
     * 债权转让投标列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCreditorRightsLender")
    @ResponseBody
    @DoNotNeedLogin
    public Object getCreditorRightsLender(HttpServletRequest request, Long creditorRightsId,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        Pagination<LenderRecordVO> result = lendOrderBidDetailService.getCreditorRightsLenderPaging(pageNo, pageSize, creditorRightsId);
        return result;
    }


    /**
     * 转让债券详情
     *
     * @param request
     * @param creditorRightsId
     * @return
     */
    @RequestMapping(value = "/turnCreditRightBidding")
    public String turnCreditRightBidding(HttpServletRequest request, @RequestParam(value = "creditorRightsId", required = false) Long creditorRightsId) {
        if (creditorRightsId == null) {
            //非法请求
            return "error";
        }
        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(creditorRights.getLoanApplicationId());
        if (loanApplicationListVO == null) {
            //非法请求
            return "error";
        }
        //token
        String token = TokenUtils.setNewToken(request);
        request.setAttribute("token", token);
        //组织页面返回值
        request.setAttribute("repayMentMethod", PaymentMethodEnum.values());
        request.setAttribute("guaranteeType", GuaranteeTypeEnum.values());

        List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRightsId);
        BigDecimal shouldCapital = BigDecimal.ZERO;
        int surpMonth = 0;
        for (RightsRepaymentDetail detailRights : detailRightsList) {

            shouldCapital = shouldCapital.add(detailRights.getShouldCapital2().subtract(detailRights.getFactCalital()));
            if (RightsRepaymentDetailIsPayOffEnum.ISPAYOFF_NO.getValue().equals(String.valueOf(detailRights.getIsPayOff()))) {
                surpMonth++;
            }

        }
        request.setAttribute("shouldCapital", shouldCapital);//剩余本金
        request.setAttribute("surpMonth", surpMonth);//剩余还款月
        request.setAttribute("loanApplicationListVO", loanApplicationListVO);
        request.setAttribute("creditorRights", creditorRights);
        long termDay = repaymentPlanService.getTermDay(detailRightsList);
        request.setAttribute("termDay", termDay);

        return "/finance/myTurnCreditRightsDetail";
    }

    /**
     * 用余额、财富券购买债权
     *
     * @param password
     * @param amount
     * @return
     */
    @RequestMapping(value = "/buyRightsByAccountBalance")
    @ResponseBody
    public String buyRightsByAccountBalance(@RequestParam(value = "psw", required = false) String password,
                                            @RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId,
                                            @RequestParam(value = "creditorRightsApplyId", required = false) Long creditorRightsApplyId, Long lendOrderId,
                                            @RequestParam(value = "amount", required = false) BigDecimal amount,
                                            @RequestParam(value = "accountPayValue", defaultValue = "0") BigDecimal accountPayValue,
                                            @RequestParam(value = "userVoucherId", required = false) String voucherIds,
                                            HttpServletRequest request) {
        ValidationUtil.checkRequiredPara(new NameValue<String, Object>("password", password),
                new NameValue<String, Object>("loanApplicationId", loanApplicationId),
                new NameValue<String, Object>("amount", amount));
        LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(loanApplicationId);
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        TokenUtils.validateToken(request);

        //校验交易密码
        if (currentUser.getBidPass() == null) {
            request.setAttribute("errorMsg", UserErrorCode.NO_BIDPASS.getDesc());
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.NO_BIDPASS.getDesc()).toJson();
        }
        if (!MD5Util.MD5Encode(password, null).equals(currentUser.getBidPass())) {
            request.setAttribute("errorMsg", UserErrorCode.ERROR_BIDPASS.getDesc());
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.ERROR_BIDPASS.getDesc()).toJson();
        }

        //校验身份认证状态
        if (!this.userInfoService.hasIdentityAuthentication(currentUser.getUserId())) {
            request.setAttribute("errorMsg", UserErrorCode.HAS_NOT_IDVERIFIED.getDesc());
            return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.HAS_NOT_IDVERIFIED.getDesc()).toJson();
        }

        //验证购买金额是否合法(不大于0的情况)
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            request.setAttribute("errorMsg", BidErrorCode.AMOUNT_ILLEGAL.getDesc());
            return "/order/payError";
        }

        HttpSession session = WebUtil.getHttpServletRequest().getSession();
        List<VoucherVO> voucherVOs = new ArrayList<VoucherVO>();
        BigDecimal voucherPayValue = BigDecimal.ZERO;
        String[] voucher_Ids = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)) {
            voucher_Ids = voucherIds.split(",");
            //财富券
            for (String voucherId : voucher_Ids) {
                VoucherVO vo = voucherService.getVoucherById(Long.valueOf(voucherId));
                voucherVOs.add(vo);
                //计算财富券总额
                voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
            }

        }

        UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);

        CreditorRightsTransferApplication crta = null;
        if (null == lendOrderId) {
            crta = creditorRightsTransferAppService.findById(creditorRightsApplyId);
        } else {
            crta = creditorRightsTransferAppService.getTransferApplicationByLendOrderId(lendOrderId);
        }
        if (null == crta) {
            request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_REVOKE.getDesc());
            return JsonView.JsonViewFactory.create().success(false).info(CreditorErrorCode.CREDITOR_RESIDUAL_REVOKE.getDesc()).toJson();
        }


        if (user != null) {
            //获取账户余额
            UserAccount cashAccount = userAccountService.getCashAccount(user.getUserId());
            if (cashAccount.getAvailValue2().compareTo(accountPayValue) < 0) {
                request.setAttribute("errorMsg", UserAccountErrorCode.ACCOUNT_BALANCE_AVAIVALUE_INSUFFICIENT.getDesc());
                return JsonView.JsonViewFactory.create().success(false).info(UserAccountErrorCode.ACCOUNT_BALANCE_AVAIVALUE_INSUFFICIENT.getDesc()).toJson();
            }
            //已经购买的债权金额
            BigDecimal totalBuy = creditorRightsTransferAppService.getRemainingRightsPrice(crta.getCreditorRightsApplyId());//lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());

            if (crta.getApplyUserId().equals(user.getUserId())) {
                request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_MYSELF.getDesc());
                return JsonView.JsonViewFactory.create().success(false).info(CreditorErrorCode.CREDITOR_RESIDUAL_BUY_MYSELF.getDesc()).toJson();
            }

            if (amount.remainder(new BigDecimal("100")).longValue() == 0) {
                if (new BigDecimal("100").compareTo(crta.getApplyPrice().subtract(totalBuy).subtract(amount)) > 0
                        && BigDecimal.ZERO.compareTo(crta.getApplyPrice().subtract(totalBuy).subtract(amount)) < 0)
                    throw new SystemException(CreditorErrorCode.CREDITOR_RESIDUAL_COMPARE_100);
            } else {
                if (amount.compareTo(crta.getApplyPrice().subtract(totalBuy)) != 0) {
                    throw new SystemException(CreditorErrorCode.CREDITOR_RESIDUAL_ALL);
                }
            }

            if (amount.compareTo(crta.getApplyPrice().subtract(totalBuy)) > 0) {
                //购买金额变为剩余可投金额
                if (crta.getApplyPrice().compareTo(totalBuy) == 0) {
                    request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_AMOUNT_ZONE.getDesc());
                    return JsonView.JsonViewFactory.create().success(false).info(CreditorErrorCode.CREDITOR_AMOUNT_ZONE.getDesc()).toJson();
                }
                request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_TOO_MUCH.getDesc());
                return JsonView.JsonViewFactory.create().success(false).info(CreditorErrorCode.CREDITOR_RESIDUAL_BUY_TOO_MUCH.getDesc()).toJson();
            }
        }

        PayResult payResult = null;
        LendOrder lendOrder = null;
        if (lendOrderId == null) {
            //可用余额支付
            ClientEnum clientEnum = ClientEnum.WAP_CLIENT;//默认微信来源，如果是APP接口来源赋相应的值。
            if (UserSource.ISO.getValue().equals(currentUser.getAppSource())) {
                clientEnum = ClientEnum.IOS_CLIENT;
            } else if (UserSource.ANDROID.getValue().equals(currentUser.getAppSource())) {
                clientEnum = ClientEnum.ANDROID_CLIENT;
            }
            payResult = lendProductService.creditorrightsByAccountBalance(crta, loanApplicationId, currentUser.getUserId(), lendProductPublish.getLendProductPublishId(), amount, clientEnum.getResource());
            lendOrder = lendOrderService.getLendOrderByPayId(payResult.getPayId(), true);
        } else {
            //可用余额、财富券支付
            lendOrder = lendOrderService.findById(lendOrderId);

            //todo 财富券校验
            if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)) {
                voucher_Ids = voucherIds.split(",");
                //先校验财富券
                JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
                if (!jsonView.isSuccess()) {
                    request.setAttribute("errorMsg", jsonView.getInfo());
                    return JsonView.JsonViewFactory.create().success(false).info(jsonView.getInfo()).toJson();
                }
            }

            //校验-购买金额加和是否等于订单金额
            if (lendOrder.getBuyBalance().compareTo(accountPayValue.add(voucherPayValue)) != 0) {
                request.setAttribute("errorMsg", BidErrorCode.AMOUNT_ILLEGAL.getDesc());
                return JsonView.JsonViewFactory.create().success(false).info(BidErrorCode.AMOUNT_ILLEGAL.getDesc()).toJson();
            }

            payResult = lendProductService.payFinanceByAccountBalanceWeb(currentUser.getUserId(), lendOrderId, null, null, voucher_Ids);
        }

        request.setAttribute("payResult", payResult);
        request.setAttribute("lendOrder", lendOrder);
        //根据执行结果跳转不同页面

        if (payResult.isPayResult() && payResult.isProcessResult())
            return JsonView.JsonViewFactory.create().success(true).info(lendOrder.getLendOrderId().toString()).toJson();

        request.setAttribute("errorMsg", payResult.getFailDesc());
        return JsonView.JsonViewFactory.create().success(false).info(payResult.getFailDesc()).toJson();


    }

    /**
     * 银行卡 购买债券
     *
     * @param creditorRightsApplyId
     * @param amount
     * @param request
     * @return
     */
    @RequestMapping(value = "toBuyRightsByPayAmount")
    public String toBuyRightsByPayAmount(Long creditorRightsApplyId, BigDecimal amount, HttpServletRequest request) {
        if (creditorRightsApplyId == null) {
            return "/error";
        }

        CreditorRightsTransferApplication crta = creditorRightsTransferAppService.findById(creditorRightsApplyId);
        if (crta == null) {
            request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_NOT_EXIST.getDesc());
            request.setAttribute("errorCode", CreditorErrorCode.CREDITOR_NOT_EXIST.getCode());
            return "error";
        }
        Long creditorRightsId = crta.getApplyCrId();

        CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
        //TokenUtils.validateToken(request);
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);

        LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(creditorRights.getLoanApplicationId());
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(creditorRights.getLoanApplicationId());

        LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());

        //已经购买的债权金额
        BigDecimal totalBuy = creditorRightsTransferAppService.getRemainingRightsPrice(crta.getCreditorRightsApplyId());//lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());

        if (crta.getApplyUserId().equals(currentUser.getUserId())) {
            request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_MYSELF.getDesc());
            request.setAttribute("errorCode", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_MYSELF.getCode());
            return "error";
        }
        if (amount.compareTo(crta.getApplyPrice().subtract(totalBuy)) > 0) {
            //购买金额变为剩余可投金额
            if (crta.getApplyPrice().compareTo(totalBuy) == 0) {
                request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_AMOUNT_ZONE.getDesc());
                request.setAttribute("errorCode", CreditorErrorCode.CREDITOR_AMOUNT_ZONE.getCode());
                return "error";
            }
            request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_TOO_MUCH.getDesc());
            request.setAttribute("errorCode", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_TOO_MUCH.getCode());
            return "error";
//			amount = crta.getApplyPrice().subtract(totalBuy);
        }

        ClientEnum clientEnum = ClientEnum.WAP_CLIENT;//默认微信来源，如果是APP接口来源赋相应的值。
        if (UserSource.ISO.getValue().equals(currentUser.getAppSource())) {
            clientEnum = ClientEnum.IOS_CLIENT;
        } else if (UserSource.ANDROID.getValue().equals(currentUser.getAppSource())) {
            clientEnum = ClientEnum.ANDROID_CLIENT;
        }

        // 新建投标订单
        LendOrder lendOrder = lendOrderService.addCreditorRightsOrder(currentUser.getUserId(), creditorRights.getLoanApplicationId(),
                lendProductPublish.getLendProductPublishId(), amount, new Date(), lendProduct, PropertiesUtils.getInstance().get(clientEnum.getResource()), crta);
        // 购买理财
        return "redirect:/finance/toPayRightsOrder?productType=2&lendOrderId=" + lendOrder.getLendOrderId();
    }

    // admin 后台专用 by mainid
    @DoNotNeedLogin
    @RequestMapping(value = "/admin_bidding")
    public String admin_bidding(HttpServletRequest request, Long loanApplicationNo) {
        if (loanApplicationNo == null) {
            //非法请求
            return "error";
        }
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoByMainId(loanApplicationNo);//main
        if (loanApplicationListVO == null) {
            //非法请求
            return "error";
        }

        Date openTime = loanApplicationListVO.getPopenTime();
        try {
            int secondBetween = 0;
            if (null != openTime) {
                secondBetween = DateUtil.secondBetween(new Date(), openTime);
            }
            request.setAttribute("secondBetwween", secondBetween);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //token
        String token = TokenUtils.setNewToken(request);
        request.setAttribute("token", token);
        //组织页面返回值
        request.setAttribute("repayMentMethod", PaymentMethodEnum.values());
        request.setAttribute("guaranteeType", GuaranteeTypeEnum.values());
        //用户是否登陆
        loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());

        //发标信息
        MainLoanPublish mainLoanPublish = loanPublicService.findMainLoanPublishById(loanApplicationNo);//main

        request.setAttribute("loanApplicationListVO", loanApplicationListVO);
        //附件快照
        List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getCustomerUploadAttachmentByMainId(loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());//main
        request.setAttribute("customerUploadSnapshots", customerUploadSnapshots);
        if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
                || LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())) {
            //个人投标详情页
//				personLoanApplication(request, loanApplicationListVO, loanPublish);
            //借款人基本信息
            CustomerBasicSnapshot basicSnapshot = customerBasicSnapshotService.getBasicByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());//main
            if (basicSnapshot != null) {
                request.setAttribute("basicSnapshot", basicSnapshot);
                //居住地
                Address adress = addressService.getAddressVOById(basicSnapshot.getResidenceAddr());
                request.setAttribute("adress", adress);
            }
            //房产抵押
            CustomerHouseSnapshot house = customerHouseSnapshotService.getHouseByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());//main
            request.setAttribute("house", house);
            request.setAttribute("loanPublish", mainLoanPublish);
            if (null != mainLoanPublish) {
                Address houseAdress = addressService.getAddressVOById(mainLoanPublish.getHourseAddress());
                request.setAttribute("houseAdress", houseAdress);
                //认证报告
                String authInfos = mainLoanPublish.getAuthInfos();
                if (StringUtils.isNotEmpty(authInfos)) {
                    request.setAttribute("authInfo", authInfos.split(","));
                }
            }
            return "/finance/front_financeBidding";
        } else {
            //企业投标详情页
//				enterpriseLoanApplication(request, loanApplicationListVO, loanPublish);
            //借款企业
            EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());//main
            EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
            request.setAttribute("enterpriseInfo", enterpriseInfo);
            //企业图
            List<EnterpriseUploadSnapshotVO> enterpriseInfoSnapshots = enterpriseInfoService.getEnterpriseUploadAttachment(enterpriseInfo.getEnterpriseId(), AttachmentIsCode.IS_CODE.getValue());
            request.setAttribute("enterpriseInfoSnapshots", enterpriseInfoSnapshots);
            switch (loanApplicationListVO.getLoanType()) {
                case "2":
                    //企业车贷
                    EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot = enterpriseCarLoanSnapshotService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());//main
                    String address = getAddress(enterpriseCarLoanSnapshot.getProvince(), enterpriseCarLoanSnapshot.getCity());
                    BigDecimal totalPrice = mortgageCarSnapshotService.getTotalPriceByCardLoanId(enterpriseCarLoanSnapshot.getEnterpriseCarLoanId());
                    request.setAttribute("totalPrice", totalPrice);
                    request.setAttribute("address", address);
                    request.setAttribute("enterpriseCarLoanSnapshot", enterpriseCarLoanSnapshot);
                    break;
                case "3":
                    //企业信贷
                    EnterpriseCreditSnapshot enterpriseCreditSnapshot = enterpriseCreditSnapshotService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());
                    address = getAddress(enterpriseCreditSnapshot.getProvince(), enterpriseCreditSnapshot.getCity());
                    request.setAttribute("enterpriseCarLoanSnapshot", enterpriseCreditSnapshot);
                    request.setAttribute("address", address);
                    break;
                case "4":
                    //企业保理
                    EnterpriseFactoringSnapshot factoringSnapshot = enterpriseFactoringSnapshotService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());
                    request.setAttribute("factoringSnapshot", factoringSnapshot);
                    //融资方
                    CoLtd financeParty = coLtdService.getCoLtdById(factoringSnapshot.getFinancingParty());
                    request.setAttribute("financeParty", financeParty);
                    break;
                case "5":
                    //企业基金
                    EnterpriseFoundationSnapshot foundationSnapshot = enterpriseFoundationSnapshotService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());
                    request.setAttribute("foundationSnapshot", foundationSnapshot);
                    //托管机构
                    CoLtd coltd = coLtdService.getCoLtdById(foundationSnapshot.getCoId());
                    request.setAttribute("coltd", coltd);
                    //标的详情说明
                    Attachment attachment = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getAttachId());
                    request.setAttribute("attachment", attachment);
                    //交易说明书
                    Attachment tradeBook = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getTradeBookId());
                    request.setAttribute("tradeBook", tradeBook);
                    //风险提示函
                    Attachment riskTip = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getRiskTipId());
                    request.setAttribute("riskTip", riskTip);
                    break;
                case "6":
                    //企业标
                    EnterprisePledgeSnapshot enterprisePledgeSnapshot = enterprisePledgeSnapshotService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());
                    address = getAddress(enterprisePledgeSnapshot.getProvince(), enterprisePledgeSnapshot.getCity());
                    request.setAttribute("enterpriseCarLoanSnapshot", enterprisePledgeSnapshot);
                    request.setAttribute("address", address);
                    break;
            }
            return "/finance/front_financeEnterpriseBidding";
        }
    }

    /**
     * @param request
     * @param lendOrderId
     * @return wangyadong add
     */
    @RequestMapping(value = "toPayRightsOrder")
    public String toPayRightsOrder(HttpServletRequest request,
                                   String productType,
                                   @RequestParam(value = "lendOrderId") Long lendOrderId) {
        TokenUtils.setNewToken(request);
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        //身份验证
        if (!Objects.equals(UserIsVerifiedEnum.BIND.getValue(), userExt.getIsVerified())) {
            throw new SystemException(UserErrorCode.HAS_NOT_IDVERIFIED).set("userId", currentUser.getUserId());
        }
        //校验-订单是否存在
        if (lendOrder == null) {
            throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderId", lendOrderId);
        }

        //校验-订单用户和当前用户必须相同
        if (!lendOrder.getLendUserId().equals(currentUser.getUserId())) {
            throw new SystemException(PayErrorCode.NOT_BELONG_PAY_ORDER).set("lendOrderUserId", lendOrder.getLendUserId()).set("user", currentUser.getUserId());
        }

        if (!lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())) {
            throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
        }

        CreditorRightsTransferApplication crtf = creditorRightsTransferAppService.getTransferApplicationByLendOrderId(lendOrder.getLendOrderId());
        // 标的状态验证
        if (!CreditorRightsTransferAppStatus.TRANSFERRING.getValue().equals(crtf.getBusStatus())) {
            throw new SystemException(BidErrorCode.BID_STATIS_NOT_SELLING).set("status", crtf.getBusStatus());
        }

        List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
        LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
        LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(lendOrderBidDetail.getLoanApplicationId());
        LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());

        loanApplicationListVO.setAwardRate(loanApplicationListVO.getAwardRate() != null && !loanApplicationListVO.getAwardRate().equals("") && !loanApplicationListVO.getAwardRate().equals("0") ? loanApplicationListVO.getAwardRate() + "%" : "");

        LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());

        // 已经购买的债权金额
        BigDecimal totalBuy = creditorRightsTransferAppService.getRemainingRightsPrice(crtf.getCreditorRightsApplyId());
        CreditorRightsTransferApplication crta = creditorRightsTransferAppService.getEffectiveTransferApplyByCreditorRightsId(crtf.getApplyCrId());

        if (lendOrder.getBuyBalance().remainder(new BigDecimal("100")).longValue() == 0) {
            if (new BigDecimal("100").compareTo(crta.getApplyPrice().subtract(totalBuy).subtract(lendOrder.getBuyBalance())) > 0
                    && BigDecimal.ZERO.compareTo(crta.getApplyPrice().subtract(totalBuy).subtract(lendOrder.getBuyBalance())) < 0) {
                throw new SystemException(CreditorErrorCode.CREDITOR_RESIDUAL_COMPARE_100);
            }
        } else {
            if (lendOrder.getBuyBalance().compareTo(crta.getApplyPrice().subtract(totalBuy)) != 0) {
                throw new SystemException(CreditorErrorCode.CREDITOR_RESIDUAL_ALL);
            }
        }
        if (lendOrder.getBuyBalance().compareTo(crta.getApplyPrice().subtract(totalBuy)) > 0) {
            if (crta.getApplyPrice().compareTo(totalBuy) == 0) {
                throw new SystemException(CreditorErrorCode.CREDITOR_AMOUNT_ZONE).set("lendOrderId", lendOrderId);
            }
            // 购买金额变为剩余可投金额
            lendOrder.setBuyBalance(crta.getApplyPrice().subtract(totalBuy));
        }
//        CustomerCard hisCustomerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);
//        if (hisCustomerCard != null) {
//            boolean isSupport = true;
//            ConstantDefine bankInfo = constantDefineService.findById(hisCustomerCard.getBankCode());
//            if (bankInfo.getConstantStatus().equals("1")) {
//                isSupport = false;
//            }
//            request.setAttribute("isSupport", isSupport);
//        }

        //查询用户卡信息
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);
        customerCard.setMobile(customerCard.getMobile().substring(0, 3) + "*****" + customerCard.getMobile().substring(8));

        //财富券
        List<VoucherVO> vouchers = voucherService.getAllVoucherList(currentUser.getUserId(), lendOrder.getBuyBalance(), VoucherConstants.UsageScenario.LOAN, VoucherConstants.UsageScenario.WHOLE);

        request.setAttribute("productType", productType);
        request.setAttribute("isRights", true);//是否债权转让
        request.setAttribute("order", lendOrder);
        request.setAttribute("loanApplication", loanApplicationListVO);
        request.setAttribute("userExtInfo", userExt);
        request.setAttribute("lendOrder", lendOrder);
        request.setAttribute("amount", lendOrder.getBuyBalance());
        request.setAttribute("truePayAmount", lendOrder.getBuyBalance());
        request.setAttribute("customerCard", customerCard);
        request.setAttribute("vouchers", vouchers);
        request.setAttribute("expectedInteresting", getExpectRightProfit(crtf.getCreditorRightsApplyId(), lendOrder.getBuyBalance()));
        request.setAttribute("lendProduct", lendProduct);
        request.setAttribute("lendProductPublish", lendProductPublish);
        request.setAttribute("creditorRightsApplyId", crtf.getCreditorRightsApplyId());
        request.setAttribute("userCashBalance", userAccountService.getCashAccount(currentUser.getUserId()).getAvailValue2());
        boolean isBidEqualLoginPass = userInfoService.isBidPassEqualLoginPass(currentUser.getUserId());
        request.setAttribute("isBidEqualLoginPass", isBidEqualLoginPass);
        return "order/payForOrder";
    }

    @RequestMapping(value = "paySucess")
    public String paySucess(HttpServletRequest request) {
        return "/order/paySuccess";
    }

    /**
     * 【微信-新】
     * 已投债权，分页查询列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCreditRightListByZhuangRan", method = RequestMethod.POST)
    @ResponseBody
    public Object getCreditRightListByZhuangRan(HttpServletRequest request,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                String flag,
                                                @RequestParam(value = "creditorRightsStatus", required = false) String creditorRightsStatus,
                                                @RequestParam(value = "backDate", defaultValue = "", required = false) String backDate,
                                                @RequestParam(value = "buyDate", defaultValue = "", required = false) String buyDate) {

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        CreditorRightsExtVo vo = new CreditorRightsExtVo();
        vo.setLendUserId(currentUser.getUserId());

        vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上

        // 封装参数
        Map<String, Object> customParams = new HashMap<String, Object>();

        if (com.xt.cfp.core.util.StringUtils.isNull(creditorRightsStatus)) {
            String[] queryStatus = {CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.getValue(),
                    CreditorRightsConstants.CreditorRightsStateEnum.TURNOUT.getValue(),
                    CreditorRightsConstants.CreditorRightsStateEnum.COMPLETE.getValue(),
                    CreditorRightsConstants.CreditorRightsStateEnum.APPLYTURNOUT.getValue(),
                    CreditorRightsConstants.CreditorRightsStateEnum.TRANSFERING.getValue()};
            customParams.put("queryStatus", queryStatus);
        }
        if (creditorRightsStatus.equals("6")) {
            String[] queryStatus = {CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.getValue()};
            customParams.put("rightsflag", "0");// 转让的标什
            customParams.put("queryStatus", queryStatus);
//			customParams.put("canTurn", true);
        } else if (creditorRightsStatus.equals("8")) {
            customParams.put("fromWhere", CreditorRightsFromWhereEnum.TURN.getValue());
        } else {
            String[] queryStatus = getQueryStatus(creditorRightsStatus);
            if (queryStatus != null)
                customParams.put("queryStatus", queryStatus);
        }

        customParams.put("productTypeEnum", "productTypeEnum");
        Date newDate = DateUtil.getShortDateWithZeroTime(new Date());
        customParams.put("newDate", newDate);
        customParams.put("orderBy", getOrderBy(backDate, buyDate));
        Pagination<CreditorRightsExtVo> pagination = creditorRightsService.getCreditorRightsPaging(pageNo, pageSize, vo, customParams);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<CreditorRightsExt> creVOs = new ArrayList<>();
        CreditorRightsExt creVO = null;
        DecimalFormat df = new DecimalFormat("#,##0.00");

        List<CreditorRightsExtVo> crevList = pagination.getRows();
        if (null != crevList && crevList.size() > 0) {
            for (CreditorRightsExtVo v : crevList) {
                creVO = new CreditorRightsExt();
                int agreementDate = DateUtil.daysBetween(v.getCreateTime(), new Date());
                creVO.setAgreementDays(agreementDate);
                creVO.setCreditorRightsName(v.getCreditorRightsName());//债权名称(借款标题)
                creVO.setLoanLoginName(v.getLoanLoginName());//借款人
                creVO.setBuyPrice(df.format(v.getBuyPrice()));//出借金额（元）
                creVO.setWaitTotalpayMent(df.format(v.getWaitTotalpayMent()));//待收回款（元）
                creVO.setFactBalance(df.format(v.getFactBalance()));//已收回款（元）
                creVO.setCurrentPayDate(DateUtil.getDateLong(v.getCurrentPayDate()));//最近回款日
                if ("0".equals(String.valueOf(v.getRightsState())) || "1".equals(String.valueOf(v.getRightsState()))) {
                    creVO.setRightsState("回款中");//债权状态:如果值为 0已生效 或 1还款中，则统一显示 回款中
                } else {
                    creVO.setRightsState(CreditorRightsStateEnum.getCreditorRightsStateEnumByValue(String.valueOf(v.getRightsState())).getDesc());//债权状态
                }
                if ("7".equals(String.valueOf(v.getRightsState()))) {// 提前结清
                    // 查询提前结清时间
                    List<RepaymentPlan> repaymentPlans = repaymentPlanService
                            .getRepaymentPlansByloanApplicationId(
                                    v.getLoanApplicationId(),
                                    ChannelTypeEnum.ONLINE);
                    for (int k = 0; k < repaymentPlans.size(); k++) {
                        if (RepaymentPlanStateEnum.BEFORE_COMPLETE.value2Char() ==
                                repaymentPlans.get(k).getPlanState()) {
                            creVO.setRepaymentRecord(repaymentRecordService
                                    .getRecentRepaymentRecordByRepaymentId(repaymentPlans
                                            .get(k).getRepaymentPlanId()));
                            break;
                        }
                    }
                    // 获取 repaymentPlanId 提前还款时间
                }
                creVO.setDetails(v.getRightsRepaymentDetailList());
                creVO.setBuyDate(DateUtil.getDateLong(v.getBuyDate()));//投标日期
                creVO.setExpectProfit(df.format(v.getExpectProfit()));//预期收益
                creVO.setCreditorRightsId(String.valueOf(v.getCreditorRightsId()));//债权ID
                creVO.setCycleCount(v.getLoanApplicationListVO().getCycleCount() + "个月");//出借时长
                creVO.setAnnualRate(v.getLoanApplicationListVO().getAnnualRate() + "%");//年化利率
                creVO.setRepayMentMethod(v.getLoanApplicationListVO().getRepayMentMethod());//还款方式
                creVO.setFromWhere(v.getFromWhere());
                creVO.setCreditorRightsApplyId(v.getCreditorRightsApplyId());
                creVO.setTurn(v.getZr());
                creVO.setLoanType(v.getLoanType());
                // 距离到期多少天
//        		List<RightsRepaymentDetail> details = v.getRightsRepaymentDetailList();
//        		if(null != details && details.size() > 0){
//        			int daysBetween = DateUtil.daysBetween(new Date(), details.get((details.size()-1)).getRepaymentDayPlanned());
//        			creVO.setExpireDays(String.valueOf(daysBetween));
//        		}
                creVOs.add(creVO);
            }
        }
        if ("6".equals(creditorRightsStatus)) {
            resultMap.put("index", "1");
        } else if ("7".equals(creditorRightsStatus)) {
            resultMap.put("index", "2");
        } else if ("8".equals(creditorRightsStatus)) {
            resultMap.put("index", "0");
        } else {
            resultMap.put("index", "");
        }
        resultMap.put("pageSize", pagination.getPageSize());
        resultMap.put("pageNo", pagination.getCurrentPage());
        resultMap.put("total", pagination.getTotal());
        resultMap.put("totalPage", pagination.getTotalPage());
        resultMap.put("rows", creVOs);
        resultMap.put("newDate", new Date());
        return returnResultMap(true, resultMap, null, null);


    }

    /**
     * 新手标方法
     *
     * @param loanApplicationListVO 预期收益
     * @param pageSize              借款期限
     * @param pageNo                付息类型
     * @return
     * @author wangyadong
     * @date 2016年10月9日
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/loanSpecialList")
    @ResponseBody
    public Object loanSpecialList(HttpServletRequest request,
                                  LoanApplicationListVO loanApplicationListVO,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                  @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {

        Pagination<LoanApplicationListVO> loanApplicationListPaging = loanApplicationService.getLoanSpecialApplicationPaging(pageNo, pageSize, loanApplicationListVO, null);
        return loanApplicationListPaging;
    }

    /**
     * 新手标方法
     *
     * @return
     * @author wangyadong
     * @date 2016年10月9日
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/to_loanSpecialList")
    public String to_loanSpecialList(HttpServletRequest request, HttpServletResponse response) {
        return "/finance/loanSpecialList";
    }

    /**
     * 用余额购买省心计划
     *
     * @param password
     * @param lendProductPublishId
     * @param amount
     * @param profitReturnConfig   省心计划收益分配方式
     * @return
     */
    @RequestMapping(value = "buySxByAccountAmount")
    public String buySxByAccountAmount(@RequestParam(value = "psw") String password,
                                       @RequestParam(value = "lendProductPublishId") Long lendProductPublishId,
                                       @RequestParam(value = "amount") BigDecimal amount,
                                       @RequestParam(value = "lendOrderId", required = false) Long lendOrderId,
                                       @RequestParam(value = "accountPayValue", defaultValue = "0") BigDecimal accountPayValue,
                                       @RequestParam(value = "isUseVoucher", required = false) String isUseVoucher,
                                       @RequestParam(value = "profitReturnConfig", required = false) String profitReturnConfig,
                                       HttpServletRequest request) {

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        TokenUtils.validateToken(request);
        logger.info(LogUtils.createLogWithParams("购买省心计划", DescTemplate.Log.BidLogTemplate.BUY_FINANCE_BY_ACCOUNTVALUE,
                currentUser.getUserId(), lendProductPublishId, amount));

        LendProductPublish lendProductPublish = lendProductService.getLendProductPublishByPublishId(lendProductPublishId);
        //校验业务数据
        checkForBuyFinanaceByAccountAmount(password, lendProductPublish, amount, currentUser);
        //执行购买
        LendOrder lendOrder = null;
        PayResult payResult = null;
        //检验剩余金额是否小于购买金额
        if (lendProductPublish.getPublishBalanceType() == LendProductPublish.PUBLISHBALANCETYPE_SPEC) {
            BigDecimal leftBalancce = lendProductPublish.getPublishBalance().subtract(lendProductPublish.getSoldBalance());
            if (amount.compareTo(leftBalancce) > 0) {
                request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_TOO_MUCH.getDesc());
                return "/order/payError";
            }
        }

        if (lendOrderId == null) {
            ClientEnum clientEnum = ClientEnum.WAP_CLIENT;//默认微信来源，如果是APP接口来源赋相应的值。
            if (UserSource.ISO.getValue().equals(currentUser.getAppSource())) {
                clientEnum = ClientEnum.IOS_CLIENT;
            } else if (UserSource.ANDROID.getValue().equals(currentUser.getAppSource())) {
                clientEnum = ClientEnum.ANDROID_CLIENT;
            }
            payResult = lendProductService.buyFinanceByAccountBalance(currentUser.getUserId(), lendProductPublishId, amount, clientEnum.getResource(), isUseVoucher, profitReturnConfig);
            lendOrder = lendOrderService.getLendOrderByPayId(payResult.getPayId(), true);
        } else {
            payResult = lendProductService.payFinanceByAccountBalanceWeb(currentUser.getUserId(), lendOrderId, null, null, null);
            lendOrder = lendOrderService.findById(lendOrderId);
        }

        request.setAttribute("payResult", payResult);
        request.setAttribute("lendOrder", lendOrder);
        //根据执行结果跳转不同页面
        if (payResult.isPayResult() && payResult.isProcessResult()) {
            //生成省息计划回款列表和合同
            financePlanProcessModule.beginCalcInterest(lendOrder, new Date());
            //匹配
            try {
                financePlanService.match(lendOrder);
            } catch (Exception e) {
                logger.info("匹配省心计划失败，失败原因：", e);
            }
            return "redirect:/lendOrder/paySuccess?orderId=" + lendOrder.getLendOrderId();
        }

        return "/order/payError";
    }


    /**
     * 省心计划订单，修改收益分配方式
     *
     * @param request
     * @param lendOrderId        省心订单
     * @param profitReturnConfig 收益分配方式
     * @return
     */
    @RequestMapping(value = "/profitReturnConfig")
    @ResponseBody
    public String profitReturnConfig(HttpServletRequest request,
                                     @RequestParam(value = "lendOrderId") Long lendOrderId,
                                     @RequestParam(value = "profitReturnConfig") String profitReturnConfig) {
        try {
            //校验-参数必填
            if (null == lendOrderId) {
                return JsonView.JsonViewFactory.create().success(false).info("缺少必要的参数").toJson();
            }
            if (null == profitReturnConfig || "".equals(profitReturnConfig)) {
                return JsonView.JsonViewFactory.create().success(false).info("缺少必要的参数").toJson();
            } else if (!FinanceProfitReturnEnum.TO_CASH_ACCOUNT.getValue().equals(profitReturnConfig)
                    && !FinanceProfitReturnEnum.TO_FINANCE_ACCOUNT.getValue().equals(profitReturnConfig)) {
                return JsonView.JsonViewFactory.create().success(false).info("必要的参数不合法").toJson();
            }

            //加载此订单
            LendOrder lendOrder = lendOrderService.findById(lendOrderId);
            if (null == lendOrder) {
                return JsonView.JsonViewFactory.create().success(false).info("必要的参数不合法").toJson();
            }

            //校验-如果订单已经被处理过
            if (!lendOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.UNPAY.getValue())) {
                return JsonView.JsonViewFactory.create().success(false).info("订单已处理过").toJson();
            }

            //修改收益分配方式
            lendOrder.setProfitReturnConfig(profitReturnConfig);
            Map<String, Object> lendOrderMap = new HashMap<String, Object>();
            lendOrderMap.put("lendOrderId", lendOrder.getLendOrderId());
            lendOrderMap.put("profitReturnConfig", lendOrder.getProfitReturnConfig());
            lendOrderService.update(lendOrderMap);

            return JsonView.JsonViewFactory.create().success(true).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonView.JsonViewFactory.create().success(false).info(e.getMessage()).toJson();
        }
    }

    /**
     * 省心计划列表
     *
     * @param financeGuessList  预期收益
     * @param financeduringList 借款期限
     * @param financeLendList   付息类型
     * @return
     * @author wangyadong
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/lendList")
    @ResponseBody
    public Object lendList(HttpServletRequest request,
                           LendProductPublishVO lendProductPublish,
                           @RequestParam(value = "pageSize", defaultValue = "6") int pageSize,
                           @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                           @RequestParam(value = "financeGuessList", defaultValue = "") String financeGuessList,
                           @RequestParam(value = "financeduringList", defaultValue = "") String financeduringList,
                           @RequestParam(value = "financeLendList", defaultValue = "") String financeLendList
    ) {

        lendProductPublish.setProfitRate(financeLendList);
        lendProductPublish.setPublishCode(financeGuessList);
        lendProductPublish.setPublishName(financeduringList);
        lendProductPublish.setClosingDate(lendProductPublish.getOrderBy());
        Pagination<LendProductPublishVO> lproductWithBalanceStatus = this.lendProductService.findFinanceProductListForWebCondition(pageSize, pageNo, lendProductPublish);

        return lproductWithBalanceStatus;
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/toSxDetail")
    public String toSxDetail(HttpServletRequest req, LendProductPublish lpp) {
        if (lpp.getLendProductPublishId() == null) {
            return "error";
        }
        LproductWithBalanceStatus financeDetail = this.lendProductService.findFinanceProductDetailForWeb(lpp.getLendProductPublishId());
        if (financeDetail == null) {
            return "error";
        }
        UserInfo user = (UserInfo) req.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
        if (null != user) {
            UserAccount userAccount = userAccountService.getCashAccount(user.getUserId());
            req.setAttribute("userAccount", userAccount);
            UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
            req.setAttribute("userExt", userExt);
        }
        // 本期申购情况
        List<LendOrderExtProduct> buySituation = lendOrderService.getCycleBuySituation(lpp.getLendProductPublishId());
        // 往期申购情况
        List<LendProductPublish> historyBuySituation = lendOrderService.getCycleBuySituationHistory(lpp);
        //token
        String token = TokenUtils.setNewToken(req);
        req.setAttribute("token", token);

        req.setAttribute("limit", financeDetail.getTimeLimit());
        req.setAttribute("financeDetail", financeDetail);
        req.setAttribute("buySituation", buySituation);
        req.setAttribute("historyBuySituation", historyBuySituation);

        return "/finance/sxDetail";
    }

    @DoNotNeedLogin
    @RequestMapping(value = "/sxDesc")
    public String sxDesc(HttpServletRequest request, String lendProductPublishId) {
        request.setAttribute("lendProductPublishId", lendProductPublishId);
        return "/finance/sxDesc";
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

    @DoNotNeedLogin
    @RequestMapping(value = "/sxLawDesc")
    public String sxDesc(HttpServletRequest request, Long id) {
        request.setAttribute("id", id);
        return "/finance/sxLawDesc";
    }

    /**
     * 跳转到：我的省心计划的详情
     *
     * @param request
     * @param lendOrderId 出借订单ID
     * @return wangyadong by web
     */
    @RequestMapping(value = "/getAllMyFinanceListDetailBy", method = RequestMethod.POST)
    public ModelAndView getAllMyFinanceListDetailBy(HttpServletRequest request, Long lendOrderId) {

        ModelAndView mv = new ModelAndView();
        LendOrderExtProduct loep = lendOrderService.findFinancialPlanById(lendOrderId);
        UserAccount financeAccount = userAccountService.getUserAccountByAccId(loep.getCustomerAccountId());

        // 查询某条省心计划，在投资金预期收益
        BigDecimal expectProfit = lendOrderService.getFinancialWaitInterestByLendOrderId(lendOrderId);

        // 查询某条省心计划，已经匹配到子标的已获奖励
        BigDecimal awardBalance = userAccountService.getUserTotalFinanceAwardByLendOrderId(lendOrderId);

        // 查询某条省心计划，总的投标奖励
        BigDecimal totalExpectAward = this.getTotalExpectAward(lendOrderId);

        // 查询某条省心计划，待回奖励 = 总的奖励 - 已获奖励
        BigDecimal waitBackAward = BigDecimalUtil.down(totalExpectAward.subtract(awardBalance), 2);

        // 查询某条省心计划，已经匹配到子标的出借总金额
        BigDecimal allBuyBalance = userAccountService.getUserFinancingAccountValueByAccId(financeAccount.getAccId());
        allBuyBalance = allBuyBalance.add(waitBackAward);

        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
        if (userInfo.getUserId().compareTo(loep.getLendUserId()) != 0) {
            request.setAttribute("errorMsg", "非法参数");
            return new ModelAndView("/error");
        }

        //【统计值】理财中金额（元）（在投资金+在投预期收益）
        mv.addObject("financeAccountValue", BigDecimalUtil.down(allBuyBalance, 2));
        //【统计值】预期年化收益率范围
        mv.addObject("profitRate", loep.getProfitRate());
        mv.addObject("profitRateMax", loep.getProfitRateMax());
        //【统计值】省心期（月）
        mv.addObject("timeLimit", loep.getTimeLimit());
        //【统计值】在投资金预期收益（元）+ 待回奖励
        mv.addObject("expectProfit", BigDecimalUtil.down(expectProfit.add(waitBackAward), 2));
        //【统计值】已获收益（元）+ 已获奖励
        mv.addObject("currentProfit", BigDecimalUtil.down(loep.getCurrentProfit2().add(awardBalance), 2));
        //【统计值】购买金额（元）
        mv.addObject("buyBalance", BigDecimalUtil.down(loep.getBuyBalance(), 2));
        //【统计值】待出借金额（元）
        mv.addObject("availValue", BigDecimalUtil.down(financeAccount.getAvailValue2().add(financeAccount.getFrozeValue2()), 2));
        //【统计值】购买日期
        mv.addObject("buyTime", loep.getBuyTime());
        //【统计值】省心计划到期日期
        mv.addObject("agreementEndDate", loep.getAgreementEndDate());
        //账户ID
        mv.addObject("accId", financeAccount.getAccId());
        //省心计划的出借订单ID
        mv.addObject("lendOrderId", loep.getLendOrderId());
        mv.addObject("loep", loep);

        mv.setViewName("/finance/myFinanceDetail");
        return mv;
    }

    /**
     * [省心计划工具]查询某条省心计划，获取总的预期投标奖励（省心订单ID --> 子订单List --> 债权List）
     *
     * @param lendOrderId 省心订单ID
     * @return wangyadong by web
     */
    private BigDecimal getTotalExpectAward(Long lendOrderId) {
        BigDecimal totalAward = BigDecimal.ZERO;
        try {
            List<CreditorRights> creditorRightList = creditorRightsService.getCreditorRightsByLendOrderPid(lendOrderId);
            if (creditorRightList != null) {
                for (CreditorRights cr : creditorRightList) {
                    if (CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char() == cr.getRightsState()// 已生效
                            || CreditorRightsConstants.CreditorRightsStateEnum.APPLYTURNOUT.value2Char() == cr.getRightsState()//申请转出
                            || CreditorRightsConstants.CreditorRightsStateEnum.TRANSFERING.value2Char() == cr.getRightsState()) {//转让中
                        totalAward = totalAward.add(creditorRightsService.getExpectAward(cr.getLoanApplicationId(), cr.getBuyPrice()));//算预期的奖励
                    }
                }
            }
            totalAward = totalAward.add(userAccountService.getUserTotalFinanceAwardByLendOrderId(lendOrderId));
        } catch (Exception e) {
            logger.error("【错误】获取总的预期投标奖励,lendOrderId=" + lendOrderId + ",message=" + e.getMessage(), e);
        }
        return totalAward;
    }


    /**
     * 我的理财-省心计划-出借明细 列表查询
     *
     * @param request
     * @param pageSize
     * @param pageNo
     * @param creditorRightsStatus
     * @param backDate
     * @param buyDate
     * @return
     */
    @RequestMapping(value = "/getSXJHCreditorRightsDetailList")
    @ResponseBody
    public Object getSXJHCreditorRightsDetailList(HttpServletRequest request, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                  @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                  String creditorRightsStatus, String backDate, String buyDate, Long lendOrderId) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);

        CreditorRightsExtVo vo = new CreditorRightsExtVo();
        vo.setLendUserId(currentUser.getUserId());
        //vo.setFromWhere(CreditorRightsFromWhereEnum.BUY.value2Char());//购买来源
        vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上

        //封装参数
        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("lendOrderPid", lendOrderId);//省心计划出借订单id=债权明细父出借订单父id
        String[] queryStatus = getQueryStatus(creditorRightsStatus);
        if (queryStatus != null) {
            customParams.put("queryStatus", queryStatus);
        }
        customParams.put("orderBy", getOrderBy(backDate, buyDate));

        Pagination<CreditorRightsExtVo> creditorRightsPaging = creditorRightsService.getSXJHCreditorRightsDetailPaging(pageNo, pageSize, vo, customParams);
        List<CreditorRightsExtVo> rows = creditorRightsPaging.getRows();
        if (null != rows && !rows.isEmpty()) {
            for (CreditorRightsExtVo v : rows) {
                List<RightsRepaymentDetail> details = v.getRightsRepaymentDetailList();
                if (null != details && details.size() > 0) {//距到期天数
                    v.setLendCustomerName(String.valueOf(DateUtil.daysBetween(new Date(), details.get((details.size() - 1)).getRepaymentDayPlanned())));
                }
            }
        }
        creditorRightsPaging.setUrl(creditorRightsStatus);

        return creditorRightsPaging;
    }


    /**
     * 我的理财-省心计划-资金流水 列表查询
     *
     * @param request
     * @param session
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/getSXJHFundDetailList")
    @ResponseBody
    public Object fundManageList(HttpServletRequest request, HttpSession session,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                 @RequestParam(value = "accId", required = false) Long accId,
                                 @RequestParam(value = "flowType[]", required = false) String[] flowType,
                                 @RequestParam(value = "searchDate[]", required = false) String[] searchDate) {
        return userAccountService.getUserAccountHisByAccId(pageNo, pageSize, accId, flowType, searchDate, AccountConstants.VisiableEnum.DISPLAY);
    }


    /**
     * 省心计划投标列表(前台展示)
     *
     * @param request
     * @param lendProductPublishId 省心产品发布ID
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/getSXJHLender")
    @ResponseBody
    @DoNotNeedLogin
    public Object getSXJHLender(HttpServletRequest request, Long lendProductPublishId,
                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        if (null == lendProductPublishId && !"".equals(lendProductPublishId)) {
            return null;
        }
        Pagination<LenderRecordVO> result = lendOrderBidDetailService.findSXJHLendOrderDetailPaging(pageNo, pageSize, lendProductPublishId, LendOrderBidStatusEnum.BIDSUCCESS);
        return result;
    }

    @RequestMapping(value = "/creditorProtocol")
    @DoNotNeedLogin
    public ModelAndView creditorProtocol(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/agreement/biao");
        return mv;
    }

    @RequestMapping(value = "/carPersonProtocol")
    @DoNotNeedLogin
    public ModelAndView carPersonProtocol(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/agreement/carPerson");
        return mv;
    }

    @RequestMapping(value = "/cashLoanProtocol")
    @DoNotNeedLogin
    public ModelAndView cashLoanProtocol(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/agreement/cashLoan");
        return mv;
    }

    @RequestMapping(value = "/accountdetailview")
    public String accountdetailview(HttpServletRequest request) {

        //---组织页面参数
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
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
        //累计出借
        BigDecimal totalLendAmount = lendOrderService.getTotalLendAmount(currentUser.getUserId());
        request.setAttribute("totalLendAmount", totalLendAmount);
        //累计借款
        BigDecimal totalLoanAmount = loanApplicationService.getTotalLoanAmount(currentUser.getUserId());
        request.setAttribute("totalLoanAmount", totalLoanAmount);
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
        return "/finance/accountdetailview";
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
     * 省心计划投标列表(前台展示)
     *
     * @param request
     * @param lendProductPublishId 省心产品发布ID
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/getIndexLoanApplication")
    @ResponseBody
    @DoNotNeedLogin
    public Object getIndexLoanApplication() {

        return loanApplicationService.getLoanApplicationPaging(1, 4, null, null);
    }

    /**
     * 跳转到：回款日历
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toPaymentCalendar")
    public ModelAndView toPaymentCalendar(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/finance/paymentCalendar");
        return mv;
    }

    /**
     * 执行：获取回款日历月数据
     *
     * @param date
     * @return
     */
    @RequestMapping(value = "/getPaymentCalendarMonthData")
    @ResponseBody
    public Object getPaymentCalendarMonthData(String date) {
        Map<String, Object> monthMap = new HashMap<String, Object>();
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", currentUser.getUserId());
        param.put("repaymentDate", date);
        BigDecimal monthMoney = rightsRepaymentDetailService.getSumShouldBalanceByMonth(param);
        List<PaymentMonthListDataVO> monthLists = rightsRepaymentDetailService.getMonthListDataByMonth(param);
        monthMap.put("monthMoney", monthMoney);
        monthMap.put("dayList", monthLists);
        return returnResultMap(true, monthMap, null, null);
    }

    /**
     * 执行：获取回款日历日数据
     *
     * @param date
     * @return
     */
    @RequestMapping(value = "/getPaymentCalendarDayData")
    @ResponseBody
    public Object getPaymentCalendarDayData(String date) {
        Map<String, Object> dayMap = new HashMap<String, Object>();
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", currentUser.getUserId());
        param.put("repaymentDate", date);
        PaymentDaySummaryDataVO daySummaryData = rightsRepaymentDetailService.getDaySummaryDataByDay(param);
        List<PaymentDayListDataVO> dayLists = rightsRepaymentDetailService.getDayListDataByDay(param);
        dayMap.put("moneyTime", daySummaryData.getMoneyTime());
        dayMap.put("moneyDay", daySummaryData.getMoneyDay());
        dayMap.put("Daylist", dayLists);
        return returnResultMap(true, dayMap, null, null);
    }

}
