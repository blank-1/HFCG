package com.xt.cfp.core.service.financePlan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.BidErrorCode;
import com.xt.cfp.core.Exception.code.ext.LendOrderErrorCode;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.CRMatchRuleEnum;
import com.xt.cfp.core.constants.CreditorRightsConstants;
import com.xt.cfp.core.constants.LendOrderConstants;
import com.xt.cfp.core.constants.LendOrderReceiveStateEnum;
import com.xt.cfp.core.constants.LendProductClosingType;
import com.xt.cfp.core.constants.LendProductTimeLimitType;
import com.xt.cfp.core.constants.LendProductTypeEnum;
import com.xt.cfp.core.constants.RepaymentPlanStateEnum;
import com.xt.cfp.core.constants.TemplateType;
import com.xt.cfp.core.constants.VisiableEnum;
import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.FeesItem;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendOrderFeesDetail;
import com.xt.cfp.core.pojo.LendOrderReceive;
import com.xt.cfp.core.pojo.LendProduct;
import com.xt.cfp.core.pojo.LendProductFeesItem;
import com.xt.cfp.core.pojo.LendProductPublish;
import com.xt.cfp.core.pojo.RepaymentPlan;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.pojo.UserAccount;
import com.xt.cfp.core.pojo.ext.LendOrderExtProduct;
import com.xt.cfp.core.service.ConstantDefineCached;
import com.xt.cfp.core.service.CreditorRightsService;
import com.xt.cfp.core.service.CreditorRightsTransferAppService;
import com.xt.cfp.core.service.FeesItemService;
import com.xt.cfp.core.service.LendLoanBindingService;
import com.xt.cfp.core.service.LendOrderBidDetailService;
import com.xt.cfp.core.service.LendOrderDetailFeesService;
import com.xt.cfp.core.service.LendOrderReceiveService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.LendProductService;
import com.xt.cfp.core.service.LoanApplicationService;
import com.xt.cfp.core.service.PayService;
import com.xt.cfp.core.service.RepaymentPlanService;
import com.xt.cfp.core.service.RightsRepaymentDetailService;
import com.xt.cfp.core.service.UserAccountOperateService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.matchrules.CreditorMatchRules;
import com.xt.cfp.core.service.matchrules.MatchCreditorVO;
import com.xt.cfp.core.util.BeanUtil;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.FinanceCalculateUtils;
import com.xt.cfp.core.util.StringUtils;

/**
 * Created by lenovo on 2015/12/8.
 */
@Service
public class FinancePlanProcess extends FinancePlanProcessModule {
	private static Logger logger = Logger.getLogger(FinancePlanProcess.class);

	@Autowired
	private LendOrderReceiveService lendOrderReceiveService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private UserAccountOperateService userAccountOperateService;
	@Autowired
	private LendProductService lendProductService;
	@Autowired
	private FeesItemService feesItemService;
	@Autowired
	private LendOrderService lendOrderService;
	@Autowired
	private ConstantDefineCached constantDefineCached;
	@Autowired
	private CreditorRightsService creditorRightsService;
	@Autowired
	private CreditorRightsTransferAppService creditorRightsTransferAppService;
	@Autowired
	private LendOrderBidDetailService lendOrderBidDetailService ;
	@Autowired
	private PayService payService ;
	@Autowired
	private RepaymentPlanService repaymentPlanService ;
	@Autowired
	private RightsRepaymentDetailService rightsRepaymentDetailService;
	@Autowired
	private LoanApplicationService loanApplicationService;
	@Autowired
	private LendOrderDetailFeesService lendOrderDetailFeesService;
	@Autowired
    private LendLoanBindingService lendLoanBindingService;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void creditorRightsAutoMatch(LendOrder lendOrder) throws Exception{
		if (lendOrder==null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("lendOrder",lendOrder);
		if (!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
			throw new SystemException(LendOrderErrorCode.NOT_MATCH_LENDERORDER_PRODUCTTYPE).set("productType", lendOrder.getProductType());
		if (! lendOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue()) &&
        		!lendOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.HASPAID.getValue())) {
			throw new SystemException(LendOrderErrorCode.FAIL_ORDER_STATE).set("orderState", lendOrder.getOrderState());
        }
		logger.debug("\t开始匹配省心计划 ：" + lendOrder.getOrderCode());
		LendOrder financeOrder = lendOrderService.findAndLockById(lendOrder.getLendOrderId());
		//todoed 改为如果省心计划的已得收益大于等于预期收益，则进入返息匹配
		List<MatchCreditorVO> matchCreditorVOs = new ArrayList<MatchCreditorVO>();
		matchCreditorVOs.addAll(CreditorMatchRules.match(financeOrder, CRMatchRuleEnum.MAXLIMIT.getValue()));

		BigDecimal newRightBalance = BigDecimal.valueOf(0);
		for (MatchCreditorVO matchCreditorVO : matchCreditorVOs) {
			newRightBalance = newRightBalance.add(matchCreditorVO.getBalance());
		}
//		boolean result = false;
//		Map<String, Object> lendOrderMap = new HashMap<String, Object>();
//		lendOrderMap.put("lendOrderId", financeOrder.getLendOrderId());
//		lendOrderMap.put("orderState", LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue());
//		lendOrderService.update(lendOrderMap);
		if(!financeOrder.getOrderState().equals( LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue())){
			financeOrder.setOrderState(LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue());
			lendOrderService.update(financeOrder);
		}
//		//匹配完成后修改订单贷理财金额
//		if (BigDecimalUtil.compareTo(newRightBalance,new BigDecimal("0"),2)>0&&
//				LendOrderConstants.FinanceOrderStatusEnum.HASPAID.getValue().equals(lendOrder.getOrderState())){
//			//待理财金额小于100,标记状态为  理财中
//			result = true;
//		}
		logger.debug("\t省心计划 ：" + financeOrder.getOrderCode() + "匹配完成，开始起息");
	}

