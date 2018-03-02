package com.xt.cfp.core.pojo.fyInterFace;

import java.io.Serializable;

/**
 * 接口交易信息
 */
public class TradUserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fyUserId;//富友账户id
	
	private Long amt;//交易金额
	
	private Long platformIncreaseAmt;//平台加息金额
	
	private Long investId;//投资标识
	
	private String realName;//个人用户真实姓名或企业用户企业名称


	public String getFyUserId() {
		return fyUserId;
	}

	public void setFyUserId(String fyUserId) {
		this.fyUserId = fyUserId;
	}

	public Long getAmt() {
		return amt;
	}

	public void setAmt(Long amt) {
		this.amt = amt;
	}

	public Long getPlatformIncreaseAmt() {
		return platformIncreaseAmt;
	}

	public void setPlatformIncreaseAmt(Long platformIncreaseAmt) {
		this.platformIncreaseAmt = platformIncreaseAmt;
	}

	public Long getInvestId() {
		return investId;
	}

	public void setInvestId(Long investId) {
		this.investId = investId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	
}
