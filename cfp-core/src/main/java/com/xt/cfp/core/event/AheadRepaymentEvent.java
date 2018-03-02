package com.xt.cfp.core.event;


import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.CreditorRightsConstants.CreditorRightsStateEnum;
import com.xt.cfp.core.event.command.*;
import com.xt.cfp.core.event.pojo.EventTriggerInfo;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Renyulin on 14-6-11 下午3:15.
 */
public class AheadRepaymentEvent extends Event {

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
    private LoanProductService loanProductService;
    private ConstantDefineCached constantDefineCached;
    private UserOpenIdService userOpenIdService;
    private ConstantDefineService constantDefineService;
    private LendProductService lendProductService;
    private List<RepaymentPlan> repaymentPlans;
    private RateLendOrderService rateLendOrderService;
    private boolean isAhead ;
    private Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap ;
    private CapitalFlowService capitalFlowService;
    private Schedule schedule;

    public AheadRepaymentEvent(LoanApplication loanApplication, LoanPublish loanPublish, List<RepaymentPlan> repaymentPlans, long payOrderId, Date repaymentDate, BigDecimal balance, long adminId, long loanAccountId, long systemAccountId,
                               Date financialLendDate, BigDecimal allInterest, boolean ignoreDelay, List<LoanApplicationFeesItem> commonFeesItems,
                               List<LoanApplicationFeesItem> breakFessItems, Map<Long, Long> lendsOrderMap,
                               AccountValueChangedQueue avcq, RepaymentPlanService repaymentPlanService,
                               CreditorRightsService creditorRightsService, LoanFeesDetailService loanFeesDetailService,
                               LoanApplicationFeesItemService loanApplicationFeesItemService, UserAccountService userAccountService,
                               LoanApplicationService loanApplicationService, RepaymentRecordService repaymentRecordService,
                               DefaultInterestDetailService defaultInterestDetailService, RightsRepaymentDetailService rightsRepaymentDetailService,
                               LendOrderService lendOrderService, RightsPrepayDetailService rightsPrepayDetailService,
                               LendOrderReceiveService lendOrderReceiveService, LendLoanBindingService lendLoanBindingService,
                               FeesItemService feesItemService, LendOrderDetailFeesService lendOrderDetailFeesService, LendProductService lendProductService,
                               LoanPublishService loanPublishService, UserInfoService userInfoService, AwardDetailService awardDetailService,
                               UserAccountOperateService userAccountOperateService, LoanProductService loanProductService, ConstantDefineCached constantDefineCached, UserOpenIdService userOpenIdService, 
                               ConstantDefineService constantDefineService,RateLendOrderService rateLendOrderService,boolean isAhead,RepaymentPlan repaymentPlan,
                               Map<Long ,Map<Long,Map<String,BigDecimal>>> resutlMap,CapitalFlowService capitalFlowService,Schedule schedule) {
        this.loanApplication = loanApplication;
        this.loanPublish = loanPublish;
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
        this.repaymentPlans = repaymentPlans;
        this.loanProductService = loanProductService;
        this.constantDefineCached = constantDefineCached;
        this.userOpenIdService = userOpenIdService;
        this.constantDefineService = constantDefineService;
        this.lendProductService = lendProductService;
        this.rateLendOrderService= rateLendOrderService;
        this.isAhead = isAhead;
        this.repaymentPlan= repaymentPlan;
        this.resultMap = resultMap ;
        this.capitalFlowService = capitalFlowService;
        this.schedule = schedule;
    }


