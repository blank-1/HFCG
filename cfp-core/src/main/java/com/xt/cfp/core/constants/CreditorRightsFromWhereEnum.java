package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-29.
 */
public enum CreditorRightsFromWhereEnum implements EnumsCanDescribed {
    BUY("1","购买"),
    TURN("2","转让"),
    ;

    private final String value;
    private final String desc;

    CreditorRightsFromWhereEnum(String value, String desc) {
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
