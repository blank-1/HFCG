package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * Created by luqinglin on 2015/6/16.
 */
public enum UserAccountErrorCode implements ErrorCode {
    UNSUPPORT_OPERATE_TYPE("0", "不支持的操作类型"),
    UNSUPPORT_BUSINESS_TYPE("1", "不支持的业务类型"),
    ACCOUNT_BALANCE_VALUE_INSUFFICIENT("2", "账户资产总额不足"),
    ACCOUNT_BALANCE_AVAIVALUE_INSUFFICIENT("3", "账户可用余额不足"),
    ACCOUNT_BALANCE_FROZEVALUE_INSUFFICIENT("4", "账户冻结余额不足"),;
    private final String code;
    private final String desc;

    private UserAccountErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