    @Override
    public void fire() throws Exception {


        Date today = new Date();
        LoanPublish loanPublish = loanPublishService.findById(loanApplication.getLoanApplicationId());

        if (repaymentPlans==null || repaymentPlans.size()==0){
            return;
        }
        List<RightsRepaymentDetail> rightsRepaymentLendOrderDetails = rightsRepaymentDetailService.getRightsRepaymentDetailsAndOrderByPlanId(repaymentPlan.getRepaymentPlanId());
        //根据订单ID索引债权还款明细的MAP
        Map<Long,Long> rightsOrderMap = new HashMap<>();
        Map<Long,RightsRepaymentDetail> rightsMap = new HashMap<>(); 
        for(RightsRepaymentDetail r: rightsRepaymentLendOrderDetails){
        	rightsOrderMap.put(r.getLendOrderId(),r.getRightsRepaymentDetailId());
        	rightsMap.put(r.getRightsRepaymentDetailId(), r);
        }
        
        //收提前还款违约费
        List<CreditorRights> creditorRightsList = creditorRightsService.getByLoanApplicationId(loanApplication.getLoanApplicationId());
        for (LoanApplicationFeesItem applicationFeesItem : breakFessItems) {
            if (applicationFeesItem.getItemType().equals(String.valueOf(FeesItemTypeEnum.ITEMTYPE_EARLYREPAYMENT.getValue()))) {
                //计算提前还款费用
                BigDecimal fee = loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(applicationFeesItem, loanApplication.getConfirmBalance(), loanApplication.getInterestBalance(), BigDecimal.ZERO,
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

                fee = BigDecimalUtil.up(fee,2).setScale(2);
                balance = balance.subtract(fee);


                BigDecimal workflowRatio = applicationFeesItem.getWorkflowRatio();
                //分配给平台部分
                BigDecimal lenderFee = BigDecimalUtil.up(fee.multiply(BigDecimal.ONE.subtract(workflowRatio)), 2);

                //借款人支出费用
                String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AHEAD_REPAYMENT_FEE, loanPublish.getLoanTitle(), fee);
                //借款合同解冻
                String desc = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOAN_AHEAD_FEES_UNFREEZE, loanPublish.getLoanTitle(), applicationFeesItem.getItemName());
                AccountValueChanged avcUnfreeze = new AccountValueChanged(loanAccountId, fee,
                        fee, AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_EARLYWITHDRAWALPENALTY_INCOME.getValue(),
                        "LoanApplicationFeesItem", VisiableEnum.DISPLAY.getValue(), applicationFeesItem.getLoanApplicationFeesItemId(),
                        AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),loanApplication.getLoanApplicationId(), today, desc, true);
                avcq.addAccountValueChanged(avcUnfreeze);

                AccountValueChanged avcPay = new AccountValueChanged(loanAccountId, fee, fee, AccountConstants.AccountOperateEnum.PAY.getValue(),
                        AccountConstants.BusinessTypeEnum.FEESTYPE_EARLYWITHDRAWALPENALTY_INCOME.getValue(), "LoanApplicationFeesItem", VisiableEnum.DISPLAY.getValue(), applicationFeesItem.getLoanApplicationFeesItemId(),
                        AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),loanApplication.getLoanApplicationId(), today, descPay, true);
                avcq.addAccountValueChanged(avcPay);

                CapitalFlow cap=new CapitalFlow();
                cap.setScheduleId(schedule.getScheduleId());
                cap.setOperationType(Integer.parseInt(HFOperationEnum.UNFROZEN.getValue()));
                cap.setFromUser(loanApplication.getUserId());
                cap.setAmount(fee);
                cap.setStartTime(new Date());
                cap.setBusinessId(repaymentPlan.getRepaymentPlanId());
                cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
                cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
                capitalFlowService.addCapital(cap);


