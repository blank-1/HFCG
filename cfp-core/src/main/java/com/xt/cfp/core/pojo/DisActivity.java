package com.xt.cfp.core.pojo;

import java.util.Date;

/**
 * 分销活动表
 */
public class DisActivity {
    private Long disId;//分销活动ID

    private String disName;//分销名称

    private Short sectionCode;//期号

    private Date ruleStartDate;//规则起始时间

    private Date ruleEndDate;//规则结束时间

    private String disDiscription;//分销描述

    private String state;//状态
    
    private Date createDate ;
    
    //目标用户
    private String targetUser ;

    public Long getDisId() {
        return disId;
    }

    public void setDisId(Long disId) {
        this.disId = disId;
    }

    public String getDisName() {
        return disName;
    }

    public void setDisName(String disName) {
        this.disName = disName == null ? null : disName.trim();
    }

    public Short getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(Short sectionCode) {
        this.sectionCode = sectionCode;
    }

    public Date getRuleStartDate() {
        return ruleStartDate;
    }

    public void setRuleStartDate(Date ruleStartDate) {
        this.ruleStartDate = ruleStartDate;
    }

    public Date getRuleEndDate() {
        return ruleEndDate;
    }

    public void setRuleEndDate(Date ruleEndDate) {
        this.ruleEndDate = ruleEndDate;
    }

    public String getDisDiscription() {
        return disDiscription;
    }

    public void setDisDiscription(String disDiscription) {
        this.disDiscription = disDiscription == null ? null : disDiscription.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser.trim();
	}
    
    
}