package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权还款明细
 */
public class RightsRepaymentDetail {
    private long rightsRepaymentDetailId;//分配还款ID[主键]
    private long creditorRightsId;//债权ID[外键]
    private long repaymentPlanId;//还款计划ID[外键]
    private long lendOrderId;//出借产品订单ID[外键]
    private long loanAccountId;//借款还款账户ID
    private long lendAccountId;//出借资金账户ID
    private long repaymentRecordId;//还款记录ID
    private long loanApplicationId;//借款申请ID
    private int sectionCode;//期号
    private BigDecimal proportion;//出资占比
    private Date repaymentDayPlanned;//计划还款日期
    private BigDecimal shouldCapital = BigDecimal.ZERO;//应还本金
    private BigDecimal shouldInterest = BigDecimal.ZERO;//应还利息
    private BigDecimal shouldBalance = BigDecimal.ZERO;//应还总额
    private BigDecimal shouldCapital2 = BigDecimal.ZERO;//应还本金2
    private BigDecimal shouldInterest2 = BigDecimal.ZERO;//应还利息2
    private BigDecimal shouldBalance2 = BigDecimal.ZERO;//应还总额2
    private BigDecimal factCalital = BigDecimal.ZERO;//实还本金
    private BigDecimal factInterest = BigDecimal.ZERO;//实还利息
    private BigDecimal factBalance = BigDecimal.ZERO;//实还总额
    private char isPayOff;//是否已还清
    private char isDelay;//是否迟还
    private BigDecimal depalFine = BigDecimal.ZERO;//迟还利息
    private int delayDays;//迟还天数

    private char rightsDetailState = '0';//债权明细状态


    //辅助字段
    private BigDecimal shouldFee;//代缴费用
    private BigDecimal defaultInterest;//罚息
    private String repaymentDayDisplay;
    
    //常量字段
    public final static String INTEREST="interest" ;
    public final static String CAPITAL="capital";
    public final static String FEES="fees";
    public final static String AWARDS="awards";

    public BigDecimal getShouldFee() {
        return shouldFee;
    }

    public void setShouldFee(BigDecimal shouldFee) {
        this.shouldFee = shouldFee;
    }

    /**
     * 是否已还清：1为还清，0为未还清
     */
    public static final char ISPAYOFF_YES = '1';
    public static final char ISPAYOFF_NO = '0';

    /**
     * 是否迟还：1为迟还，0为未迟还
     */
    public static final char ISDELAY_YES = '1';
    public static final char ISDELAY_NO = '0';

    /**
     * 还款计划状态：0未还款，1部分还款，2已还清，3逾期，4提前还款,5已转出,6平台垫付本金，7平台垫付利息，8平台垫付本息，9已删除
     */
    public static final char RIGHTSDETAILSTATE_UNCOMPLETE = '0';
    public static final char RIGHTSDETAILSTATE_PART = '1';
    public static final char RIGHTSDETAILSTATE_COMPLETE = '2';
    public static final char RIGHTSDETAILSTATE_DEFAULT = '3';
    public static final char RIGHTSDETAILSTATE_BEFORE_COMPLETE = '4';
    public static final char RIGHTSDETAILSTATE_TURNOUT = '5';
    public static final char RIGHTSDETAILSTATE_SYSTEMPREPAY_CAPITAL = '6';
    public static final char RIGHTSDETAILSTATE_SYSTEMPREPAY_INTEREST = '7';
    public static final char RIGHTSDETAILSTATE_SYSTEMPREPAY_BALANCE = '8';
    public static final char RIGHTSDETAILSTATE_DELETED = '9';

    public BigDecimal getDefaultInterest() {
        return defaultInterest;
    }

    public void setDefaultInterest(BigDecimal defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
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

    public char getRightsDetailState() {
        return rightsDetailState;
    }

    public void setRightsDetailState(char rightsDetailState) {
        this.rightsDetailState = rightsDetailState;
    }

    public int getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(int sectionCode) {
        this.sectionCode = sectionCode;
    }

    public long getRightsRepaymentDetailId() {
        return rightsRepaymentDetailId;
    }

    public void setRightsRepaymentDetailId(long rightsRepaymentDetailId) {
        this.rightsRepaymentDetailId = rightsRepaymentDetailId;
    }

    public long getCreditorRightsId() {
        return creditorRightsId;
    }

    public void setCreditorRightsId(long creditorRightsId) {
        this.creditorRightsId = creditorRightsId;
    }

    public long getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(long repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }

    public long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public long getLoanAccountId() {
        return loanAccountId;
    }

    public void setLoanAccountId(long loanAccountId) {
        this.loanAccountId = loanAccountId;
    }

    public long getLendAccountId() {
        return lendAccountId;
    }

    public void setLendAccountId(long lendAccountId) {
        this.lendAccountId = lendAccountId;
    }

    public long getRepaymentRecordId() {
        return repaymentRecordId;
    }

    public void setRepaymentRecordId(long repaymentRecordId) {
        this.repaymentRecordId = repaymentRecordId;
    }

    public long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public Date getRepaymentDayPlanned() {
        return repaymentDayPlanned;
    }

    public void setRepaymentDayPlanned(Date repaymentDayPlanned) {
        this.repaymentDayPlanned = repaymentDayPlanned;
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
        if (factCalital==null)
            return BigDecimal.ZERO;
        return factCalital;
    }

    public void setFactCalital(BigDecimal factCalital) {
        this.factCalital = factCalital;
    }

    public BigDecimal getFactInterest() {
        if (factInterest==null)
            return BigDecimal.ZERO;
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

    public char getIsPayOff() {
        return isPayOff;
    }

    public void setIsPayOff(char isPayOff) {
        this.isPayOff = isPayOff;
    }


    public char getIsDelay() {
        return isDelay;
    }

    public void setIsDelay(char isDelay) {
        this.isDelay = isDelay;
    }

    public BigDecimal getDepalFine() {
        return depalFine;
    }

    public void setDepalFine(BigDecimal depalFine) {
        this.depalFine = depalFine;
    }

    public int getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(int delayDays) {
        this.delayDays = delayDays;
    }

	public String getRepaymentDayDisplay() {
		return repaymentDayDisplay;
	}

	public void setRepaymentDayDisplay(String repaymentDayDisplay) {
		this.repaymentDayDisplay = repaymentDayDisplay;
	}
    
    
}
