package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class WithDraw {
    private Long withdrawId;

    private Long verifierId;

    private Long customerCardId;

    private Long userId;

    private Long operatorId;

    private BigDecimal withdrawAmount;

    private String remark;

    private Date createTime;

    private String verifyStatus;

    private String transStatus;

    private String verifyOpinion;

    private Date operateTime;

    private Date resultTime;

    private String resultDesc;

    private String withDrawFlowId;

    private BigDecimal commissionFee;

    private String happenType;
    
    private Date confirmWithdrawTime;


    public String getHappenType() {
        return happenType;
    }

    public void setHappenType(String happenType) {
        this.happenType = happenType;
    }

    public String getWithDrawFlowId() {
        return withDrawFlowId;
    }

    public void setWithDrawFlowId(String withDrawFlowId) {
        this.withDrawFlowId = withDrawFlowId;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public BigDecimal getCommissionFee() {
        return commissionFee;
    }

    public void setCommissionFee(BigDecimal commissionFee) {
        this.commissionFee = commissionFee;
    }

    public Long getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(Long withdrawId) {
        this.withdrawId = withdrawId;
    }

    public Long getVerifierId() {
        return verifierId;
    }

    public void setVerifierId(Long verifierId) {
        this.verifierId = verifierId;
    }

    public Long getCustomerCardId() {
        return customerCardId;
    }

    public void setCustomerCardId(Long customerCardId) {
        this.customerCardId = customerCardId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus == null ? null : verifyStatus.trim();
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus == null ? null : transStatus.trim();
    }

    public String getVerifyOpinion() {
        return verifyOpinion;
    }

    public void setVerifyOpinion(String verifyOpinion) {
        this.verifyOpinion = verifyOpinion == null ? null : verifyOpinion.trim();
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

	public Date getConfirmWithdrawTime() {
		return confirmWithdrawTime;
	}

	public void setConfirmWithdrawTime(Date confirmWithdrawTime) {
		this.confirmWithdrawTime = confirmWithdrawTime;
	}
}