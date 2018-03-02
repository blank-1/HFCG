package com.external.deposites.model.datasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;

/**
 * <pre>
 * 预授权
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public class PreAuthorizationDataSource extends AbstractDataSource {
    @ApiParameter
    @Validated(type = HfValidations.BasicValidation.NotNull)
    private String ver;
    @ApiParameter
    @Validated(type = HfValidations.BasicValidation.NotNull)
    private String mchnt_cd;
    @ApiParameter
    @Validated(type = HfValidations.BasicValidation.NotNull)
    private String mchnt_txn_ssn;
    @ApiParameter
    @Validated(type = HfValidations.BasicValidation.NotNull)
    private String out_cust_no; // 60 出账账户
    @ApiParameter
    @Validated(type = HfValidations.BasicValidation.NotNull)
    private String in_cust_no; // 60 入账账户
    @ApiParameter
    @Validated(type = HfValidations.BasicValidation.NotNull)
    private Long amt; // 12 预授权金额
    @ApiParameter
    private String rem; // 60 备注

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

    public String getOut_cust_no() {
        return out_cust_no;
    }

    public void setOut_cust_no(String out_cust_no) {
        this.out_cust_no = out_cust_no;
    }

    public String getIn_cust_no() {
        return in_cust_no;
    }

    public void setIn_cust_no(String in_cust_no) {
        this.in_cust_no = in_cust_no;
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
        return "PreAuthorizationDataSource{" +
                "ver='" + ver + '\'' +
                ", mchnt_cd='" + mchnt_cd + '\'' +
                ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\'' +
                ", out_cust_no='" + out_cust_no + '\'' +
                ", in_cust_no='" + in_cust_no + '\'' +
                ", amt='" + amt + '\'' +
                ", rem='" + rem + '\'' +
                "} " + super.toString();
    }
}
