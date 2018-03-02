package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;
/**
 * 佣金基数类型:11,-全部本金,12-全部利息,13-全部利息和本金,21-周期还款利息,22-周期还款本金,23-周期还款本息,
 * */
public enum CommisionRatioType implements EnumsCanDescribed {
	COMI_MAKELOAN_CAPITAL("11", "放款全部本金"), 
	COMI_MAKELOAN_INTEREST("1", "放款全部利息"), 
	COMI_MAKELOAN_ALLBALANCE("1", "放款全部利息和本金"), 
	COMI_REPAYMENT_CAPITAL("2", "周期还款本金"), 
	COMI_REPAYMENT_INTEREST("2", "周期还款利息"), 
	COMI_REPAYMENT_ALLBALANCE("2", "周期还款本息"), 
	;

	private final String value;
	private final String desc;

	private CommisionRatioType(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public String getValue() {
		return value;
	}

	public char value2Char() {
		return value.charAt(0);
	}
}
