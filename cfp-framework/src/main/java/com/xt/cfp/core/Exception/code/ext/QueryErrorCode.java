package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * User: yulei
 * Date: 13-7-12
 * Time: 下午6:49
 */
public enum QueryErrorCode implements ErrorCode {

    ERROR_NOTONE("1", "查询到了不止一条记录"),
    ERROR_NOTNULL("2","已经有此条记录"),
    ERROR_NULL("3","查询不到此记录"),
    ;

    private String desc;
    private String code;

    private QueryErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
