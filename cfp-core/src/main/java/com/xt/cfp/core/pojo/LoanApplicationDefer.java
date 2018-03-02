package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by duyaxing on 2014/9/2.
 */
public class LoanApplicationDefer {
    private long loanApplicationDeferId;
    private long loanApplicationId;
    private long deferLoanApplicationId;
    private long customerId;
    private long loanProductId;
    private String historyAgreementCode;
    private char useHistorySecurity;
    private char reductionState;
    private BigDecimal reductionBalance = BigDecimal.ZERO;
    private Date createTime;
    private Date confirmTime;
    private char deferState;
    private BigDecimal deferBalance = BigDecimal.ZERO;

    /**
     * 延期信息状态：1保存，2提交，3审核,4驳回
     */
    public static final char DEFERSTATE_SAVE = '1';
    public static final char DEFERSTATE_SUBMIT = '2';
    public static final char DEFERSTATE_CONFIRM = '3';
    public static final char DEFERSTATE_TURN_DOWN = '4';


    /**
     * 是否全部减免逾期费用状态：0免，1扣除
     */
    public static final char REDUCTIONSTATE_DEDUCTION_FREE = '0';
    public static final char REDUCTIONSTATE_DEDUCTION_ALL = '1';
    public static final char REDUCTIONSTATE_DEDUCTION_PART = '2';

    /**
     * 是否延用原来合同的保证金和预付利息：1是，2否
     */
    public static final char USEHISTORYSECURITY_TRUE = '1';
    public static final char USEHISTORYSECURITY_FALSE = '2';



    public long getDeferLoanApplicationId() {
        return deferLoanApplicationId;
    }

    public void setDeferLoanApplicationId(long deferLoanApplicationId) {
        this.deferLoanApplicationId = deferLoanApplicationId;
    }

    public long getLoanApplicationDeferId() {
        return loanApplicationDeferId;
    }

    public void setLoanApplicationDeferId(long loanApplicationDeferId) {
        this.loanApplicationDeferId = loanApplicationDeferId;
    }

    public long getLoanProductId() {
        return loanProductId;
    }

    public void setLoanProductId(long loanProductId) {
        this.loanProductId = loanProductId;
    }

    public long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getHistoryAgreementCode() {
        return historyAgreementCode;
    }

    public void setHistoryAgreementCode(String historyAgreementCode) {
        this.historyAgreementCode = historyAgreementCode;
    }

    public char getUseHistorySecurity() {
        return useHistorySecurity;
    }

    public void setUseHistorySecurity(char useHistorySecurity) {
        this.useHistorySecurity = useHistorySecurity;
    }

    public char getReductionState() {
        return reductionState;
    }

    public void setReductionState(char reductionState) {
        this.reductionState = reductionState;
    }

    public BigDecimal getReductionBalance() {
        return reductionBalance;
    }

    public void setReductionBalance(BigDecimal reductionBalance) {
        this.reductionBalance = reductionBalance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public char getDeferState() {
        return deferState;
    }

    public void setDeferState(char deferState) {
        this.deferState = deferState;
    }

    public BigDecimal getDeferBalance() {
        return deferBalance;
    }

    public void setDeferBalance(BigDecimal deferBalance) {
        this.deferBalance = deferBalance;
    }
}
