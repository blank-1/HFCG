package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.AppFeedBack;

public interface AppFeedBackService {
	/**
	 * 添加反馈
	 */
	AppFeedBack addAppFeedBack(AppFeedBack appFeedBack);
	
	/**
	 * 根据ID加载一条反馈信息
	 * @param feedbackId 反馈ID
	 */
	AppFeedBack getAppFeedBackById(Long feedbackId);
	
	/**
	 * 根据用户名和创建时间获取反馈信息
	 * @param appFeedBack (param is userId and createTime of yyyy-MM-dd)
	 * @return
	 */
	List<AppFeedBack> getAppFeedBackByUserIdAndCreateTime(AppFeedBack appFeedBack);
}
