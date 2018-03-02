package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-8-10.
 */
public enum LoanApplicationVerifyStateEnum implements EnumsCanDescribed {
    UNSUBMIT("0","未提交审核"),
    FIRSTAUDIT("1","风控初审中"),
    SECONDAUDIT("2","风控复审中"),
    ROLLBACKBYFIRST("3","风控初审驳回"),
    ROLLBACKBYSECOND("4","风控复审驳回"),
    PASSBYSECOND("5","风控复审通过"),
    REFUSEBYFIRST("6","初审拒贷"),
    REFUSEBYSECOND("7","终审拒贷"),
            ;

    private final String value;
    private final String desc;

    LoanApplicationVerifyStateEnum(String value, String desc) {
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
