package com.xt.cfp.core.service.task;

import com.xt.cfp.core.util.ReportResult;

public interface UserReportTask {

	void excute();

	ReportResult userReportText();

	ReportResult userReportUp(String text);
	
	ReportResult corpReportText();

	ReportResult corpReportUp(String text);
}
