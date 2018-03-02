package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
/**
 * 佣金收益统计
 */
public class CommiProfit {
    private Long comiProId;//佣金统计ID

    private Long rulesId;//规则ID

    private Long userId;//推荐人用户ID

    private Long lendOrderId;//出借产品订单ID

    private BigDecimal factProfit;//已获收益

    private BigDecimal shoulProfit;//应得收益
    
    private BigDecimal  originProfit ; //原始总收益

    public Long getComiProId() {
        return comiProId;
    }

    public void setComiProId(Long comiProId) {
        this.comiProId = comiProId;
    }

    public Long getRulesId() {
        return rulesId;
    }

    public void setRulesId(Long rulesId) {
        this.rulesId = rulesId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(Long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public BigDecimal getFactProfit() {
        return factProfit;
    }

    public void setFactProfit(BigDecimal factProfit) {
        this.factProfit = factProfit;
    }

    public BigDecimal getShoulProfit() {
        return shoulProfit;
    }

    public void setShoulProfit(BigDecimal shoulProfit) {
        this.shoulProfit = shoulProfit;
    }

	public BigDecimal getOriginProfit() {
		return originProfit;
	}

	public void setOriginProfit(BigDecimal originProfit) {
		this.originProfit = originProfit;
	}
    
}