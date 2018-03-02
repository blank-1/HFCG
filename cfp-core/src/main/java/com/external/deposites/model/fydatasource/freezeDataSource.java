package com.external.deposites.model.fydatasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;
import com.external.deposites.model.datasource.AbstractDataSource;

public class freezeDataSource  extends AbstractDataSource{
	
	private static final long serialVersionUID = 3356732454785993562L;
	@ApiParameter
    private String ver;
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_cd; // 15 商户代码
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_txn_ssn; // 30 流水号
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String cust_no; // 10-30 帐号
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String amt; // 冻结金额
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String rem; // 备注
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
	public String getCust_no() {
		return cust_no;
	}
	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getRem() {
		return rem;
	}
	public void setRem(String rem) {
		this.rem = rem;
	}
	
}
