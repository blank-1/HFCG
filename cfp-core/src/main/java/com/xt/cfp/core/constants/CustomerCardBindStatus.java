package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by lenovo on 2015/6/18.
 */
public enum CustomerCardBindStatus implements EnumsCanDescribed {
    BINDED("1","已绑卡"),
    UNBINDING("0","未绑卡"),
    ;


    private final String value;
    private final String desc;

    private CustomerCardBindStatus(String value, String desc) {
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
