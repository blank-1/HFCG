package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * 系统异常  如： 启动参数不对
 *
 * @author leixiongfei
 * @date 2013-7-11 下午1:44:06
 */
public enum VoucherErrorCode implements ErrorCode {
	VOUCHER_USED("0", "财富券已被处理过"),
    VOUCHER_NOT_FOUND("1", "对应的财富劵可能已被使用"),
    VOUCHER_NOT_FREEZE_STATUS("2", "财富劵不是冻结状态"),
    VOUCHER_NOT_USEAGE_STATUS("3", "财富劵不是已使用状态"),
    VOUCHER_CANNOT_CANCEL("4", "财富劵无法撤销使用"),
	;

	private String desc;
    private String code;

    private VoucherErrorCode(String code, String desc) {
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
