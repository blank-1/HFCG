package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;

import com.xt.cfp.core.pojo.LoanPublish;

public class LoanPublishVO extends LoanPublish {

	private String loanUseageName;
	private String applicationDesc;
	private String usageDesc;
	private String productName;
	private BigDecimal dueTime;
	private char dueTimeType;
	private String loanType;
	private String loanUseage;

	public String getLoanUseage() {
		return loanUseage;
	}

	public void setLoanUseage(String loanUseage) {
		this.loanUseage = loanUseage;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getLoanUseageName() {
		return loanUseageName;
	}

	public void setLoanUseageName(String loanUseageName) {
		this.loanUseageName = loanUseageName;
	}

	public String getUsageDesc() {
		return usageDesc;
	}

	public void setUsageDesc(String usageDesc) {
		this.usageDesc = usageDesc;
	}

	public String getApplicationDesc() {
		return applicationDesc;
	}

	public void setApplicationDesc(String applicationDesc) {
		this.applicationDesc = applicationDesc;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getDueTime() {
		return dueTime;
	}

	public void setDueTime(BigDecimal dueTime) {
		this.dueTime = dueTime;
	}

	public char getDueTimeType() {
		return dueTimeType;
	}

	public void setDueTimeType(char dueTimeType) {
		this.dueTimeType = dueTimeType;
	}

}