package com.xt.cfp.web.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.*;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum.RightsRepaymentDetailIsPayOffEnum;
import com.xt.cfp.core.constants.LendOrderConstants.FinanceOrderStatusEnum;
import com.xt.cfp.core.constants.LendOrderConstants.FinanceProfitReturnEnum;
import com.xt.cfp.core.constants.RateEnum.RateProductIsOverlayEnum;
import com.xt.cfp.core.constants.RateEnum.RateProductScenarioEnum;
import com.xt.cfp.core.constants.RateEnum.RateUserStatusEnum;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.financePlan.FinancePlanProcessModule;
import com.xt.cfp.core.service.financePlan.FinancePlanService;
import com.xt.cfp.core.service.impl.CgBizService;
import com.xt.cfp.core.util.*;
import com.xt.cfp.web.annotation.DoNotNeedLogin;
import jodd.util.NameValue;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;


/**
 * Created by yulei on 2015/7/2.
 */
@Controller
@RequestMapping(value = "/finance")
public class FinanceController extends BaseController{

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
	private UserInfoExtService userInfoExtService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private CustomerCardService customerCardService;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private PayService payService;
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
	private EnterpriseFoundationSnapshotService enterpriseFoundationSnapshotService;
	@Autowired
	private EnterprisePledgeSnapshotService enterprisePledgeSnapshotService;
	@Autowired
	private ProvinceInfoService provinceInfoService;
	@Autowired
	private CityInfoService cityInfoService;
	@Autowired
	private MortgageCarSnapshotService mortgageCarSnapshotService;
	@Autowired
	private EnterpriseCreditSnapshotService enterpriseCreditSnapshotService;
	@Autowired
	private GuaranteeCompanyService guaranteeCompanyService;
	@Autowired
	private ConstantDefineService constantDefineService;

	@Autowired
	private CoLtdService coLtdService;

	@Autowired
	private EnterpriseLoanApplicationService enterpriseLoanApplicationService;
	@Autowired
	private EnterpriseFactoringSnapshotService enterpriseFactoringSnapshotService;

	@Autowired
	private AgreementInfoService agreementInfoService ;
	@Autowired
	private RightsRepaymentDetailService rightsRepaymentDetailService;
	@Autowired
	private CreditorRightsTransferAppService creditorRightsTransferAppService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private RateUserService rateUserService;
	@Autowired
	private RateProductService rateProductService;
	@Autowired
	private FinancePlanAgreementService financePlanAgreementService;
    @Autowired
    private FinancePlanProcessModule financePlanProcessModule;
    @Autowired
    private FinancePlanService financePlanService;
	@Autowired
	private CustomerCarSnapshotService customerCarSnapshotService;
	@Autowired
	private CgBizService cgBizService;
	/**
	 * 下载免责声明
	 *
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/download/disclaimer")
	public void disclaimer(HttpServletRequest request, HttpServletResponse response) {

		try {
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			String fileName = "免责声明";
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ".pdf");// 设定输出文件头     待打款提现单
			response.setContentType("application/pdf");// 定义输出类型
			String rootPath = WebUtil.getHttpServletRequest().getSession().getServletContext().getRealPath("");
			FileInputStream in = new FileInputStream(new File(rootPath+"/resources/doc/免责声明.pdf"));
			OutputStream out = response.getOutputStream();
			byte[] b = new byte[512];
			while ((in.read(b)) != -1) {
				out.write(b);
			}
			out.flush();
			in.close();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 进入省心计划列表
	 * @param flag 判断是否为新手标
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/list")
	public String toFinanceList(HttpServletRequest request, HttpServletResponse response,String flag) {

		List<LproductWithBalanceStatus> financeProductListForWeb = this.lendProductService.findFinanceProductListForWeb();

		// -----------------------------组装页面查询用的参数---------------------------------
		Set<String> interestReturnTypes = new HashSet<String>();
		Set<Integer> closingTypes = new TreeSet<Integer>();// treeset排序
		Set<String> rateTypes = new HashSet<String>();
		for (LproductWithBalanceStatus lproductWithBalanceStatus : financeProductListForWeb) {
			// 组装返息周期
			if (lproductWithBalanceStatus.getToInterestPoint() == LendProduct.TOINTERESTPOINT_BEQUIT)
				interestReturnTypes.add("2");
			else
				interestReturnTypes.add("1");

			// 组装封闭期
			closingTypes.add(lproductWithBalanceStatus.getClosingDate());

			// 组装预计收益
			rateTypes.add(lproductWithBalanceStatus.getProfitRate().toString());
		}
		// 出借类型
		LoanTypeEnum[] loanTypes = LoanTypeEnum.values();
		request.setAttribute("loanTypes", loanTypes);
		// 借款期限
		List<Integer> durationTypes = loanApplicationService.getDurationTypes(LoanApplicationStateEnum.BIDING,LoanApplicationStateEnum.COMPLETED);
		request.setAttribute("durationTypes", durationTypes);
		// 预期收益
		if(flag!=null&&!flag.equals("")&&flag.equals("new")){
			List<BigDecimal> loanRateTypes = loanApplicationService.getLoanRateTypesByNewUser(LoanApplicationStateEnum.BIDING,LoanApplicationStateEnum.COMPLETED);
			request.setAttribute("loanRateTypes", loanRateTypes);
		}else{
			List<BigDecimal> loanRateTypes = loanApplicationService.getLoanRateTypes(LoanApplicationStateEnum.BIDING,LoanApplicationStateEnum.COMPLETED);
			request.setAttribute("loanRateTypes", loanRateTypes);
		}

		// 理财产品预期收益 wangyadong add
		List<BigDecimal> lendRateTypes = loanApplicationService.getLendRateTypes();
		request.setAttribute("lendRateTypes", lendRateTypes);

		UserInfo user = (UserInfo) request.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
		if (null != user) {
			UserAccount userAccount = userAccountService.getCashAccount(user.getUserId());
			request.setAttribute("userAccount", userAccount);
			UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
			request.setAttribute("userExt", userExt);
		}
		request.setAttribute("financeProductListForWeb", financeProductListForWeb);
		request.setAttribute("interestReturnTypes", interestReturnTypes);
		request.setAttribute("closingTypes", closingTypes);
		request.setAttribute("rateTypes", rateTypes);
		request.setAttribute("flagCustomer", flag);
		// 组装数据 传入前台展示
		JSONArray json = new JSONArray();
		for (int i = 0; i < financeProductListForWeb.size(); i++) {
			LproductWithBalanceStatus record = financeProductListForWeb.get(i);
			JSONObject obj = new JSONObject();
			obj.put("productName", record.getProductName());
			obj.put("profitRate", record.getProfitRate());
			obj.put("closingDate", record.getClosingDate());
			obj.put("publishCode", record.getPublishCode());
			obj.put("availableBalance", record.getAvailableBalance());
			obj.put("toInterestPoint", record.getToInterestPoint());
			obj.put("profitRate", record.getProfitRate());
			obj.put("lendProductPublishId", record.getLendProductPublishId());
			obj.put("timeLimitType", record.getTimeLimitType());
			obj.put("timeLimit", record.getTimeLimit());
			json.add(obj);
		}
		request.setAttribute("jsonProduct", json);
		// 设置省心计划还是出借选项页默认选中
		if("heng".equals(request.getParameter("tab"))){
			request.setAttribute("tab", "heng");
		}else {
			request.setAttribute("tab", "zhe");
		}
		return "/finance/financeList";
	}


	/**
	 * 省心计划列表
	 * @param financeGuessList  预期收益
	 * @param financeduringList 借款期限
	 * @param financeLendList   付息类型
	 * @author wangyadong
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/lendList")
	@ResponseBody
	public Object lendList(HttpServletRequest request,
			LendProductPublishVO lendProductPublish,
						   @RequestParam(value = "pageSize", defaultValue = "6") int pageSize,
						   @RequestParam(value = "pageNo",defaultValue = "1") int pageNo,
						   @RequestParam(value = "financeGuessList",defaultValue = "") String financeGuessList,
						   @RequestParam(value = "financeduringList",defaultValue = "") String financeduringList,
						   @RequestParam(value = "financeLendList",defaultValue = "") String financeLendList
						  ){

				lendProductPublish.setProfitRate(financeLendList);
				lendProductPublish.setPublishCode(financeGuessList);
				lendProductPublish.setPublishName(financeduringList);
		        lendProductPublish.setClosingDate(lendProductPublish.getOrderBy());
		Pagination<LendProductPublishVO> lproductWithBalanceStatus = this.lendProductService.findFinanceProductListForWebCondition(pageSize,pageNo,lendProductPublish);

		return lproductWithBalanceStatus;
	}

	/**
	 * 进入
	 * @param request
	 * @param lendOrderId
     * @return
     */
	@RequestMapping(value = "toPayFinanceOrder")
	public String toPayFinanceOrder(HttpServletRequest request,
								 @RequestParam(value = "lendOrderId") Long lendOrderId) {
		TokenUtils.setNewToken(request);
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
		LendOrder lendOrder = lendOrderService.findById(lendOrderId);
		//校验-订单是否存在
		if (lendOrder==null)
			throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderId", lendOrderId);

		//校验-订单用户和当前用户必须相同
		if (!lendOrder.getLendUserId().equals(currentUser.getUserId()))
			throw new SystemException(PayErrorCode.NOT_BELONG_PAY_ORDER).set("lendOrderUserId",lendOrder.getLendUserId()).set("user",currentUser.getUserId());

		//校验-订单必须是省心计划类型
		if (!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
			throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);

		LendProductPublish lendProductPublish = lendProductService.getLendProductPublishByPublishId(lendOrder.getLendProductPublishId());
		LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());
		//校验业务数据
		checkForToBuyFinanaceByPayAmount(currentUser.getUserId(), lendProductPublish, lendProduct, lendOrder.getBuyBalance());
		//获取省心计划信息明细
		LproductWithBalanceStatus financeDetail = this.lendProductService.findFinanceProductDetailForWeb(lendProductPublish.getLendProductPublishId());

		//查询用户卡信息
		CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
		if (customerCard != null && null!=customerCard.getMobile())
			customerCard.setMobile(customerCard.getMobile().substring(0, 3) + "*****" + customerCard.getMobile().substring(8));

		//预计收益
		BigDecimal interesting = BigDecimalUtil.down(InterestCalculation.getExpectedInteresting(lendOrder.getBuyBalance(),
				lendProduct.getProfitRate(), lendProduct.getTimeLimitType().toCharArray()[0], lendProduct.getTimeLimit()), 2);

		//财富券
		List<VoucherVO> vouchers = voucherService.getAllVoucherList(currentUser.getUserId(),lendOrder.getBuyBalance(),VoucherConstants.UsageScenario.WHOLE, VoucherConstants.UsageScenario.FINANCE);

		request.setAttribute("userExtInfo", userExt);
		request.setAttribute("order", lendOrder);
		request.setAttribute("productDetail", financeDetail);
		request.setAttribute("amount", lendOrder.getBuyBalance());
		request.setAttribute("truePayAmount", lendOrder.getBuyBalance());
		request.setAttribute("customerCard", customerCard);
		request.setAttribute("vouchers", vouchers);
		request.setAttribute("interesting", interesting);
		request.setAttribute("lendProduct", lendProduct);
		request.setAttribute("lendProductPublish", lendProductPublish);
		request.setAttribute("userCashBalance", userAccountService.getCashAccount(currentUser.getUserId()).getAvailValue2());
		boolean isBidEqualLoginPass = userInfoService.isBidPassEqualLoginPass(currentUser.getUserId());
		request.setAttribute("isBidEqualLoginPass",isBidEqualLoginPass);
		//获取网关充值银行卡列表
        List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByTypeNeedSort("gateway_bank");
        request.setAttribute("bankList", constantDefines);

