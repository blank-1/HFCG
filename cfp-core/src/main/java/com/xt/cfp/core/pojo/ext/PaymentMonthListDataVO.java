package com.xt.cfp.core.pojo.ext;

/**
 * 回款日历，月列表数据封装类
 */
public class PaymentMonthListDataVO {
	private Integer day;//当月有回款记录的日子
	private Integer leg;//当日回款记录的个数
	
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getLeg() {
		return leg;
	}
	public void setLeg(Integer leg) {
		this.leg = leg;
	}
}
