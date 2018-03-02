package com.external.yongyou.service;

import java.util.List;
import java.util.Map;

import com.external.yongyou.entity.UserAccountHistory;

public interface UserAccountHistoryYongYouService {

	/**
	 * 查询所有用友报表流水id
	 * */
	public Map<Integer, Integer> queryAllHisId(boolean lock);

	/**
	 * 保存用友报表list数据
	 * */
	void saveAll(List<UserAccountHistory> list) throws Exception;
	
	/**
	 * 请求cfp-web并保存流水数据
	 * */
	void requestAndSaveAccountHis(String startTime,String endTime) throws Exception ;

}
