package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by luqinglin on 2015/6/16.
 */
public enum WithDrawSource implements EnumsCanDescribed {

    SYSTEM_WITHDRAW("1","系统自动"),
    USER_WITHDRAW("2","用户手动"),
    SYSTEM_OPERT_WITHDRAW("3","系统手动")
    ;


    private final String value;
    private final String desc;

    WithDrawSource(String value, String desc) {
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
