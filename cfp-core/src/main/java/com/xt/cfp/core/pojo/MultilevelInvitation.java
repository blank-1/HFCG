package com.xt.cfp.core.pojo;

import java.util.Date;

/**
 * 多级邀请关系表
 */
public class MultilevelInvitation {
    private Long userId;//用户id

    private Long recommendUserId;//推荐用户id

    private Long saleByUserId;//所属销售的用户id

    private Long hierarchy;//层级(从1开始)

    private Date createTime;//创建时间

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRecommendUserId() {
        return recommendUserId;
    }

    public void setRecommendUserId(Long recommendUserId) {
        this.recommendUserId = recommendUserId;
    }

    public Long getSaleByUserId() {
        return saleByUserId;
    }

    public void setSaleByUserId(Long saleByUserId) {
        this.saleByUserId = saleByUserId;
    }

    public Long getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Long hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}