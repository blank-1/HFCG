package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by luqinglin on 2015/6/23.
 */
public enum PaymentMode implements EnumsCanDescribed {
    DEDUCTION("0","划扣"),
    TRANSFER("1","转账"),
    CASH("2","现金");



    private final String value;
    private final String desc;

    private PaymentMode(String value, String desc) {
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