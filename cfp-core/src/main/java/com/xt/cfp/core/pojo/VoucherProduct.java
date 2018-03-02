package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class VoucherProduct {
    private Long voucherProductId;

    private Long adminId;

    private String voucherName;

    private String voucherType;

    private BigDecimal amount;

    private BigDecimal rate;

    private String cardinalType;

    private Date startDate;

    private Date endDate;

    private Short effectiveCount;

    private String usageScenario;

    private String isOverlay;

    private BigDecimal conditionAmount;

    private String status;

    private String voucherRemark;

    private String isExperience;


    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public Long getVoucherProductId() {
        return voucherProductId;
    }

    public void setVoucherProductId(Long voucherProductId) {
        this.voucherProductId = voucherProductId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }


    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType == null ? null : voucherType.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getCardinalType() {
        return cardinalType;
    }

    public void setCardinalType(String cardinalType) {
        this.cardinalType = cardinalType == null ? null : cardinalType.trim();
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

    public Short getEffectiveCount() {
        return effectiveCount;
    }

    public void setEffectiveCount(Short effectiveCount) {
        this.effectiveCount = effectiveCount;
    }

    public String getUsageScenario() {
        return usageScenario;
    }

    public void setUsageScenario(String usageScenario) {
        this.usageScenario = usageScenario == null ? null : usageScenario.trim();
    }

    public String getIsOverlay() {
        return isOverlay;
    }

    public void setIsOverlay(String isOverlay) {
        this.isOverlay = isOverlay == null ? null : isOverlay.trim();
    }

    public BigDecimal getConditionAmount() {
        return conditionAmount;
    }

    public void setConditionAmount(BigDecimal conditionAmount) {
        this.conditionAmount = conditionAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getVoucherRemark() {
        return voucherRemark;
    }

    public void setVoucherRemark(String voucherRemark) {
        this.voucherRemark = voucherRemark == null ? null : voucherRemark.trim();
    }

    public String getIsExperience() {
        return isExperience;
    }

    public void setIsExperience(String isExperience) {
        this.isExperience = isExperience == null ? null : isExperience.trim();
    }
}