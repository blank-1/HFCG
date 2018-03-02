package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-27.
 */
public enum AwardPointEnum implements EnumsCanDescribed {
    ATMAKELOAN("1","放款"),
    ATREPAYMENT("2","周期还款"),
    ATCOMPLETE("3","结清"),
    ;


    private final String value;
    private final String desc;

    private AwardPointEnum(String value, String desc) {
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
