package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 出借产品封闭期类型
 * Created by yulei on 2015/7/2.
 */
public enum LendProductTimeLimitType implements EnumsCanDescribed {
    TIMELIMITTYPE_DAY("1", "日"),
    TIMELIMITTYPE_MONTH("2", "月");

    private String desc;
    private String value;

    LendProductTimeLimitType(String value, String desc) {
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
