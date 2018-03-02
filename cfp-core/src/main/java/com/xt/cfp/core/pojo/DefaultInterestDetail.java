package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Renyulin on 14-4-21 上午11:20.
 */
public class DefaultInterestDetail {
    private long defaultInterestDetailId;
    private long repaymentPlanId;
    private long customerAccountId;
    private long loanApplicationId;
    private Date interestDate;
    private BigDecimal interestCapital;
    private BigDecimal interestRatio;

    private BigDecimal interestBalance;
    private BigDecimal interestBalance2;
    private BigDecimal repaymentBalance;
    private BigDecimal reductionBalance = BigDecimal.valueOf(0);
    private char repaymentState;

    private String interestDateStr ;
    /**
     * 逾期罚息状态：1为未还清，2为已还清，3为减免
     */
    public static final char REPAYMENTSTATE_UNCOMPLETE = '1';
    public static final char REPAYMENTSTATE_COMPLETED = '2';
    public static final char REPAYMENTSTATE_WAIVER='3';


    public BigDecimal getInterestBalance2() {
        return interestBalance2;
    }

    public void setInterestBalance2(BigDecimal interestBalance2) {
        this.interestBalance2 = interestBalance2;
    }

    public long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public long getDefaultInterestDetailId() {
        return defaultInterestDetailId;
    }

    public void setDefaultInterestDetailId(long defaultInterestDetailId) {
        this.defaultInterestDetailId = defaultInterestDetailId;
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

    public Date getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(Date interestDate) {
        this.interestDate = interestDate;
    }

    public BigDecimal getInterestCapital() {
        return interestCapital;
    }

    public void setInterestCapital(BigDecimal interestCapital) {
        this.interestCapital = interestCapital;
    }

    public BigDecimal getInterestRatio() {
        return interestRatio;
    }

    public void setInterestRatio(BigDecimal interestRatio) {
        this.interestRatio = interestRatio;
    }

    public BigDecimal getInterestBalance() {
        return interestBalance;
    }

    public void setInterestBalance(BigDecimal interestBalance) {
        this.interestBalance = interestBalance;
    }

    public BigDecimal getRepaymentBalance() {
        return repaymentBalance;
    }

    public void setRepaymentBalance(BigDecimal repaymentBalance) {
        this.repaymentBalance = repaymentBalance;
    }

    public char getRepaymentState() {
        return repaymentState;
    }

    public void setRepaymentState(char repaymentState) {
        this.repaymentState = repaymentState;
    }

	public BigDecimal getReductionBalance() {
		return reductionBalance;
	}

	public void setReductionBalance(BigDecimal reductionBalance) {
		this.reductionBalance = reductionBalance;
	}

	public String getInterestDateStr() {
		return interestDateStr;
	}

	public void setInterestDateStr(String interestDateStr) {
		this.interestDateStr = interestDateStr;
	}
    
    
}
