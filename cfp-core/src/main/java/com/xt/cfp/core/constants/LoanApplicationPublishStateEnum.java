package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-16.
 */
public enum LoanApplicationPublishStateEnum implements EnumsCanDescribed {
    UNSUBMIT("1","未提交审核"),
    EDITDESC("2","编写发标描述"),
    AUDITING("3","发标复审中"),
    SUCCESS("4","发标复审通过"),
    REFUSE("5","驳回"),
    ;

    private final String value;
    private final String desc;

    LoanApplicationPublishStateEnum(String value, String desc) {
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
