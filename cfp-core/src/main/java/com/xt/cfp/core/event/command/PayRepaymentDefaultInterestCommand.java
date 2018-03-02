package com.xt.cfp.core.event.command;


import com.external.deposites.utils.HfUtils;
import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.CapitalFlowService;
import com.xt.cfp.core.service.LendOrderService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-11 下午5:09.
 */
public class PayRepaymentDefaultInterestCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private long loanAccountId;
    private BigDecimal sumDefaultInterest;
    private LoanApplication loanApplication;
    private long systemAccountId;
    private RepaymentRecord repaymentRecord;
    private List<LoanApplicationFeesItem> breakFessItems;
    private Map<Long, BigDecimal> lendsRatioMap;
    private Map<Long, Long> lendsOrderMap;
    private AccountValueChangedQueue avcq;
    private LoanPublish loanPublish;
    private CapitalFlowService capitalFlowService;
    private Schedule schedule;
    private UserAccountService userAccountService;
    private LendOrderService lendOrderService;

    public PayRepaymentDefaultInterestCommand(long loanAccountId, BigDecimal sumDefaultInterest,LoanApplication loanApplication,long systemAccountId,
                                              RepaymentRecord repaymentRecord, List<LoanApplicationFeesItem> breakFessItems,
                                              Map<Long, BigDecimal> lendsRatioMap, Map<Long, Long> lendsOrderMap,AccountValueChangedQueue avcq,
                                              LoanPublish loanPublish,CapitalFlowService capitalFlowService,Schedule schedule,UserAccountService userAccountService,
                                              LendOrderService lendOrderService) {
        this.loanAccountId = loanAccountId;
        this.sumDefaultInterest = sumDefaultInterest;
        this.loanApplication = loanApplication;
        this.systemAccountId = systemAccountId;
        this.repaymentRecord = repaymentRecord;
        this.breakFessItems = breakFessItems;
        this.lendsRatioMap = lendsRatioMap;
        this.lendsOrderMap = lendsOrderMap;
        this.avcq = avcq;
        this.loanPublish = loanPublish;
        this.capitalFlowService = capitalFlowService;
        this.schedule = schedule;
        this.userAccountService = userAccountService;
        this.lendOrderService = lendOrderService;
    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****开始计算支出借款罚息****");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_PAYREPAYMENTINTEREST);
        Date now = new Date();

        //借款合同解冻、支出罚息流水
        String desc = "罚息金额解冻";
        AccountValueChanged avcUnfreeze = new AccountValueChanged(loanAccountId,sumDefaultInterest,
                sumDefaultInterest, AccountConstants.AccountOperateEnum.UNFREEZE.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAYMENTDEFAULTINTEREST.getValue(),
                "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                loanApplication.getLoanApplicationId(), now,desc,true);
        avcq.addAccountValueChanged(avcUnfreeze);
        String descPay = "支出罚息流水";
        AccountValueChanged avcPay = new AccountValueChanged(loanAccountId,sumDefaultInterest,
                sumDefaultInterest, AccountConstants.AccountOperateEnum.PAY.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_PAYMENTDEFAULTINTEREST.getValue(),
                "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                AccountConstants.AccountChangedTypeEnum.LOAN.getValue(),
                loanApplication.getLoanApplicationId(), now,descPay,true);
        avcq.addAccountValueChanged(avcPay);

        UserAccount platformAccount = userAccountService.getPlatformAccountByType(AccountConstants.AccountTypeEnum.PLATFORM_OPERATING);
        CapitalFlow cap=new CapitalFlow();
        cap.setScheduleId(schedule.getScheduleId());
        cap.setOperationType(Integer.parseInt(HFOperationEnum.UNFROZEN.getValue()));
        cap.setFromUser(loanApplication.getUserId());
        cap.setAmount(sumDefaultInterest);
        cap.setStartTime(new Date());
        cap.setBusinessId(repaymentRecord.getRepaymentPlanId());
        cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
        capitalFlowService.addCapital(cap);

        cap=new CapitalFlow();
        cap.setScheduleId(schedule.getScheduleId());
        cap.setOperationType(Integer.parseInt(HFOperationEnum.ALLOCATION_PERSON_TO_COMPANY.getValue()));
        cap.setFromUser(loanApplication.getUserId());
        cap.setToUser(platformAccount.getUserId());
        cap.setAmount(sumDefaultInterest);
        cap.setStartTime(new Date());
        cap.setBusinessId(repaymentRecord.getRepaymentPlanId());
        cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
        capitalFlowService.addCapital(cap);

        //计算罚息的分配（平台账户与出借人各占比例的分配）
        for (LoanApplicationFeesItem applicationFeesItem : breakFessItems) {
            if (applicationFeesItem.getItemType().equals(String.valueOf(FeesItemTypeEnum.ITEMTYPE_DEFAULTINTREST.getValue()))) {
                BigDecimal workflowRatio = applicationFeesItem.getWorkflowRatio();
                BigDecimal lendRatio = BigDecimal.ONE.subtract(workflowRatio);

                //按各出借人出资占比，分配罚息
                Iterator lendIterator = lendsRatioMap.keySet().iterator();
                //所有出借人的罚息总额
                BigDecimal sumLendsDefaultInterest = BigDecimal.ZERO;
                while (lendIterator.hasNext()) {
                    long lendOrderId = (Long) lendIterator.next();
                    BigDecimal lendInterestBalance = sumDefaultInterest.multiply(lendRatio).multiply(lendsRatioMap.get(lendOrderId).divide(new BigDecimal("100"), 18,
            				BigDecimal.ROUND_CEILING));
                    if (BigDecimalUtil.compareTo(lendInterestBalance, BigDecimal.ZERO, false, 2) > 0) {
                        BigDecimal lendInterestBalance2 = BigDecimalUtil.down(lendInterestBalance, 2);
                        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.DEFAULTINTEREST_IN,loanPublish.getLoanTitle());
                        AccountValueChanged avcIncome = new AccountValueChanged(lendsOrderMap.get(lendOrderId),lendInterestBalance,
                                lendInterestBalance2, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_DELAYDEFAULTINTEREST.getValue(),
                                "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                                AccountConstants.AccountChangedTypeEnum.LEND.getValue(),
                                lendOrderId, now,descIncome,true);
                        avcq.addAccountValueChanged(avcIncome);
                        sumLendsDefaultInterest = sumLendsDefaultInterest.add(lendInterestBalance2);
                        LendOrder lendOrder = lendOrderService.findById(lendOrderId);
                        cap=new CapitalFlow();
                        cap.setScheduleId(schedule.getScheduleId());
                        cap.setOperationType(Integer.parseInt(HFOperationEnum.ALLOCATION_COMPANY_TO_PERSON.getValue()));
                        cap.setFromUser(platformAccount.getUserId());
                        cap.setToUser(lendOrder.getLendUserId());
                        cap.setAmount(sumDefaultInterest);
                        cap.setStartTime(new Date());
                        cap.setBusinessId(repaymentRecord.getRepaymentPlanId());
                        cap.setBusinessFlow(HfUtils.getUniqueSerialNum());
                        cap.setResult(Integer.parseInt(ScheduleEnum.RESULT_WAITING.getValue()));
                        capitalFlowService.addCapital(cap);
                    }
                }
                BigDecimal systemInterest = sumDefaultInterest.subtract(sumLendsDefaultInterest);
                if (BigDecimalUtil.compareTo(systemInterest, BigDecimal.ZERO, false, 2) > 0) {
                    //平台账户按比例收入罚息.罚息总额减去所有出借人的罚息额之和即为平台罚息
                    String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.DEFAULTINTEREST_IN,loanPublish.getLoanTitle());
                    AccountValueChanged avcIncome = new AccountValueChanged(systemAccountId,systemInterest,
                            BigDecimalUtil.up(systemInterest,2), AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_INCOME_DELAYDEFAULTINTEREST.getValue(),
                            "RepaymentRecord", VisiableEnum.DISPLAY.getValue(),repaymentRecord.getRepaymentRecordId(),
                            AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                            systemAccountId, now,descIncome,true);
                    avcq.addAccountValueChanged(avcIncome);
                }
            }
        }
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);
        taskExecLog.setLogInfo("计算支出借款罚息完毕(借款合同:" + loanApplication.getLoanApplicationCode() + ")");

        logger.debug("****计算支出借款罚息完毕****");
        return null;
    }


}
