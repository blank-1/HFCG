package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.Date;

public class CreditorRightTransferApplyVO {
	
	private Long creditorRightsApplyId;//债权转让申请ID
	private Long loanApplicationId;//借款申请ID
	private String loanApplicationName;//借款标题
	private String periodsNumber;//当前期数
	private BigDecimal whenWorth;//待收本金
	private BigDecimal applyPrice;//转出价格
	private BigDecimal discount;//转让折扣
	private Date applyTime;//发起转让日期
	private Date applyEndTime;//转让截止日
	private String loginName;//转让人用户名
	private String realName;//转让人姓名
	private String mobileNo;//转让人手机号
	private String rightsState;//债权状态
	private String turnState;//转让状态
	
	public Long getCreditorRightsApplyId() {
		return creditorRightsApplyId;
	}
	public void setCreditorRightsApplyId(Long creditorRightsApplyId) {
		this.creditorRightsApplyId = creditorRightsApplyId;
	}
	public Long getLoanApplicationId() {
		return loanApplicationId;
	}
	public void setLoanApplicationId(Long loanApplicationId) {
		this.loanApplicationId = loanApplicationId;
	}
	public String getLoanApplicationName() {
		return loanApplicationName;
	}
	public void setLoanApplicationName(String loanApplicationName) {
		this.loanApplicationName = loanApplicationName;
	}
	public String getPeriodsNumber() {
		return periodsNumber;
	}
	public void setPeriodsNumber(String periodsNumber) {
		this.periodsNumber = periodsNumber;
	}
	public BigDecimal getWhenWorth() {
		return whenWorth;
	}
	public void setWhenWorth(BigDecimal whenWorth) {
		this.whenWorth = whenWorth;
	}
	public BigDecimal getApplyPrice() {
		return applyPrice;
	}
	public void setApplyPrice(BigDecimal applyPrice) {
		this.applyPrice = applyPrice;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public Date getApplyEndTime() {
		return applyEndTime;
	}
	public void setApplyEndTime(Date applyEndTime) {
		this.applyEndTime = applyEndTime;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getRightsState() {
		return rightsState;
	}
	public void setRightsState(String rightsState) {
		this.rightsState = rightsState;
	}
	public String getTurnState() {
		return turnState;
	}
	public void setTurnState(String turnState) {
		this.turnState = turnState;
	}
	
}
