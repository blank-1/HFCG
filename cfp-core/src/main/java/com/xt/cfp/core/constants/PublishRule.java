package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 标的发布表，发布规则字段枚举
 */
public enum PublishRule implements EnumsCanDescribed {
	ONLY_MANUAL("0","仅手动"),
	FIRST_AUTOMATIC("1","省心优先"),
	ONLY_AUTOMATIC("2","仅省心"),
    ;
	
	private final String value;
    private final String desc;

    PublishRule(String value, String desc) {
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
