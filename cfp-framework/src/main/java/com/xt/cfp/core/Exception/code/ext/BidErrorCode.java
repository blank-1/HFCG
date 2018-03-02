package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * Created by yulei on 2015/7/4.
 */
public enum BidErrorCode implements ErrorCode {
    AMOUNT_ILLEGAL("0", "金额不合法"),
    FINANACE_STATUS_NOT_SELLING("1", "省心计划已不在售卖中"),
    AMOUNT_OVERFLOW("2", "金额超出范围"),
    LENDORDER_NOT_FOUND("3", "没有找到对应的订单"),
    LENDORDER_AMOUNT_NOT_ENOUGH("4", "投标金额大于剩余可投标金额"),
    LIMIT_AMOUNT_NOT_ENOUGH("5", "投标金额大于限投金额"),
    LENDORDER_AMOUNT_ZONE("6", "你选择的借款标已满标"),
    BID_STATIS_NOT_BIDDING("7", "你选择的借款标已截止，请选投其他借款标"),
    GENERATE_AGREEMENT_FAILED("8", "协议生成失败"),
    BID_STATIS_NOT_SELLING("9", "你选择的借款标不在售卖中，请选投其他借款标"),
    IS_NOT_A_FINANCE_ORDER("10", "该订单不是一个省心计划"),
    STATUS_IS_ILLEGAL("11", "状态不合法"),
    NO_CREDITRIGHTS("12", "没有相应债权"),
    NOT_NEWUSERBIDDING("13", "您还不是新手用户"),
    NOT_SPECAILPASS("14", "定向密码不可以为空"),
    NOT_NOTSPECIALUSER("15", "您还不是定向用户"),
    NOT_SPECAILPASSERROR("16", "定向密码错误"),
    NOT_RIGHT_PUBLISH_RULE_AUTO("17", "手动投标不能投仅省心标的"),
    NOT_RIGHT_PUBLISH_RULE_MANUAL("18", "省心计划不能匹配仅手动标的"),
    ;

    private String code;
    private String desc;

    BidErrorCode(String code, String desc) {
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
