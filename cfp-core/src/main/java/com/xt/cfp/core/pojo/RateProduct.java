package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 加息券产品表
 */
public class RateProduct {
    private Long rateProductId;

    private String rateProductName;

    private BigDecimal rateValue;

    private String usageScenario;

    private String condition;

    private Integer usageTimes = 1;

    private Integer usageDuration = 0;

    private Date startDate;

    private Date endDate;

    private String isOverlay;

    private String usageRemark;

    private String status;

    private Date createTime;

    private Date lastUpdateTime;

    private Long adminId;

    public Long getRateProductId() {
        return rateProductId;
    }

    public void setRateProductId(Long rateProductId) {
        this.rateProductId = rateProductId;
    }

    public String getRateProductName() {
        return rateProductName;
    }

    public void setRateProductName(String rateProductName) {
        this.rateProductName = rateProductName == null ? null : rateProductName.trim();
    }

    public BigDecimal getRateValue() {
        return rateValue;
    }

    public void setRateValue(BigDecimal rateValue) {
        this.rateValue = rateValue;
    }

    public String getUsageScenario() {
        return usageScenario;
    }

    public void setUsageScenario(String usageScenario) {
        this.usageScenario = usageScenario == null ? null : usageScenario.trim();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition == null ? null : condition.trim();
    }

    public Integer getUsageTimes() {
        return usageTimes;
    }

    public void setUsageTimes(Integer usageTimes) {
        this.usageTimes = usageTimes;
    }

    public Integer getUsageDuration() {
        return usageDuration;
    }

    public void setUsageDuration(Integer usageDuration) {
        this.usageDuration = usageDuration;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getIsOverlay() {
        return isOverlay;
    }

    public void setIsOverlay(String isOverlay) {
        this.isOverlay = isOverlay == null ? null : isOverlay.trim();
    }

    public String getUsageRemark() {
        return usageRemark;
    }

    public void setUsageRemark(String usageRemark) {
        this.usageRemark = usageRemark == null ? null : usageRemark.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}