package com.xt.cfp.core.event.command;

import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderStatusEnum;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.InterestCalculation;
import com.xt.cfp.core.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ren yulin on 15-7-14.
 */
public class RepaymentRateAward2LendCommand extends Command {
    private long systemOperationIdAccountId;
    private AccountValueChangedQueue avcq;
    private LoanApplication loanApplication;

    private LoanPublishService loanPublishService;
    private AwardDetailService awardDetailService;
    private UserAccountService userAccountService;
    private UserInfoService userInfoService;
    private LendOrderService lendOrderService;
    private CreditorRightsService creditorRightsService;

    private RateLendOrderService rateLendOrderService;
    private RepaymentPlan repaymentPlan ;
    private Map<Long,RightsRepaymentDetail> rightsMap;
    private String awardPoint ;
    private Map<Long,Long> rightsOrderMap;
    private RepaymentPlanService repaymentPlanService ;
    private LoanProductService loanProductService;
    private boolean isAhead ;
	private CapitalFlowService capitalFlowService;
	private Schedule schedule;

    private Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap ;

    public RepaymentRateAward2LendCommand(long systemOperationIdAccountId, AccountValueChangedQueue avcq,LoanApplication loanApplication,
										  AwardDetailService awardDetailService,UserAccountService userAccountService, UserInfoService userInfoService,
										  RateLendOrderService rateLendOrderService, RepaymentPlan repaymentPlan ,Map<Long,RightsRepaymentDetail> rightsMap,
										  String awardPoint,Map<Long,Long> rightsOrderMap,RepaymentPlanService repaymentPlanService,LoanPublishService loanPublishService,
										  CreditorRightsService creditorRightsService,LendOrderService lendOrderService,LoanProductService loanProductService,boolean isAhead ,
										  Map<Long,Map<Long,Map<String,BigDecimal>>>resultMap,CapitalFlowService capitalFlowService,Schedule schedule) {
        this.systemOperationIdAccountId = systemOperationIdAccountId;
        this.avcq = avcq;
        this.loanApplication = loanApplication;
        this.awardDetailService = awardDetailService;
        this.userAccountService = userAccountService;
        this.userInfoService = userInfoService;
        this.rateLendOrderService = rateLendOrderService;
        this.repaymentPlan = repaymentPlan ;
        this.rightsMap = rightsMap;
        this.awardPoint = awardPoint ;
        this.rightsOrderMap = rightsOrderMap;
        this.repaymentPlanService = repaymentPlanService;
        this.loanPublishService = loanPublishService ;
        this.creditorRightsService = creditorRightsService ;
        this.lendOrderService = lendOrderService;
        this.loanProductService=loanProductService;
        this.isAhead = isAhead ;
        this.resultMap = resultMap ;
        this.capitalFlowService = capitalFlowService;
        this.schedule = schedule;
    }

    @Override
    public Object execute() throws Exception {
        Date now = new Date();
        LoanPublish loanPublish = loanPublishService.findById(loanApplication.getLoanApplicationId());
        List<RateLendOrder> rateList = rateLendOrderService.findByLoanApplicationIdAndRepaymentPlanId(loanApplication.getLoanApplicationId(),repaymentPlan.getRepaymentPlanId());

      //如果所有的还款计划都已还清，则修改借款申请的状态为已结清，以及修改该借款申请的所有关联债权状态为已结清
        List<RepaymentPlan> planList = repaymentPlanService.getRepaymentPlansByloanApplicationId(loanApplication.getLoanApplicationId());
        boolean allComplete = true;
        for (RepaymentPlan plan : planList) {
            if (plan.getChannelType() == ChannelTypeEnum.ONLINE.value2Char()) {
                if (plan.getPlanState() == RepaymentPlanStateEnum.UNCOMPLETE.value2Char()
                        || plan.getPlanState() == RepaymentPlanStateEnum.PART.value2Char()
                        || plan.getPlanState() == RepaymentPlanStateEnum.DEFAULT.value2Char()) {
                    allComplete = false;
                }
            }
        }
        LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());

