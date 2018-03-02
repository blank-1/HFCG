package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * 反射相关的异常代码
 * User: yulei
 * Date: 13-7-10
 * Time: 下午6:02
 */
public enum ReflectionErrorCode implements ErrorCode {
    CANNOT_INSTANCE("0", "无法实例化对象"),
    CANNOT_SETFIELD("1", "无法为属性设定某特定的值"),
    CANNOT_POPULATE("2", "无法组装对象"),
    CANNOT_SPLIT_TO_SPCTYPE("3", "无法将字符串分割成指定类型的对象集合"),
    CANNOT_INVOKE_METHOD("4", "无法执行指定的方法");

    private String desc;
    private String code;

    private ReflectionErrorCode(String code, String desc) {
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
