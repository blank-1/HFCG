package com.xt.cfp.core.pojo;

import com.xt.cfp.core.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业保理
 */
public class EnterpriseFactoringSnapshot {
    private Long enterpriseFactoringId;//保理ID
    private Long loanApplicationId;//借款申请ID
    private BigDecimal financingAmount;//融资金额
    private Long financingParty;//融资方
    private Long paymentParty;//付款方
    private String sourceOfRepayment;//还款来源
    private String accountReceivableDescription;//应收账款说明
    private String projectComprehensiveEvaluati;//项目综合评价 
    private String moneyRiskAssessment;//款项风险评价
    private String fieldAdjustmentMark;//360度实地尽调(标记)
    private String fieldAdjustmentValue;//360度实地尽调(值)
    private String repaymentGuaranteeMark;//还款保障金(标记)
    private String repaymentGuaranteeValue;//还款保障金(值)
    private String aidFundMark;//法律援助基金(标记)
    private String aidFundValue;//法律援助基金(值)
    private Date createTime;//创建时间
    private Date lastUpdateTime;//最后更改时间
    private Long mainLoanApplicationId;//借款申请主表ID

    public Long getEnterpriseFactoringId() {
        return enterpriseFactoringId;
    }

    public void setEnterpriseFactoringId(Long enterpriseFactoringId) {
        this.enterpriseFactoringId = enterpriseFactoringId;
    }

    public String getSourceOfRepaymentStr(){
        if (this.getSourceOfRepayment()!=null)
            return StringUtils.getJmName(this.getSourceOfRepayment());
        return null;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public BigDecimal getFinancingAmount() {
        return financingAmount;
    }

    public void setFinancingAmount(BigDecimal financingAmount) {
        this.financingAmount = financingAmount;
    }

    public Long getFinancingParty() {
        return financingParty;
    }

    public void setFinancingParty(Long financingParty) {
        this.financingParty = financingParty;
    }

    public Long getPaymentParty() {
        return paymentParty;
    }

    public void setPaymentParty(Long paymentParty) {
        this.paymentParty = paymentParty;
    }

    public String getSourceOfRepayment() {
        return sourceOfRepayment;
    }

    public void setSourceOfRepayment(String sourceOfRepayment) {
        this.sourceOfRepayment = sourceOfRepayment;
    }

    public String getAccountReceivableDescription() {
        return accountReceivableDescription;
    }

    public void setAccountReceivableDescription(String accountReceivableDescription) {
        this.accountReceivableDescription = accountReceivableDescription == null ? null : accountReceivableDescription.trim();
    }

    public String getProjectComprehensiveEvaluati() {
        return projectComprehensiveEvaluati;
    }

    public void setProjectComprehensiveEvaluati(String projectComprehensiveEvaluati) {
        this.projectComprehensiveEvaluati = projectComprehensiveEvaluati == null ? null : projectComprehensiveEvaluati.trim();
    }

    public String getMoneyRiskAssessment() {
        return moneyRiskAssessment;
    }

    public void setMoneyRiskAssessment(String moneyRiskAssessment) {
        this.moneyRiskAssessment = moneyRiskAssessment == null ? null : moneyRiskAssessment.trim();
    }

    public String getFieldAdjustmentMark() {
        return fieldAdjustmentMark;
    }

    public void setFieldAdjustmentMark(String fieldAdjustmentMark) {
        this.fieldAdjustmentMark = fieldAdjustmentMark == null ? null : fieldAdjustmentMark.trim();
    }

    public String getFieldAdjustmentValue() {
        return fieldAdjustmentValue;
    }

    public void setFieldAdjustmentValue(String fieldAdjustmentValue) {
        this.fieldAdjustmentValue = fieldAdjustmentValue == null ? null : fieldAdjustmentValue.trim();
    }

    public String getRepaymentGuaranteeMark() {
        return repaymentGuaranteeMark;
    }

    public void setRepaymentGuaranteeMark(String repaymentGuaranteeMark) {
        this.repaymentGuaranteeMark = repaymentGuaranteeMark == null ? null : repaymentGuaranteeMark.trim();
    }

    public String getRepaymentGuaranteeValue() {
        return repaymentGuaranteeValue;
    }

    public void setRepaymentGuaranteeValue(String repaymentGuaranteeValue) {
        this.repaymentGuaranteeValue = repaymentGuaranteeValue == null ? null : repaymentGuaranteeValue.trim();
    }

    public String getAidFundMark() {
        return aidFundMark;
    }

    public void setAidFundMark(String aidFundMark) {
        this.aidFundMark = aidFundMark == null ? null : aidFundMark.trim();
    }

    public String getAidFundValue() {
        return aidFundValue;
    }

    public void setAidFundValue(String aidFundValue) {
        this.aidFundValue = aidFundValue == null ? null : aidFundValue.trim();
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
    
    public Long getMainLoanApplicationId() {
		return mainLoanApplicationId;
	}

	public void setMainLoanApplicationId(Long mainLoanApplicationId) {
		this.mainLoanApplicationId = mainLoanApplicationId;
	}
}