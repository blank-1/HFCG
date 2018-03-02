package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-13.
 */
public enum CreditorRightsChangeTypeEnum  implements EnumsCanDescribed {
    CHANGETYPE_TURNOUT("1","债权转让"),
    ;
    private final String value;
    private final String desc;

    CreditorRightsChangeTypeEnum(String value, String desc) {
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

