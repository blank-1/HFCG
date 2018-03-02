package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-30.
 */
public enum DisplayEnum implements EnumsCanDescribed {
    DISPLAY("1","显示"),
    HIDDEN("2","不显示"),
    ;


    private final String value;
    private final String desc;

    private DisplayEnum(String value, String desc) {
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
