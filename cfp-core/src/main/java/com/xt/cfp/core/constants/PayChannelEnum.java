package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-20.
 */
public enum PayChannelEnum implements EnumsCanDescribed {
    YEEPAY("1","易宝支付"),
    CASH("2","现金"),
    BANKINGTRANSFER("3","网银转账"),
    ;



    private final String value;
    private final String desc;

    private PayChannelEnum(String value, String desc) {
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
