package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.List;

import com.xt.cfp.core.constants.FeesItemTypeEnum;
import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.constants.RadiceTypeEnum;
import com.xt.cfp.core.pojo.DefaultInterestDetail;
import com.xt.cfp.core.pojo.RepaymentPlan;

public class LoanFeeDetailVO extends RepaymentPlan {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long accountId;

	private Long loanFeesDetailId;

	private BigDecimal feesRate;// 收费比例

	private int radicesType;

	private char feesCycle;// 收费周期

	private long loanApplicationFeesItemId;

	private String itemType;

	protected String radiceTypeStr;
	protected String ItemTypeStr;
	protected String feesCycleStr;

	private List<DefaultInterestDetail> defaultInterestList;
	// 利息总额
	private BigDecimal loanInterestBalance;
	// 本金总额
	private BigDecimal confirmBalance;

	public String getRadiceTypeStr() {
		return RadiceTypeEnum.getDescByValue(String.valueOf(radicesType));
	}

	public String getItemTypeStr() {
		return FeesItemTypeEnum.getDescByValue(itemType);
	}

	public String getFeesCycleStr() {
		return FeesPointEnum.getDescByValue(String.valueOf(feesCycle));
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public Long getLoanFeesDetailId() {
		return loanFeesDetailId;
	}

	public void setLoanFeesDetailId(Long loanFeesDetailId) {
		this.loanFeesDetailId = loanFeesDetailId;
	}

	public BigDecimal getFeesRate() {
		return feesRate;
	}

	public void setFeesRate(BigDecimal feesRate) {
		this.feesRate = feesRate;
	}

	public int getRadicesType() {
		return radicesType;
	}

	public void setRadicesType(int radicesType) {
		this.radicesType = radicesType;
	}

	public char getFeesCycle() {
		return feesCycle;
	}

	public void setFeesCycle(char feesCycle) {
		this.feesCycle = feesCycle;
	}

	public long getLoanApplicationFeesItemId() {
		return loanApplicationFeesItemId;
	}

	public void setLoanApplicationFeesItemId(long loanApplicationFeesItemId) {
		this.loanApplicationFeesItemId = loanApplicationFeesItemId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public List<DefaultInterestDetail> getDefaultInterestList() {
		return defaultInterestList;
	}

	public void setDefaultInterestList(List<DefaultInterestDetail> defaultInterestList) {
		this.defaultInterestList = defaultInterestList;
	}

	public BigDecimal getLoanInterestBalance() {
		return loanInterestBalance;
	}

	public void setLoanInterestBalance(BigDecimal loanInterestBalance) {
		this.loanInterestBalance = loanInterestBalance;
	}

	public BigDecimal getConfirmBalance() {
		return confirmBalance;
	}

	public void setConfirmBalance(BigDecimal confirmBalance) {
		this.confirmBalance = confirmBalance;
	}

}
