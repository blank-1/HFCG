package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class UserAccount {
    private Long accId;

    private Long userId;

    private String accTypeCode;

    private BigDecimal value;

    private BigDecimal value2;

    private BigDecimal availValue;

    private BigDecimal availValue2;

    private BigDecimal frozeValue;

    private BigDecimal frozeValue2;

    private String accStatus;

    private Date createTime;

    public Long getAccId() {
        return accId;
    }

    public void setAccId(Long accId) {
        this.accId = accId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccTypeCode() {
        return accTypeCode;
    }

    public void setAccTypeCode(String accTypeCode) {
        this.accTypeCode = accTypeCode == null ? null : accTypeCode.trim();
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue2() {
        return value2;
    }

    public void setValue2(BigDecimal value2) {
        this.value2 = value2;
    }

    public BigDecimal getAvailValue() {
        return availValue;
    }

    public void setAvailValue(BigDecimal availValue) {
        this.availValue = availValue;
    }

    public BigDecimal getAvailValue2() {
        return availValue2;
    }

    public void setAvailValue2(BigDecimal availValue2) {
        this.availValue2 = availValue2;
    }

    public BigDecimal getFrozeValue() {
        return frozeValue;
    }

    public void setFrozeValue(BigDecimal frozeValue) {
        this.frozeValue = frozeValue;
    }

    public BigDecimal getFrozeValue2() {
        return frozeValue2;
    }

    public void setFrozeValue2(BigDecimal frozeValue2) {
        this.frozeValue2 = frozeValue2;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus == null ? null : accStatus.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}