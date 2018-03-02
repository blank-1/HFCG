package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-25.
 */
public enum CreditorMakeLoanStateEnum implements EnumsCanDescribed {
    UNMAKELOAN("1","未放款"),
    MAKELOANED("2","已放款"),
    ;
    private final String value;
    private final String desc;

    CreditorMakeLoanStateEnum(String value, String desc) {
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

    public final int value2Int() {
        return Integer.parseInt(value);
    }
}
