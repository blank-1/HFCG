package com.external.llpay;

import com.xt.cfp.core.pojo.RechargeOrder;

/**
 * Created by lenovo on 2015/11/13.
 */
public class LLPayRequest {

    private String url;
    private RechargeOrder rechargeOrder;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RechargeOrder getRechargeOrder() {
        return rechargeOrder;
    }

    public void setRechargeOrder(RechargeOrder rechargeOrder) {
        this.rechargeOrder = rechargeOrder;
    }
}
