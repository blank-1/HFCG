package com.xt.cfp.core.service;

import java.math.BigDecimal;

/**
 * Created by lenovo on 2015/6/29.
 */
public abstract class CommissionFee {

    public BigDecimal getFee(){
        return BigDecimal.ZERO;
    }
}
