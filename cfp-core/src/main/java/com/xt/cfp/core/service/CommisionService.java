package com.xt.cfp.core.service;

import java.math.BigDecimal;
import java.util.Map;

import com.xt.cfp.core.pojo.Commision;
import com.xt.cfp.core.util.Pagination;

public interface CommisionService {
	
	/***
	 * 根据用户id计算对应级别的应获佣金
	 * @author hu
	 * @param userId
	 * @param level
	 * @return 计算结果
	 */
	public BigDecimal calUserLevelProfit(Long userId,String level);
	
	/**
	 * 分页查询佣金列表
	 * @author hu
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public Pagination<Commision> findAllByPage(int pageNo, int pageSize, Map<String, Object> params);
	
	/**
	 * 插入一条佣金记录
	 * */
	public void insert(Commision commision);
}
