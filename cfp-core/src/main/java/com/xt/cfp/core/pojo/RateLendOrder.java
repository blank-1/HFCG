package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 加息出借订单中间表
 */
public class RateLendOrder {
    private Long rateLendOrderId;

    private Long lendOrderId;

    private Long rateUserId;

    private Long loanApplicationId;

    private String rateType;

    private String awardPoint;

    private BigDecimal rateValue;

    private String status;

    private Date createTime;

    public Long getRateLendOrderId() {
        return rateLendOrderId;
    }

    public void setRateLendOrderId(Long rateLendOrderId) {
        this.rateLendOrderId = rateLendOrderId;
    }

    public Long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(Long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public Long getRateUserId() {
        return rateUserId;
    }

    public void setRateUserId(Long rateUserId) {
        this.rateUserId = rateUserId;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType == null ? null : rateType.trim();
    }

    public String getAwardPoint() {
        return awardPoint;
    }

    public void setAwardPoint(String awardPoint) {
        this.awardPoint = awardPoint == null ? null : awardPoint.trim();
    }

    public BigDecimal getRateValue() {
        return rateValue;
    }

    public void setRateValue(BigDecimal rateValue) {
        this.rateValue = rateValue;
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
}