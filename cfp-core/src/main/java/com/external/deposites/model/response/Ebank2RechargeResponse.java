package com.external.deposites.model.response;

import com.external.deposites.api.ApiParameter;

/**
 * <pre>
 * 响应体
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public class Ebank2RechargeResponse extends AbstractResponse {

    @ApiParameter
    private String login_id;
    @ApiParameter
    private Long amt;   // 以分为单位 (无小数位)
    @ApiParameter
    private String rem; // 60 备注

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

    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

    @Override
    public String toString() {
        return "Ebank2RechargeResponse{" +
                "login_id='" + login_id + '\'' +
                ", amt=" + amt +
                ", rem='" + rem + '\'' +
                "} " + super.toString();
    }
}
