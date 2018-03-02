package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by luqinglin on 2015/6/15.
 */
public enum RechargeSource implements EnumsCanDescribed {
    SOURCE_RECHARGE("0","渠道充值"),


    ;


    private final String value;
    private final String desc;

    private RechargeSource(String value, String desc) {
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
