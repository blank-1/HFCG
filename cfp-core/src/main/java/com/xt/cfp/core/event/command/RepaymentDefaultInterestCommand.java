package com.xt.cfp.core.event.command;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.xt.cfp.core.constants.FeesDetailEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.DefaultInterestDetail;
import com.xt.cfp.core.pojo.RepaymentRecord;
import com.xt.cfp.core.service.DefaultInterestDetailService;
import com.xt.cfp.core.util.BigDecimalUtil;

/**
 * Created by Renyulin on 14-6-11 下午4:58.
 */
public class RepaymentDefaultInterestCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private List<DefaultInterestDetail> defaultInterestDetails;
    private RepaymentRecord repaymentRecord;
    private int delayDays;
    private boolean ignoreDelay;
    private BigDecimal balance;
    private BigDecimal repaymentBalance;
    private BigDecimal sumDefaultInterest;
    private BigDecimal sumDefaultInterest18;
    private DefaultInterestDetailService defaultInterestDetailService;

    public RepaymentDefaultInterestCommand(List<DefaultInterestDetail> defaultInterestDetails, RepaymentRecord repaymentRecord,
                                           int delayDays, boolean ignoreDelay, BigDecimal balance, BigDecimal sumDefaultInterest, BigDecimal sumDefaultInterest18,
                                           DefaultInterestDetailService defaultInterestDetailService) throws Exception {
        this.defaultInterestDetails = defaultInterestDetails;
        this.repaymentRecord = repaymentRecord;
        this.delayDays = delayDays;
        this.ignoreDelay = ignoreDelay;
        this.balance = balance;
        this.repaymentBalance = balance;
        this.sumDefaultInterest = sumDefaultInterest;
        this.sumDefaultInterest18 = sumDefaultInterest18;
        this.defaultInterestDetailService = defaultInterestDetailService;

        for (DefaultInterestDetail defaultInterestDetail : defaultInterestDetails) {
            this.delayDays++;
            BigDecimal paidBalance = defaultInterestDetail.getRepaymentBalance();
            BigDecimal zeroBigdecimal = BigDecimal.ZERO;
            if (paidBalance == null) {
                paidBalance = zeroBigdecimal;
            }
            if (ignoreDelay) {
            } else {
                if (BigDecimalUtil.compareTo(this.balance, zeroBigdecimal, true, 2) <= 0)
                    break;

                if (BigDecimalUtil.compareTo(this.balance, zeroBigdecimal, true, 2) > 0) {

                    BigDecimal unpayBalance = defaultInterestDetail.getInterestBalance2().subtract(paidBalance);
                    BigDecimal unpayBalance18 = defaultInterestDetail.getInterestBalance().subtract(paidBalance);
                    if (BigDecimalUtil.compareTo(this.balance, unpayBalance, true, 2) >= 0) {

                    } else {
                        unpayBalance = this.balance;

                    }
                    //累积计算本次要还的逾期罚息
                    this.sumDefaultInterest = this.sumDefaultInterest.add(unpayBalance);
                    this.sumDefaultInterest18 = this.sumDefaultInterest18.add(unpayBalance18);
                    this.balance = this.balance.subtract(unpayBalance);
                }
            }
        }
    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****开始查询计算并修改罚息状态****");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_REPAYMENTDEFAULTINTEREST);
        for (DefaultInterestDetail defaultInterestDetail : defaultInterestDetails) {

            BigDecimal paidBalance = defaultInterestDetail.getRepaymentBalance();
            if (paidBalance == null) {
                paidBalance = BigDecimal.ZERO;
            }
            if (ignoreDelay) {
            	defaultInterestDetail.setReductionBalance(defaultInterestDetail.getInterestBalance().subtract(defaultInterestDetail.getRepaymentBalance()));
                defaultInterestDetail.setRepaymentState(FeesDetailEnum.WAIVER.value2Char());
                defaultInterestDetailService.modify(defaultInterestDetail);
            } else {
                if (BigDecimalUtil.compareTo(repaymentBalance, BigDecimal.ZERO, true, 2) <= 0)
                    break;

                if (BigDecimalUtil.compareTo(repaymentBalance, BigDecimal.ZERO, true, 2) > 0) {

                    BigDecimal unpayBalance = defaultInterestDetail.getInterestBalance2().subtract(paidBalance);
                    if (BigDecimalUtil.compareTo(repaymentBalance, unpayBalance, true, 2) >= 0) {
                        defaultInterestDetail.setRepaymentState(FeesDetailEnum.PAID.value2Char());
                        defaultInterestDetail.setRepaymentBalance(defaultInterestDetail.getInterestBalance2());
                        defaultInterestDetailService.modify(defaultInterestDetail);

                    } else {
                        defaultInterestDetail.setRepaymentBalance(repaymentBalance.add(paidBalance));
                        defaultInterestDetailService.modify(defaultInterestDetail);
                        unpayBalance = repaymentBalance;

                    }
                    //累积计算本次要还的逾期罚息
                    sumDefaultInterest = sumDefaultInterest.add(unpayBalance);

                    repaymentBalance = repaymentBalance.subtract(unpayBalance);


                }
            }
        }
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);

        taskExecLog.setLogInfo("查询计算并修改罚息状态完成");
        logger.debug("****查询计算并修改罚息状态完成****");
        return sumDefaultInterest;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getSumDefaultInterest() {
        return sumDefaultInterest;
    }

    public BigDecimal getSumDefaultInterest18() {
        return sumDefaultInterest18;
    }

    public int getDelayDays() {
        return delayDays;
    }
}
