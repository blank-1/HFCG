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
 * Created by Renyulin on 14-6-11 下午4:11.
 */
public class RepaymentFeesCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private RepaymentPlan repaymentPlan;
    private long loanAccountId;
    private long systemAccountId;
    private long adminId;
    private BigDecimal balance;
    private BigDecimal sumFeesBalance;

    private BigDecimal allInterest;
    private List<LoanApplicationFeesItem> commonFeesItems;
    private LoanFeesDetailService loanFeesDetailService;
    private LoanApplicationFeesItemService loanApplicationFeesItemService;
    private UserAccountService userAccountService;
    private LoanApplicationService loanApplicationService;
    private AccountValueChangedQueue avcq;

    private List<LoanFeesDetail> loanFeesDetails;
    private LoanApplication loanApplication;
    private LoanPublish loanPublish;

    private BigDecimal repaymentBalance;
    private long loanApplicationId;
    private CapitalFlowService capitalFlowService;
    private Schedule schedule;


    public RepaymentFeesCommand(LoanApplication loanApplication,LoanPublish loanPublish, RepaymentPlan repaymentPlan, long loanAccountId, long systemAccountId, long adminId,
                                BigDecimal balance, BigDecimal sumFeesBalance, BigDecimal allInterest, List<LoanApplicationFeesItem> commonFeesItems,
                                LoanFeesDetailService loanFeesDetailService,LoanApplicationFeesItemService loanApplicationFeesItemService,
                                UserAccountService userAccountService,LoanApplicationService loanApplicationService,AccountValueChangedQueue avcq,
                                CapitalFlowService capitalFlowService,Schedule schedule) throws Exception {
        this.repaymentPlan = repaymentPlan;
        this.loanAccountId = loanAccountId;
        this.systemAccountId = systemAccountId;
        this.adminId = adminId;
        this.balance = balance;
        this.sumFeesBalance = sumFeesBalance;
        this.repaymentBalance = balance;
        this.allInterest = allInterest;
        this.commonFeesItems = commonFeesItems;
        this.loanFeesDetailService = loanFeesDetailService;
        this.loanApplicationFeesItemService = loanApplicationFeesItemService;
        this.userAccountService = userAccountService;
        this.loanApplicationService = loanApplicationService;
        this.avcq = avcq;
        this.loanApplication = loanApplication;
        this.loanPublish = loanPublish;
        this.loanApplicationId = loanApplication.getLoanApplicationId();
        this.capitalFlowService = capitalFlowService;
        this.schedule = schedule;

        Map map = new HashMap();
        map.put("loanApplicationId", loanApplication.getLoanApplicationId());
        map.put("sectionCode", repaymentPlan.getSectionCode());
        map.put("feesCycle", FeesPointEnum.ATCYCLE.value2Int());
        loanFeesDetails = loanFeesDetailService.getFeesItemBy(map);

        BigDecimal zeroBigDecimal = BigDecimal.ZERO;
        if (loanFeesDetails != null && loanFeesDetails.size() > 0) {
            for (LoanFeesDetail feesDetail : loanFeesDetails) {
                if (feesDetail.getFeesState() == FeesDetailEnum.UNPAY.value2Char()) {
                    BigDecimal unpay = feesDetail.getFees().subtract(feesDetail.getPaidFees());
                    BigDecimal repaymentFees = zeroBigDecimal;
                    if (this.balance.compareTo(zeroBigDecimal) > 0) {
                        if (this.balance.compareTo(unpay) >= 0) {
                            repaymentFees = unpay;
                        } else {
                            repaymentFees = this.balance;
                        }
                        if (BigDecimalUtil.compareTo(repaymentFees, zeroBigDecimal, true, 2) > 0) {
                            this.balance = this.balance.subtract(repaymentFees);
                        }
                    }
                    this.sumFeesBalance = this.sumFeesBalance.add(repaymentFees);
                }
            }
        } else {
            for (LoanApplicationFeesItem applicationFeesItem : commonFeesItems) {
                if (applicationFeesItem.getChargeCycle() == FeesPointEnum.ATCYCLE.value2Char()) {

                    BigDecimal feesBalance = loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(
                            applicationFeesItem, loanApplication.getConfirmBalance(), allInterest, repaymentPlan.getShouldCapital(), repaymentPlan.getShouldInterest(),
                            zeroBigDecimal, zeroBigDecimal);
                    feesBalance = BigDecimalUtil.up(feesBalance, 2);

                    if (BigDecimalUtil.compareTo(this.balance, zeroBigDecimal, true, 2) > 0) {
                        if (BigDecimalUtil.compareTo(this.balance, feesBalance, true, 2) >= 0) {
                        } else {
                            feesBalance = this.balance;
                        }
                    } else {
                        feesBalance = zeroBigDecimal;
                        this.balance = zeroBigDecimal;
                    }

                    if (BigDecimalUtil.compareTo(feesBalance, zeroBigDecimal, true, 2) > 0 && BigDecimalUtil.compareTo(balance, feesBalance, true, 2) >= 0) {
                        this.balance = this.balance.subtract(feesBalance);
                    }
                    this.sumFeesBalance = this.sumFeesBalance.add(feesBalance);
                }
            }
        }
    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****开始计算并支出借款合同还款时所需扣的费用****");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        Date now = new Date();
        taskExecLog.setExecTime(now);
        taskExecLog.setTaskId(TaskInfo.TASKID_REPAYMENTFEES);
        BigDecimal zeroBigDecimal = BigDecimal.ZERO;
        BigDecimal sumFeesBalance = zeroBigDecimal;
        Map map = new HashMap();
        map.put("loanApplicationId", loanApplicationId);
        map.put("sectionCode", repaymentPlan.getSectionCode());
        map.put("feesCycle", FeesPointEnum.ATCYCLE.getValue());
        if (loanFeesDetails != null && loanFeesDetails.size() > 0) {
            UserAccount platformAccount = userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);
            for (LoanFeesDetail feesDetail : loanFeesDetails) {
                Map<String,Object> feesDetailMap = new HashMap<String, Object>();
                feesDetailMap.put("loanFeesDetailId",feesDetail.getLoanFeesDetailId());
                if (feesDetail.getFeesState()== FeesDetailEnum.UNPAY.value2Char()) {
                    BigDecimal unpay = feesDetail.getFees2().subtract(feesDetail.getPaidFees());
                    BigDecimal unpay18 = feesDetail.getFees().subtract(feesDetail.getPaidFees());
                    BigDecimal repaymentFees;
                    BigDecimal repaymentFees18;
                    if (repaymentBalance.compareTo(zeroBigDecimal) > 0) {
                        if (repaymentBalance.compareTo(unpay) >= 0) {
                            feesDetail.setPaidFees(feesDetail.getFees2());
                            repaymentFees = unpay;
                            repaymentFees18 = unpay18;
                            feesDetail.setFeesState(FeesDetailEnum.PAID.value2Char());
                            sumFeesBalance = sumFeesBalance.add(unpay);
                        } else {
                            repaymentFees = repaymentBalance;
                            repaymentFees18 = repaymentBalance;
                            feesDetail.setPaidFees(feesDetail.getPaidFees().add(repaymentFees));
                            sumFeesBalance = sumFeesBalance.add(repaymentBalance);
                        }
                        feesDetailMap.put("fees",feesDetail.getFees());
                        feesDetailMap.put("fees2",feesDetail.getFees2());
                        feesDetailMap.put("paidFees",feesDetail.getPaidFees());
                        feesDetailMap.put("reductionBalance",feesDetail.getReductionBalance());
                        feesDetailMap.put("feesState",feesDetail.getFeesState());
                        loanFeesDetailService.update(feesDetailMap);

                        if (BigDecimalUtil.compareTo(repaymentFees, zeroBigDecimal, true, 2) > 0) {

                            LoanApplicationFeesItem applicationFeesItem = loanApplicationFeesItemService.getLoanApplicationFeesItemById(feesDetail.getLoanApplicationFeesItemId());
                            //借款合同解冻扣费金额
                            String desc = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOAN_FEES_UNFREEZE, loanPublish.getLoanTitle(), applicationFeesItem.getItemName());
                            AccountValueChanged avcUnfreeze = new AccountValueChanged(loanAccountId,repaymentFees,
                                    repaymentFees, AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_MANAGER.getValue(),
                                    "LoanFeesDetail", VisiableEnum.DISPLAY.getValue(),feesDetail.getLoanFeesDetailId(),
                                    AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                                    loanApplicationId, now,desc,true);
                            avcq.addAccountValueChanged(avcUnfreeze);

                            CapitalFlow cap=new CapitalFlow();
                            cap.setScheduleId(schedule.getScheduleId());
                            cap.setOperationType(Integer.parseInt(HFOperationEnum.UNFROZEN.getValue()));
                            cap.setFromUser(loanApplication.getUserId());
                            cap.setAmount(repaymentFees);
                            cap.setStartTime(new Date());
                            cap.setBusinessId(feesDetail.getLoanFeesDetailId());
                            cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
                            cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
                            capitalFlowService.addCapital(cap);

                            //借款合同支出扣费金额
                            String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOAN_FEES_PAY, loanPublish.getLoanTitle(), applicationFeesItem.getItemName());
                            AccountValueChanged avcPay = new AccountValueChanged(loanAccountId,repaymentFees,
                                    repaymentFees, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_MANAGER.getValue(),
                                    "LoanFeesDetail", VisiableEnum.DISPLAY.getValue(),feesDetail.getLoanFeesDetailId(),
                                    AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                                    loanApplicationId, now,descPay,true);
                            avcq.addAccountValueChanged(avcPay);

                            //平台账户增加收入流水
                            String descSystemIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOAN_FEES_INCOME, loanPublish.getLoanTitle(), applicationFeesItem.getItemName());
                            AccountValueChanged avcSystemIncome = new AccountValueChanged(systemAccountId,repaymentFees,
                                    repaymentFees, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_MANAGER.getValue(),
                                    "LoanFeesDetail", VisiableEnum.DISPLAY.getValue(),feesDetail.getLoanFeesDetailId(),
                                    AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                                    systemAccountId, now,descSystemIncome,false);
                            avcq.addAccountValueChanged(avcSystemIncome);

                            cap=new CapitalFlow();
                            cap.setScheduleId(schedule.getScheduleId());
                            cap.setOperationType(Integer.parseInt(HFOperationEnum.ALLOCATION_PERSON_TO_COMPANY.getValue()));
                            cap.setFromUser(loanApplication.getUserId());
                            cap.setToUser(platformAccount.getUserId());
                            cap.setAmount(repaymentFees);
                            cap.setStartTime(new Date());
                            cap.setBusinessId(feesDetail.getLoanFeesDetailId());
                            cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
                            cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
                            capitalFlowService.addCapital(cap);

                            repaymentBalance = repaymentBalance.subtract(repaymentFees);
                        }
                    }
                }
            }


        } else {
            for (LoanApplicationFeesItem applicationFeesItem : commonFeesItems) {
                if (applicationFeesItem.getChargeCycle() == FeesPointEnum.ATCYCLE.value2Int()) {
                    LoanFeesDetail loanFeesDetail = new LoanFeesDetail();
                    loanFeesDetail.setLoanApplicationId(loanApplicationId);
                    loanFeesDetail.setLoanApplicationFeesItemId(applicationFeesItem.getLoanApplicationFeesItemId());
                    loanFeesDetail.setFeesCycle(applicationFeesItem.getChargeCycle());
                    loanFeesDetail.setSectionCode(repaymentPlan.getSectionCode());
                    BigDecimal feesBalance = loanApplicationFeesItemService.calculateLoanApplicationFeesBalance(applicationFeesItem, loanApplication.getConfirmBalance(), allInterest, repaymentPlan.getShouldCapital(), repaymentPlan.getShouldInterest(), zeroBigDecimal, zeroBigDecimal);
                    loanFeesDetail.setFees(feesBalance);
                    loanFeesDetail.setFees2(BigDecimalUtil.up(loanFeesDetail.getFees(), 2));
                    feesBalance = loanFeesDetail.getFees2();
                    if (BigDecimalUtil.compareTo(repaymentBalance, zeroBigDecimal, true, 2) > 0) {
                        if (BigDecimalUtil.compareTo(repaymentBalance, feesBalance, true, 2) >= 0) {
                            loanFeesDetail.setPaidFees(feesBalance);
                            loanFeesDetail.setFeesState(FeesDetailEnum.PAID.value2Char());
                        } else {
                            loanFeesDetail.setPaidFees(repaymentBalance);
                            loanFeesDetail.setFeesState(FeesDetailEnum.UNPAY.value2Char());
                            feesBalance = repaymentBalance;
                        }
                        sumFeesBalance = sumFeesBalance.add(feesBalance);
                    } else {
                        loanFeesDetail.setFeesState(FeesDetailEnum.UNPAY.value2Char());
                        loanFeesDetail.setPaidFees(zeroBigDecimal);
                        feesBalance = zeroBigDecimal;
                        repaymentBalance = zeroBigDecimal;
                    }


                    loanFeesDetailService.insert(loanFeesDetail);
                    if (BigDecimalUtil.compareTo(feesBalance, zeroBigDecimal, true, 2) > 0) {

                        //借款合同解冻扣费金额
                        String desc = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOAN_FEES_UNFREEZE, loanPublish.getLoanTitle(), applicationFeesItem.getItemName());
                        AccountValueChanged avcUnfreeze = new AccountValueChanged(loanAccountId,feesBalance,
                                feesBalance, AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_MANAGER.getValue(),
                                "LoanFeesDetail", VisiableEnum.DISPLAY.getValue(),loanFeesDetail.getLoanFeesDetailId(),
                                AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                                loanApplicationId, now,desc,true);
                        avcq.addAccountValueChanged(avcUnfreeze);
                        //借款合同支出扣费金额
                        String descPay = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOAN_FEES_PAY, loanPublish.getLoanTitle(), applicationFeesItem.getItemName());
                        AccountValueChanged avcPay = new AccountValueChanged(loanAccountId,feesBalance,
                                feesBalance, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_MANAGER.getValue(),
                                "LoanFeesDetail", VisiableEnum.DISPLAY.getValue(),loanFeesDetail.getLoanFeesDetailId(),
                                AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                                loanApplicationId, now,descPay,true);
                        avcq.addAccountValueChanged(avcPay);

                        //平台账户增加收入流水
                        String descSystemIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.LOAN_FEES_INCOME, loanPublish.getLoanTitle(), applicationFeesItem.getItemName());
                        AccountValueChanged avcSystemIncome = new AccountValueChanged(systemAccountId,feesBalance,
                                feesBalance, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_MANAGER.getValue(),
                                "LoanFeesDetail", VisiableEnum.DISPLAY.getValue(),loanFeesDetail.getLoanFeesDetailId(),
                                AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                                systemAccountId, now,descSystemIncome,false);
                        avcq.addAccountValueChanged(avcSystemIncome);
                        repaymentBalance = repaymentBalance.subtract(feesBalance);
                    }
                }
            }
        }
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);
        taskExecLog.setLogInfo("计算并支出借款合同还款时所需扣的费用完成");
        logger.debug("****计算并支出借款合同还款时所需扣的费用完成****");
        return null;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getSumFeesBalance() {
        return sumFeesBalance;
    }


}


