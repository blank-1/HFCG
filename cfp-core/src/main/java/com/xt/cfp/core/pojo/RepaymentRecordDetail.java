package com.xt.cfp.core.pojo;

import java.math.BigDecimal;

public class RepaymentRecordDetail {
    private Long detailId;

    private Long repaymentRecordId;

    private Long lendOrderId;

    private Long lendUserId;

    private Long rightsRepaymentDetailId;

    private int sectionCode;

    private BigDecimal factReceiveCapital;

    private BigDecimal factReceiveInterest;

    private BigDecimal factReceivePenalty;

    private BigDecimal factReceiveBalance;

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getRepaymentRecordId() {
        return repaymentRecordId;
    }

    public void setRepaymentRecordId(Long repaymentRecordId) {
        this.repaymentRecordId = repaymentRecordId;
    }

    public Long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(Long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public Long getLendUserId() {
        return lendUserId;
    }

    public void setLendUserId(Long lendUserId) {
        this.lendUserId = lendUserId;
    }

    public Long getRightsRepaymentDetailId() {
        return rightsRepaymentDetailId;
    }

    public void setRightsRepaymentDetailId(Long rightsRepaymentDetailId) {
        this.rightsRepaymentDetailId = rightsRepaymentDetailId;
    }

    public int getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(int sectionCode) {
        this.sectionCode = sectionCode;
    }

    public BigDecimal getFactReceiveCapital() {
        return factReceiveCapital;
    }

    public void setFactReceiveCapital(BigDecimal factReceiveCapital) {
        this.factReceiveCapital = factReceiveCapital;
    }

    public BigDecimal getFactReceiveInterest() {
        return factReceiveInterest;
    }

    public void setFactReceiveInterest(BigDecimal factReceiveInterest) {
        this.factReceiveInterest = factReceiveInterest;
    }

    public BigDecimal getFactReceivePenalty() {
        return factReceivePenalty;
    }

    public void setFactReceivePenalty(BigDecimal factReceivePenalty) {
        this.factReceivePenalty = factReceivePenalty;
    }

    public BigDecimal getFactReceiveBalance() {
        return factReceiveBalance;
    }

    public void setFactReceiveBalance(BigDecimal factReceiveBalance) {
        this.factReceiveBalance = factReceiveBalance;
    }
}