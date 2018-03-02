package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-25.
 */
public enum DistributeStatusEnum implements EnumsCanDescribed {
    WAITING("1","待分配"),
    DISTRIBUTED("2","已分配"),
    ;


    private final String value;
    private final String desc;

    private DistributeStatusEnum(String value, String desc) {
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

