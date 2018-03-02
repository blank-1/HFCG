package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;

/**
 * 回款日历，天列表数据封装类
 */
public class PaymentDayListDataVO {
	private Integer pjType;//当前列表项标的种类
	private String title;//当前还款对应的标的名称
	private Integer benOrLi;//前列表项的金额为利息还是本金
	private Integer timerAll;//当前列表项的总期数
	private Integer timmerNow;//当前列表项的当前期数
	private BigDecimal backMoney;//当前列表项的回款金额（或利息金额）
	private String limitTime;//当前列表项的到期时间
	private Integer hasBackOrNot;//当前列表项是否为已回款
	
	public Integer getPjType() {
		return pjType;
	}
	public void setPjType(Integer pjType) {
		this.pjType = pjType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getBenOrLi() {
		return benOrLi;
	}
	public void setBenOrLi(Integer benOrLi) {
		this.benOrLi = benOrLi;
	}
	public Integer getTimerAll() {
		return timerAll;
	}
	public void setTimerAll(Integer timerAll) {
		this.timerAll = timerAll;
	}
	public Integer getTimmerNow() {
		return timmerNow;
	}
	public void setTimmerNow(Integer timmerNow) {
		this.timmerNow = timmerNow;
	}
	public BigDecimal getBackMoney() {
		return backMoney;
	}
	public void setBackMoney(BigDecimal backMoney) {
		this.backMoney = backMoney;
	}
	public String getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(String limitTime) {
		this.limitTime = limitTime;
	}
	public Integer getHasBackOrNot() {
		return hasBackOrNot;
	}
	public void setHasBackOrNot(Integer hasBackOrNot) {
		this.hasBackOrNot = hasBackOrNot;
	}
}
