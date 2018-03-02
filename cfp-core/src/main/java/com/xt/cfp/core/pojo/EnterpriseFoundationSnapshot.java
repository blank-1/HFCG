package com.xt.cfp.core.pojo;

import java.util.Date;

public class EnterpriseFoundationSnapshot {
    private Long enterpriseFoundationId;

    private Long attachId;

    private Long tradeBookId;

    private Long riskTipId;

    private Date createTime;

    private Date lastUpdateTime;

    private String projectDescription;

    private String investmentType;

    private Long loanApplicationId;

    private Long coId;
    
    private Long mainLoanApplicationId;//借款申请主表ID

    public Long getEnterpriseFoundationId() {
        return enterpriseFoundationId;
    }

    public void setEnterpriseFoundationId(Long enterpriseFoundationId) {
        this.enterpriseFoundationId = enterpriseFoundationId;
    }

    public Long getRiskTipId() {
        return riskTipId;
    }

    public void setRiskTipId(Long riskTipId) {
        this.riskTipId = riskTipId;
    }

    public Long getTradeBookId() {
        return tradeBookId;
    }

    public void setTradeBookId(Long tradeBookId) {
        this.tradeBookId = tradeBookId;
    }

    public Long getAttachId() {
        return attachId;
    }

    public Long getCoId() {
        return coId;
    }

    public void setCoId(Long coId) {
        this.coId = coId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
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

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription == null ? null : projectDescription.trim();
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType == null ? null : investmentType.trim();
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }
    
    public Long getMainLoanApplicationId() {
		return mainLoanApplicationId;
	}

	public void setMainLoanApplicationId(Long mainLoanApplicationId) {
		this.mainLoanApplicationId = mainLoanApplicationId;
	}
}