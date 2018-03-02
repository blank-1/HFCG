package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 企业类型
 */
public enum EnterpriseTypeEnum implements EnumsCanDescribed {
	factoring("0","保理"),
    other("1","其它"),
    company("2","资管公司"),
    ;

    private final String value;
    private final String desc;

    private EnterpriseTypeEnum(String value, String desc) {
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
