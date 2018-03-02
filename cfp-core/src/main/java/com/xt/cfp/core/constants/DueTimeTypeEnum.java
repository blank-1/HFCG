package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-26.
 */
public enum DueTimeTypeEnum implements EnumsCanDescribed {
    DAY("1","按日"),
    MONTH("2","按月"),
    ;

    private final String value;
    private final String desc;

    DueTimeTypeEnum(String value, String desc) {
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
