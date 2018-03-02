package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-13.
 */
public enum FeesDetailEnum  implements EnumsCanDescribed {
    UNPAY("1","未缴清"),
    PAID("2","已缴清"),
    WAIVER("3","减免"),
    ;
    private final String value;
    private final String desc;

    FeesDetailEnum(String value, String desc) {
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
