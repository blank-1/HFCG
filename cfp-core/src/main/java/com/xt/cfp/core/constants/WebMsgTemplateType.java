package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public enum WebMsgTemplateType implements EnumsCanDescribed {
	WEB_MSG_AHEAD_REPAYMENT_VM("aheadRepayment", "提前还款成功短信", "aheadRepayment"), ;
	private final String value;
	private final String desc;
	private final String prekey;

	WebMsgTemplateType(String value, String desc, String prekey) {
		this.value = value;
		this.desc = desc;
		this.prekey = prekey;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public String getPrekey() {
		return prekey;
	}

}
