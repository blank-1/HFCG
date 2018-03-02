package com.xt.cfp.core.pojo;

import java.util.Date;

public class PrizePool {
    private Long prizePoolId;

    private Long prizeProductId;

    private String prizeType;

    private Long prizeNum;

    private String activityNumber;

    private String disable;

    private Date addTime;
    
    private Double winningPro;

    public Long getPrizePoolId() {
        return prizePoolId;
    }

    public void setPrizePoolId(Long prizePoolId) {
        this.prizePoolId = prizePoolId;
    }

    public Long getPrizeProductId() {
        return prizeProductId;
    }

    public void setPrizeProductId(Long prizeProductId) {
        this.prizeProductId = prizeProductId;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType == null ? null : prizeType.trim();
    }

    public Long getPrizeNum() {
        return prizeNum;
    }

    public void setPrizeNum(Long prizeNum) {
        this.prizeNum = prizeNum;
    }

    public String getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(String activityNumber) {
        this.activityNumber = activityNumber == null ? null : activityNumber.trim();
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable == null ? null : disable.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

	public Double getWinningPro() {
		return winningPro;
	}

	public void setWinningPro(Double winningPro) {
		this.winningPro = winningPro;
	}
    
}