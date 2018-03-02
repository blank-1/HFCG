package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-11.
 */
public enum SortTypeEnum implements EnumsCanDescribed {
    ASC("1","升序"),
    DESC("2","降序"),
    ;

    private final String value;
    private final String desc;

    SortTypeEnum(String value, String desc) {
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

