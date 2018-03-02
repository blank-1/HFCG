package com.xt.cfp.core.pojo.ext.crm;

import java.util.List;

import com.xt.cfp.core.pojo.CrmOrg;

public class OrganizationVO {
	
	private CrmOrg org;
	private List<OrganizationVO> childs;
	
	public CrmOrg getOrg() {
		return org;
	}
	public void setOrg(CrmOrg org) {
		this.org = org;
	}
	public List<OrganizationVO> getChilds() {
		return childs;
	}
	public void setChilds(List<OrganizationVO> childs) {
		this.childs = childs;
	}
}