		return "order/noCardPay";
	}

	@RequestMapping(value = "toBuyBidLoanByPayAmount")
	public String toBuyBidLoanByPayAmount(Long loanApplicationId,
										  BigDecimal amount,
										  @RequestParam(value = "oPass", required = false) String oPass,
										  HttpServletRequest request) {
		if (loanApplicationId==null){
			return "/error";
		}

		//TokenUtils.validateToken(request);
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		//判断定向标
		String path = chioceWhichReturnBySpecialLoanApplication(currentUser.getUserId(),loanApplicationId,oPass,request);
		if(!"".equals(path))
			return path;
		LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(loanApplicationId);
		LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationId);

		LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());
		//校验业务数据
		checkBidLoanByPay(loanApplicationListVO, amount, currentUser);
		//检查剩余金额
		BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
		if(amount.compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy))>0){
			//订单金额为剩余金额
			if(loanApplicationListVO.getConfirmBalance().compareTo(totalBuy)==0){
				throw new SystemException(BidErrorCode.LENDORDER_AMOUNT_ZONE).set("amount", 0);
			}
			throw new SystemException(BidErrorCode. LENDORDER_AMOUNT_NOT_ENOUGH);
//			amount = loanApplicationListVO.getConfirmBalance().subtract(totalBuy);
		}

		//100倍数验证
		if (amount.remainder(new BigDecimal("100")).longValue()!=0){
			throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
		}

		//新建投标订单
		LendOrder lendOrder = lendOrderService.addLoanOrder(currentUser.getUserId(), loanApplicationId, lendProductPublish.getLendProductPublishId(), amount, new Date(), lendProduct,PropertiesUtils.getInstance().get("SOURCE_PC"));
		//购买省心计划
		return "redirect:/finance/toPayForLender?lendOrderId="+lendOrder.getLendOrderId();
	}

	/**
	 * 支付订单时检查省心计划
	 * @param request
	 * @param lendOrderId
	 * @return
	 */
	@RequestMapping(value = "checkForToBuyFinanaceByPayAmount")
	@ResponseBody
	public String checkForToBuyFinanaceByPayAmount(HttpServletRequest request,
												   @RequestParam(value = "lendOrderId") Long lendOrderId){
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


		//todo 省心计划状态验证
		if (lendProductPublish.getPublishState()!=LendProductPublishStateEnum.SELLING.value2Char()){
			throw new SystemException(BidErrorCode.FINANACE_STATUS_NOT_SELLING);
		}


		//校验金额是否合法
		if(lendProductPublish.getPublishBalanceType() == LendProductPublish.PUBLISHBALANCETYPE_SPEC){
			BigDecimal waitingBuyBalance = lendProductPublish.getPublishBalance().subtract(lendProductPublish.getSoldBalance());
			if (lendOrder.getBuyBalance().compareTo(waitingBuyBalance)>0)
				return JsonView.JsonViewFactory.create().success(false).info("购买金额已超出剩余可购买金额")
						.toJson();
		}

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
										  @RequestParam(value="isUseVoucher",required=false) String isUseVoucher ,
										  @RequestParam(value="profitReturnConfig",required=false) String profitReturnConfig ,
										  HttpServletRequest request) {
//		TokenUtils.setNewToken(request);
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
				lendProduct,PropertiesUtils.getInstance().get("SOURCE_PC"),isUseVoucher,profitReturnConfig);
		/*LendOrder lendOrder = null;
		String orderId = redisCacheManger.getRedisCacheInfo("finance_orderId" + lendProductPublishId);
		if(orderId!=null){
			lendOrder = lendOrderService.findById(Long.valueOf(orderId));
		}else{
			lendOrder = lendOrderService.addFinanceOrder(currentUser.getUserId(), lendProductPublishId, amount, new Date(), lendProduct);
			redisCacheManger.setRedisCacheInfo("finance_orderId"+lendProductPublishId,lendOrder.getLendOrderId()+"");
		}*/

		//获取省心计划信息明细
		//LproductWithBalanceStatus financeDetail = this.lendProductService.findFinanceProductDetailForWeb(lendProductPublishId);

		//预计收益
		//BigDecimal interesting = InterestCalculation.getExpectedInteresting(lendOrder.getBuyBalance(), lendProduct.getProfitRate(), lendProduct.getTimeLimitType().toCharArray()[0], lendProduct.getTimeLimit());

		//查询用户卡信息
		/*CustomerCard customerCard = customerCardService.getCustomerCardByUserId(currentUser.getUserId());
		if (customerCard != null)
			customerCard.setMobile(customerCard.getMobile().substring(0, 3) + "*****" + customerCard.getMobile().substring(8));
*/
		//查询用户卡累计提现金额
		//BigDecimal withdrawAmount = customerCard != null ? customerCardService.getCardWithdrawAmount(customerCard.getCustomerCardId()) : BigDecimal.ZERO;

		//todo 财富券实现
		/*List vouchers = new ArrayList();

		request.setAttribute("lendOrder", lendOrder);
		request.setAttribute("productDetail", financeDetail);
		request.setAttribute("amount", amount);
		request.setAttribute("truePayAmount", amount);
		request.setAttribute("customerCard", customerCard);
		request.setAttribute("vouchers", vouchers);
		request.setAttribute("interesting", interesting);
		request.setAttribute("lendProduct", lendProduct);
		request.setAttribute("userExtInfo", userExt);
		request.setAttribute("lendProductPublish", lendProductPublish);
		request.setAttribute("userCashBalance", userAccountService.getCashAccount(currentUser.getUserId()).getAvailValue2());
		boolean isBidEqualLoginPass = userInfoService.isBidPassEqualLoginPass(currentUser.getUserId());
		request.setAttribute("isBidEqualLoginPass",isBidEqualLoginPass);
		if (customerCard == null)
			return "order/noCardPay";
		else
			return "order/hasCardPay";*/
		return "redirect:/finance/toPayFinanceOrder?lendOrderId="+lendOrder.getLendOrderId();
	}

	/**
	 * 用余额购买省心计划
	 *
	 * @param password
	 * @param lendProductPublishId
	 * @param amount
	 * @param profitReturnConfig 省心计划收益分配方式
	 * @return
	 */
	@RequestMapping(value = "buyFinanceByAccountAmount")
	public String buyFinanceByAccountAmount(@RequestParam(value = "password") String password,
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
			if(amount.compareTo(leftBalancce)>0){
				request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_TOO_MUCH.getDesc());
				return "/order/payError";
			}
		}

		if (lendOrderId == null) {
			payResult = lendProductService.buyFinanceByAccountBalance(currentUser.getUserId(), lendProductPublishId, amount, PropertiesUtils.getInstance().get("SOURCE_PC"),isUseVoucher,profitReturnConfig);
			lendOrder = lendOrderService.getLendOrderByPayId(payResult.getPayId(), true);
		} else {
			payResult = lendProductService.payFinanceByAccountBalanceWeb(currentUser.getUserId(), lendOrderId, null, null, null);
			lendOrder = lendOrderService.findById(lendOrderId);
		}

		request.setAttribute("payResult", payResult);
		request.setAttribute("lendOrder", lendOrder);
		//根据执行结果跳转不同页面
		if (payResult.isPayResult() && payResult.isProcessResult()){
			//生成省息计划回款列表和合同
	        financePlanProcessModule.beginCalcInterest(lendOrder,new Date());
	        //匹配
	        try {
				financePlanService.match(lendOrder);
			} catch (Exception e) {
				logger.info("匹配省心计划失败，失败原因：" , e);
			}
			return "/order/paySuccess";
		}

		return "/order/payError";
	}

	/**
	 * 用余额、财富券投标
	 *
	 * @param password
	 * @param amount
	 * @return
	 */
	@RequestMapping(value = "bidLoanByAccountBalance")
	public String bidLoanByAccountBalance(@RequestParam(value = "password", required = false) String password,
										  @RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId,
										  Long lendOrderId,
										  @RequestParam(value = "amount", required = false) BigDecimal amount,
										  @RequestParam(value = "accountPayValue", defaultValue = "0") BigDecimal accountPayValue,
										  @RequestParam(value = "userVoucherId", required = false) String voucherIds,
										  @RequestParam(value = "rateUserIds", required = false) String rateUserIds,
										  @RequestParam(value = "oPass", required = false) String oPass,
											HttpServletRequest request) {
		ValidationUtil.checkRequiredPara(new NameValue<String, Object>("password", password),
				new NameValue<String, Object>("loanApplicationId", loanApplicationId),
				new NameValue<String, Object>("amount", amount));
		LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(loanApplicationId);
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		TokenUtils.validateToken(request);
		logger.info(LogUtils.createLogWithParams("投标", DescTemplate.Log.BidLogTemplate.BID_LOAN_BY_ACCOUNTVALUE,
				currentUser.getUserId(), lendProductPublish.getLendProductPublishId(), amount));

		LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationId);
