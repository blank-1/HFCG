package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.Date;

import com.xt.cfp.core.pojo.LoanApplication;

public class LendAndLoanVO extends LoanApplication {
	private Long creditorRightsId;
	private Long lendOrderId;
	private Long lendUserId;
	private Long loanProductId;
	private BigDecimal buyBalance;// 出借金额
	private BigDecimal factBalance;// 已收回款
	private BigDecimal waitBackBalance;// 待收回款
	private BigDecimal shouldBalance;// 总计应收回款
	private BigDecimal annualRate;// 年利率
	private BigDecimal cycleCounts; 
	private BigDecimal cycleValue; 
	private Date buyDate;// 出借日期
	private String loanTitle;
	private String realName;
	private char repaymentType;//还款方式

	public Long getCreditorRightsId() {
		return creditorRightsId;
	}

	public void setCreditorRightsId(Long creditorRightsId) {
		this.creditorRightsId = creditorRightsId;
	}

	public Long getLendOrderId() {
		return lendOrderId;
	}

	public void setLendOrderId(Long lendOrderId) {
		this.lendOrderId = lendOrderId;
	}

	public BigDecimal getBuyBalance() {
		return buyBalance;
	}

	public void setBuyBalance(BigDecimal buyBalance) {
		this.buyBalance = buyBalance;
	}

	public BigDecimal getFactBalance() {
		return factBalance;
	}

	public void setFactBalance(BigDecimal factBalance) {
		this.factBalance = factBalance;
	}

	public BigDecimal getShouldBalance() {
		return shouldBalance;
	}

	public void setShouldBalance(BigDecimal shouldBalance) {
		this.shouldBalance = shouldBalance;
	}

	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	public String getLoanTitle() {
		return loanTitle;
	}

	public void setLoanTitle(String loanTitle) {
		this.loanTitle = loanTitle;
	}

	public BigDecimal getWaitBackBalance() {
		return waitBackBalance;
	}

	public void setWaitBackBalance(BigDecimal waitBackBalance) {
		this.waitBackBalance = waitBackBalance;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public BigDecimal getAnnualRate() {
		return annualRate;
	}

	public void setAnnualRate(BigDecimal annualRate) {
		this.annualRate = annualRate;
	}

	public BigDecimal getCycleCounts() {
		return cycleCounts;
	}

	public void setCycleCounts(BigDecimal cycleCounts) {
		this.cycleCounts = cycleCounts;
	}

	public BigDecimal getCycleValue() {
		return cycleValue;
	}

	public void setCycleValue(BigDecimal cycleValue) {
		this.cycleValue = cycleValue;
	}

	public char getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(char repaymentType) {
		this.repaymentType = repaymentType;
	}

	public Long getLendUserId() {
		return lendUserId;
	}

	public void setLendUserId(Long lendUserId) {
		this.lendUserId = lendUserId;
	}

	public Long getLoanProductId() {
		return loanProductId;
	}

	public void setLoanProductId(Long loanProductId) {
		this.loanProductId = loanProductId;
	}

}