		UserAccount platformAccount = userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);

        for(RateLendOrder rateOrder : rateList){

        	//加息券奖励  加息券奖励只有在还款周期时发放
        	if((rateOrder.getRateType().equals(RateEnum.RateLendOrderTypeEnum.RATE_COUPON.getValue())||
        			rateOrder.getRateType().equals(RateEnum.RateLendOrderTypeEnum.ACTIVITY.getValue())||
        			rateOrder.getRateType().equals(RateEnum.RateLendOrderTypeEnum.AWARD.getValue())
        			)
        			&& awardPoint.equals(AwardPointEnum.ATREPAYMENT.getValue())&&
        			rateOrder.getAwardPoint().equals(AwardPointEnum.ATREPAYMENT.getValue())){
        		RateLendOrderTypeEnum rateEnum = null;
        		String payDesc ="" ;
        		String  incomeDesc ="" ;
        		String platPayDesc ="" ;
        		String bidIncomeDesc ="";
        		if(rateOrder.getRateType().equals(RateEnum.RateLendOrderTypeEnum.RATE_COUPON.getValue()) ){
        			rateEnum=RateLendOrderTypeEnum.RATE_COUPON;
        			payDesc = DescTemplate.desc.AccountChanngedDesc.RATE_COUPON_PAY;
        			incomeDesc=DescTemplate.desc.AccountChanngedDesc.RATE_COUPON_INCOME;
        			platPayDesc = AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_RATE_COUPON.getValue() ;
        			bidIncomeDesc = AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_RATE_COUPON.getValue() ;
        		}else if(rateOrder.getRateType().equals(RateEnum.RateLendOrderTypeEnum.ACTIVITY.getValue())){
        			rateEnum=RateLendOrderTypeEnum.ACTIVITY;
        			payDesc = DescTemplate.desc.AccountChanngedDesc.ACTIVITY_PAY;
        			incomeDesc=DescTemplate.desc.AccountChanngedDesc.ACTIVITY_INCOME;
        			platPayDesc = AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_ACTIVITY_AWARD.getValue() ;
        			bidIncomeDesc = AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_ACTIVITY_AWARD.getValue() ;
        		}else if(rateOrder.getRateType().equals(RateEnum.RateLendOrderTypeEnum.AWARD.getValue())){
        			rateEnum=RateLendOrderTypeEnum.AWARD;
        			payDesc = DescTemplate.desc.AccountChanngedDesc.AWARD_PAY;
        			incomeDesc=DescTemplate.desc.AccountChanngedDesc.AWARD_INCOME;
        			platPayDesc = AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_AWARD.getValue() ;
        			bidIncomeDesc = AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue() ;
        		}
                RightsRepaymentDetail rightsRepaymentDetail =  rightsMap.get(rightsOrderMap.get(rateOrder.getLendOrderId()));

              //对于当期债权还款详情，只对已还清的进行发放奖励
                if(rightsRepaymentDetail.getRightsDetailState() == RightsRepaymentDetail.RIGHTSDETAILSTATE_COMPLETE ){

                    if (awardDetailService.findByRightsRepaymentDetailIdAndRateType(rightsRepaymentDetail.getRightsRepaymentDetailId(),rateEnum.getValue()) == null) {
                    	//发放奖励金额
                    	BigDecimal theAward2Lend = BigDecimal.ZERO;
                    	CreditorRights creditorRights = creditorRightsService.findById(rightsRepaymentDetail.getCreditorRightsId(), false);
                    	LendOrder lendOrder = lendOrderService.findById(creditorRights.getLendOrderId());
                    	BigDecimal ratio100 = new BigDecimal("100");
                    	if(loanProduct.getRepaymentType() == InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST){
                    		//周期还本息（本金会逐期减少）
							Map<Integer, Map<String, BigDecimal>> awardMap = shotCashLoan(loanApplication, rateOrder, loanProduct, ratio100);
							theAward2Lend = BigDecimalUtil.down(
                    				awardMap.get(repaymentPlan.getSectionCode()).get("interest")
                    				.multiply(rightsRepaymentDetail
                    						.getProportion().divide(
                    								ratio100)), 2);

						} else if (loanProduct.getRepaymentType() == InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL) {
							// 周期还本金，到期换利息（本金不变）
							//奖励为年利率，月利为年利率除12

							BigDecimal awardBalance = shotCashLoan(loanApplication,rateOrder,loanProduct);
							theAward2Lend = BigDecimalUtil.down(awardBalance.multiply(rightsRepaymentDetail.getProportion().divide(ratio100)),2);
						}
                        UserInfoVO userInfoVO = userInfoService.getUserExtByUserId(lendOrder.getLendUserId());
                        AwardDetail awardDetail = awardDetailService.insertAwardDetail(now, rightsRepaymentDetail, theAward2Lend,
                                lendOrder, loanApplication.getLoanApplicationId(), AwardPointEnum.ATREPAYMENT,rateEnum);

                      //【20170203省心计划需求：记录还款奖励 -- 此处增加金额】
                        if( resultMap.get(rightsRepaymentDetail.getLendAccountId()) == null ){
                     	   Map<Long,Map<String,BigDecimal>> valueMap = new HashMap<>();
                     	   Map<String,BigDecimal> awardsMap = new HashMap<>();
                     	   awardsMap.put(RightsRepaymentDetail.AWARDS, theAward2Lend);
                     	   valueMap.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), awardsMap);
                     	   resultMap.put(rightsRepaymentDetail.getLendAccountId(), valueMap);
                        }else{
                     	   Map<Long,Map<String,BigDecimal>> rrdMaps = resultMap.get(rightsRepaymentDetail.getLendAccountId());
                     	   if(rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId())== null ){
                     		  Map<String,BigDecimal> awardsMap = new HashMap<>();
                     		  awardsMap.put(RightsRepaymentDetail.AWARDS, theAward2Lend);
                     		  rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), awardsMap);
                     	   }else{
                     		  Map<String,BigDecimal> valueMap = rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId());
                     		  BigDecimal grandValue = valueMap.get(RightsRepaymentDetail.AWARDS);
                     		  if(grandValue == null){
                     			 grandValue = theAward2Lend;
                     		  }else{
                     			 grandValue = grandValue.add(theAward2Lend);
                     		  }
                     		  valueMap.put(RightsRepaymentDetail.AWARDS, grandValue);
                     		  rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), valueMap) ;
                     	   }
                        }

                        //平台支出奖励
                        String descPay = StringUtils.t2s(payDesc, userInfoVO.getLoginName(), loanPublish.getLoanTitle());
                        AccountValueChanged avcPay = new AccountValueChanged(systemOperationIdAccountId, theAward2Lend,
                                theAward2Lend, AccountConstants.AccountOperateEnum.PAY.getValue(), platPayDesc,
                                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                                AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(),
                                systemOperationIdAccountId, now, descPay, false);
                        avcq.addAccountValueChanged(avcPay);

                        UserAccount userAccountWh = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
                        UserAccount cashUserAccount = null;
                        //如果是理财账户，奖励发放至理财账户中；如果是非理财账户，奖励发放到资金账户
                        if(userAccountWh.getAccTypeCode().equals(AccountConstants.AccountTypeEnum.ORDER_ACCOUNT.getValue())){
                        	cashUserAccount = userAccountWh;
                        }else{
                        	//出借人资金账户收入奖励
                        	cashUserAccount = userAccountService.getCashAccount(lendOrder.getLendUserId());
                        }
                        String descIncome = StringUtils.t2s(incomeDesc, loanPublish.getLoanTitle(), AwardPointEnum.ATREPAYMENT.getDesc());
                        AccountValueChanged avcIncome = new AccountValueChanged(cashUserAccount.getAccId(), theAward2Lend,
                                theAward2Lend, AccountConstants.AccountOperateEnum.INCOM.getValue(), bidIncomeDesc,
                                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                                AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(),
                                cashUserAccount.getAccId(), now, descIncome, true);
                        avcq.addAccountValueChanged(avcIncome);

						CapitalFlow cap=new CapitalFlow();
						cap.setScheduleId(schedule.getScheduleId());
						cap.setOperationType(Integer.parseInt(HFOperationEnum.ALLOCATION_COMPANY_TO_PERSON.getValue()));
						cap.setFromUser(platformAccount.getUserId());
						cap.setToUser(cashUserAccount.getUserId());
						cap.setAmount(theAward2Lend);
						cap.setStartTime(new Date());
						cap.setBusinessId(rateOrder.getLoanApplicationId());
						cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
						cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
						capitalFlowService.addCapital(cap);
                    }
                }
        	}else{
        		//结清奖励
        		if( awardPoint.equals(AwardPointEnum.ATCOMPLETE.getValue()) && rateOrder.getAwardPoint().equals(AwardPointEnum.ATCOMPLETE.getValue())){
        	        if(allComplete || isAhead){
                        //结清时中间表状态直接置为失效
                    	rateOrder.setStatus(RateLendOrderStatusEnum.UN_VALID.getValue());
                        rateLendOrderService.updateRateLendOrder(rateOrder);

                    	LendOrder lendOrder = lendOrderService.findById(rateOrder.getLendOrderId());
                    	int dueTime=0;
                    	if(repaymentPlan != null ){
                    		dueTime = repaymentPlan.getSectionCode();
                    	}

                    	Map<Integer,Map<String, BigDecimal>> awardMap = null ;
                    	if(loanProduct.getRepaymentType() == InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST){
                    		//周期还本息算法
							awardMap = shotCashLoan(loanApplication, rateOrder, loanProduct, new BigDecimal("100"));
						} else if (loanProduct.getRepaymentType() == InterestCalculation.REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL) {
							// 周期还利息，到期还本金（本金不变）
							//奖励为年利率，月利为年利率除12
							if (loanApplication.getLoanType().equals(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue())) {
								awardMap = InterestCalculation.getDaysInfoOnce(loanApplication.getConfirmBalance(),
										rateOrder.getRateValue().divide(new BigDecimal("100")), loanProduct.getCycleValue());
							} else {
								awardMap =InterestCalculation.getMonthInfoOnceCalital(loanApplication.getConfirmBalance(), rateOrder.getRateValue().divide(new BigDecimal("100")), loanProduct.getCycleCounts());
							}

						}

                    	BigDecimal theAward2Lend = BigDecimal.ZERO;
                    	 RightsRepaymentDetail rightsRepaymentDetail =  rightsMap.get(rightsOrderMap.get(rateOrder.getLendOrderId()));
                    	 int startSectionCode = repaymentPlanService.getTheMinRepaymentForCreditorRights(rightsRepaymentDetail.getCreditorRightsId());
                    	 //如果是债权转让，查询出所有债权明细，从最早一期对应的还款计划期号开始算奖励
                		for(int i = startSectionCode; i <= dueTime ; i++){
                			theAward2Lend = theAward2Lend.add(awardMap.get(i).get("interest"));
                		}
                		theAward2Lend = BigDecimalUtil.down(theAward2Lend.multiply(rightsRepaymentDetail.getProportion().divide(new BigDecimal("100"))), 2);
//                        BigDecimal theAward2Lend = InterestCalculation.getAllInterest(lendOrder.getBuyBalance(), rateOrder.getRateValue(), loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(),
//                        		loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), dueTime, loanProduct.getCycleValue());
                        UserInfoVO userInfoVO = userInfoService.getUserExtByUserId(lendOrder.getLendUserId());
                        AwardDetail awardDetail = awardDetailService.insertAwardDetail(now, null, theAward2Lend,
                                lendOrder, loanApplication.getLoanApplicationId(), AwardPointEnum.ATREPAYMENT,RateLendOrderTypeEnum.AWARD);

                      //【20170203省心计划需求：记录还款结清奖励 -- 此处增加金额】
						if (resultMap != null && !resultMap.isEmpty()) {
							if( resultMap.get(rightsRepaymentDetail.getLendAccountId()) == null ){
								Map<Long,Map<String,BigDecimal>> valueMap = new HashMap<>();
								Map<String,BigDecimal> awardsMap = new HashMap<>();
								awardsMap.put(RightsRepaymentDetail.AWARDS, theAward2Lend);
								valueMap.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), awardsMap);
								resultMap.put(rightsRepaymentDetail.getLendAccountId(), valueMap);
							}else{
								Map<Long,Map<String,BigDecimal>> rrdMaps = resultMap.get(rightsRepaymentDetail.getLendAccountId());
								if(rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId())== null ){
									Map<String,BigDecimal> awardsMap = new HashMap<>();
									awardsMap.put(RightsRepaymentDetail.AWARDS, theAward2Lend);
									rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), awardsMap);
								}else{
									Map<String,BigDecimal> valueMap = rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId());
									BigDecimal grandValue = valueMap.get(RightsRepaymentDetail.AWARDS);
									if(grandValue == null){
										grandValue = theAward2Lend;
									}else{
										grandValue = grandValue.add(theAward2Lend);
									}
									valueMap.put(RightsRepaymentDetail.AWARDS, grandValue);
									rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), valueMap) ;
								}
							}
						}


                        //平台支出奖励
                        String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_PAY, userInfoVO.getLoginName(), loanPublish.getLoanTitle());
                        AccountValueChanged avcPay = new AccountValueChanged(systemOperationIdAccountId, theAward2Lend,
                                theAward2Lend, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_AWARD.getValue(),
                                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                                AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(),
                                systemOperationIdAccountId, now, descPay, false);
                        avcq.addAccountValueChanged(avcPay);

                        UserAccount userAccountWh = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
                        UserAccount cashUserAccount = null;
                        //如果是理财账户，奖励发放至理财账户中；如果是非理财账户，奖励发放到资金账户
                        if(userAccountWh.getAccTypeCode().equals(AccountConstants.AccountTypeEnum.ORDER_ACCOUNT.getValue())){
                        	cashUserAccount = userAccountWh;
                        }else{
                        	//出借人资金账户收入奖励
                        	cashUserAccount = userAccountService.getCashAccount(lendOrder.getLendUserId());
                        }
                        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_INCOME, loanPublish.getLoanTitle(), AwardPointEnum.ATCOMPLETE.getDesc());
                        AccountValueChanged avcIncome = new AccountValueChanged(cashUserAccount.getAccId(), theAward2Lend,
                                theAward2Lend, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
                                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
                                AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(),
                                cashUserAccount.getAccId(), now, descIncome, true);
                        avcq.addAccountValueChanged(avcIncome);

						CapitalFlow cap=new CapitalFlow();
						cap.setScheduleId(schedule.getScheduleId());
						cap.setOperationType(Integer.parseInt(HFOperationEnum.ALLOCATION_COMPANY_TO_PERSON.getValue()));
						cap.setFromUser(platformAccount.getUserId());
						cap.setToUser(cashUserAccount.getUserId());
						cap.setAmount(theAward2Lend);
						cap.setStartTime(new Date());
						cap.setBusinessId(rateOrder.getLoanApplicationId());
						cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
						cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
						capitalFlowService.addCapital(cap);
        	        }
        	        //如果当前借款标所有还款计划都结清，将标的关联的加息奖励-订单 数据都置为失效状态
        	        if(allComplete || isAhead){
        	        	List<RateLendOrder> allValidRateList = rateLendOrderService.findByLoanApplicationIdAndRepaymentPlanId(loanApplication.getLoanApplicationId(),null);
        	        	for(RateLendOrder r: allValidRateList){
        	        		r.setStatus(RateLendOrderStatusEnum.UN_VALID.getValue());
        	        		rateLendOrderService.updateRateLendOrder(r);
        	        	}
        	        }
        		}


        	}
        }

        return null;
    }

	/**
	 * 计算全部奖励
	 * @param loanApplication
	 * @param rateOrder
	 * @param loanProduct
	 * @param ratio100
	 * @return
	 */
	private Map<Integer, Map<String, BigDecimal>> shotCashLoan(LoanApplication loanApplication, RateLendOrder rateOrder
			, LoanProduct loanProduct, BigDecimal ratio100) {
		Map<Integer, Map<String, BigDecimal>> awardMap = null;
		if (loanApplication.getLoanType().equals(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue())) {
			awardMap = InterestCalculation.getDaysInfoOnce(loanApplication.getConfirmBalance(),
					rateOrder.getRateValue().divide(ratio100), loanProduct.getCycleValue());
		} else {
			//周期还本息（本金会逐期减少）
			awardMap = InterestCalculation
					.getMonthInfoByAverageCapitalPlusInterest(
							loanApplication.getConfirmBalance(),
							rateOrder.getRateValue().divide(ratio100),
							loanProduct.getCycleCounts());
		}
		return awardMap;
	}

	private BigDecimal shotCashLoan(LoanApplication loanApplication, RateLendOrder rateOrder
			, LoanProduct loanProduct) {
		BigDecimal ratio100 = new BigDecimal("100");
		BigDecimal awardBalance = null;
		if (loanApplication.getLoanType().equals(LoanTypeEnum.LOANTYPE_CASH_LOAN.getValue())) {
			awardBalance = InterestCalculation.getDaysInfoOnce(loanApplication.getConfirmBalance(),
					rateOrder.getRateValue().divide(ratio100), loanProduct.getCycleValue()).get(1).get("interest");
		} else {
			//周期还本息（本金会逐期减少）
			BigDecimal monthAwardRate = rateOrder.getRateValue().divide(ratio100).divide(new BigDecimal("12"),18,
					BigDecimal.ROUND_CEILING);
			  awardBalance = loanApplication.getConfirmBalance().multiply(monthAwardRate);
		}
		return awardBalance;
	}

}
