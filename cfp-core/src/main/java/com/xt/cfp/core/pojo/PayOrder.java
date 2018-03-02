package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 支付订单
 */
public class PayOrder {
    private Long payId;//支付单ID
    private Long userId;//用户ID
    private String busType;//支付业务类型
    private BigDecimal amount;//支付金额
    private Date createTime;//创建时间
    private Date resultTime;//结果返回时间
    private Date doneTime;//业务处理完成时间
    private String status;//状态
    private String processStatus;//后继处理状态

    private List<PayOrderDetail> payOrderDetails;
    
    //状态：0未支付、1支付成功、2支付失败
    public static final String STATUS_NO = "0";
    public static final String STATUS_SUCCESS = "1";
    public static final String STATUS_FAIL = "2";
    
    //后继处理状态：0未处理、1处理完成、2处理失败 
    public static final String PROCESSSTATUS_NO = "0";
    public static final String PROCESSSTATUS_SUCCESS = "1";
    public static final String PROCESSSTATUS_FAIL = "2";

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType == null ? null : busType.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public Date getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(Date doneTime) {
        this.doneTime = doneTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus == null ? null : processStatus.trim();
    }

    public List<PayOrderDetail> getPayOrderDetails() {
        return payOrderDetails;
    }

    public void setPayOrderDetails(List<PayOrderDetail> payOrderDetails) {
        this.payOrderDetails = payOrderDetails;
    }
}