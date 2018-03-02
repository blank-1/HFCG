package com.external.deposites.model.datasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;

/**
 * <pre>
 * 查询充值参数源
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/30
 */
public class QueryBusinessDataSource extends AbstractDataSource {
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "商户代码不能为空")
    private String mchnt_cd; // 15 商户代码
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "流水号不能为空")
    private String mchnt_txn_ssn; // 30 流水号
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "交易类型不能为空")
    private String busi_tp;    // PWPC 转账 PW13 预授权 PWCF 预授权撤销 PW03 划拨 PW14 转账冻结 PW15 划拨冻结 PWDJ 冻结 PWJD 解冻 PW19 冻结付款到冻结
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "起始时间不能为空")
    private String start_day;  // 起始时间 格式：yyyyMMdd
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "截止时间不能为空")
    private String end_day;    // 截止时间 格式：yyyyMMdd
    @ApiParameter
    private String txn_ssn; // 交易流水
    @ApiParameter
    private String cust_no; // 用户
    @ApiParameter
    private String txn_st;  // 交易状态
    @ApiParameter
    private String remark;  //交易备注
    @ApiParameter
    private Integer page_no;        // 默认为1;
    @ApiParameter
    private Integer page_size;  // 每页条数 [10,100]之间整数;默认值:10;最大值:100;


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

    public String getStart_day() {
        return start_day;
    }

    public void setStart_day(String start_day) {
        this.start_day = start_day;
    }

    public String getEnd_day() {
        return end_day;
    }

    public void setEnd_day(String end_day) {
        this.end_day = end_day;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public enum BusiTpEnum {
        PWPC("转账"),
        PW13("预授权"),
        PWCF("预授权撤销"),
        PW03("划拨"),
        PW14("转账冻结"),
        PW15("划拨冻结"),
        PWDJ("冻结"),
        PWJD("解冻"),
        PW19("冻结付款到冻结"),
        ;

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
                ", mchnt_cd='" + mchnt_cd + '\'' +
                ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\'' +
                ", busi_tp='" + busi_tp + '\'' +
                ", start_day='" + start_day + '\'' +
                ", end_day='" + end_day + '\'' +
                ", txn_ssn='" + txn_ssn + '\'' +
                ", cust_no='" + cust_no + '\'' +
                ", txn_st='" + txn_st + '\'' +
                ", remark='" + remark + '\'' +
                ", page_no=" + page_no +
                ", page_size=" + page_size +
                "} " + super.toString();
    }
}
