package com.xt.cfp.core.event.command;


import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.constants.RateEnum.RateLendOrderTypeEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.InterestCalculation;
import com.xt.cfp.core.util.StringUtils;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-11 下午6:15.
 */
public class RepaymentCompleteLoanAppCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private LoanApplication loanApplication;
    private boolean isEarly = false;

    private AccountValueChangedQueue avcq;

    private LoanApplicationService loanApplicationService;
    private CreditorRightsService creditorRightsService;
    private RepaymentPlanService repaymentPlanService;
    private RightsRepaymentDetailService rightsRepaymentDetailService;

    private LendOrderService lendOrderService;
    private LoanPublishService loanPublishService;
    private AwardDetailService awardDetailService;
    private LoanProductService loanProductService;
    private UserInfoService userInfoService;
    private ConstantDefineCached constantDefineCached;
    private UserAccountService userAccountService;

    public RepaymentCompleteLoanAppCommand(LoanApplication loanApplication, boolean isEarly, AccountValueChangedQueue avcq,
                                           LoanApplicationService loanApplicationService, CreditorRightsService creditorRightsService,
                                           RepaymentPlanService repaymentPlanService, RightsRepaymentDetailService rightsRepaymentDetailService,
                                           LendOrderService lendOrderService, LoanPublishService loanPublishService,
                                           AwardDetailService awardDetailService, LoanProductService loanProductService,
                                           UserInfoService userInfoService, ConstantDefineCached constantDefineCached,
                                           UserAccountService userAccountService) {
        this.loanApplication = loanApplication;
        this.isEarly = isEarly;
        this.avcq = avcq;
        this.loanApplicationService = loanApplicationService;
        this.creditorRightsService = creditorRightsService;
        this.repaymentPlanService = repaymentPlanService;
        this.rightsRepaymentDetailService = rightsRepaymentDetailService;
        this.lendOrderService = lendOrderService;
        this.loanPublishService = loanPublishService;
        this.awardDetailService = awardDetailService;
        this.loanProductService = loanProductService;
        this.userInfoService = userInfoService;
        this.constantDefineCached = constantDefineCached;
        this.userAccountService = userAccountService;
    }

	@Override
    public Object execute() throws Exception {
        logger.debug("****开始修改借款申请状态及债权状态（如所有的还款计划都已还清，则改为已结清）****");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_REPAYMENTCOMPLETELOANAPP);

        Date now = new Date();
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

        LoanPublish loanPublish = loanPublishService.findById(loanApplication.getLoanApplicationId());
        LoanProduct loanProduct = loanProductService.findById(loanApplication.getLoanProductId());
        List<CreditorRights> rightses = creditorRightsService.getByLoanApplicationId(loanApplication.getLoanApplicationId());


        for (CreditorRights rights : rightses) {
            if (rights.getChannelType() == ChannelTypeEnum.ONLINE.value2Long()&&rights.getRightsState() == CreditorRightsConstants.CreditorRightsStateEnum.EFFECTIVE.value2Char()) {
                Map<String, Object> creditorRightsMap = new HashMap<String, Object>();
                if (allComplete) {
                    if (isEarly) {
                        loanApplication.setApplicationState(LoanApplicationStateEnum.EARLYCOMPLETE.getValue());
                    } else {
                        loanApplication.setApplicationState(LoanApplicationStateEnum.COMPLETED.getValue());
                    }

                    if (rights.getRightsState() != CreditorRightsConstants.CreditorRightsStateEnum.TURNOUT.value2Char()
                            && rights.getRightsState() != CreditorRightsConstants.CreditorRightsStateEnum.TURNOUT_SYSTEMPREPAY.value2Char()
                            ) {
                        if (isEarly) {
                            rights.setRightsState(CreditorRightsConstants.CreditorRightsStateEnum.EARLYCOMPLETE.value2Char());
                        } else {
                            rights.setRightsState(CreditorRightsConstants.CreditorRightsStateEnum.COMPLETE.value2Char());
                        }
                    }

                    LendOrder lendOrder = lendOrderService.findById(rights.getLendOrderId());
                    UserInfo lendUserInfo = userInfoService.getUserByUserId(lendOrder.getLendUserId());
//                    UserAccount cashUserAccount = userAccountService.getCashAccount(lendUserInfo.getUserId());
//                    if (loanPublish.getAwardPoint() != null && loanPublish.getAwardPoint().equals(AwardPointEnum.ATCOMPLETE.getValue())) {
//                        BigDecimal awardBalance = BigDecimal.valueOf(0);
//                        awardBalance = InterestCalculation.getAllInterest(lendOrder.getBuyBalance(), loanPublish.getAwardRate(), loanProduct.getDueTimeType(), loanProduct.getRepaymentMethod(), loanProduct.getRepaymentType(), loanProduct.getRepaymentCycle(), loanProduct.getDueTime(), loanProduct.getCycleValue());
//                        AwardDetail awardDetail = awardDetailService.insertAwardDetail(now, null, awardBalance, lendOrder, loanApplication.getLoanApplicationId(), AwardPointEnum.ATCOMPLETE,RateLendOrderTypeEnum.AWARD);
//
//                        //todo 平台支付奖励金额
//                        String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_PAY, lendUserInfo.getLoginName(), loanPublish.getLoanTitle());
//                        Long systemAccountId = constantDefineCached.getSystemAccount().get(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue());
//                        AccountValueChanged avcPay = new AccountValueChanged(systemAccountId, awardBalance,
//                                awardBalance, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAY_AWARD.getValue(),
//                                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
//                                AccountConstants.OwnerTypeEnum.SYS_ACC.getValue(),
//                                systemAccountId, now, descPay, false);
//                        avcq.addAccountValueChanged(avcPay);
//                        //todo 出借人收入奖励
//                        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.AWARD_INCOME, loanPublish.getLoanTitle(), AwardPointEnum.ATCOMPLETE.getDesc());
//                        AccountValueChanged avcIncome = new AccountValueChanged(cashUserAccount.getAccId(), awardBalance,
//                                awardBalance, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_AWARD.getValue(),
//                                "AwardDetail", VisiableEnum.DISPLAY.getValue(), awardDetail.getAwardDetailId(),
//                                AccountConstants.AccountChangedTypeEnum.CASH_ACCOUNT.getValue(),
//                                cashUserAccount.getAccId(), now, descIncome, true);
//                        avcq.addAccountValueChanged(avcIncome);
//
//                    }
                    LendOrder lenderPOrder = null ;
                    if(lendOrder.getLendOrderPId() != null){
                    	lenderPOrder = lendOrderService.findById(lendOrder.getLendOrderPId());
                    }
                    //结清短信的发送：不发送理财订单和理财订单的子订单的结清短信
                    if(!lendOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue())
                    		&& (lenderPOrder == null || !lenderPOrder.getProductType().equals(LendProductTypeEnum.FINANCING.getValue()))){
                    	//todo 最后一期还款短信消息
                    	if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue())){
                    		UserInfo user = userInfoService.getUserByUserId(lendOrder.getLendUserId());
                    		repaymentPlanService.sendRepaymentEndMsg(loanPublish.getLoanTitle(),user.getMobileNo());
                    	}
                    }

                    //修改债权类出借订单的结清状态
                    if (lendOrder.getProductType().equals(LendProductTypeEnum.RIGHTING.getValue()) ||lendOrder.getProductType().equals(LendProductTypeEnum.CREDITOR_RIGHTS.getValue())) {
                        lendOrder.setOrderState(LendOrderConstants.LoanOrderStatusEnum.CLEAR.getValue());

                        Map<String,Object> lendOrderMap = new HashMap<String,Object>();
                        lendOrderMap.put("lendOrderId",lendOrder.getLendOrderId());
                        lendOrderMap.put("orderState",lendOrder.getOrderState());

                        lendOrderService.update(lendOrderMap);
                    }
                }
                creditorRightsMap.put("creditorRightsId", rights.getCreditorRightsId());
                creditorRightsMap.put("rightsState", rights.getRightsState());
                creditorRightsService.update(creditorRightsMap);
            }
        }
        Map<String, Object> loanAppMap = new HashMap<String, Object>();
        loanAppMap.put("loanApplicationId", loanApplication.getLoanApplicationId());
        loanAppMap.put("applicationState", loanApplication.getApplicationState());
        loanApplicationService.update(loanAppMap);
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);


        taskExecLog.setLogInfo("修改借款申请" + loanApplication.getLoanApplicationCode() + "  状态及债权状态（如所有的还款计划都已还清，则改为已结清）完成");
        logger.debug("****修改借款申请状态及债权状态（如所有的还款计划都已还清，则改为已结清）完成****");
        return null;
    }
}
