package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-2.
 */
public enum DelayStateEnum  implements EnumsCanDescribed {
    ISDELAY("1","延期"),
    NODELAY("0","未延期"),
    ;

    private final String value;
    private final String desc;

    DelayStateEnum(String value, String desc) {
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

