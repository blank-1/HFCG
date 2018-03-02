package com.xt.cfp.core.pojo;

import java.util.Date;
/**
 * 抵押车变更记录快照表
 */
public class CarChangeSnapshot {
    private Long changeId;
    private Long beforeId;
    private Long afterId;
    private Long changeReason;
    private Date changeTime;

    public Long getChangeId() {
        return changeId;
    }

    public void setChangeId(Long changeId) {
        this.changeId = changeId;
    }

    public Long getBeforeId() {
        return beforeId;
    }

    public void setBeforeId(Long beforeId) {
        this.beforeId = beforeId;
    }

    public Long getAfterId() {
        return afterId;
    }

    public void setAfterId(Long afterId) {
        this.afterId = afterId;
    }

    public Long getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(Long changeReason) {
        this.changeReason = changeReason;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }
}