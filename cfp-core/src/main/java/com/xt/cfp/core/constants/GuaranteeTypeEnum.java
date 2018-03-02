package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by luqinglin on 2015/6/23.
 */
public enum GuaranteeTypeEnum implements EnumsCanDescribed {
    PRINCIPAL_INTEREST_PROTECTION ("0","本息保障"),
    PRINCIPAL_PROTECTION("1","本金保障");



    private final String value;
    private final String desc;

    private GuaranteeTypeEnum(String value, String desc) {
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