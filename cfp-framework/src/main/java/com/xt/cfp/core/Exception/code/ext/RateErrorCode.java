package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

public enum RateErrorCode implements ErrorCode {
	RATE_NOT_EXIST("0", "加息券不存在"), 
	RATE_PRODUCT_NOT_EXIST("1", "加息券产品不存在"), 
	RATE_USEUP("2", "加息券已经使用完"), 
	RATE_NOT_IN_USE_DATE("3", "加息券不在使用日期内"), 
	RATE_TIMEOUT("4", "加息券已过期"), 
	RATE_WRONG_CYCLECOUNT("5", "该加息券不能购买此期限的标"), 
	RATE_WRONG_LOANTYPE("6", "该加息券不能购买此类型的标"), 
	RATE_WRONG_BUYBALANCE("7", "投标金额必须大于加息券的起投金额"), ;

	private String desc;
	private String code;

	private RateErrorCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getDesc() {
		return this.desc;
	}

}
