package com.xt.cfp.core.event;


import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.event.command.*;
import com.xt.cfp.core.event.pojo.EventTriggerInfo;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BigDecimalUtil;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-11 下午3:15.
 */
public class RepaymentEvent extends Event {

    private Logger logger = Logger.getLogger("eventTaskLogger");


    private LoanApplication loanApplication;
    private LoanPublish loanPublish;
    private RepaymentPlan repaymentPlan;
    private long payOrderId;
    private Date repaymentDate;
    private BigDecimal balance;
    private long adminId;
    private long loanAccountId;
    private long systemAccountId;
    private Date financialLendDate;
    private BigDecimal allInterest;
    private boolean ignoreDelay;
    private List<LoanApplicationFeesItem> commonFeesItems;
    private List<LoanApplicationFeesItem> breakFessItems;
    private Map<Long, BigDecimal> lendsRatioMap = new HashMap<Long, BigDecimal>();
    private Map<Long, Long> lendsOrderMap = new HashMap<Long, Long>();
    private AccountValueChangedQueue avcq;

    private RepaymentRecord currentRepaymentRecord;


    private RepaymentPlanService repaymentPlanService;
    private CreditorRightsService creditorRightsService;
    private LoanFeesDetailService loanFeesDetailService;
    private LoanApplicationFeesItemService loanApplicationFeesItemService;
    private UserAccountService userAccountService;
    private LoanApplicationService loanApplicationService;
    private RepaymentRecordService repaymentRecordService;
    private DefaultInterestDetailService defaultInterestDetailService;
    private RightsRepaymentDetailService rightsRepaymentDetailService;
    private LendOrderService lendOrderService;
    private RightsPrepayDetailService rightsPrepayDetailService;
    private LendLoanBindingService lendLoanBindingService;
    private FeesItemService feesItemService;
    private LendOrderDetailFeesService lendOrderDetailFeesService;
    private LendOrderReceiveService lendOrderReceiveService;
    private LoanPublishService loanPublishService;
    private UserInfoService userInfoService;
    private AwardDetailService awardDetailService;
    private UserAccountOperateService userAccountOperateService;
    private LoanProductService loanProductService;
    private ConstantDefineCached constantDefineCached;
    private UserOpenIdService userOpenIdService;
    private ConstantDefineService constantDefineService;
    private LendProductService lendProductService;
    private boolean isLastRepaymentPlan ;
    private boolean isAhead ;
    private Long theRepaymentPlanId;
    private Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap ;
    
    private RateLendOrderService rateLendOrderService ;
    private CapitalFlowService capitalFlowService;
    private Schedule schedule;

