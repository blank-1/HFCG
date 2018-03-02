package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-16.
 */
public enum PublishTarget implements EnumsCanDescribed {
    FRONT("1","前台"),
    BACKGROUND("2","后台"),
    FRONT2BACKGROUND("3","前台转后台"),
    ;

    private final String value;
    private final String desc;

    PublishTarget(String value, String desc) {
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