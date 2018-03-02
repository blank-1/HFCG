package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.MainLoanApplication;

public class MainLoanApplicationExtOne extends MainLoanApplication{
	protected String userRealName;
	protected String idCard;
	protected String mobileNo;
	protected String creditLevel;
	protected String loanTitle;
	protected long isDelay;
	protected long haveCast;
	protected int cycleCounts;//周期数
	protected int cycleValue;//还款周期值
	
	protected String agreementStatus ;//合同状态
	
	

    public String getAgreementStatus() {
		return agreementStatus;
	}

	public void setAgreementStatus(String agreementStatus) {
		this.agreementStatus = agreementStatus;
	}

	public String getLoanTitle() {
        return loanTitle;
    }

    public void setLoanTitle(String loanTitle) {
        this.loanTitle = loanTitle;
    }

    public long getIsDelay() {
		return isDelay;
	}
	public void setIsDelay(long isDelay) {
		this.isDelay = isDelay;
	}
	public long getHaveCast() {
		return haveCast;
	}
	public void setHaveCast(long haveCast) {
		this.haveCast = haveCast;
	}
	public String getUserRealName() {
		return userRealName;
	}
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getCreditLevel() {
		return creditLevel;
	}
	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}

	public int getCycleCounts() {
		return cycleCounts;
	}

	public void setCycleCounts(int cycleCounts) {
		this.cycleCounts = cycleCounts;
	}

	public int getCycleValue() {
		return cycleValue;
	}

	public void setCycleValue(int cycleValue) {
		this.cycleValue = cycleValue;
	}
}
