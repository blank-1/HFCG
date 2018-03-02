package com.external.deposites.model.fyResponse;

/**
 * <pre>
 * 明细查询明细单项
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/27
 */
public class FyDetailResponseItem {
    private String mchnt_cd; 
    private String mchnt_txn_ssn; 
    private String user_ids;
    private String start_day;
    private String end_day;
    private String signature;
	public String getMchnt_cd() {
		return mchnt_cd;
	}
	public String getMchnt_txn_ssn() {
		return mchnt_txn_ssn;
	}
	public String getUser_ids() {
		return user_ids;
	}
	public String getStart_day() {
		return start_day;
	}
	public String getEnd_day() {
		return end_day;
	}
	public String getSignature() {
		return signature;
	}
	public void setMchnt_cd(String mchnt_cd) {
		this.mchnt_cd = mchnt_cd;
	}
	public void setMchnt_txn_ssn(String mchnt_txn_ssn) {
		this.mchnt_txn_ssn = mchnt_txn_ssn;
	}
	public void setUser_ids(String user_ids) {
		this.user_ids = user_ids;
	}
	public void setStart_day(String start_day) {
		this.start_day = start_day;
	}
	public void setEnd_day(String end_day) {
		this.end_day = end_day;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
