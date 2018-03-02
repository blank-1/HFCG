package com.external.deposites.model.fyResponse;

import java.math.BigDecimal;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.model.response.AbstractResponse;

/**
 * <pre>
 * 充值响应体
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public class FyRechargeResponse extends AbstractResponse {
    @ApiParameter
    private String login_id;
    @ApiParameter
    private Long amt;   // 以分为单位 (无小数位)
    @ApiParameter
    private String resp_desc;

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public Long getAmt() {
        return amt == null ? null : new BigDecimal(amt).movePointLeft(2).longValue();
    }

    public void setAmt(Long amt) {
        this.amt = amt;
    }

    @Override
    public String toString() {
        return "RechargeResponse{" +
                "login_id='" + login_id + '\'' +
                ", amt=" + amt +
                "} " + super.toString();
    }

	public String getResp_desc() {
		return resp_desc;
	}

	public void setResp_desc(String resp_desc) {
		this.resp_desc = resp_desc;
	}
}
