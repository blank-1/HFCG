package com.xt.cfp.core.pojo;

import java.util.Date;

public class BondSource {
    private Long bondSourceId;

    private String sourceName;

    private String locatioin;

    private String contactPersion;

    private String contactPhone;

    private String status;

    private Date createTime;

    private Long userId;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBondSourceId() {
        return bondSourceId;
    }

    public void setBondSourceId(Long bondSourceId) {
        this.bondSourceId = bondSourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName == null ? null : sourceName.trim();
    }

    public String getLocatioin() {
        return locatioin;
    }

    public void setLocatioin(String locatioin) {
        this.locatioin = locatioin == null ? null : locatioin.trim();
    }

    public String getContactPersion() {
        return contactPersion;
    }

    public void setContactPersion(String contactPersion) {
        this.contactPersion = contactPersion == null ? null : contactPersion.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}