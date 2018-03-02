package com.xt.cfp.core.pojo;

import java.util.Date;

/**
 * 加息券使用历史表
 */
public class RateUsageHistory {
    private Long rateUsageHistoryId;

    private Long rateUserId;

    private Long userId;

    private Long lendOrderId;

    private Long loanApplicationId;

    private Integer thisUsedTimes;

    private Integer surplusTimes;

    private String status;

    private Date createTime;

    private Date updateTime;

    public Long getRateUsageHistoryId() {
        return rateUsageHistoryId;
    }

    public void setRateUsageHistoryId(Long rateUsageHistoryId) {
        this.rateUsageHistoryId = rateUsageHistoryId;
    }

    public Long getRateUserId() {
        return rateUserId;
    }

    public void setRateUserId(Long rateUserId) {
        this.rateUserId = rateUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(Long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public Integer getThisUsedTimes() {
        return thisUsedTimes;
    }

    public void setThisUsedTimes(Integer thisUsedTimes) {
        this.thisUsedTimes = thisUsedTimes;
    }

    public Integer getSurplusTimes() {
        return surplusTimes;
    }

    public void setSurplusTimes(Integer surplusTimes) {
        this.surplusTimes = surplusTimes;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}