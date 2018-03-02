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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-11 下午4:42.
 */
public class RepaymentDelayFeesCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private long loanApplicationId;
    private LoanPublish loanPublish;
    private boolean ignoreDelay;
    private BigDecimal balance;

    private BigDecimal delayFees;
    private BigDecimal repaymentBalance;
    private long loanAccountId;

    private long systemAccountId;

    private LoanApplicationFeesItemService loanApplicationFeesItemService;

    private LoanFeesDetailService loanFeesDetailService;

    private AccountValueChangedQueue avcq;

    private List<LoanFeesDetail> feesDetails;
    private LoanApplication loanApplication;
    private CapitalFlowService capitalFlowService;
    private Schedule schedule;
    private UserAccountService userAccountService;

    public RepaymentDelayFeesCommand(long loanApplicationId,LoanPublish loanPublish, RepaymentPlan repaymentPlan, boolean ignoreDelay,
                                     BigDecimal balance, BigDecimal delayFees, long loanAccountId,long systemAccountId, LoanApplicationFeesItemService loanApplicationFeesItemService,
                                     LoanFeesDetailService loanFeesDetailService,LoanApplicationService loanApplicationService,AccountValueChangedQueue avcq,
                                     CapitalFlowService capitalFlowService,Schedule schedule,UserAccountService userAccountService) throws Exception {
        this.loanApplicationId = loanApplicationId;
        this.loanPublish = loanPublish;
        this.ignoreDelay = ignoreDelay;
        this.balance = balance;
        this.delayFees = delayFees;
        this.repaymentBalance = balance;
        this.loanAccountId = loanAccountId;
        this.systemAccountId = systemAccountId;
        this.loanApplicationFeesItemService = loanApplicationFeesItemService;
        this.loanFeesDetailService = loanFeesDetailService;
        this.avcq = avcq;
        this.capitalFlowService = capitalFlowService;
        this.schedule = schedule;
        this.userAccountService = userAccountService;

        Map loanFeesDetailMap = new HashMap();
        loanFeesDetailMap.put("loanApplicationId", loanApplicationId);
        loanFeesDetailMap.put("sectionCode", repaymentPlan.getSectionCode());
        loanFeesDetailMap.put("feesCycle", FeesPointEnum.ATDELAY_FIRSTDAY.getValue());
        loanFeesDetailMap.put("feesState", FeesDetailEnum.UNPAY.getValue());
        feesDetails = loanFeesDetailService.getFeesItemBy(loanFeesDetailMap);
        loanApplication = loanApplicationService.findById(loanApplicationId);

        for (LoanFeesDetail feesDetail : feesDetails) {
            if (!ignoreDelay) {
                if (BigDecimalUtil.compareTo(this.balance, BigDecimal.ZERO, true, 2) > 0) {
                    this.delayFees = feesDetail.getFees2().subtract(feesDetail.getPaidFees()!=null?feesDetail.getPaidFees():BigDecimal.ZERO);
                    if (BigDecimalUtil.compareTo(this.balance, this.delayFees, true, 2) > 0) {
                        this.balance = this.balance.subtract(this.delayFees);
                    } else {
                        this.delayFees = balance;
                        this.balance = BigDecimal.ZERO;
                    }

                }
            }
        }
    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****开始计算并支出借款合同的逾期费****");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_REPAYMENTDELAYFEES);
        Date now = new Date();
        for (LoanFeesDetail feesDetail : feesDetails) {
            if (ignoreDelay) {
            	feesDetail.setReductionBalance(feesDetail.getFees().subtract(feesDetail.getPaidFees()));
                feesDetail.setFeesState(FeesDetailEnum.WAIVER.value2Char());
                feesDetail.setReductionBalance(feesDetail.getFees().subtract(feesDetail.getPaidFees()));
            } else {
                if (BigDecimalUtil.compareTo(repaymentBalance, BigDecimal.ZERO, true, 2) > 0) {
                    BigDecimal delayFees = feesDetail.getFees2().subtract(feesDetail.getPaidFees()!=null?feesDetail.getPaidFees():BigDecimal.ZERO);
                    BigDecimal delayFees18 = feesDetail.getFees().subtract(feesDetail.getPaidFees()!=null?feesDetail.getPaidFees():BigDecimal.ZERO);
                    if (BigDecimalUtil.compareTo(repaymentBalance, delayFees, true, 2) > 0) {
                        feesDetail.setPaidFees(feesDetail.getFees2());
                        feesDetail.setFeesState(LoanFeesDetail.FEESSTATE_PAID);
                        repaymentBalance = repaymentBalance.subtract(delayFees);
                    } else {
                        feesDetail.setPaidFees(feesDetail.getPaidFees()!=null?feesDetail.getPaidFees().add(repaymentBalance):BigDecimal.ZERO.add(repaymentBalance));
                        delayFees = repaymentBalance;
                        delayFees18 = repaymentBalance;
                        repaymentBalance = BigDecimal.ZERO;

                    }
                    LoanApplicationFeesItem applicationFeesItem = loanApplicationFeesItemService.getLoanApplicationFeesItemById(feesDetail.getLoanApplicationFeesItemId());


                    //借款合同解冻扣费金额
                    String desc = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.DELAYFEES_UNFREEZE,loanPublish.getLoanTitle(),applicationFeesItem.getItemName());
                    AccountValueChanged avcUnfreeze = new AccountValueChanged(loanAccountId,delayFees,
                            delayFees, AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_MANAGER.getValue(),
                            "LoanFeesDetail", VisiableEnum.DISPLAY.getValue(),feesDetail.getLoanFeesDetailId(),
                            AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                            loanApplicationId, now,desc,true);
                    avcq.addAccountValueChanged(avcUnfreeze);

                    UserAccount platformAccount = userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);
                    CapitalFlow cap=new CapitalFlow();
                    cap.setScheduleId(schedule.getScheduleId());
                    cap.setOperationType(Integer.parseInt(HFOperationEnum.UNFROZEN.getValue()));
                    cap.setFromUser(loanApplication.getUserId());
                    cap.setAmount(delayFees);
                    cap.setStartTime(new Date());
                    cap.setBusinessId(feesDetail.getLoanFeesDetailId());
                    cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
                    cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
                    capitalFlowService.addCapital(cap);

                    //借款合同支出扣费金额

                    String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.DELAYFEES_PAY,loanPublish.getLoanTitle(),applicationFeesItem.getItemName());
                    AccountValueChanged avcPay = new AccountValueChanged(loanAccountId,delayFees,
                            delayFees, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_MANAGER.getValue(),
                            "LoanFeesDetail", VisiableEnum.DISPLAY.getValue(),feesDetail.getLoanFeesDetailId(),
                            AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                            loanApplicationId, now,descPay,true);
                    avcq.addAccountValueChanged(avcPay);

                    //平台账户增加收入流水
                    String descSystemIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.DELAYFEES_INCOME,loanPublish.getLoanTitle(),applicationFeesItem.getItemName());
                    AccountValueChanged avcSystemIncome = new AccountValueChanged(systemAccountId,delayFees,
                            delayFees, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_DELAYFEES.getValue(),
                            "LoanFeesDetail", VisiableEnum.DISPLAY.getValue(),feesDetail.getLoanFeesDetailId(),
                            AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                            systemAccountId, now,descSystemIncome,false);
                    avcq.addAccountValueChanged(avcSystemIncome);


                    cap=new CapitalFlow();
                    cap.setScheduleId(schedule.getScheduleId());
                    cap.setOperationType(Integer.parseInt(HFOperationEnum.ALLOCATION_PERSON_TO_COMPANY.getValue()));
                    cap.setFromUser(loanApplication.getUserId());
                    cap.setToUser(platformAccount.getUserId());
                    cap.setAmount(delayFees);
                    cap.setStartTime(new Date());
                    cap.setBusinessId(feesDetail.getLoanFeesDetailId());
                    cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
                    cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
                    capitalFlowService.addCapital(cap);
                }
            }
            Map<String,Object> feesDetailMap = new HashMap<String, Object>();
            feesDetailMap.put("fees",feesDetail.getFees());
            feesDetailMap.put("fees2",feesDetail.getFees2());
            feesDetailMap.put("paidFees",feesDetail.getPaidFees());
            feesDetailMap.put("reductionBalance",feesDetail.getReductionBalance());
            feesDetailMap.put("feesState",feesDetail.getFeesState());
            feesDetailMap.put("loanFeesDetailId", feesDetail.getLoanFeesDetailId());
            loanFeesDetailService.update(feesDetailMap);
        }

        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);
        taskExecLog.setLogInfo("计算并支出借款合同的逾期费完成");
        logger.debug("****计算并支出借款合同的逾期费完成****");
        return null;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getDelayFees() {
        return delayFees;
    }
}
