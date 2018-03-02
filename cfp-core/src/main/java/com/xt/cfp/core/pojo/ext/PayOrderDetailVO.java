package com.xt.cfp.core.pojo.ext;

import java.util.Date;

import com.xt.cfp.core.pojo.PayOrderDetail;

public class PayOrderDetailVO extends PayOrderDetail{

	private String status;
    private Date resultTime;
    private String rechargeCode;
    private String channelName;
	private String extendNo;

	public String getExtendNo() {
		return extendNo;
	}

	public void setExtendNo(String extendNo) {
		this.extendNo = extendNo;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getResultTime() {
		return resultTime;
	}
	public void setResultTime(Date resultTime) {
		this.resultTime = resultTime;
	}
	public String getRechargeCode() {
		return rechargeCode;
	}
	public void setRechargeCode(String rechargeCode) {
		this.rechargeCode = rechargeCode;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
    
}
