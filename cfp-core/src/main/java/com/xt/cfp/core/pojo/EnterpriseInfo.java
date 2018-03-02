package com.xt.cfp.core.pojo;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业信息
 */
public class EnterpriseInfo {
    private Long enterpriseId;//企业ID
    private String enterpriseName;//企业名称
    private String enterpriseType;//企业类型
    private String legalPersonName;//法人姓名
    private String legalPersonCode;//法人身份证号
    private String accountNumber;//开户许可证号
    private String organizationCode;//组织机构代码
    private String taxRegistrationCode;//税务登记证代码
    private String businessRegistrationNumber;//营业执照注册号
    private Integer operatingPeriod;//经营年限
    private Long registeredCapital;//注册资金
    private String operatingRange;//经营范围
    private String information;//企业信息
    private BigDecimal mainRevenue;//主营收入
    private BigDecimal grossProfit;//毛利润
    private BigDecimal netProfit;//净利润
    private BigDecimal totalAssets;//总资产
    private BigDecimal netAssets;//净资产
    private Date contractBegin;//合同开始期限
    private Date contractEnd;//合同结束期限
    private BigDecimal singleMaximumAmount;//单笔最大额度
    private BigDecimal annualMaximumLimit;//年度最大限额
    private BigDecimal annualResidualAmount;//年度剩余额度
    private String state;//状态
    private Date createTime;//创建时间
    private Date lastUpdateTime;//最后修改时间
    private Long userId;//用户ID
    private String otype;
    
    
    protected String agreementStatus ;//合同状态
    private Long platformId;//关联平台账户ID

    public Long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    public String getOtype() {
		return otype;
	}

	public void setOtype(String otype) {
		this.otype = otype;
	}

	public String getAgreementStatus() {
	return agreementStatus;
}

public void setAgreementStatus(String agreementStatus) {
	this.agreementStatus = agreementStatus;
}
	/**
     * 状态（0.可用；1.不可用）
     */
    public static final String STATE_ENABLE = "0";
    public static final String STATE_DISABLE = "1";

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName == null ? null : enterpriseName.trim();
    }

    public String getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(String enterpriseType) {
        this.enterpriseType = enterpriseType == null ? null : enterpriseType.trim();
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName == null ? null : legalPersonName.trim();
    }

    public String getLegalPersonCode() {
        return legalPersonCode;
    }

    public void setLegalPersonCode(String legalPersonCode) {
        this.legalPersonCode = legalPersonCode == null ? null : legalPersonCode.trim();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber == null ? null : accountNumber.trim();
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode == null ? null : organizationCode.trim();
    }

    public String getTaxRegistrationCode() {
        return taxRegistrationCode;
    }

    public void setTaxRegistrationCode(String taxRegistrationCode) {
        this.taxRegistrationCode = taxRegistrationCode == null ? null : taxRegistrationCode.trim();
    }

    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }

    public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
        this.businessRegistrationNumber = businessRegistrationNumber == null ? null : businessRegistrationNumber.trim();
    }

    public Integer getOperatingPeriod() {
        return operatingPeriod;
    }

    public void setOperatingPeriod(Integer operatingPeriod) {
        this.operatingPeriod = operatingPeriod;
    }

    public Long getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(Long registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getOperatingRange() {
        return operatingRange;
    }

    public void setOperatingRange(String operatingRange) {
        this.operatingRange = operatingRange == null ? null : operatingRange.trim();
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information == null ? null : information.trim();
    }

    public BigDecimal getMainRevenue() {
        return mainRevenue;
    }

    public void setMainRevenue(BigDecimal mainRevenue) {
        this.mainRevenue = mainRevenue;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getNetAssets() {
        return netAssets;
    }

    public void setNetAssets(BigDecimal netAssets) {
        this.netAssets = netAssets;
    }

    public Date getContractBegin() {
        return contractBegin;
    }

    public void setContractBegin(Date contractBegin) {
        this.contractBegin = contractBegin;
    }

    public Date getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(Date contractEnd) {
        this.contractEnd = contractEnd;
    }

    public BigDecimal getSingleMaximumAmount() {
        return singleMaximumAmount;
    }

    public void setSingleMaximumAmount(BigDecimal singleMaximumAmount) {
        this.singleMaximumAmount = singleMaximumAmount;
    }

    public BigDecimal getAnnualMaximumLimit() {
        return annualMaximumLimit;
    }

    public void setAnnualMaximumLimit(BigDecimal annualMaximumLimit) {
        this.annualMaximumLimit = annualMaximumLimit;
    }

    public BigDecimal getAnnualResidualAmount() {
        return annualResidualAmount;
    }

    public void setAnnualResidualAmount(BigDecimal annualResidualAmount) {
        this.annualResidualAmount = annualResidualAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
    
    //加密企业名称
    public String getJmEnterpriseName(){
        if (StringUtils.isEmpty(this.getEnterpriseName()))
            return null;
        return com.xt.cfp.core.util.StringUtils.getJmName(this.getEnterpriseName());
    }
    //加密企业编码
    public String getJmOrganizationCode(){
        if (StringUtils.isEmpty(this.getOrganizationCode()))
            return null;
        return com.xt.cfp.core.util.StringUtils.getJmName(this.getOrganizationCode());
    }
}