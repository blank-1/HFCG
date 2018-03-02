package com.xt.cfp.core.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.ValidationUtil;

import jodd.util.NameValue;

import org.springframework.beans.factory.annotation.Autowired;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.MsgReadStatistics;
import com.xt.cfp.core.pojo.UserMessage;
import com.xt.cfp.core.pojo.UserRecive;
import com.xt.cfp.core.service.UserMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 站内信接收人类型  0 所有 1 借款 2 出借  3 系统  // 4 站内信对个人 和导入用户
 * 0 系统消息 1 公告  2 站内消息
 * 0 未读  1 已读
 * 0 不置顶  1置顶
 *
 * @author Administrator
 */
@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private MyBatisDao myBatisDao;

    @Override
    public Pagination<UserMessage> getAllMessageList(int pageNum, int pageSize, String name, String type,
                                                    String releaseStartTime, String releaseEndTime) {
    	Pagination<UserMessage> re = new Pagination<UserMessage>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("type", type);
        params.put("releaseStartTime", releaseStartTime);
        params.put("releaseEndTime", releaseEndTime);

        int totalCount = this.myBatisDao.count("getAllMessageListPaging", params);
        List<UserMessage> userMessageIfo = this.myBatisDao.getListForPaging("getAllMessageListPaging", params, pageNum, pageSize);

        re.setTotal(totalCount);
        re.setRows(userMessageIfo);
        return re;
    }

    @Override
    public UserMessage getMessageDetail(Long msgId) {
        if (msgId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("msgId", msgId);
        UserMessage userMessage = myBatisDao.get("MESSAGE.selectByPrimaryKey", msgId);
        if(UserMessage.USERMESSAGE_RECIVER_SYSTEM.equals(userMessage.getReciverType()))
        {
	        List<UserMessage> userMessageList = myBatisDao.getList("MESSAGE.getLoginNameList", msgId);
	        if(userMessageList.size()>0)
	        {
	        	StringBuffer loginNameStr = new StringBuffer();
	        	for(int i=0;i<userMessageList.size();i++)
	        	{
	        		if(i == 0)
	        		{
	        			loginNameStr.append(userMessageList.get(i).getLoginName());
	        		}
	        		else
	        		{
	        			loginNameStr.append(";"+userMessageList.get(i).getLoginName());
	        		}
	        	}
	        	userMessage.setLoginName(loginNameStr.toString());
	        }
    	}
        return userMessage;
    }
    
	@Override
	public UserMessage getMessageSysDetail(Long msgId) {
		if (msgId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("msgId", msgId);

        return myBatisDao.get("MESSAGE.selectBySysPrimaryKey", msgId);
	}
	
    @Override
    @Transactional
    public void cancelMessage(Long msgId, Long reciverId) {
        if (msgId == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("msgId", msgId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("msgId", msgId);
        params.put("reciverId", reciverId);
        myBatisDao.delete("USER_RECIVE.deleteByForeignKey", params);
        myBatisDao.delete("MESSAGE.deleteByPrimaryKey", msgId);
        
    }
    
    @Override
	public void topSign(Long msgId, String topSign) {
    	if(msgId == null)
    		throw new SystemException(ValidationErrorCode.ERROR_NULL).set("msgId", msgId);
    	if(topSign == null)
    		throw new SystemException(ValidationErrorCode.ERROR_NULL).set("topSign", topSign);
    	Map<String, Object> params = new HashMap<String, Object>();
        params.put("msgId", msgId);
        if(UserMessage.USERMESSAGE_TOPSIGN.equals(topSign)){
        	params.put("topSign", UserMessage.USERMESSAGE_NOTOPSIGN);
        }
        if(UserMessage.USERMESSAGE_NOTOPSIGN.equals(topSign)){
        	params.put("topSign", UserMessage.USERMESSAGE_TOPSIGN);
        }
    	myBatisDao.update("MESSAGE.updateTopSign", params);
	}

    @Override
    @Transactional 
    public void sendStationMessage(List<UserRecive> reciverIdList, String name, String content, String reciverType, String senderName, Long senderId, String imgAddress) {
    	ValidationUtil.checkRequiredPara(
                new NameValue<String, Object>("name", name),
                new NameValue<String, Object>("content", content),
                new NameValue<String, Object>("reciverType", reciverType),//导入用户是3
                new NameValue<String, Object>("senderId", senderId),
                new NameValue<String, Object>("senderName", senderName));
    	
    	UserMessage userMessageInfo = addUserMsgInfo(name, content, reciverType, senderName, senderId, "2", null, null, imgAddress);
        if (reciverIdList != null) {
            if (reciverIdList.size() > 0) {
                //添加组
                UserRecive userRecive;
                for (int i = 0; i < reciverIdList.size(); i++) {
                    userRecive = reciverIdList.get(i);
                    userRecive.setMsgId(userMessageInfo.getMsgId());
                    reciverIdList.get(i).setStatus(UserMessage.USERMESSAGE_NOREAD);
                    myBatisDao.insert("USER_RECIVE.insertSelective", userRecive);
                }
            } else {
                throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_FIELD).set("reciverIdList", reciverIdList);
            }
        }
    }

   

    @Override
    @Transactional
    public void sendNoticeMessage(String name, String content, Date endTime, Long senderId, String senderName, String topSign) {
    	
    	ValidationUtil.checkRequiredPara(
                new NameValue<String, Object>("name", name),
                new NameValue<String, Object>("content", content),
                new NameValue<String, Object>("endTime", endTime),
                new NameValue<String, Object>("senderId", senderId),
                new NameValue<String, Object>("senderName", senderName),
                new NameValue<String, Object>("topSign", topSign));
        
        UserMessage userMessageInfo = addUserMsgInfo(name, content, UserMessage.USERMESSAGE_RECIVER_ALL,  senderName, senderId, UserMessage.USERMESSAGE_TYPE_NOTICE, endTime, topSign,"");
    }

    @Override
    @Transactional
    public void sendSystemMessage(String name, String content, Long senderId, String senderName, Long reciverId) {
    	ValidationUtil.checkRequiredPara(
                new NameValue<String, Object>("name", name),
                new NameValue<String, Object>("content", content),
                new NameValue<String, Object>("senderId", senderId),
                new NameValue<String, Object>("senderName", senderName));
    	
    	UserMessage userMessageInfo = addUserMsgInfo(name, content, UserMessage.USERMESSAGE_RECIVER_SYSTEM, senderName, senderId, UserMessage.USERMESSAGE_TYPE_SYSTEM, null, null,"");
        UserRecive userRecive = new UserRecive();
        userRecive.setMsgId(userMessageInfo.getMsgId());
        userRecive.setReciverId(reciverId);
        userRecive.setStatus(UserMessage.USERMESSAGE_NOREAD);
        myBatisDao.insert("USER_RECIVE.insertSelective", userRecive);
    }

    @Override
    public Pagination<UserMessage> getSystemMessageList(int pageNum, int pageSize, String name,
                                                       String releaseStartTime, String releaseEndTime) {
    	Pagination<UserMessage> re = new Pagination<UserMessage>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("releaseStartTime", releaseStartTime);
        params.put("releaseEndTime", releaseEndTime);

        int totalCount = this.myBatisDao.count("getSystemMessageListPaging", params);
        List<UserMessage> UserMessageIfo = this.myBatisDao.getListForPaging("getSystemMessageListPaging", params, pageNum, pageSize);
       
        re.setTotal(totalCount);
        re.setRows(UserMessageIfo);
        return re;
    }

    @Override
    public MsgReadStatistics getCountNumber(Long msgId) {

        UserMessage userMessage = getMessageDetail(msgId);
        MsgReadStatistics MsgReadStatistics = myBatisDao.get("USER_RECIVE.getMsgReadStatistics", msgId);
        //todo  给接收类型  返回数量

        return MsgReadStatistics;
    }

    @Override
    public MsgReadStatistics getStatisticsUnreadNum(Long userId) {
        MsgReadStatistics MsgReadStatistics = myBatisDao.get("USER_RECIVE.getStatisticsUnreadNum", userId);
        return MsgReadStatistics;
    }

    @Override
    public Pagination<UserRecive> getStageMessageAllList(int pageNum, int pageSize, String read, String unRead,
                                                         String stationMessageType, String noticeMessage, String systemMessage, Long reciverId, String reciverType) {
    	Pagination<UserRecive> re = new Pagination<UserRecive>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("read", read);
        params.put("unRead", "unRead");
        params.put("stationMessageType", stationMessageType);
        params.put("noticeMessage", noticeMessage);
        params.put("systemMessage", systemMessage);
        params.put("reciverId", reciverId);
        params.put("reciverType", reciverType);

        int totalCount = this.myBatisDao.count("getStageMessageAllList", params);
        List<UserRecive> userReciveIfo = this.myBatisDao.getListForPaging("getStageMessageAllList", params, pageNum, pageSize);

        re.setTotal(totalCount);
        re.setRows(userReciveIfo);
        return re;
    }

    @Override
    @Transactional
    public UserMessage getReadMessage(Long reciveId, Long reciverId, Long msgId) {
    	if(msgId == null)
    		throw new SystemException(ValidationErrorCode.ERROR_NULL).set("msgId", msgId);
    	if(reciverId == null)
    		throw new SystemException(ValidationErrorCode.ERROR_NULL).set("reciverId", reciverId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("msgId", msgId);
        params.put("reciverId", reciverId);
        if(reciveId != null){
        	params.put("reciveId", reciveId);
        }
        UserRecive userReciveInfo = myBatisDao.get("USER_RECIVE.getUserRecive", params);
        UserMessage userMessage = getMessageDetail(msgId);
        Date now = new Date();
        if (userReciveInfo == null) {
            UserRecive userRecive = new UserRecive();
            userRecive.setStatus(UserMessage.USERMESSAGE_READ);
            userRecive.setViewTime(now);
            userRecive.setMsgId(msgId);
            userRecive.setReciverId(reciverId);
            myBatisDao.insert("USER_RECIVE.insertSelective", userRecive);
        } else {
            userReciveInfo.setStatus(UserMessage.USERMESSAGE_READ);
            userReciveInfo.setViewTime(now);
            myBatisDao.update("USER_RECIVE.updateByPrimaryKeySelective", userReciveInfo);
        }
        return userMessage;
    }
    
    /**
     * 新建用户消息
     * @param name
     * @param content
     * @param reciverType
     * @param senderName
     * @param senderId
     * @param type
     * @param endTime
     * @return
     */
    private UserMessage addUserMsgInfo(String name, String content, String reciverType, String senderName, Long senderId, String type, Date endTime, String topSign, String imgAddress) {
        UserMessage userMessageInfo = new UserMessage();
        Date now = new Date();
        userMessageInfo.setName(name);
        userMessageInfo.setContent(content);
        userMessageInfo.setReciverType(reciverType);
        userMessageInfo.setSenderName(senderName);
        userMessageInfo.setSenderId(senderId);
        userMessageInfo.setSendTime(now);
        userMessageInfo.setType(type);
        userMessageInfo.setImgAddress(imgAddress);
        if(topSign != null)
        	userMessageInfo.setTopSign(topSign);
        if (endTime != null)
            userMessageInfo.setEndTime(endTime);
        myBatisDao.insert("MESSAGE.insertSelective", userMessageInfo);
        return userMessageInfo;
    }

	@Override
	public Pagination<UserRecive> receptionUserMessageList(int pageNum, int pageSize, Long userId,
			String[] status, String[] messagetype, String reciverType) {
		if(userId == null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("userId", userId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("reciverType", reciverType);
		params.put("userId", userId);
		if(status != null && status.length ==2){
			params.put("status", "");
		}else{
			if(status == null){
				params.put("status", "");
			}else{
				params.put("status", status[0]);
			}
			
		}
		if(messagetype != null && messagetype.length == 3){
			params.put("messagetypes", "");
		}else{
			if(messagetype == null){
				params.put("messagetypes", "");
			}else{
				params.put("messagetypes", messagetype);
			}
			
		}
		Pagination<UserRecive> re = new Pagination<UserRecive>();
        re.setCurrentPage(pageNum);
        re.setPageSize(pageSize);
		int totalCount = this.myBatisDao.count("receptionUserMessageListPaging", params);
        List<UserRecive> userReciveIfo = this.myBatisDao.getListForPaging("receptionUserMessageListPaging", params, pageNum, pageSize);

        re.setTotal(totalCount);
        re.setRows(userReciveIfo);
        return re;
	}

	

	

}
