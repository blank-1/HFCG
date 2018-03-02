package com.xt.cfp.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.external.llpay.LLPayRequest;
import com.external.llpay.LLPayUtil;
import com.external.llpay.PayDataBean;
import com.external.llpay.RetBean;
import com.external.yeepay.PaymentForOnlineService;
import com.external.yeepay.TZTService;
import com.fuiou.data.QueryUserInfsReqData;
import com.fuiou.data.QueryUserInfsRspData;
import com.fuiou.data.QueryUserInfsRspDetailData;
import com.fuiou.data.RechargeAndWithdrawalRspData;
import com.fuiou.service.FuiouRspParseService;
import com.fuiou.service.FuiouService;
import com.fuiou.util.ConfigReader;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.CreditorErrorCode;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.PayConstants.CallBackType;
import com.xt.cfp.core.constants.RateEnum.RateProductIsOverlayEnum;
import com.xt.cfp.core.constants.RateEnum.RateProductScenarioEnum;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.pojo.ext.VoucherVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.util.*;
import com.xt.cfp.web.annotation.DoNotNeedLogin;
import jodd.util.NameValue;
import jodd.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;
import java.util.Map.Entry;

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
    private LendOrderBidDetailService lendOrderBidDetailService;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private WithDrawService withDrawService;
    @Autowired
    private UserInfoExtService userInfoExtService ;
    @Autowired
	private CreditorRightsTransferAppService creditorRightsTransferAppService;
    @Autowired
    private RateProductService rateProductService;
    @Autowired
    private RateUserService rateUserService;
    @Autowired
    private ScheduleService scheduleService;

	private Logger log = Logger.getLogger(RechargeController.class);


    /**
     * 支付金额验证
     * @param lendOrder
     * @return 返回null表示验证通过
     */
    private String amountValidate( LendOrder lendOrder){

        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())){
            //投标金额验证
            List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
            LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
            LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());

            if(currentUser!=null){
                //获取账户余额
                if(lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getRemain())>0){
                    return JsonView.JsonViewFactory.create().success(false).info("金额已超出剩余金额！").put("id","redirect")
                            .toJson();
                }
                BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
                if(lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy))>0){
                    //无法购买剩余标
                    JsonView.JsonViewFactory.create().success(false).info("金额已超出剩余金额！").put("id","redirect")
                            .toJson();
                }
                //计算账户已投金额
                BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(currentUser.getUserId(), loanApplicationListVO.getLoanApplicationId());
                if (totalLendAmountPerson != null) {
                    if(loanApplicationListVO.getMaxBuyBalance()==null){
                        loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
                    }else{
                        loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
                    }
                }
            }

            if(lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getMaxBuyBalanceNow())>0)
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
        }else{
            //省心计划金额验证
            if (!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
                throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
            LendProductPublish lendProductPublish = lendProductService.getLendProductPublishByPublishId(lendOrder.getLendProductPublishId());
            //校验金额是否合法
            BigDecimal waitingBuyBalance = lendProductPublish.getPublishBalance().subtract(lendProductPublish.getSoldBalance());
            if(LendProductPublish.PUBLISHBALANCETYPE_SPEC == lendProductPublish.getPublishBalanceType()){//如果购买的是指定金额的省心计划
            	if (lendOrder.getBuyBalance().compareTo(waitingBuyBalance)>0){
                	return JsonView.JsonViewFactory.create().success(false).info("购买金额已超出剩余可购买金额").put("id","redirect").toJson();
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
        map.put("card_last",StringUtils.getLast4CardNo(customerCard.getCardCode()));
        map.put("callbackurl", TZTService.getCallbackUrl());
        map.put("userip", SecurityUtil.getIpAddr(request));
        map.put("currency", 156 + "");
        ValidationUtil.checkNotExistNullValue(map);
        result = TZTService.directBindPay(map);
        return result;
    }

    private PayOrder createPayOrder(BigDecimal accountPayValue, BigDecimal rechargePayValue,BigDecimal voucherPayValue, UserInfo currentUser, LendOrder lendOrder) {
        PayConstants.BusTypeEnum busTypeEnum = null;
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
            busTypeEnum = PayConstants.BusTypeEnum.BUY_FINANCE;

        if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue()))
            busTypeEnum = PayConstants.BusTypeEnum.BID_LOAN;
        Pair<String, BigDecimal> accountAmount = new Pair<String, BigDecimal>(PayConstants.AmountType.ACCOUNT.getValue(), accountPayValue);
        Pair<String, BigDecimal> rechargeAmount = new Pair<String, BigDecimal>(PayConstants.AmountType.RECHARGE.getValue(), rechargePayValue);
        //todo 财富券
        Pair<String, BigDecimal> voucherAmount = new Pair<String, BigDecimal>(PayConstants.AmountType.VOUCHERS.getValue(), voucherPayValue);
        return this.payService.addPayOrderForLendOrder(lendOrder, new Date(), busTypeEnum, accountAmount, rechargeAmount,voucherAmount);
    }

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
        BigDecimal voucherPayValue=BigDecimal.ZERO;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)){
            String[] voucher_Ids = voucherIds.split(",");
            //先校验财富券
            for (String voucherId:voucher_Ids){
                VoucherVO vo = voucherService.getVoucherById(Long.valueOf(voucherId));
                voucherVOs.add(vo);
                //计算财富券总额
                voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
            }
            JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
            if (!jsonView.isSuccess()){
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
        PayOrder payOrder = createPayOrder(accountPayValue, rechargePayValue,voucherPayValue, currentUser, lendOrder);
        //支付单明细和财富券保存关系
        voucherService.linkVoucher(payOrder,voucherVOs);
        //创建充值订单
        RechargeOrder rechargeOrder = this.rechargeOrderService.addRechargeOrder(rechargePayValue, card.getCustomerCardId(), payOrder.getPayId(), currentUser.getUserId(), "YB_TZT",PropertiesUtils.getInstance().get("SOURCE_PC"));
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
            return JsonView.JsonViewFactory.create().success(false).info("验证码获取失败，"+result.get("error_msg")).put("id", "valid")
                    .toJson();


        //发起支付验证码请求
        result = TZTService.sendSmsByOrder(rechargeOrder.getRechargeCode());
        if (result.get("error_code") != null)
            return JsonView.JsonViewFactory.create().success(false).info("验证码获取失败，"+result.get("error_msg")).put("id", "valid")
                    .toJson();

        return JsonView.JsonViewFactory.create().success(true).toJson();
    }


    @RequestMapping(value = "/cbForYbNotification")
    @DoNotNeedLogin
    public String cbForYbNotification(HttpServletRequest request , HttpServletResponse response) throws Exception {
    	boolean flag = false;
        Map<String, String> parameterMap = new HashMap<String,String>();
        Map<String, String[]> parameterMapArray = request.getParameterMap();
        for(String key : parameterMapArray.keySet()){
        	if(key.equals("p1_MerId")){
        		flag = true ;
        		break;
        	}
            parameterMap.put(key,parameterMapArray.get(key)[0]);
        }
        if(flag){
        	return callbackPayment(request, response);
        }else{
        	Map<String, String> result = TZTService.parseCallbackRequest(parameterMap);
        	logger.info(LogUtils.createSimpleLog("支付通知返回", result.toString()));
        	rechargeOrderService.handleCallbackNotification(result);
        	printReturnData("SUCCESS", response);
        }
        return null;
    }





	private String callbackPayment(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, String[]> reqMap = request.getParameterMap();

			Map<String, String> param = new HashMap<String, String>();
			if (null != reqMap) {
				for (Entry<String, String[]> e : reqMap.entrySet()) {
					switch (e.getKey()) {
						case "r5_Pid":
						case "r8_MP": {
							param.put(e.getKey(),
									decode(formatString(e.getValue()[0])));
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+e.getValue()[0]);
                            break;
						}
						default: {
							param.put(e.getKey(), formatString(e.getValue()[0]));
							break;
						}
					}
				}
			}
			log.info("回调接收的返回参数：[" + JSONObject.toJSONString(param) + "]");
			boolean isOK = false;
			// 检验返回数据包
			isOK = PaymentForOnlineService.verifyCallback(
					param.get("hmac"), param.get("p1_MerId"),
					param.get("r0_Cmd"), param.get("r1_Code"),
					param.get("r2_TrxId"), param.get("r3_Amt"),
					param.get("r4_Cur"), param.get("r5_Pid"),
					param.get("r6_Order"), param.get("r7_Uid"),
					param.get("r8_MP"),  param.get("r9_BType"),
					PaymentForOnlineService.getMerchantKeyValue());
			// 测试+true
			if (isOK) {
				String type = param.get("r8_MP");
				// 在接收到支付结果通知后，判断是否进行过业务逻辑处理，不要重复进行业务逻辑处理
				// r1_Code “1” 代表支付成功 其他代表失败
				if (param.get("r1_Code").equals("1")) {

					if (param.get("r9_BType").equals("1")) {// 产品通用接口支付成功返回-浏览器重定向
						// out.println("callback方式:产品通用接口支付成功返回-浏览器重定向");
						// 接收浏览器返回后不需要做处理，业务第三方支付后台回调为准
						if("pay".equals(type)){
							return "order/paymentSuccess";
						}else if ("recharge".equals(type)){
							return "order/rechargePaySuccess";
						}
					} else if (param.get("r9_BType").equals("2")) {// 产品通用接口支付成功返回-服务器点对点通讯
						// 如果在发起交易请求时 设置使用应答机制时，必须应答以"success"开头的字符串，大小写不敏感
						// 产品通用接口支付成功返回-电话支付返回
						try{
						rechargeOrderService
								.handleCallbackPaymentOnlineNotification(param);
						}catch(Exception e){
							log.error("系统异常:" ,e);
						}
						printReturnData("SUCCESS", response);
					}
				} else {
					if (param.get("r9_BType").equals("1")) {
						if("pay".equals(type)){
							return "order/paymentError";
						}else if ("recharge".equals(type)){
							return "order/rechargePayError";
						}
					} else if (param.get("r9_BType").equals("2")) {
						// 支付结果失败，更新业务数据
						try{
							rechargeOrderService
									.handleCallbackPaymentOnlineNotification(param);
							}catch(Exception e){
								log.error("系统异常:" ,e);
							}
						printReturnData("SUCCESS", response);
					}
				}
			} else {
				// 非法交易请求，不做业务处理
				log.info("非法请求!");
				printReturnData("非法请求!", response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("网关回调异常，异常原因：", e);
		}

		return null;
	}
	
	private String formatString(String string) {
		if (string == null) {
			return string = "";
		}
		return string;
	}
	

	private String decode(String str) throws Exception {
        System.out.println("str = " + str);
        return URLDecoder.decode(str, "GBK");
	}
	
	

    /**
     * 充值列表
     * @param request
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/rechargeList")
    @ResponseBody
    public Object rechargeList(HttpServletRequest request,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo){
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        //封装参数
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setUserId(currentUser.getUserId());
        Pagination<RechargeOrder> rechargeOrderPaging = rechargeOrderService.getRechargeOrderPaging(pageNo, pageSize, rechargeOrder, null);
        return rechargeOrderPaging;
    }



    @RequestMapping(value = "/confirmRechargeValidate")
    @ResponseBody
    public String validate(@RequestParam(value = "valid", required = false) String validCode,HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "password") String jypassword){

        //是否点击验证码
        String smsCode = (String)session.getAttribute("recharge_smsCode");
        if(org.apache.commons.lang.StringUtils.isEmpty(smsCode)) {
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
        String rechargeOrderCode = (String)session.getAttribute("rechargeOrderCode_income");
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
     * @param validCode
     * @param lendOrderId
     * @param jypassword
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/confirmPayValidate")
    @ResponseBody
    public String confirmPayValidate(@RequestParam(value = "valid", required = false) String validCode,
                                     @RequestParam(value = "lendOrderId", required = false) Long lendOrderId,
                                     @RequestParam(value = "password") String jypassword,
                                     HttpServletRequest request,HttpSession session){
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
        String smsCode = (String)session.getAttribute("smsCode");
        if(org.apache.commons.lang.StringUtils.isEmpty(smsCode)) {
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
        String rechargeOrderCode = (String)session.getAttribute("rechargeOrderCode");
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
        if(_result!=null)
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
            if (result.get("error_code").equals("600116")){
                //验证码错误导致
                String tokenString = TokenUtils.setNewToken(request);
                session.setAttribute("smsCode","0");
                return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "valid").put("tocken",tokenString)
                        .toJson();
            }
            session.removeAttribute("smsCode");
            return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "redirect")
                    .toJson();
        }


        session.removeAttribute("smsCode");
        return JsonView.JsonViewFactory.create().success(true)
                .toJson();
    }

    /**
     * 有卡支付 充值
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
            if (result.get("error_code").equals("600116")){
                session.setAttribute("recharge_smsCode","0");
                String tokenString = TokenUtils.setNewToken(request);
                return JsonView.JsonViewFactory.create().success(false).info(result.get("error_msg")).put("id", "valid").put("tocken",tokenString)
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
     * @param session
     * @return
     */
    @RequestMapping(value = "/confirmRecharge")
    @ResponseBody
    public String confirmRecharge(HttpSession session) {
        final String rechargeOrderCode = (String)session.getAttribute("rechargeOrderCode");
        //模拟回调
    /*    session.removeAttribute("rechargeOrderCode");
        if (org.apache.commons.lang.StringUtils.isNotEmpty(rechargeOrderCode)){
            Map<String, String> result = new HashMap<String,String>();
            result.put("orderid",rechargeOrderCode);
            result.put("yborderid","00000000000000000001");
            result.put("status","1");
            rechargeOrderService.handleCallbackNotification(result);
        }*/

        RechargeOrder rechargeOrder = rechargeOrderService.getRechargeOrderByOrderCode(rechargeOrderCode, false);
        String status = rechargeOrder.getStatus();
        if(status.equals(RechargeStatus.FAILE.getValue()))
            return "false";
        if(status.equals(RechargeStatus.UN_RECHARGE.getValue()))
            return "recharging";
        if(status.equals(RechargeStatus.SUCCESS.getValue()))
            return "success";
        return null;
    }

    /**
     * 充值支付确认
     * @param session
     * @return
     */
    @RequestMapping(value = "/confirmRechargeIncome")
    @ResponseBody
    public String confirmRechargeIncome(HttpSession session) {
        String rechargeOrderCode = (String)session.getAttribute("rechargeOrderCode_income");
        RechargeOrder rechargeOrder = rechargeOrderService.getRechargeOrderByOrderCode(rechargeOrderCode, false);
        String status = rechargeOrder.getStatus();
        if(status.equals(RechargeStatus.FAILE.getValue()))
            return "false";
        if(status.equals(RechargeStatus.UN_RECHARGE.getValue()))
            return "recharging";
        if(status.equals(RechargeStatus.SUCCESS.getValue()))
            return "success";
        return null;
    }




    /**
     * 处理成功页面内
     * @return
     */
    @RequestMapping(value = "/rechargeSuccess")
    public String rechargeSuccess(){
        return "/order/rechargePaySuccess";
    }

    /**
     * 处理失败页面
     * @param request
     * @param errorMsg
     * @return
     */
    @RequestMapping(value = "/rechargeFailure")
    public String rechargeFailure(HttpServletRequest request,String errorMsg){
        request.setAttribute("errorMsg", errorMsg);
        return "/order/rechargePayError";
    }

    /**
     * 处理中页面
     * @return
     */
    @RequestMapping(value = "/recharging")
    public String recharging(){
        return "/order/recharging";
    }


    /**
     * 处理成功页面内
     * @return
     */
    @RequestMapping(value = "/paySuccess")
    public String paySuccess(HttpServletRequest request,Long lendOrderId){
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        request.setAttribute("lendOrder", lendOrder);
        if (!lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.PAID.getValue())){
            request.setAttribute("errorMsg", "订单支付失败");
            request.setAttribute("rechargeMessage", "1");
            return "/order/rechargePayError";
        }
        return "/order/paySuccess";
    }

    /**
     * 处理失败页面
     * @param request
     * @param errorMsg
     * @return
     */
    @RequestMapping(value = "/payError")
    public String payError(HttpServletRequest request,String errorMsg,Long lendOrderId){
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        request.setAttribute("errorMsg", errorMsg);
        request.setAttribute("lendOrder", lendOrder);
        return "/order/payError";
    }

    /**
     * 处理中页面
     * @return
     */
    @RequestMapping(value = "/paying")
    public String paying(HttpServletRequest request,Long lendOrderId){
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        request.setAttribute("lendOrder", lendOrder);
        return "/order/paying";
    }
    
    
	/**
	 * 富友pos支付回调通知接口
	 * 
	 * @return xml
	 * */
	@DoNotNeedLogin
	@RequestMapping(value = "/rechargePOSNotice" )
	public void cbForFYNotification(HttpServletRequest request,
			HttpServletResponse response) {
		String mchnt_txn_ssn = request.getParameter("mchnt_txn_ssn");
		boolean lock = true;
		if(StringUtil.isNotBlank(mchnt_txn_ssn)){
			mchnt_txn_ssn = mchnt_txn_ssn.trim();
		}
		String mobile_no = request.getParameter("mobile_no");
		if(StringUtil.isNotBlank(mobile_no)){
			mobile_no = mobile_no.trim();
		}
		String amount =  request.getParameter("amt");
		if(StringUtil.isNotBlank(amount)){
			amount = amount.trim();
		}
		try {
			String resp_code = "9999";
			lock = rechargeOrderService.isPOSssnLock(mchnt_txn_ssn);
			if(lock){
				printReturnData(getPOSxml(resp_code, mchnt_txn_ssn), response);
				return ;
			}
			// 响应状态码
			// 接收富友回调验签和数据解析类:
			RechargeAndWithdrawalRspData notice = FuiouRspParseService
					.rechargeAndWithdrawalNotifyParse(request);

			QueryUserInfsReqData user = new QueryUserInfsReqData();
			log.info("开始请求富友用户查询接口......");
			long st = System.currentTimeMillis();
			QueryUserInfsRspData userResult = null;
			try {
				user.setMchnt_cd(notice.getMchnt_cd());
				user.setMchnt_txn_dt(DateUtil.getNowShortDate());
				user.setMchnt_txn_ssn(notice.getMchnt_txn_ssn());
				user.setUser_ids(notice.getMobile_no());
				userResult = FuiouService.queryUserInfs(user);
			} catch (Exception e1) {
				log.error("富友用户查询异常，异常原因：", e1);
			} finally {
				log.info("查询用户接口结束，耗时：[ " + (System.currentTimeMillis() - st)
						+ " ms]");
			}
			st = System.currentTimeMillis();
			if (userResult != null) {
				Map<String, Object> rsMap = validateUserInfo(userResult, notice);
				if ((boolean) rsMap.get("flag")) {
					// 核对信息成功，开始进行充值业务
					Map<String,String> rechargeMap = rechargeOrderService
							.handlePOSRechageNotification(
									userResult.getMchnt_txn_ssn(),
									notice.getAmt(),
									Long.valueOf(rsMap.get("userId").toString()),
									PropertiesUtils.getInstance().get(
											"SOURCE_PC"));
					if (rechargeMap.get("code").equals("0001") ) {
						log.info("该流水号已经成功充值过，充值单号：" + rechargeMap.get("rechargeCode") + "，无需再充值，系统将返回成功码。富友流水号："
								+ notice.getMchnt_txn_ssn() + "，手机号： "
								+ notice.getMobile_no() + "，充值金额："
								+ notice.getAmt() + "分，充值耗时：[ "
								+ (System.currentTimeMillis() - st) + "ms]");
					} else  if(rechargeMap.get("code").equals("0002")){
						log.info("线下POS刷卡充值失败！原因是已经通过人工处理充值成功，充值单号：" + rechargeMap.get("rechargeCode") + ",富友流水号："
								+ notice.getMchnt_txn_ssn() + "，手机号： "
								+ notice.getMobile_no() + "，充值金额："
								+ notice.getAmt() + "分，充值耗时：[ "
								+ (System.currentTimeMillis() - st) + "ms]");
					} else if(rechargeMap.get("code").equals("0000")) {
						if(rechargeMap.get("rechargeResult").equals(RechargeStatus.SUCCESS.getValue())){
							log.info("线下POS刷卡充值成功！充值单号：" + rechargeMap.get("rechargeCode") + ",富友流水号："
									+ notice.getMchnt_txn_ssn() + "，手机号： "
									+ notice.getMobile_no() + "，充值金额："
									+ notice.getAmt() + "分，充值耗时：[ "
									+ (System.currentTimeMillis() - st) + "ms]");
						}else{
							log.info("线下POS刷卡充值失败！充值单号：" + rechargeMap.get("rechargeCode") + ",富友流水号："
									+ notice.getMchnt_txn_ssn() + "，手机号： "
									+ notice.getMobile_no() + "，充值金额："
									+ notice.getAmt() + "分，充值耗时：[ "
									+ (System.currentTimeMillis() - st) + "ms]");
						}
					}
					resp_code = "0000";

				}
			}else{
				throw new Exception("富友用户信息查询失败！");
			}

			// 接口收到回调后返回XML组装方法:
			lock = false;
			printReturnData(getPOSxml(resp_code, mchnt_txn_ssn), response);
		} catch (Exception e) {
			log.info("富友通知充值失败，富友流水号："
								+ mchnt_txn_ssn + "，手机号： "
								+ mobile_no + "，充值金额："
								+ amount +"分，失败原因：", e);
			lock = false;
			printReturnData(getPOSxml("9999", mchnt_txn_ssn), response);
		}finally{
			if(!lock)
				rechargeOrderService.releasePOSssnLock(mchnt_txn_ssn);
		}

	}
	
	private String getPOSxml(String resp_code ,String mchnt_txn_ssn){
		String respXML="";
		try {
			respXML = FuiouRspParseService.notifyRspXml(resp_code,
					ConfigReader.getConfig("fuiou_mchnt_cd"),
					mchnt_txn_ssn);
		} catch (Exception e1) {
			log.info("富友转换XML文件异常，异常原因：", e1);
		}
		return respXML;
	}

	// 校验富友用户信息与财富派系统用户信息是否一致
	private Map<String, Object> validateUserInfo(
			QueryUserInfsRspData userResult, RechargeAndWithdrawalRspData notice) {
		Map<String, Object> rsMap = new HashMap<String, Object>();
		rsMap.put("flag", false);
		if (userResult.getResp_code().equals("0000")) {
			List<QueryUserInfsRspDetailData> userList = userResult.getResults();
			if (userList != null && userList.size() == 0) {
				log.info("POS卡充值，富友用户不存在！富友流水号：" + notice.getMchnt_txn_ssn()
						+ "，手机号： " + notice.getMobile_no() + "，充值金额："
						+ notice.getAmt() + "分");
			} else if (userList.size() > 1) {
				log.info("POS卡充值，富友用户不唯一！富友流水号：" + notice.getMchnt_txn_ssn()	
						+ "，手机号： " + notice.getMobile_no() + "，充值金额："
						+ notice.getAmt() + "分");
			} else if (userList.size() == 1) {
				QueryUserInfsRspDetailData userInfo = userList.get(0);
				if(!StringUtils.isNull(userInfo.getCertif_id())){
					List<UserInfoExt> detailList = userInfoExtService
							.getCommonUserExtByIdCard(userInfo.getCertif_id());
					if (detailList == null || detailList.size() == 0) {
						log.info("POS卡充值，本地用户不存在！" + " 富友流水号："
								+ notice.getMchnt_txn_ssn() + "，手机号： "
								+ notice.getMobile_no() + "，充值金额："
								+ notice.getAmt()+ "分");
					} else if (detailList.size() > 1) {
						log.info("POS卡充值，本地用户不唯一！" + " 富友流水号："
								+ notice.getMchnt_txn_ssn() + "，手机号： "
								+ notice.getMobile_no() + "，充值金额："
								+ notice.getAmt()+ "分");
					} else if (detailList.size() == 1) {
						UserInfoExt detail = detailList.get(0);
						//目前只以身份证做校验，未认证的不充值
						rsMap.put("flag", true);
						rsMap.put("userId", detail.getUserId());
					}
				}else{
					log.info("POS卡充值，富友用户没有身份证号码！" + " 富友流水号："
								+ notice.getMchnt_txn_ssn() + "，手机号： "
								+ notice.getMobile_no() + "，充值金额："
								+ notice.getAmt()+ "分");
				}
			}
		}else{
			log.info("POS卡充值，富友用户返回错误！错误号：" + userResult.getResp_code()  + " 富友流水号："
					+ notice.getMchnt_txn_ssn() + "，手机号： "
					+ notice.getMobile_no() + "，充值金额："
					+ notice.getAmt()+ "分");
		}
		return rsMap;
	}

    /**
     * 连连支付购买支付
     * @param request
     * @param lendOrderId
     * @param jypassword
     * @param cardNo
     * @param accountPayValue
     * @param rechargePayValue
     * @param voucherIds
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/llpay")
    @ResponseBody
    public String newPay(HttpServletRequest request,
                         @RequestParam(value = "lendOrderId") Long lendOrderId,
                         @RequestParam(value = "password") String jypassword,
                         @RequestParam(value = "cardNo" , required = false) String cardNo,
                         @RequestParam(value = "cardId" , required = false) String cardId,
                         @RequestParam(value = "accountPayValue", defaultValue = "0") BigDecimal accountPayValue,
                         @RequestParam(value = "rechargePayValue", defaultValue = "0") BigDecimal rechargePayValue,
                         @RequestParam(value = "userVoucherId", required = false) String voucherIds,
                         @RequestParam(value = "rateUserIds", required = false) String rateUserIds) {

		// 校验-参数必填
		ValidationUtil.checkRequiredPara(new NameValue<String, Object>("lendOrderId", lendOrderId));
		LendOrder lendOrder = lendOrderService.findById(lendOrderId);
		request.setAttribute("lendOrder", lendOrder);
		// 校验-如果订单已经被处理过
		if (!lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue())) {
			return JsonView.JsonViewFactory.create().success(false).info("订单已处理过").put("id", "redirect").toJson();
		}
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
		UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(userByUserId.getUserId());
		// 校验交易密码
		if (userByUserId.getBidPass() == null)
			return JsonView.JsonViewFactory.create().success(false).info("请您输入交易密码！").put("id", "jypassword").toJson();

		if (!MD5Util.MD5Encode(jypassword, null).equals(userByUserId.getBidPass()))
			return JsonView.JsonViewFactory.create().success(false).info("交易密码错误").put("id", "jypassword").toJson();

		
		//定向标验证
		Long applocationId = (Long) request.getSession().getAttribute("applicationId");
		String Spath =chioceWhichReturnBySpecialLoanApplication(currentUser.getUserId(),applocationId,null,request);
		if(!"".equals(Spath))
			return JsonView.JsonViewFactory.create().success(false).info(UserErrorCode.HAS_NOT_IDVERIFIED.getDesc()).toJson();

		// 加息券校验
		RateUser rateUser = null;
		RateProduct rateProduct = null;
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
							return JsonView.JsonViewFactory.create().success(false).info("该加息券不能购买省心计划").toJson();
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
		
		// todo 财富券
		List<VoucherVO> voucherVOs = new ArrayList<VoucherVO>();
		BigDecimal voucherPayValue = BigDecimal.ZERO;
		if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)) {
			String[] voucher_Ids = voucherIds.split(",");
			// 先校验财富券
			for (String voucherId : voucher_Ids) {
				VoucherVO vo = voucherService.getVoucherById(Long.valueOf(voucherId));
				voucherVOs.add(vo);
				// 计算财富券总额
				voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
			}
			JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
			if (!jsonView.isSuccess()) {
				return jsonView.toJson();
			}
		}

		// 校验-财富券拆分
		if (lendOrder.getBuyBalance().compareTo(voucherPayValue) < 0) {
			return JsonView.JsonViewFactory.create().success(false).info("财富券金额不能大于订单金额").put("id", "redirect").toJson();
		}

		// 校验-购买金额加和是否等于订单金额
		if (lendOrder.getBuyBalance().compareTo(accountPayValue.add(rechargePayValue).add(voucherPayValue)) != 0) {
			return JsonView.JsonViewFactory.create().success(false).info("购买金额异常！").put("id", "redirect").toJson();
		}

		CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
		Long bankid = null;
		if (org.apache.commons.lang.StringUtils.isNotEmpty(cardId)) {
			// 有卡
			if (card == null)
				return JsonView.JsonViewFactory.create().success(false).info("您还没有绑定银行卡！").put("id", "bankid").toJson();
			if (!card.getCustomerCardId().toString().equals(cardId))
				return JsonView.JsonViewFactory.create().success(false).info("您绑定的银行卡信息异常！").put("id", "redirect").toJson();
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
			// 校验-如果该卡不在支持的银行列表中
			String bankCode = result.get("bank_code");

			List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("bank");
			for (ConstantDefine constantDefine : constantDefines) {
				if (bankCode.equals(constantDefine.getConstantValue())) {
					bankid = constantDefine.getConstantDefineId();
					break;
				}
			}
			if (bankid == null)
				return JsonView.JsonViewFactory.create().success(false).info("不支持该银行卡").put("id", "bankid").toJson();

			// 校验-用户是否已经绑卡
			if (card != null)
				return JsonView.JsonViewFactory.create().success(false).info("您已经绑过一张尾号：" + card.getCardCodeShort() + "卡了").put("id", "bankid").toJson();

			card = createNewCard(cardNo, currentUser, userInfoExt, bankid);
		}

		// todo 连连支付可以做当日限额和当月限额校验
		// 购买金额是否合法
		String _result = amountValidate(lendOrder);
		if (_result != null)
			return _result;

		LLPayRequest llPayRequest = rechargeOrderService.createPayRequest(accountPayValue, rechargePayValue, lendOrder, currentUser, userInfoExt, voucherVOs, voucherPayValue, card, ClientEnum.WEB_CLIENT,rateUser,rateProduct);

		return JsonView.JsonViewFactory.create().success(true).info(llPayRequest.getUrl()).put("rechargeCode", llPayRequest.getRechargeOrder().getRechargeCode()).toJson();

	}

    /**
     * 支付前要先记录支付请求所使用的银行卡，
     * 支付成功后标记此卡为绑定状态
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
     * 订单查询请求
     *
     * */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getOrderResult")
    public String getOrderResult(HttpServletRequest request,String rechargeCode,
                                 @RequestParam(value = "lendOrderId") Long lendOrderId) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        ValidationUtil.checkRequiredPara(new NameValue<String, Object>(
                "lendOrderId", lendOrderId));
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        request.setAttribute("lendOrder", lendOrder);
        RechargeOrder ro = rechargeOrderService.getRechargeOrderByOrderCode(rechargeCode, false);

        PayOrder payOrder = payService.getPayOrderById(ro.getPayId(), true);
        // 校验-订单用户和当前用户必须相同
        if (ro == null || !ro.getUserId().equals(currentUser.getUserId())) {
            request.setAttribute("errorMsg", "您支付的订单不属于您");
            return getResultPath("false");
        }
        // 未支付
        if (RechargeStatus.UN_RECHARGE.getValue().equals(ro.getStatus())) {

            if (PayConstants.OrderStatus.FAIL.getValue().equals(payOrder.getStatus())){
                logger.info("恒丰支付支付单失败");
                return getResultPath("false");
            }

            try{

                Map<String, String> params = new HashMap<String,String>();
                params.put("businessType",AccountConstants.BusinessTypeEnum.FEESTYPE_RECHARGE.getValue());
                params.put("businessId",ro.getRechargeId().toString());
                List<Schedule> schedules = scheduleService.findByCondition(params);
                if(null == schedules || schedules.size()==0){
                    logger.info("查询恒丰订单失败，失败原因:支付订单异常，未生成对应单号");
                    request.setAttribute("errorMsg", "请重新支付");
                    return getResultPath("false");
                }
                Schedule sch=schedules.get(0);
                if(sch.getStatus().toString().equals(ScheduleEnum.BUSINESS_PREPARE.getValue())){
                    return getResultPath("recharging");
                }
                if(sch.getStatus().toString().equals(ScheduleEnum.BUSINESS_SUCCESS.getValue())){
                    return getResultPath("success");
                }

            }catch(Exception e){
                log.error("查询恒丰订口失败，失败原因：",e);
                request.setAttribute("errorMsg", "网络异常，请重新查看订单");
                return getResultPath("false");
            }

        } else if (RechargeStatus.FAILE.getValue().equals(ro.getStatus())) {// 支付失败
            request.setAttribute("errorMsg", RechargeStatus.FAILE.getDesc());
            return getResultPath("false");
        } else if (RechargeStatus.SUCCESS.getValue().equals(ro.getStatus())) {
            Map<String,Object> rMap = getPayOrderResult(ro.getPayId(), lendOrder.getOrderState());
            request.setAttribute("errorMsg", rMap.get("errorMsg"));
            request.setAttribute("ordercode",rMap.get("ordercode"));
            return getResultPath((String)rMap.get("flag"));
        }
        return null;
    }

    private Map<String, Object> getPayOrderResult(Long payId,
                                                  String lendOrderStatus) {
        PayOrder payOrder = payService.getPayOrderById(payId, true);
        Map<String, Object> map = new HashMap<String, Object>();
        if (PayConstants.OrderStatus.SUCCESS.getValue().equals(
                payOrder.getStatus())) {
            map.put("flag", "success");
        } else {
            if (!StringUtils.isNull(lendOrderStatus)
                    && (lendOrderStatus
                    .equals(LendOrderConstants.LoanOrderStatusEnum.UNPAY
                            .getValue()) || lendOrderStatus
                    .equals(LendOrderConstants.LoanOrderStatusEnum.OUT_TIME
                            .getValue()))) {
                map.put("errorMsg", "订单支付失败");
            } else if (!StringUtils.isNull(lendOrderStatus)) {
                map.put("ordercode", "500");
                map.put("errorMsg", "订单已处理过");
            } else {
                map.put("flag", "success");
                return map;
            }
            map.put("flag", "false");
        }
        return map;
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

    /**
     * 网页通知
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/llPayReturn")
    @DoNotNeedLogin
    public String llPayReturn(HttpServletRequest request , HttpServletResponse response) throws Exception {
        //状态跳转
        String result_pay = (String)request.getParameter("result_pay");
        String no_order = (String)request.getParameter("no_order");

        if (org.apache.commons.lang.StringUtils.isNotEmpty(no_order)){
            RechargeOrder rechargeOrder = rechargeOrderService.getRechargeOrderByOrderCode(no_order, false);
            if(rechargeOrder.getPayId()!=null){
                //充值单无payid
                LendOrder lendOrder = lendOrderService.getLendOrderByPayId(rechargeOrder.getPayId(), true);
                request.setAttribute("lendOrder", lendOrder);
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(result_pay)&&result_pay.equals("SUCCESS")){
                if (rechargeOrder.getPayId()==null)
                    return getRechargePath("success");
                return getResultPath("success");
            }else{
                if (rechargeOrder.getPayId()==null)
                    return getRechargePath("false");
                return getResultPath("false");
            }
        }
        return "/error";
    }


    /**
     * 连连支付回调
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/cbForLLNotification")
    @DoNotNeedLogin
    @ResponseBody
    public void cbForLLNotification(HttpServletRequest request , HttpServletResponse response) throws Exception {

        //解析回调参数
        PayDataBean payDataBean = LLPayUtil.notifyOrganizetion(request,response,CallBackType.PAY.getValue());
        if(payDataBean.getPay_type().equals(LLPayTypeEnum.LLPAY_AUTH.getValue())){
        	//认证支付--绑卡
        	customerCardService.bindCustomCard(payDataBean);
        }
        //支付成功后续处理
        Map<String, String> handMap = new HashMap<String,String>();
        handMap.put("orderid", payDataBean.getNo_order());
        handMap.put("yborderid", payDataBean.getOid_paybill());
        handMap.put("status", "1");//标记支付成功
        try{
            rechargeOrderService.handleCallbackNotification(handMap);
        }catch (Exception e){
            log.info(e.getMessage());
        }
        //通知连连支付处理成功
        RetBean retBean = new RetBean();
        retBean.setRet_code("0000");
        retBean.setRet_msg("交易成功");
        response.getWriter().write(JSON.toJSONString(retBean));
        response.getWriter().flush();
        log.info("支付异步通知数据接收处理成功");

    }

    /**
     * 连连提现回调
     * @param request
     * @param response
     * @throws Exception 
     * 
     */
    @RequestMapping(value = "/cbForLLWithDraw")
    @DoNotNeedLogin
    @ResponseBody
    public void cbForLLWithDraw(HttpServletRequest request , HttpServletResponse response) throws Exception {
    	//解析回调参数
        PayDataBean payDataBean = LLPayUtil.notifyOrganizetion(request,response, CallBackType.WITH_DRAW.getValue());
        //支付成功后续处理
        Map<String, String> handMap = new HashMap<String,String>();
        handMap.put("orderid", payDataBean.getNo_order());
        if("WAITING".equals(payDataBean.getResult_pay()) || "PROCESSING".equals(payDataBean.getResult_pay()))
        	handMap.put("status", "DOING");
        else
        	handMap.put("status", payDataBean.getResult_pay());
        withDrawService.withDrawCallback(handMap);
        //通知连连支付处理成功
        RetBean retBean = new RetBean();
        retBean.setRet_code("0000");
        retBean.setRet_msg("交易成功");
        response.getWriter().write(JSON.toJSONString(retBean));
        response.getWriter().flush();
        log.info("代付异步通知数据接收处理成功");
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
    public String llRecharge(HttpServletRequest request, HttpSession session,
            @RequestParam(value = "cardNo",required = false) String cardNo,
            @RequestParam(value = "cardId",required = false) String cardId,
            @RequestParam(value = "rechargeAmount", defaultValue = "0") BigDecimal rechargeAmount) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
//        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(currentUser.getUserId());
        //校验-银行卡号是否为空
        if (org.apache.commons.lang.StringUtils.isEmpty(cardNo))
            return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空！").put("id", "bankid")
                    .toJson();
        //校验-购买金额是否大于50
        if (new BigDecimal("100").compareTo(rechargeAmount) > 0)
            return JsonView.JsonViewFactory.create().success(false).info("请输入大于100元的金额！").put("id", "moneyp")
                    .toJson();

        //校验-卡
        CustomerCard card = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
        Long bankid = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(cardId)){
            //有卡
            if (card == null)
                return JsonView.JsonViewFactory.create().success(false).info( "您还没有绑定银行卡！").put("id", "bankid")
                        .toJson();
            if (!card.getCustomerCardId().toString().equals(cardId))
                return JsonView.JsonViewFactory.create().success(false).info( "您绑定的银行卡信息异常！").put("id", "redirect")
                        .toJson();
        }else{
            //无卡
            //校验-银行卡号是否为空
            if (org.apache.commons.lang.StringUtils.isEmpty(cardNo)){
                return JsonView.JsonViewFactory.create().success(false).info("银行卡号为空！").put("id", "bankid")
                        .toJson();
            }
            //连连cardbin查询
            //校验-该卡是否有效
            cardNo = cardNo.replace(" ","");
            Map<String, String> result = LLPayUtil.bankCardCheck(cardNo);
            logger.info(LogUtils.createSimpleLog("银行卡信息查询接口返回", result.toString()));
            if (!"0000".equals(result.get("ret_code")))
                return JsonView.JsonViewFactory.create().success(false).info("无效的银行卡").put("id", "bankid")
                        .toJson();
            //校验-必须是借记卡
            if (result.get("card_type").equals("3"))
                return JsonView.JsonViewFactory.create().success(false).info("不支持信用卡").put("id", "bankid")
                        .toJson();
            //校验-如果该卡不在支持的银行列表中
            String bankCode = result.get("bank_code");

            List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("bank");
            for (ConstantDefine constantDefine : constantDefines) {
                if(bankCode.equals(constantDefine.getConstantValue())){
                    bankid = constantDefine.getConstantDefineId();
                    break;
                }
            }
            if (bankid == null)
                return JsonView.JsonViewFactory.create().success(false).info("不支持该银行卡").put("id", "bankid")
                        .toJson();

            //校验-用户是否已经绑卡
            if (card != null)
                return JsonView.JsonViewFactory.create().success(false).info( "您已经绑过一张尾号：" + card.getCardCodeShort() + "卡了").put("id", "bankid")
                        .toJson();
            card = createNewCard(cardNo, currentUser, userInfoExt, bankid);
        }
		// 构造请求
		LLPayRequest llPayRequest = rechargeOrderService.createRechargeRequest(rechargeAmount, currentUser, userInfoExt, card, ClientEnum.WEB_CLIENT);
		return JsonView.JsonViewFactory.create().success(true).info(llPayRequest.getUrl()).put("rechargeCode", llPayRequest.getRechargeOrder().getRechargeCode()).toJson();

    }
    /**
     * 连连网关支付充值请求
     *
     * @param rechargeAmount
     * @return
     */
    @RequestMapping(value = "/llGatewayRecharge")
    @ResponseBody
    public String llGatewayRecharge(
    		@RequestParam(value = "rechargeAmount", defaultValue = "0") BigDecimal rechargeAmount,
    		@RequestParam(value = "bkcode") String bankCode) {
    	UserInfo currentUser = SecurityUtil.getCurrentUser(true);
    	UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(currentUser.getUserId());
    	//校验-购买金额是否大于50
    	if (new BigDecimal("100").compareTo(rechargeAmount) > 0)
    		return JsonView.JsonViewFactory.create().success(false).info("请输入大于100元的金额！").put("id", "moneyp")
    				.toJson();
    	
    	Long bankid = null;
		List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByTypeNeedSort("gateway_bank");
		for (ConstantDefine constantDefine : constantDefines) {
			if(bankCode.equals(constantDefine.getConstantValue())){
				bankid = constantDefine.getConstantDefineId();
				break;
			}
		}
		if (bankid == null)
			return JsonView.JsonViewFactory.create().success(false).info("不支持该银行卡").put("id", "bankid")
					.toJson();
    		
    	// 构造请求
    	LLPayRequest llPayRequest = rechargeOrderService.createGatewayRechargeRequest(rechargeAmount,bankCode, currentUser, userInfoExt, ClientEnum.WEB_CLIENT);
    	return JsonView.JsonViewFactory.create().success(true).info(llPayRequest.getUrl()).put("rechargeCode", llPayRequest.getRechargeOrder().getRechargeCode()).toJson();
    	
    }


    /**
     * 充值单查询请求
     *
     * */
    @RequestMapping(value = "/getRechargeResult")
    public String getRechargeResult(HttpServletRequest request,	String rechargeCode) {
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        RechargeOrder ro = rechargeOrderService.getRechargeOrderByOrderCode(rechargeCode, false);
        // 校验-订单用户和当前用户必须相同
        if (ro == null || !ro.getUserId().equals(currentUser.getUserId())) {
            request.setAttribute("errorMsg", "您的充值订单不属于您");
            return getRechargePath("false");
        }
        // 未支付
        if (RechargeStatus.UN_RECHARGE.getValue().equals(ro.getStatus())) {
            Map<String, String>  result = null ;
            try{
                Map<String, String> params = new HashMap<String,String>();
                params.put("no_order",rechargeCode);
                params.put("dt_order",LLPayUtil.getDateTimeStr(ro.getCreateTime()));
                result = LLPayUtil.orderQuery(params);

                if("0000".equals(result.get("ret_code"))){
                    Map<String, String> handMap = new HashMap<String,String>();
                    handMap.put("orderid", ro.getRechargeCode());
                    handMap.put("yborderid", result.get("oid_paybill"));
                    if("SUCCESS".equals(result.get("result_pay"))){
                        PayDataBean payDataBean = new PayDataBean();
                        payDataBean.setNo_order(ro.getRechargeCode());
                        if(result.get("pay_type").equals(LLPayTypeEnum.LLPAY_AUTH.getValue())){
                        	//认证支付--绑卡
                        	customerCardService.bindCustomCard(payDataBean);
                        	logger.info("绑卡流程结束......");
                        }
                        handMap.put("status", "1");//标记支付成功
                        rechargeOrderService.handleCallbackNotification(handMap);
                        logger.info("对账执行完成，充值结果成功！");

                        return getRechargePath("success");

                    }else if("FAILURE".equals(result.get("result_pay"))){
                        handMap.put("status", "0");
                        rechargeOrderService.handleCallbackNotification(handMap);
                        logger.info("对账执行完成，充值结果失败！");
                        request.setAttribute("errorMsg", RechargeStatus.FAILE.getDesc());
                        return getRechargePath("false");
                    }else{
                        logger.info("连连支付订单状态等待处理中......");
                        return getRechargePath("recharging");
                    }
                }else if("8901".equals(result.get("ret_code"))){
                    return getRechargePath("recharging");
                }else{
                    logger.info("查询连连支付订单响应失败，失败原因:"+ result.get("ret_message"));
                    request.setAttribute("errorMsg", "网络异常，请重新查看订单");
                    return getRechargePath("false");
                }

            }catch(Exception e){
                log.error("查询连连订单接口失败，失败原因：",e);
                request.setAttribute("errorMsg", "网络异常，请重新查看订单");
                return getRechargePath("false");
            }

        } else if (RechargeStatus.FAILE.getValue().equals(ro.getStatus())) {// 支付失败
            request.setAttribute("errorMsg", RechargeStatus.FAILE.getDesc());
            return getResultPath("false");
        } else if (RechargeStatus.SUCCESS.getValue().equals(ro.getStatus())) {
            return getRechargePath("success");
        }
        return null;
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
    
    /**
     * 连连支付购买支付
     * @param request
     * @param lendOrderId
     * @param jypassword
     * @param accountPayValue
     * @param rechargePayValue
     * @param voucherIds
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/llGatewayPay")
    @ResponseBody
    public String llGateWayPay(HttpServletRequest request,
			@RequestParam(value = "bkcode") String bankCode,
			@RequestParam(value = "lendOrderId") Long lendOrderId,
			@RequestParam(value = "password") String jypassword,
			@RequestParam(value = "accountPayValue", defaultValue = "0") BigDecimal accountPayValue,
			@RequestParam(value = "rechargePayValue", defaultValue = "0") BigDecimal rechargePayValue,
			@RequestParam(value = "userVoucherId", required = false) String voucherIds,
			@RequestParam(value = "rateUserIds", required = false) String rateUserIds){

        //校验-参数必填
        ValidationUtil.checkRequiredPara(new NameValue<String, Object>("lendOrderId", lendOrderId));
        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
        request.setAttribute("lendOrder", lendOrder);
        //校验-如果订单已经被处理过
		if (!lendOrder.getOrderState().equals(LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue())) {
			return JsonView.JsonViewFactory.create().success(false).info("订单已处理过").put("id", "redirect").toJson();
		}
        UserInfo currentUser = SecurityUtil.getCurrentUser(true);
        UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(userByUserId.getUserId());
        //校验交易密码
		if (userByUserId.getBidPass() == null)
			return JsonView.JsonViewFactory.create().success(false).info("请您输入交易密码！").put("id", "jypassword").toJson();

		if (!MD5Util.MD5Encode(jypassword, null).equals(userByUserId.getBidPass()))
			return JsonView.JsonViewFactory.create().success(false).info("交易密码错误").put("id", "jypassword").toJson();
		//定向标验证
		Long applocationId = (Long) request.getSession().getAttribute("applicationId");
		String Spath =chioceWhichReturnBySpecialLoanApplication(currentUser.getUserId(),applocationId,null,request);
		if(!"".equals(Spath))
			return Spath;
		
		// 加息券校验
		RateUser rateUser = null;
		RateProduct rateProduct = null;
		if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) {
			if (!StringUtils.isNull(rateUserIds)) {
				String[] arrayRateUserIds = rateUserIds.split(",");
				if (arrayRateUserIds.length > 1) {
					return JsonView.JsonViewFactory.create().success(false).info("加息券不能叠加使用").toJson();
				}
				rateUser = rateUserService.findByRateUserId(Long.valueOf(arrayRateUserIds[0]));
				if (null == rateUser) {
					return JsonView.JsonViewFactory.create().success(false).info("该加息券不存在").toJson();
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
        BigDecimal voucherPayValue=BigDecimal.ZERO;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)){
            String[] voucher_Ids = voucherIds.split(",");
            //先校验财富券
            for (String voucherId:voucher_Ids){
                VoucherVO vo = voucherService.getVoucherById(Long.valueOf(voucherId));
                voucherVOs.add(vo);
                //计算财富券总额
                voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
            }
            JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
            if (!jsonView.isSuccess()){
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

        Long bankid = null;

        List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByTypeNeedSort("gateway_bank");
        for (ConstantDefine constantDefine : constantDefines) {
            if(bankCode.equals(constantDefine.getConstantValue())){
                bankid = constantDefine.getConstantDefineId();
                break;
            }
        }
        if (bankid == null)
            return JsonView.JsonViewFactory.create().success(false).info("不支持该银行卡").put("id", "bankid")
                    .toJson();

       //todo 连连支付可以做当日限额和当月限额校验
       // 购买金额是否合法
        String _result = amountValidate(lendOrder);
        if(_result!=null)
            return _result;

		LLPayRequest llPayRequest = rechargeOrderService.createPayGatewayRequest(accountPayValue, rechargePayValue, lendOrder, currentUser, userInfoExt, voucherVOs, voucherPayValue, ClientEnum.WEB_CLIENT, bankCode,rateUser,rateProduct);

        return JsonView.JsonViewFactory.create().success(true).info(llPayRequest.getUrl()).put("rechargeCode",llPayRequest.getRechargeOrder().getRechargeCode())
                .toJson();

    }
    
}
