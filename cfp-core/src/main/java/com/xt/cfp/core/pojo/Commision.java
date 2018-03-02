package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Commision {
    private Long commisionId;

    private Long lendOrderId;

    private Long userId;

    private Long rulesId;

    private Long lendProductId;

    private BigDecimal balance;

    private Date changeDate;

    private String userLevel;

    private BigDecimal comiRate;

    private String comiRatioType;

    private BigDecimal comiRatioBalance;

    private String lendOrderName ;
    
    public Long getCommisionId() {
        return commisionId;
    }

    public void setCommisionId(Long commisionId) {
        this.commisionId = commisionId;
    }

    public Long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(Long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRulesId() {
        return rulesId;
    }

    public void setRulesId(Long rulesId) {
        this.rulesId = rulesId;
    }

    public Long getLendProductId() {
        return lendProductId;
    }

    public void setLendProductId(Long lendProductId) {
        this.lendProductId = lendProductId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel == null ? null : userLevel.trim();
    }

    public BigDecimal getComiRate() {
        return comiRate;
    }

    public void setComiRate(BigDecimal comiRate) {
        this.comiRate = comiRate;
    }

    public String getComiRatioType() {
        return comiRatioType;
    }

    public void setComiRatioType(String comiRatioType) {
        this.comiRatioType = comiRatioType == null ? null : comiRatioType.trim();
    }

    public BigDecimal getComiRatioBalance() {
        return comiRatioBalance;
    }

    public void setComiRatioBalance(BigDecimal comiRatioBalance) {
        this.comiRatioBalance = comiRatioBalance;
    }

	public String getLendOrderName() {
		return lendOrderName;
	}

	public void setLendOrderName(String lendOrderName) {
		this.lendOrderName = lendOrderName;
	}
    
}