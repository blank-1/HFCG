package com.external.deposites.model.response;

import com.external.deposites.api.ApiParameter;

/**
 * <pre>
 * 冻结到冻结响应体
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/28
 */
public class TransferFreeze2FreezeResponse extends AbstractResponse {

    @ApiParameter
    private Long amt; // 请求付款冻结金额
    @ApiParameter
    private Long suc_amt; // 成功付款冻结金额

    public Long getAmt() {
        return amt;
    }

    public void setAmt(Long amt) {
        this.amt = amt;
    }

    public Long getSuc_amt() {
        return suc_amt;
    }

    public void setSuc_amt(Long suc_amt) {
        this.suc_amt = suc_amt;
    }

    @Override
    public String toString() {
        return "TransferFreeze2FreezeResponse{" +
                "amt=" + amt +
                ", suc_amt=" + suc_amt +
                "} " + super.toString();
    }
}
