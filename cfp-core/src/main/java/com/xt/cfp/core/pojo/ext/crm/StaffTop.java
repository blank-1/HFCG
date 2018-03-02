package com.xt.cfp.core.pojo.ext.crm;

import java.math.BigDecimal;

public class StaffTop {
	
	private Long id; 				//staffId
	private Long userId;
	private String name;	
	private String orgName;			
	private String franchinseName;
	private BigDecimal account;		//总销售金额
	private BigDecimal disAccount;  //总折标金额
	private String time;			//起止时间
	private String mobile;
	private String genName;			//邀请人
	private String genCode;			//邀请人邀请码
	private String orgCode;
	private BigDecimal gDisAccount;  //总折标金额
	
	
	public BigDecimal getgDisAccount() {
		return gDisAccount;
	}
	public void setgDisAccount(BigDecimal gDisAccount) {
		this.gDisAccount = gDisAccount;
	}
	public String getGenName() {
		return genName;
	}
	public void setGenName(String genName) {
		this.genName = genName;
	}
	public String getGenCode() {
		return genCode;
	}
	public void setGenCode(String genCode) {
		this.genCode = genCode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getFranchinseName() {
		return franchinseName;
	}
	public void setFranchinseName(String franchinseName) {
		this.franchinseName = franchinseName;
	}
	public BigDecimal getAccount() {
		return account;
	}
	public void setAccount(BigDecimal account) {
		this.account = account;
	}
	public BigDecimal getDisAccount() {
		return disAccount;
	}
	public void setDisAccount(BigDecimal disAccount) {
		this.disAccount = disAccount;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
}
