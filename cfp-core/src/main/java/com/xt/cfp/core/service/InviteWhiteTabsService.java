package com.xt.cfp.core.service;


import java.util.Map;
import java.util.Set;

import com.xt.cfp.core.pojo.InviteWhiteTabs;

public interface InviteWhiteTabsService {

	/**
	 * 数据插入
	 * @param Invitewhitetab
	 * */
	public void insert(InviteWhiteTabs whitetab) ;
	
	/**
	 * 数据更新
	 * @param whitetab
	 */
	public void update(InviteWhiteTabs whitetab) ;
	
	/**
	 * 根据用户id查找白名单
	 * @param userId
	 * @return
	 */
	public InviteWhiteTabs findById(Long userId);
	
	/**
	 * 根据用户ID查询白名单信息
	 * @param userId
	 * @return <InviteWhiteTabs>
	 * */
	public InviteWhiteTabs findByUserId(Long userId) ;
	/**
	 * 根据用户ID查询用户在白名单的数量
	 * @param userId
	 * @return <int> 
	 * 0 不在白名单内 ，1在白名单内
	 * */
	public int countUserId(Long userId) ;
	/**
	 * 批量导入白名单数据
	 * */
	public void insertImpotAll(Set<Map<String,Object>> userIdSet);
	/**
	 * 批量导入白名单数据--参与佣金人员
	 * */
	void insertImpotAllByUserId(Set<Long> userIdSet);
	
}
