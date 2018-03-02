package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.Date;

import com.xt.cfp.core.pojo.LendOrder;

public class LendOrderExtProduct extends LendOrder{

	private String productName;//出借产品名字
	private BigDecimal  expectProfit = BigDecimal.ZERO;//预计收益
	private BigDecimal  totalPayMent = BigDecimal.ZERO;//总回款
	private String productState;//出借产品状态
	private String loginName;//登录名
	private String publishCode;//期数	
	private String guaranteeType;//保障类型
	private BigDecimal startsAt = BigDecimal.ZERO;//起投金额
	private String publishState;//状态
	private String toInterestPoint;//返息周期
	private BigDecimal profitRateMax;//最大收益率
	private Date newDate;//当前时间
	
	
	
	public Date getNewDate() {
		return newDate;
	}

	public void setNewDate(Date newDate) {
		this.newDate = newDate;
	}

	public String getToInterestPoint() {
		return toInterestPoint;
	}

	public void setToInterestPoint(String toInterestPoint) {
		this.toInterestPoint = toInterestPoint;
	}

	private String realName;
	private String idCard;
	private String mobileNo;
	private String dueTimeScope;//标的期限范围（如：3-6）
	private String publishName;//出借产品发布名称
	
	public String getPublishState() {
		return publishState;
	}

	public void setPublishState(String publishState) {
		this.publishState = publishState;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public BigDecimal getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(BigDecimal startsAt) {
		this.startsAt = startsAt;
	}

	public String getGuaranteeType() {
		return guaranteeType;
	}

	public void setGuaranteeType(String guaranteeType) {
		this.guaranteeType = guaranteeType;
	}

	public String getPublishCode() {
		return publishCode;
	}

	public void setPublishCode(String publishCode) {
		this.publishCode = publishCode;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}



	public BigDecimal getExpectProfit() {
		return expectProfit;
	}

	public void setExpectProfit(BigDecimal expectProfit) {
		this.expectProfit = expectProfit;
	}

	public BigDecimal getTotalPayMent() {
		return totalPayMent;
	}

	public void setTotalPayMent(BigDecimal totalPayMent) {
		this.totalPayMent = totalPayMent;
	}

	public String getProductState() {
		return productState;
	}

	public void setProductState(String productState) {
		this.productState = productState;
	}

	public String getDueTimeScope() {
		return dueTimeScope;
	}

	public void setDueTimeScope(String dueTimeScope) {
		this.dueTimeScope = dueTimeScope;
	}

	public BigDecimal getProfitRateMax() {
		return profitRateMax;
	}

	public void setProfitRateMax(BigDecimal profitRateMax) {
		this.profitRateMax = profitRateMax;
	}

	public String getPublishName() {
		return publishName;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}
	
}
