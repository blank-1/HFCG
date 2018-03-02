package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-25.
 */
public enum LoanApplicationStateEnum implements EnumsCanDescribed {
    DRAFT("0","草稿"),
    AUDITING("1","风控审核中"),
    PUBLISHAUDITING("2","发标审核中"),
    BIDING("3","投标中"),
    LOANAUDIT("4","放款审核中"),
    WAITMAKELOANAUDIT("5","待放款"),
    REPAYMENTING("6","还款中"),
    COMPLETED("7","已结清"),
    EARLYCOMPLETE("8","已结清(提前还贷)"),
    DELETED("9","取消"),
    FAILURE("A","流标"),
    ;

    private final String value;
    private final String desc;

    LoanApplicationStateEnum(String value, String desc) {
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

    public static LoanApplicationStateEnum getByValue(String value) {
        LoanApplicationStateEnum theEnum = null;
        LoanApplicationStateEnum[] values = LoanApplicationStateEnum.values();
        for (LoanApplicationStateEnum anEnum:values) {
            if (anEnum.getValue().equals(value)) {
                theEnum = anEnum;
            }
        }
        return theEnum;
    }
}
