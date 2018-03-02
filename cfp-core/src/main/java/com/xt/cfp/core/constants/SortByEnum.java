package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-11.
 */
public enum SortByEnum  implements EnumsCanDescribed {
    ANNUALRATE("1","年利率"),
    BALANCE("2","可投金额"),
    BALANCECREATEDATE("3","剩余可投金额及提交时间"),
    TIMELIMIT("4","标的期限"),
    ;

    private final String value;
    private final String desc;

    SortByEnum(String value, String desc) {
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


