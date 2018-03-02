package com.xt.cfp.app.vo;

public class APPLoanApplicationListVO {
	private String loanApplicationId;// 借款申请id

	private String loanApplicationTitle;

	private String totalAmountOfLoan;

	private String annualRate;// 年利率
	
	private String rewardsPercent;// 奖励利率

	private String cycleCount;// 期限

	private String startAmount;// 起投金额

	private boolean begin;// 是否预热

	private String popenTime;// 预热时间

	private String applicationState;// 借款申请状态
	
	private String loanType;// 借款类型
	
	private String secondBetwween;//预热剩余时间
	
	private String targetType;//定向标类型
	
	public String getLoanApplicationId() {
		return loanApplicationId;
	}

	public void setLoanApplicationId(String loanApplicationId) {
		this.loanApplicationId = loanApplicationId;
	}

	public String getLoanApplicationTitle() {
		return loanApplicationTitle;
	}

	public void setLoanApplicationTitle(String loanApplicationTitle) {
		this.loanApplicationTitle = loanApplicationTitle;
	}

	public String getTotalAmountOfLoan() {
		return totalAmountOfLoan;
	}

	public void setTotalAmountOfLoan(String totalAmountOfLoan) {
		this.totalAmountOfLoan = totalAmountOfLoan;
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

	public String getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(String startAmount) {
		this.startAmount = startAmount;
	}

	public boolean isBegin() {
		return begin;
	}

	public void setBegin(boolean begin) {
		this.begin = begin;
	}

	public String getPopenTime() {
		return popenTime;
	}

	public void setPopenTime(String popenTime) {
		this.popenTime = popenTime;
	}

	public String getApplicationState() {
		return applicationState;
	}

	public void setApplicationState(String applicationState) {
		this.applicationState = applicationState;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getSecondBetwween() {
		return secondBetwween;
	}

	public void setSecondBetwween(String secondBetwween) {
		this.secondBetwween = secondBetwween;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

}
