package com.xt.cfp.core.pojo;

/**
 * 企业借款申请关联
 */
public class EnterpriseLoanApplication {
    private Long enterpriseLoanApplicationId;//关联ID
    private Long loanApplicationId;//借款申请ID
    private Long enterpriseId;//企业ID
    private Long mainLoanApplicationId;//借款申请主表ID
    
	public Long getEnterpriseLoanApplicationId() {
        return enterpriseLoanApplicationId;
    }

    public void setEnterpriseLoanApplicationId(Long enterpriseLoanApplicationId) {
        this.enterpriseLoanApplicationId = enterpriseLoanApplicationId;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
    
    public Long getMainLoanApplicationId() {
		return mainLoanApplicationId;
	}

	public void setMainLoanApplicationId(Long mainLoanApplicationId) {
		this.mainLoanApplicationId = mainLoanApplicationId;
	}
}