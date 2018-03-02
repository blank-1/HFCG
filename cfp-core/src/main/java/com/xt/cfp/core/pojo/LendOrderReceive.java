package com.xt.cfp.core.pojo;

import org.apache.ibatis.type.BigDecimalTypeHandler;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ren yulin on 15-7-8.
 */
public class LendOrderReceive {
    private long receiveId;
    private long lendOrderId;
    private int sectionCode;
    private BigDecimal shouldCapital;
    private BigDecimal shouldInterest;
    private BigDecimal shouldCapital2;
    private BigDecimal shouldInterest2;

    private Date receiveDate;
    private BigDecimal factCapital = BigDecimal.valueOf(0);
    private BigDecimal factInterest = BigDecimal.valueOf(0);
    private char receiveState;
    private BigDecimal receiveBalance = BigDecimal.valueOf(0);

    public long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(long receiveId) {
        this.receiveId = receiveId;
    }

    public long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public int getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(int sectionCode) {
        this.sectionCode = sectionCode;
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

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public BigDecimal getFactCapital() {
        return factCapital;
    }

    public void setFactCapital(BigDecimal factCapital) {
        this.factCapital = factCapital;
    }

    public BigDecimal getFactInterest() {
        return factInterest;
    }

    public void setFactInterest(BigDecimal factInterest) {
        this.factInterest = factInterest;
    }

    public char getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(char receiveState) {
        this.receiveState = receiveState;
    }

    public BigDecimal getReceiveBalance() {
        return receiveBalance;
    }

    public void setReceiveBalance(BigDecimal receiveBalance) {
        this.receiveBalance = receiveBalance;
    }
}
