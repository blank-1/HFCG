package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class CreditorRightsTransferApplication {
    private Long creditorRightsApplyId;

    private Long applyCrId;

    private Long applyUserId;

    private Date applyTime;

    private String transType;

    private BigDecimal applyPrice;

    private BigDecimal whenWorth;

    private BigDecimal donePrice;

    private Date doneTime;

    private String busStatus;
    
    private Integer timeLimit;

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Long getCreditorRightsApplyId() {
        return creditorRightsApplyId;
    }

    public void setCreditorRightsApplyId(Long creditorRightsApplyId) {
        this.creditorRightsApplyId = creditorRightsApplyId;
    }

    public Long getApplyCrId() {
        return applyCrId;
    }

    public void setApplyCrId(Long applyCrId) {
        this.applyCrId = applyCrId;
    }

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }


    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public BigDecimal getApplyPrice() {
        return applyPrice;
    }

    public void setApplyPrice(BigDecimal applyPrice) {
        this.applyPrice = applyPrice;
    }

    public BigDecimal getWhenWorth() {
        return whenWorth;
    }

    public void setWhenWorth(BigDecimal whenWorth) {
        this.whenWorth = whenWorth;
    }

    public BigDecimal getDonePrice() {
        return donePrice;
    }

    public void setDonePrice(BigDecimal donePrice) {
        this.donePrice = donePrice;
    }

    public Date getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(Date doneTime) {
        this.doneTime = doneTime;
    }

    public String getBusStatus() {
        return busStatus;
    }

    public void setBusStatus(String busStatus) {
        this.busStatus = busStatus == null ? null : busStatus.trim();
    }

	public Integer getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}
    
}