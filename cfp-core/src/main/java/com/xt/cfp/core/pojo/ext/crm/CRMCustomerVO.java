package com.xt.cfp.core.pojo.ext.crm;

import java.math.BigDecimal;
import java.util.Date;

public class CRMCustomerVO {
	
	private Long userId;
	private String loginName;
	private String realName;
	private String mobile;
	private BigDecimal account;
	private BigDecimal accountFroze;
	private BigDecimal accountInvestment;
	private BigDecimal accountWithDraw;
	private String source;
	private Date regTime;
	private Date firstInvest;
	private Date lastInvest;
	private Date lastLogin;
	private String code;
	private String status;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public BigDecimal getAccount() {
		return account;
	}
	public void setAccount(BigDecimal account) {
		this.account = account;
	}
	public BigDecimal getAccountInvestment() {
		return accountInvestment;
	}
	public void setAccountInvestment(BigDecimal accountInvestment) {
		this.accountInvestment = accountInvestment;
	}
	public BigDecimal getAccountWithDraw() {
		return accountWithDraw;
	}
	public void setAccountWithDraw(BigDecimal accountWithDraw) {
		this.accountWithDraw = accountWithDraw;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Date getRegTime() {
		return regTime;
	}
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	public Date getFirstInvest() {
		return firstInvest;
	}
	public void setFirstInvest(Date firstInvest) {
		this.firstInvest = firstInvest;
	}
	public Date getLastInvest() {
		return lastInvest;
	}
	public void setLastInvest(Date lastInvest) {
		this.lastInvest = lastInvest;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getAccountFroze() {
		return accountFroze;
	}
	public void setAccountFroze(BigDecimal accountFroze) {
		this.accountFroze = accountFroze;
	}
}
