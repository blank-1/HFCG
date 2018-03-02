package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;

import com.xt.cfp.core.pojo.DisActivity;
import com.xt.cfp.core.util.BigDecimalUtil;

/**
 * 分销活动表
 */
public class DisActivityVO extends DisActivity {

	private String ruleStartDateStr;

	private String ruleEndDateStr;

	private String stateStr;

	private String disProductName;

	private Long disProductId;

	private BigDecimal salesPointStart;// 销售额起点

	private BigDecimal salesPointEnd;// 销售额终点

	private String amountStr;

	public String getRuleStartDateStr() {
		return ruleStartDateStr;
	}

	public void setRuleStartDateStr(String ruleStartDateStr) {
		this.ruleStartDateStr = ruleStartDateStr;
	}

	public String getRuleEndDateStr() {
		return ruleEndDateStr;
	}

	public void setRuleEndDateStr(String ruleEndDateStr) {
		this.ruleEndDateStr = ruleEndDateStr;
	}

	public String getStateStr() {
		return stateStr;
	}

	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}

	public String getDisProductName() {
		return disProductName;
	}

	public void setDisProductName(String disProductName) {
		this.disProductName = disProductName;
	}

	public Long getDisProductId() {
		return disProductId;
	}

	public void setDisProductId(Long disProductId) {
		this.disProductId = disProductId;
	}

	public BigDecimal getSalesPointStart() {
		return salesPointStart;
	}

	public void setSalesPointStart(BigDecimal salesPointStart) {
		this.salesPointStart = salesPointStart;
	}

	public BigDecimal getSalesPointEnd() {
		return salesPointEnd;
	}

	public void setSalesPointEnd(BigDecimal salesPointEnd) {
		this.salesPointEnd = salesPointEnd;
	}

	public String getAmountStr() {
		return BigDecimalUtil.down(this.salesPointStart.divide(BigDecimal.valueOf(10000)),2)
				.toString()
				+ " - "
				+ BigDecimalUtil.down(this.salesPointEnd.divide(BigDecimal.valueOf(10000)),2)
						.toString() + "万元";
	}

}