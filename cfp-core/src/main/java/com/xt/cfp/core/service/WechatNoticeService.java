package com.xt.cfp.core.service;

import java.util.Map;

import com.xt.cfp.core.constants.WechatNoticeConstants;
import com.xt.cfp.core.pojo.WechatNotice;
import com.xt.cfp.core.util.Pagination;

public interface WechatNoticeService {

	/**
	 * 添加微信公告
	 */
	void addWechatNotice(WechatNotice wechatNotice);

	/**
	 * 修改微信公告内容
	 */
	void updateWechatNotice(WechatNotice wechatNotice);
	
	/**
	 * 后台分页查询列表
	 * @param pageNo
	 * @param pageSize
	 * @param wechatNotice 对象条件
	 * @param customParams map条件
	 * @return
	 */
	Pagination<WechatNotice> getWechatNoticePaging(int pageNo, int pageSize, WechatNotice wechatNotice, Map<String, Object> customParams);
	
	/**
	 * 根据ID加载一条数据
	 * @param noticeId 公告ID
	 * @return
	 */
	WechatNotice getWechatNoticeById(Long noticeId);
	
	/**
	 * 设置公告状态
	 * @param noticeId 公告ID
	 * @param wnse 状态枚举值
	 */
	void changeWechatNoticeState(Long noticeId, WechatNoticeConstants.WechatNoticeStateEnum wnse);
	
	/**
	 * 获取最新一条公告
	 * @return
	 */
	WechatNotice getTopNewWechatNotice();
	
}
