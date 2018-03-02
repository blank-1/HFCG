package com.external.deposites.model.fyResponse;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.model.response.AbstractResponse;

/**
 * 用户查询响应明细
 *
 * @author zuowansheng
 */
public class FyUserInfoResponseItem extends AbstractResponse {
	@ApiParameter
	private String mchnt_nm;
   @ApiParameter
   private String mobile_no;
   @ApiParameter
   private String login_id;
   @ApiParameter
   private String cust_nm;
   @ApiParameter
   private String certif_id;
   @ApiParameter
   private String email;
   @ApiParameter
   private String city_id;
   @ApiParameter
   private String parent_bank_id;
   @ApiParameter
   private String bank_nm;
   @ApiParameter
   private String capAcntNo;
   @ApiParameter
   private String card_pwd_verify_st;
   @ApiParameter
   private String id_nm_verify_st;
   @ApiParameter
   private String contract_st;
   @ApiParameter
   private String user_st;
   @ApiParameter
   private String User_tp;
   private String signature;
	public String getMobile_no() {
		return mobile_no;
	}
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	public String getLogin_id() {
		return login_id;
	}
	public void setLogin_id(String login_id) {
		this.login_id = login_id;
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
	public String getBank_nm() {
		return bank_nm;
	}
	public void setBank_nm(String bank_nm) {
		this.bank_nm = bank_nm;
	}
	public String getCapAcntNo() {
		return capAcntNo;
	}
	public void setCapAcntNo(String capAcntNo) {
		this.capAcntNo = capAcntNo;
	}
	public String getCard_pwd_verify_st() {
		return card_pwd_verify_st;
	}
	public void setCard_pwd_verify_st(String card_pwd_verify_st) {
		this.card_pwd_verify_st = card_pwd_verify_st;
	}
	public String getId_nm_verify_st() {
		return id_nm_verify_st;
	}
	public void setId_nm_verify_st(String id_nm_verify_st) {
		this.id_nm_verify_st = id_nm_verify_st;
	}
	public String getContract_st() {
		return contract_st;
	}
	public void setContract_st(String contract_st) {
		this.contract_st = contract_st;
	}
	public String getUser_st() {
		return user_st;
	}
	public void setUser_st(String user_st) {
		this.user_st = user_st;
	}
	public String getUser_tp() {
		return User_tp;
	}
	public void setUser_tp(String user_tp) {
		User_tp = user_tp;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getMchnt_nm() {
		return mchnt_nm;
	}
	public void setMchnt_nm(String mchnt_nm) {
		this.mchnt_nm = mchnt_nm;
	}

}
