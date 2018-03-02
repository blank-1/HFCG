package com.xt.cfp.core.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款计划
 */
public class RepaymentPlan implements Serializable {
    private long repaymentPlanId;//还款计划ID
    private long customerAccountId;//还款账户ID
    private long loanApplicationId;//借款申请ID
    private int sectionCode;//期号
    private Date repaymentDay;//还款日	
    private Date startDate;//开始日期
    private BigDecimal shouldCapital = BigDecimal.ZERO;//应还本金
    private BigDecimal shouldInterest = BigDecimal.ZERO;//应还利息
    private BigDecimal shouldBalance = BigDecimal.ZERO;//应还总额
    private BigDecimal shouldCapital2 = BigDecimal.ZERO;//应还本金2
    private BigDecimal shouldInterest2 = BigDecimal.ZERO;//应还利息2
    private BigDecimal shouldBalance2 = BigDecimal.ZERO;//应还总额2

    private BigDecimal factCalital = BigDecimal.ZERO;//实还本金
    private BigDecimal factInterest = BigDecimal.ZERO;//实还利息
    private BigDecimal factBalance = BigDecimal.ZERO;//实还总额
    private char planState;//状态
    private char channelType;//来源渠道：线下/线上
    
    // 辅助字段
    private String repaymentDayDisplay;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public BigDecimal getShouldCapital2() {
        return shouldCapital2;
    }

    public void setShouldCapital2(BigDecimal shouldCapital2) {
        this.shouldCapital2 = shouldCapital2;
    }

    public BigDecimal getShouldInterest2() {
        return shouldInterest2;
    }

    public void setShouldInterest2(BigDecimal shouldInterest2) {
        this.shouldInterest2 = shouldInterest2;
    }

    public BigDecimal getShouldBalance2() {
        return shouldBalance2;
    }

    public void setShouldBalance2(BigDecimal shouldBalance2) {
        this.shouldBalance2 = shouldBalance2;
    }

    public long getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(long repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }

    public long getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(long customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    public long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public int getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(int sectionCode) {
        this.sectionCode = sectionCode;
    }

    public Date getRepaymentDay() {
        return repaymentDay;
    }

    public void setRepaymentDay(Date repaymentDay) {
        this.repaymentDay = repaymentDay;
    }

    public BigDecimal getShouldCapital() {
        return shouldCapital;
    }

    public void setShouldCapital(BigDecimal shouldCapital) {
        this.shouldCapital = shouldCapital;
    }

    public BigDecimal getShouldInterest() {
        return shouldInterest;
    }

    public void setShouldInterest(BigDecimal shouldInterest) {
        this.shouldInterest = shouldInterest;
    }

    public BigDecimal getShouldBalance() {
        return shouldBalance;
    }

    public void setShouldBalance(BigDecimal shouldBalance) {
        this.shouldBalance = shouldBalance;
    }

    public BigDecimal getFactCalital() {
        return factCalital;
    }

    public void setFactCalital(BigDecimal factCalital) {
        this.factCalital = factCalital;
    }

    public BigDecimal getFactInterest() {
        return factInterest;
    }

    public void setFactInterest(BigDecimal factInterest) {
        this.factInterest = factInterest;
    }

    public BigDecimal getFactBalance() {
        return factBalance;
    }

    public void setFactBalance(BigDecimal factBalance) {
        this.factBalance = factBalance;
    }

    public char getPlanState() {
        return planState;
    }

    public void setPlanState(char planState) {
        this.planState = planState;
    }

    public char getChannelType() {
        return channelType;
    }

    public void setChannelType(char channelType) {
        this.channelType = channelType;
    }

	public String getRepaymentDayDisplay() {
		return repaymentDayDisplay;
	}

	public void setRepaymentDayDisplay(String repaymentDayDisplay) {
		this.repaymentDayDisplay = repaymentDayDisplay;
	}
    
}
