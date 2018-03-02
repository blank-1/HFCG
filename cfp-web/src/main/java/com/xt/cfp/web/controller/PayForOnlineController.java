package com.xt.cfp.web.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.NameValue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.external.yeepay.PaymentForOnlineService;
import com.external.yeepay.QueryResult;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.constants.LendOrderConstants;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.constants.PayConstants;
import com.xt.cfp.core.constants.RechargeStatus;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendOrderBidDetail;
import com.xt.cfp.core.pojo.LendProductPublish;
import com.xt.cfp.core.pojo.PayOrder;
import com.xt.cfp.core.pojo.RechargeOrder;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.ext.LoanApplicationListVO;
import com.xt.cfp.core.pojo.ext.VoucherVO;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.PayService;
import com.xt.cfp.core.service.RechargeOrderService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.VoucherService;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.MD5Util;
import com.xt.cfp.core.util.Pair;
import com.xt.cfp.core.util.PropertiesUtils;
import com.xt.cfp.core.util.SecurityUtil;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.util.ValidationUtil;
import com.xt.cfp.web.annotation.DoNotNeedLogin;

/**
 * 网关支付controller
 * 
 * @author lianghuan
 * */
@Controller
@RequestMapping(value = "/payment")
public class PayForOnlineController extends BaseController {

	private Logger log = Logger.getLogger(PayForOnlineController.class);

	@Autowired
	private PayService payService;
	@Autowired
	private RechargeOrderService rechargeOrderService;
	@Autowired
	private LendProductService lendProductService;
	@Autowired
	private LoanApplicationService loanApplicationService;
	@Autowired
	private LendOrderService lendOrderService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private LendOrderBidDetailService lendOrderBidDetailService;

	/**
	 * 签名，返回易宝支付网址
	 * 
	 * @param orderId
	 *            -- 支付订单唯一标识
	 * @param rechargeAmount
	 *            --支付金额
	 * @param productName
	 *            -- 商品名称
	 * @param bankChannel
	 *            --- 支付通道
	 * @param type
	 *            -- 支付类型
	 * @return
	 * @throws UnsupportedEncodingException
	 * */
	public String getPayOnlineURL(String orderId, BigDecimal rechargeAmount,
			String productName, String bankChannel, String type)
			throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("p0_Cmd", "Buy");
		paramMap.put("p1_MerId", PaymentForOnlineService.getMerchantAccount());
		paramMap.put("p2_Order", orderId);

