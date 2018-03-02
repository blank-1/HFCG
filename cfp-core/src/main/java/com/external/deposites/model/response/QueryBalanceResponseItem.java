package com.external.deposites.model.response;

/**
 * <pre>
 * 用户余额明细单项
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/27
 */
public class QueryBalanceResponseItem {
    private String user_id; // 用户名
    private Long ct_balance;// 账面总余额 以分为单位
    private Long ca_balance; // 可用余额
    private Long cf_balance; // 冻结余额
    private Long cu_balance; // 未转结余额

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Long getCt_balance() {
        return ct_balance;
    }

    public void setCt_balance(Long ct_balance) {
        this.ct_balance = ct_balance;
    }

    public Long getCa_balance() {
        return ca_balance;
    }

    public void setCa_balance(Long ca_balance) {
        this.ca_balance = ca_balance;
    }

    public Long getCf_balance() {
        return cf_balance;
    }

    public void setCf_balance(Long cf_balance) {
        this.cf_balance = cf_balance;
    }

    public Long getCu_balance() {
        return cu_balance;
    }

    public void setCu_balance(Long cu_balance) {
        this.cu_balance = cu_balance;
    }

    @Override
    public String toString() {
        return "BalanceResponseItem{" +
                "user_id='" + user_id + '\'' +
                ", ct_balance=" + ct_balance +
                ", ca_balance=" + ca_balance +
                ", cf_balance=" + cf_balance +
                ", cu_balance=" + cu_balance +
                '}';
    }
}
