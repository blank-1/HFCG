package com.xt.cfp.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.CreditorRightsFromWhereEnum;
import com.xt.cfp.core.constants.FeesItemTypeEnum;
import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.constants.LoanAppLendAuditStatusEnums;
import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.constants.PayChannelEnum;
import com.xt.cfp.core.constants.RepaymentPlanStateEnum;
import com.xt.cfp.core.constants.SubjectTypeEnum;
import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.CustomerCard;
import com.xt.cfp.core.pojo.DefaultInterestDetail;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.LoanApplicationFeesItem;
import com.xt.cfp.core.pojo.LoanFeesDetail;
import com.xt.cfp.core.pojo.PayOrderDetail;
import com.xt.cfp.core.pojo.RepaymentPlan;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.ext.CustomerCardVO;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.CustomerBasicSnapshotService;
import com.xt.cfp.core.service.CustomerCardService;
import com.xt.cfp.core.service.DefaultInterestDetailService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LoanApplicationFeesItemService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.LoanFeesDetailService;
import com.xt.cfp.core.service.PayService;
import com.xt.cfp.core.service.RechargeOrderService;
import com.xt.cfp.core.service.RepaymentPlanService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.VerifyService;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.FilterUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;

@Controller
@RequestMapping(value = "/jsp/financial")
public class FinancialController extends BaseController {

    @Autowired
    LoanApplicationService loanApplicationService;
    @Autowired
    private LoanApplicationFeesItemService loanApplicationFeesItemService;
    @Autowired
    CustomerBasicSnapshotService customerBasicSnapshotService;
    @Autowired
    CustomerCardService customerCardService;
    @Autowired
    LendOrderService lendOrderService;
    @Autowired
    PayService payService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private LoanFeesDetailService loanFeesDetailService;
    @Autowired
    private VerifyService verifyService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private DefaultInterestDetailService defaultInterestDetailService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private CreditorRightsService creditorRightsService;

    @RequestMapping(value = "/toWaitLoanList")
    public String toWaitLoanList() {

        return "jsp/financial/waitLoanList";
    }

    @RequestMapping(value = "/waitLoanList")
    @ResponseBody
    public Object waitLoanList(HttpServletRequest request,
                               @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                               @RequestParam(value = "page", defaultValue = "1") int pageNum,
                               @RequestParam(value = "loanApplicationCode", required = false) String loanApplicationCode,
                               @RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,
                               @RequestParam(value = "channel", required = false) String channel, @RequestParam(value = "loanType", required = false) String loanType,
                               @RequestParam(value = "realName", required = false) String realName,
                               @RequestParam(value = "idCard", required = false) String idCard, @RequestParam(value = "mobileNo", required = false) String mobileNo) {


        return loanApplicationService.getWaitLoanList(pageNum, pageSize, loanApplicationCode, loanApplicationName, channel, loanType, realName, idCard, mobileNo);
    }

