package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by lenovo on 2015/6/18.
 */
public enum CustomerCardStatus implements EnumsCanDescribed {
    NORMAL("1","正常"),
    DISABLED("0","禁用"),
    ;


    private final String value;
    private final String desc;

    private CustomerCardStatus(String value, String desc) {
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
