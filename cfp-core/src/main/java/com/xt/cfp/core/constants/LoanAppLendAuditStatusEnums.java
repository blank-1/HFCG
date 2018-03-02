package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-26.
 */
public enum LoanAppLendAuditStatusEnums implements EnumsCanDescribed {
    //0未提交审核、1满标审核中、2放款审核中、3放款驳回、4放款通过
    UN_COMMIT("0", "未提交审核"),
    FULL_AUDITING("1", "满标审核中"),
    SEND_AUDITING("2", "放款审核中"),
    REJECT("3", "放款驳回"),
    PASS("4", "放款通过"),
    TRANSFER_SUCCESS("5", "打款成功"),
    ;

    private final String value;
    private final String desc;

    LoanAppLendAuditStatusEnums(String value, String desc) {
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
