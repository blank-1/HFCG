package com.xt.cfp.app.vo;

public class APPTurnCreditRightListVO {
	private String creditorRightsId;//债权ID
	private String creditorRightsApplyId;//债权转让申请ID
	private String loanApplicationTitle;//借款标题
	private String annualRate;//年化利率
	private String rewardsPercent;//奖励利率
	private String cycleCount;//剩余期限
	private String whenWorth;//剩余本金
	private String applyPrice;//转出价格
	private String ratePercent;//进度(totalAmountOfLoan / applyPrice)
	private String totalAmountOfLoan;//已经转出的价格
	private String loanType;//借款申请类型
	
	public String getCreditorRightsId() {
		return creditorRightsId;
	}
	public void setCreditorRightsId(String creditorRightsId) {
		this.creditorRightsId = creditorRightsId;
	}
	public String getCreditorRightsApplyId() {
		return creditorRightsApplyId;
	}
	public void setCreditorRightsApplyId(String creditorRightsApplyId) {
		this.creditorRightsApplyId = creditorRightsApplyId;
	}
	public String getLoanApplicationTitle() {
		return loanApplicationTitle;
	}
	public void setLoanApplicationTitle(String loanApplicationTitle) {
		this.loanApplicationTitle = loanApplicationTitle;
	}
	public String getAnnualRate() {
		return annualRate;
	}
	public void setAnnualRate(String annualRate) {
		this.annualRate = annualRate;
	}
	public String getRewardsPercent() {
		return rewardsPercent;
	}
	public void setRewardsPercent(String rewardsPercent) {
		this.rewardsPercent = rewardsPercent;
	}
	public String getCycleCount() {
		return cycleCount;
	}
	public void setCycleCount(String cycleCount) {
		this.cycleCount = cycleCount;
	}
	public String getWhenWorth() {
		return whenWorth;
	}
	public void setWhenWorth(String whenWorth) {
		this.whenWorth = whenWorth;
	}
	public String getApplyPrice() {
		return applyPrice;
	}
	public void setApplyPrice(String applyPrice) {
		this.applyPrice = applyPrice;
	}
	public String getRatePercent() {
		return ratePercent;
	}
	public void setRatePercent(String ratePercent) {
		this.ratePercent = ratePercent;
	}
	public String getTotalAmountOfLoan() {
		return totalAmountOfLoan;
	}
	public void setTotalAmountOfLoan(String totalAmountOfLoan) {
		this.totalAmountOfLoan = totalAmountOfLoan;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	
}
