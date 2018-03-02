package com.xt.cfp.core.pojo;

import java.math.BigDecimal;

/**
 * 借款产品费用
 */
public class LoanProductFeesItem {
	
    private long lpfiId;//借款产品费用ID
    private long loanProductId;//借款产品ID
    private long feesItemId;//费用项目ID
    private char chargeCycle;//收费周期,收取时机
    private BigDecimal workflowRatio = BigDecimal.ZERO;//平台收取比例
    
    /**
     * 辅助字段
     */
    private FeesItem feesItem;
    private BigDecimal feesRate = BigDecimal.ZERO;
    
    public BigDecimal getWorkflowRatio() {
        return workflowRatio;
    }

    public void setWorkflowRatio(BigDecimal workflowRatio) {
        this.workflowRatio = workflowRatio;
    }

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

    public long getLoanProductId() {
        return loanProductId;
    }

    public void setLoanProductId(long loanProductId) {
        this.loanProductId = loanProductId;
    }

    public long getFeesItemId() {
        return feesItemId;
    }

    public void setFeesItemId(long feesItemId) {
        this.feesItemId = feesItemId;
    }

    public char getChargeCycle() {
        return chargeCycle;
    }

    public void setChargeCycle(char chargeCycle) {
        this.chargeCycle = chargeCycle;
    }

	public BigDecimal getFeesRate() {
		return feesRate;
	}

	public void setFeesRate(BigDecimal feesRate) {
		this.feesRate = feesRate;
	}
    
    
}
