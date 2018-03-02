package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by yulei on 2015/8/7 0007.
 */
public enum LendProductClosingType implements EnumsCanDescribed {
    CLOSINGTYPE_MONTH("1", "月"),
    CLOSINGTYPE_DAY("2", "日"),
    ;

    private String value;
    private String desc;

    LendProductClosingType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
