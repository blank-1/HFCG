package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

/**
 * 系统异常  如： 启动参数不对
 *
 * @author leixiongfei
 * @date 2013-7-11 下午1:44:06
 */
public enum WithDrawErrorCode implements ErrorCode {
	ERROR_AMOUNT("0", "请输入正确的提现金额"),
	MORE_THAN_HUNDRED("1", "请输入大于100元的金额!"),
	WITHDRAW_AMOUNT_NOT_ENOUGH("2", "提现金额不能大于可提现金额!"),
	WITHDRAW_TIMES_OUT("3", "提现次数超出当日最大限次!"),
	WITHDRAW_FLOW_NULL("4", "提现流水号为空!"),
	WITHDRAW_STATUS_NOT_END("5", "提现单状态不为提现失败"),
	LOANAPPLICATION_STATUS_NOT_SUPPORT("6", "借款申请状态不支持放款提现"),
	WITHDRAW_AMOUNT_OVERLIMIT("7", "提现金额超出限额!"),
	WITHDRAW_OWER_FLOW("8", "提现金额超出可用余额"),
	WITHDRAW_AMOUNT("9", "提现金额为0或为空"),
	WITHDRAW_VOUCHER_NULL("10", "没有可使用的3元财富券"),
	WITHDRAW_VERIFIED("11", "提现单已被处理过"),
	;

	private String desc;
    private String code;

    private WithDrawErrorCode(String code, String desc) {
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
