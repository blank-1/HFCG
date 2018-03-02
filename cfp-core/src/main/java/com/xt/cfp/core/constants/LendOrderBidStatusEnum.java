package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-30.
 */
public enum LendOrderBidStatusEnum implements EnumsCanDescribed {
    BIDING("0","投标中"),
    BIDSUCCESS("1","投标成功"),
    BIDFAILURE("2","投标失败"),
    WAITING_PAY("3","待支付"),
    OUT_TIME("4","过期"),
    ;

    private final String value;
    private final String desc;

    LendOrderBidStatusEnum(String value, String desc) {
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
