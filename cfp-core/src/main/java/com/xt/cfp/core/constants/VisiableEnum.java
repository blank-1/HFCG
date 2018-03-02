package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-24.
 */
public enum VisiableEnum  implements EnumsCanDescribed {
    DISPLAY("1","显示"),
    HIDDEN("0","隐藏"),
    ;

    private final String value;
    private final String desc;

    VisiableEnum(String value, String desc) {
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
