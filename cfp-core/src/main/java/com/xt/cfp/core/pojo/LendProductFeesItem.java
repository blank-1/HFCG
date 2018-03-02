package com.xt.cfp.core.pojo;

import java.math.BigDecimal;

/**
 * 出借产品相关费用
 */
public class LendProductFeesItem {
    private long lpfiId;
    private long lendProductId;
    private long feesItemId;
    private char chargePoint;
    private BigDecimal workflowRatio;


    /**
     * 回收周期：1放款收取,2还款周期收费，3到期收取
     */
    public static final char CHARGE_POINT_SEND_MONEY ='1';
    public static final char CHARGE_POINT_CYCLE ='2';
    public static final char CHARGE_POINT_MATURITY ='3';

    public BigDecimal getWorkflowRatio() {
        return workflowRatio;
    }

    public void setWorkflowRatio(BigDecimal workflowRatio) {
        this.workflowRatio = workflowRatio;
    }

    public FeesItem feesItem;

    public FeesItem getFeesItem() {
        return feesItem;
    }

    public void setFeesItem(FeesItem feesItem) {
        this.feesItem = feesItem;
    }

    public long getLpfiId() {
        return lpfiId;
    }

    public void setLpfiId(long lpfiId) {
        this.lpfiId = lpfiId;
    }

    public long getLendProductId() {
        return lendProductId;
    }

    public void setLendProductId(long lendProductId) {
        this.lendProductId = lendProductId;
    }

    public long getFeesItemId() {
        return feesItemId;
    }

    public void setFeesItemId(long feesItemId) {
        this.feesItemId = feesItemId;
    }

    public char getChargePoint() {
        return chargePoint;
    }

    public void setChargePoint(char chargePoint) {
        this.chargePoint = chargePoint;
    }
}
