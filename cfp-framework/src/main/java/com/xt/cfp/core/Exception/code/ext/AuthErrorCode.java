package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * User: yulei
 * Date: 14-2-26
 * Time: 下午1:59
 */
public enum AuthErrorCode implements ErrorCode {
    CAN_NOT_ADD_SUB_RESOUCE_ON_WEDGIT("0", "不能在控件节点下建立子资源节点");

    private AuthErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
