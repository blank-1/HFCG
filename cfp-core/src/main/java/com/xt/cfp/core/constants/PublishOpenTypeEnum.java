package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-18.
 */
public enum PublishOpenTypeEnum implements EnumsCanDescribed {
    NOWOPEN("0","即时开标"),
    SPECOPEN("1","定时开标"),
    ;

    private final String value;
    private final String desc;

    PublishOpenTypeEnum(String value, String desc) {
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
