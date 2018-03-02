package com.xt.cfp.core.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by yulei on 2015/8/25 0025.
 */
public interface ReportService {

    /**
     * 导出某一日加盟商的报表数据
     * @param response
     * @param date
     */
    void exportAllianceExcel(HttpServletResponse response, String date);
    /**
     * 导出某一时间段加盟商的报表数据
     * @param response
     * @param startdate
     * @param endDate 
     */
	void exportAllianceExcelBetween(HttpServletResponse response, String startdate, String endDate);
	
	/**
	 * 导出移动端数据报表
	 * @param response
	 * @param customParams
	 */
	void exportMobileInfoExcel(HttpServletResponse response, Map<String, Object> customParams);
	
	/**
	 * 财务管理,到期还款,导出查询结果
	 * @param response
	 * @param customParams
	 */
	void exportRepaymentListExcel(HttpServletResponse response, List list);
	
	/**
	 * 财务管理,提现申请列表，导出查询结果
	 * @param response
	 * @param list
	 */
	void exportWithDrawListExcel(HttpServletResponse response, List list);
	/**
	 * 导出分销列表
	 * @param customParams
	 * @author wangyadong
	 * @param response 
	 */
	void toExportEccelProduct(HttpServletResponse response, Map<String, Object> customParams);
}
