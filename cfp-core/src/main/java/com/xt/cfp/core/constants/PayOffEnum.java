package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-10.
 */
public enum PayOffEnum implements EnumsCanDescribed {
    YES("1","已结清"),
    NO("0","未结清"),
    ;

    private final String value;
    private final String desc;

    PayOffEnum(String value, String desc) {
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
