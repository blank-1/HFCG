package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by yulei on 2015/7/11.
 */
public enum UserIsVerifiedEnum implements EnumsCanDescribed {
    NO("0", "未验证"),
    YES("1", "已验证"),
    BIND("2","恒丰开户"),
    ;

    private String desc;
    private String value;

    UserIsVerifiedEnum(String value, String desc) {
        this.desc = desc;
        this.value = value;
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
