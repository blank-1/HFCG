package com.external.deposites.model.response;

import com.external.deposites.api.ApiParameter;
import com.xt.cfp.core.util.BigDecimalUtil;

import java.math.BigDecimal;

/**
 * <pre>
 * 充值响应体
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public class RechargeResponse extends AbstractResponse {
    @ApiParameter
    private String login_id;
    @ApiParameter
    private Long amt;   // 以分为单位 (无小数位)
    @ApiParameter
    private String rem; // 这个只有网银充值用，app不能用



    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

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
                ", rem='" + rem + '\'' +
                "} " + super.toString();
    }
}
