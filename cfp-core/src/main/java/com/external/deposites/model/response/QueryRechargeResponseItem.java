package com.external.deposites.model.response;

/**
 * <pre>
 * 用户余额明细单项
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/27
 */
public class QueryRechargeResponseItem {
    private String ext_tp; // O 扩展类型以下情况必填：充值（网银充值、快速充 值）IPCZ 网银充值IPDKBB 或 IPDK 代扣充值
    private String txn_date;    // 充值􁨀现日期
    private String txn_time;    // 充值􁨀现时分
    private String mchnt_ssn; // 充值􁨀现流水
    private Long txn_amt;     // 充值􁨀现金额 以分为单位 (无小数位)
    private String fuiou_acct_no;   // 用户虚拟账户
    private String cust_no; // 用户名
    private String artif_nm;    // 用户名称
    private String remark;      // 备注
    private String txn_rsp_cd;  // 返回码
    private String rsp_cd_desc; // 返回码描述

    public boolean isSuccess() {
        return "0000".equals(this.txn_rsp_cd);
    }

    public String getExt_tp() {
        return ext_tp;
    }

    public void setExt_tp(String ext_tp) {
        this.ext_tp = ext_tp;
    }

    public String getTxn_date() {
        return txn_date;
    }

    public void setTxn_date(String txn_date) {
        this.txn_date = txn_date;
    }

    public String getTxn_time() {
        return txn_time;
    }

    public void setTxn_time(String txn_time) {
        this.txn_time = txn_time;
    }

    public String getMchnt_ssn() {
        return mchnt_ssn;
    }

    public void setMchnt_ssn(String mchnt_ssn) {
        this.mchnt_ssn = mchnt_ssn;
    }

    public Long getTxn_amt() {
        return txn_amt;
    }

    public void setTxn_amt(Long txn_amt) {
        this.txn_amt = txn_amt;
    }

    public String getFuiou_acct_no() {
        return fuiou_acct_no;
    }

    public void setFuiou_acct_no(String fuiou_acct_no) {
        this.fuiou_acct_no = fuiou_acct_no;
    }

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    public String getArtif_nm() {
        return artif_nm;
    }

    public void setArtif_nm(String artif_nm) {
        this.artif_nm = artif_nm;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTxn_rsp_cd() {
        return txn_rsp_cd;
    }

    public void setTxn_rsp_cd(String txn_rsp_cd) {
        this.txn_rsp_cd = txn_rsp_cd;
    }

    public String getRsp_cd_desc() {
        return rsp_cd_desc;
    }

    public void setRsp_cd_desc(String rsp_cd_desc) {
        this.rsp_cd_desc = rsp_cd_desc;
    }

    @Override
    public String toString() {
        return "QueryRechargeResponseItem{" +
                "ext_tp='" + ext_tp + '\'' +
                ", txn_date='" + txn_date + '\'' +
                ", txn_time='" + txn_time + '\'' +
                ", mchnt_ssn='" + mchnt_ssn + '\'' +
                ", txn_amt=" + txn_amt +
                ", fuiou_acct_no='" + fuiou_acct_no + '\'' +
                ", cust_no='" + cust_no + '\'' +
                ", artif_nm='" + artif_nm + '\'' +
                ", remark='" + remark + '\'' +
                ", txn_rsp_cd='" + txn_rsp_cd + '\'' +
                ", rsp_cd_desc='" + rsp_cd_desc + '\'' +
                '}';
    }
}
