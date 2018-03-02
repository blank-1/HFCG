package com.xt.cfp.core.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class LoanFeesDetail implements Serializable {
    private long loanFeesDetailId;//借款申请费用明细ID
    private long feesItemId;
    private long loanApplicationFeesItemId;//借款申请费用Id
    private long loanApplicationId;//借款申请ID
    private char feesCycle;//收费周期
    private int sectionCode;//期号
    private BigDecimal fees;//费用金额
    private BigDecimal fees2;//费用金额2
    private BigDecimal paidFees;//已缴费用
    private BigDecimal reductionBalance = BigDecimal.ZERO;////减免金额
    private char feesState;//缴费状态
    private FeesItem  feesItem;

    /**
     * 缴费状态：1为未缴清，2为已缴清，3为减免
     */
    public static final char FEESSTATE_UNPAY='1';
    public static final char FEESSTATE_PAID='2';
    public static final char FEESSTATE_WAIVER='3';


    public long getLoanApplicationFeesItemId() {
        return loanApplicationFeesItemId;
    }

    public void setLoanApplicationFeesItemId(long loanApplicationFeesItemId) {
        this.loanApplicationFeesItemId = loanApplicationFeesItemId;
    }

    public int getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(int sectionCode) {
        this.sectionCode = sectionCode;
    }

    public char getFeesState() {
        return feesState;
    }

    public void setFeesState(char feesState) {
        this.feesState = feesState;
    }

    public BigDecimal getFees2() {
        return fees2;
    }

    public void setFees2(BigDecimal fees2) {
        this.fees2 = fees2;
    }

    @Deprecated

    public FeesItem getFeesItem() {
        return feesItem;
    }

    public BigDecimal getPaidFees() {
        return paidFees;
    }

    public void setPaidFees(BigDecimal paidFees) {
        this.paidFees = paidFees;
    }
    @Deprecated
    public void setFeesItem(FeesItem feesItem) {
        this.feesItem = feesItem;
    }

    public long getLoanFeesDetailId() {
        return loanFeesDetailId;
    }

    public void setLoanFeesDetailId(long loanFeesDetailId) {
        this.loanFeesDetailId = loanFeesDetailId;
    }

    @Deprecated
    public long getFeesItemId() {
        return feesItemId;
    }

    @Deprecated
    public void setFeesItemId(long feesItemId) {
        this.feesItemId = feesItemId;
    }

    public long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public char getFeesCycle() {
        return feesCycle;
    }

    public void setFeesCycle(char feesCycle) {
        this.feesCycle = feesCycle;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

	public BigDecimal getReductionBalance() {
		return reductionBalance;
	}

	public void setReductionBalance(BigDecimal reductionBalance) {
		this.reductionBalance = reductionBalance;
	}

    
}