//		//校验是否为定向标极其类型
		String path = chioceWhichReturnBySpecialLoanApplication(currentUser.getUserId(),loanApplicationId,oPass,request);
		if(!"".equals(path))
			return path;
		//校验交易密码
		if (currentUser.getBidPass() == null){
			request.setAttribute("errorMsg", UserErrorCode.NO_BIDPASS.getDesc());
			return "/order/payError";
		}
		if (!MD5Util.MD5Encode(password, null).equals(currentUser.getBidPass())){
			request.setAttribute("errorMsg", UserErrorCode.ERROR_BIDPASS.getDesc());
			return "/order/payError";
		}

		//校验身份认证状态
		if (!this.userInfoService.hasIdentityAuthentication(currentUser.getUserId())){
			request.setAttribute("errorMsg", UserErrorCode.HAS_NOT_IDVERIFIED.getDesc());
			return "/order/payError";
		}

		//标的状态验证
		if (!LoanApplicationStateEnum.BIDING.getValue().equals(loanApplicationListVO.getApplicationState())){
			request.setAttribute("errorMsg", BidErrorCode.BID_STATIS_NOT_BIDDING.getDesc());
			return "/order/payError";
		}

		//预热标验证
		if (!loanApplicationListVO.isBegin()){
			request.setAttribute("errorMsg", BidErrorCode.BID_STATIS_NOT_SELLING.getDesc());
			return "/order/payError";
		}

		//验证购买金额是否合法(不大于0的情况)
		if (amount.compareTo(BigDecimal.ZERO) <= 0){
			request.setAttribute("errorMsg", BidErrorCode.AMOUNT_ILLEGAL.getDesc());
			return "/order/payError";
		}

		//校验金额是否合法
		loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());
		HttpSession session = WebUtil.getHttpServletRequest().getSession();
		List<VoucherVO> voucherVOs = new ArrayList<VoucherVO>();
		BigDecimal voucherPayValue=BigDecimal.ZERO;
		String[] voucher_Ids = null;
		if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)){
			voucher_Ids = voucherIds.split(",");
			//财富券
			for (String voucherId:voucher_Ids){
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
				return "/order/payError";
			}
			rateUser = rateUserService.findByRateUserId(Long.valueOf(arrayRateUserIds[0]));
			if (null == rateUser) {
				request.setAttribute("errorMsg", "该加息券不存在");
				return "/order/payError";
			}
			LendProduct product = lendProductService.findById(lendProductPublish.getLendProductId());
			rateProduct = rateProductService.getRateProductById(rateUser.getRateProductId());
			JsonView checkRateUser = rateUserService.checkRateUser(rateUser, rateProduct, currentUser.getUserId(), product.getTimeLimit(), loanApplicationListVO.getLoanType(), amount);
			if (!checkRateUser.isSuccess()) {
				request.setAttribute("errorMsg", checkRateUser.getInfo());
				return "/order/payError";
			}
			if (!rateProduct.getUsageScenario().equals(RateProductScenarioEnum.ALL.getValue()) && !rateProduct.getUsageScenario().equals(RateProductScenarioEnum.BIDDING.getValue())) {
				request.setAttribute("errorMsg", "该加息券不能购买投标产品");
				return "/order/payError";
			}
			if (!com.xt.cfp.core.util.StringUtils.isNull(voucherIds)) {
				if (rateProduct.getIsOverlay().equals(RateProductIsOverlayEnum.DISABLED.getValue())) {
					request.setAttribute("errorMsg", "该加息券不能和财富券叠加使用");
					return "/order/payError";
				}
			}
		}

		UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);
		if(user!=null){
			//获取账户余额
			UserAccount cashAccount = userAccountService.getCashAccount(user.getUserId());
			if (cashAccount.getAvailValue2().compareTo(accountPayValue)<0){
				request.setAttribute("errorMsg", UserAccountErrorCode.ACCOUNT_BALANCE_AVAIVALUE_INSUFFICIENT.getDesc());
				return "/order/payError";
			}
			BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
			if(amount.compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy))>0){
				//购买金额变为剩余可投金额
				if(loanApplicationListVO.getConfirmBalance().compareTo(totalBuy)==0){
					request.setAttribute("errorMsg", BidErrorCode.LENDORDER_AMOUNT_ZONE.getDesc());
					return "/order/payError";
				}
				request.setAttribute("errorMsg", BidErrorCode.LENDORDER_AMOUNT_NOT_ENOUGH.getDesc());
				return "/order/payError";
//				amount = loanApplicationListVO.getConfirmBalance().subtract(totalBuy);
			}
			//计算账户已投金额
			BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(user.getUserId(), loanApplicationListVO.getLoanApplicationId());
			if (totalLendAmountPerson != null) {
				if(loanApplicationListVO.getMaxBuyBalance()==null){
					loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
				}else{
					loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
				}
			}

			if(amount.compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy))>0){
				//无法购买剩余标
				throw new SystemException(BidErrorCode.LENDORDER_AMOUNT_NOT_ENOUGH).set("amount", amount).set("remain",loanApplicationListVO.getConfirmBalance().subtract(totalBuy));
			}
		}

		if(amount.compareTo(loanApplicationListVO.getMaxBuyBalanceNow())>0){
			request.setAttribute("errorMsg", BidErrorCode.LIMIT_AMOUNT_NOT_ENOUGH.getDesc());
			return "/order/payError";
		}

		//100倍数验证
		if (amount.remainder(new BigDecimal("100")).longValue()!=0){
			throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
		}

		PayResult payResult = null;
		LendOrder lendOrder = null;
		if (lendOrderId == null) {
			//可用余额支付
			payResult = lendProductService.bidLoanByAccountBalance(loanApplicationId, currentUser.getUserId(), lendProductPublish.getLendProductPublishId(), amount, PropertiesUtils.getInstance().get("SOURCE_PC") );
			lendOrder = lendOrderService.getLendOrderByPayId(payResult.getPayId(), true);
		} else {
			//可用余额、财富券支付
			lendOrder = lendOrderService.findById(lendOrderId);

			//todo 财富券校验
			if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)){
				voucher_Ids = voucherIds.split(",");
				//先校验财富券
				JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
				if (!jsonView.isSuccess()){
					request.setAttribute("errorMsg",jsonView.getInfo());
					return "/order/payError";
				}
			}

			//校验-购买金额加和是否等于订单金额
			if (lendOrder.getBuyBalance().compareTo(accountPayValue.add(voucherPayValue)) != 0) {
				request.setAttribute("errorMsg", BidErrorCode.AMOUNT_ILLEGAL.getDesc());
				return "/order/payError";
			}

			payResult = lendProductService.payFinanceByAccountBalanceWeb(currentUser.getUserId(), lendOrderId, rateUser, rateProduct, voucher_Ids);
		}

		request.setAttribute("payResult", payResult);
		request.setAttribute("lendOrder", lendOrder);
		//根据执行结果跳转不同页面
		if (payResult.isPayResult()&&payResult.isProcessResult())
			return "/order/paySuccess";

		request.setAttribute("errorMsg", payResult.getFailDesc());
		return "/order/payError";
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
		if (!LoanApplicationStateEnum.BIDING.getValue().equals(loanApplicationListVO.getApplicationState())){
			throw new SystemException(BidErrorCode.BID_STATIS_NOT_BIDDING).set("status", loanApplicationListVO.getApplicationState());
		}
		//预热标验证
		if (!loanApplicationListVO.isBegin()){
			throw new SystemException(BidErrorCode.BID_STATIS_NOT_SELLING).set("begin",loanApplicationListVO.isBegin());
		}

		if(currentUser!=null){
			//获取账户余额
			if(lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getRemain())>0){
				return JsonView.JsonViewFactory.create().success(false).info("金额已超出当前最大可投金额！")
						.toJson();
			}
			BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
			if(lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy))>0){
				//无法购买剩余标
				JsonView.JsonViewFactory.create().success(false).info("金额已超出当前最大可投金额！")
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
			return JsonView.JsonViewFactory.create().success(false).info("金额已超出当前最大可投金额！")
				.toJson();


		return JsonView.JsonViewFactory.create().success(true).toJson();

	}

	private void checkBidLoanByPay(LoanApplicationListVO loanApplicationListVO, BigDecimal amount, UserInfo currentUser) {

		HttpServletRequest request = this.getRequest();
		UserInfo userByUserId = this.userInfoService.getUserByUserId(currentUser.getUserId());
		//校验身份认证状态
		if (!this.userInfoService.hasIdentityAuthentication(userByUserId.getUserId()))
			throw new SystemException(UserErrorCode.HAS_NOT_IDVERIFIED).set("userId", userByUserId.getUserId());

		//标的状态验证
		if (!LoanApplicationStateEnum.BIDING.getValue().equals(loanApplicationListVO.getApplicationState())){
			throw new SystemException(BidErrorCode.BID_STATIS_NOT_BIDDING).set("status", loanApplicationListVO.getApplicationState());
		}
		//预热标验证
		if (!loanApplicationListVO.isBegin()){
			throw new SystemException(BidErrorCode.BID_STATIS_NOT_SELLING).set("begin",loanApplicationListVO.isBegin());
		}

		//验证购买金额是否合法(不大于0的情况)
		if (amount.compareTo(BigDecimal.ZERO) <= 0){
			throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
		}

		//校验金额是否合法
		loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());
		HttpSession session = WebUtil.getHttpServletRequest().getSession();

		UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);
		if(user!=null){
			//计算账户已投金额
			BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(user.getUserId(), loanApplicationListVO.getLoanApplicationId());
			if (totalLendAmountPerson != null) {
				if(loanApplicationListVO.getMaxBuyBalance()==null){
					loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
				}else{
					loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
				}
			}
		}

		if(amount.compareTo(loanApplicationListVO.getMaxBuyBalanceNow())>0)
			throw new SystemException(BidErrorCode.LIMIT_AMOUNT_NOT_ENOUGH).set("amount", amount).set("maxBuyBalanceNow",loanApplicationListVO.getMaxBuyBalanceNow());

	}

	@RequestMapping(value = "checkCreditorRightsByPay")
	@ResponseBody
	public String checkCreditorRightsByPay(@RequestParam(value = "lendOrderId") Long lendOrderId) {

		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		LendOrder lendOrder = lendOrderService.findById(lendOrderId);
		// 校验-订单用户和当前用户必须相同
		if (!lendOrder.getLendUserId().equals(currentUser.getUserId())) {
			return JsonView.JsonViewFactory.create().success(false).info(PayErrorCode.NOT_BELONG_PAY_ORDER.getDesc()).toJson();
		}

		// 已经购买的债权金额
		CreditorRightsTransferApplication crta = creditorRightsTransferAppService.getTransferApplicationByLendOrderId(lendOrderId);
		BigDecimal totalBuy = creditorRightsTransferAppService.getRemainingRightsPrice(crta.getCreditorRightsApplyId());

		// 剩余金额验证
		if (lendOrder.getBuyBalance().compareTo(crta.getApplyPrice().subtract(totalBuy)) > 0) {
			// 购买金额变为剩余可投金额
			if (crta.getApplyPrice().compareTo(totalBuy) == 0) {
				return JsonView.JsonViewFactory.create().success(false).info(CreditorErrorCode.CREDITOR_AMOUNT_ZONE.getDesc()).toJson();
			}
			return JsonView.JsonViewFactory.create().success(false).info(CreditorErrorCode.CREDITOR_RESIDUAL_BUY_TOO_MUCH.getDesc()).toJson();
		}
		return JsonView.JsonViewFactory.create().success(true).toJson();
	}


	/**
	 * 投标列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLender")
	@ResponseBody
	@DoNotNeedLogin
	public Object getLender(HttpServletRequest request,Long loanApplicationId,
							@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
							@RequestParam(value = "pageNo",defaultValue = "1") int pageNo){
		//获取借款申请状态
		LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
		if ("345".indexOf(loanApplication.getApplicationState())!=-1){
			//发标中、放款审核中、待放款
			//查询出借订单明细表中 投标中 的状态数据
			Pagination<LenderRecordVO> result = lendOrderBidDetailService.findLendOrderDetailPaging(pageNo, pageSize,loanApplicationId, LendOrderBidStatusEnum.BIDING, null);
			return result;
		}else{
			//6、7、8 还款中、已结清、已结清(提前还贷)
			//到债权表查询  已生效，还款中 状态的数据
//			Pagination<LenderRecordVO> result_1 = creditorRightsService.findLendOrderDetailPaging(pageNo, pageSize, pageNo, pageSize,
//					loanApplicationId, CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE, CreditorRightsConstants.CreditorRightsStateEnum.COMPLETE, CreditorRightsConstants.CreditorRightsStateEnum.EARLYCOMPLETE);
//			return result_1;
			Pagination<LenderRecordVO> result = lendOrderBidDetailService.findLendOrderDetailPaging(pageNo, pageSize,loanApplicationId, LendOrderBidStatusEnum.BIDSUCCESS, null);
			return result;
		}
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
	public Object getCreditorRightsLender(HttpServletRequest request, Long creditorRightsApplyId,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
		Pagination<LenderRecordVO> result = lendOrderBidDetailService.getCreditorRightsLenderPaging(pageNo, pageSize, creditorRightsApplyId);
		return result;
	}

	/**
	 * 汽车列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getCardList")
	@ResponseBody
	@DoNotNeedLogin
	public Object getCardList(HttpServletRequest request,Long loanApplicationId,
							@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
							@RequestParam(value = "pageNo",defaultValue = "1") int pageNo) {
		//获取借款申请状态
		EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot = enterpriseCarLoanSnapshotService.getByloanApplicationId(loanApplicationId);
		if (enterpriseCarLoanSnapshot==null)
			return null;
		MortgageCarSnapshot mortgageCarSnapshot = new MortgageCarSnapshot();
		mortgageCarSnapshot.setCarLoanId(enterpriseCarLoanSnapshot.getEnterpriseCarLoanId());
		return	mortgageCarSnapshotService.getMortgageCarSnapshotPaging(pageNo, pageSize, mortgageCarSnapshot, null);

	}
	/**
	 * 验证交易密码
	 * @param bidPass
	 * @return
	 */
	@RequestMapping(value = "checkBidLoanByAccountBalance")
	@ResponseBody
	public String checkBidLoanByAccountBalance(HttpServletRequest request,String bidPass){
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
		if (amount.remainder(new BigDecimal("100")).longValue()!=0){
			throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("financeId", lendProduct.getLendProductId());
		}
		//校验省心计划产品状态
		if (lendProductPublish.getPublishState() != LendProductPublishStateEnum.SELLING.value2Char())
			throw new SystemException(BidErrorCode.FINANACE_STATUS_NOT_SELLING).set("financePublishId", lendProductPublish.getLendProductPublishId());
	}

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
		if (amount.compareTo(BigDecimal.ZERO) <= 0){
			throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
		}

		//校验金额是否合法
		LendProduct financeProduct = lendProductService.findById(lendProductPublish.getLendProductId());
		if (amount.subtract(financeProduct.getStartsAt()).remainder(financeProduct.getUpAt()).compareTo(BigDecimal.ZERO)!=0)
			throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("financeId", financeProduct.getLendProductId())
					.set("financePublishId", lendProductPublish.getLendProductPublishId()).set("amount", amount)
					.set("startsAt", financeProduct.getStartsAt()).set("upAt", financeProduct.getUpAt());

		//100倍数验证
		if (amount.remainder(new BigDecimal("100")).longValue()!=0){
			throw new SystemException(BidErrorCode.AMOUNT_ILLEGAL).set("amount", amount);
		}

		//校验省心计划产品状态
		if (lendProductPublish.getPublishState() != LendProductPublishStateEnum.SELLING.value2Char())
			throw new SystemException(BidErrorCode.FINANACE_STATUS_NOT_SELLING).set("financePublishId", lendProductPublish.getLendProductPublishId());

		//校验账户余额
		UserAccount cashAccount = userAccountService.getCashAccount(userByUserId.getUserId());
		if (cashAccount.getAvailValue2().compareTo(amount) < 0)
			throw new SystemException(UserAccountErrorCode.ACCOUNT_BALANCE_AVAIVALUE_INSUFFICIENT).set("needAmount", amount).set("userAvailAmount", cashAccount.getAvailValue2()).set("userId", userByUserId.getUserId());
	}

	@DoNotNeedLogin
	@RequestMapping(value = "/toFinanceDetail")
	public String toFinanceDetail( HttpServletRequest req,LendProductPublish lpp) {
		if(lpp.getLendProductPublishId() == null){
			return "error";
		}
		LproductWithBalanceStatus financeDetail = this.lendProductService.findFinanceProductDetailForWeb(lpp.getLendProductPublishId());
		if(financeDetail == null){
			return "error";
		}
		UserInfo user = (UserInfo) req.getSession().getAttribute(Constants.USER_ID_IN_SESSION);
		if(null!=user){
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
		req.setAttribute("token",token);

		req.setAttribute("limit", financeDetail.getTimeLimit());
		req.setAttribute("financeDetail", financeDetail);
		req.setAttribute("buySituation", buySituation);
		req.setAttribute("historyBuySituation", historyBuySituation);

		return "/finance/financeDetail";
	}

	/**
	 * 出借列表
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/loanList")
	@ResponseBody
	public Object loanList(HttpServletRequest request,
						   LoanApplicationListVO loanApplicationListVO,
						   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
						   @RequestParam(value = "pageNo",defaultValue = "1") int pageNo){

		Pagination<LoanApplicationListVO> loanApplicationListPaging = loanApplicationService.getLoanApplicationPaging(pageNo, pageSize, loanApplicationListVO, null);
		return loanApplicationListPaging;
	}

	@DoNotNeedLogin
	@RequestMapping(value = "/getExpectProfit")
	@ResponseBody
	public Object getExpectProfit(HttpServletRequest request,
						  Long loanApplicationId,BigDecimal amount){
		LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);
		LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
		BigDecimal profit = BigDecimal.ZERO;
		BigDecimal award = BigDecimal.ZERO;
		LoanPublish loanPublish = loanPublicService.findById(loanApplication.getLoanApplicationId());
		if (loanPublish.getAwardRate()!=null){
			try {
				award = InterestCalculation.getAllInterest(amount,loanPublish.getAwardRate(),loanProduct.getDueTimeType(),
						loanProduct.getRepaymentMethod(),loanProduct.getRepaymentType(),loanProduct.getRepaymentCycle(),loanProduct.getDueTime(),loanProduct.getCycleValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
			award = BigDecimalUtil.down(award,2);
		}
		try {
			profit = InterestCalculation.getAllInterest(amount,loanApplication.getAnnualRate(),loanProduct.getDueTimeType(),
                    loanProduct.getRepaymentMethod(),loanProduct.getRepaymentType(),loanProduct.getRepaymentCycle(),loanProduct.getDueTime(),loanProduct.getCycleValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		profit = BigDecimalUtil.down(profit,2);
		if (award.compareTo(BigDecimal.ZERO)>0)
			return profit+","+award+"";
		else
			return profit.toString();
	}
	/**
	 * 债权的预期收益
	 * @param request
	 * @param loanApplicationId
	 * @param amount
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/getExpectRightProfit")
	@ResponseBody
	public Object getExpectRightProfit(Long creditorRightsApplyId, BigDecimal amount) {
		String result = creditorRightsService.getExpectRightProfit(creditorRightsApplyId,amount);
		return result;
	}

	/**
	 * 投标详情页
	 * @param request
	 * @param loanApplicationNo
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/bidding")
	public String bidding(HttpServletRequest request,@RequestParam(value = "loanApplicationNo", required = false)Long loanApplicationNo){
		if(loanApplicationNo==null){
			//非法请求
			return "error";
		}
		LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
		if (loanApplicationListVO==null){
			//非法请求
			return "error";
		}

		//标的状态验证
		if (LoanApplicationStateEnum.DELETED.getValue().equals(loanApplicationListVO.getApplicationState())
				|| LoanApplicationStateEnum.FAILURE.getValue().equals(loanApplicationListVO.getApplicationState())){
			throw new SystemException(BidErrorCode.BID_STATIS_NOT_BIDDING).set("status", loanApplicationListVO.getApplicationState());
		}


		//发标信息
		LoanPublish loanPublish = loanPublicService.findLoanPublishVO(loanApplicationNo);
		request.setAttribute("loanPublish", loanPublish);
		if (loanPublish==null) {
			//非法请求
			return "error";
		}

//		if (!PublishTarget.FRONT.getValue().equals(loanPublish.getPublishTarget()) && !PublishTarget.FRONT2BACKGROUND.getValue().equals(loanPublish.getPublishTarget()) ){
//			//非法请求
//			return "error";
//		}
		//担保公司
		if (loanPublish.getCompanyId()!=null){
			GuaranteeCompany company = guaranteeCompanyService.getGuaranteeCompanyByCompanyId(loanPublish.getCompanyId());
			request.setAttribute("guaranteeCompany",company);
		}

		//开标时间
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
		request.setAttribute("token",token);
		//组织页面返回值
		request.setAttribute("repayMentMethod", PaymentMethodEnum.values());
		request.setAttribute("guaranteeType", GuaranteeTypeEnum.values());
		//用户是否登陆
		loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());

		HttpSession session = WebUtil.getHttpServletRequest().getSession();
		UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);

		//标的状态REPAYMENTING("6","还款中"), COMPLETED("7","已结清"), EARLYCOMPLETE("8","已结清(提前还贷)"),
	    //增加还款信息展示
	    //未还款时计算还款信息
		//已还本息
		BigDecimal hasPaidBalance = BigDecimal.ZERO ;
		//待还本息
		BigDecimal waitPaidBalance = BigDecimal.ZERO ;
		Map<Object,String> stateMap = new HashMap<>();
		for(RepaymentPlanStateEnum r : RepaymentPlanStateEnum.values()){
			stateMap.put(r.value2Char(), r.getDesc());
		}
		request.setAttribute("stateMap", stateMap);

		if (LoanApplicationStateEnum.REPAYMENTING.getValue().equals(loanApplicationListVO.getApplicationState())
			|| LoanApplicationStateEnum.COMPLETED.getValue().equals(loanApplicationListVO.getApplicationState())
			|| LoanApplicationStateEnum.EARLYCOMPLETE.getValue().equals(loanApplicationListVO.getApplicationState())){
			List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplicationNo,ChannelTypeEnum.ONLINE);
			if(repaymentPlanList!=null &&repaymentPlanList.size()>0){
				for (RepaymentPlan plan : repaymentPlanList){
					hasPaidBalance = hasPaidBalance.add(plan.getFactBalance());
					if(plan.getPlanState() == RepaymentPlanStateEnum.BEFORE_COMPLETE.value2Char()){
						waitPaidBalance = waitPaidBalance.add(plan.getShouldCapital2().subtract(plan.getFactCalital()));
					}else if(plan.getPlanState() == RepaymentPlanStateEnum.COMPLETE.value2Char()){

					}else{
						waitPaidBalance = waitPaidBalance.add(plan.getShouldBalance2().subtract(plan.getFactBalance()));
					}
				}
			}
			waitPaidBalance = BigDecimalUtil.up(waitPaidBalance, 2);
			request.setAttribute("showRepaypaymentList", repaymentPlanList);
			request.setAttribute("waitPaidBalance", waitPaidBalance);
			request.setAttribute("hasPaidBalance", hasPaidBalance);
			request.setAttribute("isRepaying", true);
		}else{
			LoanProduct product = loanProductService.findById(loanApplicationListVO.getLoanProductId());
			LoanApplication loanApp = loanApplicationService.findById(loanApplicationListVO.getLoanApplicationId());
			List<RepaymentPlan> repaymentList = new ArrayList<>();
			try {
				repaymentList = loanApplicationService.getRepaymentPLanData(product, loanApp);
			} catch (Exception e) {
				logger.error("查询异常",e);
				e.printStackTrace();
			}
			 for(RepaymentPlan plan : repaymentList){
				 waitPaidBalance = waitPaidBalance.add(plan.getShouldBalance2());
			 }
			request.setAttribute("showRepaypaymentList", repaymentList);
			waitPaidBalance = BigDecimalUtil.up(waitPaidBalance, 2);
			request.setAttribute("waitPaidBalance", waitPaidBalance);
			request.setAttribute("hasPaidBalance", hasPaidBalance);
			request.setAttribute("isRepaying", false);
		}


		if(user!=null){
			//获取账户余额
			UserAccount cashAccount = userAccountService.getCashAccount(user.getUserId());
			//获取是否为定向用户

			UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
			request.setAttribute("userExt", userExt);
			request.setAttribute("cashAccount", cashAccount);
			//计算账户已投金额
			BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(user.getUserId(), loanApplicationListVO.getLoanApplicationId());
			if (totalLendAmountPerson != null) {
				if(loanApplicationListVO.getMaxBuyBalance()==null){
					loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
				}else{
					loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
				}
			}

			//个人财富券统计
			List<VoucherVO> useageList = voucherService.getAllVoucherList(user.getUserId(), null, VoucherConstants.UsageScenario.LOAN, VoucherConstants.UsageScenario.WHOLE);
			if(useageList!=null) {
				request.setAttribute("useageCount", useageList.size());
				BigDecimal useageSum = BigDecimal.ZERO;
				for (VoucherVO vo : useageList){
					useageSum = useageSum.add(vo.getVoucherValue());
				}
				request.setAttribute("useageSum",useageSum);

				List<VoucherProductVO> voucherProductVOs = voucherService.getVoucherStatistics(user.getUserId(),VoucherConstants.UsageScenario.LOAN, VoucherConstants.UsageScenario.WHOLE);
				request.setAttribute("voucherProductVOs",voucherProductVOs);
			}

		}
		request.setAttribute("loanApplicationListVO",loanApplicationListVO);
		//附件快照
		List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getcustomerUploadAttachment(loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());
		request.setAttribute("customerUploadSnapshots",customerUploadSnapshots);

		/**
		 * 王亚东修改验证 是否为定向标
		 */
		 String [] view={"",""};
		int type =loanApplicationService.getLoanApplicationType(loanApplicationNo);
		request.getSession().setAttribute("applicationId", loanApplicationNo);
		if(type==1){//定向密码
			view[0]="/finance/financeBiddingPass";
			view[1]= "/finance/financeEnterpriseBiddingPass";
			return whichViewReturnBySpecialBadding(loanApplicationListVO.getLoanType(),request, loanApplicationListVO, loanPublish,view);

		}else if(type==2){
			request.setAttribute("otype",type);
			if(user!=null){
				int userCount =userInfoService.normalOrOrienta(user.getUserId(),loanApplicationNo);
				request.setAttribute("userCount", userCount);
				view[0]="/finance/financeBiddingOrient";
				view[1]= "/finance/financeEnterpriseBiddingOrient";
				return whichViewReturnBySpecialBadding(loanApplicationListVO.getLoanType(),request, loanApplicationListVO, loanPublish,view);
			}else{
				request.setAttribute("userCount", 0);
				return "/finance/financeBiddingOrient";
			}
		}else if(type==3){
			//定向新手用户
			//根据当前用户查询是否为没有投资过的新用户
			request.setAttribute("otype", type);
			if(user!=null){
				String [] querys={"1","2"
						,"5","6"};
				int mCount =	lendOrderService.countMakedLoan(user.getUserId(),querys);
				if(mCount==0){//是新手用户
					request.setAttribute("newUserChoice", 0);
				}else{
					request.setAttribute("newUserChoice", 1);
				}
			}

			view[0]="/finance/financeBiddingNewUser";
			view[1]= "/finance/financeEnterpriseBiddingNewUser";
			 //返回新手用户
			return whichViewReturnBySpecialBadding(loanApplicationListVO.getLoanType(),request, loanApplicationListVO, loanPublish,view);
		}
		//省心计划匹配时间计算(开始)
		long time=60;//倒计时时间 秒
		int second=0;
		if(PublishRule.FIRST_AUTOMATIC.getValue().equals(loanPublish.getPublishRule())){//只有省心优先才倒计时
			Long otime=loanApplicationListVO.getOtime().getTime();
			Date now = new Date();
			if(otime.compareTo(now.getTime()) < 0){
				if((now.getTime()-otime)/1000<time){
					second=(int)(time-(now.getTime()-otime)/1000);
				}
			}
		}
		request.setAttribute("second",second);
		//省心计划匹配时间计算(结束)
		view[0]="/finance/financeBidding";
		view[1]= "/finance/financeEnterpriseBidding";
		return whichViewReturnBySpecialBadding(loanApplicationListVO.getLoanType(),request, loanApplicationListVO, loanPublish,view);
	}



	/**
	 * 企业投标详情页
	 * @param request
	 * @param loanApplicationListVO
	 * @param loanPublish
	 */
	private void enterpriseLoanApplication(HttpServletRequest request, LoanApplicationListVO loanApplicationListVO, LoanPublish loanPublish) {
		//借款企业
		EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
		EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
		request.setAttribute("enterpriseInfo",enterpriseInfo);
		//企业图
		List<EnterpriseUploadSnapshotVO> enterpriseInfoSnapshots = enterpriseInfoService.getEnterpriseUploadAttachment(enterpriseInfo.getEnterpriseId(), AttachmentIsCode.IS_CODE.getValue());
		request.setAttribute("enterpriseInfoSnapshots",enterpriseInfoSnapshots);
		switch (loanApplicationListVO.getLoanType()){
			case "2":
				//企业车贷
				EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot = enterpriseCarLoanSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				String address = getAddress(enterpriseCarLoanSnapshot.getProvince(),enterpriseCarLoanSnapshot.getCity());
				BigDecimal totalPrice= mortgageCarSnapshotService.getTotalPriceByCardLoanId(enterpriseCarLoanSnapshot.getEnterpriseCarLoanId());
				request.setAttribute("totalPrice",totalPrice);
				request.setAttribute("address",address);
				request.setAttribute("enterpriseCarLoanSnapshot",enterpriseCarLoanSnapshot);
				break;
			case "3":
				//企业信贷
				EnterpriseCreditSnapshot enterpriseCreditSnapshot = enterpriseCreditSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				address = getAddress(enterpriseCreditSnapshot.getProvince(),enterpriseCreditSnapshot.getCity());
				request.setAttribute("enterpriseCarLoanSnapshot",enterpriseCreditSnapshot);
				request.setAttribute("address",address);
				break;
			case "4":
				//企业保理
				EnterpriseFactoringSnapshot factoringSnapshot = enterpriseFactoringSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				request.setAttribute("factoringSnapshot",factoringSnapshot);
				//融资方
				CoLtd financeParty = coLtdService.getCoLtdById(factoringSnapshot.getFinancingParty());
				request.setAttribute("financeParty",financeParty);
				break;
			case "5":
				//企业基金
				EnterpriseFoundationSnapshot foundationSnapshot = enterpriseFoundationSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				request.setAttribute("foundationSnapshot",foundationSnapshot);
				//托管机构
				CoLtd coltd = coLtdService.getCoLtdById(foundationSnapshot.getCoId());
				request.setAttribute("coltd",coltd);
				//标的详情说明
				Attachment attachment = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getAttachId());
				request.setAttribute("attachment",attachment);
				//交易说明书
				Attachment tradeBook = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getTradeBookId());
				request.setAttribute("tradeBook",tradeBook);
				//风险提示函
				Attachment riskTip = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getRiskTipId());
				request.setAttribute("riskTip",riskTip);
				break;
			case "6":
				//企业标（质押标）
				EnterprisePledgeSnapshot enterprisePledgeSnapshot = enterprisePledgeSnapshotService.getByloanApplicationId(loanApplicationListVO.getLoanApplicationId());
				address = getAddress(enterprisePledgeSnapshot.getProvince(),enterprisePledgeSnapshot.getCity());
				request.setAttribute("enterpriseCarLoanSnapshot",enterprisePledgeSnapshot);
				request.setAttribute("address",address);
				break;
		}

	}

	private String getAddress(Long provinceId,Long cityId) {
		ProvinceInfo province =null ;
		CityInfo city = null;
		String address = "";
		if (provinceId!=null) {
            province = provinceInfoService.findById(provinceId);
        }
		if (cityId!=null){
            city = cityInfoService.findById(cityId);
        }
		if (province==null) return null;

		if (city==null)
			return province.getProvinceName();

		if (city.getCityName().equals(province.getProvinceName())){
			address = city.getCityName();
		}else {
			address = province.getProvinceName()+city.getCityName();
		}
		return address;
	}


	/**
	 * 个人投标详情页
	 * @param request
	 * @param loanApplicationListVO
	 * @param loanPublish
	 */
	private void personLoanApplication(HttpServletRequest request, LoanApplicationListVO loanApplicationListVO, LoanPublish loanPublish) {
		//借款人基本信息
		CustomerBasicSnapshot basicSnapshot = customerBasicSnapshotService.getBasicByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
		if(basicSnapshot!=null){
			request.setAttribute("basicSnapshot",basicSnapshot);
			//居住地
			Address adress = addressService.getAddressVOById(basicSnapshot.getResidenceAddr());
			request.setAttribute("adress",adress);
		}
		if(LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())){
			CustomerCarSnapshot basicInfoForPeopleAndCar = customerCarSnapshotService.getCarByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
			request.setAttribute("basicInfoForPeopleAndCar", basicInfoForPeopleAndCar);
		//	request.setAttribute("basicInfoForPeopleAndCarType", basicInfoForPeopleAndCar.getPledgeType().equals("1") ? "一抵" : "二抵");
		}else{
			//房产抵押
			CustomerHouseSnapshot house = customerHouseSnapshotService.getHouseByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
			request.setAttribute("house",house);
			request.setAttribute("loanPublish",loanPublish);
			Address houseAdress = addressService.getAddressVOById(loanPublish.getHourseAddress());
			request.setAttribute("houseAdress",houseAdress);
		}

		//认证报告
		String authInfos = loanPublish.getAuthInfos();
		if(StringUtils.isNotEmpty(authInfos)){
			request.setAttribute("authInfo",authInfos.split(","));
		}
	}

	@DoNotNeedLogin
	@RequestMapping(value = "/front_bidding")
	public String front_bidding(HttpServletRequest request,Long loanApplicationNo){
		if(loanApplicationNo==null){
			//非法请求
			return "error";
		}
		LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(loanApplicationNo);
		if (loanApplicationListVO==null){
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
		request.setAttribute("token",token);
		//组织页面返回值
		request.setAttribute("repayMentMethod", PaymentMethodEnum.values());
		request.setAttribute("guaranteeType", GuaranteeTypeEnum.values());
		//用户是否登陆
		loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());
		HttpSession session = WebUtil.getHttpServletRequest().getSession();

		UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);
		if(user!=null){
			//获取账户余额
			UserAccount cashAccount = userAccountService.getCashAccount(user.getUserId());
			UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
			request.setAttribute("userExt", userExt);
			request.setAttribute("cashAccount", cashAccount);
			//计算账户已投金额
			BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(user.getUserId(), loanApplicationListVO.getLoanApplicationId());
			if (totalLendAmountPerson != null) {
				if(loanApplicationListVO.getMaxBuyBalance()==null){
					loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
				}else{
					loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
				}
			}
		}
		//发标信息
		LoanPublish loanPublish = loanPublicService.findLoanPublishVO(loanApplicationNo);

		request.setAttribute("loanApplicationListVO",loanApplicationListVO);
		//附件快照
		List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getcustomerUploadAttachment(loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());
		request.setAttribute("customerUploadSnapshots",customerUploadSnapshots);
		if(LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanApplicationListVO.getLoanType())
				|| LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
				|| LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
				|| LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())){
			//个人投标详情页
			personLoanApplication(request, loanApplicationListVO, loanPublish);
			return "/finance/front_financeBidding";
		}else{
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
	 * 跳转到:我的理财-省心计划列表页
	 * @return
	 */
	@RequestMapping(value = "/toAllMyFinanceList")
	public String toAllMyFinanceList(HttpServletRequest request){
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
		for(LendOrder finance : financeOrderList){
			totalValue = totalValue.add(finance.getBuyBalance().add(finance.getCurrentProfit()));
			currentProfit = currentProfit.add(finance.getCurrentProfit());
			// 状态为理财中或者授权期结束的
			if(FinanceOrderStatusEnum.REPAYMENTING.getValue().equals(finance.getOrderState())
					|| FinanceOrderStatusEnum.QUITING.getValue().equals(finance.getOrderState())){
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

		return "/finance/myFinanceList";
	}

	/**
	 * 我的理财-省心计划的列表数据
	 * @param request
	 * @param pageSize
	 * @param pageNo
	 * @param queryState 省心计划状态（已支付0，理财中1，已结束2）
	 * @param queryType 计划类型（省心期数值）
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
	 * 跳转到：我的省心计划的详情
	 * @param request
	 * @param lendOrderId 出借订单ID
	 * @return
	 */
	@RequestMapping(value = "/getAllMyFinanceListDetail")
	public ModelAndView getAllMyFinanceListDetail(HttpServletRequest request, Long lendOrderId) {

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
		BigDecimal waitBackAward = BigDecimalUtil.down(totalExpectAward.subtract(awardBalance),2);

		// 查询某条省心计划，已经匹配到子标的出借总金额
		BigDecimal allBuyBalance = userAccountService.getUserFinancingAccountValueByAccId(financeAccount.getAccId());
		allBuyBalance = allBuyBalance.add(waitBackAward);

		//【统计值】理财中金额（元）（在投资金+在投预期收益）
		mv.addObject("financeAccountValue", BigDecimalUtil.down(allBuyBalance,2));
		//【统计值】预期年化收益率范围
		mv.addObject("profitRate", loep.getProfitRate());
		mv.addObject("profitRateMax", loep.getProfitRateMax());
		//【统计值】省心期（月）
		mv.addObject("timeLimit", loep.getTimeLimit());
		//【统计值】在投资金预期收益（元）+ 待回奖励
		mv.addObject("expectProfit", BigDecimalUtil.down(expectProfit.add(waitBackAward),2));
		//【统计值】已获收益（元）+ 已获奖励
		mv.addObject("currentProfit", BigDecimalUtil.down(loep.getCurrentProfit2().add(awardBalance),2));
		//【统计值】购买金额（元）
		mv.addObject("buyBalance", BigDecimalUtil.down(loep.getBuyBalance(),2));
		//【统计值】待出借金额（元）
		mv.addObject("availValue", BigDecimalUtil.down(financeAccount.getAvailValue2().add(financeAccount.getFrozeValue2()),2));
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
	 * @param lendOrderId 省心订单ID
	 * @return
	 */
	private BigDecimal getTotalExpectAward(Long lendOrderId){
		BigDecimal totalAward = BigDecimal.ZERO;
		try {
			List<CreditorRights> creditorRightList = creditorRightsService.getCreditorRightsByLendOrderPid(lendOrderId);
			if(creditorRightList != null){
				for (CreditorRights cr : creditorRightList) {
					if(CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char() == cr.getRightsState()// 已生效
						||  CreditorRightsConstants.CreditorRightsStateEnum.APPLYTURNOUT.value2Char() == cr.getRightsState()//申请转出
						||  CreditorRightsConstants.CreditorRightsStateEnum.TRANSFERING.value2Char() == cr.getRightsState()){//转让中
						totalAward = totalAward.add(creditorRightsService.getExpectAward(cr.getLoanApplicationId(), cr.getBuyPrice()));//算预期的奖励
					}
				}
			}
			totalAward = totalAward.add(userAccountService.getUserTotalFinanceAwardByLendOrderId(lendOrderId));
		} catch (Exception e) {
			logger.error("【错误】获取总的预期投标奖励,lendOrderId=" + lendOrderId + ",message=" + e.getMessage(),e);
		}
		return totalAward;
	}


	/**
	 * 债券列表
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
    		@RequestParam(value = "lendOrderId", required=false) Long lendOrderId) {

        return lendOrderService.findCreditorRightsByDetailList(pageSize, pageNo, lendOrderId);
    }

	/**
	 * 跳转至出借债券页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toMyCreditRightList")
	public String toCreditRightList(HttpServletRequest request,ModelMap map) {
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		if(currentUser==null){
			throw new SystemException(SystemErrorCode.SESSION_NOT_EXISIT);
		}
		CreditorRightsCount count=creditorRightsService.selectUserRightsByDaiHui(currentUser.getUserId());
		map.put("count", count);
		return "/finance/myCreditRightList";
	}

	@DoNotNeedLogin
	@RequestMapping(value = "/myCreditRightAll")
	public String myCreditRightAll(HttpServletRequest request,ModelMap map,String creditorRightsStatus,String backDate,String buyDate,String userId) {
		CreditorRightsExtVo vo = new CreditorRightsExtVo();
		vo.setLendUserId(Long.parseLong(userId));
		vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上
		Map<String, Object> customParams = new HashMap<String, Object>();
		customParams.put("queryStatus",new String[]{"0","1"});
		customParams.put("orderBy",getOrderBy(backDate,buyDate));
		List<CreditorRightsExtVo> list = creditorRightsService.getUserAllCreditorRights(vo, customParams);
		map.put("list", list);
		CreditorRightsCount count=creditorRightsService.selectUserRightsByDaiHui(Long.parseLong(userId));
		map.put("count", count);
		return "/common/myCreditRightList";
	}

	@RequestMapping(value = "/downloadMyCreditRights")
	public void downloadMyCreditRights(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		if(currentUser==null){
			throw new SystemException(SystemErrorCode.SESSION_NOT_EXISIT);
		}
		String fileName = currentUser.getLoginName()+"_出借债权列表.pdf" ;
		File file = new File(PropertiesUtils.getInstance().get("PERSONAL_CREDITORRIGHTS") + fileName);
		try {
			CreditorRightsExtVo vo = new CreditorRightsExtVo();
			vo.setLendUserId(currentUser.getUserId());
			vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上
			Map<String, Object> customParams = new HashMap<>();
			customParams.put("queryStatus",new String[]{"0","1"});
			customParams.put("orderBy",getOrderBy(null,null));
			List<CreditorRightsExtVo> list = creditorRightsService.getUserAllCreditorRights(vo, customParams);
			CreditorRightsCount count=creditorRightsService.selectUserRightsByDaiHui(currentUser.getUserId());
			ConverterHtml2PDF converter = new ConverterHtml2PDF();
            //ConverterHtml2PDF converter = new ConverterHtml2PDF("/finance/myCreditRightAll?userId="+currentUser.getUserId());
			//converter.generatePDF(request,file,null);
			converter.generatePDF(file,getHtml(count,list));
			FileUtil.download(file, response);
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
			throw new SystemException(SystemErrorCode.IO_ERROR);
		}
	}

	private String formatNum(Object obj){
		if(null==obj||obj.toString().equals("")){
			return "";
		}
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(new BigDecimal(obj.toString()));
	}

	private String getHtml(CreditorRightsCount count,List<CreditorRightsExtVo> list){
		String html="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">  \n" +
				"<html xmlns=\"http://www.w3.org/1999/xhtml\"> \n" +
				"<head>\n" +
				"  \t<meta charset=\"utf-8\" />\n" +
				"  \t<title>出借债权 - 财富派</title>\n" +
				"  \t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
				"\t<meta name=\"renderer\" content=\"webkit|ie-comp|ie-stand\"></meta>\n" +
				"</head>\n" +
				"<style>\n" +
				"html, body, div, div, object, iframe,h1, h2, h3, h4, h5, h6, p, blockquote, pre,abbr, address, cite, code,del, dfn, em, img, ins, kbd, q, samp,small, strong, sub, sup, var,b, i,dl, dt, dd, ol, ul, li,fieldset, form, label, legend,table, caption, tbody, tfoot, thead, tr, th, td,article, aside, canvas, details, figcaption, figure, footer, header, hgroup, menu, nav, section, summary,time, mark, audio, video{margin:0;padding:0;border:0; outline:0;vertical-align:baseline;font-style: normal;}\n" +
				"html,ul,li,ol,p,h1,h2,h3,h4,h5,h6,dl,dt,dd{margin:0;padding:0;}\n" +
				"html{font-family:Tahoma,Arial,Roboto,\"Droid Sans\",\"Helvetica Neue\",\"Droid Sans Fallback\",\"Heiti SC\",sans-self,Microsoft YaHei;box-sizing:border-box;-webkit-font-smoothing:antialiased;text-shadow:1px 1px 1px rgba(0,0,0,0.004);}\n" +
				"li{list-style:none;\t}\n" +
				"a{text-decoration:none; }\n" +
				"img{border:none;}\n" +
				"html{width: 100%;height: 100%;background: #afafaf;}\n" +
				"body{width: 1200px;height: 100%;margin: 0 auto;background: #fff;color: #666;font-size: 12px;font-family:Microsoft YaHei; }\n" +
				".userInfo{width: 90%;color: #666;height: 12px;font-size: 20px;padding: 10px 0;margin: 0 5%;}\n" +
				".formHeadmain{width: 100%;height: 60px;padding: 30px 0;background: #e6e6e6;overflow: hidden;}\n" +
				".formHeadmain div{width: 100px;text-align: center;display:block;float: left;}\n" +
				".formHeadmain .first{width: 400px;}\n" +
				".formHead{height: 60px;background: #fff;padding: 30px 0;}\n" +
				".formContent{width: 1160px;padding: 20px;height: auto;background: #f1f1f1;}\n" +
				".titleInfo{width: 900px;height: 12px;padding: 5px 0;}\n" +
				".titleInfo div{width: 300px;height: 12px;display: block;float: left;padding: 5px 0;}\n" +
				".formContent ul{width: 1160px;height: auto;margin:30px 0 10px 0;}\n" +
				".formContent ul li{width: 100%;height: 12px;padding: 20px 0;border-bottom: #afafaf;background: #fff;text-align: center;}\n" +
				".firstLi{background: #afafaf!important;}\n" +
				".formContent ul li div{width: 120px;display: block;float: left;}\n" +
				"</style>\n" +
				"<style type=\"text/css\">\n" +
				"*{\n" +
				"font-family:SongTi_GB2312;\n" +
				"}\n" +
				"</style>\n" +
				"<body>\n" +
				"\t<h5 class=\"userInfo\">\n" +
				"\t\t用户名："+count.getUserName()+"\n" +
				"\t\t回款中债权总数："+count.getCountNum()+"个\n" +
				"\t\t待回本金："+formatNum(count.getCapital())+"元\n" +
				"\t\t待回利息："+formatNum(count.getInterest())+"元 \n" +
				"\t</h5>\n" +
				"\t<div class=\"formHeadmain\">\n" +
				"\t\t<div class=\"first\">债权名称</div> \n" +
				"\t\t<div>借款人</div> \n" +
				"\t\t<div>出借金额(元</div> \n" +
				"\t\t<div>待收回款(元)</div>\n" +
				"\t\t<div>已回金额(元)</div> \n" +
				"\t\t<div>最近回款日</div> \n" +
				"\t\t<div>债权状态</div> \n" +
				"\t\t<div>投标时间</div>\n" +
				"\t\t<div>明细</div>\n" +
				"\t</div>\n";
				for(int i=0;i<list.size();i++) {
					String status="";
					switch (list.get(i).getRightsStateStr().toCharArray()[0]){
						case '0':
							status="已生效";
							break;
						case '1':
							status="还款中";
							break;
						case '2':
							status="已转出";
							break;
						case '3':
							status="已结清";
							break;
						case '4':
							status="已删除";
							break;
						case '5':
							status="申请转出";
							break;
						case '6':
							status="已转出(平台垫付)";
							break;
						case '7':
							status="提前结清";
							break;
						case '8':
							status="转让中";
							break;
					}
					String rateStr="";
					if((list.get(i).getFromWhere()+"").equals("1")||((list.get(i).getFromWhere()+"").equals("2")&&!list.get(i).getAwardPoint().equals("放款"))){
						if(!com.xt.cfp.core.util.StringUtils.isNull(list.get(i).getAwardRate())&&!com.xt.cfp.core.util.StringUtils.isNull(list.get(i).getAwardPoint())){
							rateStr=list.get(i).getAwardRate()+"<i class='borlisdeta'>注</i>";
						}
					}
					String rateTime="";
					if(!com.xt.cfp.core.util.StringUtils.isNull(list.get(i).getAwardPoint())){
						rateTime="<i class='borlisdeta2'>注</i>投标奖励金额于"+list.get(i).getAwardPoint()+"时，发放至您的账户，请注意查收。\n";
					}
					if(null!=list.get(i).getRateList()&&list.get(i).getRateList().size()>0&&!com.xt.cfp.core.util.StringUtils.isNull(list.get(i).getRateValue2())){
						rateTime+=list.get(i).getRateValue2();
					}
					if(null!=list.get(i).getRateList()&&list.get(i).getRateList().size()>0&&!com.xt.cfp.core.util.StringUtils.isNull(list.get(i).getRateValue())){
						rateTime+=list.get(i).getRateValue();
					}
					html+="\t\t<div class=\"formHeadmain formHead\">\n" +
							"\t\t\t<div class=\"first\">"+list.get(i).getCreditorRightsName()+"</div> \n" +
							"\t\t\t<div>"+list.get(i).getLoanRealName()+"</div>\n" +
							"\t\t\t<div>"+formatNum(list.get(i).getBuyPrice())+"</div> \n" +
							"\t\t\t<div>"+formatNum(list.get(i).getWaitTotalpayMent())+"</div> \n" +
							"\t\t\t<div>"+formatNum(list.get(i).getFactBalance())+"</div> \n" +
							"\t\t\t<div>"+DateUtil.getDateLong(list.get(i).getCurrentPayDate())+"</div>\n" +
							"\t\t\t<div>\n" +status+
							"\t\t\t</div> \n" +
							"\t\t\t<div>"+DateUtil.getDateLong(list.get(i).getBuyDate())+"</div> \n" +
							"\t\t\t<div></div>\n" +
							"\t\t</div>\n" +
							"\t\t<div class=\"formContent\">\n" +
							"\t\t\t<div class=\"titleInfo\">\n" +
							"\t\t\t\t<div>借款标题："+list.get(i).getLoanApplicationListVO().getLoanApplicationTitle()+"</div> \n" +
							"\t\t\t\t<div>借款时长："+list.get(i).getLoanApplicationListVO().getCycleCount()+"个月</div> \n" +
							"\t\t\t\t<div>\n" +
							"\t\t\t\t\t年化利率："+list.get(i).getLoanApplicationListVO().getAnnualRate()+"%\n" +rateStr+
							"\t\t\t\t</div>\n" +
							"\t\t\t</div>\n" +
							"\t\t\t<div class=\"titleInfo\">\n" +
							"\t\t\t\t<div>出借金额："+formatNum(list.get(i).getBuyPrice())+"元</div> \n" +
							"\t\t\t\t<div>预期收益："+formatNum(list.get(i).getExpectProfit())+"元</div> \n" +
							"\t\t\t\t<div>还款方式："+list.get(i).getLoanApplicationListVO().getRepayMentMethod()+"</div>\n" +
							"\t\t\t</div>\n" +
							"\t\t\t<p class=\"titleInfo\">\n" +rateTime+
							"\t\t\t</p>\n" +
							"\t\t\t<ul>\n" +
							"\t\t\t\t<li class=\"firstLi\">\n" +
							"\t\t\t\t\t<div>回款期</div> \n" +
							"\t\t\t\t\t<div>回款日期</div> \n" +
							"\t\t\t\t\t<div>应回本金(元)</div>\n" +
							"\t\t\t\t\t<div>应回利息(元)</div> \n" +
							"\t\t\t\t\t<div>罚息(元)</div> \n" +
							"\t\t\t\t\t<div>应回款总额(元)</div> \n" +
							"\t\t\t\t\t<div>已回款总额(元)</div>\n" +
							"\t\t\t\t\t<div>应缴费用(元)</div> \n" +
							"\t\t\t\t\t<div>状态</div>\n" +
							"\t\t\t\t</li>\n";
							for(int j=0;j<list.get(i).getYlist().length;j++) {
								Object[] ylist=(Object[]) list.get(i).getYlist()[j];
								html+="\t\t\t\t\t<li>\n" +
										"\t\t\t\t\t\t<div>"+ylist[0]+"</div> \n" +
										"\t\t\t\t\t\t<div>"+DateUtil.getDateLong((Date)ylist[1])+"</div> \n" +
										"\t\t\t\t\t\t<div>\n";
										if(ylist[2].toString().equals("---")){
											html+=ylist[2];
										}else{
											html+=formatNum(ylist[2]);
										}
								html+="\t\t\t\t\t\t</div> \n" +
										"\t\t\t\t\t\t<div>\n";
										if(ylist[3].toString().equals("---")){
											html+=ylist[3];
										}else{
											html+=formatNum(ylist[3]);
										}
								html+="\t\t\t\t\t\t</div>\n" +
										"\t\t\t\t\t\t<div>\n";
										if(ylist[4].toString().equals("---")){
											html+=ylist[4];
										}else{
											html+=formatNum(ylist[4]);
										}
								html+="\t\t\t\t\t\t</div> \n" +
										"\t\t\t\t\t\t<div>\n";
										if(ylist[6].toString().equals("---")){
											html+=ylist[6];
										}else{
											html+=formatNum(ylist[6]);
										}
								html+="\t\t\t\t\t\t</div> \n" +
										"\t\t\t\t\t\t<div>\n";
										if(ylist[7].toString().equals("---")){
											html+=ylist[7];
										}else{
											html+=formatNum(ylist[7]);
										}
								html+="\t\t\t\t\t\t</div> \n" +
										"\t\t\t\t\t\t<div>\n";
										if(ylist[5].toString().equals("---")){
											html+=ylist[5];
										}else{
											html+=formatNum(ylist[5]);
										}
								html+="\t\t\t\t\t\t</div>\n" +
										"\t\t\t\t\t\t<div>\n";
										switch(ylist[8].toString()){
											case "0":
												html+="未还款";
												break;
											case "1":
												html+="部分还款";
												break;
											case "2":
												html+="已还清";
												break;
											case "3":
												html+="逾期";
												break;
											case "4":
												html+="提前结清";
												break;
											case "5":
												html+="已转出";
												break;
											case "6":
												html+="平台垫付利息";
												break;
											case "8":
												html+="转让中";
												break;
										}
								html+="\t\t\t\t\t\t</div>\n" +
										"\t\t\t\t\t</li>\n";
							}
							html+="\t\t\t</ul>\n" +
									"\t\t</div>\n";
				}
				html+="</body>\n" +
						"\n" +
						"</html>";
		return html;
	}

	/**
	 * 分页查询列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getCreditRightList")
	@ResponseBody
	public Object getCreditRightList(HttpServletRequest request,@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
									 @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
									 String creditorRightsStatus,String backDate,String buyDate) {
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		CreditorRightsExtVo vo = new CreditorRightsExtVo();
		vo.setLendUserId(currentUser.getUserId());
		//vo.setFromWhere(CreditorRightsFromWhereEnum.BUY.value2Char());//购买来源
		vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上


		//封装参数
		Map<String, Object> customParams = new HashMap<String, Object>();

		String[] queryStatus = getQueryStatus(creditorRightsStatus);
		if(queryStatus!=null)
			customParams.put("queryStatus",queryStatus);
		customParams.put("orderBy",getOrderBy(backDate,buyDate));
		Pagination<CreditorRightsExtVo> creditorRightsPaging = creditorRightsService.getCreditorRightsPaging(pageNo, pageSize, vo, customParams);
		return creditorRightsPaging;
	}

	/**
	 * 我的理财-省心计划-出借明细 列表查询
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
	public Object getSXJHCreditorRightsDetailList(HttpServletRequest request,@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
									 @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
									 String creditorRightsStatus,String backDate,String buyDate,Long lendOrderId) {
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);

		CreditorRightsExtVo vo = new CreditorRightsExtVo();
		vo.setLendUserId(currentUser.getUserId());
		//vo.setFromWhere(CreditorRightsFromWhereEnum.BUY.value2Char());//购买来源
		vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上

		//封装参数
		Map<String, Object> customParams = new HashMap<String, Object>();
		customParams.put("lendOrderPid",lendOrderId);//省心计划出借订单id=债权明细父出借订单父id
		String[] queryStatus = getQueryStatus(creditorRightsStatus);
		if(queryStatus!=null){
			customParams.put("queryStatus",queryStatus);
		}
		customParams.put("orderBy",getOrderBy(backDate,buyDate));

		Pagination<CreditorRightsExtVo> creditorRightsPaging = creditorRightsService.getSXJHCreditorRightsDetailPaging(pageNo, pageSize, vo, customParams);
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

	@RequestMapping(value = "/toRePayForLender")
	public String  toRePayForLender(HttpServletRequest request,@RequestParam(value = "lendOrderId") Long lendOrderId){
		//todo 支付单失效
		PayOrder payOrder = payService.getCurrentUnpayPayOrderByLendOrderId(lendOrderId);
		payOrder.setStatus(PayConstants.OrderStatus.FAIL.getValue());
		payOrder.setProcessStatus(PayConstants.ProcessStatus.FAIL.getValue());
		payService.update(payOrder);
		return "redirect:/finance/toPayForLender?lendOrderId="+lendOrderId;
	}

	/**
	 *
	 * 投标类支付订单，再次支付时，跳到支付页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toPayForLender")
	public String toPayForLender(HttpServletRequest request,@RequestParam(value = "lendOrderId") Long lendOrderId){
		TokenUtils.setNewToken(request);
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
		LendOrder lendOrder = lendOrderService.findById(lendOrderId);
		//身份验证
		if (!userExt.getIsVerified().equals(UserIsVerifiedEnum.BIND.getValue())){
			throw new SystemException(UserErrorCode.HAS_NOT_BIND_HF).set("userId", currentUser.getUserId());
		}
		//校验-订单是否存在
		if (lendOrder==null) {
			throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderId", lendOrderId);
		}
		//校验-订单用户和当前用户必须相同
		if (!lendOrder.getLendUserId().equals(currentUser.getUserId())) {
			throw new SystemException(PayErrorCode.NOT_BELONG_PAY_ORDER).set("lendOrderUserId",lendOrder.getLendUserId()).set("user", currentUser.getUserId());
		}

		List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
		LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
		LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(lendOrderBidDetail.getLoanApplicationId());
		LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());
		String point=com.xt.cfp.core.util.StringUtils.isNull(loanApplicationListVO.getAwardPoint())?"":null;
    	if(point==null){
    		if(loanApplicationListVO.getAwardPoint().equals("1")){
    			point=AwardPointEnum.ATMAKELOAN.getDesc();
    		}else if(loanApplicationListVO.getAwardPoint().equals("2")){
    			point=AwardPointEnum.ATREPAYMENT.getDesc();
    		}else{
    			point=AwardPointEnum.ATCOMPLETE.getDesc();
    		}
    	}
		//奖励发放时机
    	loanApplicationListVO.setAwardPoint(point);
    	//计算奖励金额
    	if(loanApplicationListVO.getAwardRate()!=null&&!loanApplicationListVO.getAwardRate().equals("")&&!loanApplicationListVO.getAwardRate().equals("0")){
    		LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(lendOrderBidDetail.getLoanApplicationId());
        	LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
        	BigDecimal amount=lendOrder.getBuyBalance();
        	BigDecimal profit = BigDecimal.ZERO;
    		try {
    			profit = InterestCalculation.getAllInterest(amount,new BigDecimal(loanApplicationListVO.getAwardRate()),loanProduct.getDueTimeType(),
                        loanProduct.getRepaymentMethod(),loanProduct.getRepaymentType(),loanProduct.getRepaymentCycle(),loanProduct.getDueTime(),loanProduct.getCycleValue());
			} catch (Exception e) {
    			e.printStackTrace();
    		}
    		profit = BigDecimalUtil.down(profit,2);
    		request.setAttribute("awardProfit", profit);
    	}

    	loanApplicationListVO.setAwardRate(loanApplicationListVO.getAwardRate()!=null&&!loanApplicationListVO.getAwardRate().equals("")&&!loanApplicationListVO.getAwardRate().equals("0")?loanApplicationListVO.getAwardRate()+"%":"");

		LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());
		//校验业务数据
		checkBidLoanByPay(loanApplicationListVO, lendOrder.getBuyBalance(), currentUser);
		//检查剩余金额
		BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
		if(lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy))>0){
			//无法购买剩余标
			throw new SystemException(BidErrorCode.LENDORDER_AMOUNT_NOT_ENOUGH).set("amount", lendOrder.getBuyBalance()).set("remain",loanApplicationListVO.getConfirmBalance().subtract(totalBuy));
		}
		//预期收益
		LoanProduct loanProduct = loanProductService.findById(loanApplicationListVO.getLoanProductId());
		BigDecimal expectedInteresting = null;
		try {
			expectedInteresting = InterestCalculation.getAllInterest(lendOrder.getBuyBalance(), lendOrder.getProfitRate(), lendOrder.getTimeLimitType().charAt(0),
					loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
			expectedInteresting = BigDecimalUtil.down(expectedInteresting,2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//历史卡信息
		/*CustomerCard hisCustomerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(),PayConstants.PayChannel.YB);
		if(hisCustomerCard!=null){
			boolean isSupport = true;
			ConstantDefine bankInfo = constantDefineService.findById(hisCustomerCard.getBankCode());
			if (bankInfo.getConstantStatus().equals("1")){
				isSupport = false;
			}
			request.setAttribute("isSupport", isSupport);
		}*/
		//查询用户卡信息
		CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.HF);
		CgBank cgBank = cgBizService.getBankInfo(customerCard.getBankNum(), CgBank.IdTypeEnum.PERSON);
		customerCard.setBankCode(cgBank.getIconCode());

		//查询用户卡累计提现金额
		BigDecimal withdrawAmount = customerCard != null ? customerCardService.getCardWithdrawAmount(customerCard.getCustomerCardId()) : BigDecimal.ZERO;

		//todo 财富券实现
		List<VoucherVO> vouchers = voucherService.getAllVoucherList(currentUser.getUserId(),lendOrder.getBuyBalance(),VoucherConstants.UsageScenario.LOAN,VoucherConstants.UsageScenario.WHOLE);

		// 加息券
		List<RateUserVO> rateUsers = rateUserService.findRateUsersByUserId(currentUser.getUserId(), loanApplicationListVO.getCycleCount(), loanApplicationListVO.getLoanType(), lendOrder.getBuyBalance(), RateUserStatusEnum.UNUSED,
				RateUserStatusEnum.USING);

		request.setAttribute("order", lendOrder);
		request.setAttribute("loanApplication", loanApplicationListVO);
		request.setAttribute("withdrawAmount", withdrawAmount);
		request.setAttribute("amount", lendOrder.getBuyBalance());
		request.setAttribute("customerCard", customerCard);
		/*request.setAttribute("hisCustomerCard", hisCustomerCard);*/
		request.setAttribute("vouchers", vouchers);
		request.setAttribute("rateUsers", rateUsers);
		request.setAttribute("expectedInteresting",expectedInteresting);
		request.setAttribute("lendProduct", lendProduct);
		request.setAttribute("userExtInfo", userExt);
		request.setAttribute("lendProductPublish", lendProductPublish);
		request.setAttribute("userCashBalance", userAccountService.getCashAccount(currentUser.getUserId()).getAvailValue2());
		boolean isBidEqualLoginPass = userInfoService.isBidPassEqualLoginPass(currentUser.getUserId());
		request.setAttribute("isBidEqualLoginPass",isBidEqualLoginPass);
		 //获取网关充值银行卡列表
        /*List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByTypeNeedSort("gateway_bank");
        request.setAttribute("bankList", constantDefines);*/
		//		if (customerCard == null )
			return "order/noCardBidLoanPay";
		/*else
			return "order/hasCardBidLoanPay";*/
	}

	public String getOrderBy(String backDate,String buyDate){

		String orderBy = "";
		String str1 = StringUtils.isEmpty(backDate)?null:(backDate.equals("D")?"CURRENT_PAY_DATE desc":"CURRENT_PAY_DATE asc");
		String str2 = StringUtils.isEmpty(buyDate)?null:(buyDate.equals("D")?" LOBD.BUY_DATE  desc":" LOBD.BUY_DATE  asc");


		orderBy += str1==null?"":str1;
		orderBy += str2==null?"":(StringUtils.isEmpty(orderBy)?str2:","+str2);
		return orderBy;
	}

	private String[] getQueryStatus(String status){
		if(org.apache.commons.lang.StringUtils.isNotEmpty(status)){
			if(status.indexOf("0")!=-1){
				return null;
			}
			if(status.indexOf("1-2-3")!=-1){
				return null;
			}
			if(status.indexOf("1")!=-1){
				status = status.replace("1","0-1");
			}
			if (status.indexOf("2")!=-1){
				status = status.replace("2","8");
			}

			if (status.indexOf("3")!=-1){
				status = status.replace("3","2-3-7");
			}
			if(status.indexOf("6-6")!=-1){
				status = status.replace("6-6","0-5-8");
			}
			if(status.indexOf("6")!=-1){
				status = status.replace("6","0-5-8");
			}
			if(status.indexOf("7")!=-1){
				status = status.replace("7","2");
			}
			return status.split("-");
		}
		return null;
	}

	/**
	 * 下载协议
	 * @param creditorRightsId
	 * @param response
	 */
	@RequestMapping(value = "/downloadAgreement")
	public void downloadAgreement(Long creditorRightsId, HttpServletResponse response) {
		CreditorRights creditorRights = this.creditorRightsService.findById(creditorRightsId, false);
		AgreementInfo agreement = agreementInfoService.findVersionByCreditorRightsId(creditorRightsId);
		String fileName = null ;
		if(agreement.getVersion() !=null && agreement.getVersion() > 1){
			fileName = creditorRights.getCreditorRightsCode() + "-" + agreement.getVersion();
		}else{
			fileName = creditorRights.getCreditorRightsCode() ;
		}
		File file = new File(PropertiesUtils.getInstance().get("AGREEMENT_PATH") + fileName + ".zip");
		if (file.exists()) {
			try {
				FileUtil.download(file, response);
			} catch (IOException e) {
				throw SystemException.wrap(e, SystemErrorCode.IO_ERROR);
			}
		} else {
			throw new SystemException(SystemErrorCode.FILE_ERROR);
		}
	}
	/**
	 * 省心计划下载协议
	 * @param lendOrderId
	 * @param response
	 */
	@RequestMapping(value = "/downloadFinanceAgreement")
	public void downloadFinanceAgreement(Long lendOrderId, HttpServletResponse response) {
		FinancePlanAgreement financePlanAgreement = financePlanAgreementService.findAvalidByLendOrderId(lendOrderId,
						AgreementEnum.FinancePlanAgreementStatusEnum.AVALID_AGREEMENT.value2Char());
		if(null != financePlanAgreement){
			String fileName = financePlanAgreement.getFinanceAgreementCode() + "-" + financePlanAgreement.getVersion();
			File file = new File(PropertiesUtils.getInstance().get("AGREEMENT_PATH") + fileName + ".zip");
			logger.info("path:"+PropertiesUtils.getInstance().get("AGREEMENT_PATH") + fileName + ".zip");
			if (file.exists()) {
				try {
					FileUtil.download(file, response);
				} catch (IOException e) {
					throw SystemException.wrap(e, SystemErrorCode.IO_ERROR);
				}
			} else {
				throw new SystemException(SystemErrorCode.FILE_ERROR);
			}
		}else {
			throw new SystemException(SystemErrorCode.FILE_ERROR);
		}
	}

	/**
	 * 跳转至出借债券转让列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toTurnCreditRightList")
	public String toTurnCreditRightList(HttpServletRequest request) {
		return "/finance/myTurnCreditRightList";
	}

	/**
	 * 债券转让查询列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getTurnCreditRightList")
	@ResponseBody
	public Object getTurnCreditRightList(HttpServletRequest request,@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
									 @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
									 String creditorRightsStatus,String backDate,String buyDate) {
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);
		CreditorRightsExtVo vo = new CreditorRightsExtVo();
		vo.setLendUserId(currentUser.getUserId());

		vo.setChannelType(ChannelTypeEnum.ONLINE.value2Long());//线上

		// 封装参数
		Map<String, Object> customParams = new HashMap<String, Object>();

		if (com.xt.cfp.core.util.StringUtils.isNull(creditorRightsStatus)) {
			String[] queryStatus = { CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.getValue(),
					CreditorRightsConstants.CreditorRightsStateEnum.TURNOUT.getValue(),
					CreditorRightsConstants.CreditorRightsStateEnum.COMPLETE.getValue(),
					CreditorRightsConstants.CreditorRightsStateEnum.APPLYTURNOUT.getValue(),
					CreditorRightsConstants.CreditorRightsStateEnum.TRANSFERING.getValue() };
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
		customParams.put("orderBy",getOrderBy(backDate,buyDate));
		Pagination<CreditorRightsExtVo> creditorRightsPaging = creditorRightsService.getCreditorRightsPaging(pageNo, pageSize, vo, customParams);
		return creditorRightsPaging;
	}

	/**
	 * 债权市场列表
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/creditorRightsList")
	@ResponseBody
	public Object creditorRightsList(HttpServletRequest request,
						   LoanApplicationListVO loanApplicationListVO,
						   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
						   @RequestParam(value = "pageNo",defaultValue = "1") int pageNo){

		Pagination<LoanApplicationListVO> loanApplicationListPaging = loanApplicationService.getTurnCreditRightPaging(pageNo, pageSize, loanApplicationListVO, null);
		return loanApplicationListPaging;
	}

	/**
	 * 债券转让详情
	 * @param request
	 * @param loanApplicationNo
	 * @return
	 */
	@DoNotNeedLogin
	@RequestMapping(value = "/creditRightBidding")
	public String creditRightBidding(HttpServletRequest request,@RequestParam(value = "creditorRightsApplyId", required = false)Long creditorRightsApplyId){
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

		//发标信息
		LoanPublish loanPublish = loanPublicService.findLoanPublishVO(loanApplicationNo);
		request.setAttribute("loanPublish", loanPublish);
		if (loanPublish==null) {
			//非法请求
			return "error";
		}

		if (PublishTarget.BACKGROUND.getValue().equals(loanPublish.getPublishTarget())){
			//非法请求
			return "error";
		}
		//担保公司
		if (loanPublish.getCompanyId()!=null){
			GuaranteeCompany company = guaranteeCompanyService.getGuaranteeCompanyByCompanyId(loanPublish.getCompanyId());
			request.setAttribute("guaranteeCompany",company);
		}

		//token
		String token = TokenUtils.setNewToken(request);
		request.setAttribute("token",token);
		//组织页面返回值
		request.setAttribute("repayMentMethod", PaymentMethodEnum.values());
		request.setAttribute("guaranteeType", GuaranteeTypeEnum.values());
		//用户是否登陆
		loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());

		HttpSession session = WebUtil.getHttpServletRequest().getSession();
		UserInfo user = (UserInfo) session.getAttribute(Constants.USER_ID_IN_SESSION);

		//已还本息
		BigDecimal hasPaidBalance = BigDecimal.ZERO ;
		//待还本息
		BigDecimal waitPaidBalance = BigDecimal.ZERO ;
		Map<Object,String> stateMap = new HashMap<>();
		for(RepaymentPlanStateEnum r : RepaymentPlanStateEnum.values()){
			stateMap.put(r.value2Char(), r.getDesc());
		}
		request.setAttribute("stateMap", stateMap);
		List<RepaymentPlan> repaymentPlanList = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplicationNo,ChannelTypeEnum.ONLINE);
		List<RepaymentPlan> showRepaypaymentList = new ArrayList<RepaymentPlan>();
		if(repaymentPlanList!=null &&repaymentPlanList.size()>0){
			for (RepaymentPlan plan : repaymentPlanList){
				hasPaidBalance = hasPaidBalance.add(plan.getFactBalance());
				if(plan.getPlanState() == RepaymentPlanStateEnum.BEFORE_COMPLETE.value2Char()){
					waitPaidBalance = waitPaidBalance.add(plan.getShouldCapital2().subtract(plan.getFactCalital()));
				}else if(plan.getPlanState() == RepaymentPlanStateEnum.COMPLETE.value2Char()){

				}else{
					waitPaidBalance = waitPaidBalance.add(plan.getShouldBalance2().subtract(plan.getFactBalance()));
					showRepaypaymentList.add(plan);
				}
			}
		}
		waitPaidBalance = BigDecimalUtil.up(waitPaidBalance, 2);
		request.setAttribute("showRepaypaymentList", showRepaypaymentList);
		request.setAttribute("waitPaidBalance", waitPaidBalance);
		request.setAttribute("hasPaidBalance", hasPaidBalance);

		if(user!=null){
			//获取账户余额
			UserAccount cashAccount = userAccountService.getCashAccount(user.getUserId());
			UserInfoVO userExt = userInfoService.getUserExtByUserId(user.getUserId());
			request.setAttribute("userExt", userExt);
			request.setAttribute("cashAccount", cashAccount);
			//计算账户已投金额
			BigDecimal totalLendAmountPerson = lendOrderService.getTotalLendAmount(user.getUserId(), loanApplicationListVO.getLoanApplicationId());
			if (totalLendAmountPerson != null) {
				if(loanApplicationListVO.getMaxBuyBalance()==null){
					loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getConfirmBalance().subtract(totalLendAmountPerson));
				}else{
					loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance().subtract(totalLendAmountPerson));
				}
			}

			//个人财富券统计
			List<VoucherVO> useageList = voucherService.getAllVoucherList(user.getUserId(), null, VoucherConstants.UsageScenario.LOAN, VoucherConstants.UsageScenario.WHOLE);
			if(useageList!=null) {
				request.setAttribute("useageCount", useageList.size());
				BigDecimal useageSum = BigDecimal.ZERO;
				for (VoucherVO vo : useageList){
					useageSum = useageSum.add(vo.getVoucherValue());
				}
				request.setAttribute("useageSum",useageSum);

				List<VoucherProductVO> voucherProductVOs = voucherService.getVoucherStatistics(user.getUserId(),VoucherConstants.UsageScenario.LOAN, VoucherConstants.UsageScenario.WHOLE);
				request.setAttribute("voucherProductVOs",voucherProductVOs);
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
				nextRepaymentDay = null == nextRepaymentDay ? detailRights.getRepaymentDayPlanned() : nextRepaymentDay;
			}
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
		request.setAttribute("loanApplicationListVO",loanApplicationListVO);
		request.setAttribute("shouldCapital", creditorRights.getRightsWorth());//剩余本金
		request.setAttribute("surpMonth", surpMonth);//剩余还款月
		request.setAttribute("creditorRightsId", creditorRights.getCreditorRightsId());
		request.setAttribute("creditorRightsApplyId", crta.getCreditorRightsApplyId());
		int residualTime = 0;// 下一个还款日距离当前时间相差秒数
		try {
			residualTime = DateUtil.secondBetween(new Date(), nextRepaymentDay);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("residualTime", residualTime);
		//附件快照
		List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getcustomerUploadAttachment(loanApplicationListVO.getLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());
		request.setAttribute("customerUploadSnapshots",customerUploadSnapshots);
		UserInfoExt creditLend = userInfoExtService.getUserInfoExtById(creditorRights.getLendUserId());
		request.setAttribute("lendCustomerName", creditLend.getJMRealName());//转让人

		if(LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanApplicationListVO.getLoanType())
				|| LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
				|| LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
				|| LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())){
			//个人投标详情页
			personLoanApplication(request, loanApplicationListVO, loanPublish);
			return "/CreditroRights/financeBidding";
		}else{
			//企业投标详情页
			enterpriseLoanApplication(request, loanApplicationListVO, loanPublish);
			return "/CreditroRights/financeEnterpriseBidding";
		}
	}
	/**
	 * 转让债券详情
	 * @param request
	 * @param loanApplicationNo
	 * @return
	 */
	@RequestMapping(value = "/turnCreditRightBidding")
	public String turnCreditRightBidding(HttpServletRequest request,@RequestParam(value = "creditorRightsId", required = false)Long creditorRightsId){
		if(creditorRightsId==null){
			//非法请求
			return "error";
		}
		CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
		LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(creditorRights.getLoanApplicationId());
		if (loanApplicationListVO==null){
			//非法请求
			return "error";
		}

		List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRightsId);
		long termDay = repaymentPlanService.getTermDay(detailRightsList);
		if (termDay < 0) {
			throw new SystemException(CreditorErrorCode.CREDITORCANTTURN).set("termDay", termDay);
		}
		request.setAttribute("termDay", termDay == 0 ? 1 : termDay);

		//token
		String token = TokenUtils.setNewToken(request);
		request.setAttribute("token",token);
		//组织页面返回值
		request.setAttribute("repayMentMethod", PaymentMethodEnum.values());
		request.setAttribute("guaranteeType", GuaranteeTypeEnum.values());

		BigDecimal shouldCapital = BigDecimal.ZERO;
		int surpMonth = 0;
		for(RightsRepaymentDetail detailRights:detailRightsList){

			shouldCapital = shouldCapital.add(detailRights.getShouldCapital2().subtract(detailRights.getFactCalital()));
			if(RightsRepaymentDetailIsPayOffEnum.ISPAYOFF_NO.getValue().equals(String.valueOf(detailRights.getIsPayOff()))){
				surpMonth++;
			}

		}
		request.setAttribute("shouldCapital", shouldCapital);//剩余本金
		request.setAttribute("surpMonth", surpMonth);//剩余还款月
		request.setAttribute("loanApplicationListVO", loanApplicationListVO);
		request.setAttribute("creditorRights", creditorRights);

		return "/finance/myTurnCreditRightsDetail";
	}

	/**
	 * 开始转让
	 * @param request
	 * @param loanApplicationNo
	 * @return
	 */
	@RequestMapping(value = "/turnCreditRight")
	public String turnCreditRight(HttpServletRequest request,
			@RequestParam(value = "creditorRightsId", required = false) Long creditorRightsId,
			@RequestParam(value = "rightAcount", required = false) BigDecimal rightAcount,
			@RequestParam(value = "changePoint", required = false) Integer changePoint) {
		if(creditorRightsId==null || rightAcount == null){
			//非法请求
			throw new SystemException(SystemErrorCode.SESSION_NOT_EXISIT);
		}
		if (changePoint == null || changePoint > 0 || changePoint < -5) {
			// 非法请求
			throw new SystemException(CreditorErrorCode.CREDITOR_AMOUNT_ERROR);
		}
		TokenUtils.validateToken(request);
		List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRightsId);

		BigDecimal shouldCapital = BigDecimal.ZERO;
		//获取是否符合转让的天数
		long termDay = repaymentPlanService.getTermDay(detailRightsList);
		if(termDay<0){
        	throw new SystemException(CreditorErrorCode.CREDITORCANTTURN).set("termDay",termDay);
        }
		int surpMonth = 0;
		for(RightsRepaymentDetail detailRights:detailRightsList){
			shouldCapital = shouldCapital.add(detailRights.getShouldCapital2().subtract(detailRights.getFactCalital()));
			if(RightsRepaymentDetailIsPayOffEnum.ISPAYOFF_NO.getValue().equals(String.valueOf(detailRights.getIsPayOff()))){
				surpMonth++;
			}
		}
		BigDecimal temp_shouldCapital = shouldCapital.add(shouldCapital.multiply(new BigDecimal(changePoint).divide(new BigDecimal("100"))));

		if (temp_shouldCapital.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(rightAcount) != 0) {
			throw new SystemException(CreditorErrorCode.CREDITOR_AMOUNT_ERROR);
		}

		creditorRightsService.turnCreditor(rightAcount, creditorRightsId, surpMonth);
		return "/finance/trunRightsSucess";
	}

	/**
	 * 定向密码验证
	 * @param pass
	 * @param loanApplicationId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getPass")
	@ResponseBody
	public String getPass(String pass ,Long loanApplicationId,HttpServletRequest request){
		String pass1 =loanApplicationService.getLoanApplicationPass(loanApplicationId);
		String passwordCiphertext = MD5Util.MD5Encode(pass, "utf-8");
		if(pass1.equals(passwordCiphertext)){
			request.getSession().setAttribute("opass_"+loanApplicationId, pass);
			return "sucess";
		}
		return "fail";
	}
	/**
	 * 用余额、财富券购买债权
	 *
	 * @param password
	 * @param amount
	 * @return
	 */
	@RequestMapping(value = "/buyRightsByAccountBalance")
	public String buyRightsByAccountBalance(@RequestParam(value = "password", required = false) String password,
										  @RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId,
										  @RequestParam(value = "creditorRightsApplyId", required = false) Long creditorRightsApplyId,Long lendOrderId,
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
//		logger.info(LogUtils.createLogWithParams("购买债权", DescTemplate.Log.BidLogTemplate.BID_LOAN_BY_ACCOUNTVALUE,
//				currentUser.getUserId(), lendProductPublish.getLendProductPublishId(), amount));

		//校验交易密码
		if (currentUser.getBidPass() == null){
			request.setAttribute("errorMsg", UserErrorCode.NO_BIDPASS.getDesc());
			return "/order/payError";
		}
		if (!MD5Util.MD5Encode(password, null).equals(currentUser.getBidPass())){
			request.setAttribute("errorMsg", UserErrorCode.ERROR_BIDPASS.getDesc());
			return "/order/payError";
		}

		//校验身份认证状态
		if (!this.userInfoService.hasIdentityAuthentication(currentUser.getUserId())){
			request.setAttribute("errorMsg", UserErrorCode.HAS_NOT_IDVERIFIED.getDesc());
			return "/order/payError";
		}

		//验证购买金额是否合法(不大于0的情况)
		if (amount.compareTo(BigDecimal.ZERO) <= 0){
			request.setAttribute("errorMsg", BidErrorCode.AMOUNT_ILLEGAL.getDesc());
			return "/order/payError";
		}

		HttpSession session = WebUtil.getHttpServletRequest().getSession();
		List<VoucherVO> voucherVOs = new ArrayList<VoucherVO>();
		BigDecimal voucherPayValue=BigDecimal.ZERO;
		String[] voucher_Ids = null;
		if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)){
			voucher_Ids = voucherIds.split(",");
			//财富券
			for (String voucherId:voucher_Ids){
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
			return "/order/payError";
		}
		if(crta.getBusStatus().equals(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus.CANCEL.getValue())){
			request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_REVOKE.getDesc());
			return "/order/payError";
		}

		if(crta.getBusStatus().equals(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus.SUCCESS.getValue())){
			request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_AMOUNT_ZONE.getDesc());
			return "/order/payError";
		}

		if(crta.getBusStatus().equals(CreditorRightsConstants.CreditorRightsStateEnum.CreditorRightsTransferAppStatus.OVERDUE.getValue())){
			request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_AMOUNT_TIMEOUT.getDesc());
			return "/order/payError";
		}

		if(user!=null){
			//获取账户余额
			UserAccount cashAccount = userAccountService.getCashAccount(user.getUserId());
			if (cashAccount.getAvailValue2().compareTo(accountPayValue)<0){
				request.setAttribute("errorMsg", UserAccountErrorCode.ACCOUNT_BALANCE_AVAIVALUE_INSUFFICIENT.getDesc());
				return "/order/payError";
			}
			//已经购买的债权金额
			BigDecimal totalBuy = creditorRightsTransferAppService.getRemainingRightsPrice(crta.getCreditorRightsApplyId());//lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());

			if (crta.getApplyUserId().equals(user.getUserId())) {
				request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_MYSELF.getDesc());
				return "/order/payError";
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

			if(amount.compareTo(crta.getApplyPrice().subtract(totalBuy))>0){
				//购买金额变为剩余可投金额
				if(crta.getApplyPrice().compareTo(totalBuy)==0){
					request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_AMOUNT_ZONE.getDesc());
					return "/order/payError";
				}
				request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_TOO_MUCH.getDesc());
				return "/order/payError";
