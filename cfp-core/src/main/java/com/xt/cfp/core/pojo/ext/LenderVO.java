package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by luqinglin on 2015/8/3.
 */
public class LenderVO {
    //出借人用户名
    private String lenderLoginName;
    //出借人姓名
    private String lenderRealName;
    //出借时间
    private Date lendTime;
    //结束时间
    private Date completeTime;
    //出借金额
    private BigDecimal buyPrice;
    //已还本金
    private BigDecimal factBalance;
    //未还本金
    private BigDecimal waitTotalpayMent;
    //状态
    private String rightState;

    public String getLenderLoginName() {
        return lenderLoginName;
    }

    public void setLenderLoginName(String lenderLoginName) {
        this.lenderLoginName = lenderLoginName;
    }

    public String getLenderRealName() {
        return lenderRealName;
    }

    public void setLenderRealName(String lenderRealName) {
        this.lenderRealName = lenderRealName;
    }

    public Date getLendTime() {
        return lendTime;
    }

    public void setLendTime(Date lendTime) {
        this.lendTime = lendTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getFactBalance() {
        return factBalance;
    }

    public void setFactBalance(BigDecimal factBalance) {
        this.factBalance = factBalance;
    }

    public BigDecimal getWaitTotalpayMent() {
        return waitTotalpayMent;
    }

    public void setWaitTotalpayMent(BigDecimal waitTotalpayMent) {
        this.waitTotalpayMent = waitTotalpayMent;
    }

    public String getRightState() {
        return rightState;
    }

    public void setRightState(String rightState) {
        this.rightState = rightState;
    }
}
