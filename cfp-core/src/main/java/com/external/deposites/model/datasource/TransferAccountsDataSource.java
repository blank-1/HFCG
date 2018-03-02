package com.external.deposites.model.datasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;

/**
 * <pre>
 * 转账接口
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/28
 */
public class TransferAccountsDataSource extends AbstractDataSource {
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
    private String out_cust_no; // 60 付款登录账户
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String in_cust_no; // 60 收款登录账户
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private Long amt; // 12 转账金额 以分为单位 (无小数位)
    /**
     * <pre>
     *  -- 预授权请求交易成功时账户系 统返回的合同号; 是否需要带合同号，就看具体业务 只有当付款用户为个人用户时该字段有效
     * 1/如果带合同号，则是从对应预 授权的冻结余额转出;
     * 2/如果没带合同号，则是直接从 出账用户的可用余额转出;
     * </pre>
     */
    @ApiParameter
    private String contract_no; // 30 预授权合同号
    @ApiParameter
    private String signature;
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

    public String getContract_no() {
        return contract_no;
    }

    public void setContract_no(String contract_no) {
        this.contract_no = contract_no;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

    @Override
    public String toString() {
        return "TransferAccountsDataSource{" +
                "ver='" + ver + '\'' +
                ", mchnt_cd='" + mchnt_cd + '\'' +
                ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\'' +
                ", out_cust_no='" + out_cust_no + '\'' +
                ", in_cust_no='" + in_cust_no + '\'' +
                ", amt=" + amt +
                ", contract_no='" + contract_no + '\'' +
                ", signature='" + signature + '\'' +
                ", rem='" + rem + '\'' +
                "} " + super.toString();
    }
}