//				amount = crta.getApplyPrice().subtract(totalBuy);
			}
		}

		PayResult payResult = null;
		LendOrder lendOrder = null;
		if (lendOrderId == null) {
			//可用余额支付
			payResult = lendProductService.creditorrightsByAccountBalance(crta, loanApplicationId, currentUser.getUserId(), lendProductPublish.getLendProductPublishId(), amount, PropertiesUtils.getInstance().get("SOURCE_PC"));
			lendOrder = lendOrderService.getLendOrderByPayId(payResult.getPayId(), true);
		} else {
			//可用余额、财富券支付
			lendOrder = lendOrderService.findById(lendOrderId);

			//todo 财富券校验
			if (org.apache.commons.lang.StringUtils.isNotEmpty(voucherIds)){
				voucher_Ids = voucherIds.split(",");
				//先校验财富券
				JsonView jsonView = voucherService.voucherValidate(currentUser, lendOrder, voucherVOs);
				if (!jsonView.isSuccess()){
					request.setAttribute("errorMsg",jsonView.getInfo());
					return "/order/payError";
				}
			}

			//校验-购买金额加和是否等于订单金额
			if (lendOrder.getBuyBalance().compareTo(accountPayValue.add(voucherPayValue)) != 0) {
				request.setAttribute("errorMsg", BidErrorCode.AMOUNT_ILLEGAL.getDesc());
				return "/order/payError";
			}

			payResult = lendProductService.payFinanceByAccountBalanceWeb(currentUser.getUserId(), lendOrderId, null, null, voucher_Ids);
		}

		request.setAttribute("payResult", payResult);
		request.setAttribute("lendOrder", lendOrder);
		//根据执行结果跳转不同页面
		if (payResult.isPayResult()&&payResult.isProcessResult())
			return "/order/paySuccess";

		request.setAttribute("errorMsg", payResult.getFailDesc());
		return "/order/payError";
	}
	/**
	 * 银行卡 购买债券
	 * @param creditorRightsId
	 * @param amount
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "toBuyRightsByPayAmount")
	public String toBuyRightsByPayAmount(Long creditorRightsApplyId, BigDecimal amount, HttpServletRequest request) {
		if (creditorRightsApplyId == null) {
			return "/error";
		}

		CreditorRightsTransferApplication crta =  creditorRightsTransferAppService.findById(creditorRightsApplyId);
		if(crta == null){
			request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_NOT_EXIST.getDesc());
			return "/order/payError";
		}
		Long creditorRightsId = crta.getApplyCrId();

		CreditorRights creditorRights = creditorRightsService.findById(creditorRightsId, false);
		//TokenUtils.validateToken(request);
		UserInfo currentUser = SecurityUtil.getCurrentUser(true);

		LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(creditorRights.getLoanApplicationId());

		LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());

		//已经购买的债权金额
		BigDecimal totalBuy = creditorRightsTransferAppService.getRemainingRightsPrice(crta.getCreditorRightsApplyId());//lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());

		if (crta.getApplyUserId().equals(currentUser.getUserId())) {
			request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_MYSELF.getDesc());
			return "/order/payError";
		}
		if(amount.compareTo(crta.getApplyPrice().subtract(totalBuy))>0){
			//购买金额变为剩余可投金额
			if(crta.getApplyPrice().compareTo(totalBuy)==0){
				request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_AMOUNT_ZONE.getDesc());
				return "/order/payError";
			}
			request.setAttribute("errorMsg", CreditorErrorCode.CREDITOR_RESIDUAL_BUY_TOO_MUCH.getDesc());
			return "/order/payError";
//			amount = crta.getApplyPrice().subtract(totalBuy);
		}

		// 新建投标订单
		LendOrder lendOrder = lendOrderService.addCreditorRightsOrder(currentUser.getUserId(), creditorRights.getLoanApplicationId(),
				lendProductPublish.getLendProductPublishId(), amount, new Date(), lendProduct, PropertiesUtils.getInstance().get("SOURCE_PC"), crta);
		// 购买省心计划
		return "redirect:/finance/toPayRightsOrder?lendOrderId=" + lendOrder.getLendOrderId();
	}

	// admin 后台专用 by mainid
		@DoNotNeedLogin
		@RequestMapping(value = "/admin_bidding")
		public String admin_bidding(HttpServletRequest request,Long loanApplicationNo){
			if(loanApplicationNo==null){
				//非法请求
				return "error";
			}
			LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoByMainId(loanApplicationNo);//main
			if (loanApplicationListVO==null){
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
			request.setAttribute("token",token);
			//组织页面返回值
			request.setAttribute("repayMentMethod", PaymentMethodEnum.values());
			request.setAttribute("guaranteeType", GuaranteeTypeEnum.values());
			//用户是否登陆
			loanApplicationListVO.setMaxBuyBalanceNow(loanApplicationListVO.getMaxBuyBalance());

			//发标信息
			MainLoanPublish mainLoanPublish = loanPublicService.findMainLoanPublishById(loanApplicationNo);//main

			request.setAttribute("loanApplicationListVO",loanApplicationListVO);
			//附件快照
			List<CustomerUploadSnapshotVO> customerUploadSnapshots = loanApplicationService.getCustomerUploadAttachmentByMainId(loanApplicationListVO.getMainLoanApplicationId(), AttachmentIsCode.IS_CODE.getValue());//main
			request.setAttribute("customerUploadSnapshots",customerUploadSnapshots);
			if(LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanApplicationListVO.getLoanType())
					|| LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
					|| LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
					|| LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())
					|| LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(loanApplicationListVO.getLoanType())
					){
				//个人投标详情页
//				personLoanApplication(request, loanApplicationListVO, loanPublish);
				//借款人基本信息
				CustomerBasicSnapshot basicSnapshot = customerBasicSnapshotService.getBasicByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());//main
				if(basicSnapshot!=null){
					request.setAttribute("basicSnapshot",basicSnapshot);
					//居住地
					Address adress = addressService.getAddressVOById(basicSnapshot.getResidenceAddr());
					request.setAttribute("adress",adress);
				}
				if(loanApplicationListVO.getLoanType().equals(LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue())){
					CustomerCarSnapshot carSnapshot = customerCarSnapshotService.getCarByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());
					request.setAttribute("basicInfoForPeopleAndCar",carSnapshot);
					request.setAttribute("loanPublish",mainLoanPublish);
					/*request.setAttribute("basicInfoForPeopleAndCarType", carSnapshot.getPledgeType().equals("1") ? "一抵" : "二抵");*/
				}else{
					//房产抵押
					CustomerHouseSnapshot house = customerHouseSnapshotService.getHouseByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());//main
					request.setAttribute("house",house);
					request.setAttribute("loanPublish",mainLoanPublish);
					}

				if(null != mainLoanPublish){
					Address houseAdress = addressService.getAddressVOById(mainLoanPublish.getHourseAddress());
					request.setAttribute("houseAdress",houseAdress);
					//认证报告
					String authInfos = mainLoanPublish.getAuthInfos();
					if(StringUtils.isNotEmpty(authInfos)){
						request.setAttribute("authInfo",authInfos.split(","));
					}
				}
				return "/finance/front_financeBidding";
			}else{
				//企业投标详情页
//				enterpriseLoanApplication(request, loanApplicationListVO, loanPublish);
				//借款企业
				EnterpriseLoanApplication enterpriseLoanApplication = enterpriseLoanApplicationService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());//main
				EnterpriseInfo enterpriseInfo = enterpriseInfoService.getEnterpriseById(enterpriseLoanApplication.getEnterpriseId());
				request.setAttribute("enterpriseInfo",enterpriseInfo);
				//企业图
				List<EnterpriseUploadSnapshotVO> enterpriseInfoSnapshots = enterpriseInfoService.getEnterpriseUploadAttachment(enterpriseInfo.getEnterpriseId(), AttachmentIsCode.IS_CODE.getValue());
				request.setAttribute("enterpriseInfoSnapshots",enterpriseInfoSnapshots);
				switch (loanApplicationListVO.getLoanType()){
					case "2":
						//企业车贷
						EnterpriseCarLoanSnapshot enterpriseCarLoanSnapshot = enterpriseCarLoanSnapshotService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());//main
						String address = getAddress(enterpriseCarLoanSnapshot.getProvince(),enterpriseCarLoanSnapshot.getCity());
						BigDecimal totalPrice= mortgageCarSnapshotService.getTotalPriceByCardLoanId(enterpriseCarLoanSnapshot.getEnterpriseCarLoanId());
						request.setAttribute("totalPrice",totalPrice);
						request.setAttribute("address",address);
						request.setAttribute("enterpriseCarLoanSnapshot",enterpriseCarLoanSnapshot);
						break;
					case "3":
						//企业信贷
						EnterpriseCreditSnapshot enterpriseCreditSnapshot = enterpriseCreditSnapshotService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());
						address = getAddress(enterpriseCreditSnapshot.getProvince(),enterpriseCreditSnapshot.getCity());
						request.setAttribute("enterpriseCarLoanSnapshot",enterpriseCreditSnapshot);
						request.setAttribute("address",address);
						break;
					case "4":
						//企业保理
						EnterpriseFactoringSnapshot factoringSnapshot = enterpriseFactoringSnapshotService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());
						request.setAttribute("factoringSnapshot",factoringSnapshot);
						//融资方
						CoLtd financeParty = coLtdService.getCoLtdById(factoringSnapshot.getFinancingParty());
						request.setAttribute("financeParty",financeParty);
						break;
					case "5":
						//企业基金
						EnterpriseFoundationSnapshot foundationSnapshot = enterpriseFoundationSnapshotService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());
						request.setAttribute("foundationSnapshot",foundationSnapshot);
						//托管机构
						CoLtd coltd = coLtdService.getCoLtdById(foundationSnapshot.getCoId());
						request.setAttribute("coltd",coltd);
						//标的详情说明
						Attachment attachment = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getAttachId());
						request.setAttribute("attachment",attachment);
						//交易说明书
						Attachment tradeBook = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getTradeBookId());
						request.setAttribute("tradeBook",tradeBook);
						//风险提示函
						Attachment riskTip = enterpriseFoundationSnapshotService.getAttachmentById(foundationSnapshot.getRiskTipId());
						request.setAttribute("riskTip",riskTip);
						break;
					case "6":
						//企业贷-质押标
						EnterprisePledgeSnapshot enterprisePledgeSnapshot = enterprisePledgeSnapshotService.getByMainLoanApplicationId(loanApplicationListVO.getMainLoanApplicationId());
						address = getAddress(enterprisePledgeSnapshot.getProvince(),enterprisePledgeSnapshot.getCity());
						request.setAttribute("enterpriseCarLoanSnapshot",enterprisePledgeSnapshot);
						request.setAttribute("address",address);
						break;
				}
				return "/finance/front_financeEnterpriseBidding";
			}
		}
		@RequestMapping(value = "toPayRightsOrder")
		public String toPayRightsOrder(HttpServletRequest request,
									 @RequestParam(value = "lendOrderId") Long lendOrderId) {
			TokenUtils.setNewToken(request);
			UserInfo currentUser = SecurityUtil.getCurrentUser(true);
			UserInfoVO userExt = userInfoService.getUserExtByUserId(currentUser.getUserId());
			LendOrder lendOrder = lendOrderService.findById(lendOrderId);
			//身份验证
			if (!userExt.getIsVerified().equals("1")){
				throw new SystemException(UserErrorCode.HAS_NOT_IDVERIFIED).set("userId", currentUser.getUserId());
			}
			//校验-订单是否存在
			if (lendOrder==null) {
				throw new SystemException(PayErrorCode.NOT_EXIST_PAY_ORDER).set("lendOrderId", lendOrderId);
			}

			//校验-订单用户和当前用户必须相同
			if (!lendOrder.getLendUserId().equals(currentUser.getUserId())) {
				throw new SystemException(PayErrorCode.NOT_BELONG_PAY_ORDER).set("lendOrderUserId",lendOrder.getLendUserId()).set("user",currentUser.getUserId());
			}

			if (!lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue()))
				throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);

			CreditorRightsTransferApplication crtf = creditorRightsTransferAppService.getTransferApplicationByLendOrderId(lendOrder.getLendOrderId());
			// 标的状态验证
			if (!CreditorRightsTransferAppStatus.TRANSFERRING.getValue().equals(crtf.getBusStatus())) {
				throw new SystemException(BidErrorCode.BID_STATIS_NOT_SELLING).set("status", crtf.getBusStatus());
			}

