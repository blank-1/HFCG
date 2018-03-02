package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-3.
 */
public enum LendProductTypeEnum implements EnumsCanDescribed {
    RIGHTING("1","投标类"),
    FINANCING("2","省心计划"),
    CREDITOR_RIGHTS("3","债权类"),
    ;


    private final String value;
    private final String desc;

    private LendProductTypeEnum(String value, String desc) {
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

    public static LendProductTypeEnum getByValue(String value) {
        LendProductTypeEnum[] values = LendProductTypeEnum.values();
        LendProductTypeEnum theEnum = null;
        for (LendProductTypeEnum productTypeEnum:values) {
            if (value.equals(productTypeEnum.getValue())) {
                theEnum = productTypeEnum;
            }
        }
        return theEnum;
    }
}
