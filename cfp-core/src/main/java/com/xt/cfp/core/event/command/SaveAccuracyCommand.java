package com.xt.cfp.core.event.command;


import com.xt.cfp.core.common.DescTemplate;
import com.xt.cfp.core.constants.AccountConstants;
import com.xt.cfp.core.constants.VisiableEnum;
import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.LoanApplication;
import com.xt.cfp.core.pojo.RepaymentRecord;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;
import com.xt.cfp.core.pojo.SupplementaryDifference;
import com.xt.cfp.core.service.RightsPrepayDetailService;
import com.xt.cfp.core.service.UserAccountService;
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
 * Created by Renyulin on 14-7-4 下午2:00.
 */
public class SaveAccuracyCommand extends Command {
    private Logger logger = Logger.getLogger("eventTaskLogger");
    private long systemAccountId;
    private BigDecimal accuracy;
    private RightsRepaymentDetail rightsRepaymentDetail;
    private RepaymentRecord repaymentRecord;

    private AccountValueChangedQueue avcq;

    public SaveAccuracyCommand(long systemAccountId, BigDecimal accuracy, RightsRepaymentDetail rightsRepaymentDetail,
                               AccountValueChangedQueue avcq,RepaymentRecord repaymentRecord) {
        this.systemAccountId = systemAccountId;
        this.accuracy = accuracy;
        this.repaymentRecord = repaymentRecord;
        this.rightsRepaymentDetail = rightsRepaymentDetail;
        this.avcq = avcq;
    }

    @Override
    public Object execute() throws Exception {
        logger.debug("****开始保存精度误差****");
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        taskExecLog.setTaskId(TaskInfo.TASKID_SAVEACCURACY);

        Date now = new Date();
        BigDecimal accuracy2 = BigDecimalUtil.down(accuracy, 2);

        //记录平台补差
        SupplementaryDifference sd = new SupplementaryDifference();
        sd.setCreateTime(new Date());
        sd.setAmount(accuracy);
        sd.setRepaymentRecordId(repaymentRecord.getRepaymentRecordId());
        sd.setAccId(systemAccountId);
        sd.setType("0");//+收入

        CallBack callBack = new RepaymentCallBack(sd);

        String descIncome = StringUtils.t2s(DescTemplate.desc.AccountChanngedDesc.ACCURACYERROR_INCOME,accuracy.toPlainString(),accuracy2.toPlainString());
        AccountValueChanged avcIncome = new AccountValueChanged(systemAccountId,accuracy,
                accuracy, AccountConstants.AccountOperateEnum.INCOM.getValue(), AccountConstants.BusinessTypeEnum.FEESTYPE_ACCURACYERROR.getValue(),
                "RightsRepaymentDetail", VisiableEnum.DISPLAY.getValue(),rightsRepaymentDetail.getRightsRepaymentDetailId(),
                AccountConstants.AccountChangedTypeEnum.SYSTEM.getValue(),
                systemAccountId, now,descIncome,true,callBack);
        avcq.addAccountValueChanged(avcIncome);
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);

        taskExecLog.setLogInfo("精度误差。18位精度：" + accuracy.toPlainString() + ",2位精度:" + accuracy2.toPlainString());
        logger.debug("****保存精度误差完毕****");
        return null;
    }
}
