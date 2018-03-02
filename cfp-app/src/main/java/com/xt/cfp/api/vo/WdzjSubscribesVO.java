package com.xt.cfp.api.vo;

import java.math.BigDecimal;

public class WdzjSubscribesVO {
	private String subscribeUserName;//投标人ID【必】
	private BigDecimal amount;//投标金额【必】
	private BigDecimal validAmount;//有效金额【必】
	private String addDate;//投标时间【必】
	private Integer status;//投标状态【必】
	private Integer type;//标识手动或自动投标【必】
	private Integer sourceType;//投标来源【不必】
	
	public String getSubscribeUserName() {
		return subscribeUserName;
	}
	public void setSubscribeUserName(String subscribeUserName) {
		this.subscribeUserName = subscribeUserName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getValidAmount() {
		return validAmount;
	}
	public void setValidAmount(BigDecimal validAmount) {
		this.validAmount = validAmount;
	}
	public String getAddDate() {
		return addDate;
	}
	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSourceType() {
		return sourceType;
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	
}
