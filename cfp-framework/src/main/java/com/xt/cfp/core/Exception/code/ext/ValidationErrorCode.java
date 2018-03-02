package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * User: yulei
 * Date: 13-7-10
 * Time: 下午5:12
 */
public enum ValidationErrorCode implements ErrorCode {
    ERROR_REQUIRED("0", "缺少必要的参数"),
    ERROR_NULL("1", "对象不能为空"),
    ERROR_REQUIRED_FIELD("2", "缺少必要的属性值"),
    ERROR_SIGN("3", "签名验证失败"),
    ERROR_REQUIRED_ENTRY("4", "缺少必要的条目"),
    ERROR_STRING_CAN_NOT_BE_EMPTY("5", "字符串不能为空"),
    ERROR_REQUIRED_NOT_EMPTY_FIELD("6", "缺少属性值或属性值为NULL"),
    ERROR_NOT_SAME("7", "两个值不相等"),
    ERROR_STR_NOT_NUMERIC("8", "该字符串不是数字"),
    ERROR_STR_NOT_DATE("9", "该字符串不是指定格式的日期字串"),
    ERROR_TOKEN("10", "页面已过期"),
    ERROR_NOT_A_NUMRIC("11", "不是一个数字"),
    ERROR_KEY_DUPLICATE("12", "不允许出现重复的key"),
    ERROR_PARAM_ILLEGAL("13", "参数信息不合法"),
    ERROR_PUBLISH_REPART("14", "定向用户信息不匹配"),
    ERROR_PUBLISH_FAIL("15", "定向设置保存失败 "),
    ERROR_PUBLISH_EXIST("16", "已经定向设置"),
    ERROR_PUBLISH_NOTEQUALS("17", "用户名与数据库用户名不匹配"),
    ERROR_MORE_RESULTS("18", "查询出多个重复数据"),
    ;

    private String code;
    private String desc;

    private ValidationErrorCode(String code, String desc) {
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
