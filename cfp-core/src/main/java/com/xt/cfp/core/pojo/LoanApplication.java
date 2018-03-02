package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 借款申请
 */
public class LoanApplication {
    private Long loanApplicationId;//借款申请id
    private Long customerAccountId;//放款账户id
    private Long repaymentAccountId;//还款账户id
    private Long inCardId;//放款卡id
    private Long outCardId;//还款划扣卡id
    private Long userId;//用户id
    private Long loanProductId;//借款产品id
    private String loanApplicationCode;//借款申请编号
    private String loanApplicationName;//借款合同名称
    private String offlineApplyCode;//线下编号
    private String publishTarget;//发布目标（前台/理财）
    private String loanUseage;//借款用途
    private String renewAgreementCode;//续借合同号
    private String subjectType;//标的类型
    private String loanType;//借款类型
    private BigDecimal loanBalance;//申请金额
    private BigDecimal confirmBalance;//批复金额
    private BigDecimal resultBalance;//放款金额
    private BigDecimal annualRate;//年利率
    private String applicationDesc;//借款申请描述
    private String publishState;//发标审核状态
    private String lendState;//放款审核状态
    private String verifyState;//审核状态
    private BigDecimal interestBalance;//利息总额
    private String applicationState;//借款申请状态
    private Long recorderPersonnel;//录入人员id
    private String recorderName;//录入人员姓名
    private String channel;//渠道来源类型
    private Long channelId;//来源id
    private Long originalUserId;//原始债权人
    private Date preheatTime;//预热时间
    private Date openTime;//开标时间
    private Date fullTime;//满标时间
    private Date paymentDate;//放款日期
    private Date recordTime;//录入时间
    private Date publishTime;//发标时间
    private Date cancelTime;//流标时间
    private Date createTime;//购买时间
    private Date agreementStartDate;//合同生效日期
    private Date firstRepaymentDate;//首期还款日期
    private Date lastRepaymentDate;//还款截止日
    private BigDecimal maxBuyBalance;//单人最大可投金额
    private String loanUseageDesc;//借款用途描述
    private Long mainLoanApplicationId;//借款申请主表ID
    private String riskControlInformation;//风险控制信息 
    
	//借款申请状态：0草稿，1风控审核中，2发标审核中，3投标中，4放款审核中，5待放款，6还款中，7已结清，8提前还贷，9取消，A流标
    public final static String APPLICATIONSTATE_DRAFT = "0";
    public final static String APPLICATIONSTATE_RISK_CONTROL_AUDIT = "1";
    public final static String APPLICATIONSTATE_ISSUING_AUDIT = "2";
    public final static String APPLICATIONSTATE_BIDING = "3";
    public final static String APPLICATIONSTATE_LOAN_REVIEW = "4";
    public final static String APPLICATIONSTATE_WAITING_LOAN = "5";
    public final static String APPLICATIONSTATE_REPAYMENT = "6";
    public final static String APPLICATIONSTATE_CLOSED_ACCOUNT = "7";
    public final static String APPLICATIONSTATE_EARLY_SETTLEMENT = "8";
    public final static String APPLICATIONSTATE_CANCEL = "9";
    public final static String APPLICATIONSTATE_FLOW_STANDARD = "A";
    
	//发布目标：0前台投标，1后台理财
    public final static String PUBLISHTARGET_FRONT_BID = "0";
    public final static String PUBLISHTARGET_BACKGROUND_FINANCING = "1";
    
	//放款审核状态:0未提交审核、1满标审核中、2放款审核中、3放款驳回、4放款通过
    public final static String LENDSTATE_NOT_AUDIT = "0";
    public final static String LENDSTATE_FULL_AUDIT = "1";
    public final static String LENDSTATE_LOAN_REVIEW = "2";
    public final static String LENDSTATE_LOAN_REJECTION = "3";
    public final static String LENDSTATE_LOAN_THROUGH = "4";
    
	//标的类型:1借款标,0债权标
    public final static String SUBJECTTYPE_LOANMARK = "1";
    public final static String SUBJECTTYPE_RIGHTSMARK = "0";
    
	//借款类型：0信贷、1房贷、2车贷
    public final static String LOANTYPE_CREDIT = "0";
    public final static String LOANTYPE_HOUSE = "1";
    public final static String LOANTYPE_CAR = "2";
    
	//审核状态：0未提交初审、1风控初审中、2风控复审中、3风控初审驳回、4风控复审驳回、5风控复审通过、6初审拒贷、7终审拒贷
    public final static String VERIFYSTATE_NOT_AUDIT = "0";
    public final static String VERIFYSTATE_FIRST_AUDIT = "1";
    public final static String VERIFYSTATE_REVIEW_AUDIT = "2";
    public final static String VERIFYSTATE_FIRST_REJECT = "3";
    public final static String VERIFYSTATE_REVIEW_REJECT = "4";
    public final static String VERIFYSTATE_REVIEW_THROUGH = "5";
    public final static String VERIFYSTATE_FIRST_REJECTION = "6";
    public final static String VERIFYSTATE_FINAL_REJECTION = "7";
    
	//发标审核状态：0未提交审核、1编写发表描述、2发标复审中、3发标复审通过、4驳回
    public final static String PUBLISHSTATE_NOT_AUDIT = "0";
    public final static String PUBLISHSTATE_WRITE_DESC = "1";
    public final static String PUBLISHSTATE_REVIEW = "2";
    public final static String PUBLISHSTATE_REVIEW_THROUGH = "3";
    public final static String PUBLISHSTATE_REJECT = "4";
    
    //来源类型（1.渠道：channel;2.门店：store）
    public final static String CHANNEL_CHANNEL = "1";
    public final static String CHANNEL_STORE = "2";

    public BigDecimal getMaxBuyBalance() {
        return maxBuyBalance;
    }

    public void setMaxBuyBalance(BigDecimal maxBuyBalance) {
        this.maxBuyBalance = maxBuyBalance;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
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

    public String getLoanUseageDesc() {
        return loanUseageDesc;
    }

    public void setLoanUseageDesc(String loanUseageDesc) {
        this.loanUseageDesc = loanUseageDesc;
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

    public String getPublishState() {
        return publishState;
    }

    public void setPublishState(String publishState) {
        this.publishState = publishState;
    }

	public Long getMainLoanApplicationId() {
		return mainLoanApplicationId;
	}

	public void setMainLoanApplicationId(Long mainLoanApplicationId) {
		this.mainLoanApplicationId = mainLoanApplicationId;
	}

	public String getRiskControlInformation() {
		return riskControlInformation;
	}

	public void setRiskControlInformation(String riskControlInformation) {
		this.riskControlInformation = riskControlInformation;
	}
	
    
}