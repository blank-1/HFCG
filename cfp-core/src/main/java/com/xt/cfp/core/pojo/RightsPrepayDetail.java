package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ren yulin on 15-7-9.
 */
public class RightsPrepayDetail {

    private long prepayDetailId;
    private long creditorRightsId; //垫付债权ID(即垫付的哪个债权)
    private long rightsRepaymentDetailId;//债权明细（即分配还款ID,垫付的哪一期的债权）

    private long  crHistoryId;
    private long accountId;
    private BigDecimal prepayBalance;
    private BigDecimal prepayBalance2;
    private BigDecimal repaymentBalance;
    private char isPayOff;
    private Date prepayTime;
    private Date willBackTime;

    public long getPrepayDetailId() {
        return prepayDetailId;
    }

    public void setPrepayDetailId(long prepayDetailId) {
        this.prepayDetailId = prepayDetailId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getPrepayBalance() {
        return prepayBalance;
    }

    public void setPrepayBalance(BigDecimal prepayBalance) {
        this.prepayBalance = prepayBalance;
    }

    public BigDecimal getPrepayBalance2() {
        return prepayBalance2;
    }

    public void setPrepayBalance2(BigDecimal prepayBalance2) {
        this.prepayBalance2 = prepayBalance2;
    }

    public BigDecimal getRepaymentBalance() {
        return repaymentBalance;
    }

    public void setRepaymentBalance(BigDecimal repaymentBalance) {
        this.repaymentBalance = repaymentBalance;
    }

    public char getIsPayOff() {
        return isPayOff;
    }

    public void setIsPayOff(char isPayOff) {
        this.isPayOff = isPayOff;
    }

    public Date getPrepayTime() {
        return prepayTime;
    }

    public void setPrepayTime(Date prepayTime) {
        this.prepayTime = prepayTime;
    }

    public long getCreditorRightsId() {
        return creditorRightsId;
    }

    public void setCreditorRightsId(long creditorRightsId) {
        this.creditorRightsId = creditorRightsId;
    }

    public long getRightsRepaymentDetailId() {
        return rightsRepaymentDetailId;
    }

    public void setRightsRepaymentDetailId(long rightsRepaymentDetailId) {
        this.rightsRepaymentDetailId = rightsRepaymentDetailId;
    }

    public long getCrHistoryId() {
        return crHistoryId;
    }

    public void setCrHistoryId(long crHistoryId) {
        this.crHistoryId = crHistoryId;
    }

    public Date getWillBackTime() {
        return willBackTime;
    }

    public void setWillBackTime(Date willBackTime) {
        this.willBackTime = willBackTime;
    }
}
