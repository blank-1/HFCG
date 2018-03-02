package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
/**
 * 支付订单明细
 */
public class PayOrderDetail {
    private Long detailId;//支付明细ID
    private Long payId;//支付单ID
    private BigDecimal amount;//金额
    private String amountType;//金额类型

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType == null ? null : amountType.trim();
    }

}