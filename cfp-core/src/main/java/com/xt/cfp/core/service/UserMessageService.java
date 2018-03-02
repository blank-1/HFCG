package com.xt.cfp.core.service;

import java.util.Date;
import java.util.List;

import com.xt.cfp.core.pojo.UserMessage;
import com.xt.cfp.core.pojo.MsgReadStatistics;
import com.xt.cfp.core.pojo.UserRecive;
import com.xt.cfp.core.util.Pagination;


/**
 * Created by wangxudong on 2015/6/15.
 */
public interface UserMessageService {

	
	/**
	 * 站内信和公告的消息列表 + 页面查询条件 + 统计读取率
	 * @param pageNum 条数
	 * @param pageSize 页数
	 * @param name 标题
	 * @param type 类型
	 * @param releaseStartTime 发布开始时间
	 * @param releaseEndTime  发布结束时间
	 * @return
	 */
	Pagination<UserMessage> getAllMessageList(int pageNum, int pageSize, String name, String type,String releaseStartTime, String releaseEndTime);	
	
	/**
	 * 详情
	 * @param msgId 消息ID
	 * @return
	 */
	UserMessage getMessageDetail(Long msgId);
	/**
	 * 详情
	 * @param msgId 消息ID
	 * @return
	 */
	UserMessage getMessageSysDetail(Long msgId);
	
	/**
	 * 撤回（公告消息和系统消息）
	 * @param msgId 消息ID
	 * @return
	 */
	void cancelMessage(Long msgId, Long reciverId);
	/**
	 * 置顶
	 * @param msgId
	 * @param topSign
	 */
	void topSign(Long msgId, String topSign);
	/**
	 * 发布站内信 （要查发布人）
	 * @param reciverIdList 导入人的信息	
	 * @param name 标题
	 * @param content 消息体
	 * @param reciverType 接收人类型
	 * @param senderName 发送人姓名
	 * @param senderId 发送人Id
	 * @return
	 */
	void sendStationMessage(List<UserRecive> reciverIdList, String name, String content, String reciverType, String senderName, Long senderId, String imgAddress);
	
	/**
	 * 发布公告 (人员类型全体) 
	 * @param name 标题
	 * @param content 消息体
	 * @param endTime 有效期
	 * @param senderId 发送人Id
	 * @param senderName 发送人名字
	 * @param topSign 是否置顶
	 * @return
	 */
	void sendNoticeMessage(String name, String content, Date endTime, Long senderId, String senderName, String topSign);
	
	/**
	 * 发送系统消息 (个人)
	 * @param name 标题
	 * @param content 消息体
	 * @param senderId 发送人Id
	 * @param senderName 发送人姓名
	 * @param reciverId 接收人Id
	 * @return
	 */
	void sendSystemMessage(String name, String content, Long senderId,String senderName, Long reciverId);
	
	/**
	 * 系统消息列表 + 页面查询条件 + 统计读取率 
	 * @param pageNum 条数	
	 * @param pageSize 页数
	 * @param name 标题
	 * @param releaseStartTime 发布开始时间
	 * @param releaseEndTime 发布结束时间
	 * @return
	 */
	Pagination<UserMessage> getSystemMessageList(int pageNum, int pageSize,String name, String releaseStartTime, String releaseEndTime);
	
	/**
	 * 获取已读和未读的统计数量
	 * @param msgId 消息ID
	 * @return
	 */
	MsgReadStatistics getCountNumber(Long msgId);

	/**
	 * 用户登录  获取未读数量
	 * @param userId
	 * @return
	 */
	MsgReadStatistics getStatisticsUnreadNum(Long userId);
	
	/**
	 * 消息列表+条件查询
	 * @param pageNum 条数
	 * @param pageSize 页数
	 * @param read 已读
	 * @param unRead 未读
	 * @param stationMessageType 站内信
	 * @param noticeMessage 公告
	 * @param systemMessage 系统消息
	 * @param reciverId 接收人Id
	 * @param reciverType 接收人类型
	 * @return
	 */
	Pagination<UserRecive> getStageMessageAllList(int pageNum, int pageSize, String read, String unRead, String stationMessageType, String noticeMessage, String systemMessage, Long reciverId, String reciverType);
	
	/**
	 * 读取信息
	 * @param reciveId 信息接收Id
	 * @param msgId 信息Id
	 * @param reciverId 接收人ID
	 * @return
	 */
	UserMessage getReadMessage(Long reciveId, Long reciverId,Long msgId);
	/**
	 * 前台消息列表
	 * @param userId
	 * @param status
	 * @param messagetype
	 * @param reciverType
	 * @return
	 */
	Pagination<UserRecive> receptionUserMessageList(int pageNum, int pageSize, Long userId, String[] status, String[] messagetype, String reciverType);
}
