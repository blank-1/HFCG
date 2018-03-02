package com.external.deposites.model.datasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;
import com.external.deposites.utils.HfUtils;

/**
 * <pre>
 * 自助开发
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/30
 */
public class OpenAccount4PCPersonalDataSource extends AbstractOpenAccount4PCDataSource {
    @ApiParameter
    private String ver;
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_cd; // 15 商户代码
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_txn_ssn; // 30 流水号
    @ApiParameter
    private String user_id_from; // 用户在商户系 统的标志
    @ApiParameter
    private String mobile_no; // 11
    @ApiParameter
    private String cust_nm; // 客户姓名
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private Integer certif_tp; // 证件类型 0:身份证 7:其他证件
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String certif_id;   // 身份证号码/证件
    @ApiParameter
    private String email;
    @ApiParameter
    private String city_id;    // 开户行地区代 码
    @ApiParameter
    private String parent_bank_id; //  现账户开户行
    @ApiParameter
    private String bank_nm; //  现账户支行名称
    @ApiParameter
    private String capAcntNo;// 帐号
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String page_notify_url;
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String back_notify_url;

    @ApiParameter
    private String signature; // 签名信息 按参数名字母排序后的值 用“|”线连接起来的明文， 然后用 rsa 签名的值
    private String actionUrl;

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

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

    public String getUser_id_from() {
        return user_id_from;
    }

    public void setUser_id_from(String user_id_from) {
        this.user_id_from = user_id_from;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
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
        HfUtils.CertifTpEnum.typeOf(certif_tp);
        this.certif_tp = certif_tp;
    }

    public String getCertif_id() {
        return certif_id;
    }

    public void setCertif_id(String certif_id) {
        this.certif_id = certif_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getBank_nm() {
        return bank_nm;
    }

    public void setBank_nm(String bank_nm) {
        this.bank_nm = bank_nm;
    }

    public String getPage_notify_url() {
        return page_notify_url;
    }

    public void setPage_notify_url(String page_notify_url) {
        this.page_notify_url = page_notify_url;
    }

    public String getBack_notify_url() {
        return back_notify_url;
    }

    public void setBack_notify_url(String back_notify_url) {
        this.back_notify_url = back_notify_url;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "OpenAccount4PersonalBySelfDataSource{" +
                "ver='" + ver + '\'' +
                ", mchnt_cd='" + mchnt_cd + '\'' +
                ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\'' +
                ", user_id_from='" + user_id_from + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", cust_nm='" + cust_nm + '\'' +
                ", certif_tp=" + certif_tp +
                ", certif_id='" + certif_id + '\'' +
                ", email='" + email + '\'' +
                ", city_id='" + city_id + '\'' +
                ", parent_bank_id='" + parent_bank_id + '\'' +
                ", bank_nm='" + bank_nm + '\'' +
                ", capAcntNo='" + capAcntNo + '\'' +
                ", page_notify_url='" + page_notify_url + '\'' +
                ", back_notify_url='" + back_notify_url + '\'' +
                ", signature='" + signature + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                "} " + super.toString();
    }
}
