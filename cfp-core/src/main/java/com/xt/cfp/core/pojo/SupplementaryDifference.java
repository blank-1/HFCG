package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lenovo on 2015/10/23.
 */
public class SupplementaryDifference {

    private Long makeUpId;

    private Long repaymentRecordId;

    private Long accId;

    private Long hisId;

    private BigDecimal amount;

    private String type;

    private Date createTime;


    public Long getMakeUpId() {
        return makeUpId;
    }

    public void setMakeUpId(Long makeUpId) {
        this.makeUpId = makeUpId;
    }

    public Long getRepaymentRecordId() {
        return repaymentRecordId;
    }

    public void setRepaymentRecordId(Long repaymentRecordId) {
        this.repaymentRecordId = repaymentRecordId;
    }

    public Long getAccId() {
        return accId;
    }

    public void setAccId(Long accId) {
        this.accId = accId;
    }

    public Long getHisId() {
        return hisId;
    }

    public void setHisId(Long hisId) {
        this.hisId = hisId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
