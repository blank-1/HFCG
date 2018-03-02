package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * Created by luqinglin on 2015/6/12.
 */
public enum MsgErrorCode implements ErrorCode {
	SMS_SEND_FAILE("0", "短信发送失败"), 
	EMAIL_SEND_FAILE("1", "邮件发送失败"), ;

	private final String code;
	private final String desc;

	private MsgErrorCode(String code, String desc) {
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