//			LendProductPublish lendProductPublish = lendProductService.getLendProductPublishByPublishId(lendOrder.getLendProductPublishId());
//			LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());
//			List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
//
//			LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
//			LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());
//			String point=com.xt.cfp.core.util.StringUtils.isNull(loanApplicationListVO.getAwardPoint())?"":null;
//	    	if(point==null){
//	    		if(loanApplicationListVO.getAwardPoint().equals("1")){
//	    			point=AwardPointEnum.ATMAKELOAN.getDesc();
//	    		}else if(loanApplicationListVO.getAwardPoint().equals("2")){
//	    			point=AwardPointEnum.ATREPAYMENT.getDesc();
//	    		}else{
//	    			point=AwardPointEnum.ATCOMPLETE.getDesc();
//	    		}
//	    	}
//			//奖励发放时机
//	    	loanApplicationListVO.setAwardPoint(point);
//	    	//计算奖励金额
//	    	if(loanApplicationListVO.getAwardRate()!=null&&!loanApplicationListVO.getAwardRate().equals("")&&!loanApplicationListVO.getAwardRate().equals("0")){
//	    		LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(lendOrderBidDetail.getLoanApplicationId());
//	        	LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
//	        	BigDecimal amount=lendOrder.getBuyBalance();
//	        	BigDecimal profit = BigDecimal.ZERO;
//	    		try {
//	    			profit = InterestCalculation.getAllInterest(amount,new BigDecimal(loanApplicationListVO.getAwardRate()),loanProduct.getDueTimeType(),
//	                        loanProduct.getRepaymentMethod(),loanProduct.getRepaymentType(),loanProduct.getRepaymentCycle(),loanProduct.getDueTime(),loanProduct.getCycleValue());
//				} catch (Exception e) {
//	    			e.printStackTrace();
//	    		}
//	    		profit = BigDecimalUtil.down(profit,2);
//	    		request.setAttribute("awardProfit", profit);
//	    	}

			List<LendOrderBidDetail> lendOrderBidDetails = lendOrderBidDetailService.findByLendOrderId(lendOrder.getLendOrderId());
			LendOrderBidDetail lendOrderBidDetail = lendOrderBidDetails.get(0);
			LendProductPublish lendProductPublish = loanApplicationService.getLendProductPublishByLoanApplicationId(lendOrderBidDetail.getLoanApplicationId());
			LoanApplicationListVO loanApplicationListVO = loanApplicationService.getLoanApplicationVoById(lendOrderBidDetail.getLoanApplicationId());

	    	loanApplicationListVO.setAwardRate(loanApplicationListVO.getAwardRate()!=null&&!loanApplicationListVO.getAwardRate().equals("")&&!loanApplicationListVO.getAwardRate().equals("0")?loanApplicationListVO.getAwardRate()+"%":"");

	    	LendProduct lendProduct = lendProductService.findById(lendProductPublish.getLendProductId());

			//检查剩余金额
