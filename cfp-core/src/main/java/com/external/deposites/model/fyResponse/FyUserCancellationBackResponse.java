package com.external.deposites.model.fyResponse;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.model.response.AbstractResponse;

/**
 * <pre>
 * 用户注销通知
 * </pre>
 *
 * @author zuowansheng
 */
public class FyUserCancellationBackResponse extends AbstractResponse {
    
	@ApiParameter
    private String mchnt_txn_dt;
	@ApiParameter
	private String login_id;
	@ApiParameter
	private String state;
	public String getMchnt_txn_dt() {
		return mchnt_txn_dt;
	}
	public void setMchnt_txn_dt(String mchnt_txn_dt) {
		this.mchnt_txn_dt = mchnt_txn_dt;
	}
	public String getLogin_id() {
		return login_id;
	}
	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
    
}
