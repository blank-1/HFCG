package com.xt.cfp.core.pojo;

import com.xt.cfp.core.constants.FeesPointEnum;

import java.math.BigDecimal;

public class LendLoanBinding {
	private Long lendLoanBindingId;
	private Long lendProductId;
	private Long loanProductId;
	private BigDecimal loanRatio;
	private Long feesItemId;
	private String chargePoint;
	private LendProduct lendProduct;
	private LoanProduct loanProduct;
	private FeesItem feesItem;
	protected String chargePointStr;

	public String getChargePointStr() {
		return chargePointStr;
	}

	public void setChargePointStr(String chargePointStr) {
		this.chargePointStr = chargePointStr;
	}

	public Long getLendLoanBindingId() {
		return lendLoanBindingId;
	}

	public void setLendLoanBindingId(Long lendLoanBindingId) {
		this.lendLoanBindingId = lendLoanBindingId;
	}

	public Long getLendProductId() {
		return lendProductId;
	}

	public void setLendProductId(Long lendProductId) {
		this.lendProductId = lendProductId;
	}

	public Long getLoanProductId() {
		return loanProductId;
	}

	public void setLoanProductId(Long loanProductId) {
		this.loanProductId = loanProductId;
	}

	public BigDecimal getLoanRatio() {
		return loanRatio;
	}

	public void setLoanRatio(BigDecimal loanRatio) {
		this.loanRatio = loanRatio;
	}

	public Long getFeesItemId() {
		return feesItemId;
	}

	public void setFeesItemId(Long feesItemId) {
		this.feesItemId = feesItemId;
	}

	public String getChargePoint() {
		return chargePoint;
	}

	public void setChargePoint(String chargePoint) {
		this.chargePoint = chargePoint;
		this.chargePointStr = FeesPointEnum.getDescByValue(chargePoint);
	}

	public LendProduct getLendProduct() {
		return lendProduct;
	}

	public void setLendProduct(LendProduct lendProduct) {
		this.lendProduct = lendProduct;
	}

	public LoanProduct getLoanProduct() {
		return loanProduct;
	}

	public void setLoanProduct(LoanProduct loanProduct) {
		this.loanProduct = loanProduct;
	}

	public FeesItem getFeesItem() {
		return feesItem;
	}

	public void setFeesItem(FeesItem feesItem) {
		this.feesItem = feesItem;
	}

}
