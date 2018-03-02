package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class RechargeOrder {
    private Long rechargeId;

    private String rechargeCode;

    private String channelCode;

    private Long userId;

    private Long detailId;

    private Long payId;

    private BigDecimal amount;

    private String status;

    private String bankCode;

    private String cardNo;

    private Date createTime;

    private String externalNo;

    private Date resultTime;

    private String desc;

    private Long adminId;

    private Long customerCardId;

    //辅组字段
    private String encryptCardNo;//获得后四位的银行卡

    public String getEncryptCardNo() {
        if (this.getCardNo()==null||this.getCardNo()==""){
            if ("YB_TZT".equals(this.getChannelCode()))
                return null;
            else if("YB_EBK".equals(this.getChannelCode()))
                return "网银";
            else
                return "线下";
        }

        return com.xt.cfp.core.util.StringUtils.getLast4CardNo(this.getCardNo());
    }

    public void setEncryptCardNo(String encryptCardNo) {
        this.encryptCardNo = encryptCardNo;
    }

    public Long getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(Long rechargeId) {
        this.rechargeId = rechargeId;
    }

    public String getRechargeCode() {
        return rechargeCode;
    }

    public void setRechargeCode(String rechargeCode) {
        this.rechargeCode = rechargeCode == null ? null : rechargeCode.trim();
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


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

    public Long getCustomerCardId() {
        return customerCardId;
    }

    public void setCustomerCardId(Long customerCardId) {
        this.customerCardId = customerCardId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExternalNo() {
        return externalNo;
    }

    public void setExternalNo(String externalNo) {
        this.externalNo = externalNo == null ? null : externalNo.trim();
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}