	/**
	 * 计算每一期的应还金额
	 * @param lendOrder 出借合同
	 * @param profitRate 费率
	 * @param toInterestPoint 返息周期
	 * @return
	 */
	public BigDecimal getShouldReceiveInterest(LendOrder lendOrder, BigDecimal profitRate, String toInterestPoint) {
		BigDecimal balance = lendOrder.getBuyBalance();
		BigDecimal shouldReceiveInterest = new BigDecimal(0);
		if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_WEEK)) {
			shouldReceiveInterest = balance.multiply(profitRate)
					.divide(new BigDecimal(LendProduct.TIMELIMITTYPE_DAY_YEAR), 18, BigDecimal.ROUND_CEILING)
					.multiply(new BigDecimal(LendProduct.TIMELIMITTYPE_DAY_WEEK));
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_MONTH)) {
			shouldReceiveInterest = balance.multiply(profitRate)
					.divide(new BigDecimal(12), 18, BigDecimal.ROUND_CEILING);
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_QUARTER)) {
			shouldReceiveInterest = balance.multiply(profitRate)
					.divide(new BigDecimal(12), 18, BigDecimal.ROUND_CEILING).multiply(new BigDecimal(3));
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_HALFYEAR)) {
			shouldReceiveInterest = balance.multiply(profitRate)
					.divide(new BigDecimal(12), 18, BigDecimal.ROUND_CEILING).multiply(new BigDecimal(6));
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_YEAR)) {
			shouldReceiveInterest = balance.multiply(profitRate)
					.divide(new BigDecimal(12), 18, BigDecimal.ROUND_CEILING);
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_BEQUIT)) {
			if (lendOrder.getTimeLimitType().equals(LendProductTimeLimitType.TIMELIMITTYPE_DAY.getValue())) {
				shouldReceiveInterest = balance.multiply(profitRate)
						.divide(new BigDecimal(LendProduct.TIMELIMITTYPE_DAY_YEAR), 18, BigDecimal.ROUND_CEILING)
						.multiply(new BigDecimal(lendOrder.getTimeLimit()));
			}else if (lendOrder.getTimeLimitType().equals(LendProductTimeLimitType.TIMELIMITTYPE_MONTH.getValue())){
				shouldReceiveInterest = balance.multiply(profitRate)
						.divide(new BigDecimal(12), 18, BigDecimal.ROUND_CEILING)
						.multiply(new BigDecimal(lendOrder.getTimeLimit()));
			}
		}
		return shouldReceiveInterest;
	}

	/**
	 * 计算期数
	 * @param timeLimitType 期限类型
	 * @param timeLimit 期限值
	 * @param toInterestPoint 返息周期类型
	 * @return
	 */
	public int getPeriods(String timeLimitType, int timeLimit, String toInterestPoint) {
		int periods = 1;

		if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_WEEK)) {
			if (timeLimitType.equals(LendProductClosingType.CLOSINGTYPE_MONTH.getValue())) {
				periods = timeLimit * LendProduct.TIMELIMITTYPE_DAY_MONTH % LendProduct.TIMELIMITTYPE_DAY_WEEK == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_WEEK : timeLimit / LendProduct.TIMELIMITTYPE_DAY_WEEK + 1;
			} else if (timeLimitType.equals(LendProductClosingType.CLOSINGTYPE_DAY.getValue())) {
				periods = timeLimit % LendProduct.TIMELIMITTYPE_DAY_WEEK == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_WEEK : timeLimit / LendProduct.TIMELIMITTYPE_DAY_WEEK + 1;
			}
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_MONTH)) {
			if (timeLimitType.equals(LendProductClosingType.CLOSINGTYPE_MONTH.getValue())) {
				periods = timeLimit;
			} else if (timeLimitType.equals(LendProductClosingType.CLOSINGTYPE_DAY.getValue())) {
				periods = timeLimit % LendProduct.TIMELIMITTYPE_DAY_MONTH == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_MONTH : timeLimit / LendProduct.TIMELIMITTYPE_DAY_MONTH + 1;
			}
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_QUARTER)) {
			if (timeLimitType.equals(LendProductClosingType.CLOSINGTYPE_MONTH.getValue())) {
				periods = timeLimit % 3 == 0 ? timeLimit / 3 : timeLimit / 3 + 1;
			} else if (timeLimitType.equals(LendProductClosingType.CLOSINGTYPE_DAY.getValue())) {
				periods = timeLimit % LendProduct.TIMELIMITTYPE_DAY_QUARTER == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_QUARTER : timeLimit / LendProduct.TIMELIMITTYPE_DAY_QUARTER + 1;
			}
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_HALFYEAR)) {
			if (timeLimitType.equals(LendProductClosingType.CLOSINGTYPE_MONTH.getValue())) {
				periods = timeLimit % 6 == 0 ? timeLimit / 6 : timeLimit / 6 + 1;
			} else if (timeLimitType.equals(LendProductClosingType.CLOSINGTYPE_DAY.getValue())) {
				periods = timeLimit % LendProduct.TIMELIMITTYPE_DAY_HALFYEAR == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_HALFYEAR : timeLimit / LendProduct.TIMELIMITTYPE_DAY_HALFYEAR + 1;
			}
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_YEAR)) {
			if (timeLimitType.equals(LendProductClosingType.CLOSINGTYPE_MONTH.getValue())) {
				periods = timeLimit % 12 == 0 ? timeLimit / 12 : timeLimit / 12 + 1;
			} else if (timeLimitType.equals(LendProductClosingType.CLOSINGTYPE_DAY.getValue())) {
				periods = timeLimit % LendProduct.TIMELIMITTYPE_DAY_YEAR == 0 ? timeLimit / LendProduct.TIMELIMITTYPE_DAY_YEAR : timeLimit / LendProduct.TIMELIMITTYPE_DAY_YEAR + 1;
			}
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_BEQUIT)) {
			periods = 1;
		}

		return periods;

	}

	@Override
	@Transactional
	public void beginCalcInterest(LendOrder lendOrder,Date date) throws SystemException {
		//判断订单不为空并且是省心计划订单
		if (lendOrder==null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("lendOrder",lendOrder);
		if (!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))
			throw new SystemException(LendOrderErrorCode.NOT_MATCH_LENDERORDER_PRODUCTTYPE).set("productType", lendOrder.getProductType());
		//修改订单状态//设置省心计划生效的起止日期
		if(lendOrder.getAgreementStartDate() == null){
			lendOrder.setAgreementStartDate(date);
			FinanceCalculateUtils.calAndSetAgreementEndDate(lendOrder);
//			FinanceCalculateUtils.calAndSetClosingOverDate(lendOrder);
			lendOrderService.update(lendOrder);
		}
		//生成合同
		try {
			loanApplicationService.createFinanceAgreementAndFile(lendOrder, date);
		} catch (Exception e) {
			logger.error("省心计划生成合同失败，失败原因：" ,  e );
		}
	}

	/**
	 * 根据当前时间，周期数，周期类型返回对应周期的日期
	 * @param now
	 * @param sectionCode
	 * @param toInterestPoint
	 * */
	private Date getCurrentPeriodsReceiveDate(Date now, int sectionCode,
			String toInterestPoint) {
		Date newDate = null ;
		int betweenDays = 0 ;
		if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_WEEK)) {
			newDate = DateUtil.addDate(now, sectionCode * LendProduct.TIMELIMITTYPE_DAY_WEEK);
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_MONTH)) {
			newDate = DateUtil.addMonth(now, sectionCode);
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_QUARTER)) {
			betweenDays = sectionCode * LendProduct.TIMELIMITTYPE_DAY_QUARTER ;
			newDate = DateUtil.addDate(now, betweenDays);
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_HALFYEAR)) {
			betweenDays = sectionCode * LendProduct.TIMELIMITTYPE_DAY_HALFYEAR ;
			newDate = DateUtil.addDate(now, betweenDays);
		} else if (toInterestPoint.equals(LendProduct.TOINTERESTPOINT_YEAR)) {
			betweenDays = sectionCode * LendProduct.TIMELIMITTYPE_DAY_YEAR ;
			newDate = DateUtil.addYear(now, sectionCode);
		} 
		return newDate;
	}

	@Override
	public void cycleCoupon() {

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void exitPlan(Long lendOrderId) {
		LendOrder financeOrder = lendOrderService.findAndLockById(lendOrderId);
		// 校验-是否是省心计划
		if (!LendProductTypeEnum.FINANCING.getValue().equals(financeOrder.getProductType()))
			throw new SystemException(BidErrorCode.IS_NOT_A_FINANCE_ORDER).set("lendOrderId", lendOrderId);
		financeCycleAccount(financeOrder);
		//退出状态
		List<LendOrder> orderList = lendOrderService.findUnRepayOrdersByFinanceOrder(financeOrder);
		if(orderList == null || orderList.size() == 0){
			if(financeOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue())){
				//发送授权期到期短信 --SMS_FINANCEPLAN_EXPIRED_VM
				try {
					payService.sendFinanceResultMsg(financeOrder,TemplateType.SMS_FINANCEPLAN_EXPIRED_VM);
				} catch (Exception e) {
					e.printStackTrace();logger.error("发送授权期结束短信失败，失败原因：" , e);
				}
			}
			//结清短信通知
			try {
				payService.sendFinanceResultMsg(financeOrder,TemplateType.SMS_FINANCEPLAN_LAST_RETURN_BALANCE_VM);
			} catch (Exception e) {
				e.printStackTrace();logger.error("发送授省心计划结清短信失败，失败原因：" , e);
			}
			this.lendOrderService.updateLendOrderStatus(lendOrderId, LendOrderConstants.FinanceOrderStatusEnum.CLEAR);
		}else{
			if(financeOrder.getOrderState().equals(LendOrderConstants.FinanceOrderStatusEnum.REPAYMENTING.getValue())){
				//发送退出中短信 --SMS_FINANCEPLAN_EXPIRED_VM
				try {
					payService.sendFinanceResultMsg(financeOrder,TemplateType.SMS_FINANCEPLAN_EXPIRED_VM);
				} catch (Exception e) {
					e.printStackTrace();logger.error("发送授权期结束短信失败，失败原因：" , e);
				}
				this.lendOrderService.updateLendOrderStatus(lendOrderId, LendOrderConstants.FinanceOrderStatusEnum.QUITING);
			}
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void reApplyTurnRights(Long creditorRightId) {
		CreditorRights creditorRights = creditorRightsService.findById(creditorRightId, false);
		List<RightsRepaymentDetail> detailRightsList = rightsRepaymentDetailService.getDetailListByRightsId(creditorRightId);
        int surpMonth = 0;
        for(RightsRepaymentDetail detailRights:detailRightsList){
            if(CreditorRightsConstants.CreditorRightsStateEnum.RightsRepaymentDetailIsPayOffEnum.ISPAYOFF_NO.getValue().equals(String.valueOf(detailRights.getIsPayOff()))){
                surpMonth++;
            }
        }
        creditorRightsService.turnCreditor(creditorRights.getRightsWorth(),creditorRights.getCreditorRightsId(),surpMonth);
	}

	//根据债权获取未还清的期数
	private Integer getLeftTimeLimit(long loanApplicationId) {
		int result = 0; 
		List<RepaymentPlan> repayPlanList = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplicationId);
		for(RepaymentPlan plan : repayPlanList){
			if(plan.getPlanState() == RepaymentPlanStateEnum.UNCOMPLETE.value2Char() 
					|| plan.getPlanState() == RepaymentPlanStateEnum.PART.value2Char()
					|| plan.getPlanState() == RepaymentPlanStateEnum.DEFAULT.value2Char()){
				result ++ ;
			}
		}
		return result;
	}

	@Override
	public void clear() {

	}

	/**
	 * 计算溢出费
	 */
	@Override
	protected BigDecimal overflowFee(LendOrder lendOrder) {
		List<LendOrderReceive> receives = lendOrderReceiveService.getByLendOrderId(lendOrder.getLendOrderId());
		BigDecimal should = BigDecimal.ZERO;
		for (int i = 0; i < receives.size(); i++) {
			should = should.add(receives.get(i).getShouldCapital2()).add(receives.get(i).getShouldInterest2());
		}
		UserAccount account = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
		return account.getAvailValue2().subtract(should);
	}

	/**
	 * 用户支出溢出费
	 */
	@Override
	@Transactional
	public void UserPayOverflowFee(LendOrder lendOrder, BigDecimal overflowFee) {
		// 获取省心计划账户
		UserAccount userAccount = this.userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());

		AccountValueChangedQueue avcq = new AccountValueChangedQueue();
		AccountValueChanged pay = new AccountValueChanged(userAccount.getAccId(), overflowFee, overflowFee,
				AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAYOVERFLOW.getValue(),
				AccountConstants.AccountChangedTypeEnum.LEND.getValue(), AccountConstants.VisiableEnum.HIDDEN.getValue(), lendOrder.getLendOrderId(),
				AccountConstants.OwnerTypeEnum.USER.getValue(), userAccount.getUserId(), new Date(), StringUtils.t2s(
						DescTemplate.desc.AccountChanngedDesc.OVERFLOW_FEE_PAY, overflowFee), false);
		avcq.addAccountValueChanged(pay);
		this.userAccountOperateService.execute(avcq);
	}

	/**
	 * 用户收入溢出费
	 */
	@Override
	@Transactional
	public void UserIncomeOverflowFee(LendOrder lendOrder, BigDecimal overflowFee) {
		// 获取省心计划账户
		UserAccount userAccount = this.userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
		LendOrderExtProduct product = lendOrderService.findFinancialPlanById(lendOrder.getLendOrderId());
		AccountValueChangedQueue avcq = new AccountValueChangedQueue();
		AccountValueChanged income = new AccountValueChanged(userAccount.getAccId(), overflowFee, overflowFee,
				AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEOVERFLOW.getValue(),
				AccountConstants.AccountChangedTypeEnum.LEND.getValue(), AccountConstants.VisiableEnum.HIDDEN.getValue(), lendOrder.getLendOrderId(),
				AccountConstants.OwnerTypeEnum.USER.getValue(), userAccount.getUserId(), new Date(), StringUtils.t2s(
						DescTemplate.desc.AccountChanngedDesc.OVERFLOW_FEE_INCOME, product.getProductName(), overflowFee), false);
		avcq.addAccountValueChanged(income);
		this.userAccountOperateService.execute(avcq);
	}

	/**
	 * 平台收入溢出费
	 */
	@Override
	@Transactional
	public void platformIncomeOverflowFee(LendOrder lendOrder, BigDecimal overflowFee) {
		// 平台收支账户
		long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue());
		LendOrderExtProduct product = lendOrderService.findFinancialPlanById(lendOrder.getLendOrderId());
		AccountValueChangedQueue avcq = new AccountValueChangedQueue();
		AccountValueChanged income = new AccountValueChanged(systemAccountId, overflowFee, overflowFee,
				AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEOVERFLOW.getValue(),
				AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(), AccountConstants.VisiableEnum.HIDDEN.getValue(),
				lendOrder.getLendOrderId(), AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(), systemAccountId, new Date(), StringUtils.t2s(
						DescTemplate.desc.AccountChanngedDesc.OVERFLOW_FEE_INCOME, product.getProductName(), overflowFee), false);
		avcq.addAccountValueChanged(income);
		this.userAccountOperateService.execute(avcq);

	}

	/**
	 * 省心计划账户钱转入资金账户
	 */
	@Override
	@Transactional
	public void financeAccountPayToAssetAccount(LendOrder lendOrder) {
		// 获取省心计划账户
		UserAccount financeAccount = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
		if(financeAccount.getAvailValue().compareTo(BigDecimal.ZERO) <= 0){
			return ;
		}
		// 获取资金账户
		UserAccount cashAccount = userAccountService.getCashAccount(lendOrder.getLendUserId());

		AccountValueChangedQueue avcq = new AccountValueChangedQueue();

		AccountValueChanged pay = new AccountValueChanged(financeAccount.getAccId(), financeAccount.getAvailValue(), financeAccount.getAvailValue2(),
				AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_BALANCECHANGEOUT.getValue(),
				AccountConstants.AccountChangedTypeEnum.LEND.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(), lendOrder.getLendOrderId(),
				AccountConstants.OwnerTypeEnum.USER.getValue(), financeAccount.getUserId(), new Date(), StringUtils.t2s(
						DescTemplate.desc.AccountChanngedDesc.BUY_FINANCE_QUIT_OUT, financeAccount.getAvailValue()), false);
		AccountValueChanged income = new AccountValueChanged(cashAccount.getAccId(), financeAccount.getAvailValue(), financeAccount.getAvailValue2(),
				AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_BALANCECHANGEINTO.getValue(),
				AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(), AccountConstants.VisiableEnum.DISPLAY.getValue(), lendOrder.getLendOrderId(),
				AccountConstants.OwnerTypeEnum.USER.getValue(), cashAccount.getUserId(), new Date(), StringUtils.t2s(
						DescTemplate.desc.AccountChanngedDesc.BUY_FINANCE_QUIT_IN, financeAccount.getAvailValue()), false);
		avcq.addAccountValueChanged(pay);
		avcq.addAccountValueChanged(income);
		this.userAccountOperateService.execute(avcq);
		
	}
	
	/**
	 * 省心计划到期回款
	 * */
	@Transactional
	public void financeCycleAccount(LendOrder lendOrder){
		try {
			financeAccountPayToAssetAccount(lendOrder);
		}catch(Exception e){
			throw new SystemException(e,SystemErrorCode.SYSTEM_ERROR_CODE);
		}
	}
	
	/**
	 * 对省心计划账户的回款计划进行全部回款
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private BigDecimal repayLendOrderReceive(BigDecimal shouldBalance,
			BigDecimal avalidBalance, List<LendOrderReceive> receiveList) {
		Date now = new Date();
		//如果实际收益小于预期收益，优先还本金，再回利息
		if(shouldBalance.compareTo(avalidBalance) > 0){
			//优先还本金
			for(LendOrderReceive old : receiveList){
				if(avalidBalance.compareTo(old.getShouldCapital2()) > 0){
					old.setFactCapital(old.getShouldCapital2());
					avalidBalance = avalidBalance.subtract(old.getShouldCapital2());
				}else{
					if(avalidBalance.compareTo(BigDecimal.ZERO) > 0){
						old.setFactCapital(avalidBalance);
						avalidBalance = avalidBalance.subtract(avalidBalance);
					}else{
						break;
					}
				}
			}
			// 再还利息
			for(LendOrderReceive old : receiveList){
				if(avalidBalance.compareTo(old.getShouldInterest2()) > 0){
					old.setFactInterest(old.getShouldInterest2());
					avalidBalance = avalidBalance.subtract(old.getShouldInterest2());
				}else{
					if(avalidBalance.compareTo(BigDecimal.ZERO) > 0){
						old.setFactInterest(avalidBalance);
						avalidBalance = avalidBalance.subtract(avalidBalance);
					}else{
						
					}
				}
				old.setReceiveBalance(old.getFactCapital().add(old.getFactInterest()));
				//不论是否还清，都置为已返息
				old.setReceiveState(LendOrderReceiveStateEnum.RECEIVED.value2Char());
			}
		}else{
			for(LendOrderReceive receive : receiveList){
				receive.setFactCapital(receive.getShouldCapital2());
				receive.setFactInterest(receive.getShouldInterest2());
				receive.setReceiveBalance(receive.getFactCapital().add(receive.getFactInterest()));
				receive.setReceiveDate(now);
				receive.setReceiveState(LendOrderReceiveStateEnum.RECEIVED.value2Char());
			}
			avalidBalance = avalidBalance.subtract(shouldBalance);
		}
		//回款计划更新
		for(LendOrderReceive receive : receiveList){
			Map map = new HashMap();
			map.put("factCapital", receive.getFactCapital());
			map.put("factInterest", receive.getFactInterest());
			map.put("receiveBalance", receive.getReceiveBalance());
			map.put("receiveDate", now);
			map.put("receiveState", receive.getReceiveState());
			map.put("receiveId", receive.getReceiveId());
			lendOrderReceiveService.update(map);
		}
		return avalidBalance;
	}


	
	void cacularFees(LendOrder lendOrder,BigDecimal shouldCapital2, BigDecimal shouldInterest2, LendProductPublish lendProductPublish,
			AccountValueChangedQueue avcq, Long systemAccountId)throws Exception{
		

        logger.debug("****【省心计划退出中】开始计算并支出出借合同回款时的费用****");
        LendOrderReceive receiveDetail = lendOrderReceiveService.getByOrderAndSectionCode(lendOrder.getLendOrderId(),1);
        if (receiveDetail == null) {
            logger.debug("查询不到出借合同的返息周期，默认计算第一个返息周期");
            receiveDetail = lendOrderReceiveService.getFirstByOrder(lendOrder.getLendOrderId());
        }
        List<LendOrderFeesDetail> lendOrderFeesDetails = lendOrderDetailFeesService.getDetailByLendOrderIdAndSectionCode(lendOrder.getLendOrderId(), receiveDetail.getSectionCode());

        Date now = new Date();
        if (lendOrderFeesDetails == null || lendOrderFeesDetails.size() == 0) {
            List<LendProductFeesItem> lendOrderProductFeesItemList = lendProductService.findAllProductFeesItemsByLendOrderId(lendOrder.getLendOrderId());
            for (LendProductFeesItem lendLoanBinding : lendOrderProductFeesItemList) {
                FeesItem feesItem = lendLoanBinding.getFeesItem();
                if (feesItem != null) {
                    LendOrderFeesDetail lendOrderFeesDetail = new LendOrderFeesDetail();
                    lendOrderFeesDetail.setLendOrderId(lendOrder.getLendOrderId());
                    lendOrderFeesDetail.setChargePoint(lendLoanBinding.getChargePoint());
                    lendOrderFeesDetail.setSectionCode(receiveDetail.getSectionCode());//省心计划只有一期
                    lendOrderFeesDetail.setFeesItemId(lendLoanBinding.getFeesItemId());
                    BigDecimal feesBalance = feesItemService.calculateFeesBalance(feesItem.getFeesItemId(), BigDecimal.ZERO, BigDecimal.ZERO,
                    		shouldCapital2, shouldInterest2, BigDecimal.ZERO, BigDecimal.ZERO);
                    //计算费用大于0
                    if (BigDecimalUtil.compareTo(feesBalance, BigDecimal.ZERO, false, 2) > 0){

                        lendOrderFeesDetail.setFeesBalance(feesBalance);
                        lendOrderFeesDetail.setFeesBalance2(BigDecimalUtil.up(lendOrderFeesDetail.getFeesBalance(), 2));

                        if (BigDecimalUtil.compareTo(shouldInterest2, lendOrderFeesDetail.getFeesBalance2(), true, 2) >= 0) {
                            lendOrderFeesDetail.setPaidFees(lendOrderFeesDetail.getFeesBalance2());
                            lendOrderFeesDetail.setFeesState(LendOrderFeesDetail.FEESSTATE_PAID);
                            lendOrderDetailFeesService.insert(lendOrderFeesDetail);
                        } else {
                            throw new SystemException(new Exception("收费异常！"),UserAccountErrorCode.UNSUPPORT_OPERATE_TYPE);
                        }
                        String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVE_FEES_PAY_FINANCE, lendProductPublish.getPublishName(), feesItem.getItemName());
                        AccountValueChanged avcPay = new AccountValueChanged(lendOrder.getCustomerAccountId(), lendOrderFeesDetail.getPaidFees(),
                                lendOrderFeesDetail.getPaidFees(), AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue(),
                                "LendOrderFeesDetail", VisiableEnum.DISPLAY.getValue(), lendOrderFeesDetail.getLendOrderFeesDetailId(),
                                AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                                lendOrder.getLendOrderId(), now, descPay, true);
                        avcq.addAccountValueChanged(avcPay);


                        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVE_FEES_INCOME_FINANCE, lendProductPublish.getPublishName(), lendOrder.getOrderCode(), feesItem.getItemName());
                        AccountValueChanged avcIncome = new AccountValueChanged(systemAccountId, lendOrderFeesDetail.getPaidFees(),
                                lendOrderFeesDetail.getPaidFees(), AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue(),
                                "LendOrderFeesDetail", VisiableEnum.DISPLAY.getValue(), lendOrderFeesDetail.getLendOrderFeesDetailId(),
                                AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                                systemAccountId, now, descIncome, true);
                        avcq.addAccountValueChanged(avcIncome);
                    }

                }

            }

        } else {
            for (LendOrderFeesDetail orderFeesDetail : lendOrderFeesDetails) {
                if (orderFeesDetail.getFeesState() == LendOrderFeesDetail.FEESSTATE_UNPAY) {
                    FeesItem feesItem = feesItemService.findById(orderFeesDetail.getFeesItemId());
                    BigDecimal shouldFees = orderFeesDetail.getFeesBalance2().subtract(orderFeesDetail.getPaidFees());
                    BigDecimal theFactFees = BigDecimal.ZERO;

                    if (BigDecimalUtil.compareTo(shouldInterest2, shouldFees, true, 2) >= 0) {
                        orderFeesDetail.setPaidFees(orderFeesDetail.getFeesBalance2());
                        orderFeesDetail.setFeesState(LendOrderFeesDetail.FEESSTATE_PAID);
                        theFactFees = shouldFees;
                    } else {
                        throw new SystemException(new Exception("收费异常！"),UserAccountErrorCode.UNSUPPORT_OPERATE_TYPE);
                    }

                    lendOrderDetailFeesService.update(BeanUtil.bean2Map(orderFeesDetail));
                    String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVE_FEES_PAY_FINANCE, lendProductPublish.getPublishName(), feesItem.getItemName());
                    AccountValueChanged avcPay = new AccountValueChanged(lendOrder.getCustomerAccountId(), theFactFees,
                            theFactFees, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue(),
                            "LendOrderFeesDetail", VisiableEnum.DISPLAY.getValue(), orderFeesDetail.getLendOrderFeesDetailId(),
                            AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                            lendOrder.getLendOrderId(), now, descPay, true);
                    avcq.addAccountValueChanged(avcPay);

                    String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVE_FEES_INCOME_FINANCE, lendProductPublish.getPublishName(), lendOrder.getOrderCode(), feesItem.getItemName());
                    AccountValueChanged avcIncome = new AccountValueChanged(systemAccountId, theFactFees,
                            theFactFees, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue(),
                            "LendOrderFeesDetail", VisiableEnum.DISPLAY.getValue(), orderFeesDetail.getLendOrderFeesDetailId(),
                            AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                            systemAccountId, now, descIncome, true);
                    avcq.addAccountValueChanged(avcIncome);

                }
            }
        }
        logger.debug("****【省心计划退出中】计算并支出出借合同回款时的费用****");
    
	}
	
	/**
	 * 获取省心账户的各种支出费用
	 * */
	public BigDecimal getAllFeesByAccountId(Long accId){
		List<String> busTypeList = new ArrayList<>();
		setFeesBusTypeList(busTypeList);
		Map<Object,Object> param = new HashMap<>();
		param.put("accId", accId);
		param.put("busTypes", busTypeList);
		BigDecimal feesPay = userAccountService.getSXJHLastNeedTurnValuePay(param);
		return feesPay==null?BigDecimal.ZERO:feesPay ;
	}
	/**
	 * 获取所有省心账户的各种支出费用
	 * */
	public BigDecimal getAllFeesByUserId(Long userId){
		List<String> busTypeList = new ArrayList<>();
		setFeesBusTypeList(busTypeList);
		Map<Object,Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("busTypes", busTypeList);
		BigDecimal feesPay = userAccountService.getSXJHTurnValuePay(param);
		return feesPay==null?BigDecimal.ZERO:feesPay ;
	}
	public void setFeesBusTypeList(List<String> busTypeList) {
		busTypeList.add(AccountConstants.BusinessTypeEnum.FEESTYPE_MANAGER.getValue());
		busTypeList.add(AccountConstants.BusinessTypeEnum.FEESTYPE_EXIST.getValue());
		busTypeList.add(AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue());
		busTypeList.add(AccountConstants.BusinessTypeEnum.FEESTYPE_DELAYFEES.getValue());
		busTypeList.add(AccountConstants.BusinessTypeEnum.FEESTYPE_EARLYWITHDRAWALPENALTY.getValue());
		busTypeList.add(AccountConstants.BusinessTypeEnum.FEESTYPE_AUTHENTICATIONFEE.getValue());
		busTypeList.add(AccountConstants.BusinessTypeEnum.FEESTYPE_WITHDRAWCASHFEE.getValue());
		busTypeList.add(AccountConstants.BusinessTypeEnum.FEESTYPE_PAYOVERFLOW.getValue());
		busTypeList.add(AccountConstants.BusinessTypeEnum.FEESTYPE_EARLYWITHDRAWALPENALTY_INCOME.getValue());
		busTypeList.add(AccountConstants.BusinessTypeEnum.FEESTYPE_TURNRIGHTS_FEE.getValue());
		busTypeList.add(AccountConstants.BusinessTypeEnum.FEESTYPE_ERROR_PAY_AWARD.getValue());
	}
}
