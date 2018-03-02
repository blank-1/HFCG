package com.xt.cfp.core.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.external.yongyou.service.UserAccountHistoryYongYouService;
import com.xt.cfp.core.service.BidTaskForTimerService;
import com.xt.cfp.core.util.DateUtil;

/**
 * 用友报表timer实现类
 * */
@Service(value = "bidTaskForTimerService")
public class BidTaskForTimerServiceImpl implements BidTaskForTimerService {

	private Logger logger = Logger.getLogger(BidTaskForTimerServiceImpl.class) ;
	
	@Autowired
	private UserAccountHistoryYongYouService userAccountHistoryYongYouService ;
	
	@Override
	public void exportAccountHisToYongYou()  {
		Date now = new Date();
		logger.info("开始执行用友报表流水timer【"+"时间：" + DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd HH:mm:ss")+"】");
		Calendar calendar = Calendar.getInstance() ;
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		now = calendar.getTime();
		String yesterday = DateUtil.getFormattedDateUtil(now, "yyyy-MM-dd");
		try {
			userAccountHistoryYongYouService.requestAndSaveAccountHis(yesterday, yesterday);
		} catch (Exception e) {
			logger.error("执行定时任务失败，失败原因：" , e);
		}
		Date endTime = new Date();
		logger.info("结束执行用友报表流水timer【时间：" + DateUtil.getFormattedDateUtil(endTime, "yyyy-MM-dd HH:mm:ss")+"】");
	}
	
	@Override
	public void test(){
		System.out.println("test-timer---------------");
		logger.info("test-timer---------------");
	}

}
