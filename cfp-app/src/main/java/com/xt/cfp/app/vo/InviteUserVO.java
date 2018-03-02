package com.xt.cfp.app.vo;

public class InviteUserVO {
	private String loginName;
	private String createTime;
	private String inviteAction;
	private String platformAward;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getInviteAction() {
		return inviteAction;
	}
	public void setInviteAction(String inviteAction) {
		this.inviteAction = inviteAction;
	}
	public String getPlatformAward() {
		return platformAward;
	}
	public void setPlatformAward(String platformAward) {
		this.platformAward = platformAward;
	}
	
}
