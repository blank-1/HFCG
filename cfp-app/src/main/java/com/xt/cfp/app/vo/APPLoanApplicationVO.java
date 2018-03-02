package com.xt.cfp.app.vo;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.xt.cfp.core.util.DateUtil;

public class APPLoanApplicationVO {

	public String loanApplicationTitle;

	public String totalAmountOfLoan;

	public String rewardsPercent;
	// 信用等级
	public String creditLevel;
	// 期限
	public String cycleCount;
	// 进度百分比
	public String ratePercent;
	// 预期收益排序
	public Boolean annRateOrder;
	// 期限排序排序
	public Boolean durationOrder;
	// 信用等级排序
	public Boolean creditRankOrder;
	// 还款方法
	public String repayMentMethod;
	// 还款方式
	public String repayMethod;
	// 起投金额
	public String startAmount;
	// 保障说明
	public String guaranteeType;
	// 剩余金额
	public String remain;
	// 限投
	public String maxBuyBalanceNow;
	// 借款描述
	public String desc;
	// 借款用途描述
	private String useageDesc;
	// 预热时间
	private Date popenTime;
	public boolean begin;

	public String getUseageDesc() {
		return useageDesc;
	}

	public void setUseageDesc(String useageDesc) {
		this.useageDesc = useageDesc;
	}

	public boolean isBegin() {
		try {
			if (null != popenTime) {
				if (DateUtil.secondBetween(new Date(), popenTime) > 0) {
					return false;
				}
			}
			return true;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return begin;
	}

	public void setBegin(boolean begin) {
		this.begin = begin;
	}

	public Date getPopenTime() {
		return popenTime;
	}

	public void setPopenTime(Date popenTime) {
		this.popenTime = popenTime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRepayMethod() {
		return repayMethod;
	}

	public void setRepayMethod(String repayMethod) {
		this.repayMethod = repayMethod;
	}

	public String getMaxBuyBalanceNow() {
		if (maxBuyBalanceNow == null) {
			return "0";
		}
		return maxBuyBalanceNow;
	}

	public void setMaxBuyBalanceNow(String maxBuyBalanceNow) {
		this.maxBuyBalanceNow = maxBuyBalanceNow;
	}

	public void setRemain(String remain) {
		this.remain = remain;
	}

	public String getGuaranteeType() {
		return guaranteeType;
	}

	public void setGuaranteeType(String guaranteeType) {
		this.guaranteeType = guaranteeType;
	}

	public String getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(String startAmount) {
		this.startAmount = startAmount;
	}

	public String getRepayMentMethod() {
		return repayMentMethod;
	}

	public void setRepayMentMethod(String repayMentMethod) {
		this.repayMentMethod = repayMentMethod;
	}

	public String getCycleCount() {
		return cycleCount;
	}

	public void setCycleCount(String cycleCount) {
		this.cycleCount = cycleCount;
	}

	public String getLoanApplicationTitle() {
		return loanApplicationTitle;
	}

	public void setLoanApplicationTitle(String loanApplicationTitle) {
		this.loanApplicationTitle = loanApplicationTitle;
	}

	public String getCreditLevel() {
		return creditLevel;
	}

	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}

	public String getTotalAmountOfLoan() {
		return totalAmountOfLoan;
	}

	public void setTotalAmountOfLoan(String totalAmountOfLoan) {
		this.totalAmountOfLoan = totalAmountOfLoan;
	}

	public void setRatePercent(String ratePercent) {
		this.ratePercent = ratePercent;
	}

	public Boolean getAnnRateOrder() {
		return annRateOrder;
	}

	public void setAnnRateOrder(Boolean annRateOrder) {
		this.annRateOrder = annRateOrder;
	}

	public Boolean getDurationOrder() {
		return durationOrder;
	}

	public void setDurationOrder(Boolean durationOrder) {
		this.durationOrder = durationOrder;
	}

	public Boolean getCreditRankOrder() {
		return creditRankOrder;
	}

	public void setCreditRankOrder(Boolean creditRankOrder) {
		this.creditRankOrder = creditRankOrder;
	}

	public String getRewardsPercent() {
		return rewardsPercent;
	}

	public void setRewardsPercent(String rewardsPercent) {
		this.rewardsPercent = rewardsPercent;
	}

	public String getOrderBy() {
		String orderBy = "";
		String str1 = annRateOrder == null ? null : (annRateOrder ? "rate asc" : "rate desc");
		String str2 = durationOrder == null ? null : (durationOrder ? "lpp.CYCLE_COUNTS asc" : "lpp.CYCLE_COUNTS desc");
		String str3 = creditRankOrder == null ? null : (creditRankOrder ? "lp.CREDIT_LEVEL asc" : "lp.CREDIT_LEVEL desc");

		orderBy += str1 == null ? "" : str1;
		orderBy += str2 == null ? "" : (StringUtils.isEmpty(orderBy) ? str2 : "," + str2);
		orderBy += str3 == null ? "" : (StringUtils.isEmpty(orderBy) ? str3 : "," + str3);
		return orderBy;
	}
}
