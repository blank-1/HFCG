package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-25.
 */
public enum LenderTypeEnum implements EnumsCanDescribed {
    LENDER("1","出借人"),
    SYSTEM("2","平台"),
    THECHANNEL("3","渠道"),
    ;
    private final String value;
    private final String desc;

    LenderTypeEnum(String value, String desc) {
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
}
