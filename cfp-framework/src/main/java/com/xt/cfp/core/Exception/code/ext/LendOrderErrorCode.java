package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * User: yulei
 * Date: 14-2-26
 * Time: 下午1:59
 */
public enum LendOrderErrorCode implements ErrorCode {
    NOT_SUPPORT_RIGHTING_ORDER("0", "不支持投标订单"),
    NOT_MATCH_LENDERORDER_PRODUCTTYPE("1", "订单类型不匹配"),
    EXPIRES_ORDER_AGREEMENT_ENDDATE("2","订单合同截止日期到期"),
    FAIL_ORDER_AGREEMENT("3","省心计划生成合同失败"),
    FAIL_ORDER_STATE("4","订单状态不匹配"),
    ;

    private LendOrderErrorCode(String code, String desc) {
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
