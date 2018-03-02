package com.xt.cfp.core.pojo;

import java.util.Date;

public class ActivityBinding {
    private Long bindingId;

    private String mobileNo;

    private String openId;

    private Date createTime;
    
    private Long usageFrequency;

    public Long getBindingId() {
        return bindingId;
    }

    public void setBindingId(Long bindingId) {
        this.bindingId = bindingId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Long getUsageFrequency() {
		return usageFrequency;
	}

	public void setUsageFrequency(Long usageFrequency) {
		this.usageFrequency = usageFrequency;
	}
    
}