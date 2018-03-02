package com.external.deposites.model.fydatasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;
import com.external.deposites.model.datasource.AbstractDataSource;

/**
 * 
 * @author zuowansheng
 * 明细查询接口数据源
 *
 */
public class FyDetailDataSource extends AbstractDataSource{
	private static final long serialVersionUID = 8687100376708144740L;
	
	@ApiParameter
	@Validated(type = {HfValidations.BasicValidation.NotNull})
	private String mchnt_cd; // 15 商户代码
	@ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_txn_ssn; // 30 流水号
	@ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
	private String user_ids;
	@ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
	private String start_day;
	@ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
	private String end_day;
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
	public String getUser_ids() {
		return user_ids;
	}
	public void setUser_ids(String user_ids) {
		this.user_ids = user_ids;
	}
	public String getStart_day() {
		return start_day;
	}
	public void setStart_day(String start_day) {
		this.start_day = start_day;
	}
	public String getEnd_day() {
		return end_day;
	}
	public void setEnd_day(String end_day) {
		this.end_day = end_day;
	}
	
}
