package com.xt.cfp.core.pojo;

import java.util.Date;

public class UserMessage {
	
	//站内信接收人类型  0 所有 1 借款 2 出借  3 系统  // 4 站内信对个人 和导入用户
	public final static String USERMESSAGE_RECIVER_ALL = "0";
	public final static String USERMESSAGE_RECIVER_LOAN = "1";
	public final static String USERMESSAGE_RECIVER_LEND = "2";
	public final static String USERMESSAGE_RECIVER_SYSTEM = "3";
	//0 系统消息 1 公告  2 站内消息
	public final static String USERMESSAGE_TYPE_SYSTEM = "0";
	public final static String USERMESSAGE_TYPE_NOTICE = "1";
	public final static String USERMESSAGE_TYPE_STATION = "2";
	//0 未读  1 已读
	public final static String USERMESSAGE_NOREAD = "0";
	public final static String USERMESSAGE_READ = "1";
	//1不置顶  0置顶
	public final static String USERMESSAGE_NOTOPSIGN = "1";
	public final static String USERMESSAGE_TOPSIGN = "0";
	
	
	
	
    private Long msgId;

    private String name;

    private String type;

    private Long senderId;

    private String senderName;

    private Date sendTime;

    private Date endTime;

    private String topSign;

    private String reciverType;

    private String content;
    
    private String loginName;
    
    private String imgAddress;
    

    public String getImgAddress() {
		return imgAddress;
	}

	public void setImgAddress(String imgAddress) {
		this.imgAddress = imgAddress;
	}

	public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName == null ? null : senderName.trim();
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTopSign() {
        return topSign;
    }

    public void setTopSign(String topSign) {
        this.topSign = topSign == null ? null : topSign.trim();
    }

    public String getReciverType() {
        return reciverType;
    }

    public void setReciverType(String reciverType) {
        this.reciverType = reciverType == null ? null : reciverType.trim();
    }

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}