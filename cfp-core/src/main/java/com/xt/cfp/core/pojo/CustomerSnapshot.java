package com.xt.cfp.core.pojo;

import java.util.Date;

/**
 * Created by Renyulin on 14-3-26 下午5:05.
 */
public class CustomerSnapshot  {
    private long customerSnapshotId;
    private long customerId;
    private String trueName;
    private long countryId;
    private long provinceId;
    private long pCityId;
    private long cityId;
    private String address;
    private long hourseCountryId;
    private long hourseProvinceId;
    private long hoursePCityId;
    private long hourseCityId;
    private String hourseAddress;
    private Date birthday = new Date();
    private char certificatesType;
    private String certificatesCode;
    private String mobilePhone;
    private String telphone;
    private String zipcode;
    private String email;
    private String companyName;
    private String post;
    private char  isMarried;
    private long childNum;
    private String occupation;
    private String monthlyIncome;
    private String education;
    private String educationCode;
    private Date createTime = new Date();

    public static final char PLAY_MONEY_CARD_TYPE = '1';//打款卡类型
    public static final char SIMPLY_DEDUCT_CARD_TYPE = '2';//划扣卡类型

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getCustomerSnapshotId() {
        return customerSnapshotId;
    }

    public void setCustomerSnapshotId(long customerSnapshotId) {
        this.customerSnapshotId = customerSnapshotId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHourseAddress() {
        return hourseAddress;
    }

    public void setHourseAddress(String hourseAddress) {
        this.hourseAddress = hourseAddress;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }

    public long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(long provinceId) {
        this.provinceId = provinceId;
    }

    public long getpCityId() {
        return pCityId;
    }

    public void setpCityId(long pCityId) {
        this.pCityId = pCityId;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public long getHourseCountryId() {
        return hourseCountryId;
    }

    public void setHourseCountryId(long hourseCountryId) {
        this.hourseCountryId = hourseCountryId;
    }

    public long getHourseProvinceId() {
        return hourseProvinceId;
    }

    public void setHourseProvinceId(long hourseProvinceId) {
        this.hourseProvinceId = hourseProvinceId;
    }

    public long getHoursePCityId() {
        return hoursePCityId;
    }

    public void setHoursePCityId(long hoursePCityId) {
        this.hoursePCityId = hoursePCityId;
    }

    public long getHourseCityId() {
        return hourseCityId;
    }

    public void setHourseCityId(long hourseCityId) {
        this.hourseCityId = hourseCityId;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public char getCertificatesType() {
        return certificatesType;
    }

    public void setCertificatesType(char certificatesType) {
        this.certificatesType = certificatesType;
    }

    public String getCertificatesCode() {
        return certificatesCode;
    }

    public void setCertificatesCode(String certificatesCode) {
        this.certificatesCode = certificatesCode;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public char getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(char isMarried) {
        this.isMarried = isMarried;
    }

    public long getChildNum() {
        return childNum;
    }

    public void setChildNum(long childNum) {
        this.childNum = childNum;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducationCode() {
        return educationCode;
    }

    public void setEducationCode(String educationCode) {
        this.educationCode = educationCode;
    }

}
