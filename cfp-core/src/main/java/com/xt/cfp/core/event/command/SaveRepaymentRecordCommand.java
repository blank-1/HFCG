package com.xt.cfp.core.event.command;


import com.xt.cfp.core.event.Command;
import com.xt.cfp.core.event.pojo.TaskExecLog;
import com.xt.cfp.core.event.pojo.TaskInfo;
import com.xt.cfp.core.pojo.RepaymentRecord;
import com.xt.cfp.core.service.RepaymentRecordService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Renyulin on 14-6-12 下午6:55.
 */
public class SaveRepaymentRecordCommand extends Command {
    private RepaymentRecord repaymentRecord;
    private RepaymentRecordService repaymentRecordService;
    private boolean newRecord;

    public SaveRepaymentRecordCommand(RepaymentRecord repaymentRecord, RepaymentRecordService repaymentRecordService, boolean newRecord) {
        this.repaymentRecord = repaymentRecord;
        this.repaymentRecordService = repaymentRecordService;
        this.newRecord = newRecord;
    }

    @Override
    public Object execute() throws Exception {
        repaymentRecord.setFactBalance(repaymentRecord.getFactInterest().add(repaymentRecord.getFactCalital().add(repaymentRecord.getRepaymentFees().add(repaymentRecord.getDepalFine()))));
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_FAILURE);
        taskExecLog.setExecTime(new Date());
        if (newRecord) {
            repaymentRecordService.addRepaymentRecord(repaymentRecord);
            taskExecLog.setTaskId(TaskInfo.TASKID_SAVEREPAYMENTRECORD);
        } else {

//            RepaymentRecord updateRecord = new RepaymentRecord();
//            updateRecord.setRepaymentRecordId(repaymentRecord.getRepaymentRecordId());
//            updateRecord.setFaceDate(new Date());
//            updateRecord.setFactCalital(repaymentRecord.getFactCalital());
//            updateRecord.setFactInterest(repaymentRecord.getFactInterest());
//            updateRecord.setFactBalance(repaymentRecord.getFactBalance());
//            updateRecord.setRepaymentFees(repaymentRecord.getRepaymentFees());
//            updateRecord.setIsDelay(repaymentRecord.getIsDelay());
//            updateRecord.setDelayDays(repaymentRecord.getDelayDays());
//            updateRecord.setDepalFine(repaymentRecord.getDepalFine());
//            updateRecord.setIsEarly(repaymentRecord.getIsEarly());
//            updateRecord.setPayId(repaymentRecord.getPayId());
            repaymentRecordService.update(repaymentRecord);
            taskExecLog.setTaskId(TaskInfo.TASKID_UPDATEREPAYMENTRECORD);
        }
        taskExecLog.setExecResult(TaskExecLog.EXECRESULT_SUCCESS);

        taskExecLog.setLogInfo("保存/修改还款记录");
        return null;
    }
}
