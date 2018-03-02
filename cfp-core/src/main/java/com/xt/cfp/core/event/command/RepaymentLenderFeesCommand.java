package com.xt.cfp.core.event.command;


import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.HFOperationEnum;
import com.xt.cfp.core.constants.ScheduleEnum;
import com.xt.cfp.core.constants.VisiableEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BeanUtil;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-11 下午5:53.
 */
public class RepaymentLenderFeesCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");

    private long systemAccountId;
    private BigDecimal theLendBalance;
    private BigDecimal theLendFeesBalance;
    private Date today;

    private LendOrder lendOrder;
    private LoanPublish loanPublish;
    private LendLoanBindingService lendLoanBindingService;
    private RepaymentPlan repaymentPlan;
    private FeesItemService feesItemService;
    private LoanApplication loanApplication;
    private RightsRepaymentDetail rightsRepaymentDetail;
    private AccountValueChangedQueue avcq;

    private LendOrderDetailFeesService lendOrderDetailFeesService;
    private LendOrderReceiveService lendOrderReceiveService;
    private CapitalFlowService capitalFlowService;
    private Schedule schedule;
    private UserAccountService userAccountService;
    
    private Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap ;

    public RepaymentLenderFeesCommand(long systemAccountId, Date today, BigDecimal theLendBalance, BigDecimal theLendFeesBalance,
                                      LendOrder lendOrder, LoanPublish loanPublish, LendLoanBindingService lendLoanBindingService,
                                      RepaymentPlan repaymentPlan, FeesItemService feesItemService, LoanApplication loanApplication,
                                      RightsRepaymentDetail rightsRepaymentDetail, AccountValueChangedQueue avcq,
                                      LendOrderDetailFeesService lendOrderDetailFeesService,
                                      LendOrderReceiveService lendOrderReceiveService, Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap,
                                      CapitalFlowService capitalFlowService,Schedule schedule,UserAccountService userAccountService) {
        this.systemAccountId = systemAccountId;
        this.today = today;
        this.theLendBalance = theLendBalance;
        this.theLendFeesBalance = theLendFeesBalance;
        this.lendOrder = lendOrder;
        this.loanPublish = loanPublish;
        this.lendLoanBindingService = lendLoanBindingService;
        this.repaymentPlan = repaymentPlan;
        this.feesItemService = feesItemService;
        this.loanApplication = loanApplication;
        this.rightsRepaymentDetail = rightsRepaymentDetail;
        this.avcq = avcq;
        this.lendOrderDetailFeesService = lendOrderDetailFeesService;
        this.lendOrderReceiveService = lendOrderReceiveService;
        this.resultMap = resultMap;
        this.capitalFlowService = capitalFlowService;
        this.schedule = schedule;
        this.userAccountService = userAccountService;
    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****开始计算并支出出借合同回款时的费用****");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_REPAYMENTLENDERFEES);
        LendOrderReceive receiveDetail = lendOrderReceiveService.getByOrderAndSectionCode(lendOrder.getLendOrderId(),repaymentPlan.getSectionCode());
        if (receiveDetail == null) {
            logger.debug("查询不到出借合同的返息周期，默认计算第一个返息周期");
            receiveDetail = lendOrderReceiveService.getFirstByOrder(lendOrder.getLendOrderId());
        }
        List<LendOrderFeesDetail> lendOrderFeesDetails = lendOrderDetailFeesService.getDetailByLendOrderIdAndSectionCode(lendOrder.getLendOrderId(), receiveDetail.getSectionCode());
        //判断本期 之前是否计算过费用清单
        BigDecimal theLendBalanceCopy = theLendBalance;

        Date now = new Date();
        if (lendOrderFeesDetails == null || lendOrderFeesDetails.size() == 0) {
            List<LendLoanBinding> lendLoanBindings = lendLoanBindingService.findByLendAndLoan(lendOrder.getLendProductId(), loanApplication.getLoanProductId());
            for (LendLoanBinding lendLoanBinding : lendLoanBindings) {
                FeesItem feesItem = lendLoanBinding.getFeesItem();
                if (feesItem != null) {
                    LendOrderFeesDetail lendOrderFeesDetail = new LendOrderFeesDetail();
                    lendOrderFeesDetail.setLendOrderId(lendOrder.getLendOrderId());
                    lendOrderFeesDetail.setChargePoint(lendLoanBinding.getChargePoint().toCharArray()[0]);
                    lendOrderFeesDetail.setSectionCode(repaymentPlan.getSectionCode());
                    lendOrderFeesDetail.setFeesItemId(lendLoanBinding.getFeesItemId());
                    BigDecimal feesBalance = feesItemService.calculateFeesBalance(feesItem.getFeesItemId(), BigDecimal.ZERO, BigDecimal.ZERO,
                            rightsRepaymentDetail.getShouldCapital2(), rightsRepaymentDetail.getShouldInterest2(), BigDecimal.ZERO, BigDecimal.ZERO);
                    //计算费用大于0
                    if (BigDecimalUtil.compareTo(feesBalance, BigDecimal.ZERO, false, 2) > 0){

                        lendOrderFeesDetail.setFeesBalance(feesBalance);
                        lendOrderFeesDetail.setFeesBalance2(BigDecimalUtil.up(lendOrderFeesDetail.getFeesBalance(), 2));

                        if (BigDecimalUtil.compareTo(theLendBalanceCopy, lendOrderFeesDetail.getFeesBalance2(), true, 2) >= 0) {
                            lendOrderFeesDetail.setPaidFees(lendOrderFeesDetail.getFeesBalance2());
                            lendOrderFeesDetail.setFeesState(LendOrderFeesDetail.FEESSTATE_PAID);

                            lendOrderDetailFeesService.insert(lendOrderFeesDetail);

                        } else {
                            lendOrderFeesDetail.setPaidFees(theLendBalanceCopy);
                            lendOrderFeesDetail.setFeesState(LendOrderFeesDetail.FEESSTATE_UNPAY);
                            lendOrderDetailFeesService.insert(lendOrderFeesDetail);

                        }
                        theLendBalanceCopy = theLendBalanceCopy.subtract(lendOrderFeesDetail.getPaidFees());

                      //【20170203省心计划需求：记录还款费用 -- 此处增加金额】
                        if( resultMap.get(rightsRepaymentDetail.getLendAccountId()) == null ){
                     	   Map<Long,Map<String,BigDecimal>> valueMap = new HashMap<>();
                     	   Map<String,BigDecimal> feesMap = new HashMap<>();
                     	  feesMap.put(RightsRepaymentDetail.FEES, lendOrderFeesDetail.getPaidFees());
                     	   valueMap.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), feesMap);
                     	   resultMap.put(rightsRepaymentDetail.getLendAccountId(), valueMap);
                        }else{
                     	   Map<Long,Map<String,BigDecimal>> rrdMaps = resultMap.get(rightsRepaymentDetail.getLendAccountId());
                     	   if(rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId())== null ){
                     		  Map<String,BigDecimal> feesMap = new HashMap<>();
                     		  feesMap.put(RightsRepaymentDetail.FEES, lendOrderFeesDetail.getPaidFees());
                     		  rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), feesMap);
                     	   }else{
                     		  Map<String,BigDecimal> valueMap = rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId());
                     		  BigDecimal feesValue = valueMap.get(RightsRepaymentDetail.FEES);
                     		  if(feesValue == null){
                     			  feesValue = lendOrderFeesDetail.getPaidFees(); 
                     		  }else{
                     			  feesValue = feesValue.add(lendOrderFeesDetail.getPaidFees());
                     		  }
                     		  valueMap.put(RightsRepaymentDetail.FEES, feesValue);
                     		  rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), valueMap) ;
                     	   }
                        }

                        String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVE_FEES_PAY, loanPublish.getLoanTitle(), feesItem.getItemName());
                        AccountValueChanged avcPay = new AccountValueChanged(rightsRepaymentDetail.getLendAccountId(), lendOrderFeesDetail.getPaidFees(),
                                lendOrderFeesDetail.getPaidFees(), AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue(),
                                "LendOrderFeesDetail", VisiableEnum.DISPLAY.getValue(), lendOrderFeesDetail.getLendOrderFeesDetailId(),
                                AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                                lendOrder.getLendOrderId(), now, descPay, true);
                        avcq.addAccountValueChanged(avcPay);


                        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVE_FEES_INCOME, loanPublish.getLoanTitle(), lendOrder.getOrderCode(), feesItem.getItemName());
                        AccountValueChanged avcIncome = new AccountValueChanged(systemAccountId, lendOrderFeesDetail.getPaidFees(),
                                lendOrderFeesDetail.getPaidFees(), AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue(),
                                "LendOrderFeesDetail", VisiableEnum.DISPLAY.getValue(), lendOrderFeesDetail.getLendOrderFeesDetailId(),
                                AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                                systemAccountId, now, descIncome, true);
                        avcq.addAccountValueChanged(avcIncome);

                        UserAccount platformAccount = userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);
                        CapitalFlow cap=new CapitalFlow();
                        cap.setScheduleId(schedule.getScheduleId());
                        cap.setOperationType(Integer.parseInt(HFOperationEnum.ALLOCATION_PERSON_TO_COMPANY.getValue()));
                        cap.setFromUser(lendOrder.getLendUserId());
                        cap.setToUser(platformAccount.getUserId());
                        cap.setAmount(lendOrderFeesDetail.getPaidFees());
                        cap.setStartTime(new Date());
                        cap.setBusinessId(repaymentPlan.getRepaymentPlanId());
                        cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
                        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
                        capitalFlowService.addCapital(cap);

                        theLendFeesBalance = theLendFeesBalance.add(lendOrderFeesDetail.getPaidFees());
                    }

                }

            }

        } else {
            for (LendOrderFeesDetail orderFeesDetail : lendOrderFeesDetails) {
                if (orderFeesDetail.getFeesState() == LendOrderFeesDetail.FEESSTATE_UNPAY) {
                    FeesItem feesItem = feesItemService.findById(orderFeesDetail.getFeesItemId());
                    BigDecimal shouldFees = orderFeesDetail.getFeesBalance2().subtract(orderFeesDetail.getPaidFees());
                    BigDecimal theFactFees = BigDecimal.ZERO;

                    if (BigDecimalUtil.compareTo(theLendBalanceCopy, shouldFees, true, 2) >= 0) {
                        orderFeesDetail.setPaidFees(orderFeesDetail.getFeesBalance2());
                        orderFeesDetail.setFeesState(LendOrderFeesDetail.FEESSTATE_PAID);

                        theFactFees = shouldFees;
                        theLendBalanceCopy = theLendBalanceCopy.subtract(shouldFees);
                    } else {
                        orderFeesDetail.setPaidFees(orderFeesDetail.getPaidFees().add(theLendBalance));
                        theFactFees = theLendBalance;
                        theLendBalanceCopy = BigDecimal.ZERO;
                    }

                    lendOrderDetailFeesService.update(BeanUtil.bean2Map(orderFeesDetail));


                  //【20170203省心计划需求：记录还款费用 -- 此处增加金额】
                    if( resultMap.get(rightsRepaymentDetail.getLendAccountId()) == null ){
                 	   Map<Long,Map<String,BigDecimal>> valueMap = new HashMap<>();
                 	   Map<String,BigDecimal> feesMap = new HashMap<>();
                 	  feesMap.put(RightsRepaymentDetail.FEES, theFactFees);
                 	   valueMap.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), feesMap);
                 	   resultMap.put(rightsRepaymentDetail.getLendAccountId(), valueMap);
                    }else{
                 	   Map<Long,Map<String,BigDecimal>> rrdMaps = resultMap.get(rightsRepaymentDetail.getLendAccountId());
                 	   if(rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId())== null ){
                 		  Map<String,BigDecimal> feesMap = new HashMap<>();
                 		  feesMap.put(RightsRepaymentDetail.FEES, theFactFees);
                 		  rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), feesMap);
                 	   }else{
                 		  Map<String,BigDecimal> valueMap = rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId());
                 		  BigDecimal feesValue = valueMap.get(RightsRepaymentDetail.FEES);
                 		  if(feesValue == null){
                 			  feesValue = theFactFees; 
                 		  }else{
                 			  feesValue = feesValue.add(theFactFees);
                 		  }
                 		  valueMap.put(RightsRepaymentDetail.FEES, feesValue);
                 		  rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), valueMap) ;
                 	   }
                    }
                    
                    String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVE_FEES_PAY, loanPublish.getLoanTitle(), feesItem.getItemName());
                    AccountValueChanged avcPay = new AccountValueChanged(rightsRepaymentDetail.getLendAccountId(), theFactFees,
                            theFactFees, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue(),
                            "LendOrderFeesDetail", VisiableEnum.DISPLAY.getValue(), orderFeesDetail.getLendOrderFeesDetailId(),
                            AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                            lendOrder.getLendOrderId(), now, descPay, true);
                    avcq.addAccountValueChanged(avcPay);


                    String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.RECEIVE_FEES_INCOME, loanPublish.getLoanTitle(), lendOrder.getOrderCode(), feesItem.getItemName());
                    AccountValueChanged avcIncome = new AccountValueChanged(systemAccountId, theFactFees,
                            theFactFees, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_WORKFLOWFEES.getValue(),
                            "LendOrderFeesDetail", VisiableEnum.DISPLAY.getValue(), orderFeesDetail.getLendOrderFeesDetailId(),
                            AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                            systemAccountId, now, descIncome, true);
                    avcq.addAccountValueChanged(avcIncome);

                    theLendFeesBalance = theLendFeesBalance.add(theFactFees);
                }
            }
        }
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);
        taskExecLog.setLogInfo("计算并支出出借合同回款时的费用");
        logger.debug("****计算并支出出借合同回款时的费用****");
        return null;
    }
}
