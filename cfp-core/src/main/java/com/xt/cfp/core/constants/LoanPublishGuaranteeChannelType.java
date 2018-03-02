package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public enum LoanPublishGuaranteeChannelType implements EnumsCanDescribed {
	NONE("1", "无"), 
	GUARANTEECORPORATION("2", "担保公司"), 
	RISKRESERVEFUND("3", "风险备用金"), 
    ;

	LoanPublishGuaranteeChannelType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
	
	private final String value;
	private final String desc;

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public String getValue() {
		return value;
	}

}
