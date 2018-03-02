package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by luqinglin on 2015/6/18.
 */
public enum VerifyType implements EnumsCanDescribed {
    LOAN_REVIEW("0","放款复审"),
    FULL_SCALE_REVIEW("1","满标复审"),
    ISSUE_SCALE_REVIEW("2","发标复审"),
    ;


    private final String value;
    private final String desc;

    private VerifyType(String value, String desc) {
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
