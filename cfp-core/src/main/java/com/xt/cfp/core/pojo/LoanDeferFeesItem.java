package com.xt.cfp.core.pojo;

import java.math.BigDecimal;

/**
 * Created by duyaxing on 2014/7/30.
 */
public class LoanDeferFeesItem {
    private long loanDeferFeesItemId;
    private long feesItemId;
    private long loanApplicationDeferId;
    private char chargeCycle;
    private BigDecimal workflowRatio;
    private String itemName;
    private String itemType;
    private BigDecimal feesRate;
    private char radicesType;
    private String radiceLogic;
    private String radiceName;

    public long getLoanDeferFeesItemId() {
        return loanDeferFeesItemId;
    }

    public void setLoanDeferFeesItemId(long loanDeferFeesItemId) {
        this.loanDeferFeesItemId = loanDeferFeesItemId;
    }

    public long getFeesItemId() {
        return feesItemId;
    }

    public void setFeesItemId(long feesItemId) {
        this.feesItemId = feesItemId;
    }

    public long getLoanApplicationDeferId() {
        return loanApplicationDeferId;
    }

    public void setLoanApplicationDeferId(long loanApplicationDeferId) {
        this.loanApplicationDeferId = loanApplicationDeferId;
    }

    public char getChargeCycle() {
        return chargeCycle;
    }

    public void setChargeCycle(char chargeCycle) {
        this.chargeCycle = chargeCycle;
    }

    public BigDecimal getWorkflowRatio() {
        return workflowRatio;
    }

    public void setWorkflowRatio(BigDecimal workflowRatio) {
        this.workflowRatio = workflowRatio;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getFeesRate() {
        return feesRate;
    }

    public void setFeesRate(BigDecimal feesRate) {
        this.feesRate = feesRate;
    }

    public char getRadicesType() {
        return radicesType;
    }

    public void setRadicesType(char radicesType) {
        this.radicesType = radicesType;
    }

    public String getRadiceLogic() {
        return radiceLogic;
    }

    public void setRadiceLogic(String radiceLogic) {
        this.radiceLogic = radiceLogic;
    }

    public String getRadiceName() {
        return radiceName;
    }

    public void setRadiceName(String radiceName) {
        this.radiceName = radiceName;
    }
}
