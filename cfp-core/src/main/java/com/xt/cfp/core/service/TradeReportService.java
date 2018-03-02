package com.xt.cfp.core.service;

import java.util.List;

import com.xt.cfp.core.pojo.TradeReport;
import com.xt.cfp.core.util.PageData;

public interface TradeReportService {
	/**
	 * 添加文件报备记录，全属性添加
	 * 
	 * @param trade
	 */
	TradeReport addTradeReport(TradeReport tradeReport);

	/**
	 * 添加文件报备记录，部分属性添加
	 * 
	 * @param trade
	 * @return
	 */
	TradeReport addTradeReportSelective(TradeReport tradeReport);

	/**
	 * 修改文件报备记录，全属性修改
	 * 
	 * @param trade
	 */
	TradeReport updateByPrimaryKey(TradeReport tradeReport);

	/**
	 * 修改文件报备记录，部分属性修改
	 * 
	 * @param trade
	 * @return
	 */
	TradeReport updateByPrimaryKeySelective(TradeReport tradeReport);

	/**
	 * 根据id删除文件报备记录
	 * 
	 * @param id
	 */
	void delTradeReportById(Long id);

	/**
	 * 根据ID查询文件报备记录
	 * 
	 * @param id
	 */
	TradeReport getTradeReportById(Long id);

	/**
	 * 查询文件报备记录
	 * 
	 * @param pd
	 * @return
	 */
	List<TradeReport> selectTradeReport(PageData pd);
	
	/**
	 * 查询交易记录
	 * @param pd
	 * @return
	 */
	List<PageData> selectTrades(PageData pd);

	// ===========================个人、法人开户、项目报备数据查询===============================
	/**
	 * 查询个人开户记录
	 * 
	 * @param pd
	 * @return
	 */
	List<PageData> selectUsers(PageData pd);

	/**
	 * 查询法人开户记录
	 * 
	 * @param pd
	 * @return
	 */
	List<PageData> selectCorps(PageData pd);

	/**
	 * 查询新增项目
	 * 
	 * @param pd
	 * @return
	 */
	List<PageData> selectLoans(PageData pd);

}