//			BigDecimal totalBuy = lendOrderService.getTotalLendAmountByLoanApplicationId(loanApplicationListVO.getLoanApplicationId());
//			if(lendOrder.getBuyBalance().compareTo(loanApplicationListVO.getConfirmBalance().subtract(totalBuy))>0){
//				//无法购买剩余标
//				throw new SystemException(BidErrorCode.LENDORDER_AMOUNT_NOT_ENOUGH).set("amount", lendOrder.getBuyBalance()).set("remain",loanApplicationListVO.getConfirmBalance().subtract(totalBuy));
//			}

			// 已经购买的债权金额
			BigDecimal totalBuy = creditorRightsTransferAppService.getRemainingRightsPrice(crtf.getCreditorRightsApplyId());
			CreditorRightsTransferApplication crta = creditorRightsTransferAppService.getEffectiveTransferApplyByCreditorRightsId(crtf.getApplyCrId());

			if (lendOrder.getBuyBalance().remainder(new BigDecimal("100")).longValue() == 0) {
				if (new BigDecimal("100").compareTo(crta.getApplyPrice().subtract(totalBuy).subtract(lendOrder.getBuyBalance())) > 0
						&& BigDecimal.ZERO.compareTo(crta.getApplyPrice().subtract(totalBuy).subtract(lendOrder.getBuyBalance())) < 0)
					throw new SystemException(CreditorErrorCode.CREDITOR_RESIDUAL_COMPARE_100);
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
			//历史卡信息
			CustomerCard hisCustomerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(),PayConstants.PayChannel.YB);
			if(hisCustomerCard!=null){
				boolean isSupport = true;
				ConstantDefine bankInfo = constantDefineService.findById(hisCustomerCard.getBankCode());
				if (bankInfo.getConstantStatus().equals("1")){
					isSupport = false;
				}
				request.setAttribute("isSupport", isSupport);
			}

			//查询用户卡信息
			CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(currentUser.getUserId(), PayConstants.PayChannel.LL);
			if (customerCard != null && null!=customerCard.getMobile())
				customerCard.setMobile(customerCard.getMobile().substring(0, 3) + "*****" + customerCard.getMobile().substring(8));

			//财富券
			List<VoucherVO> vouchers = voucherService.getAllVoucherList(currentUser.getUserId(),lendOrder.getBuyBalance(),VoucherConstants.UsageScenario.LOAN,VoucherConstants.UsageScenario.WHOLE);

			//预计收益
			String[] temp_expectedInteresting = creditorRightsService.getExpectRightProfit(crtf.getCreditorRightsApplyId(), lendOrder.getBuyBalance()).toString().split(",");
			BigDecimal expectedInteresting = new BigDecimal(String.valueOf(temp_expectedInteresting[0]));
			if(temp_expectedInteresting.length==2){
				BigDecimal awardProfit = new BigDecimal(String.valueOf(temp_expectedInteresting[1]));
				request.setAttribute("awardProfit", awardProfit);
			}

			request.setAttribute("isRights", true);//是否债权转让
			request.setAttribute("order", lendOrder);
			request.setAttribute("loanApplication", loanApplicationListVO);
			request.setAttribute("userExtInfo", userExt);
			request.setAttribute("lendOrder", lendOrder);
			request.setAttribute("amount", lendOrder.getBuyBalance());
			request.setAttribute("truePayAmount", lendOrder.getBuyBalance());
			request.setAttribute("customerCard", customerCard);
			request.setAttribute("vouchers", vouchers);
			request.setAttribute("expectedInteresting", expectedInteresting);
			request.setAttribute("lendProduct", lendProduct);
			request.setAttribute("lendProductPublish", lendProductPublish);
			request.setAttribute("userCashBalance", userAccountService.getCashAccount(currentUser.getUserId()).getAvailValue2());
			boolean isBidEqualLoginPass = userInfoService.isBidPassEqualLoginPass(currentUser.getUserId());
			request.setAttribute("isBidEqualLoginPass",isBidEqualLoginPass);
			//获取网关充值银行卡列表
	        List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByTypeNeedSort("gateway_bank");
	        request.setAttribute("bankList", constantDefines);
