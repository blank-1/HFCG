package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;


public enum WechatErrorCode implements ErrorCode {
    WECHAT_SEND_FAILE("0", "请求失败"),
    WECHAT_NO_NULL("1", "参数缺失"),
    ;

    private final String code;
    private final String desc;

    private WechatErrorCode(String code, String desc) {
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
