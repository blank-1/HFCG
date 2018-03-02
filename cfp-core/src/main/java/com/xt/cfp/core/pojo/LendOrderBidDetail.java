package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ren yulin on 15-6-30.
 * 出借订单投标明细
 */
public class LendOrderBidDetail {
    private Long detailId;//投标明细ID
    private Long lendOrderId;//出借产品订单ID
    private Long loanApplicationId;//借款申请ID
    private BigDecimal buyBalance;//投标金额
    private Character status;//状态
    private Date buyDate;//投标时间
    private Long creditorRightsId;//债权ID，只有生成债权后，该字段才有值
    
    //状态：0投标中、1投标成功、2投标失败
    public static final char STATUS_ING = '0';
    public static final char STATUS_SUCCESS = '1';
    public static final char STATUS_FAIL = '2';


    public Long getCreditorRightsId() {
        return creditorRightsId;
    }

    public void setCreditorRightsId(Long creditorRightsId) {
        this.creditorRightsId = creditorRightsId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(Long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public BigDecimal getBuyBalance() {
        return buyBalance;
    }

    public void setBuyBalance(BigDecimal buyBalance) {
        this.buyBalance = buyBalance;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
}
