package com.external.deposites.model.datasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;

/**
 * <pre>
 * 冻结
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/30
 */
public class FreezeDataSource extends AbstractDataSource {
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String ver; //0.44
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_cd; // 15 商户代码
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_txn_ssn; // 30 流水号
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String cust_no; // 60 冻结目标登录 账户
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private Long amt; // 12 转账金额 以分为单位 (无小数位)
    @ApiParameter
    private String rem; // 60 备注
    @ApiParameter
    private String signature;


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

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "FreezeDataSource{" +
                "ver='" + ver + '\'' +
                ", mchnt_cd='" + mchnt_cd + '\'' +
                ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\'' +
                ", cust_no='" + cust_no + '\'' +
                ", amt=" + amt +
                ", rem='" + rem + '\'' +
                ", signature='" + signature + '\'' +
                "} " + super.toString();
    }
}
