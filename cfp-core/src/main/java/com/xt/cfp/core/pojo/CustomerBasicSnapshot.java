package com.xt.cfp.core.pojo;

import java.util.Date;

/**
 * 客户基本信息快照
 */
public class CustomerBasicSnapshot {
    private Long snapshotId;//客户快照ID
    private Long residenceAddr;//居住地址
    private Long registAddr;//户口地址
    private Long bornAddr;//籍贯地址
    private Long loanApplicationId;//借款申请ID
    private String trueName;//姓名
    private String sex;//性别
    private Date birthday;//出生日期
    private String idCard;//身份证号码
    private String mobilePhone;//手机号码
    private String zipcode;//居住地邮政编码
    private String email;//电子邮箱
    private String isMarried;//婚姻状况
    private String childStatus;//子女状况
    private String monthlyIncome;//月收入
    private Long maxCreditValue;//信用卡最高额度
    private String assetsInfo;//资产情况
    private String education;//最高学历
    private String status;//状态
    private Long mainLoanApplicationId;//借款申请主表ID
    
    // 性别：1男；0女
    public static final String SEX_MALE = "1";
    public static final String SEX_FEMALE = "0";
    
    public String getSexStr(String sex){
    	if(this.SEX_MALE.equals(sex)){
    		return "男";
    	}else if (this.SEX_FEMALE.equals(sex)) {
			return "女";
		}else {
			return "";
		}
    }

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Long getResidenceAddr() {
        return residenceAddr;
    }

    public void setResidenceAddr(Long residenceAddr) {
        this.residenceAddr = residenceAddr;
    }

    public Long getRegistAddr() {
        return registAddr;
    }

    public void setRegistAddr(Long registAddr) {
        this.registAddr = registAddr;
    }

    public Long getBornAddr() {
        return bornAddr;
    }

    public void setBornAddr(Long bornAddr) {
        this.bornAddr = bornAddr;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName == null ? null : trueName.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode == null ? null : zipcode.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(String isMarried) {
        this.isMarried = isMarried == null ? null : isMarried.trim();
    }

    public String getChildStatus() {
        return childStatus;
    }

    public void setChildStatus(String childStatus) {
        this.childStatus = childStatus == null ? null : childStatus.trim();
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome == null ? null : monthlyIncome.trim();
    }

    public Long getMaxCreditValue() {
        return maxCreditValue;
    }

    public void setMaxCreditValue(Long maxCreditValue) {
        this.maxCreditValue = maxCreditValue;
    }

    public String getAssetsInfo() {
        return assetsInfo;
    }

    public void setAssetsInfo(String assetsInfo) {
        this.assetsInfo = assetsInfo == null ? null : assetsInfo.trim();
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
    
    public Long getMainLoanApplicationId() {
		return mainLoanApplicationId;
	}

	public void setMainLoanApplicationId(Long mainLoanApplicationId) {
		this.mainLoanApplicationId = mainLoanApplicationId;
	}
}