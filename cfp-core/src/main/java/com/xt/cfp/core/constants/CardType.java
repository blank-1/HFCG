package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by lenovo on 2015/6/18.
 */
public enum CardType implements EnumsCanDescribed {
    MONEY_CARD("1","打款卡"),
    DRAW_CARD("2","划扣卡"),
    FULL_CARD("3","全能卡"),
    ;


    private final String value;
    private final String desc;

    private CardType(String value, String desc) {
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
