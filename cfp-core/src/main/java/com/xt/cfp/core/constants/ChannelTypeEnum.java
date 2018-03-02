package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-29.
 */
public enum ChannelTypeEnum implements EnumsCanDescribed {
    OFFLINE("1","线下"),
    ONLINE("2","线上"),
    ;


    private final String value;
    private final String desc;

    private ChannelTypeEnum(String value, String desc) {
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

    public long value2Long() {
        return Long.valueOf(value);
    }
    public long value2Char() {
        return value.charAt(0);
    }
}
