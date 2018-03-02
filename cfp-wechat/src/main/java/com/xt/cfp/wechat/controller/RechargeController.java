package com.xt.cfp.wechat.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.external.deposites.api.IhfApi;
import com.external.deposites.exception.HfApiException;
import com.external.deposites.model.datasource.RechargeDataSource;
import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.service.impl.CgBizService;
import com.xt.cfp.core.util.*;
import jodd.util.NameValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.external.llpay.LLPayRequest;
import com.external.llpay.LLPayUtil;
import com.external.yeepay.TZTService;
import com.google.gson.Gson;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.CreditorErrorCode;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.constants.RateEnum.RateProductIsOverlayEnum;
import com.xt.cfp.core.constants.RateEnum.RateProductScenarioEnum;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.pojo.CreditorRightsTransferApplication;
import com.xt.cfp.core.pojo.CustomerCard;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendOrderBidDetail;
import com.xt.cfp.core.pojo.LendProduct;
import com.xt.cfp.core.pojo.LendProductPublish;
import com.xt.cfp.core.pojo.PayOrder;
import com.xt.cfp.core.pojo.RateProduct;
import com.xt.cfp.core.pojo.RateUser;
import com.xt.cfp.core.pojo.RechargeOrder;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.pojo.ext.VoucherVO;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.service.CreditorRightsTransferAppService;
import com.xt.cfp.core.service.CustomerCardService;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanPublishService;
import com.xt.cfp.core.service.PayService;
import com.xt.cfp.core.service.RateProductService;
import com.xt.cfp.core.service.RateUserService;
import com.xt.cfp.core.service.RechargeOrderService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.VoucherService;
import com.xt.cfp.wechat.annotation.DoNotNeedLogin;

/**
 * Created by yulei on 2015/7/11.
 */
@Controller
@RequestMapping(value = "/recharge")
public class RechargeController extends BaseController {
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private PayService payService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private LendOrderService lendOrderService;
    @Autowired
    private LendProductService lendProductService;
    @Autowired
    private ConstantDefineService constantDefineService;

    @Autowired
    private LoanPublishService loanPublicService;
    @Autowired
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private CreditorRightsTransferAppService creditorRightsTransferAppService;
    @Autowired
    private RateProductService rateProductService;
    @Autowired
    private RateUserService rateUserService;
    @Autowired
    private CgBizService cgBizService;
    @Autowired
    private IhfApi ihfApi;

    /**
     * 未绑定银行卡时的确认支付操作
     *
     * @param request
     * @param session
     * @param validCode
     * @param lendOrderId
     * @param jypassword
     * @param cardNo
     * @param phone
     * @param accountPayValue
     * @param rechargePayValue
     * @param voucherIds
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/confirmPayValidateNoCard")
    @ResponseBody
    public String confirmPayValidateNoCard(HttpServletRequest request, HttpSession session,
                                           @RequestParam(value = "valid") String validCode,
                                           @RequestParam(value = "lendOrderId") Long lendOrderId,
                                           @RequestParam(value = "password") String jypassword,
                                           @RequestParam(value = "cardNo") String cardNo,
                                           @RequestParam(value = "phone") String phone,
                                           @RequestParam(value = "accountPayValue", defaultValue = "0") BigDecimal accountPayValue,
                                           @RequestParam(value = "rechargePayValue", defaultValue = "0") BigDecimal rechargePayValue,
                                           @RequestParam(value = "userVoucherId", required = false) String voucherIds) {
        //校验-是否点击验证码
        String rechargeOrderCode = (String) session.getAttribute("smsCode");
        if (org.apache.commons.lang.StringUtils.isEmpty(rechargeOrderCode)) {
            return JsonView.JsonViewFactory.create().success(false).info("您未获取短信验证码！").put("id", "valid")
                    .toJson();
        }

        //校验-参数必填
        ValidationUtil.checkRequiredPara(new NameValue<String, Object>("valid", validCode),
                new NameValue<String, Object>("lendOrderId", lendOrderId));
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        request.setAttribute("lendOrder", lendOrder);

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        LendProduct lendProduct = lendProductService.findById(lendOrder.getLendProductId());
        Map<String, String> bindCardInfo = (Map<String, String>) session.getAttribute("bindCardInfo");
        request.setAttribute("lendOrder", lendOrder);
        //todo 财富券校验
        List<VoucherVO> voucherVOs = new ArrayList<VoucherVO>();
        BigDecimal voucherPayValue = BigDecimal.ZERO;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)) {
            String[] voucher_Ids = voucherIds.split(",");
            //先校验财富券
            for (String voucherId : voucher_Ids) {
                VoucherVO vo = voucherService.getVoucherById(Long.valueOf(voucherId));
                voucherVOs.add(vo);
                //计算财富券总额
                voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
            }
            JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
            if (!jsonView.isSuccess()) {
                return jsonView.toJson();
            }
        }
        //校验-订单用户和当前用户必须相同
        if (!lendOrder.getLendUserId().equals(currentUser.getUserId())) {
            return JsonView.JsonViewFactory.create().success(false).info("您支付的订单不属于您").put("id", "redirect")
                    .toJson();
        }

        //校验-如果订单已经被处理过
        if (!lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue())) {
            return JsonView.JsonViewFactory.create().success(false).info("订单已处理过").put("id", "redirect")
                    .toJson();
        }

        //校验-银行卡号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(cardNo)) {
            return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空").put("id", "cardNo")
                    .toJson();
        }

        //校验-手机号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(phone)) {
            return JsonView.JsonViewFactory.create().success(false).info("手机号为空").put("id", "phone")
                    .toJson();
        }
        //校验-验证码是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(validCode)) {
            return JsonView.JsonViewFactory.create().success(false).info("验证码为空").put("id", "valid")
                    .toJson();
        }

        //校验-是否有交易密码
        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        //校验-交易密码是否正确
        if (!userByUserId.getBidPass().equals(MD5Util.MD5Encode(jypassword, "utf-8"))) {
            return JsonView.JsonViewFactory.create().success(false).info("交易密码错误").put("id", "jypassword")
                    .toJson();
        }

        //校验-财富券拆分
        if (lendOrder.getBuyBalance().compareTo(voucherPayValue) < 0) {
            return JsonView.JsonViewFactory.create().success(false).info("财富券金额不能打于订单金额").put("id", "redirect")
                    .toJson();
        }

        //校验-购买金额加和是否等于订单金额
        if (lendOrder.getBuyBalance().compareTo(accountPayValue.add(rechargePayValue).add(voucherPayValue)) != 0) {
            return JsonView.JsonViewFactory.create().success(false).info("购买金额异常").put("id", "redirect")
                    .toJson();
        }
        //购买金额是否合法
        String _result = amountValidate(lendOrder);
        if (_result != null)
            return _result;


        //校验-卡号不能更改
        cardNo = cardNo.replace(" ", "");
        if (!cardNo.equals(bindCardInfo.get("bcCardNo"))) {
            return JsonView.JsonViewFactory.create().success(false).info("点击获取验证码以后不能再更改卡号").put("id", "cardNo")
                    .toJson();
        }

        //校验-手机号不能更改
        if (!phone.equals(bindCardInfo.get("bcPhone"))) {
            request.setAttribute("errorMsg", "");
            return JsonView.JsonViewFactory.create().success(false).info("点击获取验证码以后不能再更改手机号").put("id", "phone")
                    .toJson();
        }

        //绑卡
        //由于绑卡后不能二次绑卡，所以测试环境的时候先设置为非测试环境进行绑卡，然后设置成测试环境进行支付
        Map<String, String> result = new HashMap<String, String>();
        System.out.println(">>>>>>>>>>>>>>>>>>>" + TZTService.isBindCard());
        TokenUtils.validateToken(request);
        if (TZTService.isBindCard())
            result = bindCard(validCode, bindCardInfo);
        if (result.get("error_code") != null) {
            if (result.get("error_code").equals("600311")) {

                String tokenString = TokenUtils.setNewToken(request);
                return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "valid").put("tocken", tokenString)
                        .toJson();
            }
            session.removeAttribute("smsCode");
            return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "redirect")
                    .toJson();
        }

        //更新银行卡状态数据
        CustomerCard customerCard = updateCardStatus(currentUser, cardNo);
        //确定易宝绑卡成功
        boolean bool = confirmBindCardSuccess(currentUser, cardNo);
        if (!bool)
            return JsonView.JsonViewFactory.create().success(false).info("支付失败，请稍候重试").put("id", "redirect")
                    .toJson();


        session.removeAttribute("bindCardInfo");
        session.removeAttribute("smsCode");
        if (customerCard == null) {
            return JsonView.JsonViewFactory.create().success(false).info("绑卡信息已过期").put("id", "redirect")
                    .toJson();
        }

        //创建支付订单
        PayOrder payOrder = createPayOrder(accountPayValue, rechargePayValue, voucherPayValue, currentUser, lendOrder);

        //支付单明细和财富券保存关系同时冻结财富券
        voucherService.linkVoucher(payOrder, voucherVOs);

        //创建充值订单
        RechargeOrder rechargeOrder = this.rechargeOrderService.addRechargeOrder(rechargePayValue, customerCard.getCustomerCardId(), payOrder.getPayId(), currentUser.getUserId(), "YB_TZT", PropertiesUtils.getInstance().get("SOURCE_WEIXIN"));
        session.setAttribute("rechargeOrderCode", rechargeOrder.getRechargeCode());
        //发起无验证码支付，但并不
        result = doNoVCRecharge(request, rechargePayValue, currentUser, lendProduct, customerCard, rechargeOrder);
        if (result.get("error_code") != null) {
            return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "redirect")
                    .toJson();
        }
        return JsonView.JsonViewFactory.create().success(true)
                .toJson();
    }

    @SuppressWarnings({"unused", "rawtypes"})
    private boolean confirmBindCardSuccess(UserInfo currentUser, String card) {
        int times = 3;
        boolean bool = false;
        for (int i = 3; i > 0; i--) {
            Map<String, String> queryList = TZTService.queryAuthbindList(StringUtils.getFixLengthUserId(currentUser.getUserId().toString()), "2");
            if (queryList.get("error_code") == null) {
                String cardList = queryList.get("cardlist");
                Gson gson = new Gson();
                Collection result = (List) JSON.parseObject(cardList, List.class);
                if (result != null && result.size() > 0) {
                    for (Object jo : result) {
                        JSONObject j = (JSONObject) jo;
                        String card_last = (String) j.get("card_last");
                        String card_top = (String) j.get("card_top");
                        if (card.indexOf(card_top) != -1 && card.indexOf(card_last) != -1) {
                            bool = true;
                            break;
                        }
                    }
                }
            }
            //成功了停止查询
            if (bool) {
                break;
            }
            try {
                Thread.sleep(2000);//两秒查询一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return bool;
    }

    /**
     * 支付金额验证
     *
     * @param lendOrder
     * @return 返回null表示验证通过
     */
    private String amountValidate(LendOrder lendOrder) {

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
            //投标金额验证
            List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
            LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
            LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());

