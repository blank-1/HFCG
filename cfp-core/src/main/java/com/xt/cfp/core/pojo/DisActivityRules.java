package com.xt.cfp.core.pojo;

import java.math.BigDecimal;

/**
 * 规则表
 */
public class DisActivityRules {
    private Long rulesId;//规则ID

    private Long lendProductId;//出借产品ID

    private Long disId;//分销活动ID

    private BigDecimal salesPointStart;//销售额起点

    private BigDecimal salesPointEnd;//销售额终点

    private String commiPaidNode;//佣金发放节点

    private BigDecimal firstRate;//一级佣金比例

    private BigDecimal secondRate;//二级佣金比例

    private BigDecimal thirdRate;//三级佣金比例

    private String commiRatioType;//佣金基数类型

    public Long getRulesId() {
        return rulesId;
    }

    public void setRulesId(Long rulesId) {
        this.rulesId = rulesId;
    }

    public Long getLendProductId() {
        return lendProductId;
    }

    public void setLendProductId(Long lendProductId) {
        this.lendProductId = lendProductId;
    }

    public Long getDisId() {
        return disId;
    }

    public void setDisId(Long disId) {
        this.disId = disId;
    }

    public BigDecimal getSalesPointStart() {
        return salesPointStart;
    }

    public void setSalesPointStart(BigDecimal salesPointStart) {
        this.salesPointStart = salesPointStart;
    }

    public BigDecimal getSalesPointEnd() {
        return salesPointEnd;
    }

    public void setSalesPointEnd(BigDecimal salesPointEnd) {
        this.salesPointEnd = salesPointEnd;
    }

    public String getCommiPaidNode() {
        return commiPaidNode;
    }

    public void setCommiPaidNode(String commiPaidNode) {
        this.commiPaidNode = commiPaidNode == null ? null : commiPaidNode.trim();
    }

    public BigDecimal getFirstRate() {
        return firstRate;
    }

    public void setFirstRate(BigDecimal firstRate) {
        this.firstRate = firstRate;
    }

    public BigDecimal getSecondRate() {
        return secondRate;
    }

    public void setSecondRate(BigDecimal secondRate) {
        this.secondRate = secondRate;
    }

    public BigDecimal getThirdRate() {
        return thirdRate;
    }

    public void setThirdRate(BigDecimal thirdRate) {
        this.thirdRate = thirdRate;
    }

    public String getCommiRatioType() {
        return commiRatioType;
    }

    public void setCommiRatioType(String commiRatioType) {
        this.commiRatioType = commiRatioType == null ? null : commiRatioType.trim();
    }
}