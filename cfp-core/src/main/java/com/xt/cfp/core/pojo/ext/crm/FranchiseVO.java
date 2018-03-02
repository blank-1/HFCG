package com.xt.cfp.core.pojo.ext.crm;

public class FranchiseVO {
	
	private Long franchiseId;
	private String franchiseName;
	private String concatPerson;
	private String concatInformation;
	private String province;
	private String city;
	private String area;
	private String address;
	private Long mOid;//组织机构加盟商映射表主键
	
	public Long getFranchiseId() {
		return franchiseId;
	}
	public void setFranchiseId(Long franchiseId) {
		this.franchiseId = franchiseId;
	}
	public String getFranchiseName() {
		return franchiseName;
	}
	public void setFranchiseName(String franchiseName) {
		this.franchiseName = franchiseName;
	}
	public String getConcatPerson() {
		return concatPerson;
	}
	public void setConcatPerson(String concatPerson) {
		this.concatPerson = concatPerson;
	}
	public String getConcatInformation() {
		return concatInformation;
	}
	public void setConcatInformation(String concatInformation) {
		this.concatInformation = concatInformation;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getmOid() {
		return mOid;
	}
	public void setmOid(Long mOid) {
		this.mOid = mOid;
	}
}
