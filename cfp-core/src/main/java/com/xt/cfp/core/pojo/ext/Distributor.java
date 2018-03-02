package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.DistributionInvite;

public class Distributor {
	
	private DistributionInvite distributionInvite;
	private String name;
	private String createTime;
	
	public DistributionInvite getDistributionInvite() {
		return distributionInvite;
	}
	public void setDistributionInvite(DistributionInvite distributionInvite) {
		this.distributionInvite = distributionInvite;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
