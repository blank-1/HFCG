package com.external.deposites.model.fydatasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;
import com.external.deposites.model.datasource.AbstractDataSource;

/**
 * 
 * @author 刘伟
 * 充值提现
 *接口数据源
 *
 */
public class FyRechargeAndWithdrawDataSource extends AbstractDataSource{
	private static final long serialVersionUID = 8687100376708144740L;
	@ApiParameter
    private String ver; //0.44
	@ApiParameter
	@Validated(type = {HfValidations.BasicValidation.NotNull})
	private String mchnt_cd; // 15 商户代码
	@ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_txn_ssn; // 30 流水号
	@ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String busi_tp; // 4 交易类型
	@ApiParameter
	private String txn_ssn;
	@ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull} ,message = "start_day 不能为空")
	private String start_time; //8 起始时间
	@ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String end_time; //8 截至时间
	@ApiParameter
	private String cust_no; //60 交易用户
	@ApiParameter
	private String txn_st; //1 交易状态
	@ApiParameter
	private String page_no; //页码
	@ApiParameter
	private String page_size; //3 每页条数
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getMchnt_cd() {
		return mchnt_cd;
	}
	public void setMchnt_cd(String mchnt_cd) {
		this.mchnt_cd = mchnt_cd;
	}
	public String getMchnt_txn_ssn() {
		return mchnt_txn_ssn;
	}
	public void setMchnt_txn_ssn(String mchnt_txn_ssn) {
		this.mchnt_txn_ssn = mchnt_txn_ssn;
	}
	public String getBusi_tp() {
		return busi_tp;
	}
	public void setBusi_tp(String busi_tp) {
		this.busi_tp = busi_tp;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getCust_no() {
		return cust_no;
	}
	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}
	public String getTxn_st() {
		return txn_st;
	}
	public void setTxn_st(String txn_st) {
		this.txn_st = txn_st;
	}
	public String getPage_no() {
		return page_no;
	}
	public void setPage_no(String page_no) {
		this.page_no = page_no;
	}
	public String getPage_size() {
		return page_size;
	}
	public void setPage_size(String page_size) {
		this.page_size = page_size;
	}
	public String getTxn_ssn() {
		return txn_ssn;
	}
	public void setTxn_ssn(String txn_ssn) {
		this.txn_ssn = txn_ssn;
	}
	
	
	
}
