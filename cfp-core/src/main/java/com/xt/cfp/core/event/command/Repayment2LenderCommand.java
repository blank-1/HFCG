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
import com.xt.cfp.core.service.message.WechatMessageBody;
import com.xt.cfp.core.util.*;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-11 下午5:36.
 */
public class Repayment2LenderCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");

    private boolean theLastRepayment;
    private BigDecimal repayDetailBalance;
    private CreditorRights creditorRights;
    private int lendRightIndex;
    private List<RightsRepaymentDetail> rightsRepaymentDetails;
    private BigDecimal balanceAfterFees;
    private BigDecimal currentShouldCalital;
    private BigDecimal theLastLendCalital;
    private BigDecimal theLendInterest;
    private BigDecimal sumDefaultInterest;
    private long adminId;
    private Date repaymentDate;
    private int delayDays;

    private LoanApplication loanApplication;
    private LoanPublish loanPublish;
    private LendOrder lendOrder;
    private RepaymentRecord repaymentRecord;
    private RightsRepaymentDetail rightsRepaymentDetail;
    private AccountValueChangedQueue avcq;

    private CreditorRightsService creditorRightsService;
    private RightsRepaymentDetailService rightsRepaymentDetailService;
    private LendOrderService lendOrderService;
    private LendOrderReceiveService lendOrderReceiveService;
    private UserAccountService userAccountService;
    private RepaymentRecordService repaymentRecordService;
    private UserInfoService userInfoService;
    private UserOpenIdService userOpenIdService;
    private ConstantDefineService constantDefineService;
    private boolean isAhead ;
    private long theRepaymentPlanId ;
    private Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap ;
    private CapitalFlowService capitalFlowService;
    private Schedule schedule;


    public Repayment2LenderCommand(boolean theLastRepayment, BigDecimal repayDetailBalance, CreditorRights creditorRights,
                                   int lendRightIndex, List<RightsRepaymentDetail> rightsRepaymentDetails,
                                   BigDecimal balanceAfterFees, BigDecimal currentShouldCalital, BigDecimal theLastLendCalital,
                                   BigDecimal theLendInterest, BigDecimal sumDefaultInterest, long adminId, Date repaymentDate,
                                   int delayDays, LoanApplication loanApplication,LoanPublish loanPublish, LendOrder lendOrder, RepaymentRecord repaymentRecord,
                                   RightsRepaymentDetail rightsRepaymentDetail, AccountValueChangedQueue avcq, CreditorRightsService creditorRightsService,
                                   RightsRepaymentDetailService rightsRepaymentDetailService, LendOrderService lendOrderService,
                                   LendOrderReceiveService lendOrderReceiveService, UserAccountService userAccountService,RepaymentRecordService repaymentRecordService,
                                   UserInfoService userInfoService, UserOpenIdService userOpenIdService, ConstantDefineService constantDefineService,boolean isAhead,long theRepaymentPlanId,
                                   Map<Long,Map<Long,Map<String,BigDecimal>>> resultMap,CapitalFlowService capitalFlowService,Schedule schedule) throws Exception {
        this.theLastRepayment = theLastRepayment;
        this.repayDetailBalance = repayDetailBalance;//平台垫付的利息
        this.creditorRights = creditorRights;
        this.lendRightIndex = lendRightIndex;
        this.rightsRepaymentDetails = rightsRepaymentDetails;
        this.balanceAfterFees = balanceAfterFees;
        this.currentShouldCalital = currentShouldCalital;
        this.theLastLendCalital = theLastLendCalital;
        this.theLendInterest = theLendInterest;
        this.sumDefaultInterest = sumDefaultInterest;
        this.adminId = adminId;
        this.repaymentDate = repaymentDate;
        this.delayDays = delayDays;
        this.loanApplication = loanApplication;
        this.loanPublish = loanPublish;
        this.lendOrder = lendOrder;
        this.repaymentRecord = repaymentRecord;
        this.rightsRepaymentDetail = rightsRepaymentDetail;
        this.avcq = avcq;
        this.creditorRightsService = creditorRightsService;
        this.rightsRepaymentDetailService = rightsRepaymentDetailService;
        this.lendOrderService = lendOrderService;
        this.lendOrderReceiveService = lendOrderReceiveService;
        this.userAccountService = userAccountService;
        this.repaymentRecordService = repaymentRecordService;
        this.userInfoService = userInfoService;
        this.userOpenIdService = userOpenIdService;
        this.constantDefineService = constantDefineService;
        this.isAhead = isAhead;
        this.theRepaymentPlanId=theRepaymentPlanId;
        this.resultMap = resultMap;
        this.capitalFlowService = capitalFlowService;
        this.schedule = schedule;

        BigDecimal theLendBalance;
        if (theLastRepayment) {

            this.theLendInterest = rightsRepaymentDetail.getShouldInterest2().subtract(rightsRepaymentDetail.getFactInterest());

        } else {
//            if (lendRightIndex == rightsRepaymentDetails.size() - 1) {
                theLendBalance = balanceAfterFees;
                if (BigDecimalUtil.compareTo(theLendBalance, rightsRepaymentDetail.getShouldInterest2().subtract(rightsRepaymentDetail.getFactInterest()), true, 2) >= 0) {
//                if (theLendBalance.compareTo(rightsRepaymentDetail.getShouldInterest2()) >= 0) {
                    this.theLendInterest = rightsRepaymentDetail.getShouldInterest2();
                } else {
                    this.theLendInterest = theLendBalance;
                }

//            } else {
//                theLendBalance = balanceAfterFees;
//                if (BigDecimalUtil.compareTo(theLendBalance, rightsRepaymentDetail.getShouldInterest2().subtract(rightsRepaymentDetail.getFactInterest()), true, 2) >= 0) {
////                if (theLendBalance.compareTo(rightsRepaymentDetail.getShouldInterest2()) >= 0) {
//                    this.theLendInterest = rightsRepaymentDetail.getShouldInterest2();
//                } else {
//                    this.theLendInterest = theLendBalance;
//                }
//
//            }

        }

    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****开始针对各个理财订单进行回款（含回款、债权价值减少及返息）****");
        Date now = new Date();

        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(now);
        taskExecLog.setTaskId(TaskInfo.TASKID_PAYREPAYMENTINTEREST);

        BigDecimal theLendBalance;
        BigDecimal theLendCalital;
        BigDecimal theLendInterest18;
        Map<String,Object> creditorRightsMap = new HashMap<String,Object>();
        if (theLastRepayment) {
            //本期还的本金与利息，回款至理财订单的待理财金额

            theLendBalance = rightsRepaymentDetail.getShouldBalance2().subtract(rightsRepaymentDetail.getFactBalance());
            lendOrder.setForLendBalance(lendOrder.getForLendBalance().add(theLendBalance));

            theLendCalital = rightsRepaymentDetail.getShouldCapital2().subtract(rightsRepaymentDetail.getFactCalital());
            theLendInterest = rightsRepaymentDetail.getShouldInterest2().subtract(rightsRepaymentDetail.getFactInterest());
            theLendInterest18 = rightsRepaymentDetail.getShouldInterest().subtract(rightsRepaymentDetail.getFactInterest());

            //将债权价值相应减少
            creditorRights.setRightsWorth(creditorRights.getRightsWorth().subtract(theLendCalital));
            creditorRightsMap.put("rightsWorth", creditorRights.getRightsWorth());


            rightsRepaymentDetail.setFactBalance(rightsRepaymentDetail.getShouldBalance2());
            rightsRepaymentDetail.setFactCalital(rightsRepaymentDetail.getShouldCapital2());
            rightsRepaymentDetail.setFactInterest(rightsRepaymentDetail.getShouldInterest2());
            rightsRepaymentDetail.setIsPayOff(RightsRepaymentDetail.ISPAYOFF_YES);
            rightsRepaymentDetail.setRightsDetailState(RightsRepaymentDetail.RIGHTSDETAILSTATE_COMPLETE);
            creditorRights.setCompleteTime(now);
            creditorRightsMap.put("completeTime", now);


        } else {
            if (lendRightIndex == rightsRepaymentDetails.size() - 1) {
                //如果是本次债权列表循环的最后一次（即当前列表的最后一个出借人），则将本次还款额减去已还其它出借人的金额做为本出借人的还款额，目的是为了减少计算误差
//                theLendBalance = balanceAfterFees.multiply(BigDecimal.ONE.subtract(rightsRatioSum));
                theLendBalance = balanceAfterFees;
                lendOrder.setForLendBalance(lendOrder.getForLendBalance().add(theLendBalance));

                if (BigDecimalUtil.compareTo(theLendBalance, rightsRepaymentDetail.getShouldInterest2().subtract(rightsRepaymentDetail.getFactInterest()), true, 2) >= 0) {
//                if (theLendBalance.compareTo(rightsRepaymentDetail.getShouldInterest2()) >= 0) {
                    theLendInterest = rightsRepaymentDetail.getShouldInterest2();
                    theLendInterest18 = rightsRepaymentDetail.getShouldInterest();

                } else {
                    theLendInterest = theLendBalance;
                    theLendInterest18 = theLendBalance;
                }

                if (BigDecimalUtil.compareTo(theLendBalance, rightsRepaymentDetail.getShouldCapital2().subtract(rightsRepaymentDetail.getFactCalital()), true, 2) >= 0) {
                    theLendCalital = rightsRepaymentDetail.getShouldCapital2();
                } else if (theLendBalance.subtract(theLendInterest).compareTo(BigDecimal.ZERO) > 0) {
                    theLendCalital = theLendBalance.subtract(theLendInterest);
                } else {
                    theLendCalital = BigDecimal.ZERO;
                }


                rightsRepaymentDetail.setFactBalance(rightsRepaymentDetail.getFactBalance().add(theLendBalance));
                rightsRepaymentDetail.setFactCalital(rightsRepaymentDetail.getFactCalital().add(theLendCalital));
                rightsRepaymentDetail.setFactInterest(rightsRepaymentDetail.getFactInterest().add(theLendInterest));
                rightsRepaymentDetail.setIsPayOff(RightsRepaymentDetail.ISPAYOFF_NO);

                //todoed 将债权价值相应减少
                creditorRights.setRightsWorth(creditorRights.getRightsWorth().subtract(theLendCalital));
                creditorRightsMap.put("rightsWorth", creditorRights.getRightsWorth());

            } else {
//                BigDecimal theRightsRatio = creditorRights.getRightsWorth().divide(loanApplication.getLoanBalance(), 18, BigDecimal.ROUND_CEILING);
                theLendBalance = balanceAfterFees;
                //本期还的本金与利息，回款至理财订单的待理财金额
                lendOrder.setForLendBalance(lendOrder.getForLendBalance().add(theLendBalance));
                //计算当前出借人的应还本金和应还利息


                if (theLendBalance.compareTo(rightsRepaymentDetail.getShouldInterest2()) >= 0) {
                    theLendInterest = rightsRepaymentDetail.getShouldInterest2();
                    theLendInterest18 = rightsRepaymentDetail.getShouldInterest();

                } else {
                    theLendInterest = theLendBalance;
                    theLendInterest18 = theLendBalance;
                }

                if (theLendBalance.subtract(theLendInterest).compareTo(rightsRepaymentDetail.getShouldCapital2()) >= 0) {
                    theLendCalital = rightsRepaymentDetail.getShouldCapital2();
                } else if (theLendBalance.subtract(theLendInterest).compareTo(BigDecimal.ZERO) > 0) {
                    theLendCalital = theLendBalance.subtract(theLendInterest);
                } else {
                    theLendCalital = BigDecimal.ZERO;
                }

                rightsRepaymentDetail.setFactBalance(rightsRepaymentDetail.getFactBalance().add(theLendBalance));
                rightsRepaymentDetail.setFactCalital(rightsRepaymentDetail.getFactCalital().add(theLendCalital));
                rightsRepaymentDetail.setFactInterest(rightsRepaymentDetail.getFactInterest().add(theLendInterest));
                rightsRepaymentDetail.setIsPayOff(RightsRepaymentDetail.ISPAYOFF_NO);
                //todoed 待测试 将债权价值相应减少
                creditorRights.setRightsWorth(creditorRights.getRightsWorth().subtract(theLendCalital));
                creditorRightsMap.put("rightsWorth", creditorRights.getRightsWorth());

                theLastLendCalital = currentShouldCalital.subtract(theLendCalital);
            }


        }

        BigDecimal rightsWorth = creditorRights.getRightsWorth();
        creditorRights.setLendPrice(rightsWorth.setScale(2, BigDecimal.ROUND_UP));
        creditorRightsMap.put("lendPrice", creditorRights.getLendPrice());
        creditorRightsMap.put("creditorRightsId", creditorRights.getCreditorRightsId());
        //保存债权更改后的信息(债权价值)
        creditorRightsService.update(creditorRightsMap);

        if (delayDays > 0) {
            rightsRepaymentDetail.setIsDelay(RightsRepaymentDetail.ISDELAY_YES);
            rightsRepaymentDetail.setDepalFine(sumDefaultInterest);
            rightsRepaymentDetail.setDelayDays(delayDays);
        } else {
            rightsRepaymentDetail.setIsDelay(RightsRepaymentDetail.ISDELAY_NO);
        }
        //把还款记录保存到债权分配明细中
        rightsRepaymentDetail.setRepaymentRecordId(repaymentRecord.getRepaymentRecordId());
        Map<String, Object> rightsRepaymentMap = BeanUtil.bean2Map(rightsRepaymentDetail);
        rightsRepaymentDetailService.update(rightsRepaymentMap);
        //修改省心计划订单的待理财金额
        Map<String,Object> lendOrderMap = new HashMap<String,Object>();
        lendOrderMap.put("forLendBalance",lendOrder.getForLendBalance());
        lendOrderMap.put("lendOrderId",lendOrder.getLendOrderId());
        lendOrderService.update(lendOrderMap);
        //修改还款记录的分配状态
        repaymentRecord.setDistributeStatus(DistributeStatusEnum.DISTRIBUTED.value2Char());
        repaymentRecordService.update(repaymentRecord);

        //保存还款分配明细
        RepaymentRecordDetail repaymentRecordDetail = new RepaymentRecordDetail();
        repaymentRecordDetail.setLendOrderId(lendOrder.getLendOrderId());
        repaymentRecordDetail.setRepaymentRecordId(repaymentRecord.getRepaymentRecordId());
        repaymentRecordDetail.setLendUserId(lendOrder.getLendUserId());
        repaymentRecordDetail.setRightsRepaymentDetailId(rightsRepaymentDetail.getRightsRepaymentDetailId());
        repaymentRecordDetail.setSectionCode(rightsRepaymentDetail.getSectionCode());
        repaymentRecordDetail.setFactReceiveCapital(rightsRepaymentDetail.getFactCalital());
        repaymentRecordDetail.setFactReceiveInterest(rightsRepaymentDetail.getFactInterest());
        repaymentRecordDetail.setFactReceivePenalty(rightsRepaymentDetail.getDepalFine());
        repaymentRecordDetail.setFactReceiveBalance(rightsRepaymentDetail.getFactBalance());
        repaymentRecordService.addRecordDetail(repaymentRecordDetail);

        BigDecimal interest = theLendInterest;
        //出借账户收入还款-利息
        if (BigDecimalUtil.compareTo(interest, BigDecimal.ZERO, true, 2) > 0) {
            String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.INTEREST_IN, loanPublish.getLoanTitle());
            AccountValueChanged avcIncome = new AccountValueChanged(rightsRepaymentDetail.getLendAccountId(), interest,
                    interest, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEDEFAULTINTEREST.getValue(),
                    "RightsRepaymentDetail", VisiableEnum.DISPLAY.getValue(), rightsRepaymentDetail.getRightsRepaymentDetailId(),
                    AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                    lendOrder.getLendOrderId(), now, descIncome, true);
            avcq.addAccountValueChanged(avcIncome);

            CapitalFlow cap=new CapitalFlow();
            cap.setScheduleId(schedule.getScheduleId());
            cap.setOperationType(Integer.parseInt(HFOperationEnum.ALLOCATION_COMPANY_TO_PERSON.getValue()));
            cap.setFromUser(loanApplication.getUserId());
            cap.setToUser(lendOrder.getLendUserId());
            cap.setAmount(interest);
            cap.setStartTime(new Date());
            cap.setBusinessId(repaymentRecordDetail.getLendOrderId());
            cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
            cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
            capitalFlowService.addCapital(cap);

            //【20170203省心计划需求：记录还款利息 -- 此处增加金额】
            if( resultMap.get(rightsRepaymentDetail.getLendAccountId()) == null ){
         	   Map<Long,Map<String,BigDecimal>> valueMap = new HashMap<>();
         	   Map<String,BigDecimal> interestMap = new HashMap<>();
         	   interestMap.put(RightsRepaymentDetail.INTEREST, interest);
         	   valueMap.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), interestMap);
         	   resultMap.put(rightsRepaymentDetail.getLendAccountId(), valueMap);
            }else{
         	   Map<Long,Map<String,BigDecimal>> rrdMaps = resultMap.get(rightsRepaymentDetail.getLendAccountId());
         	   if(rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId())== null ){
         		  Map<String,BigDecimal> interestMap = new HashMap<>();
         		  interestMap.put(RightsRepaymentDetail.INTEREST, interest);
         		  rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), interestMap);
         	   }else{
         		  Map<String,BigDecimal> valueMap = rrdMaps.get(rightsRepaymentDetail.getRightsRepaymentDetailId());
         		  valueMap.put(RightsRepaymentDetail.INTEREST, interest);
         		  rrdMaps.put(rightsRepaymentDetail.getRightsRepaymentDetailId(), valueMap) ;
         	   }
            }

        }
        //出借账户收入还款-本金
        if (BigDecimalUtil.compareTo(theLendCalital, BigDecimal.ZERO, true, 2) > 0) {
            //计算财富券 todo

            String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.REPAYMENT_IN, loanPublish.getLoanTitle());
            AccountValueChanged avcIncome = new AccountValueChanged(rightsRepaymentDetail.getLendAccountId(), theLendCalital,
                    BigDecimalUtil.down(theLendCalital, 2), AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEREPAYMENT.getValue(),
                    "RightsRepaymentDetail", VisiableEnum.DISPLAY.getValue(), rightsRepaymentDetail.getRightsRepaymentDetailId(),
                    AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                    lendOrder.getLendOrderId(), now, descIncome, true);
            avcq.addAccountValueChanged(avcIncome);

            CapitalFlow cap=new CapitalFlow();
            cap.setScheduleId(schedule.getScheduleId());
            cap.setOperationType(Integer.parseInt(HFOperationEnum.ALLOCATION_PERSON_TO_PERSON.getValue()));
            cap.setFromUser(loanApplication.getUserId());
            cap.setToUser(lendOrder.getLendUserId());
            cap.setAmount(theLendCalital);
            cap.setStartTime(new Date());
            cap.setBusinessId(repaymentRecordDetail.getLendOrderId());
            cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
            cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
            capitalFlowService.addCapital(cap);
        }

        //todo 判断是否需要冻结返息
        // 更新回款计划的实还金额
        boolean requiredToInterest = false;
        LendOrderReceive orderReceive = null;
        if (lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) {
            orderReceive = lendOrderReceiveService.getByOrderAndDate(lendOrder.getLendOrderId(), DateUtil.getDateLong(repaymentDate));
        } else {
            orderReceive = lendOrderReceiveService.getByOrderAndSectionCode(lendOrder.getLendOrderId(),rightsRepaymentDetail.getSectionCode());
        }
        if (orderReceive == null) {
            requiredToInterest = true;
            orderReceive = lendOrderReceiveService.getFirstByOrder(lendOrder.getLendOrderId());
        } else {
            UserAccount lendAccount = userAccountService.getUserAccountByAccId(lendOrder.getCustomerAccountId());
            if (BigDecimalUtil.compareTo(lendAccount.getFrozeValue2(), orderReceive.getShouldInterest2(), 2) >= 0) {
                requiredToInterest = false;
            }
        }

        //如果订单的返息周期为退出时返息，或者订单类型为债权类订单，则返息不冻结
        requiredToInterest = !((lendOrder.getToInterestPoint().equals(LendProduct.TOINTERESTPOINT_BEQUIT)) || (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())) || (lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())));
        orderReceive.setFactCapital(orderReceive.getFactCapital().add(BigDecimalUtil.down(theLendCalital, 2)));
        orderReceive.setFactInterest(orderReceive.getFactInterest().add(BigDecimalUtil.down(theLendInterest, 2)));

        Map<String, Object> orderReceiveMap = BeanUtil.bean2Map(orderReceive);
        lendOrderReceiveService.update(orderReceiveMap);
        if (requiredToInterest && BigDecimalUtil.compareTo(theLendInterest18, BigDecimal.ZERO, true, 2) > 0) {
            lendOrder.setForLendBalance(lendOrder.getForLendBalance().subtract(theLendInterest));
            lendOrderMap.put("forLendBalance", lendOrder.getForLendBalance());
            lendOrderService.update(lendOrderMap);


            String descFreeze = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.INCOMEDEFAULTINTEREST_FROZEN, loanPublish.getLoanTitle());
            AccountValueChanged avcFreeze = new AccountValueChanged(rightsRepaymentDetail.getLendAccountId(), interest,
                    interest, AccountConstants.AccountOperateEnum.FREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEDEFAULTINTEREST_FROZEN.getValue(),
                    "RightsRepaymentDetail", VisiableEnum.DISPLAY.getValue(), rightsRepaymentDetail.getRightsRepaymentDetailId(),
                    AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                    lendOrder.getLendOrderId(), now, descFreeze, true);
            avcq.addAccountValueChanged(avcFreeze);

        }

        //todo smsmessage
        //Amount
        BigDecimal balanceAmount = balanceAfterFees ;
        BigDecimal capitalAmount = BigDecimal.ZERO ;
        if(isAhead && rightsRepaymentDetail.getRepaymentPlanId() == theRepaymentPlanId){
        	List<RightsRepaymentDetail> rightsDetailList = rightsRepaymentDetailService.getDetailListByRightsId(rightsRepaymentDetail.getCreditorRightsId());
        	for(RightsRepaymentDetail detail : rightsDetailList){
        		if(detail.getRepaymentDayPlanned().after(rightsRepaymentDetail.getRepaymentDayPlanned())){
        			capitalAmount = capitalAmount.add(detail.getShouldCapital());
        		}
        	}
        }else{
        	capitalAmount = orderReceive.getShouldCapital2();
        }
        if(isAhead){
        	balanceAmount = BigDecimalUtil.down(balanceAmount.add(capitalAmount),2);
        }
        capitalAmount = BigDecimalUtil.down(capitalAmount, 2);
        LendOrder lenderPOrder = null ;
        if(lendOrder.getLendOrderPId() != null){
        	lenderPOrder = lendOrderService.findById(lendOrder.getLendOrderPId());
        }
        //非理财订单  、 非理财子订单 会发周期还款短信
		if (!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
				&& (lenderPOrder == null || !lenderPOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())) ) {
            UserInfo user = userInfoService.getUserByUserId(lendOrder.getLendUserId());
            lendOrderReceiveService.sendToLenderRepaymentSuccessMsg(orderReceive.getSectionCode(),loanApplication,new Date(),balanceAmount,user.getMobileNo());
          //发送微信消息
            try {	
                UserOpenId userOpenId = userOpenIdService.getOpenIdByCondition(user.getUserId(), null, null);
                
                if(userOpenId != null && !"".equals(userOpenId.getOpenId())){
                	List<ConstantDefine> tokenSurvivalTimeList = constantDefineService.getConstantDefinesByType("tokenSurvivalTime");
                    List<ConstantDefine> accessTokenList = constantDefineService.getConstantDefinesByType("accessToken");
                    Date date = new Date();
                    if("0".equals(tokenSurvivalTimeList.get(0).getConstantValue().toString()) || (date.getTime()/1000) > Long.valueOf(tokenSurvivalTimeList.get(0).getConstantValue().toString())){
                    	//to do重新获取
			        	String access_token = Sign.getAccessToken();
			        	Date resultTime = new Date();
			        	tokenSurvivalTimeList.get(0).setConstantValue(String.valueOf(resultTime.getTime()/1000+3600));
			        	accessTokenList.get(0).setConstantValue(access_token);
			        	constantDefineService.updateConstantDefine(tokenSurvivalTimeList.get(0));
 			        	constantDefineService.updateConstantDefine(accessTokenList.get(0));
                    }
                	WechatMessageBody wechatMessageBody =  new WechatMessageBody();
                	wechatMessageBody.setShouldCapital2(capitalAmount);
                	wechatMessageBody.setShouldInterest2(orderReceive.getShouldInterest2());
                	wechatMessageBody.setOpenId(userOpenId.getOpenId());
                	wechatMessageBody.setFlag("0");
                	wechatMessageBody.setBalance(balanceAmount);
                	wechatMessageBody.setSectionCode(orderReceive.getSectionCode());
                	wechatMessageBody.setAccess_token(accessTokenList.get(0).getConstantValue());
                	
                	//如果是提前回款，调用回款专用模板。
                	if(isAhead){
                		wechatMessageBody.setFlag("6");
                	}
                	
                	Sign.sendWechatMsg(wechatMessageBody);
                }
			} catch (Exception e) {
				e.printStackTrace();
                logger.error("回款发送微信消息失败.错误信息:" + e.getMessage());
			}
        }

        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);

        taskExecLog.setLogInfo("理财订单:" + lendOrder.getOrderCode() + "  回款完成（含回款、债权价值减少及返息）");
        logger.debug("****各个理财订单进行回款（含回款、债权价值减少及返息）完毕****");
        return null;
    }

    public BigDecimal getTheLendInterest() {
        return theLendInterest;
    }
}
