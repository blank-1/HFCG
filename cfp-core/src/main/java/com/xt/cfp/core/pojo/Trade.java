package com.xt.cfp.core.pojo;

import java.util.Date;

/**
 * 交易流水
 */
public class Trade {
	private Long id; // 标识
	private String serial_number; // 流水号
	private Long user_id; // 用户标识
	private String message_id; // 交易业务类型
	private String trade_operate;// 交易操作类型
	private String request_message; // 请求报文
	private String response_message; // 响应报文
	private String request_trading_account; // 请求交易存管系统登录账户
	private String request_organization; // 请求交易账户存管系统真实姓名（个人）、企业注册名称（法人）
	private Long request_trading_amount; // 请求交易金额
	private String response_trading_account; // 响应交易存管系统登录账户
	private String response_organization; // 响应交易账户存管系统真实姓名（个人）、企业注册名称（法人）
	private Long response_trading_amount; // 响应交易金额
	private String trade_status; // 交易状态
	private Date trade_date; // 添加交易时间
	private Long loanid; // 表的标识
	private Long invest_id;// 投资标识
	private String serial_number_tx; // 原交易流水号（放款时对应标的标识）
	private String fail_reason;//返回错误信息

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public String getTrade_operate() {
		return trade_operate;
	}

	public void setTrade_operate(String trade_operate) {
		this.trade_operate = trade_operate;
	}

	public String getRequest_message() {
		return request_message;
	}

	public void setRequest_message(String request_message) {
		this.request_message = request_message;
	}

	public String getResponse_message() {
		return response_message;
	}

	public void setResponse_message(String response_message) {
		this.response_message = response_message;
	}

	public String getRequest_trading_account() {
		return request_trading_account;
	}

	public void setRequest_trading_account(String request_trading_account) {
		this.request_trading_account = request_trading_account;
	}

	public String getRequest_organization() {
		return request_organization;
	}

	public void setRequest_organization(String request_organization) {
		this.request_organization = request_organization;
	}

	public Long getRequest_trading_amount() {
		return request_trading_amount;
	}

	public void setRequest_trading_amount(Long request_trading_amount) {
		this.request_trading_amount = request_trading_amount;
	}

	public String getResponse_trading_account() {
		return response_trading_account;
	}

	public void setResponse_trading_account(String response_trading_account) {
		this.response_trading_account = response_trading_account;
	}

	public String getResponse_organization() {
		return response_organization;
	}

	public void setResponse_organization(String response_organization) {
		this.response_organization = response_organization;
	}

	public Long getResponse_trading_amount() {
		return response_trading_amount;
	}

	public void setResponse_trading_amount(Long response_trading_amount) {
		this.response_trading_amount = response_trading_amount;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public Date getTrade_date() {
		return trade_date;
	}

	public void setTrade_date(Date trade_date) {
		this.trade_date = trade_date;
	}

	public Long getLoanid() {
		return loanid;
	}

	public void setLoanid(Long loanid) {
		this.loanid = loanid;
	}

	public Long getInvest_id() {
		return invest_id;
	}

	public void setInvest_id(Long invest_id) {
		this.invest_id = invest_id;
	}

	public String getSerial_number_tx() {
		return serial_number_tx;
	}

	public void setSerial_number_tx(String serial_number_tx) {
		this.serial_number_tx = serial_number_tx;
	}

	public String getFail_reason() {
		return fail_reason;
	}

	public void setFail_reason(String fail_reason) {
		this.fail_reason = fail_reason;
	}

}
