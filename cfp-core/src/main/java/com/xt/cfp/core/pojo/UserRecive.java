package com.xt.cfp.core.pojo;

import java.util.Date;

public class UserRecive extends UserMessage{
    private Long reciveId;

    private Long msgId;

    private Long reciverId;

    private String status;

    private Date viewTime;

    public Long getReciveId() {
        return reciveId;
    }

    public void setReciveId(Long reciveId) {
        this.reciveId = reciveId;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Long getReciverId() {
        return reciverId;
    }

    public void setReciverId(Long reciverId) {
        this.reciverId = reciverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getViewTime() {
        return viewTime;
    }

    public void setViewTime(Date viewTime) {
        this.viewTime = viewTime;
    }
}