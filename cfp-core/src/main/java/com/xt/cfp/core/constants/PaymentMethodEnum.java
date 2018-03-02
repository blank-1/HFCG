package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by luqinglin on 2015/6/23.
 */
public enum PaymentMethodEnum implements EnumsCanDescribed {
    AVERAGE_CAPITAL_PLUS_INTEREST("1","等额本息"),
    AVERAGE_CAPTIAL("2","等额本金");



    private final String value;
    private final String desc;

    private PaymentMethodEnum(String value, String desc) {
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


    public static PaymentMethodEnum getPaymentMethod(String value){
        PaymentMethodEnum[] values = PaymentMethodEnum.values();
        for(PaymentMethodEnum paymentMethod:values){
            if(paymentMethod.getValue().equals(value)){
                return paymentMethod;
            }
        }
        return null;
    }
}