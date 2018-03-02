package com.external.deposites.model.response;

import com.external.deposites.api.ApiParameter;

public class legalPersonOpenAccountResponse extends AbstractResponse{
  @ApiParameter
  private String mobile_no;
  @ApiParameter
  private String cust_nm;
  @ApiParameter
  private String artif_nm;
  @ApiParameter
  private String certif_id;
  @ApiParameter
  private String city_id;
  @ApiParameter
  private String parent_bank_id;
  @ApiParameter
  private String capAcntNo;
  @ApiParameter
  private String email;
  @ApiParameter
  private String bank_nm;
  @ApiParameter
  private String user_id_from;
  @ApiParameter
  private String mchnt_cd;
  @ApiParameter
  private String mchnt_txn_ssn;
  
  public String getMobile_no()
  {
    return this.mobile_no;
  }
  
  public void setMobile_no(String mobile_no)
  {
    this.mobile_no = mobile_no;
  }
  
  public String getCust_nm()
  {
    return this.cust_nm;
  }
  
  public void setCust_nm(String cust_nm)
  {
    this.cust_nm = cust_nm;
  }
  
  public String getCertif_id()
  {
    return this.certif_id;
  }
  
  public void setCertif_id(String certif_id)
  {
    this.certif_id = certif_id;
  }
  
  public String getCity_id()
  {
    return this.city_id;
  }
  
  public void setCity_id(String city_id)
  {
    this.city_id = city_id;
  }
  
  public String getParent_bank_id()
  {
    return this.parent_bank_id;
  }
  
  public void setParent_bank_id(String parent_bank_id)
  {
    this.parent_bank_id = parent_bank_id;
  }
  
  public String getCapAcntNo()
  {
    return this.capAcntNo;
  }
  
  public void setCapAcntNo(String capAcntNo)
  {
    this.capAcntNo = capAcntNo;
  }
  
  public String getEmail()
  {
    return this.email;
  }
  
  public void setEmail(String email)
  {
    this.email = email;
  }
  
  public String getBank_nm()
  {
    return this.bank_nm;
  }
  
  public void setBank_nm(String bank_nm)
  {
    this.bank_nm = bank_nm;
  }
  
  public String getUser_id_from()
  {
    return this.user_id_from;
  }
  
  public void setUser_id_from(String user_id_from)
  {
    this.user_id_from = user_id_from;
  }
  
  public String toString()
  {
    return "OpenAccount4PersonalBySelfResponse{mobile_no='" + this.mobile_no + '\'' + ", cust_nm='" + this.cust_nm + '\'' + ", certif_id='" + this.certif_id + '\'' + ", city_id=" + this.city_id + ", parent_bank_id=" + this.parent_bank_id + ", capAcntNo='" + this.capAcntNo + '\'' + ", email='" + this.email + '\'' + ", bank_nm='" + this.bank_nm + '\'' + ", user_id_from='" + this.user_id_from + '\'' + "} " + super.toString();
  }
  
  public String getMchnt_cd()
  {
    return this.mchnt_cd;
  }
  
  public void setMchnt_cd(String mchnt_cd)
  {
    this.mchnt_cd = mchnt_cd;
  }
  
  public String getMchnt_txn_ssn()
  {
    return this.mchnt_txn_ssn;
  }
  
  public void setMchnt_txn_ssn(String mchnt_txn_ssn)
  {
    this.mchnt_txn_ssn = mchnt_txn_ssn;
  }

 public String getArtif_nm() {
	return artif_nm;
 }

 public void setArtif_nm(String artif_nm) {
	this.artif_nm = artif_nm;
 }
  
}
