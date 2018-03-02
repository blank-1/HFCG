package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * 通用支付异常代码
 * User: yulei
 * Date: 13-7-9
 * Time: 上午11:40
 */
public enum PayErrorCode implements ErrorCode {
    NOT_SC_STATE("0", "请求没有收到成功的响应"),
    CONFIG_NOT_FOUND_FOR_TYPECODE("1", "没有找到对应类型的支付"),
    CONFIG_NOT_FOUND_FOR_SYSCODE("2", "没有找到对应系统的配置"),
    BANK_NOT_FOUND_FOR_BANKCODE("3", "没有找到对应银行代码的银行信息"),
    PLATBANK_NOT_FOUND("4", "没有找到对应的第三方平台银行信息"),
    CANNOT_SEND_PAYREQ("5", "转发支付请求失败"),
    ORDER_NOT_FOUND_FOR_ORDERNO("6", "没有找到对应支付单编号的支付单"),
    ORDER_HAS_HANDLED("7", "该支付订单被处理过"),
    PLATBANK_NEEDONE("8", "没有获取到任何第三方支付平台的银行信息"),
    CANNOT_DO_BUS_CALLBACK("9", "无法进行对业务系统的回调"),
    BANK_NOT_FOUND_BANKID("10", "没有找到对应银行id的银行信息"),
    SINGLE_ORDER_QUERY_ERROR("11", "进行单笔订单查询时出现异常"),
    CANNOT_SEND_SUCCESSMSG("12", "无法向第三方支付平台发送消息已处理信息"),
    GET_WRONG_RESPONSE("13", "获得了错误的支付响应"),
    CONFIG_NOT_FOUND_FOR_REQUEST("14", "没有找到合适的响应解析器"),
    CANNOT_RESOLVE_CALLBACK_RESPONSE("15", "无法解析第三方的回调响应"),
    ORDER_IS_HANDLING("16", "该支付订单正在被处理"),
    WRONG_PAY_ORDER_DETAIL_STATUS("17", "错误的支付明细状态"),
    ORDER_NOT_FOUND_FOR_ORDERID("18", "没有找到对应订单ID的订单数据"),
    ERROR_RELATION("19", "错误的充值、支付对应关系"),
    PAYORDER_NOT_INCLUD_RECHARGE_DETAIL("19", "支付订单中不包含充值明细"),
    RECHARGE_AMOUNT_NOT_EQUALS_PAYORDER_DEATIL_RECHARGE_AMOUNT("20", "与支付订单的支付明细金额不相等"),
    RECHARGE_AMOUNT_NOT_EQUALS("21", "第三方返回的充值金额与充值订单金额不等"),
    WRONG_PAY_ORDER_STATUS("22", "错误的支付订单状态"),
    NOT_BELONG_PAY_ORDER("23", "此订单不属于您"),
    NOT_EXIST_PAY_ORDER("24", "订单不存在"),
    VOUCHER_PAY_ORDER("25", "财富券变现失败"),
    ;

    private String desc;
    private String code;

    PayErrorCode(String code, String desc) {
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
