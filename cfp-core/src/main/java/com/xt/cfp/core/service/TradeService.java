package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.Trade;
import com.xt.cfp.core.util.PageData;


/**
 * 
 * @author liuwei
 * 交易流水表接口
 */
public interface TradeService {
	/**
	 * 添加交易流水
	 * @param trade
	 */
	Trade addTrade(Trade trade);
	
	/**
	 * 添加交易流水
	 * @param trade
	 * @return
	 */
	Trade addTradeSelective(Trade trade);
	
	/**
	 * 修改交易流水
	 * @param trade
	 */
	Trade updateByPrimaryKey(Trade trade);
	
	/**
	 * 修改交易流水
	 * @param trade
	 * @return
	 */
	Trade updateByPrimaryKeySelective(Trade trade);
	
	/**
	 * 根据id删除交易流水
	 * @param id
	 */
	void delTradeById(Long id);
	
	/**
	 * 根据ID查询交易流水
	 * @param id
	 */
	Trade getTradeById(Long id);
	
	/**
	 * 查询交易流水
	 * @param trade
	 * @return
	 */
	List<Trade> selectTrade(Trade trade);
	
	/**
	 * 查询交易流水，报备用
	 * @param pd
	 * @return
	 */
	List<PageData> selectTrade4Report(PageData pd);
	
}
