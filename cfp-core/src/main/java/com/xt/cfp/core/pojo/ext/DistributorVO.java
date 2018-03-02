package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.Date;

public class DistributorVO{
	
	private Long userId;
	private String loginName;
	private String realName;
	private String mobile;
	private String idCard;
	private Integer firstNum;
	private Integer secondNum;
	private Integer thirdNum;
	private BigDecimal profit;
	private BigDecimal shouldProfit;
	private String status;
	private String sex;
	private Date birthDay;
	private String birthStr;
	private String education;
	private BigDecimal firstProfit;
	private BigDecimal secondProfit;
	private BigDecimal thirdProfit;
	private String disLevel;
	private Date creatTime;
	private BigDecimal amount;
	private BigDecimal transforM;
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public BigDecimal getFirstProfit() {
		return firstProfit;
	}
	public void setFirstProfit(BigDecimal firstProfit) {
		this.firstProfit = firstProfit;
	}
	public BigDecimal getSecondProfit() {
		return secondProfit;
	}
	public void setSecondProfit(BigDecimal secondProfit) {
		this.secondProfit = secondProfit;
	}
	public BigDecimal getThirdProfit() {
		return thirdProfit;
	}
	public void setThirdProfit(BigDecimal thirdProfit) {
		this.thirdProfit = thirdProfit;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public Integer getFirstNum() {
		return firstNum;
	}
	public void setFirstNum(Integer firstNum) {
		this.firstNum = firstNum;
	}
	public Integer getSecondNum() {
		return secondNum;
	}
	public void setSecondNum(Integer secondNum) {
		this.secondNum = secondNum;
	}
	public Integer getThirdNum() {
		return thirdNum;
	}
	public void setThirdNum(Integer thirdNum) {
		this.thirdNum = thirdNum;
	}
	public BigDecimal getProfit() {
		return profit;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	public BigDecimal getShouldProfit() {
		return shouldProfit;
	}
	public void setShouldProfit(BigDecimal shouldProfit) {
		this.shouldProfit = shouldProfit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDisLevel() {
		return disLevel;
	}
	public void setDisLevel(String disLevel) {
		this.disLevel = disLevel;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getTransforM() {
		return transforM;
	}
	public void setTransforM(BigDecimal transforM) {
		this.transforM = transforM;
	}
	public String getBirthStr() {
		return birthStr;
	}
	public void setBirthStr(String birthStr) {
		this.birthStr = birthStr;
	}
	
}
