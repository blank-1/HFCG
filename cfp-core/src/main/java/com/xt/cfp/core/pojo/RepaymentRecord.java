package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Renyulin on 14-4-21 下午3:31.
 */
public class RepaymentRecord {
    private long repaymentRecordId;
    private long repaymentPlanId;
    private long customerAccountId;
    private long loanApplicationId;
    private long payId;
    private Date faceDate;
    private BigDecimal factCalital = BigDecimal.valueOf(0);
    private BigDecimal factInterest = BigDecimal.valueOf(0);
    private BigDecimal factBalance = BigDecimal.valueOf(0);
    private BigDecimal repaymentFees = BigDecimal.valueOf(0);
    private char distributeStatus;
    private char isDelay;
    private int delayDays;
    private BigDecimal depalFine = BigDecimal.ZERO;
    private char isEarly;
    
    private Date opertionDate;
    private Date financialRepaymentDate;


    public long getPayId() {
        return payId;
    }

    public void setPayId(long payId) {
        this.payId = payId;
    }

    /**
     * 是否迟还,1为迟还，0为未迟还
     */
    public static char ISDELAY_YES = '1';
    public static char ISDELAY_NO = '0';

    /**
     * 是否提前还：1为提前，0为未提前
     */
    public static char ISEARLY_YES = '1';
    public static char ISEARLY_NO = '0';

    public char getDistributeStatus() {
        return distributeStatus;
    }

    public void setDistributeStatus(char distributeStatus) {
        this.distributeStatus = distributeStatus;
    }

    public BigDecimal getRepaymentFees() {
        return repaymentFees;
    }

    public void setRepaymentFees(BigDecimal repaymentFees) {
        this.repaymentFees = repaymentFees;
    }

    public long getRepaymentRecordId() {
        return repaymentRecordId;
    }

    public void setRepaymentRecordId(long repaymentRecordId) {
        this.repaymentRecordId = repaymentRecordId;
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

    public Date getFaceDate() {
        return faceDate;
    }

    public void setFaceDate(Date faceDate) {
        this.faceDate = faceDate;
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

    public char getIsDelay() {
        return isDelay;
    }

    public void setIsDelay(char isDelay) {
        this.isDelay = isDelay;
    }

    public int getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(int delayDays) {
        this.delayDays = delayDays;
    }

    public BigDecimal getDepalFine() {
        return depalFine;
    }

    public void setDepalFine(BigDecimal depalFine) {
        this.depalFine = depalFine;
    }

    public char getIsEarly() {
        return isEarly;
    }

    public void setIsEarly(char isEarly) {
        this.isEarly = isEarly;
    }

	public Date getOpertionDate() {
		return opertionDate;
	}

	public void setOpertionDate(Date opertionDate) {
		this.opertionDate = opertionDate;
	}

	public Date getFinancialRepaymentDate() {
		return financialRepaymentDate;
	}

	public void setFinancialRepaymentDate(Date financialRepaymentDate) {
		this.financialRepaymentDate = financialRepaymentDate;
	}
    
    
}
