package com.xt.cfp.core.event.command;


import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.PayOffEnum;
import com.xt.cfp.core.constants.VisiableEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.RightsPrepayDetail;
import com.xt.cfp.core.pojo.Schedule;
import com.xt.cfp.core.service.CapitalFlowService;
import com.xt.cfp.core.service.RightsPrepayDetailService;
import com.xt.cfp.core.service.UserAccountService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.util.BigDecimalUtil;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-11 下午5:30.
 */
public class RepaymentRightsPrepayCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private BigDecimal theLenderRightsBalance;
    private BigDecimal repayDetailBalance;
    private AccountValueChangedQueue avcq;
    private RightsPrepayDetailService rightsPrepayDetailService;
    private RightsPrepayDetail rightsPrepayDetails;
    private CapitalFlowService capitalFlowService;
    private Schedule schedule;
    private UserAccountService userAccountService;

    public RepaymentRightsPrepayCommand(BigDecimal theLenderRightsBalance, BigDecimal repayDetailBalance,long creditorRightsId,AccountValueChangedQueue avcq,
                                        RightsPrepayDetailService rightsPrepayDetailService,CapitalFlowService capitalFlowService,Schedule schedule,
                                        UserAccountService userAccountService) throws Exception {
        this.theLenderRightsBalance = theLenderRightsBalance;
        this.repayDetailBalance = repayDetailBalance;
        this.avcq = avcq;
        this.rightsPrepayDetailService = rightsPrepayDetailService;
        this.capitalFlowService = capitalFlowService;
        this.schedule = schedule;
        this.userAccountService = userAccountService;

        rightsPrepayDetails = rightsPrepayDetailService.findByNewRightsId(creditorRightsId);
        if (rightsPrepayDetails != null) {
            if (rightsPrepayDetails.getIsPayOff() == PayOffEnum.NO.value2Char()) {
                BigDecimal unpayBalance = rightsPrepayDetails.getPrepayBalance2().subtract(rightsPrepayDetails.getRepaymentBalance());
                if (BigDecimalUtil.compareTo(theLenderRightsBalance, unpayBalance, true, 2) >= 0) {
                } else {
                    unpayBalance = theLenderRightsBalance;
                }
                this.repayDetailBalance = repayDetailBalance.add(unpayBalance);
            }
        }
    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****开始计算并支出平台垫付的费用****");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_REPAYMENTRIGHTSPREPAY);
        //查询是否垫付，如有垫付，则先还垫付
        Date now = new Date();
        if (rightsPrepayDetails != null) {
            if (rightsPrepayDetails.getIsPayOff() == PayOffEnum.NO.value2Char()) {
                BigDecimal unpayBalance = rightsPrepayDetails.getPrepayBalance2().subtract(rightsPrepayDetails.getRepaymentBalance());
                if (BigDecimalUtil.compareTo(theLenderRightsBalance, unpayBalance, true, 2) >= 0) {
                    rightsPrepayDetails.setRepaymentBalance(rightsPrepayDetails.getPrepayBalance());
                    rightsPrepayDetails.setIsPayOff(PayOffEnum.YES.value2Char());

                } else {
                    unpayBalance = theLenderRightsBalance;
                    rightsPrepayDetails.setRepaymentBalance(rightsPrepayDetails.getRepaymentBalance().add(unpayBalance));
                    rightsPrepayDetails.setIsPayOff(PayOffEnum.NO.value2Char());
                }
                repayDetailBalance = repayDetailBalance.add(unpayBalance);


                String descIncome = "返还平台垫付的利息";
                AccountValueChanged avcIncome = new AccountValueChanged(rightsPrepayDetails.getAccountId(),unpayBalance,
                        BigDecimalUtil.up(unpayBalance, 2), AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_RETURNPREPAY.getValue(),
                        "RightsPrepayDetail", VisiableEnum.DISPLAY.getValue(),rightsPrepayDetails.getPrepayDetailId(),
                        AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                        rightsPrepayDetails.getAccountId(), now,descIncome,true);
                avcq.addAccountValueChanged(avcIncome);

                Map<String,Object> prepayDetailMap = new HashMap<String,Object>();
                prepayDetailMap.put("prepayDetailId",rightsPrepayDetails.getPrepayDetailId());
                prepayDetailMap.put("repaymentBalance",rightsPrepayDetails.getRepaymentBalance());
                prepayDetailMap.put("isPayOff",rightsPrepayDetails.getIsPayOff());
                rightsPrepayDetailService.update(prepayDetailMap);
                theLenderRightsBalance = theLenderRightsBalance.subtract(unpayBalance);
            }
        }
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);
        taskExecLog.setLogInfo("计算并支出平台垫付的费用完成");
        logger.debug("****计算并支出平台垫付的费用完成****");
        return null;
    }

    public BigDecimal getRepayDetailBalance() {
        return repayDetailBalance;
    }
}
