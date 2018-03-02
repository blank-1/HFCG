package com.xt.cfp.core.pojo;

import com.xt.cfp.core.constants.VoucherConstants;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class Voucher {
    private Long voucherId;

    private Long userId;

    private Long detailId;

    private BigDecimal voucherValue;

    private String status;

    private Date createDate;

    private Date endDate;

    private String sourceType;

    private Long voucherProductId;

    private Date usageDate;

    private String sourceDesc;

    private String sourceDescStr;

    public String getSourceDescStr() {
        if (StringUtils.isEmpty(getSourceType()))
            return null;
        VoucherConstants.SourceType sourceType = VoucherConstants.SourceType.getSourceTypeByValue(this.getSourceType());
        if (sourceType.getValue().equals(VoucherConstants.SourceType.OTHER.getValue())){
            return getSourceDesc();
        }
        return sourceType.getDesc();
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getSourceDesc() {
        return sourceDesc;
    }

    public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
    }

    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(BigDecimal voucherValue) {
        this.voucherValue = voucherValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType == null ? null : sourceType.trim();
    }

    public Long getVoucherProductId() {
        return voucherProductId;
    }

    public void setVoucherProductId(Long voucherProductId) {
        this.voucherProductId = voucherProductId;
    }

    public Date getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(Date usageDate) {
        this.usageDate = usageDate;
    }
}