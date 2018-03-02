package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by lenovo on 2015/6/18.
 */
public enum AttachmentIsCode implements EnumsCanDescribed {
    IS_CODE("0","打码"),
    NO_CODE("1","不打码")
    ;


    private final String value;
    private final String desc;

    private AttachmentIsCode(String value, String desc) {
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
