package com.xt.cfp.core.service.task;

import com.xt.cfp.core.util.ReportResult;

public interface LoanReportTask {
	void excute();
	
	ReportResult loanReportTxt();
	
	ReportResult loanReportUp(String text);
}
