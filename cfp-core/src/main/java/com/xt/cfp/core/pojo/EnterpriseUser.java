package com.xt.cfp.core.pojo;

import java.util.Date;

/**
 * 企业人员关联表
 */
public class EnterpriseUser {
    private Long enterpriseUserId;
    private Long enterpriseId;
    private Long userId;
    private Date createTime;

    public Long getEnterpriseUserId() {
        return enterpriseUserId;
    }

    public void setEnterpriseUserId(Long enterpriseUserId) {
        this.enterpriseUserId = enterpriseUserId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}