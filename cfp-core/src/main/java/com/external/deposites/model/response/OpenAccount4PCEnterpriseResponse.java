package com.external.deposites.model.response;

import com.external.deposites.api.ApiParameter;

/**
 * <pre>
 * 企业 自助开户响应体
 * resp_desc 不参与签名
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/30
 */
public class OpenAccount4PCEnterpriseResponse extends AbstractResponse {
    @ApiParameter
    private String cust_nm;     // 企业名称
    @ApiParameter
    private String artif_nm;    // 法人姓名
    @ApiParameter
    private String mobile_no;
    @ApiParameter
    private String certif_id;       //身份证号码
    @ApiParameter
    private String email; // o
    @ApiParameter
    private String city_id;        // 开户行地区代码
    @ApiParameter
    private String parent_bank_id; // 现账户开户行
    @ApiParameter
    private String bank_nm; // o  现账户支行名称
    @ApiParameter
    private String user_id_from;   // o 用户在商户系统的 标志
    @ApiParameter
    private String capAcntNo;   // 帐号


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

    public String getCertif_id() {
        return certif_id;
    }

    public void setCertif_id(String certif_id) {
        this.certif_id = certif_id;
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

    public String getUser_id_from() {
        return user_id_from;
    }

    public void setUser_id_from(String user_id_from) {
        this.user_id_from = user_id_from;
    }

    public String getArtif_nm() {
        return artif_nm;
    }

    public void setArtif_nm(String artif_nm) {
        this.artif_nm = artif_nm;
    }

    @Override
    public String toString() {
        return "OpenAccount4PCEnterpriseResponse{" +
                "cust_nm='" + cust_nm + '\'' +
                ", artif_nm='" + artif_nm + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", certif_id='" + certif_id + '\'' +
                ", email='" + email + '\'' +
                ", city_id='" + city_id + '\'' +
                ", parent_bank_id='" + parent_bank_id + '\'' +
                ", bank_nm='" + bank_nm + '\'' +
                ", user_id_from='" + user_id_from + '\'' +
                ", capAcntNo='" + capAcntNo + '\'' +
                "} " + super.toString();
    }
}
