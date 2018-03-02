package com.external.deposites.model.fyResponse;

import java.math.BigDecimal;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.model.response.AbstractResponse;

/**
 * <pre>
 * 提现通知
 * </pre>
 *
 * @author zuowansheng
 */
public class FyWithdrawalBackResponse extends AbstractResponse {
    @ApiParameter
    private Long amt;   // 以分为单位 (无小数位)
    @ApiParameter
    private String mobile_no;
    @ApiParameter
    private String remark;
    @ApiParameter
    private String mchnt_txn_dt;
    
    private String resp_code;//重写，不需验签

    public Long getAmt() {
        return amt == null ? null : new BigDecimal(amt).movePointLeft(2).longValue();
    }

    public void setAmt(Long amt) {
        this.amt = amt;
    }

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getResp_code() {
		return resp_code;
	}

	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}

	public String getMchnt_txn_dt() {
		return mchnt_txn_dt;
	}

	public void setMchnt_txn_dt(String mchnt_txn_dt) {
		this.mchnt_txn_dt = mchnt_txn_dt;
	}

}
