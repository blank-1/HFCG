package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;

import com.xt.cfp.core.constants.RateEnum.RateProductScenarioEnum;
import com.xt.cfp.core.constants.RateEnum.RateUserStatusEnum;
import com.xt.cfp.core.pojo.RateUser;

public class RateUserVO extends RateUser {

	private String loginName;
	private String realName;
	private String mobileNo;
	private String rateProductName;
	private BigDecimal rateValue;
	private String usageScenario;
	private String condition;
	private String usageScenarioStr;
	private String statusStr;
	private String conditionStr;
	private Integer startAmount;// 起投金额
	private String isOverlay;// 是否和财富券叠加

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

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getRateProductName() {
		return rateProductName;
	}

	public void setRateProductName(String rateProductName) {
		this.rateProductName = rateProductName;
	}

	public BigDecimal getRateValue() {
		return rateValue;
	}

	public void setRateValue(BigDecimal rateValue) {
		this.rateValue = rateValue;
	}

	public String getUsageScenario() {
		return usageScenario;
	}

	public void setUsageScenario(String usageScenario) {
		this.usageScenario = usageScenario;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getUsageScenarioStr() {
		RateProductScenarioEnum rpse = RateProductScenarioEnum.getRateProductScenarioEnumByValue(this.getUsageScenario());
		if (null != rpse) {
			return rpse.getDesc();
		}
		return usageScenarioStr;
	}

	public void setUsageScenarioStr(String usageScenarioStr) {
		this.usageScenarioStr = usageScenarioStr;
	}

	public String getStatusStr() {
		RateUserStatusEnum ruse = RateUserStatusEnum.getRateUserStatusEnumByValue(this.getStatus());
		if (null != ruse) {
			return ruse.getDesc();
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getConditionStr() {
		return conditionStr;
	}

	public void setConditionStr(String conditionStr) {
		this.conditionStr = conditionStr;
	}

	public Integer getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(Integer startAmount) {
		this.startAmount = startAmount;
	}

	public String getIsOverlay() {
		return isOverlay;
	}

	public void setIsOverlay(String isOverlay) {
		this.isOverlay = isOverlay;
	}

}
