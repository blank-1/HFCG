package com.xt.cfp.api.vo;

import java.math.BigDecimal;
import java.util.List;

public class WdzjBorrowListVO {
	private String projectId;//项目主键(唯一)【必】
	private String title;//借款标题【必】
	private BigDecimal amount;//借款金额【必】
	private String schedule;//进度【必】
	private String interestRate;//利率【必】
	private Integer deadline;//借款期限【必】
	private String deadlineUnit;//期限单位【必】
	private BigDecimal reward;//奖励【必】
	private String type;//标类型【必】
	private Integer repaymentType;//还款方式【必】
	private String plateType;//标所属平台频道板块【不必】
	private String guarantorsType;//保障担保机构名称【不必】
	private List<WdzjSubscribesVO> subscribes;//投资人数据【必】
	private String province;//借款人所在省份【不必】
	private String city;//借款人所在城市【不必】
	private String userName;//借款人ID【必】
	private String userAvatarUrl;//发标人头像的URL【不必】
	private String amountUsedDesc;//借款用途【不必】
	private BigDecimal revenue;//营收【不必】
	private String loanUrl;//标的详细页面地址链接【必】
	private String successTime;//满标时间【必】
	private String publishTime;//发标时间【不必】
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}
	public Integer getDeadline() {
		return deadline;
	}
	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
	}
	public String getDeadlineUnit() {
		return deadlineUnit;
	}
	public void setDeadlineUnit(String deadlineUnit) {
		this.deadlineUnit = deadlineUnit;
	}
	public BigDecimal getReward() {
		return reward;
	}
	public void setReward(BigDecimal reward) {
		this.reward = reward;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getRepaymentType() {
		return repaymentType;
	}
	public void setRepaymentType(Integer repaymentType) {
		this.repaymentType = repaymentType;
	}
	public String getPlateType() {
		return plateType;
	}
	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}
	public String getGuarantorsType() {
		return guarantorsType;
	}
	public void setGuarantorsType(String guarantorsType) {
		this.guarantorsType = guarantorsType;
	}
	public List<WdzjSubscribesVO> getSubscribes() {
		return subscribes;
	}
	public void setSubscribes(List<WdzjSubscribesVO> subscribes) {
		this.subscribes = subscribes;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserAvatarUrl() {
		return userAvatarUrl;
	}
	public void setUserAvatarUrl(String userAvatarUrl) {
		this.userAvatarUrl = userAvatarUrl;
	}
	public String getAmountUsedDesc() {
		return amountUsedDesc;
	}
	public void setAmountUsedDesc(String amountUsedDesc) {
		this.amountUsedDesc = amountUsedDesc;
	}
	public BigDecimal getRevenue() {
		return revenue;
	}
	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}
	public String getLoanUrl() {
		return loanUrl;
	}
	public void setLoanUrl(String loanUrl) {
		this.loanUrl = loanUrl;
	}
	public String getSuccessTime() {
		return successTime;
	}
	public void setSuccessTime(String successTime) {
		this.successTime = successTime;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

}
