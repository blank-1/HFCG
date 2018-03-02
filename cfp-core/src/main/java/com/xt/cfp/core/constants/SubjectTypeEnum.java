package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-26.
 */
public enum SubjectTypeEnum implements EnumsCanDescribed {
    LOAN("1","借款标"),
    CREDITOR("0","债权标"),
    CREDITOR_LOAN("2","借款及服务协议"),
    CREDITOR_TRANSFER("3","债权转让及受让协议"),
    ;

    private final String value;
    private final String desc;

    SubjectTypeEnum(String value, String desc) {
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
