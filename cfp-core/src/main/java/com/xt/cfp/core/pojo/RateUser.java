package com.xt.cfp.core.pojo;

import java.util.Date;

import com.xt.cfp.core.constants.RateEnum;

/**
 * 加息券发放表
 */
public class RateUser {
    private Long rateUserId;

    private Long rateProductId;

    private Long userId;

    private String source;

    private Integer totalTimes;

    private Integer usedTimes;

    private Integer surplusTimes;

    private String status;

    private Date startDate;

    private Date endDate;

    private Long adminId;

    public Long getRateUserId() {
        return rateUserId;
    }

    public void setRateUserId(Long rateUserId) {
        this.rateUserId = rateUserId;
    }

    public Long getRateProductId() {
        return rateProductId;
    }

    public void setRateProductId(Long rateProductId) {
        this.rateProductId = rateProductId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Integer getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(Integer usedTimes) {
        this.usedTimes = usedTimes;
    }

    public Integer getSurplusTimes() {
        return surplusTimes;
    }

    public void setSurplusTimes(Integer surplusTimes) {
        this.surplusTimes = surplusTimes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
    
    public RateUser setStatus(RateUser rateUser) {
        Date current = new Date();
        if(rateUser.getEndDate().before(current)){
        	rateUser.setStatus(RateEnum.RateUserStatusEnum.TIMEOUT.getValue());
        	return rateUser;
        }
        if(rateUser.getUsedTimes() == 0){
        	rateUser.setStatus(RateEnum.RateUserStatusEnum.UNUSED.getValue());
        	return rateUser ;
        }
        if(rateUser.getUsedTimes() == rateUser.getTotalTimes()){
        	rateUser.setStatus(RateEnum.RateUserStatusEnum.USEUP.getValue());
        	return rateUser ;
        }
        if(rateUser.getSurplusTimes() > 0 && rateUser.getSurplusTimes() < rateUser.getTotalTimes()){
        	rateUser.setStatus(RateEnum.RateUserStatusEnum.USING.getValue());
        	return rateUser; 
        }
        return rateUser; 
    }
    

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}