		if (PaymentForOnlineService.isTesting()) {
			paramMap.put("p3_Amt", "0.01");
		} else {
			paramMap.put("p3_Amt", rechargeAmount.toString());
		}
		paramMap.put("p4_Cur", "CNY");
		paramMap.put("p5_Pid", productName);
		paramMap.put("p6_Pcat", "");
		paramMap.put("p7_Pdesc", "");
		paramMap.put("p8_Url", PaymentForOnlineService.getCallbackUrl());
		paramMap.put("p9_SAF", "0");
		// “pay”
		// “recharge” 区分充值订单和支付订单
		paramMap.put("pa_MP", type);
		paramMap.put("pd_FrpId", bankChannel);
		// 订单的有效期 默认为7天，数字 需和 pn_Unit一起使用
		paramMap.put("pm_Period", "1");
		// 订单有效期的单位 “day” 或“month”
		paramMap.put("pn_Unit", "day");
		paramMap.put("pr_NeedResponse", "1"); // 固定为1 ， 需要应答
		String hmac = PaymentForOnlineService.getHmacOrderValid(paramMap);
		paramMap.put("hmac", hmac);
		String params = "";
		for (Entry<String, String> etr : paramMap.entrySet()) {
			if (StringUtils.isNull(params))
				params = "?" + encode(etr.getKey()) + "="
						+ encode(etr.getValue());
			else
				params += "&" + encode(etr.getKey()) + "="
						+ encode(etr.getValue());
		}
		return com.external.yeepay.PaymentForOnlineService.getDirectCommonURL()
				+ params;
	}

	/**
	 * 在线支付请求
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
	@RequestMapping(value = "/payOnline", method = RequestMethod.POST)
	@ResponseBody
	public String payOnline(
			HttpServletRequest request,
			@RequestParam(value = "bkcode") String bankCode,
			@RequestParam(value = "lendOrderId") Long lendOrderId,
			@RequestParam(value = "password") String jypassword,
			@RequestParam(value = "accountPayValue", defaultValue = "0") BigDecimal accountPayValue,
			@RequestParam(value = "rechargePayValue", defaultValue = "0") BigDecimal rechargePayValue,
			@RequestParam(value = "userVoucherId", required = false) String voucherIds) {
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		// 校验-参数必填
		ValidationUtil.checkRequiredPara(new NameValue<String, Object>(
				"lendOrderId", lendOrderId));
		LendOrder lendOrder = lendOrderService.findById(lendOrderId);
		request.setAttribute("lendOrder", lendOrder);
		//校验-财富券校验
		List<VoucherVO> voucherVOs = new ArrayList<VoucherVO>();
		BigDecimal voucherPayValue = BigDecimal.ZERO;
		if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)) {
			String[] voucher_Ids = voucherIds.split(",");
			// 先校验财富券
			for (String voucherId : voucher_Ids) {
				VoucherVO vo = voucherService.getVoucherById(Long
						.valueOf(voucherId));
				voucherVOs.add(vo);
				// 计算财富券总额
				voucherPayValue = voucherPayValue.add(vo.getVoucherValue());
			}
			JsonView jsonView = voucherService.voucherValidate(currentUser,
					lendOrder, voucherVOs);
			if (!jsonView.isSuccess()) {
				return jsonView.toJson();
			}
		}
		// 校验-订单用户和当前用户必须相同
		if (!lendOrder.getLendUserId().equals(currentUser.getUserId())) {
			return JsonView.JsonViewFactory.create().success(false)
					.info("您支付的订单不属于您").put("id", "redirect").toJson();
		}

		// 校验-订单用户和当前用户必须相同
		if (StringUtils.isNull(bankCode)) {
			return JsonView.JsonViewFactory.create().success(false)
					.info("银行卡信息为空").put("id", "redirect").toJson();
		}

		// 校验-如果订单已经被处理过
		if (!lendOrder.getOrderState().equals(
				LendOrderConstants.LoanOrderStatusEnum.UNPAY.getValue())) {
			return JsonView.JsonViewFactory.create().success(false)
					.info("订单已处理过").put("id", "redirect").toJson();
		}

		// 校验-是否有交易密码
		UserInfo userByUserId = this.userInfoService
				.getUserByUserId(currentUser.getUserId());

		// 校验-交易密码是否正确
		if (!userByUserId.getBidPass().equals(
				MD5Util.MD5Encode(jypassword, "utf-8"))) {
			return JsonView.JsonViewFactory.create().success(false)
					.info("交易密码错误").put("id", "jypassword").toJson();
		}

		// 校验-财富券拆分
		if (lendOrder.getBuyBalance().compareTo(voucherPayValue) < 0) {
			return JsonView.JsonViewFactory.create().success(false)
					.info("财富券金额不能大于订单金额").put("id", "redirect").toJson();
		}

		// 校验-购买金额加和是否等于订单金额
		if (lendOrder.getBuyBalance().compareTo(
				accountPayValue.add(rechargePayValue).add(voucherPayValue)) != 0) {
			return JsonView.JsonViewFactory.create().success(false)
					.info("购买金额异常").put("id", "redirect").toJson();
		}
		// 购买金额是否合法
		String _result = amountValidate(lendOrder);
		if (_result != null)
			return _result;

		// 创建支付订单
		PayOrder payOrder = createPayOrder(accountPayValue, rechargePayValue,
				voucherPayValue, lendOrder);

		// 支付单明细和财富券保存关系同时冻结财富券
		voucherService.linkVoucher(payOrder, voucherVOs);

		// 创建充值订单
		RechargeOrder rechargeOrder = this.rechargeOrderService
				.addRechargeOrder(rechargePayValue, null, payOrder.getPayId(),
						currentUser.getUserId(), "YB_EBK",PropertiesUtils.getInstance().get("SOURCE_PC"));

		// 支付
		String url;
		try {
			url = getPayOnlineURL(rechargeOrder.getRechargeCode(),
					rechargeOrder.getAmount(), "caifupad", bankCode, "pay");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("参数加密异常", e);
			return JsonView.JsonViewFactory.create().success(false)
					.info("参数加密异常").put("id", "redirect").toJson();
		}
		log.info("获取到的支付地址：" + url);
		return JsonView.JsonViewFactory.create().success(true).info(url)
				.put("rechargeCode", rechargeOrder.getRechargeCode()).toJson();
	}

	/**
	 * 支付金额验证
	 * 
	 * @param lendOrder
	 * @return 返回null表示验证通过
	 */
	private String amountValidate(LendOrder lendOrder) {

		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		if (lendOrder.getProductType().equals(
				LendProductTypeEnum.RIGHTING.getValue())) {
			// 投标金额验证
			List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService
					.findByLendOrderId(lendOrder.getLendOrderId());
			LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
			LoanApplicationListVO loanApplicationListVO = loanApplicationService
					.getLoanApplicationVoById(lendOrderBidDetail
							.getLoanApplicationId());

			if (currentUser != null) {
				// 获取账户余额
				if (lendOrder.getBuyBalance().compareTo(
						loanApplicationListVO.getRemain()) > 0) {
					return JsonView.JsonViewFactory.create().success(false)
							.info("金额已超出剩余金额！").put("id", "redirect").toJson();
				}
				BigDecimal totalBuy = lendOrderService
						.getTotalLendAmountByLoanApplicationId(loanApplicationListVO
								.getLoanApplicationId());
				if (lendOrder.getBuyBalance().compareTo(
						loanApplicationListVO.getConfirmBalance().subtract(
								totalBuy)) > 0) {
					// 无法购买剩余标
					JsonView.JsonViewFactory.create().success(false)
							.info("金额已超出剩余金额！").put("id", "redirect").toJson();
				}
				// 计算账户已投金额
				BigDecimal totalLendAmountPerson = lendOrderService
						.getTotalLendAmount(currentUser.getUserId(),
								loanApplicationListVO.getLoanApplicationId());
				if (totalLendAmountPerson != null) {
					if (loanApplicationListVO.getMaxBuyBalance() == null) {
						loanApplicationListVO
								.setMaxBuyBalanceNow(loanApplicationListVO
										.getConfirmBalance().subtract(
												totalLendAmountPerson));
					} else {
						loanApplicationListVO
								.setMaxBuyBalanceNow(loanApplicationListVO
										.getMaxBuyBalance().subtract(
												totalLendAmountPerson));
					}
				}
			}

			if (lendOrder.getBuyBalance().compareTo(
					loanApplicationListVO.getMaxBuyBalanceNow()) > 0)
				return JsonView.JsonViewFactory.create().success(false)
						.info("金额已超出当前最大可投金额！").put("id", "redirect").toJson();
		} else {
			// 省心计划金额验证
			if (!lendOrder.getProductType().equals(
					LendProductTypeEnum.FINANCING.getValue()))
				throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);
			LendProductPublish lendProductPublish = lendProductService
					.getLendProductPublishByPublishId(lendOrder
							.getLendProductPublishId());
			// 校验金额是否合法
			BigDecimal waitingBuyBalance = lendProductPublish
					.getPublishBalance().subtract(
							lendProductPublish.getSoldBalance());
			if (lendOrder.getBuyBalance().compareTo(waitingBuyBalance) > 0)
				return JsonView.JsonViewFactory.create().success(false)
						.info("购买金额已超出剩余可购买金额").put("id", "redirect").toJson();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private PayOrder createPayOrder(BigDecimal accountPayValue,
			BigDecimal rechargePayValue, BigDecimal voucherPayValue, LendOrder lendOrder) {
		PayConstants.BusTypeEnum busTypeEnum = null;
		if (lendOrder.getProductType().equals(
				LendProductTypeEnum.FINANCING.getValue()))
			busTypeEnum = PayConstants.BusTypeEnum.BUY_FINANCE;

		if (lendOrder.getProductType().equals(
				LendProductTypeEnum.RIGHTING.getValue()))
			busTypeEnum = PayConstants.BusTypeEnum.BID_LOAN;
		Pair<String, BigDecimal> accountAmount = new Pair<String, BigDecimal>(
				PayConstants.AmountType.ACCOUNT.getValue(), accountPayValue);
		Pair<String, BigDecimal> rechargeAmount = new Pair<String, BigDecimal>(
				PayConstants.AmountType.RECHARGE.getValue(), rechargePayValue);
		// todo 财富券
		Pair<String, BigDecimal> voucherAmount = new Pair<String, BigDecimal>(
				PayConstants.AmountType.VOUCHERS.getValue(), voucherPayValue);
		return this.payService.addPayOrderForLendOrder(lendOrder, new Date(),
				busTypeEnum, accountAmount, rechargeAmount, voucherAmount);
	}

	/**
	 * 网关充值请求
	 * 
	 * @param request
	 * @param rechargeAmount
	 * @return
	 */
	@RequestMapping(value = "/rechargeOnline")
	@ResponseBody
	public String rechargeOnline(
			HttpServletRequest request,
			@RequestParam(value = "bkcode") String bankCode,
			@RequestParam(value = "rechargeAmount", defaultValue = "0") BigDecimal rechargeAmount) {
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		UserInfo userByUserId = this.userInfoService
				.getUserByUserId(currentUser.getUserId());
		// 校验交易密码
		if (userByUserId.getBidPass() == null)
			return JsonView.JsonViewFactory.create().success(false)
					.info("请您输入交易密码！").put("id", "jypassword").toJson();

		// 校验-是否有交易密码
		if (userByUserId.getBidPass() == null)
			return JsonView.JsonViewFactory.create().success(false)
					.info("还未设定交易密码！").put("id", "jypassword").toJson();

		// 校验-购买金额是否大于50
		if (new BigDecimal("100").compareTo(rechargeAmount) > 0)
			return JsonView.JsonViewFactory.create().success(false)
					.info("请输入大于100元的金额！").put("id", "moneybk").toJson();

		// 创建充值订单
		RechargeOrder rechargeOrder = this.rechargeOrderService
				.addRechargeOrder(rechargeAmount, null, null,
						currentUser.getUserId(), "YB_EBK",PropertiesUtils.getInstance().get("SOURCE_PC"));

		// 支付
		String url;
		try {
			url = getPayOnlineURL(rechargeOrder.getRechargeCode(),
					rechargeAmount, "caifupad", bankCode, "recharge");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("参数加密异常", e);
			return JsonView.JsonViewFactory.create().success(false)
					.info("参数加密异常").put("id", "redirect").toJson();
		}
		log.info("获取到的支付地址：" + url);
		return JsonView.JsonViewFactory.create().success(true).info(url)
				.put("rechargeCode", rechargeOrder.getRechargeCode()).toJson();
	}

	/**
	 * 易宝网关支付回调请求
	 * */
	@DoNotNeedLogin
	@RequestMapping(value = "/callbackPayment")
	public String callbackPayment(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("GBK");
			Map<String, String[]> reqMap = request.getParameterMap();
			Map<String, String> param = new HashMap<String, String>();
			if (null != reqMap) {
				for (Entry<String, String[]> e : reqMap.entrySet()) {
					switch (e.getKey()) {
						case "r5_Pid":
						case "r8_MP": {
							param.put(e.getKey(),
									decode(formatString(e.getValue()[0])));
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
			isOK = PaymentForOnlineService.verifyCallbackOrderValid(
					param.get("hmac"), param.get("p1_MerId"),
					param.get("r0_Cmd"), param.get("r1_Code"),
					param.get("r2_TrxId"), param.get("r3_Amt"),
					param.get("r4_Cur"), param.get("r5_Pid"),
					param.get("r6_Order"), param.get("r7_Uid"),
					param.get("r8_MP"),  param.get("r9_BType"),
					PaymentForOnlineService.getMerchantKeyValue());
			// 测试+true
			if (isOK) {
				// 在接收到支付结果通知后，判断是否进行过业务逻辑处理，不要重复进行业务逻辑处理
				// r1_Code “1” 代表支付成功 其他代表失败
				if (param.get("r1_Code").equals("1")) {

					if (param.get("r9_BType").equals("1")) {// 产品通用接口支付成功返回-浏览器重定向
						// out.println("callback方式:产品通用接口支付成功返回-浏览器重定向");
						// 接收浏览器返回后不需要做处理，业务第三方支付后台回调为准
						return "order/paymentSuccess";
					} else if (param.get("r9_BType").equals("2")) {// 产品通用接口支付成功返回-服务器点对点通讯
						// 如果在发起交易请求时 设置使用应答机制时，必须应答以"success"开头的字符串，大小写不敏感
						// 产品通用接口支付成功返回-电话支付返回
						rechargeOrderService
								.handleCallbackPaymentOnlineNotification(param);
						printReturnData("SUCCESS", response);
					}
				} else {
					if (param.get("r9_BType").equals("1")) {
						return "order/paymentError";
					} else if (param.get("r9_BType").equals("2")) {
						// 支付结果失败，更新业务数据
						rechargeOrderService
								.handleCallbackPaymentOnlineNotification(param);
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
		// 校验-订单用户和当前用户必须相同
		if (ro == null || !ro.getUserId().equals(currentUser.getUserId())) {
			request.setAttribute("errorMsg", "您支付的订单不属于您");
			return getResultPath("false");
		}
		// 未支付
		if (RechargeStatus.UN_RECHARGE.getValue().equals(ro.getStatus())) {
			QueryResult result = null ;
			try{
				result = PaymentForOnlineService
						.queryByOrder(rechargeCode);
			}catch(Exception e){
				log.error("查询易宝订单接口失败，失败原因：",e);
				request.setAttribute("errorMsg", "网络异常，请重新查看订单");
				return getResultPath("false");
			}
			if (result == null || !"1".equals(result.getR1_Code())) {
				request.setAttribute("errorMsg", RechargeStatus.FAILE.getDesc());
				return getResultPath("false");
			} else {
				if (result.getRb_PayStatus().equalsIgnoreCase("SUCCESS")) {
					Map<String, String> param = new HashMap<String, String>();
					param.put("r6_Order", result.getR6_Order());
					param.put("r2_TrxId", result.getR2_TrxId());
					param.put("r1_Code", result.getR1_Code());
					try{
						rechargeOrderService
								.handleCallbackPaymentOnlineNotification(param);
						}catch(Exception e){
							log.error("系统异常:" ,e);
							request.setAttribute("errorMsg", "购买出现错误");
							return getResultPath("false");
						}
					Map<String,Object> rMap = getPayOrderResult(ro.getPayId(), lendOrder.getOrderState());
					request.setAttribute("errorMsg", rMap.get("errorMsg"));
					request.setAttribute("ordercode",rMap.get("ordercode"));
					return getResultPath((String)rMap.get("flag"));
				} else {
					return getResultPath("recharging");
				}
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
	/**
	 * 充值单查询请求
	 * 
	 * */
	@RequestMapping(value = "/getRechargeResult")
	public String getRechargeResul(HttpServletRequest request,	String rechargeCode) {
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		RechargeOrder ro = rechargeOrderService.getRechargeOrderByOrderCode(rechargeCode, false);
		// 校验-订单用户和当前用户必须相同
		if (ro == null || !ro.getUserId().equals(currentUser.getUserId())) {
			request.setAttribute("errorMsg", "您的充值订单不属于您");
			return getRechargePath("false");
		}
		// 未支付
		if (RechargeStatus.UN_RECHARGE.getValue().equals(ro.getStatus())) {
			QueryResult result = null ;
			try{
				result = PaymentForOnlineService
						.queryByOrder(rechargeCode);
			}catch(Exception e){
				log.error("查询易宝订单接口失败，失败原因：",e);
				request.setAttribute("errorMsg", "网络异常，请稍后查看充值结果");
				return getRechargePath("false");
			}
			if (result == null || !"1".equals(result.getR1_Code())) {
				request.setAttribute("errorMsg", RechargeStatus.FAILE.getDesc());
				return getRechargePath("false");
			} else {
				if (result.getRb_PayStatus().equalsIgnoreCase("SUCCESS")) {
					Map<String, String> param = new HashMap<String, String>();
					param.put("r6_Order", result.getR6_Order());
					param.put("r2_TrxId", result.getR2_TrxId());
					param.put("r1_Code", result.getR1_Code());
					try{
						rechargeOrderService
								.handleCallbackPaymentOnlineNotification(param);
						}catch(Exception e){
							log.error("系统异常:" ,e);
							request.setAttribute("errorMsg", "充值出现错误");
							return getRechargePath("false");
						}
					return getRechargePath("success");
				} else {
					return getRechargePath("recharging");
				}
			}

		} else if (RechargeStatus.FAILE.getValue().equals(ro.getStatus())) {// 支付失败
			request.setAttribute("errorMsg", RechargeStatus.FAILE.getDesc());
			return getResultPath("false");
		} else if (RechargeStatus.SUCCESS.getValue().equals(ro.getStatus())) {
			return getRechargePath("success");
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

	private String formatString(String string) {
		if (string == null) {
			return string = "";
		}
		return string;
	}



	private String encode(String str) throws Exception {
		return URLEncoder.encode(str, "GB2312");
	}

	private String decode(String str) throws Exception {
		return URLDecoder.decode(str, "UTF-8");
	}

}
