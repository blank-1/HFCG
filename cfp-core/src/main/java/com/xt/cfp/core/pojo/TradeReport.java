package com.xt.cfp.core.pojo;

import java.util.Date;

/**
 * 报备记录
 *
 */
public class TradeReport {
	private Long id;
	private String file_num;
	private String report_type;
	private String report_status;
	private Date create_date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFile_num() {
		return file_num;
	}

	public void setFile_num(String file_num) {
		this.file_num = file_num;
	}

	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

	public String getReport_status() {
		return report_status;
	}

	public void setReport_status(String report_status) {
		this.report_status = report_status;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	@Override
	public String toString() {
		return "TradeReport [id=" + id + ", file_num=" + file_num + ", report_type=" + report_type + ", report_status="
				+ report_status + ", create_date=" + create_date + "]";
	}

}