    public RepaymentEvent(LoanApplication loanApplication,LoanPublish loanPublish, RepaymentPlan repaymentPlan, long payOrderId, Date repaymentDate, BigDecimal balance, long adminId, long loanAccountId, long systemAccountId,
                          Date financialLendDate, BigDecimal allInterest, boolean ignoreDelay, List<LoanApplicationFeesItem> commonFeesItems,
                          List<LoanApplicationFeesItem> breakFessItems, Map<Long, BigDecimal> lendsRatioMap, Map<Long, Long> lendsOrderMap,
                          AccountValueChangedQueue avcq, RepaymentPlanService repaymentPlanService,
                          CreditorRightsService creditorRightsService, LoanFeesDetailService loanFeesDetailService,
                          LoanApplicationFeesItemService loanApplicationFeesItemService, UserAccountService userAccountService,
                          LoanApplicationService loanApplicationService, RepaymentRecordService repaymentRecordService,
                          DefaultInterestDetailService defaultInterestDetailService, RightsRepaymentDetailService rightsRepaymentDetailService,
                          LendOrderService lendOrderService, RightsPrepayDetailService rightsPrepayDetailService,
                          LendOrderReceiveService lendOrderReceiveService, LendLoanBindingService lendLoanBindingService,
                          FeesItemService feesItemService, LendOrderDetailFeesService lendOrderDetailFeesService,LendProductService lendProductService,
                          LoanPublishService loanPublishService, UserInfoService userInfoService, AwardDetailService awardDetailService,
                          UserAccountOperateService userAccountOperateService, LoanProductService loanProductService, ConstantDefineCached constantDefineCached, 
                          UserOpenIdService userOpenIdService, ConstantDefineService constantDefineService,boolean isLastRepaymentPlan,boolean isAhead,Long theRepaymentPlanId,
                          RateLendOrderService rateLendOrderService,Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap,CapitalFlowService capitalFlowService,Schedule schedule) {
        this.loanApplication = loanApplication;
        this.loanPublish = loanPublish;
        this.repaymentPlan = repaymentPlan;
        this.payOrderId = payOrderId;
        this.repaymentDate = repaymentDate;
        this.balance = balance;
        this.adminId = adminId;
        this.loanAccountId = loanAccountId;
        this.systemAccountId = systemAccountId;
        this.financialLendDate = financialLendDate;
        this.allInterest = allInterest;
        this.ignoreDelay = ignoreDelay;
        this.commonFeesItems = commonFeesItems;
        this.breakFessItems = breakFessItems;
        this.lendsRatioMap = lendsRatioMap;
        this.lendsOrderMap = lendsOrderMap;
        this.avcq = avcq;
        this.repaymentPlanService = repaymentPlanService;
        this.creditorRightsService = creditorRightsService;
        this.loanFeesDetailService = loanFeesDetailService;
        this.loanApplicationFeesItemService = loanApplicationFeesItemService;
        this.userAccountService = userAccountService;
        this.loanApplicationService = loanApplicationService;
        this.repaymentRecordService = repaymentRecordService;
        this.defaultInterestDetailService = defaultInterestDetailService;
        this.rightsRepaymentDetailService = rightsRepaymentDetailService;
        this.lendOrderService = lendOrderService;
        this.rightsPrepayDetailService = rightsPrepayDetailService;
        this.lendOrderReceiveService = lendOrderReceiveService;
        this.lendLoanBindingService = lendLoanBindingService;
        this.feesItemService = feesItemService;
        this.lendOrderDetailFeesService = lendOrderDetailFeesService;
        this.loanPublishService = loanPublishService;
        this.userInfoService = userInfoService;
        this.awardDetailService = awardDetailService;
        this.userAccountOperateService = userAccountOperateService;
        this.loanProductService = loanProductService;
        this.constantDefineCached = constantDefineCached;
        this.userOpenIdService = userOpenIdService;
        this.constantDefineService = constantDefineService;
        this.lendProductService = lendProductService;
        this.isLastRepaymentPlan = isLastRepaymentPlan;
        this.isAhead = isAhead ;
        this.theRepaymentPlanId = theRepaymentPlanId ;
        this.rateLendOrderService = rateLendOrderService ;
        this.resultMap = resultMap;
        this.capitalFlowService = capitalFlowService;
        this.schedule = schedule;
    }


    @Override
    public void fire() throws Exception {


        Date today = new Date();
        LoanPublish loanPublish = loanPublishService.findById(loanApplication.getLoanApplicationId());
        long systemOperationIdAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue());
        List<RightsRepaymentDetail> rightsRepaymentLendOrderDetails = rightsRepaymentDetailService.getRightsRepaymentDetailsAndOrderByPlanId(repaymentPlan.getRepaymentPlanId());
        //根据订单ID索引债权还款明细的MAP
        Map<Long,Long> rightsOrderMap = new HashMap<>();
        for(RightsRepaymentDetail r: rightsRepaymentLendOrderDetails){
        	rightsOrderMap.put(r.getLendOrderId(),r.getRightsRepaymentDetailId());
        }
        Map<Long,RightsRepaymentDetail> rightsMap = new HashMap<>(); 
        
