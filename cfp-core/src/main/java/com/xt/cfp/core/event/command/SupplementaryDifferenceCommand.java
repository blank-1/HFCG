package com.xt.cfp.core.event.command;

import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.VisiableEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.RightsPrepayDetailService;
import com.xt.cfp.core.service.container.AccountValueChanged;
import com.xt.cfp.core.service.container.AccountValueChangedQueue;
import com.xt.cfp.core.service.container.CallBack;
import com.xt.cfp.core.service.container.RepaymentCallBack;
import com.xt.cfp.core.util.BigDecimalUtil;
import com.xt.cfp.core.util.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lenovo on 2015/10/26.
 */
public class SupplementaryDifferenceCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private long systemAccountId;
    private BigDecimal factBelance;
    private BigDecimal shouldBelance;
    private RepaymentRecord repaymentRecord;

    private AccountValueChangedQueue avcq;

    public SupplementaryDifferenceCommand(long systemAccountId, BigDecimal factBelance, BigDecimal shouldBelance, RepaymentRecord repaymentRecord, AccountValueChangedQueue avcq) {
        this.systemAccountId = systemAccountId;
        this.factBelance = factBelance;
        this.shouldBelance = shouldBelance;
        this.repaymentRecord = repaymentRecord;
        this.avcq = avcq;

    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****开始平台补差****");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_SAVEACCURACY);

        Date now = new Date();
        //记录平台补差
        SupplementaryDifference sd = new SupplementaryDifference();
        sd.setCreateTime(now);
        String descIncome = "";
        String operatetype = "";
        if (BigDecimalUtil.compareTo(factBelance.subtract(shouldBelance), BigDecimal.ZERO, false, 2) > 0) {
            //收入
            sd.setAmount(factBelance.subtract(shouldBelance));
            sd.setType("0");//+收入
            descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.SUPPLEMETARY_DIFFERENCE_INCOME, sd.getAmount().toPlainString());
            operatetype = AccountConstants.AccountOperateEnum.INCOM.getValue();
        }else if (BigDecimalUtil.compareTo(factBelance.subtract(shouldBelance), BigDecimal.ZERO, false, 2) < 0) {
            //补差
            sd.setAmount(shouldBelance.subtract(factBelance));
            sd.setType("1");//- 支出
            descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.SUPPLEMETARY_DIFFERENCE_PAY, sd.getAmount().toPlainString());
            operatetype = AccountConstants.AccountOperateEnum.PAY.getValue();
        }else{
            return null;
        }
        sd.setRepaymentRecordId(repaymentRecord.getRepaymentRecordId());
        sd.setAccId(systemAccountId);

        CallBack callBack = new RepaymentCallBack(sd);

        AccountValueChanged avcIncome = new AccountValueChanged(systemAccountId, sd.getAmount(),
                sd.getAmount(), operatetype, AccountConstants.BusinessTypeEnum.FEESTYPE_ACCURACYERROR.getValue(),
                "RightsRepaymentRecord", VisiableEnum.DISPLAY.getValue(), repaymentRecord.getRepaymentRecordId(),
                AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                systemAccountId, now, descIncome, false, callBack);
        avcq.addAccountValueChanged(avcIncome);

        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);

        taskExecLog.setLogInfo("平台补差：" + sd.getAmount().toPlainString());
        logger.debug("****平台补差完毕****");
        return null;
    }
}