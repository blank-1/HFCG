package com.xt.cfp.core.pojo;

import java.util.Date;

public class RealPrizeProduct {
    private Long realPrizeProduct;

    private Long adminId;

    private String prizeName;

    private Date createTime;

    private String remark;

    public Long getRealPrizeProduct() {
        return realPrizeProduct;
    }

    public void setRealPrizeProduct(Long realPrizeProduct) {
        this.realPrizeProduct = realPrizeProduct;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName == null ? null : prizeName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}