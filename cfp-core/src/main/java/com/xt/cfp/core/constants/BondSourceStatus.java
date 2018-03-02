package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by luqinglin on 2015/6/18.
 */
public enum BondSourceStatus implements EnumsCanDescribed {
    NORMAL("0","正常"),
    DISABLED("1","禁用"),
    ;


    private final String value;
    private final String desc;

    private BondSourceStatus(String value, String desc) {
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