        //这个循环只是为了使用代码中的break;
        for (int i = 0; i < 1; i++) {
            if (BigDecimalUtil.compareTo(balance, BigDecimal.ZERO, true, 2) > 0) {
                Command saveRepaymentRecordCommand;


                //创建还款记录
                RepaymentRecord repaymentRecord = new RepaymentRecord();
                repaymentRecord.setRepaymentPlanId(repaymentPlan.getRepaymentPlanId());
                repaymentRecord.setCustomerAccountId(loanAccountId);
                repaymentRecord.setLoanApplicationId(loanApplication.getLoanApplicationId());
                repaymentRecord.setFaceDate(repaymentDate);
                repaymentRecord.setOpertionDate(today);
                repaymentRecord.setDistributeStatus(DistributeStatusEnum.WAITING.value2Char());
                repaymentRecord.setFinancialRepaymentDate(financialLendDate);//财务确认时间
                repaymentRecord.setPayId(payOrderId);

                currentRepaymentRecord = repaymentRecord;


                //计算还款时需扣的费用，保存费用清单 借款人账户扣款 增加平台账户收入
                BigDecimal sumFeesBalance = BigDecimal.ZERO;
                RepaymentFeesCommand repaymentFeesCommand = new RepaymentFeesCommand(loanApplication,loanPublish, repaymentPlan, loanAccountId, systemAccountId, adminId, balance,
                        sumFeesBalance, allInterest, commonFeesItems, loanFeesDetailService, loanApplicationFeesItemService, userAccountService, loanApplicationService, avcq,capitalFlowService,schedule);
                balance = repaymentFeesCommand.getBalance();
                sumFeesBalance = repaymentFeesCommand.getSumFeesBalance();
                addCommand(repaymentFeesCommand);

                repaymentRecord.setRepaymentFees(sumFeesBalance);

                if (BigDecimalUtil.compareTo(balance, BigDecimal.ZERO, true, 2) <= 0) {
                    saveRepaymentRecordCommand = new SaveRepaymentRecordCommand(repaymentRecord, repaymentRecordService, true);
                    addCommand(saveRepaymentRecordCommand);
                    logger.debug("金额不足以进行以下操作");
                    balance = BigDecimal.valueOf(-1);
                    break;
                }


                //统计罚息
                Map parameters = new HashMap();
                parameters.put("repaymentPlanId", repaymentPlan.getRepaymentPlanId());
                parameters.put("repaymentState", FeesDetailEnum.UNPAY.getValue());
                List<DefaultInterestDetail> defaultInterestDetails = defaultInterestDetailService.findBy(parameters);
                BigDecimal sumDefaultInterest = BigDecimal.ZERO;
                BigDecimal sumDefaultInterest18 = BigDecimal.ZERO;
                int delayDays = 0;//计算迟还天数
                boolean haveDelayInterest = false;
                if (defaultInterestDetails != null && defaultInterestDetails.size() > 0) {
                    haveDelayInterest = true;
                    //查询借款合同的费用清单，如果有逾期的费用项目，则扣逾期费，如忽略逾期则将逾期状态改为减免
                    BigDecimal delayFees = BigDecimal.ZERO;
                    RepaymentDelayFeesCommand repaymentDelayFeesCommand = new RepaymentDelayFeesCommand(
                            loanApplication.getLoanApplicationId(),loanPublish, repaymentPlan, ignoreDelay, balance, delayFees,
                            loanAccountId, systemAccountId, loanApplicationFeesItemService, loanFeesDetailService, loanApplicationService, avcq,
                            capitalFlowService,schedule,userAccountService);
                    balance = repaymentDelayFeesCommand.getBalance();
                    delayFees = repaymentDelayFeesCommand.getDelayFees();
                    addCommand(repaymentDelayFeesCommand);

                    repaymentRecord.setRepaymentFees(repaymentRecord.getRepaymentFees().add(delayFees));
                    if (BigDecimalUtil.compareTo(balance, BigDecimal.ZERO, true, 2) <= 0) {
                        saveRepaymentRecordCommand = new SaveRepaymentRecordCommand(repaymentRecord, repaymentRecordService, true);
                        addCommand(saveRepaymentRecordCommand);
                        balance = BigDecimal.valueOf(-1);
                        logger.debug("金额不足以进行以下操作");
                        break;
                    }

                    //计算罚息，如忽略罚息，则将状态改为减免
                    RepaymentDefaultInterestCommand repaymentDefaultInterestCommand = new RepaymentDefaultInterestCommand(defaultInterestDetails, repaymentRecord,
                            delayDays, ignoreDelay, balance, sumDefaultInterest, sumDefaultInterest18, defaultInterestDetailService);
                    balance = repaymentDefaultInterestCommand.getBalance();
                    sumDefaultInterest = repaymentDefaultInterestCommand.getSumDefaultInterest();
                    sumDefaultInterest18 = repaymentDefaultInterestCommand.getSumDefaultInterest18();
                    delayDays = repaymentDefaultInterestCommand.getDelayDays();
                    addCommand(repaymentDefaultInterestCommand);


                }
                //保存还款记录中的罚息金额
                repaymentRecord.setDepalFine(sumDefaultInterest);
                saveRepaymentRecordCommand = new SaveRepaymentRecordCommand(repaymentRecord, repaymentRecordService, true);
                addCommand(saveRepaymentRecordCommand);

                //如产生罚息且不忽略罚息，根据已统计的罚息，扣除借款人罚息，增加平台账户与出借人列表的罚息收入流水
                if (haveDelayInterest && !ignoreDelay) {
                    Command payRepaymentDefaultInterestCommand = new PayRepaymentDefaultInterestCommand(
                            loanAccountId, sumDefaultInterest, loanApplication,systemAccountId, repaymentRecord, breakFessItems, lendsRatioMap, lendsOrderMap,
                            avcq,loanPublish,capitalFlowService,schedule,userAccountService,lendOrderService);
                    addCommand(payRepaymentDefaultInterestCommand);
                }

                if (BigDecimalUtil.compareTo(balance, BigDecimal.ZERO, true, 2) <= 0) {
                    logger.debug("金额不足以进行以下操作");
                    balance = BigDecimal.valueOf(-1);
                    break;
                }

                //将此时的钱，作为出借人回款去分配
                BigDecimal balanceAfterFees = balance;
                //此次还款实还最大金额
                BigDecimal shouldMaxBalanceAfterFees = BigDecimal.ZERO;

                boolean theLastRepayment = false;
                //还款
                BigDecimal currentShouldCalital = BigDecimal.ZERO;
                boolean repayInterest = true;
                RepaymentCommand repaymentCommand = new RepaymentCommand(
                        loanApplication,loanPublish, repaymentPlan, balance, currentShouldCalital, theLastRepayment, loanAccountId, repayInterest,
                        repaymentRecord, avcq, repaymentPlanService,capitalFlowService,schedule);
                balance = repaymentCommand.getBalance();
                theLastRepayment = repaymentCommand.isTheLastRepayment();
                addCommand(repaymentCommand);

                //保存还款记录(是否迟还)
                if (delayDays > 0) {
                    repaymentRecord.setIsDelay(RepaymentRecord.ISDELAY_YES);
                } else {
                    repaymentRecord.setIsDelay(RepaymentRecord.ISDELAY_NO);
                }
                repaymentRecord.setIsEarly(RepaymentRecord.ISEARLY_NO);
                saveRepaymentRecordCommand = new SaveRepaymentRecordCommand(repaymentRecord, repaymentRecordService, false);
                addCommand(saveRepaymentRecordCommand);


                //还款分配，更新债权还款明细，增加出借人流水
                int lendRightIndex = 0;
                BigDecimal theLastLendCalital = BigDecimal.ZERO;
                List<RightsRepaymentDetail> rightsRepaymentDetails = rightsRepaymentDetailService.getRightsRepaymentDetailsByPlanId(repaymentPlan.getRepaymentPlanId());
                BigDecimal rightBalanceSum = BigDecimal.ZERO;

                BigDecimal balanceAfterCalc = BigDecimal.ZERO;
                for (RightsRepaymentDetail rightsRepaymentDetail : rightsRepaymentDetails) {
                    CreditorRights creditorRights = creditorRightsService.findById(rightsRepaymentDetail.getCreditorRightsId(), false);
                    //非已生效债权排除
                    if (creditorRights.getRightsState() != CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char())
                        continue;
                    //用于保存精度误差,精度误差的值存入平台账户
                    BigDecimal accuracy = BigDecimal.ZERO;

                    BigDecimal theLenderRightsBalance;
                    if (theLastRepayment) {
                        theLenderRightsBalance = rightsRepaymentDetail.getShouldBalance2().subtract(rightsRepaymentDetail.getFactBalance());
                    } else {
                        if (lendRightIndex == rightsRepaymentDetails.size() - 1) {
                            theLenderRightsBalance = balanceAfterFees.subtract(rightBalanceSum);
                        } else {
                            BigDecimal theRightsRatio = creditorRights.getRightsWorth().divide(loanApplication.getLoanBalance(), 18, BigDecimal.ROUND_DOWN);
                            theLenderRightsBalance = balanceAfterFees.multiply(theRightsRatio);
                            theLenderRightsBalance = BigDecimalUtil.down(theLenderRightsBalance, 2);//降位处理
                            rightBalanceSum = rightBalanceSum.add(theLenderRightsBalance);
                        }

                    }

                    //如有平台垫付的利息，则先还平台垫付的利息
                    BigDecimal repayDetailBalance = BigDecimal.ZERO;
                    RepaymentRightsPrepayCommand repaymentRightsPrepayCommand = new RepaymentRightsPrepayCommand(theLenderRightsBalance,
                            repayDetailBalance, rightsRepaymentDetail.getCreditorRightsId(), avcq,
                            rightsPrepayDetailService,capitalFlowService,schedule,userAccountService);
                    repayDetailBalance = repaymentRightsPrepayCommand.getRepayDetailBalance();
                    if (!theLastRepayment) {
                        theLenderRightsBalance = theLenderRightsBalance.subtract(repayDetailBalance);
                    }


                    addCommand(repaymentRightsPrepayCommand);
                    if (BigDecimalUtil.compareTo(theLenderRightsBalance, BigDecimal.ZERO, false, 2) <= 0) {
                        logger.debug("本债权扣掉平台垫付息利息，不足以给出借人回款");
                        balance = BigDecimal.valueOf(-1);
                        break;
                    }

                    //回款至出借账户及平台账户
                    BigDecimal theLendBalance = BigDecimal.ZERO;
                    LendOrder lendOrder = lendOrderService.findById(creditorRights.getLendOrderId());
                    BigDecimal theLendInterest = BigDecimal.ZERO;
                    BigDecimal theLenderRightsBalance2 = BigDecimalUtil.down(theLenderRightsBalance, 2);
                    accuracy = accuracy.add(theLenderRightsBalance.subtract(theLenderRightsBalance2));

                    Repayment2LenderCommand repayment2LenderCommand = new Repayment2LenderCommand(theLastRepayment, repayDetailBalance, creditorRights, lendRightIndex,
                            rightsRepaymentDetails, theLenderRightsBalance2, currentShouldCalital,
                            theLastLendCalital, theLendInterest, sumDefaultInterest, adminId, repaymentDate, delayDays, loanApplication,loanPublish, lendOrder,
                            repaymentRecord, rightsRepaymentDetail, avcq, creditorRightsService,
                            rightsRepaymentDetailService, lendOrderService, lendOrderReceiveService, userAccountService, repaymentRecordService, userInfoService, userOpenIdService,
                            constantDefineService,isAhead,theRepaymentPlanId,resultMap,capitalFlowService,schedule);
                    addCommand(repayment2LenderCommand);
                    theLendInterest = repayment2LenderCommand.getTheLendInterest();


                    //出借订单回款扣费
                    BigDecimal theLendFeesBalance = BigDecimal.ZERO;
                    theLendBalance = theLenderRightsBalance;
                    Command repaymentLenderFeesCommand = new RepaymentLenderFeesCommand(systemAccountId, repaymentDate, theLendBalance, theLendFeesBalance, lendOrder,loanPublish,
                            lendLoanBindingService, repaymentPlan, feesItemService, loanApplication, rightsRepaymentDetail, avcq,
                            lendOrderDetailFeesService, lendOrderReceiveService, resultMap,capitalFlowService,schedule,userAccountService);
                    addCommand(repaymentLenderFeesCommand);

                    //todo 出借订单结清时扣费
                    RepaymentCompleteLenderFeesCommand repaymentCompleteLenderFeesCommandnew = new RepaymentCompleteLenderFeesCommand(loanApplication,avcq,constantDefineCached,
                    		userAccountService,lendOrder,lendProductService,feesItemService,lendOrderReceiveService,rightsRepaymentDetail,loanPublish,
                    		repaymentPlanService,resultMap,capitalFlowService,schedule);
                    addCommand(repaymentCompleteLenderFeesCommandnew);
                    //todo 待测试
                    //保存理财订单收益
                    Command saveLendOrderProfitCommand = new SaveLendOrderProfitCommand(theLendFeesBalance, theLendInterest, lendOrder, lendOrderService);
                    addCommand(saveLendOrderProfitCommand);
                    //当期 还款计划 还款完成，继续下一期 的还款计划

                    lendRightIndex++;
                    //后续平台补差时使用
                    balanceAfterCalc = balanceAfterCalc.add(theLenderRightsBalance2);
                }

                //计算平台补差
                shouldMaxBalanceAfterFees = balanceAfterFees.subtract(balance);
                if(!isLastRepaymentPlan && shouldMaxBalanceAfterFees.compareTo( balanceAfterFees) < 0){
                	balanceAfterFees = shouldMaxBalanceAfterFees;
                }
                SupplementaryDifferenceCommand sdfc = new SupplementaryDifferenceCommand(constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue()),balanceAfterFees,balanceAfterCalc,repaymentRecord,avcq);
                addCommand(sdfc);

                for(RightsRepaymentDetail r:rightsRepaymentDetails){
                	rightsMap.put(r.getRightsRepaymentDetailId(), r);
                }
                //todoed 还款奖励
//                if (loanPublish.getAwardPoint() != null && loanPublish.getAwardPoint().equals(AwardPointEnum.ATREPAYMENT.getValue()) && BigDecimalUtil.compareTo(loanPublish.getAwardRate(),BigDecimal.valueOf(0),2) > 0) {
//                    Command awardDetailCommand = new RepaymentAward2LendCommand(systemOperationIdAccountId, avcq, loanApplication, rightsRepaymentDetails, loanPublishService, awardDetailService, userAccountService, userInfoService, lendOrderService,creditorRightsService);
//                    addCommand(awardDetailCommand);
                    Command awardDetailCommand = new RepaymentRateAward2LendCommand(systemOperationIdAccountId, avcq, loanApplication,   awardDetailService, 
                    		userAccountService, userInfoService,rateLendOrderService, repaymentPlan,rightsMap,AwardPointEnum.ATREPAYMENT.getValue(),
                    		rightsOrderMap,repaymentPlanService,loanPublishService,creditorRightsService,lendOrderService,loanProductService,false,resultMap,capitalFlowService,schedule);
                    addCommand(awardDetailCommand);
//                }
                

            }


        }

        //修改借款申请状态和债权状态
        Command repaymentCompleteLoanAppCommand = new RepaymentCompleteLoanAppCommand(loanApplication, false, avcq, loanApplicationService,
                creditorRightsService, repaymentPlanService, rightsRepaymentDetailService, lendOrderService, loanPublishService, awardDetailService,
                loanProductService, userInfoService, constantDefineCached, userAccountService);
        addCommand(repaymentCompleteLoanAppCommand);
        Command awardDetailCommand = new RepaymentRateAward2LendCommand(systemOperationIdAccountId, avcq, loanApplication,   awardDetailService, userAccountService, 
        		userInfoService,rateLendOrderService, repaymentPlan,rightsMap,AwardPointEnum.ATCOMPLETE.getValue(),rightsOrderMap,repaymentPlanService,loanPublishService,creditorRightsService,lendOrderService,loanProductService,false,resultMap,capitalFlowService,schedule);
        addCommand(awardDetailCommand);

        //


        EventTriggerInfo eventTriggerInfo = new EventTriggerInfo();
        eventTriggerInfo.setEventId(Event.EVENT_REPAYMENT);
        eventTriggerInfo.setHappenResult(EventTriggerInfo.HAPPENRESULT_FAILURE);
        eventTriggerInfo.setHappenTime(new Date());
        eventTriggerInfo.setTriggerObjId(String.valueOf(loanApplication.getLoanApplicationId()));
        eventTriggerInfo.setTriggerType("LoanApplication");
        super.setEventTriggerInfo(eventTriggerInfo);
        super.fire();

    }

    public BigDecimal getBalance() {
        if (balance == null) {
            return BigDecimal.valueOf(-1);
        }
        return BigDecimalUtil.down(balance, 2);
    }

    public RepaymentRecord getCurrentRepaymentRecord() {
        return currentRepaymentRecord;
    }
}
