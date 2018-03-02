package com.xt.cfp.core.service.task;

import com.xt.cfp.core.util.ReportResult;

public interface TradeReportTask {
	public void excute();

	ReportResult tradeReportTxt(String tradeType);

	ReportResult tradeReportUp(String tradeType,String text);
}
