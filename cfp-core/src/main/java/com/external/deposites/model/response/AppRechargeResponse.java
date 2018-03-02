package com.external.deposites.model.response;

import com.external.deposites.api.ApiParameter;

import java.math.BigDecimal;

/**
 * <pre>
 * 充值响应体
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public class AppRechargeResponse extends AbstractResponse {
    @ApiParameter
    private String login_id;
    @ApiParameter
    private Long amt;   // 以分为单位 (无小数位)

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
}
