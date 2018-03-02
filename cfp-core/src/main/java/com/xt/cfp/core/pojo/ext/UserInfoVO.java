package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.Date;

import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.util.StringUtils;

public class UserInfoVO extends UserInfo {
	private String realName;
	private String sex;
	private Date birthday;
	private String mobileNo;// userinfoext表的手机号
	private String educationLevel;
	private Long province;
	private Long city;
	private String detail;
	private String idCard;
    private String type;
	private String isVerified;//是否已做过身份认证
	private BigDecimal residentAddress;
	private BigDecimal value;// 用户总资产
	private BigDecimal availValue;// 用户可用资金
	private BigDecimal frozeValue;// 用户冻结资金
	private BigDecimal balance;// 用户当前负债
	private BigDecimal zichan;// 用户当前资产
	private Long recUserId;//邀请好友id
	private String email;
	
	private String  roleID;//判断是否客服角色

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	private Date verifyTime;//实名认证时间
	private Date firstLenderTime;//首次投标时间
	@SuppressWarnings("unused")
	private String encryptIdCardNo;//加密后的身份证号

	public String getEncryptIdCardNo() {
		if(this.getIdCard() == null || this.getIdCard() == ""){
			return  null;
		}
		
		return StringUtils.getEncryptIdCard(this.getIdCard());
	}

	public void setEncryptIdCardNo(String encryptIdCardNo) {
		this.encryptIdCardNo = encryptIdCardNo;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public Date getFirstLenderTime() {
		return firstLenderTime;
	}

	public void setFirstLenderTime(Date firstLenderTime) {
		this.firstLenderTime = firstLenderTime;
	}

	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}

	public Long getProvince() {
		return province;
	}

	public void setProvince(Long province) {
		this.province = province;
	}

	public Long getCity() {
		return city;
	}

	public void setCity(Long city) {
		this.city = city;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getAvailValue() {
		return availValue;
	}

	public void setAvailValue(BigDecimal availValue) {
		this.availValue = availValue;
	}

	public BigDecimal getFrozeValue() {
		return frozeValue;
	}

	public void setFrozeValue(BigDecimal frozeValue) {
		this.frozeValue = frozeValue;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(String isVerified) {
		this.isVerified = isVerified;
	}

	public Long getRecUserId() {
		return recUserId;
	}

	public void setRecUserId(Long recUserId) {
		this.recUserId = recUserId;
	}

	public BigDecimal getZichan() {
		return zichan;
	}

	public void setZichan(BigDecimal zichan) {
		this.zichan = zichan;
	}

	public BigDecimal getResidentAddress() {
		return residentAddress;
	}

	public void setResidentAddress(BigDecimal residentAddress) {
		this.residentAddress = residentAddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}