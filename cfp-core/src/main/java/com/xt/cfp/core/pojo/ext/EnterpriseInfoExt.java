package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;

import com.xt.cfp.core.pojo.EnterpriseInfo;

public class EnterpriseInfoExt extends EnterpriseInfo{
	private BigDecimal avilableValue;
    private BigDecimal freezeValue;
    private BigDecimal value;
    private Long borrowCount;
    private Long bondUserCount;
    
	public BigDecimal getAvilableValue() {
		return avilableValue;
	}
	public void setAvilableValue(BigDecimal avilableValue) {
		this.avilableValue = avilableValue;
	}
	public BigDecimal getFreezeValue() {
		return freezeValue;
	}
	public void setFreezeValue(BigDecimal freezeValue) {
		this.freezeValue = freezeValue;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public Long getBorrowCount() {
		return borrowCount;
	}
	public void setBorrowCount(Long borrowCount) {
		this.borrowCount = borrowCount;
	}
	public Long getBondUserCount() {
		return bondUserCount;
	}
	public void setBondUserCount(Long bondUserCount) {
		this.bondUserCount = bondUserCount;
	}
    
    
}
