package com.xt.cfp.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.Trade;
import com.xt.cfp.core.service.TradeService;
import com.xt.cfp.core.util.PageData;

/**
 * 
 * @author liuwei
 *
 */
@Service
public class TradeServiceImpl implements TradeService {
	
	@Autowired
	private MyBatisDao myBatisDao;

	/**
	 * 添加交易流水，全属性添加
	 */
	@Override
	public Trade addTrade(Trade trade) {
		myBatisDao.insert("TRADE.insert", trade);
		return trade;
	}
	
	/**
	 * 添加交易流水，部分属性添加
	 */
	@Override
	public Trade addTradeSelective(Trade trade) {
		myBatisDao.insert("TRADE.insertSelective", trade);
		return trade;
	}

	/**
	 * 全属性更新
	 */
	@Override
	public Trade updateByPrimaryKey(Trade trade) {
		myBatisDao.update("TRADE.updateByPrimaryKey", trade);
		return trade;
	}
	
	/**
	 * 部分属性更新
	 */
	@Override
	public Trade updateByPrimaryKeySelective(Trade trade) {
		myBatisDao.update("TRADE.updateByPrimaryKeySelective", trade);
		return trade;
	}

	@Override
	public void delTradeById(Long id) {
		myBatisDao.delete("TRADE.deleteByPrimaryKey", id);
	}

	@Override
	public Trade getTradeById(Long id) {
		return myBatisDao.get("TRADE.selectByPrimaryKey", id);
	}

	@Override
	public List<Trade> selectTrade(Trade trade) {
		return myBatisDao.getList("TRADE.queryTrade", trade);
	}

	@Override
	public List<PageData> selectTrade4Report(PageData pd) {
		return myBatisDao.getList("TRADE.selectTrade4Report", pd);
	}
	
}
