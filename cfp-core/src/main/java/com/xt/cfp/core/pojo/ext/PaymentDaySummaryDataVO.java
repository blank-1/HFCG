package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;

/**
 * 回款日历，天汇总数据封装类
 */
public class PaymentDaySummaryDataVO {
	private String moneyTime;//当日回款笔数
	private BigDecimal moneyDay;//当日总回款额
	
	public String getMoneyTime() {
		return moneyTime;
	}
	public void setMoneyTime(String moneyTime) {
		this.moneyTime = moneyTime;
	}
	public BigDecimal getMoneyDay() {
		return moneyDay;
	}
	public void setMoneyDay(BigDecimal moneyDay) {
		this.moneyDay = moneyDay;
	}
}
