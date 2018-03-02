package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-14.
 */
public enum RepaymentPlanStateEnum implements EnumsCanDescribed {
    UNCOMPLETE("1","未还款"),
    PART("2","部分还款"),
    COMPLETE("3","已还清"),
    DEFAULT("4","逾期"),
    BEFORE_COMPLETE("5","提前还款"),
    ;

    private final String value;
    private final String desc;

    RepaymentPlanStateEnum(String value, String desc) {
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

    public char getValueChar(){
        return value.charAt(0);
    }
    public char value2Char() {
        return value.charAt(0);
    }
}

