package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;

import com.xt.cfp.core.pojo.RateUsageHistory;

public class RateUsageHistoryVO extends RateUsageHistory {
	private String lendOrderName;
	private Integer timeLimit;
	private BigDecimal buyBalance;
	
	public String getLendOrderName() {
		return lendOrderName;
	}
	public void setLendOrderName(String lendOrderName) {
		this.lendOrderName = lendOrderName;
	}
	public Integer getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}
	public BigDecimal getBuyBalance() {
		return buyBalance;
	}
	public void setBuyBalance(BigDecimal buyBalance) {
		this.buyBalance = buyBalance;
	}

}
