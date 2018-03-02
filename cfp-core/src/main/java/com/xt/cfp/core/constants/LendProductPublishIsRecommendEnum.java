package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-3.
 */
public enum LendProductPublishIsRecommendEnum implements EnumsCanDescribed {
	DISRECOMMEND("0","不推荐"),
    RECOMMEND("1","推荐"),
    ;


    private final String value;
    private final String desc;

    private LendProductPublishIsRecommendEnum(String value, String desc) {
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

    public static LendProductPublishIsRecommendEnum getByValue(String value) {
        LendProductPublishIsRecommendEnum[] values = LendProductPublishIsRecommendEnum.values();
        LendProductPublishIsRecommendEnum theEnum = null;
        for (LendProductPublishIsRecommendEnum productTypeEnum:values) {
            if (value.equals(productTypeEnum.getValue())) {
                theEnum = productTypeEnum;
            }
        }
        return theEnum;
    }
}
