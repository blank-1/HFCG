package com.xt.cfp.core.pojo.ext.crm;

import com.xt.cfp.core.util.StringUtils;

public class TransforStaff {
	
	private Long id;
	private String name;
	private String mobile;
	private String franchinseName;
	private String orgName;
	private String roleName;
	
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFranchinseName() {
		return StringUtils.isNull(franchinseName)?"":franchinseName;
	}
	public void setFranchinseName(String franchinseName) {
		this.franchinseName = franchinseName;
	}
	public String getOrgName() {
		return StringUtils.isNull(orgName)?"":orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getRoleName() {
		return StringUtils.isNull(roleName)?"":roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