            if (currentUser != null) {
                //获取账户余额
                if (lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getRemain()) > 0) {
                    return JsonView.JsonViewFactory.create().success(false).info("金额已超出剩余金额！").put("id", "redirect")
                            .toJson();
                }
                BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
                if (lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy)) > 0) {
                    //无法购买剩余标
                    JsonView.JsonViewFactory.create().success(false).info("金额已超出剩余金额！").put("id", "redirect")
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
                return JsonView.JsonViewFactory.create().success(false).info("金额已超出当前最大可投金额！").put("id", "redirect")
                        .toJson();
        } else if (lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())) {
            CreditorRightsTransferApplication creditorRights = creditorRightsTransferAppService.getTransferApplicationByLendOrderId(lendOrder.getLendOrderId());
            Long creditorRightsId = creditorRights.getApplyCrId();
            BigDecimal amount = lendOrder.getBuyBalance();

            BigDecimal totalBuy = creditorRightsTransferAppService.getRemainingRightsPrice(creditorRights.getCreditorRightsApplyId());
            CreditorRightsTransferApplication crta = creditorRightsTransferAppService.getEffectiveTransferApplyByCreditorRightsId(creditorRightsId);
            if (new BigDecimal("100").compareTo(crta.getApplyPrice().subtract(totalBuy).subtract(amount)) > 0
                    && BigDecimal.ZERO.compareTo(crta.getApplyPrice().subtract(totalBuy).subtract(amount)) < 0)
                throw new SystemException(CreditorErrorCode.CREDITOR_RESIDUAL_MIN);
            if (amount.compareTo(crta.getApplyPrice().subtract(totalBuy)) > 0) {
                // 购买金额变为剩余可投金额
                if (crta.getApplyPrice().compareTo(totalBuy) == 0) {
                    return JsonView.JsonViewFactory.create().success(false).info(CreditorErrorCode.CREDITOR_AMOUNT_ZONE.getDesc())
                            .put("id", "redirect").toJson();
                }
                amount = crta.getApplyPrice().subtract(totalBuy);
            }
        } else {
            //省心计划金额验证
            if (!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
                throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
            LendProductPublish lendProductPublish = lendProductService.getLendProductPublishByPublishId(lendOrder.getLendProductPublishId());
            //校验金额是否合法
            BigDecimal waitingBuyBalance = lendProductPublish.getPublishBalance().subtract(lendProductPublish.getSoldBalance());
            if (LendProductPublish.PUBLISHBALANCETYPE_SPEC == lendProductPublish.getPublishBalanceType()) {//如果购买的是指定金额的省心计划
                if (lendOrder.getBuyBalance().compareTo(waitingBuyBalance) > 0) {
                    return JsonView.JsonViewFactory.create().success(false).info("购买金额已超出剩余可购买金额").put("id", "redirect").toJson();
                }
            }
        }
        return null;
    }


    private Map<String, String> doNoVCRecharge(HttpServletRequest request,
                                               BigDecimal rechargePayValue,
                                               UserInfo currentUser,
                                               LendProduct lendProduct,
                                               CustomerCard customerCard,
                                               RechargeOrder rechargeOrder) {
        Map<String, String> result;
        Map<String, String> map = new TreeMap<String, String>();
        map.put("orderid", rechargeOrder.getRechargeCode());
        map.put("transtime", StringUtils.getTimeStr());
        map.put("amount", TZTService.isTesting() ? "1" : String.valueOf(rechargePayValue.multiply(new BigDecimal("100")).longValue()));
        map.put("productname", lendProduct.getProductName());
        map.put("identityid", StringUtils.getFixLengthUserId(currentUser.getUserId() + ""));
        map.put("identitytype", "2");
        map.put("orderexpdate", "30");
        map.put("card_top", StringUtils.getPrefix6ardNo(customerCard.getCardCode()));
        map.put("card_last", StringUtils.getLast4CardNo(customerCard.getCardCode()));
        map.put("callbackurl", TZTService.getCallbackUrl());
        map.put("userip", SecurityUtil.getIpAddr(request));
        map.put("currency", 156 + "");
        ValidationUtil.checkNotExistNullValue(map);
        result = TZTService.directBindPay(map);
        return result;
    }

    @SuppressWarnings("unchecked")
    private PayOrder createPayOrder(BigDecimal accountPayValue, BigDecimal rechargePayValue, BigDecimal voucherPayValue, UserInfo currentUser, LendOrder lendOrder) {
        PayConstants.BusTypeEnum busTypeEnum = null;
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
            busTypeEnum = PayConstants.BusTypeEnum.BUY_FINANCE;

        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue()))
            busTypeEnum = PayConstants.BusTypeEnum.BID_LOAN;
        Pair<String, BigDecimal> accountAmount = new Pair<String, BigDecimal>(PayConstants.AmountType.ACCOUNT.getValue(), accountPayValue);
        Pair<String, BigDecimal> rechargeAmount = new Pair<String, BigDecimal>(PayConstants.AmountType.RECHARGE.getValue(), rechargePayValue);
        //todo 财富券
        Pair<String, BigDecimal> voucherAmount = new Pair<String, BigDecimal>(PayConstants.AmountType.VOUCHERS.getValue(), voucherPayValue);
        return this.payService.addPayOrderForLendOrder(lendOrder, new Date(), busTypeEnum, accountAmount, rechargeAmount, voucherAmount);
    }

    @SuppressWarnings("unchecked")
    private CustomerCard updateCardStatus(UserInfo currentUser, String cardNo) {
        HttpSession session = this.getRequest().getSession();
        Map<String, String> bindCardInfo = (Map<String, String>) session.getAttribute("bindCardInfo");

        CustomerCard customerCard = this.customerCardService.findById(Long.valueOf(bindCardInfo.get("bcCardId")));
        if (!customerCard.getCardCode().equals(cardNo))
            return null;
        customerCard.setStatus(CustomerCard.STATUS_ENABLE);//卡状态
        customerCard.setBindStatus(CustomerCardBindStatus.BINDED.getValue());
        customerCard.setUpdateTime(new Date());//最后更改时间
        customerCard = customerCardService.saveOrUpdateCustomerCard(customerCard);
        return customerCard;
    }

    private Map<String, String> bindCard(String validCode, Map<String, String> bindCardInfo) {
        String requestId = bindCardInfo.get("bcRequestId");
        Map<String, String> map = new TreeMap<String, String>();
        map.put("requestid", requestId);
        map.put("validatecode", validCode);
        ValidationUtil.checkNotExistNullValue(map);
        return TZTService.confirmBindBankcard(map);
    }


    /**
     * 已绑定银行卡的获取验证码请求
     *
     * @param request
     * @param lendOrderId
     * @param cardId
     * @param accountPayValue
     * @param rechargePayValue
     * @param voucherIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/invokePay")
    public Object invokePay(HttpServletRequest request,
                            @RequestParam(value = "lendOrderId") Long lendOrderId,
                            @RequestParam(value = "cardId") Long cardId,
                            @RequestParam(value = "accountPayValue", defaultValue = "0") BigDecimal accountPayValue,
                            @RequestParam(value = "rechargePayValue", defaultValue = "0") BigDecimal rechargePayValue,
                            @RequestParam(value = "userVoucherId", required = false) String voucherIds) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        CustomerCard card = customerCardService.findById(cardId);
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        LendProduct lendProduct = lendProductService.findById(lendOrder.getLendProductId());

        //校验-如果订单已经被处理过
        if (!lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue())) {
            return JsonView.JsonViewFactory.create().success(false).info("订单已处理过").put("id", "redirect")
                    .toJson();
        }
        //todo 财富券
        List<VoucherVO> voucherVOs = new ArrayList<VoucherVO>();
        BigDecimal voucherPayValue = BigDecimal.ZERO;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)) {
            String[] voucher_Ids = voucherIds.split(",");
            //先校验财富券
            for (String voucherId : voucher_Ids) {
                VoucherVO vo = voucherService.getVoucherById(Long.valueOf(voucherId));
                voucherVOs.add(vo);
                //计算财富券总额
                voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
            }
            JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
            if (!jsonView.isSuccess()) {
                return jsonView.toJson();
            }
        }
        //校验-财富券拆分
        if (lendOrder.getBuyBalance().compareTo(voucherPayValue) < 0) {
            return JsonView.JsonViewFactory.create().success(false).info("财富券金额不能打于订单金额").put("id", "redirect")
                    .toJson();
        }

        //校验-购买金额加和是否等于订单金额
        if (lendOrder.getBuyBalance().compareTo(accountPayValue.add(rechargePayValue).add(voucherPayValue)) != 0) {
            return JsonView.JsonViewFactory.create().success(false).info("购买金额异常！").put("id", "redirect")
                    .toJson();
        }

        //校验-卡是否存在
        if (card == null) {
            return JsonView.JsonViewFactory.create().success(false).info("您还没有绑定银行卡！").put("id", "redirect")
                    .toJson();
        }

        //校验-是否是当前用户的银行卡
        if (!card.getUserId().equals(currentUser.getUserId())) {
            return JsonView.JsonViewFactory.create().success(false).info("请用自己的卡操作！").put("id", "redirect")
                    .toJson();
        }

        //校验-当前卡是否已经失效
        if (card.getStatus().equals(CustomerCardStatus.DISABLED.getValue())) {
            return JsonView.JsonViewFactory.create().success(false).info("卡已失效").put("id", "redirect")
                    .toJson();
        }

        //创建支付订单
        PayOrder payOrder = createPayOrder(accountPayValue, rechargePayValue, voucherPayValue, currentUser, lendOrder);
        //支付单明细和财富券保存关系
        voucherService.linkVoucher(payOrder, voucherVOs);
        //创建充值订单
        RechargeOrder rechargeOrder = this.rechargeOrderService.addRechargeOrder(rechargePayValue, card.getCustomerCardId(), payOrder.getPayId(), currentUser.getUserId(), "YB_TZT", PropertiesUtils.getInstance().get("SOURCE_WEIXIN"));
        request.getSession().setAttribute("rechargeOrderCode", rechargeOrder.getRechargeCode());
        //发起支付请求
        Map<String, String> map = new TreeMap<String, String>();
        map.put("orderid", rechargeOrder.getRechargeCode());
        map.put("transtime", StringUtils.getTimeStr());
        map.put("amount", TZTService.isTesting() ? "1" : String.valueOf(rechargePayValue.multiply(new BigDecimal("100")).longValue()));
        map.put("productname", lendProduct.getProductName());
        map.put("identityid", StringUtils.getFixLengthUserId(currentUser.getUserId() + ""));
        map.put("currency", 156 + "");
        map.put("identitytype", "2");
        map.put("orderexpdate", "60");
        map.put("card_top", StringUtils.getPrefix6ardNo(card.getCardCode()));
        map.put("card_last", StringUtils.getLast4CardNo(card.getCardCode()));
        map.put("callbackurl", TZTService.getCallbackUrl());
        map.put("userip", SecurityUtil.getIpAddr(request));
        ValidationUtil.checkNotExistNullValue(map);
        Map<String, String> result = TZTService.payNeedSms(map);

        request.getSession().setAttribute("smsCode", "0");
        logger.info(LogUtils.createSimpleLog("支付请求接口返回", result.toString()));
        if (result.get("error_code") != null)
            return JsonView.JsonViewFactory.create().success(false).info("验证码获取失败，" + result.get("error_msg")).put("id", "valid")
                    .toJson();


        //发起支付验证码请求
        result = TZTService.sendSmsByOrder(rechargeOrder.getRechargeCode());
        if (result.get("error_code") != null)
            return JsonView.JsonViewFactory.create().success(false).info("验证码获取失败，" + result.get("error_msg")).put("id", "valid")
                    .toJson();

        return JsonView.JsonViewFactory.create().success(true).toJson();
    }

    /**
     * 未绑定银行卡时的获取验证码请求
     *
     * @param cardNo
     * @param phone
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = "/invokeBindCard")
    public Object invokeBindCard(@RequestParam(value = "cardNo", required = false) String cardNo,
                                 @RequestParam(value = "phone", required = false) String phone,
                                 HttpServletRequest request) throws Exception {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);

        //校验-银行卡号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(cardNo)) {
            return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空！").put("id", "bankid")
                    .toJson();
        }

        //校验-手机号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(phone)) {
            return JsonView.JsonViewFactory.create().success(false).info("手机号为空！").put("id", "phone")
                    .toJson();
        }

        //校验参数 todo
        cardNo = cardNo.replace(" ", "");
        //校验-用户是否已经绑卡
        CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
        if (card != null)
            return JsonView.JsonViewFactory.create().success(false).info("您已经绑过一张尾号：" + card.getCardCodeShort() + "卡了").put("id", "bankid")
                    .toJson();

        //校验-该卡是否有效
        Map<String, String> result = TZTService.bankCardCheck(cardNo);
        logger.info(LogUtils.createSimpleLog("银行卡信息查询接口返回", result.toString()));
        if (result.get("error_code") != null)
            return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "bankid")
                    .toJson();


        if (result.get("isvalid").equals("0"))
            return JsonView.JsonViewFactory.create().success(false).info("该卡已经失效").put("id", "bankid")
                    .toJson();

        //校验-必须是借记卡
        if (result.get("cardtype").equals("2"))
            return JsonView.JsonViewFactory.create().success(false).info("不支持信用卡").put("id", "bankid")
                    .toJson();

        //校验-如果该卡不在支持的银行列表中
        String bankname = result.get("bankname");
        Long bankid = null;
        List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("bank");
        for (ConstantDefine constantDefine : constantDefines) {
            if (bankname.equals(constantDefine.getConstantName())) {
                bankid = constantDefine.getConstantDefineId();
                break;
            }
        }

        if (bankid == null)
            return JsonView.JsonViewFactory.create().success(false).info("不支持该银行卡").put("id", "cardNo")
                    .toJson();

        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());

        //（2）请求绑卡
        //由于绑卡后不能二次绑卡，所以测试环境的时候先设置为非测试环境进行绑卡，然后设置成测试环境进行支付
        if (TZTService.isBindCard()) {
            Map map = new TreeMap();
            map.put("identityid", StringUtils.getFixLengthUserId(currentUser.getUserId() + ""));
            map.put("identitytype", "2");
            map.put("requestid", UUID.randomUUID().toString().replaceAll("-", ""));
            map.put("cardno", cardNo);
            map.put("idcardtype", "01");
            map.put("idcardno", userExt.getIdCard());
            map.put("username", userExt.getRealName());
            map.put("phone", phone);
            map.put("userip", SecurityUtil.getIpAddr(request));
            ValidationUtil.checkNotExistNullValue(map);
            result = TZTService.bindBankcard(map);
            if (result.get("error_code") != null)
                return JsonView.JsonViewFactory.create().success(false).info("验证码获取失败，" + result.get("error_msg")).put("id", "valid")
                        .toJson();
        }

        //（1）新建用户卡
        CustomerCard customerCard = new CustomerCard();
        customerCard.setUserId(currentUser.getUserId());
        customerCard.setCardType(CardType.FULL_CARD.getValue());
        customerCard.setMobile(phone);
        customerCard.setStatus(CustomerCardStatus.DISABLED.getValue());
        customerCard.setBindStatus(CustomerCardBindStatus.UNBINDING.getValue());
        customerCard.setCardCode(cardNo);
        customerCard.setCardcustomerName(userExt.getRealName());
        customerCard.setUpdateTime(new Date());
        customerCard.setBankCode(bankid);
        customerCardService.addCustomerCard(customerCard);

        Map<String, String> bindCardInfo = new HashMap<String, String>();
        bindCardInfo.put("bcCardId", customerCard.getCustomerCardId().toString());
        bindCardInfo.put("bcRequestId", result.get("requestid"));
        bindCardInfo.put("bcCardNo", cardNo);
        bindCardInfo.put("bcPhone", phone);
        request.getSession().setAttribute("bindCardInfo", bindCardInfo);
        request.getSession().setAttribute("smsCode", "0");

        return JsonView.JsonViewFactory.create().success(true).info("交易成功")
                .toJson();
    }

    @ResponseBody
    @RequestMapping(value = "/cbForYbNotification")
    @DoNotNeedLogin
    public String cbForYbNotification(HttpServletRequest request) throws Exception {
        Map<String, String> parameterMap = new HashMap<String, String>();
        Map<String, String[]> parameterMapArray = request.getParameterMap();
        for (String key : parameterMapArray.keySet()) {
            parameterMap.put(key, parameterMapArray.get(key)[0]);
        }
        Map<String, String> result = TZTService.parseCallbackRequest(parameterMap);
        logger.info(LogUtils.createSimpleLog("支付通知返回", result.toString()));
        rechargeOrderService.handleCallbackNotification(result);
        return "success";
    }


    /**
     * 充值列表
     *
     * @param request
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/rechargeList")
    @ResponseBody
    public Object rechargeList(HttpServletRequest request,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        //封装参数
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setUserId(currentUser.getUserId());
        Pagination<RechargeOrder> rechargeOrderPaging = rechargeOrderService.getRechargeOrderPaging(pageNo, pageSize, rechargeOrder, null);
        return rechargeOrderPaging;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = "/invokeBindCardRecharge")
    public Object invokeBindCardRecharge(@RequestParam(value = "cardNo", required = false) String cardNo,
                                         @RequestParam(value = "phone", required = false) String phone,
                                         HttpServletRequest request) throws Exception {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);

        //校验-银行卡号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(cardNo)) {
            return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空！").put("id", "bankid")
                    .toJson();
        }

        //校验-手机号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(phone)) {
            return JsonView.JsonViewFactory.create().success(false).info("手机号为空！").put("id", "phone")
                    .toJson();
        }

        //校验参数 todo
        cardNo = cardNo.replace(" ", "");
        //校验-用户是否已经绑卡
        CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
        if (card != null)
            return JsonView.JsonViewFactory.create().success(false).info("您已经绑过一张尾号：" + card.getCardCodeShort() + "卡了").put("id", "bankid")
                    .toJson();

        //校验-该卡是否有效
        Map<String, String> result = TZTService.bankCardCheck(cardNo);
        logger.info(LogUtils.createSimpleLog("银行卡信息查询接口返回", result.toString()));
        if (result.get("error_code") != null)
            return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "bankid")
                    .toJson();


        if (result.get("isvalid").equals("0"))
            return JsonView.JsonViewFactory.create().success(false).info("该卡已经失效").put("id", "bankid")
                    .toJson();

        //校验-必须是借记卡
        if (result.get("cardtype").equals("2"))
            return JsonView.JsonViewFactory.create().success(false).info("不支持信用卡").put("id", "bankid")
                    .toJson();

        //校验-如果该卡不在支持的银行列表中
        String bankname = result.get("bankname");
        Long bankid = null;
        List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("bank");
        for (ConstantDefine constantDefine : constantDefines) {
            if (bankname.equals(constantDefine.getConstantName())) {
                bankid = constantDefine.getConstantDefineId();
                break;
            }
        }

        if (bankid == null)
            return JsonView.JsonViewFactory.create().success(false).info("不支持该银行卡").put("id", "bankid")
                    .toJson();

        UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());

        //（2）请求绑卡
        //由于绑卡后不能二次绑卡，所以测试环境的时候先设置为非测试环境进行绑卡，然后设置成测试环境进行支付
        if (TZTService.isBindCard()) {
            Map map = new TreeMap();
            map.put("identityid", StringUtils.getFixLengthUserId(currentUser.getUserId() + ""));
            map.put("identitytype", "2");
            map.put("requestid", UUID.randomUUID().toString().replaceAll("-", ""));
            map.put("cardno", cardNo);
            map.put("idcardtype", "01");
            map.put("idcardno", userExt.getIdCard());
            map.put("username", userExt.getRealName());
            map.put("phone", phone);
            map.put("userip", SecurityUtil.getIpAddr(request));
            ValidationUtil.checkNotExistNullValue(map);
            result = TZTService.bindBankcard(map);
            if (result.get("error_code") != null)
                return JsonView.JsonViewFactory.create().success(false).info("验证码获取失败，" + result.get("error_msg")).put("id", "valid")
                        .toJson();
        }

        //（1）新建用户卡
        CustomerCard customerCard = new CustomerCard();
        customerCard.setUserId(currentUser.getUserId());
        customerCard.setCardType(CardType.FULL_CARD.getValue());
        customerCard.setMobile(phone);
        customerCard.setStatus(CustomerCardStatus.DISABLED.getValue());
        customerCard.setBindStatus(CustomerCardBindStatus.UNBINDING.getValue());
        customerCard.setCardCode(cardNo);
        customerCard.setCardcustomerName(userExt.getRealName());
        customerCard.setUpdateTime(new Date());
        customerCard.setBankCode(bankid);
        customerCardService.addCustomerCard(customerCard);

        Map<String, String> bindCardInfo = new HashMap<String, String>();
        bindCardInfo.put("bcCardId", customerCard.getCustomerCardId().toString());
        bindCardInfo.put("bcRequestId", result.get("requestid"));
        bindCardInfo.put("bcCardNo", cardNo);
        bindCardInfo.put("bcPhone", phone);
        request.getSession().setAttribute("bindCardInfo", bindCardInfo);
        request.getSession().setAttribute("recharge_smsCode", "0");

        return JsonView.JsonViewFactory.create().success(true).info("交易成功")
                .toJson();
    }


    @ResponseBody
    @RequestMapping(value = "/invokeRecharge")
    public Object invokeRecharge(HttpServletRequest request,
                                 @RequestParam(value = "rechargeAmount", defaultValue = "0") BigDecimal rechargeAmount) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);

        LendProduct lendProduct = new LendProduct();
        lendProduct.setProductName("财富派充值");
        //校验-购买金额是否大于50
        if (new BigDecimal("100").compareTo(rechargeAmount) > 0) {
            return JsonView.JsonViewFactory.create().success(false).info("请输入大于100元的金额！").put("id", "moneyp")
                    .toJson();
        }

        //校验-卡是否存在
        if (card == null) {
            return JsonView.JsonViewFactory.create().success(false).info("您还没有绑定银行卡！").put("id", "redirect")
                    .toJson();
        }

        //校验-是否是当前用户的银行卡
        if (!card.getUserId().equals(currentUser.getUserId())) {
            return JsonView.JsonViewFactory.create().success(false).info("请用自己的卡操作！").put("id", "redirect")
                    .toJson();
        }

        //校验-当前卡是否已经失效
        if (card.getStatus().equals(CustomerCardStatus.DISABLED.getValue())) {
            return JsonView.JsonViewFactory.create().success(false).info("卡已失效！").put("id", "redirect")
                    .toJson();
        }

        //创建充值订单
        RechargeOrder rechargeOrder = this.rechargeOrderService.addRechargeOrder(rechargeAmount, card.getCustomerCardId(), null, currentUser.getUserId(), "YB_TZT", PropertiesUtils.getInstance().get("SOURCE_WEIXIN"));
        request.getSession().setAttribute("rechargeOrderCode_income", rechargeOrder.getRechargeCode());
        //发起支付请求
        Map<String, String> map = new TreeMap<String, String>();
        map.put("orderid", rechargeOrder.getRechargeCode());
        map.put("transtime", StringUtils.getTimeStr());
        map.put("amount", TZTService.isTesting() ? "1" : rechargeAmount.multiply(new BigDecimal("100")).toString());
        map.put("productname", lendProduct.getProductName());
        map.put("identityid", StringUtils.getFixLengthUserId(currentUser.getUserId() + ""));
        map.put("currency", 156 + "");
        map.put("identitytype", "2");
        map.put("orderexpdate", "60");
        map.put("card_top", StringUtils.getPrefix6ardNo(card.getCardCode()));
        map.put("card_last", StringUtils.getLast4CardNo(card.getCardCode()));
        map.put("callbackurl", TZTService.getCallbackUrl());
        map.put("userip", SecurityUtil.getIpAddr(request));
        ValidationUtil.checkNotExistNullValue(map);
        Map<String, String> result = TZTService.payNeedSms(map);
        request.getSession().setAttribute("recharge_smsCode", "0");
        logger.info(LogUtils.createSimpleLog("支付请求接口返回", result.toString()));
        if (result.get("error_code") != null)
            return JsonView.JsonViewFactory.create().success(false).info("获取验证码失败，" + result.get("error_msg")).put("id", "valid")
                    .toJson();

        //发起支付验证码请求
        result = TZTService.sendSmsByOrder(rechargeOrder.getRechargeCode());
        if (result.get("error_code") != null)
            return JsonView.JsonViewFactory.create().success(false).info("获取验证码失败，" + result.get("error_msg")).put("id", "valid")
                    .toJson();

        return JsonView.JsonViewFactory.create().success(true)
                .toJson();
    }

    /**
     * 确认绑卡并充值
     *
     * @param request
     * @param session
     * @param validCode
     * @param jypassword
     * @param cardNo
     * @param phone
     * @param rechargeAmount
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/confirmRechargeValidateNoCard")
    @ResponseBody
    public String confirmRechargeValidateNoCard(HttpServletRequest request, HttpSession session,
                                                @RequestParam(value = "valid") String validCode,
                                                @RequestParam(value = "password") String jypassword,
                                                @RequestParam(value = "cardNo") String cardNo,
                                                @RequestParam(value = "phone") String phone,
                                                @RequestParam(value = "rechargeAmount", defaultValue = "0") BigDecimal rechargeAmount) {
        //是否点击验证码
        String rechargeOrderCode = (String) session.getAttribute("recharge_smsCode");
        if (org.apache.commons.lang.StringUtils.isEmpty(rechargeOrderCode)) {
            return JsonView.JsonViewFactory.create().success(false).info("您未获取短信验证码！").put("id", "valid")
                    .toJson();
        }
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        //校验交易密码
        if (userByUserId.getBidPass() == null)
            return JsonView.JsonViewFactory.create().success(false).info("请您输入交易密码！").put("id", "jypassword")
                    .toJson();
        if (!MD5Util.MD5Encode(jypassword, null).equals(userByUserId.getBidPass()))
            return JsonView.JsonViewFactory.create().success(false).info("交易密码错误").put("id", "jypassword")
                    .toJson();

        //校验-银行卡号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(cardNo))
            return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空！").put("id", "bankid")
                    .toJson();

        //校验-手机号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(phone))
            return JsonView.JsonViewFactory.create().success(false).info("手机号为空！").put("id", "phone")
                    .toJson();

        //校验-验证码是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(validCode))
            return JsonView.JsonViewFactory.create().success(false).info("验证码为空！").put("id", "valid")
                    .toJson();

        //校验-是否有交易密码
        if (userByUserId.getBidPass() == null)
            return JsonView.JsonViewFactory.create().success(false).info("还未设定交易密码！").put("id", "jypassword")
                    .toJson();

        //校验-购买金额是否大于50
        if (new BigDecimal("100").compareTo(rechargeAmount) > 0)
            return JsonView.JsonViewFactory.create().success(false).info("请输入大于100元的金额！").put("id", "moneyp")
                    .toJson();

        //校验-卡号不能更改
        Map<String, String> bindCardInfo = (Map<String, String>) session.getAttribute("bindCardInfo");
        cardNo = cardNo.replace(" ", "");
        if (!cardNo.equals(bindCardInfo.get("bcCardNo")))
            return JsonView.JsonViewFactory.create().success(false).info("点击获取验证码以后不能再更改卡号！").put("id", "bankid")
                    .toJson();

        //校验-手机号不能更改
        if (!phone.equals(bindCardInfo.get("bcPhone")))
            return JsonView.JsonViewFactory.create().success(false).info("点击获取验证码以后不能再更改手机号！").put("id", "phone")
                    .toJson();

        //绑卡
        //由于绑卡后不能二次绑卡，所以测试环境的时候先设置为非测试环境进行绑卡，然后设置成测试环境进行支付
        Map<String, String> result = new HashMap<String, String>();
        if (TZTService.isBindCard())
            TokenUtils.validateToken(request);
        result = bindCard(validCode, bindCardInfo);
        if (result.get("error_code") != null) {
            if (result.get("error_code").equals("600311")) {
                String tokenString = TokenUtils.setNewToken(request);
                session.setAttribute("recharge_smsCode", "0");
                return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "valid").put("tocken", tokenString)
                        .toJson();
            }
            session.removeAttribute("recharge_smsCode");
            return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "redirect")
                    .toJson();
        }

        //更新银行卡状态数据
        CustomerCard customerCard = updateCardStatus(currentUser, cardNo);
        //确定易宝绑卡成功
        boolean bool = confirmBindCardSuccess(currentUser, cardNo);
        if (!bool)
            return JsonView.JsonViewFactory.create().success(false).info("支付失败，请稍候重试").put("id", "redirect")
                    .toJson();

        session.removeAttribute("bindCardInfo");
        if (customerCard == null) {
            return JsonView.JsonViewFactory.create().success(false).info("绑卡信息已过期").put("id", "redirect")
                    .toJson();
        }

        //创建充值订单
        RechargeOrder rechargeOrder = this.rechargeOrderService.addRechargeOrder(rechargeAmount, customerCard.getCustomerCardId(), null, currentUser.getUserId(), "YB_TZT", PropertiesUtils.getInstance().get("SOURCE_WEIXIN"));
        request.getSession().setAttribute("rechargeOrderCode_income", rechargeOrder.getRechargeCode());
        //发起无验证码支付，但并不
        LendProduct lendProduct = new LendProduct();
        lendProduct.setProductName("财富派充值");
        result = doNoVCRecharge(request, rechargeAmount, currentUser, lendProduct, customerCard, rechargeOrder);

        session.removeAttribute("recharge_smsCode");
        if (result.get("error_code") != null) {
            return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "redirect")
                    .toJson();
        }
        return JsonView.JsonViewFactory.create().success(true).info("交易密码正确").put("id", "jypassword")
                .toJson();
    }

    @RequestMapping(value = "/confirmRechargeValidate")
    @ResponseBody
    public String validate(@RequestParam(value = "valid", required = false) String validCode, HttpServletRequest request, HttpSession session) {

        //是否点击验证码
        String smsCode = (String) session.getAttribute("recharge_smsCode");
        if (org.apache.commons.lang.StringUtils.isEmpty(smsCode)) {
            return JsonView.JsonViewFactory.create().success(false).info("您未获取短信验证码！").put("id", "valid")
                    .toJson();
        }
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        //校验交易密码
        if (userByUserId.getBidPass() == null)
            return JsonView.JsonViewFactory.create().success(false).info("请您输入交易密码！").put("id", "jypassword")
                    .toJson();

        //校验通过后继续支付
        String rechargeOrderCode = (String) session.getAttribute("rechargeOrderCode_income");
        //校验-参数必填
        ValidationUtil.checkRequiredPara(new NameValue<String, Object>("valid", validCode));

        RechargeOrder rechargeOrder = rechargeOrderService.getRechargeOrderByOrderCode(rechargeOrderCode, false);

        //校验-充值订单必须存在
        if (rechargeOrder == null) {
            return JsonView.JsonViewFactory.create().success(false).info("充值订单时出现异常").put("id", "redirect")
                    .toJson();
        }

        //校验-订单用户和当前用户必须相同
        if (!rechargeOrder.getUserId().equals(currentUser.getUserId())) {
            return JsonView.JsonViewFactory.create().success(false).info("您充值的订单不属于您").put("id", "redirect")
                    .toJson();
        }
        //确认支付
        return rechargeWithCard(validCode, request, session, rechargeOrder);
    }


    /**
     * 已绑定银行卡的确认支付操作
     *
     * @param validCode
     * @param lendOrderId
     * @param jypassword
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/confirmPayValidate")
    @ResponseBody
    public String confirmPayValidate(@RequestParam(value = "valid", required = false) String validCode,
                                     @RequestParam(value = "lendOrderId", required = false) Long lendOrderId,
                                     @RequestParam(value = "password") String jypassword,
                                     HttpServletRequest request, HttpSession session) {
        //校验-参数必填
        ValidationUtil.checkRequiredPara(new NameValue<String, Object>("valid", validCode),
                new NameValue<String, Object>("lendOrderId", lendOrderId));
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        request.setAttribute("lendOrder", lendOrder);
        //校验-如果订单已经被处理过
        if (!lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue())) {
            return JsonView.JsonViewFactory.create().success(false).info("订单已处理过").put("id", "redirect")
                    .toJson();
        }
        //是否点击验证码
        String smsCode = (String) session.getAttribute("smsCode");
        if (org.apache.commons.lang.StringUtils.isEmpty(smsCode)) {
            return JsonView.JsonViewFactory.create().success(false).info("您未获取短信验证码！").put("id", "valid")
                    .toJson();
        }
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        //校验交易密码
        if (userByUserId.getBidPass() == null)
            return JsonView.JsonViewFactory.create().success(false).info("请您输入交易密码！").put("id", "jypassword")
                    .toJson();

        if (!MD5Util.MD5Encode(jypassword, null).equals(userByUserId.getBidPass()))
            return JsonView.JsonViewFactory.create().success(false).info("交易密码错误").put("id", "jypassword")
                    .toJson();

        //校验通过后继续支付
        String rechargeOrderCode = (String) session.getAttribute("rechargeOrderCode");
        //校验-参数必填
        ValidationUtil.checkRequiredPara(new NameValue<String, Object>("valid", validCode));

        RechargeOrder rechargeOrder = rechargeOrderService.getRechargeOrderByOrderCode(rechargeOrderCode, false);
        session.setAttribute("rechargeOrderCode", rechargeOrder.getRechargeCode());
        PayOrder payOrder = payService.getCurrentUnpayPayOrderByLendOrderId(lendOrderId);
        //校验-支付订单必须存在
        if (payOrder == null) {
            return JsonView.JsonViewFactory.create().success(false).info("支付订单时出现异常").put("id", "redirect")
                    .toJson();
        }

        //校验-订单用户和当前用户必须相同
        if (!rechargeOrder.getUserId().equals(currentUser.getUserId())) {
            return JsonView.JsonViewFactory.create().success(false).info("该支付单不属于您").put("id", "redirect")
                    .toJson();
        }

        //校验-充值订单必须存在
        if (rechargeOrder == null) {
            return JsonView.JsonViewFactory.create().success(false).info("支付订单时出现异常").put("id", "redirect")
                    .toJson();
        }
        //购买金额是否合法
        String _result = amountValidate(lendOrder);
        if (_result != null)
            return _result;


        //确认支付
        TokenUtils.validateToken(request);
        Map<String, String> map = new TreeMap<String, String>();
        map.put("orderid", rechargeOrder.getRechargeCode());
        map.put("validatecode", validCode);
        ValidationUtil.checkNotExistNullValue(map);
        Map<String, String> result = TZTService.smsConfirm(map);

        request.getSession().removeAttribute("bindCardInfo");
        if (result.get("error_code") != null) {
            if (result.get("error_code").equals("600116")) {
                //验证码错误导致
                String tokenString = TokenUtils.setNewToken(request);
                session.setAttribute("smsCode", "0");
                return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "valid").put("tocken", tokenString)
                        .toJson();
            }
            session.removeAttribute("smsCode");
            return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "redirect")
                    .toJson();
        }


        return JsonView.JsonViewFactory.create().success(true)
                .toJson();
    }

    /**
     * 有卡支付 充值
     *
     * @param validCode
     * @param request
     * @param session
     * @param rechargeOrder
     * @return
     */
    private String rechargeWithCard(@RequestParam(value = "valid", required = false) String validCode, HttpServletRequest request, HttpSession session, RechargeOrder rechargeOrder) {
        TokenUtils.validateToken(request);
        Map<String, String> map = new TreeMap<String, String>();
        map.put("orderid", rechargeOrder.getRechargeCode());
        map.put("validatecode", validCode);
        ValidationUtil.checkNotExistNullValue(map);

        Map<String, String> result = TZTService.smsConfirm(map);

        if (result.get("error_code") != null) {
            if (result.get("error_code").equals("600116")) {
                session.setAttribute("recharge_smsCode", "0");
                String tokenString = TokenUtils.setNewToken(request);
                return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "valid").put("tocken", tokenString)
                        .toJson();
            }
            session.removeAttribute("recharge_smsCode");
            return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "redirect")
                    .toJson();
        }

        return JsonView.JsonViewFactory.create().success(true).info("支付请求成功").put("id", "redirect")
                .toJson();
    }


    /**
     * 购买支付确认
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/confirmRecharge")
    @ResponseBody
    public String confirmRecharge(HttpSession session) {
        final String rechargeOrderCode = (String) session.getAttribute("rechargeOrderCode");
        //模拟回调
      /*  Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> result = new HashMap<String,String>();
                result.put("orderid",rechargeOrderCode);
                result.put("yborderid","00000000000000000001");
                result.put("status","1");
                rechargeOrderService.handleCallbackNotification(result);
            }
        });

        thread.start();*/

        RechargeOrder rechargeOrder = rechargeOrderService.getRechargeOrderByOrderCode(rechargeOrderCode, false);
        String status = rechargeOrder.getStatus();
        if (status.equals(RechargeStatus.FAILE.getValue()))
            return "false";
        if (status.equals(RechargeStatus.UN_RECHARGE.getValue()))
            return "recharging";
        if (status.equals(RechargeStatus.SUCCESS.getValue()))
            return "success";
        return null;
    }

    /**
     * 充值支付确认
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/confirmRechargeIncome")
    @ResponseBody
    public String confirmRechargeIncome(HttpSession session) {
        String rechargeOrderCode = (String) session.getAttribute("rechargeOrderCode_income");
        RechargeOrder rechargeOrder = rechargeOrderService.getRechargeOrderByOrderCode(rechargeOrderCode, false);
        String status = rechargeOrder.getStatus();
        if (status.equals(RechargeStatus.FAILE.getValue()))
            return "false";
        if (status.equals(RechargeStatus.UN_RECHARGE.getValue()))
            return "recharging";
        if (status.equals(RechargeStatus.SUCCESS.getValue()))
            return "success";
        return null;
    }


    /**
     * 处理成功页面内
     *
     * @return
     */
    @RequestMapping(value = "/rechargeSuccess")
    public String rechargeSuccess() {
        return "/order/rechargePaySuccess";
    }

    /**
     * 处理失败页面
     *
     * @param request
     * @param errorMsg
     * @return
     */
    @RequestMapping(value = "/rechargeFailure")
    public String rechargeFailure(HttpServletRequest request, String errorMsg) {
        request.setAttribute("errorMsg", errorMsg);
        return "/order/rechargePayError";
    }

    /**
     * 处理中页面
     *
     * @return
     */
    @RequestMapping(value = "/recharging")
    public String recharging() {
        return "/order/recharging";
    }


    /**
     * 处理成功页面内
     *
     * @return
     */
    @RequestMapping(value = "/paySuccess")
    public String paySuccess(HttpServletRequest request, Long lendOrderId) {
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        request.setAttribute("lendOrder", lendOrder);
        return "/order/paySuccess";
    }

    /**
     * 处理失败页面
     *
     * @param request
     * @param errorMsg
     * @return
     */
    @RequestMapping(value = "/payError")
    public String payError(HttpServletRequest request, String errorMsg, Long lendOrderId) {
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        request.setAttribute("errorMsg", errorMsg);
        request.setAttribute("lendOrder", lendOrder);
        return "/order/payError";
    }

    /**
     * 处理中页面
     *
     * @return
     */
    @RequestMapping(value = "/paying")
    public String paying(HttpServletRequest request, Long lendOrderId) {
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        request.setAttribute("lendOrder", lendOrder);
        return "/order/paying";
    }

    /**
     * 连连支付充值请求
     *
     * @param request
     * @param rechargeAmount
     * @return
     */
    @RequestMapping(value = "/llRecharge")
    @ResponseBody
    public String llRecharge(HttpServletRequest request, HttpSession session, @RequestParam(value = "cardNo", required = false) String cardNo,
                             @RequestParam(value = "cardId", required = false) String cardId,
                             @RequestParam(value = "rechargeAmount", defaultValue = "0") BigDecimal rechargeAmount) {

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(currentUser.getUserId());
        // 校验-银行卡号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(cardNo))
            return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空！").put("id", "bankid").toJson();
        // 校验-购买金额是否大于50
        if (new BigDecimal("100").compareTo(rechargeAmount) > 0)
            return JsonView.JsonViewFactory.create().success(false).info("请输入大于100元的金额！").put("id", "moneyp").toJson();

        // 校验-卡
        CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
        Long bankid = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(cardId)) {
            // 有卡
            if (card == null) {
                // 连连没有绑定，查询易宝绑定情况
                CustomerCard ybBindCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.YB);
                if (null != ybBindCard) {
                    // 易宝绑定的卡 连连是否支持
                    boolean support = LLPayUtil.checkLLCardSupport(ybBindCard.getCardCode());
                    if (!support) {
                        return JsonView.JsonViewFactory.create().success(false).info("您已绑定的卡支付通道不支持！").put("id", "bankid").toJson();
                    } else {
                        card = createNewCard(ybBindCard.getCardCode(), currentUser, userInfoExt, ybBindCard.getBankCode());
                    }
                } else {
                    // 易宝连连都没绑定
                    return JsonView.JsonViewFactory.create().success(false).info("您还没有绑定银行卡！").put("id", "bankid").toJson();
                }
            } else {
                if (!card.getCustomerCardId().toString().equals(cardId))
                    return JsonView.JsonViewFactory.create().success(false).info("您绑定的银行卡信息异常！").put("id", "redirect").toJson();
            }
        } else {
            // 无卡
            // 校验-银行卡号是否为空
            if (org.apache.commons.lang.StringUtils.isEmpty(cardNo)) {
                return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空！").put("id", "bankid").toJson();
            }
            // 连连cardbin查询
            // 校验-该卡是否有效
            cardNo = cardNo.replace(" ", "");
            Map<String, String> result = LLPayUtil.bankCardCheck(cardNo);
            logger.info(LogUtils.createSimpleLog("银行卡信息查询接口返回", result.toString()));
            if (!"0000".equals(result.get("ret_code")))
                return JsonView.JsonViewFactory.create().success(false).info("无效的银行卡").put("id", "bankid").toJson();
            // 校验-必须是借记卡
            if (result.get("card_type").equals("3"))
                return JsonView.JsonViewFactory.create().success(false).info("不支持信用卡").put("id", "bankid").toJson();
            //校验-如果该卡不在支持的银行列表中
            String bankCode = result.get("bank_code");
            List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("bank");
            for (ConstantDefine constantDefine : constantDefines) {
                if (bankCode.equals(constantDefine.getConstantValue())) {
                    bankid = constantDefine.getConstantDefineId();
                    break;
                }
            }
            if (bankid == null)
                return JsonView.JsonViewFactory.create().success(false).info("不支持" + result.get("bank_name") + "卡").put("id", "cardNo").toJson();

            // 校验-用户是否已经绑卡
            if (card != null)
                return JsonView.JsonViewFactory.create().success(false).info("您已经绑过一张尾号：" + card.getCardCodeShort() + "卡了").put("id", "bankid")
                        .toJson();
            card = createNewCard(cardNo, currentUser, userInfoExt, bankid);
        }
        // 构造请求
        ClientEnum clientEnum = ClientEnum.from(currentUser.getAppSource());
        LLPayRequest llPayRequest = rechargeOrderService.createRechargeRequest(rechargeAmount, currentUser, userInfoExt, card, clientEnum);
        return JsonView.JsonViewFactory.create().success(true).info(llPayRequest.getUrl()).put("rechargeCode", llPayRequest.getRechargeOrder().getRechargeCode()).toJson();

    }

    /**
     * 富友支付充值请求
     * 提交充值后由定时任务处理充值接口的请求
     */
    @RequestMapping(value = "/fuuiRecharge")
    @ResponseBody
    public Object fuuiRecharge(@RequestParam(defaultValue = "0") Long rechargeAmount) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        try {
            UserInfo currentUser = SecurityUtil.getCurrentUser(true);

            // 校验金额是否大于100
            if (100L > rechargeAmount) {
                result.put("error", "请输入大于100元的金额！");
                return result;
            }
            // 校验-卡
            CustomerCard card = cgBizService.findCurrentCard(currentUser.getUserId());
            if (card == null) {
                result.put("error", "您还没有绑定银行卡！");
                return result;
            }
            // 构造请求
            RechargeDataSource rechargeDataSource = cgBizService.preAppRecharge(rechargeAmount, currentUser, card);
            result.put("params", rechargeDataSource);
            result.put("success", true);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            result.put("success", false);
            result.put("error", "服务忙，请稍后再试");
        }
        return result;
    }

    /**
     * 支付前要先记录支付请求所使用的银行卡， 支付成功后标记此卡为绑定状态
     *
     * @param cardNo
     * @param currentUser
     * @param userInfoExt
     * @param bankid
     * @return
     */
    private CustomerCard createNewCard(@RequestParam(value = "cardNo") String cardNo, UserInfo currentUser, UserInfoExt userInfoExt, Long bankid) {
        CustomerCard customerCard = new CustomerCard();
        customerCard.setUserId(currentUser.getUserId());
        customerCard.setCardType(CardType.FULL_CARD.getValue());
        customerCard.setMobile(null);
        customerCard.setStatus(CustomerCardStatus.DISABLED.getValue());
        customerCard.setBelongChannel(PayConstants.CardChannel.LL.getValue());
        customerCard.setBindStatus(CustomerCardBindStatus.UNBINDING.getValue());
        customerCard.setCardCode(cardNo);
        customerCard.setCardcustomerName(userInfoExt.getRealName());
        customerCard.setUpdateTime(new Date());
        customerCard.setBankCode(bankid);
        return customerCard;
    }

    /**
     * 支付购买支付
     */
    @RequestMapping(value = "/pay")
    @ResponseBody
    public String newPay(HttpServletRequest request,
                         @RequestParam(value = "lendOrderId") Long lendOrderId,
                         @RequestParam(value = "psw") String jypassword,
                         @RequestParam(value = "cardId", required = false) String cardId,
                         @RequestParam(value = "accountPayValue", defaultValue = "0") BigDecimal accountPayValue,
                         @RequestParam(value = "rechargePayValue", defaultValue = "0") BigDecimal rechargePayValue,
                         @RequestParam(value = "userVoucherId", required = false) String voucherIds,
                         @RequestParam(value = "rateUserIds", required = false) String rateUserIds) {

        //校验-参数必填
        ValidationUtil.checkRequiredPara(new NameValue<String, Object>("lendOrderId", lendOrderId));
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        request.setAttribute("lendOrder", lendOrder);
        //校验-如果订单已经被处理过
        if (!lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue())) {
            return JsonView.JsonViewFactory.create().success(false).info("订单已处理过").put("id", "redirect")
                    .toJson();
        }
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        //校验交易密码
        if (userByUserId.getBidPass() == null)
            return JsonView.JsonViewFactory.create().success(false).info("请您输入交易密码！").put("id", "jypassword")
                    .toJson();

        if (!MD5Util.MD5Encode(jypassword, null).equals(userByUserId.getBidPass()))
            return JsonView.JsonViewFactory.create().success(false).info("交易密码错误").put("id", "jypassword")
                    .toJson();

        RateUser rateUser = null;
        RateProduct rateProduct = null;
        // 加息券校验
        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
            if (!StringUtils.isNull(rateUserIds)) {
                String[] arrayRateUserIds = rateUserIds.split(",");
                if (arrayRateUserIds.length > 1) {
                    return JsonView.JsonViewFactory.create().success(false).info("加息券不能叠加使用").toJson();
                }
                rateUser = rateUserService.findByRateUserId(Long.valueOf(arrayRateUserIds[0]));
                if (null == rateUser) {
                    request.setAttribute("errorMsg", "该加息券不存在");
                    return "/order/payError";
                }
                List<LendOrderBidDetail> details = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId(), LendOrderBidStatusEnum.WAITING_PAY);
                if (details.size() == 0) {
                    return JsonView.JsonViewFactory.create().success(false).info("该订单出现异常").toJson();
                }
                LoanApplicationListVO loanApp = loanApplicationService.getLoanApplicationVoById(details.get(0).getLoanApplicationId());
                rateProduct = rateProductService.getRateProductById(rateUser.getRateProductId());
                JsonView checkRateUser = rateUserService.checkRateUser(rateUser, rateProduct, currentUser.getUserId(), lendOrder.getTimeLimit(), loanApp.getLoanType(), lendOrder.getBuyBalance());
                if (!checkRateUser.isSuccess()) {
                    return checkRateUser.toJson();
                }

                if (!rateProduct.getUsageScenario().equals(RateProductScenarioEnum.ALL.getValue())) {
                    if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) {
                        if (!rateProduct.getUsageScenario().equals(RateProductScenarioEnum.FINANCE.getValue())) {
                            return JsonView.JsonViewFactory.create().success(false).info("该加息券不能购买理财").toJson();
                        }
                    } else if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
                        if (!rateProduct.getUsageScenario().equals(RateProductScenarioEnum.BIDDING.getValue())) {
                            return JsonView.JsonViewFactory.create().success(false).info("该加息券不能购买投标产品").toJson();
                        }
                    } else if (lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())) {
                        return JsonView.JsonViewFactory.create().success(false).info("该加息券不能债权转让产品").toJson();
                    }

                }
                if (!com.xt.cfp.core.util.StringUtils.isNull(voucherIds)) {
                    if (rateProduct.getIsOverlay().equals(RateProductIsOverlayEnum.DISABLED.getValue())) {
                        return JsonView.JsonViewFactory.create().success(false).info("该加息券不能和财富券叠加使用").toJson();
                    }
                }

            }
        }

        //todo 财富券
        List<VoucherVO> voucherVOs = new ArrayList<VoucherVO>();
        BigDecimal voucherPayValue = BigDecimal.ZERO;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)) {
            String[] voucher_Ids = voucherIds.split(",");
            //先校验财富券
            for (String voucherId : voucher_Ids) {
                VoucherVO vo = voucherService.getVoucherById(Long.valueOf(voucherId));
                voucherVOs.add(vo);
                //计算财富券总额
                voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
            }
            JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
            if (!jsonView.isSuccess()) {
                return jsonView.toJson();
            }
        }
        //校验-财富券拆分
        if (lendOrder.getBuyBalance().compareTo(voucherPayValue) < 0) {
            return JsonView.JsonViewFactory.create().success(false).info("财富券金额不能打于订单金额").put("id", "redirect")
                    .toJson();
        }

        //校验-购买金额加和是否等于订单金额
        if (lendOrder.getBuyBalance().compareTo(accountPayValue.add(rechargePayValue).add(voucherPayValue)) != 0) {
            return JsonView.JsonViewFactory.create().success(false).info("购买金额异常！").put("id", "redirect")
                    .toJson();
        }
        CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(cardId)) {
            if (card == null) {
                return JsonView.JsonViewFactory.create().success(false).info("您还没有绑定银行卡！").put("id", "bankid").toJson();
            } else {
                if (!card.getCustomerCardId().toString().equals(cardId)) {
                    return JsonView.JsonViewFactory.create().success(false).info("您绑定的银行卡信息异常！").put("id", "redirect").toJson();
                }
            }
        }

        // 购买金额是否合法
        String _result = amountValidate(lendOrder);
        if (_result != null) {
            return _result;
        }
        try {
            RechargeDataSource dataSource = new RechargeDataSource();
            dataSource.setLogin_id(userInfoExtService.getUserInfoExtById(currentUser.getUserId()).getMobileNo());
            dataSource.setAmt(rechargePayValue.multiply(new BigDecimal(100)).longValue());

            dataSource.setBack_notify_url(com.external.deposites.utils.PropertiesUtils.property("hf-config", "cg.hf.api.recharge.app_buy.personal.back_notify_url"));
            dataSource.setPage_notify_url(com.external.deposites.utils.PropertiesUtils.property("hf-config", "cg.hf.api.recharge.app.personal.page_notify_url"));
            dataSource = ihfApi.appRecharge4personal(dataSource);
            //默认微信来源，如果是APP接口来源赋相应的值。
            rechargeOrderService.createHFPayRequest(accountPayValue, rechargePayValue, lendOrder,
                    currentUser, voucherVOs, voucherPayValue, card, ClientEnum.from(currentUser.getAppSource()), rateUser, rateProduct, RechargeChannelEnum.HF_AUTHPAY, dataSource.getMchnt_txn_ssn());
            return JsonView.JsonViewFactory.create().info(lendOrderId.toString())
                    .put("dataSource", JsonUtil.toMap(dataSource))
                    .success(true).toJson();
        } catch (HfApiException e) {
            logger.error(e.getMessage(), e);
            return JsonView.JsonViewFactory.create().info(e.getMessage()).success(true).toJson();
        }

    }

    /**
     * 网页通知
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @DoNotNeedLogin
    @RequestMapping(value = "/llPayReturn")
    public String llPayReturn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 状态跳转
        String res_data = request.getParameter("res_data");
        if (null == res_data) {
            return "redirect:/person/account/overview";
        }
        JSONObject jsonobject = JSONObject.parseObject(res_data);
        String result_pay = (String) jsonobject.get("result_pay");
        String no_order = (String) jsonobject.get("no_order");
        if (org.apache.commons.lang.StringUtils.isNotEmpty(no_order)) {
            RechargeOrder rechargeOrder = rechargeOrderService.getRechargeOrderByOrderCode(no_order, false);
            if (rechargeOrder.getPayId() != null) {
                // 充值单无payid
                LendOrder lendOrder = lendOrderService.getLendOrderByPayId(rechargeOrder.getPayId(), true);
                request.setAttribute("lendOrder", lendOrder);
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(result_pay) && result_pay.equals("SUCCESS")) {
                if (rechargeOrder.getPayId() == null)
                    return getRechargePath("success");
                return getResultPath("success");
            } else {
                if (rechargeOrder.getPayId() == null)
                    return getRechargePath("false");
                return getResultPath("false");
            }
        }
        return "/error";
    }

    private String getRechargePath(String result) {
        switch (result) {
            case "success": {
                return "/order/rechargePaySuccess";
            }
            case "recharging": {
                return "/order/recharging";
            }
            case "false": {
                return "/order/rechargePayError";
            }
        }
        return null;
    }

    private String getResultPath(String result) {
        switch (result) {
            case "success": {
                return "/order/paySuccess";
            }
            case "recharging": {
                return "/order/paying";
            }
            case "false": {
                return "/order/payError";
            }
        }
        return null;
    }

}
