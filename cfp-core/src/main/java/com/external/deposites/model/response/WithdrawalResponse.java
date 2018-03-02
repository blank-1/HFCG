package com.external.deposites.model.response;

import com.external.deposites.api.ApiParameter;

/**
 * 商户P2P免登陆提现 响应
 * @author HuYonkui
 *
 */
public class WithdrawalResponse extends AbstractResponse{
	@ApiParameter
    private Long amt;   // 金额
    @ApiParameter
    private Long login_id; // 交易用户

    public Long getAmt() {
        return amt;
    }

    public void setAmt(Long amt) {
        this.amt = amt;
    }

    public Long getLogin_id() {
        return login_id;
    }

    public void setLogin_id(Long login_id) {
        this.login_id = login_id;
    }

    @Override
    public String toString() {
        return "WithdrawalResponse{" +
                "amt=" + amt +
                ", login_id=" + login_id +
                "} " + super.toString();
    }
}
