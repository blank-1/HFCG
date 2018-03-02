package com.xt.cfp.core.event.pojo;

import java.util.Date;

/**
 * Created by Renyulin on 14-6-13 上午11:13.
 */
public class TaskExecLog {
    private long taskExecLogId;
    private long eventTriggerInfoId;
    private long taskId;
    private Date execTime;
    private char execResult;
    private String logInfo;
    private String taskName;

    public static char EXECRESULT_SUCCESS = '1';
    public static char EXECRESULT_FAILURE = '0';

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getTaskExecLogId() {
        return taskExecLogId;
    }

    public void setTaskExecLogId(long taskExecLogId) {
        this.taskExecLogId = taskExecLogId;
    }

    public long getEventTriggerInfoId() {
        return eventTriggerInfoId;
    }

    public void setEventTriggerInfoId(long eventTriggerInfoId) {
        this.eventTriggerInfoId = eventTriggerInfoId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Date getExecTime() {
        return execTime;
    }

    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }

    public char getExecResult() {
        return execResult;
    }

    public void setExecResult(char execResult) {
        this.execResult = execResult;
    }

    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }
}
