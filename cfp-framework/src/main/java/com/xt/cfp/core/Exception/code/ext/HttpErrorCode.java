package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * 和Http相关的错误码
 * User: yulei
 * Date: 14-2-13
 * Time: 下午12:41
 */
public enum HttpErrorCode implements ErrorCode{
    CAN_NOT_REDIRECT("0", "无法重定向到指定URL"),
    CAN_NOT_WRITE_CONTENT_TO_RESP("1", "无法向response中写入数据"),
    CAN_HANDLE_GET("2", "无法处理GET请求"),
    CAN_HANDLE_POST("3", "无法处理POST请求"),
    ILLEGAL_REQUEST("4", "非法请求"),
    CAN_NOT_REQUEST("5", "接口调用异常"),
    ;

    private String code;
    private String desc;

    private HttpErrorCode(String code, String desc) {
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
