package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by wangyadong on 2016/9/27.
 */
public class SpecialBiddingEnum {

    public enum SpecialTypeEnum implements EnumsCanDescribed {
    NORMAL_BIDDING("0","普通标"),	
    SPECIAL_PASSWORD("1","定向密码"),
    SPECIAL_USER("2","定向用户"),
    SPECIAL_NEWUSER("3","新手计划"),
    ;


    private final String value;
    private final String desc;

    private SpecialTypeEnum(String value, String desc) {
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
}
