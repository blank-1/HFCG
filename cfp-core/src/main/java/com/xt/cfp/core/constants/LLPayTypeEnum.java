package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public enum LLPayTypeEnum implements EnumsCanDescribed {
	LLPAY_AUTH("D","认证支付"),
	LLPAY_DEBIT_CARD("1","借记卡支付"),
	;
	private final String value;
	private final String desc;

	private LLPayTypeEnum(String value, String desc) {
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

}
