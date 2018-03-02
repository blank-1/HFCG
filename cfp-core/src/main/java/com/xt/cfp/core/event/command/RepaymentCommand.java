package com.xt.cfp.core.event.command;


import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.CapitalFlowService;
import com.xt.cfp.core.service.RepaymentPlanService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Renyulin on 14-6-11 下午5:21.
 */
public class RepaymentCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private RepaymentPlan repaymentPlan;
    private BigDecimal balance;
    private BigDecimal currentShouldCalital;
    private BigDecimal repaymentBalance;
    private boolean repayInterest;
    private LoanApplication loanApplication;
    private LoanPublish loanPublish;
    private boolean theLastRepayment;
    private long loanAccountId;
    private RepaymentRecord repaymentRecord;
    private AccountValueChangedQueue avcq;

    private RepaymentPlanService repaymentPlanService;
    private CapitalFlowService capitalFlowService;
    private Schedule schedule;

    public RepaymentCommand(LoanApplication loanApplication, LoanPublish loanPublish, RepaymentPlan repaymentPlan,BigDecimal balance, BigDecimal currentShouldCalital,
                            boolean theLastRepayment,long loanAccountId, boolean repayInterest,RepaymentRecord repaymentRecord,AccountValueChangedQueue avcq,
                            RepaymentPlanService repaymentPlanService,CapitalFlowService capitalFlowService,Schedule schedule) throws Exception {
        this.loanApplication = loanApplication;
        this.loanPublish = loanPublish;
        this.repaymentPlan = repaymentPlan;
        this.balance = balance;
        this.currentShouldCalital= currentShouldCalital;
        this.repaymentBalance = balance;
        this.theLastRepayment = theLastRepayment;
        this.loanAccountId = loanAccountId;
        this.repayInterest = repayInterest;
        this.repaymentRecord = repaymentRecord;
        this.avcq = avcq;
        this.repaymentPlanService = repaymentPlanService;
        this.capitalFlowService = capitalFlowService;
        this.schedule = schedule;


        //本次还款的应还本金
        //本次还款的应还总金额
        BigDecimal currentShouldBalance = repaymentPlan.getShouldBalance2().subtract(repaymentPlan.getFactBalance());
        //本次还款金额大于等于应还金额（即本次可完成本期还款）
        BigDecimal currentShouldInterest = repaymentPlan.getShouldInterest2().subtract(repaymentPlan.getFactInterest());

        if (repayInterest) {
            //优先还利息
            if (BigDecimalUtil.compareTo(balance, currentShouldInterest, true, 2) >= 0) {

                if (BigDecimalUtil.compareTo(balance, currentShouldBalance, true, 2) >= 0) {
                    this.theLastRepayment = true;
                    this.balance = balance.subtract(currentShouldBalance);

                } else {
                    this.theLastRepayment = false;
                    this.balance = BigDecimal.ZERO;

                }
            } else {
                this.theLastRepayment = false;
                this.balance = BigDecimal.ZERO;
            }
        } else {
            this.theLastRepayment = true;
            if (BigDecimalUtil.compareTo(balance, currentShouldBalance, true, 2) >= 0) {
                this.balance = balance.subtract(currentShouldBalance);

            } else {
                this.balance = BigDecimal.ZERO;

            }
        }
    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****借款合同开始还款（变更还款计划、还款记录）****");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_PAYREPAYMENTINTEREST);
        //本次还款的应还本金
        //本次还款的应还总金额
        BigDecimal currentShouldBalance = repaymentPlan.getShouldBalance2().subtract(repaymentPlan.getFactBalance());
        //本次还款金额大于等于应还金额（即本次可完成本期还款）
        BigDecimal currentShouldInterest = repaymentPlan.getShouldInterest2().subtract(repaymentPlan.getFactInterest());
        //判断是否需要还利息
        if (repayInterest) {
            //优先还利息
            if (BigDecimalUtil.compareTo(repaymentBalance, currentShouldInterest, true, 2) >= 0) {

                if (BigDecimalUtil.compareTo(repaymentBalance, currentShouldBalance, true, 2) >= 0) {
                    //还全部利息+全部本金
                    repayAllInterestAndCapital(currentShouldBalance, currentShouldInterest);

                } else {
                    //还全部利息+部分本金
                    repayAllInterestAndHalfCapital(currentShouldInterest);

                }
            } else {
                //还部分利息
                repayHalfInterest();

            }
        } else {
            //还全部本金，不含利息
            repayAllCapital(repaymentPlan.getShouldCapital2());
        }
        //todo 还款短信(给借款人)
        //repaymentPlanService.sendRepaymentSuccessMsg(balance,loanApplication,repaymentRecord,repaymentPlan);

        repaymentPlanService.update(repaymentPlan);
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);

        taskExecLog.setLogInfo("借款合同:" + loanApplication.getLoanApplicationCode() + "  还款（变更还款计划、还款记录）完成");
        logger.debug("****借款合同还款（变更还款计划、还款记录）完成****");
        return null;
    }

    /**
     * 还款(当期部分利息)
     *
     * @throws Exception
     */
    private void repayHalfInterest() throws Exception {
        Date now = new Date();
        repaymentPlan.setFactInterest(repaymentPlan.getFactInterest().add(repaymentBalance));
        repaymentPlan.setFactBalance(repaymentPlan.getFactBalance().add(repaymentBalance));


        repaymentRecord.setFactInterest(repaymentBalance);
        if(repaymentPlan.getPlanState() != RepaymentPlanStateEnum.DEFAULT.value2Char()){
        	repaymentPlan.setPlanState(RepaymentPlanStateEnum.PART.value2Char());
        }

        //借款人资金账户解冻、支出还款金额
        String descUnFreeze = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.REPAYMENT_UNFROZEN, loanPublish.getLoanTitle());
        AccountValueChanged avcUnFreeze = new AccountValueChanged(loanAccountId,repaymentBalance,
                repaymentBalance, AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENT.getValue(),
                "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                loanApplication.getLoanApplicationId(), now,descUnFreeze,true);
        avcq.addAccountValueChanged(avcUnFreeze);

        CapitalFlow cap=new CapitalFlow();
        cap.setScheduleId(schedule.getScheduleId());
        cap.setOperationType(Integer.parseInt(HFOperationEnum.UNFROZEN.getValue()));
        cap.setFromUser(loanApplication.getUserId());
        cap.setAmount(repaymentBalance);
        cap.setStartTime(new Date());
        cap.setBusinessId(repaymentRecord.getRepaymentPlanId());
        cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
        capitalFlowService.addCapital(cap);

        String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.REPAYMENT_PAY, loanPublish.getLoanTitle());
        AccountValueChanged avcPay = new AccountValueChanged(loanAccountId,repaymentBalance,
                repaymentBalance, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENT.getValue(),
                "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                loanApplication.getLoanApplicationId(), now,descPay,true);
        avcq.addAccountValueChanged(avcPay);
    }

    /**
     * 还款(当期全部利息+部分本金)
     *
     * @param currentShouldInterest
     * @throws Exception
     */
    private void repayAllInterestAndHalfCapital(BigDecimal currentShouldInterest) throws Exception {
        Date now = new Date();
        theLastRepayment = false;
        //增加还款记录

        BigDecimal currentInterest = currentShouldInterest;
        BigDecimal currentCalital = repaymentBalance.subtract(currentInterest);
        currentShouldCalital = currentCalital;

        repaymentRecord.setFactInterest(currentInterest);
        repaymentRecord.setFactCalital(currentCalital);

        //修改还款计划状态
        repaymentPlan.setFactBalance(repaymentPlan.getFactBalance().add(currentCalital.add(currentInterest)));
        repaymentPlan.setFactCalital(repaymentPlan.getFactCalital().add(currentCalital));
        repaymentPlan.setFactInterest(repaymentPlan.getFactInterest().add(currentInterest));
        if (BigDecimalUtil.compareTo(repaymentPlan.getShouldCapital2(), BigDecimal.ZERO, true, 2) == 0){
            repaymentPlan.setPlanState(RepaymentPlanStateEnum.COMPLETE.value2Char());
        } else if(repaymentPlan.getPlanState() != RepaymentPlanStateEnum.DEFAULT.value2Char()){
        	repaymentPlan.setPlanState(RepaymentPlanStateEnum.PART.value2Char());
        }

        //借款人资金账户支出还款金额
        String descUnFreeze = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.REPAYMENT_UNFROZEN, loanPublish.getLoanTitle());
        AccountValueChanged avcUnFreeze = new AccountValueChanged(loanAccountId,repaymentBalance,
                repaymentBalance, AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENT.getValue(),
                "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                loanApplication.getLoanApplicationId(), now,descUnFreeze,true);
        avcq.addAccountValueChanged(avcUnFreeze);

        CapitalFlow cap=new CapitalFlow();
        cap.setScheduleId(schedule.getScheduleId());
        cap.setOperationType(Integer.parseInt(HFOperationEnum.UNFROZEN.getValue()));
        cap.setFromUser(loanApplication.getUserId());
        cap.setAmount(repaymentBalance);
        cap.setStartTime(new Date());
        cap.setBusinessId(repaymentRecord.getRepaymentPlanId());
        cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
        capitalFlowService.addCapital(cap);

        String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.REPAYMENT_PAY, loanPublish.getLoanTitle());
        AccountValueChanged avcPay = new AccountValueChanged(loanAccountId,repaymentBalance,
                repaymentBalance, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENT.getValue(),
                "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                loanApplication.getLoanApplicationId(), now,descPay,true);
        avcq.addAccountValueChanged(avcPay);
    }

    /**
     * 还款（本期全部本金+本期全部利息）
     *
     * @param currentShouldBalance
     * @param currentShouldInterest
     * @throws Exception
     */
    private void repayAllInterestAndCapital(BigDecimal currentShouldBalance, BigDecimal currentShouldInterest) throws Exception {
        Date now = new Date();
        theLastRepayment = true;
        currentShouldCalital = repaymentPlan.getShouldCapital2().subtract(repaymentPlan.getFactCalital());
        //增加还款记录
        repaymentRecord.setFactInterest(currentShouldInterest);
        repaymentRecord.setFactCalital(repaymentPlan.getShouldCapital2().subtract(repaymentPlan.getFactCalital()));


        //修改还款计划状态
        repaymentPlan.setFactBalance(repaymentPlan.getShouldBalance2());
        repaymentPlan.setFactCalital(repaymentPlan.getShouldCapital2());
        repaymentPlan.setFactInterest(repaymentPlan.getShouldInterest2());
        repaymentPlan.setPlanState(RepaymentPlanStateEnum.COMPLETE.value2Char());


        //借款人资金账户支出还款金额
        String descUnFreeze = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.REPAYMENT_UNFROZEN, loanPublish.getLoanTitle());
        AccountValueChanged avcUnFreeze = new AccountValueChanged(loanAccountId,currentShouldBalance,
                currentShouldBalance, AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENT.getValue(),
                "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                loanApplication.getLoanApplicationId(), now,descUnFreeze,true);
        avcq.addAccountValueChanged(avcUnFreeze);

        CapitalFlow cap=new CapitalFlow();
        cap.setScheduleId(schedule.getScheduleId());
        cap.setOperationType(Integer.parseInt(HFOperationEnum.UNFROZEN.getValue()));
        cap.setFromUser(loanApplication.getUserId());
        cap.setAmount(currentShouldBalance);
        cap.setStartTime(new Date());
        cap.setBusinessId(repaymentRecord.getRepaymentPlanId());
        cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
        capitalFlowService.addCapital(cap);

        String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.REPAYMENT_PAY, loanPublish.getLoanTitle());
        AccountValueChanged avcPay = new AccountValueChanged(loanAccountId,currentShouldBalance,
                currentShouldBalance, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENT.getValue(),
                "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                loanApplication.getLoanApplicationId(), now,descPay,true);
        avcq.addAccountValueChanged(avcPay);
        repaymentBalance = repaymentBalance.subtract(currentShouldBalance);
    }

    /**
     * 还款（本期全部本金+不还利息）
     *
     * @param currentShouldBalance
     * @throws Exception
     */
    private void repayAllCapital(BigDecimal currentShouldBalance) throws Exception {
        Date now = new Date();
        theLastRepayment = true;
        currentShouldCalital = repaymentPlan.getShouldCapital2().subtract(repaymentPlan.getFactCalital());
        //增加还款记录
        repaymentRecord.setFactInterest(BigDecimal.valueOf(0));
        repaymentRecord.setFactCalital(repaymentPlan.getShouldCapital2().subtract(repaymentPlan.getFactCalital()));




        //修改还款计划状态
        repaymentPlan.setFactCalital(repaymentPlan.getShouldCapital2());
        repaymentPlan.setFactInterest(repaymentPlan.getFactInterest());
        repaymentPlan.setFactBalance(repaymentPlan.getFactCalital().add(repaymentPlan.getFactInterest()));
        repaymentPlan.setPlanState(RepaymentPlanStateEnum.BEFORE_COMPLETE.value2Char());


        //借款人资金账户支出还款金额
        if (BigDecimalUtil.compareTo(currentShouldBalance, BigDecimal.valueOf(0),true,2) > 0) {

            String descUnFreeze = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.REPAYMENT_UNFROZEN, loanPublish.getLoanTitle());
            AccountValueChanged avcUnFreeze = new AccountValueChanged(loanAccountId,currentShouldBalance,
                    currentShouldBalance, AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENT.getValue(),
                    "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                    AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                    loanApplication.getLoanApplicationId(), now,descUnFreeze,true);
            avcq.addAccountValueChanged(avcUnFreeze);

            CapitalFlow cap=new CapitalFlow();
            cap.setScheduleId(schedule.getScheduleId());
            cap.setOperationType(Integer.parseInt(HFOperationEnum.UNFROZEN.getValue()));
            cap.setFromUser(loanApplication.getUserId());
            cap.setAmount(currentShouldBalance);
            cap.setStartTime(new Date());
            cap.setBusinessId(repaymentRecord.getRepaymentPlanId());
            cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
            cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
            capitalFlowService.addCapital(cap);

            String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.REPAYMENT_PAY, loanPublish.getLoanTitle());
            AccountValueChanged avcPay = new AccountValueChanged(loanAccountId,currentShouldBalance,
                    currentShouldBalance, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_REPAYMENT.getValue(),
                    "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                    AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                    loanApplication.getLoanApplicationId(), now,descPay,true);
            avcq.addAccountValueChanged(avcPay);
        }
        repaymentBalance = repaymentBalance.subtract(currentShouldBalance);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isTheLastRepayment() {
        return theLastRepayment;
    }
}
