package com.xt.cfp.core.pojo;

import java.util.Date;
/**
 * 企业标（质押标）
 */
public class EnterprisePledgeSnapshot{
    private Long enterprisePledgeId;//企业标ID
    private Long loanApplicationId;//借款申请ID
    private Long province;//省份
    private Long city;//城市
    private String useDescription;//用途描述
    private String projectDescription;//项目描述
    private String internalRating;//内部评级
    private String riskControlInformation;//风险控制信息
    private Date createTime;//创建时间
    private Date lastUpdateTime;//最后更改时间
    private Long mainLoanApplicationId;//借款申请主表ID
    
 // 辅助字段
    private String provinceName;
    private String cityName;


    public Long getEnterprisePledgeId() {
		return enterprisePledgeId;
	}

	public void setEnterprisePledgeId(Long enterprisePledgeId) {
		this.enterprisePledgeId = enterprisePledgeId;
	}

	public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
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

    public String getUseDescription() {
        return useDescription;
    }

    public void setUseDescription(String useDescription) {
        this.useDescription = useDescription == null ? null : useDescription.trim();
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription == null ? null : projectDescription.trim();
    }

    public String getInternalRating() {
        return internalRating;
    }

    public void setInternalRating(String internalRating) {
        this.internalRating = internalRating == null ? null : internalRating.trim();
    }

    public String getRiskControlInformation() {
        return riskControlInformation;
    }

    public void setRiskControlInformation(String riskControlInformation) {
        this.riskControlInformation = riskControlInformation == null ? null : riskControlInformation.trim();
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

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
    
    public Long getMainLoanApplicationId() {
		return mainLoanApplicationId;
	}

	public void setMainLoanApplicationId(Long mainLoanApplicationId) {
		this.mainLoanApplicationId = mainLoanApplicationId;
	}

}