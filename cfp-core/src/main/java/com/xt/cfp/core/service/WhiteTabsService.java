package com.xt.cfp.core.service;

import java.util.Set;

import com.xt.cfp.core.pojo.WhiteTabs;

public interface WhiteTabsService {

	/**
	 * 数据插入
	 * @param whitetab
	 * */
	public void insert(WhiteTabs whitetab) ;
	/**
	 * 根据用户ID查询白名单信息
	 * @param userId
	 * @return <WhiteTabs>
	 * */
	public WhiteTabs findByUserId(Long userId) ;
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
	public void insertImpotAll(Set<Long> userIdSet);
	
	public void delete(Long userId);
	
	/**
	 * 【新需求】判断当前的邀请人 他的三级内邀请人是否有销售 无销售【1】 有销售【2】
	 * */
	public boolean isSaleInvite(Long userId);
	
}
