package com.external.deposites.model.response;

import com.external.deposites.api.ApiParameter;

import java.math.BigDecimal;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/20
 */
public class PCRechargeResponse extends AbstractResponse {
    @ApiParameter
    private String login_id;
    @ApiParameter
    private Long amt;   // 以分为单位 (无小数位)
    @ApiParameter
    private String resp_desc;

    @Override
    public String getResp_desc() {
        return resp_desc;
    }

    @Override
    public void setResp_desc(String resp_desc) {
        this.resp_desc = resp_desc;
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
        return "PCRechargeResponse{" +
                "login_id='" + login_id + '\'' +
                ", amt=" + amt +
                ", resp_desc='" + resp_desc + '\'' +
                "} " + super.toString();
    }
}