//			if (customerCard == null)
				return "order/noCardBidLoanPay";
//			else
//				return "order/hasCardPay";
		}



		/**
		 * 区别返回企业标或者是用户标
		 * @param string
		 * @param request
		 * @param loanApplicationListVO
		 * @param loanPublish
		 * @param viewPath 0为个人标 1为企业标
		 * @return 路径
		 */

	public String whichViewReturnBySpecialBadding(String string,
			HttpServletRequest request,
			LoanApplicationListVO loanApplicationListVO,
			LoanPublish loanPublish, String[] viewPath) {
		String view = "";
		if (LoanTypeEnum.LOANTYPE_CREDIT.getValue().equals(loanApplicationListVO.getLoanType())
				|| LoanTypeEnum.LOANTYPE_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
				|| LoanTypeEnum.LOANTYPE_DIRECT_HOUSE.getValue().equals(loanApplicationListVO.getLoanType())
				|| LoanTypeEnum.LOANTYPE_CREDIT_CAR_PEOPLE.getValue().equals(loanApplicationListVO.getLoanType())
				||LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue().equals(loanApplicationListVO.getLoanType())
				) {
			// 定向个人投标详情页
			personLoanApplication(request, loanApplicationListVO, loanPublish);
			view = viewPath[0].trim();
		} else {
			enterpriseLoanApplication(request, loanApplicationListVO,
					loanPublish);
			// 定向企业投标详情页
			view = viewPath[1].trim();
		}

		return view;
	}

		/**
		 * 新手标方法
		 * @param loanApplicationListVO
		 * @author wangyadong
		 * @date   2016年10月8日
		 * @return
		 */
		@DoNotNeedLogin
		@RequestMapping(value = "/loanSpecialList")
		@ResponseBody
		public Object loanSpecialList(HttpServletRequest request,
							   LoanApplicationListVO loanApplicationListVO,
							   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
							   @RequestParam(value = "pageNo",defaultValue = "1") int pageNo){

			Pagination<LoanApplicationListVO> loanApplicationListPaging = loanApplicationService.getLoanSpecialApplicationPaging(pageNo, pageSize, loanApplicationListVO,null);
			return loanApplicationListPaging;
		}

		/**
		 * 省心计划投标列表(前台展示)
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
								@RequestParam(value = "pageNo",defaultValue = "1") int pageNo){
			if(null == lendProductPublishId && !"".equals(lendProductPublishId)){
				return null;
			}
			Pagination<LenderRecordVO> result = lendOrderBidDetailService.findSXJHLendOrderDetailPaging(pageNo, pageSize, lendProductPublishId, LendOrderBidStatusEnum.BIDSUCCESS);
			return result;
		}

		/**
		 * 省心计划订单，修改收益分配方式
		 * @param request
		 * @param lendOrderId 省心订单
		 * @param profitReturnConfig 收益分配方式
		 * @return
		 */
	    @RequestMapping(value = "/profitReturnConfig")
	    @ResponseBody
	    public String profitReturnConfig(HttpServletRequest request,
				@RequestParam(value = "lendOrderId") Long lendOrderId,
				@RequestParam(value = "profitReturnConfig") String profitReturnConfig){
			try {
				//校验-参数必填
		        if(null == lendOrderId){
		        	return JsonView.JsonViewFactory.create().success(false).info("缺少必要的参数").toJson();
		        }
		        if(null == profitReturnConfig || "".equals(profitReturnConfig)){
		        	return JsonView.JsonViewFactory.create().success(false).info("缺少必要的参数").toJson();
		        }else if(!FinanceProfitReturnEnum.TO_CASH_ACCOUNT.getValue().equals(profitReturnConfig)
		        		&& !FinanceProfitReturnEnum.TO_FINANCE_ACCOUNT.getValue().equals(profitReturnConfig)){
		        	return JsonView.JsonViewFactory.create().success(false).info("必要的参数不合法").toJson();
				}

		        //加载此订单
		        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
		        if(null == lendOrder){
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

}
