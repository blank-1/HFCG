package com.xt.cfp.core.constants;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by luqinglin on 2015/6/16.
 */
public enum VerifyStatus implements EnumsCanDescribed {

    APPROVAL("0","待审核"),
    PAST("1","审核通过"),
    UN_PAST("2","驳回"),
    ;


    private final String value;
    private final String desc;

    VerifyStatus(String value, String desc) {
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
