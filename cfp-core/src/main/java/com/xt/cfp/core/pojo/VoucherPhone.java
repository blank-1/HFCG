package com.xt.cfp.core.pojo;

import java.util.Date;

public class VoucherPhone {
    private Long id;

    private String mobileNo;
    
    private String realName; 
    
    private Date createTime;
    
    private String activityNumber;
    
    private Long usageFrequency;
    
    private Long bindingId;
    
    private Long totalNum;
    
    private Long prizeId;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getActivityNumber() {
		return activityNumber;
	}

	public void setActivityNumber(String activityNumber) {
		this.activityNumber = activityNumber;
	}

	public Long getUsageFrequency() {
		return usageFrequency;
	}

	public void setUsageFrequency(Long usageFrequency) {
		this.usageFrequency = usageFrequency;
	}

	public Long getBindingId() {
		return bindingId;
	}

	public void setBindingId(Long bindingId) {
		this.bindingId = bindingId;
	}

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public Long getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(Long prizeId) {
		this.prizeId = prizeId;
	}

}