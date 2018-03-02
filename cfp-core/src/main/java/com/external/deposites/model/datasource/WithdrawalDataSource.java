package com.external.deposites.model.datasource;

/**
 * 商户P2P免登陆提现
 * @author HuYonkui
 */
import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;

public class WithdrawalDataSource extends AbstractDataSource {
	@ApiParameter
	@Validated(type = { HfValidations.BasicValidation.NotNull })
	private String mchnt_cd; // 商户代码
	@ApiParameter
	@Validated(type = { HfValidations.BasicValidation.NotNull })
	private String mchnt_txn_ssn; // 流水号
	@ApiParameter
	@Validated(type = { HfValidations.BasicValidation.NotNull })
	private String login_id; // 用户登录名,存管系统个人用户登录名
	@ApiParameter
	@Validated(type = { HfValidations.BasicValidation.NotNull })
	private Long amt; // 提现金额,以分为单位 (无小数位)
	@ApiParameter
	@Validated(type = { HfValidations.BasicValidation.NotNull })
	private String page_notify_url; // 商户返回地址
	@ApiParameter
	private String back_notify_url; // 商户后台通知地址

	private String actionUrl;

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

	public String getLogin_id() {
		return login_id;
	}

	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}

	public Long getAmt() {
		return amt;
	}

	public void setAmt(Long amt) {
		this.amt = amt;
	}

	public String getPage_notify_url() {
		return page_notify_url;
	}

	public void setPage_notify_url(String page_notify_url) {
		this.page_notify_url = page_notify_url;
	}

	public String getBack_notify_url() {
		return back_notify_url;
	}

	public void setBack_notify_url(String back_notify_url) {
		this.back_notify_url = back_notify_url;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	@Override
	public String toString() {
		return "WithdrawalDataSource{" + "mchnt_cd='" + mchnt_cd + '\'' + ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\''
				+ ", login_id='" + login_id + '\'' + ", amt='" + amt + '\'' + ", page_notify_url='" + page_notify_url
				+ '\'' + ", back_notify_url='" + back_notify_url + '\'' + "} " + super.toString();
	}

}
