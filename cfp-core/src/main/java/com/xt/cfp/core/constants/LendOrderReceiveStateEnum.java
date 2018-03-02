package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-26.
 */
public enum  LendOrderReceiveStateEnum implements EnumsCanDescribed {
    UNRECEIVE("1","未返息"),
    RECEIVED("2","已返息"),
    TURNOUT("3","已转出"),
            ;


    private final String value;
    private final String desc;

    private LendOrderReceiveStateEnum(String value, String desc) {
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