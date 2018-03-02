package com.xt.cfp.core.pojo.ext.cfh;

import java.util.Date;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public class CfhUserRelation {
    private Long relationId;

    private Long userId;

    private Long cfhUserId;

    private String status;

    private Date createTime;

    private Date updateTime;

    private String reqStatu;

    private Date reqStartTime;

    private Date reqEndTime;
    
    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCfhUserId() {
        return cfhUserId;
    }

    public void setCfhUserId(Long cfhUserId) {
        this.cfhUserId = cfhUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getReqStatu() {
        return reqStatu;
    }

    public void setReqStatu(String reqStatu) {
        this.reqStatu = reqStatu == null ? null : reqStatu.trim();
    }

    public Date getReqStartTime() {
        return reqStartTime;
    }

    public void setReqStartTime(Date reqStartTime) {
        this.reqStartTime = reqStartTime;
    }

    public Date getReqEndTime() {
        return reqEndTime;
    }

    public void setReqEndTime(Date reqEndTime) {
        this.reqEndTime = reqEndTime;
    }
    
    public enum UserStatus implements EnumsCanDescribed {
		NORMAL("0", "正常"), 
		DISABLED("1", "失效"), ;

		private final String value;
		private final String desc;

		private UserStatus(String value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		@Override
		public String getDesc() {
			return desc;
		}

		@Override
		public String getValue() {
			return value;
		}
	}
    
    public enum ReqStatu implements EnumsCanDescribed {
    	SUCCESS("0", "成功"),
        FAIL("1", "失败") ;

		private final String value;
		private final String desc;

		private ReqStatu(String value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		@Override
		public String getDesc() {
			return desc;
		}

		@Override
		public String getValue() {
			return value;
		}
	}
	
}