package com.xt.cfp.core.pojo;

import java.util.Date;

import com.xt.cfp.core.constants.WechatNoticeConstants;

/**
 * 微信公告表
 */
public class WechatNotice {
    private Long noticeId;//公告ID
    private String noticeTitle;//标题
    private String noticeContent;//内容
    private Date publishTime;//发布日期
    private Long adminId;//发送人
    private String noticeState;//状态
    private String noticeSynopsis;//简介
    
    //辅助字段
    private String adminName;

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle == null ? null : noticeTitle.trim();
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent == null ? null : noticeContent.trim();
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getNoticeState() {
        return noticeState;
    }

    public void setNoticeState(String noticeState) {
        this.noticeState = noticeState == null ? null : noticeState.trim();
    }
    
    public String getNoticeStateStr(){
    	WechatNoticeConstants.WechatNoticeStateEnum wechatNoticeStateEnum = WechatNoticeConstants.WechatNoticeStateEnum.getWechatNoticeStateEnumByValue(this.noticeState);
    	if(null != wechatNoticeStateEnum){
    		return wechatNoticeStateEnum.getDesc();
    	}else{
    		return this.noticeState;
    	}
    }

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getNoticeSynopsis() {
		return noticeSynopsis;
	}

	public void setNoticeSynopsis(String noticeSynopsis) {
		this.noticeSynopsis = noticeSynopsis;
	}
	
}