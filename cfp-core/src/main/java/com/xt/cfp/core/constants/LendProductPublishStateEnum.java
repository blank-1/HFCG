package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-3.
 */
public enum LendProductPublishStateEnum implements EnumsCanDescribed {
    WAITING("1","未开始"),
    SELLING("2","发售中"),
    SOLDOUT("3","售罄"),
    TIMEOUT("4","过期"),
    PAUSE("5","暂停"),
    FORCESTOP("6","手动下线"),
    ;


    private final String value;
    private final String desc;

    private LendProductPublishStateEnum(String value, String desc) {
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

    public char value2Char(){
        return value.charAt(0);
    }
}
