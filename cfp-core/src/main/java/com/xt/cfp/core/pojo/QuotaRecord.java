package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 额度记录表
 */
public class QuotaRecord {
    private Long quotaRecordId;//额度ID
    private Long enterpriseId;//企业ID
    private Date contractBegin;//合同开始期限
    private Date contractEnd;//合同结束期限
    private BigDecimal singleMaximumAmount;//单笔最大额度
    private BigDecimal annualMaximumLimit;//年度最大限额
    private Date createTime;//创建时间
    private Date lastUpdateTime;//最后修改时间

    public Long getQuotaRecordId() {
        return quotaRecordId;
    }

    public void setQuotaRecordId(Long quotaRecordId) {
        this.quotaRecordId = quotaRecordId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Date getContractBegin() {
        return contractBegin;
    }

    public void setContractBegin(Date contractBegin) {
        this.contractBegin = contractBegin;
    }

    public Date getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(Date contractEnd) {
        this.contractEnd = contractEnd;
    }

    public BigDecimal getSingleMaximumAmount() {
        return singleMaximumAmount;
    }

    public void setSingleMaximumAmount(BigDecimal singleMaximumAmount) {
        this.singleMaximumAmount = singleMaximumAmount;
    }

    public BigDecimal getAnnualMaximumLimit() {
        return annualMaximumLimit;
    }

    public void setAnnualMaximumLimit(BigDecimal annualMaximumLimit) {
        this.annualMaximumLimit = annualMaximumLimit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}