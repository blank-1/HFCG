package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * 格式异常代码
 * User: yulei
 * Date: 13-7-10
 * Time: 下午5:28
 */
public enum FormatErrorCode implements ErrorCode {
    NOT_LONG("0", "目标字符串不是一个整数"),
    CANNOT_FORMAT("1", "无法将目标转换为指定的类型");

    private String desc;
    private String code;

    private FormatErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
