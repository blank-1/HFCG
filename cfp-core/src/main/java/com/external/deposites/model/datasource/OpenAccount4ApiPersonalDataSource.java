package com.external.deposites.model.datasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;

/**
 * <pre>
 * 开户 -- 必要参数
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/15
 */
public class OpenAccount4ApiPersonalDataSource extends AbstractOpenAccount4ApiDataSource {
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "var参数不能为空")
    private String ver;
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "商户代码不能为空")
    private String mchnt_cd; // 15 商户代码
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "流水号不能为空")
    private String mchnt_txn_ssn; // 30 流水号
    @ApiParameter
    private String signature; // 签名信息 按参数名字母排序后的值 用“|”线连接起来的明文， 然后用 rsa 签名的值
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "客户姓名不能为空")
    private String cust_nm;     // 30 客户姓名
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "身份证号码/证件不能为空")
    private String certif_id; // 身份证号码/证件
    @Validated(type = HfValidations.BasicValidation.Phone)
    @ApiParameter
    private String mobile_no; // 手机号码
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "开户行地区代码不能为空")
    private String city_id;// 4 开户行地区代码 代码列表见附件
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "开户行行别不能为空")
    private String parent_bank_id;// 4 开户行行别
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull}, message = "银行卡号不能为空")
    private String capAcntNo;   //Max(10,30) 帐号

    @ApiParameter
    private Integer certif_tp; // 1 0:身份证 7:其他证件
    @ApiParameter
    private String email;
    @ApiParameter
    private String bank_nm;
    @ApiParameter
    private String capAcntNm;
    @ApiParameter
    private String password;
    @ApiParameter
    private String lpassword;
    @ApiParameter
    private String rem;


    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getCust_nm() {
        return cust_nm;
    }

    public void setCust_nm(String cust_nm) {
        this.cust_nm = cust_nm;
    }

    public Integer getCertif_tp() {
        return certif_tp;
    }

    public void setCertif_tp(Integer certif_tp) {
        this.certif_tp = certif_tp;
    }

    public String getCertif_id() {
        return certif_id;
    }

    public void setCertif_id(String certif_id) {
        this.certif_id = certif_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getParent_bank_id() {
        return parent_bank_id;
    }

    public void setParent_bank_id(String parent_bank_id) {
        this.parent_bank_id = parent_bank_id;
    }

    public String getCapAcntNo() {
        return capAcntNo;
    }

    public void setCapAcntNo(String capAcntNo) {
        this.capAcntNo = capAcntNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBank_nm() {
        return bank_nm;
    }

    public void setBank_nm(String bank_nm) {
        this.bank_nm = bank_nm;
    }

    public String getCapAcntNm() {
        return capAcntNm;
    }

    public void setCapAcntNm(String capAcntNm) {
        this.capAcntNm = capAcntNm;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLpassword() {
        return lpassword;
    }

    public void setLpassword(String lpassword) {
        this.lpassword = lpassword;
    }

    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "OpenAccount4ApiPersonalDataSource{" +
                "ver='" + ver + '\'' +
                ", mchnt_cd='" + mchnt_cd + '\'' +
                ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\'' +
                ", signature='" + signature + '\'' +
                ", cust_nm='" + cust_nm + '\'' +
                ", certif_id='" + certif_id + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", city_id='" + city_id + '\'' +
                ", parent_bank_id='" + parent_bank_id + '\'' +
                ", capAcntNo='" + capAcntNo + '\'' +
                ", certif_tp=" + certif_tp +
                ", email='" + email + '\'' +
                ", bank_nm='" + bank_nm + '\'' +
                ", capAcntNm='" + capAcntNm + '\'' +
                ", password='" + password + '\'' +
                ", lpassword='" + lpassword + '\'' +
                ", rem='" + rem + '\'' +
                "} " + super.toString();
    }
}