    @RequestMapping(value = "/fullLoanApp")
    @ResponseBody
    public Object fullLoanApp(@RequestParam(value = "loanApplicationId", required = false) long loanApplicationId,
                              @RequestParam(value = "fullContent", required = false) String fullContent) {
        try {
            LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
            if (!LoanApplicationStateEnum.LOANAUDIT.getValue().equals(loanApplication.getApplicationState())) {
                return "借款申请状态不正确";
            } else if (!LoanAppLendAuditStatusEnums.FULL_AUDITING.getValue().equals(loanApplication.getLendState())) {
                return "借款申请状态不正确";
            } else {
                loanApplication.setLendState(LoanAppLendAuditStatusEnums.PASS.getValue());
                loanApplication.setApplicationState(LoanApplicationStateEnum.WAITMAKELOANAUDIT.getValue());

                loanApplicationService.updateLoanAndAddVerify(loanApplication, getCurrentUser().getAdminId(), fullContent);
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "满标复审失败";
        }


    }

    @RequestMapping(value = "/toRepaymentList")
    public String toRepaymentList(Long loanApplicationId) {
        if (loanApplicationId!=null){
            LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
            getRequest().setAttribute("loanApplicationCode",loanApplication.getLoanApplicationCode());
            getRequest().setAttribute("loanApplicationId",loanApplicationId);
        }
        return "jsp/financial/repaymentList";
    }

    @RequestMapping(value = "/repaymentList")
    @ResponseBody
	public Object repaymentList(HttpServletRequest request, 
								@RequestParam(value = "rows", defaultValue = "10") int pageSize,
								@RequestParam(value = "page", defaultValue = "1") int pageNum,
								@RequestParam(value = "loanApplicationCode", required = false) String loanApplicationCode,
								@RequestParam(value = "loanApplicationName", required = false) String loanApplicationName,
								@RequestParam(value = "channel", required = false) String channel, 
								@RequestParam(value = "loanType", required = false) String loanType,
								@RequestParam(value = "realName", required = false) String realName, 
								@RequestParam(value = "idCard", required = false) String idCard,
								@RequestParam(value = "mobileNo", required = false) String mobileNo,
								@RequestParam(value = "planState", required = false) String planState,
								@RequestParam(value = "beginRepaymentDay", required = false) String beginRepaymentDay,
								@RequestParam(value = "endRepaymentDay", required = false) String endRepaymentDay) {
    	
    	//模糊查询字段，去空格
    	boolean isLegal = true;
    	if(null != loanApplicationCode && !"".equals(loanApplicationCode)){
    		loanApplicationCode = loanApplicationCode.replaceAll(" ", "");
    		if(FilterUtil.isSpecialCharacters(loanApplicationCode)){
    			isLegal = false;
            }
    	}
    	if(null != loanApplicationName && !"".equals(loanApplicationName)){
    		loanApplicationName = loanApplicationName.replaceAll(" ", "");
    		if(FilterUtil.isSpecialCharacters(loanApplicationName)){
    			isLegal = false;
            }
    	}
    	if(null != realName && !"".equals(realName)){
    		realName = realName.replaceAll(" ", "");
    		if(FilterUtil.isSpecialCharacters(realName)){
    			isLegal = false;
            }
    	}
    	if(null != mobileNo && !"".equals(mobileNo)){
    		mobileNo = mobileNo.replaceAll(" ", "");
    		if(FilterUtil.isSpecialCharacters(mobileNo)){
    			isLegal = false;
            }
    	}
    	if(!isLegal){
    		return new Pagination();
    	}
		return loanApplicationService.getRepaymentList(pageNum, pageSize, loanApplicationCode, loanApplicationName, channel, loanType, realName,
				idCard, mobileNo, planState, beginRepaymentDay, endRepaymentDay);
    }

    @RequestMapping(value = "/toMakeLoan")
    public ModelAndView toMakeLoan(@RequestParam(value = "loanApplicationId", required = true) long loanApplicationId) {

        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);

        UserInfoVO userInfoVO = userInfoService.getUserExtByUserId(loanApplication.getUserId());
        CustomerCardVO customerCard = customerCardService.getCustomerCardVOById(loanApplication.getInCardId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("loanApplication", loanApplication);
        if (loanApplication.getSubjectType().equals(SubjectTypeEnum.CREDITOR.getValue())) {
            modelAndView.addObject("subjectTypeName", SubjectTypeEnum.CREDITOR.getDesc());
        } else {
            modelAndView.addObject("subjectTypeName", SubjectTypeEnum.LOAN.getDesc());
        }
        modelAndView.addObject("applicationStateName", LoanApplicationStateEnum.getByValue(loanApplication.getApplicationState()).getDesc());

        modelAndView.addObject("user", userInfoVO);
        modelAndView.addObject("customerCard", customerCard);

        List<LoanApplicationFeesItem> applicationFeesItems = loanApplicationFeesItemService.getByLoanApplicationIdAndFeePoint(loanApplicationId, FeesPointEnum.ATMAKELOAN);
        BigDecimal sumFees = BigDecimal.valueOf(0l);
        BigDecimal zeroBigDecimal = BigDecimal.valueOf(0l);
        for (LoanApplicationFeesItem applicationFeesItem : applicationFeesItems) {
            if (applicationFeesItem.getChargeCycle() == FeesPointEnum.ATMAKELOAN.value2Char()) {
                sumFees = sumFees.add(loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(
                        applicationFeesItem, loanApplication.getConfirmBalance(), loanApplication.getInterestBalance(), zeroBigDecimal, zeroBigDecimal, zeroBigDecimal, zeroBigDecimal));
            }
        }
        sumFees = BigDecimalUtil.up(sumFees, 2);
        modelAndView.addObject("fees", sumFees);
        modelAndView.addObject("makeLoanBalance", loanApplication.getConfirmBalance().subtract(sumFees));
        modelAndView.setViewName("jsp/financial/makeLoan");
        return modelAndView;
    }

    @RequestMapping(value = "/makeLoan")
    @ResponseBody
    public Object makeLoan(@RequestParam(value = "loanApplicationId", required = true) long loanApplicationId,
                           @RequestParam(value = "desc", required = true) String desc) {

        Date now = new Date();
        try {
            AdminInfo currentUser = super.getCurrentUser();
            loanApplicationService.submitMakeLoan(loanApplicationId, currentUser.getAdminId(), desc, PayChannelEnum.YEEPAY.value2Char(), now, now);
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        return "success";
    }

	@RequestMapping(value = "/createAgreement")
	@ResponseBody
	public Object createAgreement(@RequestParam(value = "loanApplicationId", required = true) long loanApplicationId) {
		try {
			loanApplicationService.createAllAgreement(loanApplicationId);
		} catch (Exception e) {
			return "failure";
		}
		return "success";
	}
    
    @RequestMapping(value = "/toFullSubjectReview")
    public ModelAndView toFullSubjectReview(@RequestParam(value = "loanApplicationId", required = false) Long loanApplicationId) {
        ModelAndView mv = new ModelAndView();
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);
        UserInfoVO uvo = userInfoService.getUserExtByUserId(loanApplication.getUserId());
        CustomerCard customerCard = customerCardService.findById(loanApplication.getInCardId());
        mv.addObject("customerCard", customerCard);
        mv.addObject("loanApplication", loanApplication);
        mv.addObject("cbs", uvo);
        mv.setViewName("/jsp/financial/fullSubjectReview");
        return mv;
    }

    @RequestMapping(value = "/lenderInformation")
    @ResponseBody
    public Object lenderInformation(HttpServletRequest request,
                                    @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "page", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "loanApplicationId") Long loanApplicationId) {
        System.out.println("aaaaa=" + loanApplicationId);
        return lendOrderService.getLenderInformationById(pageSize, pageNum, loanApplicationId);
    }

