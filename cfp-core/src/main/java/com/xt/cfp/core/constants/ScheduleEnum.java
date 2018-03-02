package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public enum ScheduleEnum implements EnumsCanDescribed {

    BUSINESS_OBSOLETE("0","过期作废"),
    BUSINESS_PREPARE ("1","准备中"),
    BUSINESS_WAITING ("2","等待中") ,
    BUSINESS_PART_SUCCESS ("3","部分执行成功") ,
    BUSINESS_SUCCESS ("4","成功") ,
    BUSINESS_FAILD ("5","失败") ,
    RESULT_WAITING("0","等待中") ,
    RESULT_SUCCESS("1","执行成功") ,
    RESULT_FAILD("2","执行失败") ,
    RESULT_DOING("3","执行中") ,
    RESULT_OBSOLETE("4","作废") ,
    ;

    private final String value;
    private final String desc;

    ScheduleEnum(String value, String desc) {
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