                BigDecimal lenderFee_ = BigDecimal.ZERO;
                for (CreditorRights creditorRights : creditorRightsList) {
                    //线上债权、已生效
                    if (creditorRights.getChannelType() == ChannelTypeEnum.ONLINE.value2Long() && creditorRights.getRightsState() == CreditorRightsStateEnum.EFFECTIVE.value2Char()) {
                        BigDecimal ratio = creditorRights.getRightsWorth().divide(loanApplication.getLoanBalance(), 18, BigDecimal.ROUND_DOWN);
                        BigDecimal lend_fee = BigDecimalUtil.down(lenderFee.multiply(ratio), 2);

                        //出借人资金账户
                        UserAccount lendUserAccount = userAccountService.getCashAccount(creditorRights.getLendUserId());
                        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AHEAD_REPAYMENT_FEE_USER_INCOME,loanPublish.getLoanTitle(), lend_fee);
                        AccountValueChanged avcIncome = new AccountValueChanged(lendUserAccount.getAccId(), lend_fee,
                                lend_fee, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_EARLYWITHDRAWALPENALTY_INCOME.getValue(),
                                "LoanApplicationFeesItem", VisiableEnum.DISPLAY.getValue(), applicationFeesItem.getLoanApplicationFeesItemId(),
                                AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                                creditorRights.getLendOrderId(), today, descIncome, true);
                        avcq.addAccountValueChanged(avcIncome);

                        lenderFee_ = lenderFee_.add(lend_fee);

                        cap=new CapitalFlow();
                        cap.setScheduleId(schedule.getScheduleId());
                        cap.setOperationType(Integer.parseInt(HFOperationEnum.ALLOCATION_PERSON_TO_PERSON.getValue()));
                        cap.setFromUser(loanApplication.getUserId());
                        cap.setToUser(creditorRights.getLendUserId());
                        cap.setAmount(lend_fee);
                        cap.setStartTime(new Date());
                        cap.setBusinessId(repaymentPlan.getRepaymentPlanId());
                        cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
                        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
                        capitalFlowService.addCapital(cap);
                    }
                }

                //平台收支账户
                long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue());
                String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AHEAD_REPAYMENT_FEE_PLATFORM_INCOME, loanApplication.getLoanApplicationName(), fee.subtract(lenderFee_));
                AccountValueChanged avcIncome = new AccountValueChanged(systemAccountId, fee.subtract(lenderFee_),
                        fee.subtract(lenderFee_), AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_EARLYWITHDRAWALPENALTY_INCOME.getValue(),
                        "LoanApplicationFeesItem", VisiableEnum.DISPLAY.getValue(),applicationFeesItem.getLoanApplicationFeesItemId(),
                        AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                        systemAccountId, today, descIncome, true);
                avcq.addAccountValueChanged(avcIncome);

            }
        }
        
        //实还本金总额
        BigDecimal allFactCallital = BigDecimal.ZERO ;
        //保存还款的出借人债权
        Set<CreditorRights> creditorRightsSet = new HashSet<>();
        for (RepaymentPlan repaymentPlan : repaymentPlans){

            if (BigDecimalUtil.compareTo(balance, BigDecimal.ZERO, true, 2) <= 0) {
                break;
            }

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
            repaymentRecord.setRepaymentFees(BigDecimal.ZERO);
            saveRepaymentRecordCommand = new SaveRepaymentRecordCommand(repaymentRecord, repaymentRecordService, true);
            addCommand(saveRepaymentRecordCommand);

            //将此时的钱，作为出借人回款去分配
            BigDecimal balanceAfterFees = balance;

            boolean theLastRepayment = false;
            //还款
            BigDecimal currentShouldCalital = BigDecimal.ZERO;
            boolean repayInterest = false;
            RepaymentCommand repaymentCommand = new RepaymentCommand(
                    loanApplication, loanPublish, repaymentPlan, balance, currentShouldCalital, theLastRepayment, loanAccountId, repayInterest,
                    repaymentRecord, avcq, repaymentPlanService,capitalFlowService,schedule);
            balance = repaymentCommand.getBalance();
            theLastRepayment = repaymentCommand.isTheLastRepayment();
            addCommand(repaymentCommand);
            allFactCallital = allFactCallital.add(currentShouldCalital);
            
            //保存还款记录(是否迟还)
            repaymentRecord.setIsDelay(RepaymentRecord.ISDELAY_NO);
            repaymentRecord.setIsEarly(RepaymentRecord.ISEARLY_YES);

            saveRepaymentRecordCommand = new SaveRepaymentRecordCommand(repaymentRecord, repaymentRecordService, false);
            addCommand(saveRepaymentRecordCommand);

            //还款分配，更新债权还款明细，增加出借人流水
            int lendRightIndex = 0;
            BigDecimal theLastLendCalital = BigDecimal.ZERO;
            List<RightsRepaymentDetail> rightsRepaymentDetails = rightsRepaymentDetailService.getRightsRepaymentDetailsByPlanId(repaymentPlan.getRepaymentPlanId());
            
            BigDecimal rightBalanceSum = BigDecimal.ZERO;

            BigDecimal balanceAfterCalc = BigDecimal.ZERO;
            
            for (RightsRepaymentDetail rightsRepaymentDetail : rightsRepaymentDetails) {
                CreditorRights creditorRights = creditorRightsService.findById(rightsRepaymentDetail.getCreditorRightsId(),false);
                //非已生效债权排除
                if (creditorRights.getRightsState() != CreditorRightsStateEnum.EFFECTIVE.value2Char())
                    continue;

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
                LendOrder lendOrder = lendOrderService.findById(creditorRights.getLendOrderId());
                BigDecimal theLendInterest = BigDecimal.ZERO;
                BigDecimal theLenderRightsBalance2 = BigDecimalUtil.down(theLenderRightsBalance, 2);

                AheadRepayment2LenderCommand repayment2LenderCommand = new AheadRepayment2LenderCommand(theLastRepayment, repayDetailBalance, creditorRights, lendRightIndex,
                        rightsRepaymentDetails, theLenderRightsBalance2, currentShouldCalital,
                        theLastLendCalital, theLendInterest, BigDecimal.ZERO, adminId, repaymentDate, 0, loanApplication, loanPublish, lendOrder,
                        repaymentRecord, rightsRepaymentDetail, avcq, creditorRightsService,
                        rightsRepaymentDetailService, lendOrderService, lendOrderReceiveService, userAccountService, repaymentRecordService, userInfoService, userOpenIdService, constantDefineService);
                addCommand(repayment2LenderCommand);


                //todo 出借订单结清时扣费
                RepaymentCompleteLenderFeesCommand repaymentCompleteLenderFeesCommandnew = new RepaymentCompleteLenderFeesCommand(loanApplication,
                		avcq, constantDefineCached, userAccountService, lendOrder, lendProductService, feesItemService, lendOrderReceiveService, rightsRepaymentDetail,
                		loanPublish, repaymentPlanService,resultMap,capitalFlowService,schedule);
                addCommand(repaymentCompleteLenderFeesCommandnew);
                //todo 待测试
                //保存理财订单收益
                Command saveLendOrderProfitCommand = new SaveLendOrderProfitCommand(BigDecimal.ZERO, theLendInterest, lendOrder, lendOrderService);
                addCommand(saveLendOrderProfitCommand);
                //当期 还款计划 还款完成，继续下一期 的还款计划

                lendRightIndex++;
                //后续平台补差时使用
                balanceAfterCalc = balanceAfterCalc.add(theLenderRightsBalance2);
                
                creditorRightsSet.add(creditorRights);
               
            }

        }
        
        long systemOperationIdAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue());

        //修改借款申请状态和债权状态
        Command repaymentCompleteLoanAppCommand = new RepaymentCompleteLoanAppCommand(loanApplication, true, avcq, loanApplicationService,
                creditorRightsService, repaymentPlanService, rightsRepaymentDetailService, lendOrderService, loanPublishService, awardDetailService,
                loanProductService, userInfoService, constantDefineCached, userAccountService);
        addCommand(repaymentCompleteLoanAppCommand);
        //奖励和加息
        Command awardDetailCommand = new RepaymentRateAward2LendCommand(systemOperationIdAccountId, avcq, loanApplication,   awardDetailService, userAccountService, 
        		userInfoService,rateLendOrderService, repaymentPlan,rightsMap,AwardPointEnum.ATCOMPLETE.getValue(),rightsOrderMap,repaymentPlanService,
        		loanPublishService,creditorRightsService,lendOrderService,loanProductService,isAhead,resultMap,capitalFlowService,schedule);
        addCommand(awardDetailCommand);

        //todo 发送提前还款通知短信
        // 只发送 非省心计划订单和非省心计划子订单的短信
        LendOrder lendPOrder = null ;
        List<Long> userIds = new ArrayList<>();
        Date payTime = null ;
        for (CreditorRights creditorRights : creditorRightsSet) {
            //线上债权、已生效
           LendOrder lendOrder = lendOrderService.findById(creditorRights.getLendOrderId());
           if(lendOrder.getLendOrderPId()!=null){
        	   lendPOrder = lendOrderService.findById(lendOrder.getLendOrderPId());
           }
           if(lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
        		   ||(lendPOrder != null && lendPOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))){
        	   lendPOrder = null ;
        	   continue;
           }
           UserInfo user = userInfoService.getUserByUserId(lendOrder.getLendUserId());
           lendOrderReceiveService.sendToLenderAheadRepaymentSuccessMsg(lendOrder,loanPublish,user.getMobileNo());
           userIds.add(lendOrder.getLendUserId());
           if(payTime == null) {
        	   payTime = lendOrder.getPayTime();
           }
           lendPOrder = null ;
        }
        lendOrderReceiveService.sendToLenderAheadRepaymentSuccessWebMsg(payTime,loanPublish,userIds);

        

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
