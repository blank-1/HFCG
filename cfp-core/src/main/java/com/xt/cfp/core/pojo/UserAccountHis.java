package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class UserAccountHis {
    private BigDecimal hisId;

    private Long accId;

    private String accTypeCode;

    private BigDecimal changeValue;

    private BigDecimal changeValue2;

    private String busType;

    private String changeType;

    private String isVisible;

    private BigDecimal valueAfter;

    private BigDecimal valueAfter2;

    private BigDecimal availValueAfter;

    private BigDecimal availValueAfter2;

    private BigDecimal frozeValueAfter;

    private BigDecimal frozeValueAfter2;

    private String statusAfter;

    private BigDecimal valueBefore;

    private BigDecimal valueBefore2;

    private BigDecimal availValueBefore;

    private BigDecimal availValueBefore2;

    private BigDecimal frozeValueBefore;

    private BigDecimal frozeValueBefore2;

    private String statusBefore;

    private Date changeTime;

    private String desc;

    private String fromType;

    private Long fromId;

    private String ownerType;

    private Long ownerId;


    private BigDecimal changeValueBak;

    public BigDecimal getChangeValueBak() {
        return changeValue;
    }

    public void setChangeValueBak(BigDecimal changeValueBak) {
        this.changeValueBak = changeValueBak;
    }

    public BigDecimal getHisId() {
        return hisId;
    }

    public void setHisId(BigDecimal hisId) {
        this.hisId = hisId;
    }

    public Long getAccId() {
        return accId;
    }

    public void setAccId(Long accId) {
        this.accId = accId;
    }

    public String getAccTypeCode() {
        return accTypeCode;
    }

    public void setAccTypeCode(String accTypeCode) {
        this.accTypeCode = accTypeCode == null ? null : accTypeCode.trim();
    }

    public BigDecimal getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(BigDecimal changeValue) {
        this.changeValue = changeValue;
    }

    public BigDecimal getChangeValue2() {
        return changeValue2;
    }

    public void setChangeValue2(BigDecimal changeValue2) {
        this.changeValue2 = changeValue2;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType == null ? null : busType.trim();
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType == null ? null : changeType.trim();
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible == null ? null : isVisible.trim();
    }

    public BigDecimal getValueAfter() {
        return valueAfter;
    }

    public void setValueAfter(BigDecimal valueAfter) {
        this.valueAfter = valueAfter;
    }

    public BigDecimal getValueAfter2() {
        return valueAfter2;
    }

    public void setValueAfter2(BigDecimal valueAfter2) {
        this.valueAfter2 = valueAfter2;
    }

    public BigDecimal getAvailValueAfter() {
        return availValueAfter;
    }

    public void setAvailValueAfter(BigDecimal availValueAfter) {
        this.availValueAfter = availValueAfter;
    }

    public BigDecimal getAvailValueAfter2() {
        return availValueAfter2;
    }

    public void setAvailValueAfter2(BigDecimal availValueAfter2) {
        this.availValueAfter2 = availValueAfter2;
    }

    public BigDecimal getFrozeValueAfter() {
        return frozeValueAfter;
    }

    public void setFrozeValueAfter(BigDecimal frozeValueAfter) {
        this.frozeValueAfter = frozeValueAfter;
    }

    public BigDecimal getFrozeValueAfter2() {
        return frozeValueAfter2;
    }

    public void setFrozeValueAfter2(BigDecimal frozeValueAfter2) {
        this.frozeValueAfter2 = frozeValueAfter2;
    }

    public String getStatusAfter() {
        return statusAfter;
    }

    public void setStatusAfter(String statusAfter) {
        this.statusAfter = statusAfter == null ? null : statusAfter.trim();
    }

    public BigDecimal getValueBefore() {
        return valueBefore;
    }

    public void setValueBefore(BigDecimal valueBefore) {
        this.valueBefore = valueBefore;
    }

    public BigDecimal getValueBefore2() {
        return valueBefore2;
    }

    public void setValueBefore2(BigDecimal valueBefore2) {
        this.valueBefore2 = valueBefore2;
    }

    public BigDecimal getAvailValueBefore() {
        return availValueBefore;
    }

    public void setAvailValueBefore(BigDecimal availValueBefore) {
        this.availValueBefore = availValueBefore;
    }

    public BigDecimal getAvailValueBefore2() {
        return availValueBefore2;
    }

    public void setAvailValueBefore2(BigDecimal availValueBefore2) {
        this.availValueBefore2 = availValueBefore2;
    }

    public BigDecimal getFrozeValueBefore() {
        return frozeValueBefore;
    }

    public void setFrozeValueBefore(BigDecimal frozeValueBefore) {
        this.frozeValueBefore = frozeValueBefore;
    }

    public BigDecimal getFrozeValueBefore2() {
        return frozeValueBefore2;
    }

    public void setFrozeValueBefore2(BigDecimal frozeValueBefore2) {
        this.frozeValueBefore2 = frozeValueBefore2;
    }

    public String getStatusBefore() {
        return statusBefore;
    }

    public void setStatusBefore(String statusBefore) {
        this.statusBefore = statusBefore == null ? null : statusBefore.trim();
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType == null ? null : fromType.trim();
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType == null ? null : ownerType.trim();
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}