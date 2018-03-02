package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-25.
 */
public enum FeesPointEnum implements EnumsCanDescribed {
    ATMAKELOAN("1","放款收取"),
    ATCYCLE("2","还款周期收取"),
    ATCOMPLETE("3","到期收取"),
    ATEARLYREPAYMENT("4","提前还款收取"),
    ATEARLYBEQUIT("5","提前退出"),
    ATDELAY("6","逾期收取"),
    ATDELAY_FIRSTDAY("7","首日逾期"),
    CYCLE_BACK_INTEREST("8","周期返息"),
    ATOVERFLOW("9","溢出"),
    TURNOUT("0","转让成功"),
    ;
    private final String value;
    private final String desc;

    FeesPointEnum(String value, String desc) {
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

    public final char value2Char() {
        return value.charAt(0);
    }

    public static String getDescByValue(String value) {
        for (FeesPointEnum feesPointEnum : values()) {
            if (feesPointEnum.getValue().equals(value)) {
                return feesPointEnum.getDesc();
            }
        }
        return null;
    }
}
