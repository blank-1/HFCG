package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by yulei on 2015/8/20 0020.
 */
public enum LoanApplicationLogTypeNameEnum implements EnumsCanDescribed {
    FAILBID("1", "流标log"),
    ;

    private String desc;
    private String value;

    LoanApplicationLogTypeNameEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
