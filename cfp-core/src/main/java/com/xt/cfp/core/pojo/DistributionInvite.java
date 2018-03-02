package com.xt.cfp.core.pojo;

/**
 * 分销邀请关系表
 */
public class DistributionInvite {
    private Long disInviteId;//分销邀请关系ID

    private Long userPid;//邀请人ID

    private Long userId;//被邀请人ID

    private String disLevel;//被邀请人级别

    

    public Long getDisInviteId() {
        return disInviteId;
    }

    public void setDisInviteId(Long disInviteId) {
        this.disInviteId = disInviteId;
    }

    public Long getUserPid() {
        return userPid;
    }

    public void setUserPid(Long userPid) {
        this.userPid = userPid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDisLevel() {
        return disLevel;
    }

    public void setDisLevel(String disLevel) {
        this.disLevel = disLevel == null ? null : disLevel.trim();
    }

    
}