package com.xt.cfp.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.AppFeedBack;
import com.xt.cfp.core.service.AppFeedBackService;

@Service
public class AppFeedBackServiceImpl implements AppFeedBackService {
	
	@Autowired
	private MyBatisDao myBatisDao;
	
	/**
	 * 添加反馈
	 */
	@Override
	public AppFeedBack addAppFeedBack(AppFeedBack appFeedBack) {
		myBatisDao.insert("APP_FEED_BACK.insert", appFeedBack);
		return appFeedBack;
	}

	/**
	 * 根据ID加载一条反馈信息
	 * @param feedbackId 反馈ID
	 */
	@Override
	public AppFeedBack getAppFeedBackById(Long feedbackId) {
		return myBatisDao.get("APP_FEED_BACK.selectByPrimaryKey", feedbackId);
	}

	/**
	 * 根据用户名和创建时间获取反馈信息
	 * @param appFeedBack (param is userId and createTime of yyyy-MM-dd)
	 * @return
	 */
	@Override
	public List<AppFeedBack> getAppFeedBackByUserIdAndCreateTime(AppFeedBack appFeedBack) {
		return myBatisDao.getList("APP_FEED_BACK.getAppFeedBackByUserIdAndCreateTime", appFeedBack);
	}
}
