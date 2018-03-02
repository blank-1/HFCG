package com.external.deposites.model.datasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * <pre>
 * 查询充值参数源
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/30
 */
public class QueryRechargeWithdrawDataSource extends AbstractDataSource {
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "ver不能为空")
    private String ver; //0.44
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "商户代码不能为空")
    private String mchnt_cd; // 15 商户代码
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "流水号不能为空")
    private String mchnt_txn_ssn; // 30 流水号
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "交易类型不能为空")
    private String busi_tp;     //交易类型PW11 充值 PWTX 􁨀提现 PWTP 退票
    @ApiParameter
    private String txn_ssn;     // 交易流水
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "起始时间不能为空")
    private String start_time;  // 起始时间 格式：yyyy-MM-dd HH:mm:ss
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "截止时间不能为空")
    private String end_time;    // 截止时间 格式：yyyy-MM-dd HH:mm:ss
    @ApiParameter
    private String cust_no; // 用户
    @ApiParameter
    private String txn_st;  // 交易状态
    @ApiParameter
    private Integer page_no;        // 默认为1;
    @ApiParameter
    private Integer page_size;  // 每页条数 [10,100]之间整数;默认值:10;最大值:100;


    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getMchnt_cd() {
        return mchnt_cd;
    }

    public void setMchnt_cd(String mchnt_cd) {
        this.mchnt_cd = mchnt_cd;
    }

    public String getMchnt_txn_ssn() {
        return mchnt_txn_ssn;
    }

    public void setMchnt_txn_ssn(String mchnt_txn_ssn) {
        this.mchnt_txn_ssn = mchnt_txn_ssn;
    }

    public String getBusi_tp() {
        return busi_tp;
    }

    public void setBusi_tp(String busi_tp) {
        this.busi_tp = busi_tp;
    }

    public String getTxn_ssn() {
        return txn_ssn;
    }

    public void setTxn_ssn(String txn_ssn) {
        this.txn_ssn = txn_ssn;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    public String getTxn_st() {
        return txn_st;
    }

    public void setTxn_st(String txn_st) {
        this.txn_st = txn_st;
    }

    public Integer getPage_no() {
        return page_no;
    }

    public void setPage_no(Integer page_no) {
        this.page_no = page_no;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public enum BusiTpEnum {
        PW11("充值"),
        PWTX("提现"),
        PWTP("退票");

        private final String desc;

        public String getDesc() {
            return desc;
        }

        BusiTpEnum(String desc) {
            this.desc = desc;
        }
    }

    @Override
    public String toString() {
        return "QueryRechargeDataSource{" +
                "ver='" + ver + '\'' +
                ", mchnt_cd='" + mchnt_cd + '\'' +
                ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\'' +
                ", busi_tp='" + busi_tp + '\'' +
                ", txn_ssn='" + txn_ssn + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", cust_no='" + cust_no + '\'' +
                ", txn_st='" + txn_st + '\'' +
                ", page_no=" + page_no +
                ", page_size=" + page_size +
                "} " + super.toString();
    }
}
