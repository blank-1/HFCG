package com.external.deposites.model.fyResponse;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.model.response.AbstractResponse;

/**
 * <pre>
 * 用户修改信息通知
 * </pre>
 *
 * @author zuowansheng
 */
public class FyModifyUserInoBackResponse extends AbstractResponse {
    
	@ApiParameter
    private String user_id_from;
    @ApiParameter
    private String mobile_no;
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
    


}
