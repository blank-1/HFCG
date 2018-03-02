package com.xt.cfp.core.pojo;

import java.util.Date;

public class UserShare {
    private Long userShareId;

    private Long userId;

    private String activityNumber;

    private Date createTime;

    private String isShare;

    private Integer luckDrawNum;
    
    private Integer usedLuckDrawNum;

    public Long getUserShareId() {
        return userShareId;
    }

    public void setUserShareId(Long userShareId) {
        this.userShareId = userShareId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(String activityNumber) {
        this.activityNumber = activityNumber == null ? null : activityNumber.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare == null ? null : isShare.trim();
    }

    public Integer getLuckDrawNum() {
        return luckDrawNum;
    }

    public void setLuckDrawNum(Integer luckDrawNum) {
        this.luckDrawNum = luckDrawNum;
    }

	public Integer getUsedLuckDrawNum() {
		return usedLuckDrawNum;
	}

	public void setUsedLuckDrawNum(Integer usedLuckDrawNum) {
		this.usedLuckDrawNum = usedLuckDrawNum;
	}
    
}