package com.external.deposites.model.fyResponse;

import java.math.BigDecimal;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.model.response.AbstractResponse;

/**
 * <pre>
 * 交易通知
 * </pre>
 *
 * @author zuowansheng
 */
public class FyTradeBackResponse extends AbstractResponse {
    @ApiParameter
    private Long amt;   // 以分为单位 (无小数位)
    @ApiParameter
    private Long suc_amt;
    @ApiParameter
    private String mchnt_txn_dt;
    

    public Long getAmt() {
        return amt == null ? null : new BigDecimal(amt).movePointLeft(2).longValue();
    }

    public void setAmt(Long amt) {
        this.amt = amt;
    }

	public String getMchnt_txn_dt() {
		return mchnt_txn_dt;
	}

	public void setMchnt_txn_dt(String mchnt_txn_dt) {
		this.mchnt_txn_dt = mchnt_txn_dt;
	}

	public Long getSuc_amt() {
		return suc_amt;
	}

	public void setSuc_amt(Long suc_amt) {
		this.suc_amt = suc_amt;
	}

}
