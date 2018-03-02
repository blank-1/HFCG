package com.external.deposites.model.response;

/**
 * <pre>
 * 用户余额明细单项
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/27
 */
public class QueryBusinessResponseItem {
    private String ext_tp; // O 扩展类型以下情况必填：充值（网银充值、快速充 值）IPCZ 网银充值IPDKBB 或 IPDK 代扣充值
    private String txn_date;    // 交易日期
    private String txn_time;    // 交易时分
    private String src_tp;      //交易请求方式
    private String mchnt_ssn; // 交易流水
    private Long txn_amt;     // 交易金额 以分为单位 (无小数位)
    private Long txn_amt_suc;      //成功交易金额
    private String contract_no;     //合同号
    private String out_fuiou_acct_no;   //出账用户虚拟账户
    private String out_cust_no; // 出账用户名
    private String out_artif_nm;    //出用户名称
    private String in_fuiou_acct_no;   //入账用户虚拟账户
    private String in_cust_no; // 入账用户名
    private String in_artif_nm;    //入用户名称
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

    public String getSrc_tp() {
        return src_tp;
    }

    public void setSrc_tp(String src_tp) {
        this.src_tp = src_tp;
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

    public Long getTxn_amt_suc() {
        return txn_amt_suc;
    }

    public void setTxn_amt_suc(Long txn_amt_suc) {
        this.txn_amt_suc = txn_amt_suc;
    }

    public String getContract_no() {
        return contract_no;
    }

    public void setContract_no(String contract_no) {
        this.contract_no = contract_no;
    }

    public String getOut_fuiou_acct_no() {
        return out_fuiou_acct_no;
    }

    public void setOut_fuiou_acct_no(String out_fuiou_acct_no) {
        this.out_fuiou_acct_no = out_fuiou_acct_no;
    }

    public String getOut_cust_no() {
        return out_cust_no;
    }

    public void setOut_cust_no(String out_cust_no) {
        this.out_cust_no = out_cust_no;
    }

    public String getOut_artif_nm() {
        return out_artif_nm;
    }

    public void setOut_artif_nm(String out_artif_nm) {
        this.out_artif_nm = out_artif_nm;
    }

    public String getIn_fuiou_acct_no() {
        return in_fuiou_acct_no;
    }

    public void setIn_fuiou_acct_no(String in_fuiou_acct_no) {
        this.in_fuiou_acct_no = in_fuiou_acct_no;
    }

    public String getIn_cust_no() {
        return in_cust_no;
    }

    public void setIn_cust_no(String in_cust_no) {
        this.in_cust_no = in_cust_no;
    }

    public String getIn_artif_nm() {
        return in_artif_nm;
    }

    public void setIn_artif_nm(String in_artif_nm) {
        this.in_artif_nm = in_artif_nm;
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
        return "QueryBusinessResponseItem{" +
                "ext_tp='" + ext_tp + '\'' +
                ", txn_date='" + txn_date + '\'' +
                ", txn_time='" + txn_time + '\'' +
                ", src_tp='" + src_tp + '\'' +
                ", mchnt_ssn='" + mchnt_ssn + '\'' +
                ", txn_amt=" + txn_amt +
                ", txn_amt_suc=" + txn_amt_suc +
                ", contract_no=" + contract_no +
                ", out_fuiou_acct_no='" + out_fuiou_acct_no + '\'' +
                ", out_cust_no='" + out_cust_no + '\'' +
                ", out_artif_nm='" + out_artif_nm + '\'' +
                ", in_fuiou_acct_no='" + in_fuiou_acct_no + '\'' +
                ", in_cust_no='" + in_cust_no + '\'' +
                ", in_artif_nm='" + in_artif_nm + '\'' +
                ", remark='" + remark + '\'' +
                ", txn_rsp_cd='" + txn_rsp_cd + '\'' +
                ", rsp_cd_desc='" + rsp_cd_desc + '\'' +
                '}';
    }
}
