package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by luqinglin on 2015/6/15.
 */
public enum RechargeStatus implements EnumsCanDescribed {
    UN_RECHARGE("0", "未充值"),
    SUCCESS("1", "充值成功"),
    FAILE("2", "充值失败"),
    NULL("-1", "状态不明");

    private final String value;
    private final String desc;

    RechargeStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getValue() {
        return value;
    }


    public static RechargeStatus typeOf(String value) {
        for (RechargeStatus rechargeStatus : RechargeStatus.values()) {
            if (rechargeStatus.value.equals(value)) {
                return rechargeStatus;
            }
        }
        return RechargeStatus.NULL;
    }
}
