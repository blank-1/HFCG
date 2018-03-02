package com.xt.cfp.core.event.command;


import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
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
 * Created by Renyulin on 14-6-11 涓嬪崍6:15.
 */
public class RepaymentCompleteLenderFeesCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private LoanApplication loanApplication;
    private AccountValueChangedQueue avcq;


    private RepaymentPlanService repaymentPlanService;

    private ConstantDefineCached constantDefineCached;
    private UserAccountService userAccountService;
    private LendOrder lendOrder;
    private LendProductService lendProductService;
    private FeesItemService feesItemService;
    private LendOrderReceiveService lendOrderReceiveService;
    private RightsRepaymentDetail rightsRepaymentDetail;
    private LoanPublish loanPublish;
    private CapitalFlowService capitalFlowService;
    private Schedule schedule;

    private Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap ;

    public RepaymentCompleteLenderFeesCommand(LoanApplication loanApplication, AccountValueChangedQueue avcq,ConstantDefineCached constantDefineCached,
                                              UserAccountService userAccountService,LendOrder lendOrder,LendProductService lendProductService,FeesItemService feesItemService,
                                              LendOrderReceiveService lendOrderReceiveService,RightsRepaymentDetail rightsRepaymentDetail,LoanPublish loanPublish,
                                              RepaymentPlanService repaymentPlanService, Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap,
                                              CapitalFlowService capitalFlowService,Schedule schedule) {
        this.loanApplication = loanApplication;
        this.avcq = avcq;
        this.constantDefineCached = constantDefineCached;
        this.userAccountService = userAccountService;
        this.lendOrder = lendOrder;
        this.lendProductService = lendProductService;
        this.feesItemService = feesItemService;
        this.lendOrderReceiveService = lendOrderReceiveService;
        this.rightsRepaymentDetail = rightsRepaymentDetail;
        this.loanPublish = loanPublish;
        this.repaymentPlanService = repaymentPlanService;
        this.resultMap = resultMap ;
        this.capitalFlowService = capitalFlowService;
        this.schedule = schedule;

    }

    @Override
    public Object execute() throws Exception {


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
        Date now = new Date();
        if (allComplete){
            //只处理已结清数据
            logger.debug("****还款结清扣费****");
            taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
            taskExecLog.setExecTime(new Date());
            taskExecLog.setTaskId(TaskInfo.TASKID_REPAYMENTCOMPLETELOANAPP);

            //获得出借产品所有费用项
            List<LendProductFeesItem> feeitems = lendProductService.findAllProductFeesItemsByLendOrderId(lendOrder.getLendOrderId());
            //需要计算结清费用项
            List<LendProductFeesItem> complateFeeitems = new ArrayList<LendProductFeesItem>();
            for (LendProductFeesItem feesItem : feeitems) {
                if (FeesPointEnum.ATCOMPLETE.value2Char() == feesItem.getChargePoint()) {
                    //已结清费用
                    FeesItem fis = feesItemService.findById(feesItem.getFeesItemId());
                    BigDecimal exceptProfit = lendOrderReceiveService.getExceptProfitByUserId(lendOrder.getLendOrderId(), lendOrder.getLendUserId());
                    if(fis!=null){
                    /*    BigDecimal fee = feesItemService.calculateFeesBalance(fis.getFeesItemId(), BigDecimal.ZERO, BigDecimal.ZERO,
                                rightsRepaymentDetail.getShouldCapital2(), rightsRepaymentDetail.getShouldInterest2(), exceptProfit, rightsRepaymentDetail.getShouldInterest2());*/
                        BigDecimal fee = lendProductService.calculateLendProductFeesItemBalance(feesItem, BigDecimal.ZERO, BigDecimal.ZERO, rightsRepaymentDetail.getShouldCapital2(), rightsRepaymentDetail.getShouldInterest2(), exceptProfit, rightsRepaymentDetail.getShouldInterest2());
                        BigDecimal fee_ = BigDecimalUtil.up(fee,2);
                        //计算费用大于0
                        if (BigDecimalUtil.compareTo(fee_, BigDecimal.ZERO, false, 2) > 0){
                            //出借人资金账户
                            UserAccount lendUserAccount = userAccountService.getCashAccount(lendOrder.getLendUserId());
                            //借款人资金账户
                            UserAccount loanUserAccount = userAccountService.getCashAccount(loanApplication.getUserId());
                            //平台收支账户
                            long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue());

                          //【20170203省心计划需求：记录还款结清费用 】
                            if( resultMap.get(rightsRepaymentDetail.getLendAccountId()) == null ){
                         	   Map<Long,Map<String,BigDecimal>> valueMap = new HashMap<>();
                         	   Map<String,BigDecimal> feesMap = new HashMap<>();
                         	   feesMap.put(RightsRepaymentDetail.FEES, fee_);
                         	   valueMap.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), feesMap);
                         	   resultMap.put(rightsRepaymentDetail.getLendAccountId(), valueMap);
                            }else{
                         	   Map<Long,Map<String,BigDecimal>> rrdMaps = resultMap.get(rightsRepaymentDetail.getLendAccountId());
                         	   if(rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId())== null ){
                         		  Map<String,BigDecimal> feesMap = new HashMap<>();
                         		  feesMap.put(RightsRepaymentDetail.FEES, fee_);
                         		  rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), feesMap);
                         	   }else{
                         		  Map<String,BigDecimal> valueMap = rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId());
                         		  BigDecimal feesValue = valueMap.get(RightsRepaymentDetail.FEES);
                         		  if(feesValue == null){
                         			 feesValue = fee_;
                         		  }else{
                         			 feesValue = feesValue.add(fee_);
                         		  }
                         		  valueMap.put(RightsRepaymentDetail.FEES, feesValue);
                         		  rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), valueMap) ;
                         	   }
                            }
                            
                            //出借人支付费用
                            String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVE_END_FEES_PAY,lendOrder.getOrderCode(), fis.getItemName());
                            AccountValueChanged avcPay = new AccountValueChanged(lendUserAccount.getAccId(), fee_, fee_, AccountConstants.AccountOperateEnum.PAY.getValue(),
                                    AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue(),  "LendProductFeesItem", VisiableEnum.DISPLAY.getValue(), feesItem.getFeesItemId(),
                                    AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                                    lendOrder.getLendOrderId(), now, descPay, true);
                            avcq.addAccountValueChanged(avcPay);

                            UserAccount platformAccount = userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);
                            CapitalFlow cap=new CapitalFlow();
                            cap.setScheduleId(schedule.getScheduleId());
                            cap.setOperationType(Integer.parseInt(HFOperationEnum.ALLOCATION_PERSON_TO_COMPANY.getValue()));
                            cap.setFromUser(lendOrder.getLendUserId());
                            cap.setToUser(platformAccount.getUserId());
                            cap.setAmount(fee_);
                            cap.setStartTime(new Date());
                            cap.setBusinessId(lendOrder.getLendOrderId());
                            cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
                            cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
                            capitalFlowService.addCapital(cap);

                            //瓜分
                            if (BigDecimalUtil.compareTo(feesItem.getWorkflowRatio(), BigDecimal.ONE, false, 2) == 0){
                                //平台100%收取
                                platformFeeHandle(systemAccountId,fee_,feesItem,now,fis);
                            }else if(BigDecimalUtil.compareTo(feesItem.getWorkflowRatio(), BigDecimal.ZERO, false, 2) == 0){
                                //平台不收取,借款人100%收取
                               loanFeeHandle(loanUserAccount,fee_,feesItem,now,fis);
                            }else{
                                //分摊
                                BigDecimal platformFee = fee_.multiply(feesItem.getWorkflowRatio());
                                platformFee = BigDecimalUtil.up(platformFee, 2);
                                BigDecimal loanFee = fee_.subtract(platformFee);

                                //平台
                                platformFeeHandle(systemAccountId,platformFee,feesItem,now,fis);
                                //借款人
                                loanFeeHandle(loanUserAccount,loanFee,feesItem,now,fis);
                            }
                        }
                    }
                }
            }

            taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);


            taskExecLog.setLogInfo("借款申请" + loanApplication.getLoanApplicationCode() + " 还款结清扣费完成");
            logger.debug("****还款结清扣费完成****");
        }

        return null;
    }

    /**
     * 平台收入平台费
     * @param systemAccountId
     * @param platformFee
     * @param feesItem
     * @param now
     * @param fis
     */
    private void platformFeeHandle(Long systemAccountId, BigDecimal platformFee, LendProductFeesItem feesItem, Date now, FeesItem fis){
        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVE_FEES_INCOME, loanApplication.getLoanApplicationName(), lendOrder.getOrderCode(), fis.getItemName());
        AccountValueChanged avcIncome = new AccountValueChanged(systemAccountId, platformFee,
                platformFee, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue(),
                "LendProductFeesItem", VisiableEnum.DISPLAY.getValue(), feesItem.getFeesItemId(),
                AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                systemAccountId, now, descIncome, true);
        avcq.addAccountValueChanged(avcIncome);
    }

    /**
     * 借款人收入平台费
     * @param loanUserAccount
     * @param fee_
     * @param feesItem
     * @param now
     * @param fis
     */
    private void loanFeeHandle(UserAccount loanUserAccount, BigDecimal fee_, LendProductFeesItem feesItem, Date now, FeesItem fis){
        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVE_FEES_INCOME, loanApplication.getLoanApplicationName(), lendOrder.getOrderCode(), fis.getItemName());
        AccountValueChanged avcIncome = new AccountValueChanged(loanUserAccount.getAccId(), fee_,
                fee_, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue(),
                "LendProductFeesItem", VisiableEnum.DISPLAY.getValue(), feesItem.getFeesItemId(),
                AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                loanApplication.getLoanApplicationId(), now, descIncome, true);
        avcq.addAccountValueChanged(avcIncome);
    }

}
