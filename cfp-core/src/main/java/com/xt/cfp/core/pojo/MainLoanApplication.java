package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class MainLoanApplication {
    private Long mainLoanApplicationId;//借款申请主ID
    private String mainCode;//主编号
    private BigDecimal mainLoanBalance;//主借款总金额
    private String mainState;//主状态，(0.未发标；1.发标中；2.发标完成)
    private BigDecimal mainPublishBalance;//主已发标金额
    private Date mainCreateTime;//主创建时间
    private Date mainUpdateTime;//主最后更改时间
    private Long mainAdminId;//主最后操作人
    private String mainAutoPublish;//自动发标（0关闭的；1开启的） 
    private Long customerAccountId;
    private Long repaymentAccountId;
    private Long inCardId;
    private Long outCardId;
    private Long userId;
    private Long loanProductId;
    private Long originLoanAppId;
    private String loanApplicationCode;
    private String loanApplicationName;
    private String offlineApplyCode;
    private String publishTarget;
    private String loanUseage;
    private String renewAgreementCode;
    private String subjectType;
    private String loanType;
    private BigDecimal loanBalance;
    private BigDecimal confirmBalance;
    private BigDecimal resultBalance;
    private BigDecimal maxBuyBalance;
    private BigDecimal annualRate;
    private String applicationDesc;
    private String lendState;
    private String verifyState;
    private String publishState;
    private BigDecimal interestBalance;
    private String applicationState;
    private Long recorderPersonnel;
    private String recorderName;
    private String channel;
    private Long channelId;
    private Long originalUserId;
    private Date preheatTime;
    private Date openTime;
    private Date fullTime;
    private Date paymentDate;
    private Date recordTime;
    private Date publishTime;
    private Date cancelTime;
    private Date createTime;
    private Date agreementStartDate;
    private Date firstRepaymentDate;
    private Date lastRepaymentDate;
    private String loanUseageDesc;
    private String riskControlInformation;//风险控制信息 
    
    public Long getMainLoanApplicationId() {
        return mainLoanApplicationId;
    }

    public void setMainLoanApplicationId(Long mainLoanApplicationId) {
        this.mainLoanApplicationId = mainLoanApplicationId;
    }

    public String getMainCode() {
        return mainCode;
    }

    public void setMainCode(String mainCode) {
        this.mainCode = mainCode == null ? null : mainCode.trim();
    }

    public BigDecimal getMainLoanBalance() {
        return mainLoanBalance;
    }

    public void setMainLoanBalance(BigDecimal mainLoanBalance) {
        this.mainLoanBalance = mainLoanBalance;
    }

    public String getMainState() {
        return mainState;
    }

    public void setMainState(String mainState) {
        this.mainState = mainState == null ? null : mainState.trim();
    }

    public BigDecimal getMainPublishBalance() {
        return mainPublishBalance;
    }

    public void setMainPublishBalance(BigDecimal mainPublishBalance) {
        this.mainPublishBalance = mainPublishBalance;
    }

    public Date getMainCreateTime() {
        return mainCreateTime;
    }

    public void setMainCreateTime(Date mainCreateTime) {
        this.mainCreateTime = mainCreateTime;
    }

    public Date getMainUpdateTime() {
        return mainUpdateTime;
    }

    public void setMainUpdateTime(Date mainUpdateTime) {
        this.mainUpdateTime = mainUpdateTime;
    }

    public Long getMainAdminId() {
        return mainAdminId;
    }

    public void setMainAdminId(Long mainAdminId) {
        this.mainAdminId = mainAdminId;
    }

    public Long getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(Long customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    public Long getRepaymentAccountId() {
        return repaymentAccountId;
    }

    public void setRepaymentAccountId(Long repaymentAccountId) {
        this.repaymentAccountId = repaymentAccountId;
    }

    public Long getInCardId() {
        return inCardId;
    }

    public void setInCardId(Long inCardId) {
        this.inCardId = inCardId;
    }

    public Long getOutCardId() {
        return outCardId;
    }

    public void setOutCardId(Long outCardId) {
        this.outCardId = outCardId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLoanProductId() {
        return loanProductId;
    }

    public void setLoanProductId(Long loanProductId) {
        this.loanProductId = loanProductId;
    }

    public Long getOriginLoanAppId() {
        return originLoanAppId;
    }

    public void setOriginLoanAppId(Long originLoanAppId) {
        this.originLoanAppId = originLoanAppId;
    }

    public String getLoanApplicationCode() {
        return loanApplicationCode;
    }

    public void setLoanApplicationCode(String loanApplicationCode) {
        this.loanApplicationCode = loanApplicationCode == null ? null : loanApplicationCode.trim();
    }

    public String getLoanApplicationName() {
        return loanApplicationName;
    }

    public void setLoanApplicationName(String loanApplicationName) {
        this.loanApplicationName = loanApplicationName == null ? null : loanApplicationName.trim();
    }

    public String getOfflineApplyCode() {
        return offlineApplyCode;
    }

    public void setOfflineApplyCode(String offlineApplyCode) {
        this.offlineApplyCode = offlineApplyCode == null ? null : offlineApplyCode.trim();
    }

    public String getPublishTarget() {
        return publishTarget;
    }

    public void setPublishTarget(String publishTarget) {
        this.publishTarget = publishTarget == null ? null : publishTarget.trim();
    }

    public String getLoanUseage() {
        return loanUseage;
    }

    public void setLoanUseage(String loanUseage) {
        this.loanUseage = loanUseage == null ? null : loanUseage.trim();
    }

    public String getRenewAgreementCode() {
        return renewAgreementCode;
    }

    public void setRenewAgreementCode(String renewAgreementCode) {
        this.renewAgreementCode = renewAgreementCode == null ? null : renewAgreementCode.trim();
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType == null ? null : subjectType.trim();
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType == null ? null : loanType.trim();
    }

    public BigDecimal getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(BigDecimal loanBalance) {
        this.loanBalance = loanBalance;
    }

    public BigDecimal getConfirmBalance() {
        return confirmBalance;
    }

    public void setConfirmBalance(BigDecimal confirmBalance) {
        this.confirmBalance = confirmBalance;
    }

    public BigDecimal getResultBalance() {
        return resultBalance;
    }

    public void setResultBalance(BigDecimal resultBalance) {
        this.resultBalance = resultBalance;
    }

    public BigDecimal getMaxBuyBalance() {
        return maxBuyBalance;
    }

    public void setMaxBuyBalance(BigDecimal maxBuyBalance) {
        this.maxBuyBalance = maxBuyBalance;
    }

    public BigDecimal getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(BigDecimal annualRate) {
        this.annualRate = annualRate;
    }

    public String getApplicationDesc() {
        return applicationDesc;
    }

    public void setApplicationDesc(String applicationDesc) {
        this.applicationDesc = applicationDesc == null ? null : applicationDesc.trim();
    }

    public String getLendState() {
        return lendState;
    }

    public void setLendState(String lendState) {
        this.lendState = lendState == null ? null : lendState.trim();
    }

    public String getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(String verifyState) {
        this.verifyState = verifyState == null ? null : verifyState.trim();
    }

    public String getPublishState() {
        return publishState;
    }

    public void setPublishState(String publishState) {
        this.publishState = publishState == null ? null : publishState.trim();
    }

    public BigDecimal getInterestBalance() {
        return interestBalance;
    }

    public void setInterestBalance(BigDecimal interestBalance) {
        this.interestBalance = interestBalance;
    }

    public String getApplicationState() {
        return applicationState;
    }

    public void setApplicationState(String applicationState) {
        this.applicationState = applicationState == null ? null : applicationState.trim();
    }

    public Long getRecorderPersonnel() {
        return recorderPersonnel;
    }

    public void setRecorderPersonnel(Long recorderPersonnel) {
        this.recorderPersonnel = recorderPersonnel;
    }

    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName == null ? null : recorderName.trim();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getOriginalUserId() {
        return originalUserId;
    }

    public void setOriginalUserId(Long originalUserId) {
        this.originalUserId = originalUserId;
    }

    public Date getPreheatTime() {
        return preheatTime;
    }

    public void setPreheatTime(Date preheatTime) {
        this.preheatTime = preheatTime;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getFullTime() {
        return fullTime;
    }

    public void setFullTime(Date fullTime) {
        this.fullTime = fullTime;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getAgreementStartDate() {
        return agreementStartDate;
    }

    public void setAgreementStartDate(Date agreementStartDate) {
        this.agreementStartDate = agreementStartDate;
    }

    public Date getFirstRepaymentDate() {
        return firstRepaymentDate;
    }

    public void setFirstRepaymentDate(Date firstRepaymentDate) {
        this.firstRepaymentDate = firstRepaymentDate;
    }

    public Date getLastRepaymentDate() {
        return lastRepaymentDate;
    }

    public void setLastRepaymentDate(Date lastRepaymentDate) {
        this.lastRepaymentDate = lastRepaymentDate;
    }

	public String getLoanUseageDesc() {
		return loanUseageDesc;
	}

	public void setLoanUseageDesc(String loanUseageDesc) {
		this.loanUseageDesc = loanUseageDesc;
	}

	public String getMainAutoPublish() {
		return mainAutoPublish;
	}

	public void setMainAutoPublish(String mainAutoPublish) {
		this.mainAutoPublish = mainAutoPublish;
	}

	public String getRiskControlInformation() {
		return riskControlInformation;
	}

	public void setRiskControlInformation(String riskControlInformation) {
		this.riskControlInformation = riskControlInformation;
	}
	
	
}