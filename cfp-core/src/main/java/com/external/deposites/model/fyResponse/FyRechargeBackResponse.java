package com.external.deposites.model.fyResponse;

import java.math.BigDecimal;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.model.response.AbstractResponse;

/**
 * <pre>
 * 充值响应体
 * </pre>
 *
 * @author zuowansheng
 */
public class FyRechargeBackResponse extends AbstractResponse {
    @ApiParameter
    private Long amt;   // 以分为单位 (无小数位)
    @ApiParameter
    private String mobile_no;
    @ApiParameter
    private String remark;
    
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

}
