package com.xt.cfp.core.pojo;

import java.math.BigDecimal;

/**
 * Created by Renyulin on 14-4-3 下午4:34.
 */
public class LendOrderFeesDetail {
    private long lendOrderFeesDetailId;
    private long lendOrderId;
    private char chargePoint;
    private BigDecimal feesBalance = BigDecimal.ZERO;
    private BigDecimal feesBalance2 = BigDecimal.ZERO;

    private int sectionCode;
    private BigDecimal paidFees = BigDecimal.ZERO;
    private char feesState;
    private long feesItemId;


    /**
     * 缴费状态：1为未缴清，2为已缴清
     */
    public static final char FEESSTATE_UNPAY = '1';
    public static final char FEESSTATE_PAID = '2';

    public BigDecimal getFeesBalance2() {
        return feesBalance2;
    }

    public void setFeesBalance2(BigDecimal feesBalance2) {
        this.feesBalance2 = feesBalance2;
    }

    public long getFeesItemId() {
        return feesItemId;
    }

    public void setFeesItemId(long feesItemId) {
        this.feesItemId = feesItemId;
    }

    public int getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(int sectionCode) {
        this.sectionCode = sectionCode;
    }

    public BigDecimal getPaidFees() {
        return paidFees;
    }

    public void setPaidFees(BigDecimal paidFees) {
        this.paidFees = paidFees;
    }

    public char getFeesState() {
        return feesState;
    }

    public void setFeesState(char feesState) {
        this.feesState = feesState;
    }

    public long getLendOrderFeesDetailId() {
        return lendOrderFeesDetailId;
    }

    public void setLendOrderFeesDetailId(long lendOrderFeesDetailId) {
        this.lendOrderFeesDetailId = lendOrderFeesDetailId;
    }

    public long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public char getChargePoint() {
        return chargePoint;
    }

    public void setChargePoint(char chargePoint) {
        this.chargePoint = chargePoint;
    }

    public BigDecimal getFeesBalance() {
        return feesBalance;
    }

    public void setFeesBalance(BigDecimal feesBalance) {
        this.feesBalance = feesBalance;
    }


}
