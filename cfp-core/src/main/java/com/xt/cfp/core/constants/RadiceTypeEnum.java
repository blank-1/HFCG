package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-24.
 */
public enum RadiceTypeEnum implements EnumsCanDescribed {
    PRINCIPAL("1","本金"),
    CURRENTINTEREST("2","当期利息"),
    ALLINTEREST("3","全部利息"),
    CURRENTPI("4","当期本金+利息"),
    ALLPI("5","全部本金+利息"),
    SUMPROFIT("6","当前收益"),
    ALLPROFIT("7","全部收益"),
    CURRENTPRINCIPAL("8","当期本金"),
    CURRENTLEFTPI("9","当期剩余本金和利息"),
    TRANSFERPRINCIPAL("10","转让本金"),
    ;
    private final String value;
    private final String desc;

    RadiceTypeEnum(String value, String desc) {
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

    public final int value2Int() {
        return Integer.parseInt(value);
    }

    public static String getDescByValue(String value) {
        for (RadiceTypeEnum radiceTypeEnum : values()) {
            if (radiceTypeEnum.getValue().equals(value)) {
                return radiceTypeEnum.getDesc();
            }
        }
        return null;
    }
}