    @RequestMapping(value = "/paymentOrderDetail")
    @ResponseBody
    public Object paymentOrderDetail(HttpServletRequest request,
                                     @RequestParam(value = "lendOrderId") Long lendOrderId,
                                     @RequestParam(value = "productType") String productType) {
        System.out.println(lendOrderId + " " + productType);
        List<PayOrderDetail> payOrderDetailList = new ArrayList<PayOrderDetail>();
        if (LendProductTypeEnum.RIGHTING.getValue().equals(productType)) {//投标
            payOrderDetailList = payService.getPaymentOrderDetail(lendOrderId);
            return payOrderDetailList;
        } else if (LendProductTypeEnum.FINANCING.getValue().equals(productType)) {//理财
            return payOrderDetailList;
        }
        return payOrderDetailList;
    }

    @RequestMapping(value = "/toRepayment")
    public ModelAndView toRepayment(@RequestParam(value = "repaymentPlanId") Long repaymentPlanId) {

        ModelAndView modelAndView = new ModelAndView();
        RepaymentPlan repaymentPlan = repaymentPlanService.findById(repaymentPlanId);
        Map<String, BigDecimal> map = calculateBalanceByDate(repaymentPlan.getLoanApplicationId(), repaymentPlan.getRepaymentDay());
        modelAndView.addObject("repaymentPlanId", repaymentPlanId);
        modelAndView.addObject("loanApplicationId",repaymentPlan.getLoanApplicationId());
        modelAndView.addObject("balanceInfo",map);
        modelAndView.setViewName("jsp/financial/repayment");
        return modelAndView;
    }

