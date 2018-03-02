package com.xt.cfp.core.pojo;

import java.util.Date;

public class UserInfoExt {
    private Long userId;//用户ID
    private Long residentAddress;//常住地
    private Long avatar;//头像
    private String sex;//性别
    private String realName;//真实姓名
    private String idCard;//身份证号
    private String email;//邮箱
    private Date birthday;//出生日期
    private String educationLevel;//学历
    private Long recUserId;//推荐好友ID
    private String isVerified;//是否身份认证
    private String mobileNo;//手机号
    
    public String getSexStr(){
    	if("1".equals(sex)){
    		return "男";
    	}else if ("0".equals(sex)) {
			return "女";
		}else {
			return "";
		}
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getResidentAddress() {
        return residentAddress;
    }

    public void setResidentAddress(Long residentAddress) {
        this.residentAddress = residentAddress;
    }

    public Long getAvatar() {
        return avatar;
    }

    public void setAvatar(Long avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel == null ? null : educationLevel.trim();
    }

    public Long getRecUserId() {
        return recUserId;
    }

    public void setRecUserId(Long recUserId) {
        this.recUserId = recUserId;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified == null ? null : isVerified.trim();
    }

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getJMRealName() {
		return realName.substring(0, 1) + "**";
	}
}