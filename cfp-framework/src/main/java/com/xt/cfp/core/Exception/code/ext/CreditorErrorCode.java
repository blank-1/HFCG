package com.xt.cfp.core.Exception.code.ext;


import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * Created by Renyulin on 14-9-20 下午4:03.
 */
public enum CreditorErrorCode implements ErrorCode {

    CREDITORCANTTURN("01","债权不允许转让"),
    CREDITOR_TURNAPPLY_NOT_EXIST("03","没有申请转让记录"),
    CREDITOR_NOT_EXIST("02","债权不存在"),
    CREDITOR_TURNAPPLY_AMOUNT_ERROR("04","债权转让金额异常"),
    CREDITOR_AMOUNT_ZONE("05", "你选择的债权已满"),
    CREDITOR_RESIDUAL_MIN("06", "剩余债权不能在被购买"),
    CREDITOR_RESIDUAL_COMPARE_100("07", "用户购买完成后剩下的债权不能小于100"),
    CREDITOR_RESIDUAL_ALL("08", "购买金额必须为100的整数倍或者全部购买"),
    CREDITOR_RESIDUAL_BUY_MYSELF("09", "用户不能购买自己转让的债权"),
    CREDITOR_RESIDUAL_BUY_TOO_MUCH("10", "用户购买金额大于剩余金额"),
    CREDITOR_RESIDUAL_REVOKE("11", "转让已撤销"),
    CREDITOR_AMOUNT_ERROR("12", "请重新申请"),
    CREDITOR_AMOUNT_TIMEOUT("13", "转让已过期"),
    ;

    private CreditorErrorCode(String code, String desc) {
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