    @RequestMapping(value = "/toAheadRepayment")
    public ModelAndView toAheadRepayment(@RequestParam(value = "repaymentPlanId") Long repaymentPlanId) {

        ModelAndView modelAndView = new ModelAndView();
        RepaymentPlan repaymentPlan = repaymentPlanService.findById(repaymentPlanId);
        Map<String, BigDecimal> map = calculateBalanceByDate(repaymentPlan.getLoanApplicationId(), repaymentPlan.getRepaymentDay(),true);
        modelAndView.addObject("repaymentPlanId", repaymentPlanId);
        modelAndView.addObject("loanApplicationId", repaymentPlan.getLoanApplicationId());
        modelAndView.addObject("balanceInfo", map);
        modelAndView.addObject("isAhead", true);
        modelAndView.setViewName("jsp/financial/aheadRepayment");
        return modelAndView;
    }
    
    @RequestMapping(value = "/repayment")
    @ResponseBody
    public String repayment(@RequestParam(value = "loanApplicationId") Long loanApplicationId,
                            @RequestParam(value = "repaymentPlanId") Long repaymentPlanId,
                            @RequestParam(value = "repaymentDate") String repaymentDate,
                            @RequestParam(value = "ignoreDelay", required = false) String ignoreDelayStr,
                            @RequestParam(value = "repaymentBalance") double repaymentBalance,
                            @RequestParam(value = "paidBalance") double paidBalance,
                            @RequestParam(value = "desc") String desc,
                            @RequestParam(value="isAheadStr") String isAheadStr 
    ) {


        try {
            AdminInfo currentUser = super.getCurrentUser();
            boolean ignoreDelay = false;
            if (!StringUtils.isNull(ignoreDelayStr)) {
                ignoreDelay = Boolean.valueOf(ignoreDelayStr);
            }
            boolean isAhead = false ;
            if(!StringUtils.isNull(isAheadStr)){
            	isAhead = Boolean.valueOf(isAheadStr);
            }
            List<Long> repaymentIdList = new ArrayList<>();
            Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap = repaymentPlanService.repayment(loanApplicationId,repaymentPlanId, DateUtil.parseStrToDate(repaymentDate, "yyyy-MM-dd"),
                    BigDecimal.valueOf(repaymentBalance), BigDecimal.valueOf(paidBalance), currentUser.getAdminId(), ignoreDelay, desc,isAhead,repaymentIdList);
            try {
            	//【省心计划】处理省心计划子订单--需要将本金以外的金额转至资金账户
            	repaymentPlanService.handleFinanceChildOrder(resultMap);
            } catch (Exception e) {
            	logger.error("省心计划收益转入余额方法执行异常，异常原因：" , e );
            }
            try {
				//调用省心计划退出方法
            	lendOrderService.scanFinanceOrderAndDoQuit();
			} catch (Exception e) {
				logger.error("省心计划退出方法执行异常，异常原因：" , e );
			}
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }

    @RequestMapping(value = "/calculateBalance")
    @ResponseBody
    public Object calculateBalance(@RequestParam(value = "loanApplicationId") Long loanApplicationId,
                                   @RequestParam(value = "repaymentDate") Date repaymentDate) {

        Map<String, BigDecimal> map = calculateBalanceByDate(loanApplicationId, repaymentDate);
        Map resultMap = new HashMap();
        resultMap.put("data", map);
        return resultMap;
    }

    private Map<String, BigDecimal> calculateBalanceByDate(Long loanApplicationId, Date repaymentDate) {
        List<RepaymentPlan> repaymentPlanVOs = repaymentPlanService.getRepaymentPlanList(loanApplicationId, repaymentDate);
        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
        BigDecimal loanBalance = loanApplication.getLoanBalance();
        BigDecimal allInterest = loanApplication.getInterestBalance();
        BigDecimal zeroBigDecimal = BigDecimal.valueOf(0l);
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        List<LoanApplicationFeesItem> applicationFeesItems = loanApplicationFeesItemService.getByLoanApplicationIdAndFeePoint(loanApplicationId, FeesPointEnum.ATCYCLE);
        List<LoanApplicationFeesItem> delayFees = loanApplicationFeesItemService.getByLoanApplicationIdAndFeePoint(loanApplicationId, FeesPointEnum.ATDELAY_FIRSTDAY);
        List<LoanFeesDetail> loanFeesDetails = loanFeesDetailService.getLoanFeesDetailByLoanId(loanApplicationId);
        BigDecimal balance = zeroBigDecimal;
        BigDecimal capital = zeroBigDecimal;
        BigDecimal interest = zeroBigDecimal;
        BigDecimal defaultInterest = zeroBigDecimal;
        BigDecimal feesBalance = zeroBigDecimal;

        for (RepaymentPlan repaymentPlan : repaymentPlanVOs) {
            balance = balance.add(repaymentPlan.getShouldBalance2()).subtract(repaymentPlan.getFactBalance());
            capital = capital.add(repaymentPlan.getShouldCapital2().subtract(repaymentPlan.getFactCalital()));
            interest = interest.add(repaymentPlan.getShouldInterest2().subtract(repaymentPlan.getFactInterest()));
            for (LoanApplicationFeesItem feesItem : applicationFeesItems) {
//                feesBalance = feesBalance.add(loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(feesItem, loanBalance, allInterest, repaymentPlan.getShouldCapital2(),
//                        repaymentPlan.getShouldInterest2(), zeroBigDecimal, zeroBigDecimal));
                feesBalance = feesBalance.add(repaymentPlanService.getDefaultBalance(feesItem.getFeesRate(), feesItem.getRadicesType(), repaymentPlan.getShouldCapital2(), repaymentPlan.getShouldInterest2(), 
        				loanBalance, allInterest, repaymentPlan.getFactCalital(), repaymentPlan.getFactInterest(), zeroBigDecimal, zeroBigDecimal));
                for (LoanFeesDetail feesDetail : loanFeesDetails) {
                    if (feesDetail.getLoanApplicationFeesItemId() == feesItem.getLoanApplicationFeesItemId() && feesDetail.getSectionCode() == repaymentPlan.getSectionCode()) {
                        feesBalance = feesBalance.subtract(feesDetail.getPaidFees());
                    }
                }
            }
            for (LoanApplicationFeesItem feesItem : delayFees) {
                if (feesItem.getChargeCycle() == FeesPointEnum.ATDELAY_FIRSTDAY.value2Char() && repaymentPlan.getPlanState() == RepaymentPlanStateEnum.DEFAULT.value2Char()) {
                    for (LoanFeesDetail feesDetail : loanFeesDetails ) {
                        if (feesDetail.getLoanApplicationFeesItemId() == feesItem.getLoanApplicationFeesItemId() && feesDetail.getSectionCode() == repaymentPlan.getSectionCode()) {
                            feesBalance = feesBalance.add(feesDetail.getFees2().subtract(feesDetail.getPaidFees()!=null?feesDetail.getPaidFees():zeroBigDecimal));
                        }
                    }
                }
            }
            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("UNCOMPLETE_WAIVER", "true");
            paraMap.put("repaymentPlanId", repaymentPlan.getRepaymentPlanId());
            try {
                List<DefaultInterestDetail> defaultInterestDetails = defaultInterestDetailService.findBy(paraMap);
                for (DefaultInterestDetail interestDetail : defaultInterestDetails) {
                    defaultInterest = defaultInterest.add(interestDetail.getInterestBalance2().subtract(interestDetail.getRepaymentBalance()!=null ? interestDetail.getRepaymentBalance() : BigDecimal.ZERO));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        balance = balance.add(feesBalance).add(defaultInterest);
        map.put("balance", balance);
        map.put("capital", capital);
        map.put("interest", interest);
        map.put("feesBalance", feesBalance);
        map.put("defaultInterest", defaultInterest);
        return map;
    }

    @RequestMapping(value = "/getUserBalance")
    @ResponseBody
    public Object getUserBalance(@RequestParam(value = "loanApplicationId") Long loanApplicationId) {
        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
        UserAccount userAccount = userAccountService.getUserAccountByAccId(loanApplication.getRepaymentAccountId());
        if (userAccount != null) {
            return userAccount.getAvailValue2().toPlainString();
        } else {
            return "";
        }
    }

    /**
     * 导出excel
     * @param request
     * @param response
     * @param loanApplicationId
     */
    @RequestMapping(value = "exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "loanApplicationId") Long loanApplicationId ){
    	rechargeOrderService.exportExcelByLoanAppId(response,loanApplicationId);
	}
    
    /**
     * to平台流水
     * @return
     */
    @RequestMapping(value = "/toSystemFlowList")
    public String toSystemFlowList(HttpServletRequest request) {
    	//费用类型
        request.setAttribute("businessType",AccountConstants.BusinessTypeEnum.values());
        return "jsp/financial/systemFlowList";
    }

    /**
     * 计算还款金额
     * @param loanApplicationId
     * @param repaymentDate
     * @param isAhead 是否提前还款(全部还清)
     * @return
     */
    private Map<String, BigDecimal> calculateBalanceByDate(Long loanApplicationId, Date repaymentDate,boolean isAhead) {
        //提前还款
        Date queryDate = null;
        if (!isAhead)
            queryDate = repaymentDate;
        List<RepaymentPlan> repaymentPlanVOs = repaymentPlanService.getRepaymentPlanList(loanApplicationId, queryDate);
        LoanApplication loanApplication = loanApplicationService.findById(loanApplicationId);
        BigDecimal loanBalance = loanApplication.getLoanBalance();
        BigDecimal allInterest = loanApplication.getInterestBalance();
        BigDecimal zeroBigDecimal = BigDecimal.valueOf(0l);
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        List<LoanApplicationFeesItem> applicationFeesItems = loanApplicationFeesItemService.getByLoanApplicationIdAndFeePoint(loanApplicationId, FeesPointEnum.ATCYCLE);
        List<LoanApplicationFeesItem> delayFees = loanApplicationFeesItemService.getByLoanApplicationIdAndFeePoint(loanApplicationId, FeesPointEnum.ATDELAY_FIRSTDAY);
        List<LoanFeesDetail> loanFeesDetails = loanFeesDetailService.getLoanFeesDetailByLoanId(loanApplicationId);

        BigDecimal balance = zeroBigDecimal;
        BigDecimal capital = zeroBigDecimal;
        BigDecimal interest = zeroBigDecimal;
        BigDecimal defaultInterest = zeroBigDecimal;
        BigDecimal feesBalance = zeroBigDecimal;

        for (RepaymentPlan repaymentPlan : repaymentPlanVOs) {
            if (!repaymentDate.before(repaymentPlan.getRepaymentDay())){
                //当期以及前期未结清部分计算
                balance = balance.add(repaymentPlan.getShouldBalance2()).subtract(repaymentPlan.getFactBalance());
                capital = capital.add(repaymentPlan.getShouldCapital2().subtract(repaymentPlan.getFactCalital()));
                interest = interest.add(repaymentPlan.getShouldInterest2().subtract(repaymentPlan.getFactInterest()));

                for (LoanApplicationFeesItem feesItem : applicationFeesItems) {
                    feesBalance = feesBalance.add(repaymentPlanService.getDefaultBalance(feesItem.getFeesRate(), feesItem.getRadicesType(), repaymentPlan.getShouldCapital2(), repaymentPlan.getShouldInterest2(),
                            loanBalance, allInterest, repaymentPlan.getFactCalital(), repaymentPlan.getFactInterest(), zeroBigDecimal, zeroBigDecimal));
                    for (LoanFeesDetail feesDetail : loanFeesDetails) {
                        if (feesDetail.getLoanApplicationFeesItemId() == feesItem.getLoanApplicationFeesItemId() && feesDetail.getSectionCode() == repaymentPlan.getSectionCode()) {
                            feesBalance = feesBalance.subtract(feesDetail.getPaidFees());
                        }
                    }
                }
                for (LoanApplicationFeesItem feesItem : delayFees) {
                    if (feesItem.getChargeCycle() == FeesPointEnum.ATDELAY_FIRSTDAY.value2Char() && repaymentPlan.getPlanState() == RepaymentPlanStateEnum.DEFAULT.value2Char()) {
                        for (LoanFeesDetail feesDetail : loanFeesDetails) {
                            if (feesDetail.getLoanApplicationFeesItemId() == feesItem.getLoanApplicationFeesItemId() && feesDetail.getSectionCode() == repaymentPlan.getSectionCode()) {
                                feesBalance = feesBalance.add(feesDetail.getFees2().subtract(feesDetail.getPaidFees() != null ? feesDetail.getPaidFees() : zeroBigDecimal));
                            }
                        }
                    }
                }
            }else{
                balance = balance.add(repaymentPlan.getShouldCapital2().subtract(repaymentPlan.getFactCalital()));
                capital = capital.add(repaymentPlan.getShouldCapital2().subtract(repaymentPlan.getFactCalital()));
            }

            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("UNCOMPLETE_WAIVER", "true");
            paraMap.put("repaymentPlanId", repaymentPlan.getRepaymentPlanId());
            try {
                List<DefaultInterestDetail> defaultInterestDetails = defaultInterestDetailService.findBy(paraMap);
                for (DefaultInterestDetail interestDetail : defaultInterestDetails) {
                    defaultInterest = defaultInterest.add(interestDetail.getInterestBalance2().subtract(interestDetail.getRepaymentBalance() != null ? interestDetail.getRepaymentBalance() : BigDecimal.ZERO));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isAhead){
            List<LoanApplicationFeesItem> loanApplicationFeesItems = loanApplicationFeesItemService.getByLoanApplicationId(loanApplicationId);

            for (LoanApplicationFeesItem applicationFeesItem : loanApplicationFeesItems) {

                if (applicationFeesItem.getItemType().equals(String.valueOf(FeesItemTypeEnum.ITEMTYPE_EARLYREPAYMENT.getValue()))) {
                    //计算提前还款费用
                    BigDecimal fee = loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(applicationFeesItem, loanApplication.getConfirmBalance(), loanApplication.getInterestBalance(), BigDecimal.ZERO,
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                    BigDecimal upFee = BigDecimalUtil.up(fee, 2);
                    map.put("aheadFee", upFee.setScale(2));
                    balance = balance.add(fee);
                }
            }
        }
        balance = balance.add(feesBalance).add(defaultInterest);


        map.put("balance", balance);
        map.put("capital", capital);
        map.put("interest", interest);
        map.put("feesBalance", feesBalance);
        map.put("defaultInterest", defaultInterest);
        return map;
    }
}
