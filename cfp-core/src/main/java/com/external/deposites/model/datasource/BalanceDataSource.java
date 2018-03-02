package com.external.deposites.model.datasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;

/**
 * <pre>
 * 查询余额
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/24
 */
public class BalanceDataSource extends AbstractDataSource {
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_cd; // 15 商户代码
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_txn_ssn; // 30 流水号
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_txn_dt; // 8 商户查询当前日期
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String cust_no; // 120 查询企业注册的手机号或者用户 名，多个帐号请以”|”分隔 (最 多只能同时查 10 个用户)
    @ApiParameter
    private String signature; // 签名信息 按参数名字母排序后的值 用“|”线连接起来的明文， 然后用 rsa 签名的值

    private String actionUrl;

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
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

    public String getMchnt_txn_dt() {
        return mchnt_txn_dt;
    }

    public void setMchnt_txn_dt(String mchnt_txn_dt) {
        this.mchnt_txn_dt = mchnt_txn_dt;
    }

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "BalanceDataSource{" +
                "mchnt_cd='" + mchnt_cd + '\'' +
                ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\'' +
                ", mchnt_txn_dt='" + mchnt_txn_dt + '\'' +
                ", cust_no='" + cust_no + '\'' +
                ", signature='" + signature + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                "} " + super.toString();
    }
}
