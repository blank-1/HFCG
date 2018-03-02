package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;

import com.xt.cfp.core.pojo.EnterpriseUser;

public class EnterpriseUserExt extends EnterpriseUser{
	private String loginName;
	private Long borrowCount;
	private BigDecimal availValue;
	private BigDecimal frozeValue;
	private BigDecimal value;
	private String idCard;
	private String realName;
	private String mobileNo;
	private String email;
	private String status;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Long getBorrowCount() {
		return borrowCount;
	}
	public void setBorrowCount(Long borrowCount) {
		this.borrowCount = borrowCount;
	}
	public BigDecimal getAvailValue() {
		return availValue;
	}
	public void setAvailValue(BigDecimal availValue) {
		this.availValue = availValue;
	}
	public BigDecimal getFrozeValue() {
		return frozeValue;
	}
	public void setFrozeValue(BigDecimal frozeValue) {
		this.frozeValue = frozeValue;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
