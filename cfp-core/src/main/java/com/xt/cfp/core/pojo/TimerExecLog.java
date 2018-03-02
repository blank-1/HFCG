package com.xt.cfp.core.pojo;

import java.util.Date;

public class TimerExecLog {
    private Long logId;

    private String timerTypeName;

    private Date startExecTime;

    private Date endExecTime;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getTimerTypeName() {
        return timerTypeName;
    }

    public void setTimerTypeName(String timerTypeName) {
        this.timerTypeName = timerTypeName == null ? null : timerTypeName.trim();
    }

    public Date getStartExecTime() {
        return startExecTime;
    }

    public void setStartExecTime(Date startExecTime) {
        this.startExecTime = startExecTime;
    }

    public Date getEndExecTime() {
        return endExecTime;
    }

    public void setEndExecTime(Date endExecTime) {
        this.endExecTime = endExecTime;
    }
}