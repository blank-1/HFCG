package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-13.
 */
public enum FeesItemTypeEnum implements EnumsCanDescribed {
    ITEMTYPE_CONSULT("1","咨询费"),
    ITEMTYPE_AUDITING("2","审核费"),
    ITEMTYPE_SERVICE("3","服务费"),
    ITEMTYPE_EARLYREPAYMENT("4","提前还款"),
    ITEMTYPE_DELAYREPAYMENT("5","逾期费"),
    ITEMTYPE_DEFAULTINTREST("6","罚息"),
    ITEMTYPE_WORKFLOW("7","平台费"),
    ITEMTYPE_OVERFLOW("8","溢出费"),
    ;
    private final String value;
    private final String desc;

    FeesItemTypeEnum(String value, String desc) {
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

    /**
     * 根据value获取描述
     * @param value
     * @return
     */
    public static String getDescByValue(String value) {
        for (FeesItemTypeEnum feesItemTypeEnum : FeesItemTypeEnum.values()) {
            if (feesItemTypeEnum.getValue().equals(value)) {
                return feesItemTypeEnum.getDesc();
            }
        }
        return null;
    }
}

