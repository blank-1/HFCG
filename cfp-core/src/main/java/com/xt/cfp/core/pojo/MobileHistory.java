package com.xt.cfp.core.pojo;

import java.util.Date;

public class MobileHistory {
    private Long hisId;

    private Long userId;

    private String beforeNo;

    private String afterNo;

    private Date updateTime;

    private String updateSource;

    public Long getHisId() {
        return hisId;
    }

    public void setHisId(Long hisId) {
        this.hisId = hisId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBeforeNo() {
        return beforeNo;
    }

    public void setBeforeNo(String beforeNo) {
        this.beforeNo = beforeNo == null ? null : beforeNo.trim();
    }

    public String getAfterNo() {
        return afterNo;
    }

    public void setAfterNo(String afterNo) {
        this.afterNo = afterNo == null ? null : afterNo.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateSource() {
        return updateSource;
    }

    public void setUpdateSource(String updateSource) {
        this.updateSource = updateSource == null ? null : updateSource.trim();
    }
}