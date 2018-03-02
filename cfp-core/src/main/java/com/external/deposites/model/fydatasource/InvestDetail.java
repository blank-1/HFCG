package com.external.deposites.model.fydatasource;

public class InvestDetail {
	//投资人真实名称，企业时为企业名称
	private String investUserRealName;
	//借款人真实名称，企业时为企业名称
	private String borrowerUserRealName;
	//投资金额
	private Long amt;
	//使用财富卷金额
	private Long wealthVolume;
	//投资人账户
	private String investAcount; 
	//投资标识
	private Long investId;
	
	public Long getAmt() {
		return amt;
	}
	public void setAmt(Long amt) {
		this.amt = amt;
	}
	public Long getWealthVolume() {
		return wealthVolume;
	}
	public void setWealthVolume(Long wealthVolume) {
		this.wealthVolume = wealthVolume;
	}
	public String getInvestAcount() {
		return investAcount;
	}
	public void setInvestAcount(String investAcount) {
		this.investAcount = investAcount;
	}
	public Long getInvestId() {
		return investId;
	}
	public void setInvestId(Long investId) {
		this.investId = investId;
	}
	public String getInvestUserRealName() {
		return investUserRealName;
	}
	public void setInvestUserRealName(String investUserRealName) {
		this.investUserRealName = investUserRealName;
	}
	public String getBorrowerUserRealName() {
		return borrowerUserRealName;
	}
	public void setBorrowerUserRealName(String borrowerUserRealName) {
		this.borrowerUserRealName = borrowerUserRealName;
	}
	
}
