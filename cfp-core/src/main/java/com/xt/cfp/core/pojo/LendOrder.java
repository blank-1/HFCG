package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 出借订单
 */
public class LendOrder {
    private Long lendOrderId;//出借产品订单ID
    private Long customerAccountId;//客户资金账户ID
    private Long lendProductId;//出借产品ID
    private Long lendProductPublishId;//发布明细Id
    private Long inCardId;//打款客户卡ID
    
    private BigDecimal currentProfit;//当前收益
    private BigDecimal currentProfit2;//当前收益2
    private Long outCardId;//划扣客户卡ID
    private Long loanApplicationId;//借款申请ID
    private Long lendUserId;//出借人ID
    
    private String productType;//产品分类
    private String orderCode;//订单编码
    private String lendOrderName;//出借合同名称
    private String unitOrderCode;//关联合同号
    private String renewOrderCode;//续签合同号	
    
    private String useTotalHostoryOrder;//是否使用历史总额
    private Date buyTime;//购买时间
    private Date agreementStartDate;//合同生效日期
    private Date completionDate;//合同结清日期
    private Date agreementEndDate;//合同结束日期
    
    private Date closingOverDate;//封闭截止日
    private BigDecimal buyBalance;//购买总金额
    private BigDecimal forLendBalance;//待理财金额
    private String orderState;//订单状态
    private Date payTime;//支付时间
    
    private String displayState;//显示状态
    private Date recordTime;//录入时间
    private Long salerPersonnel;//销售人员
    private String salerCode;//销售人员工号
    private String salerName;//销售人员姓名
    
    private Long recorderPersonnel;//录入人员
    private String recorderCode;//录入人员工号
    private String recorderName;//录入人员姓名
    private BigDecimal profitRate;//收益利率
    private String closingType;//封闭期类型
    
    private Integer closingDate;//封闭期(天)
    private String  timeLimitType;//期限类型
    private Integer timeLimit;//期限值
    private String toInterestPoint;//返息周期
    private String renewalCycleType;//续费周期类型
    
    private String renewalType;//续费起投类型
    private BigDecimal renewal;//续费起投额	
    private String theReturnMethod;//到期返还方式
    private Date drawCashTime;//提现时间
    private Date financiaDrawCashTime;//财务提现时间

    private Long lendOrderPId;//父订单id
    
    //省心计划：是否使用财富券 0 -- 否 ， 1 -- 是
    private String isUseVoucher ;
    //省心计划收益返回配置：0 -- 返回至省心账户， 1 -- 返回至资金账户
    private String profitReturnConfig ;
    
    //产品ID字段说明：对于出借类订单，此ID为借款ID，对于省心计划订单，此ID为省心计划ID
    
    //订单状态（省心计划）：0未支付，1理财中，2已结清，3已过期，4已撤销，5匹配中，6退出中
    public static final String ORDERSTATE_FINANCING_NOT_PAID = "0";
    public static final String ORDERSTATE_FINANCING_FINANCING = "1";
    public static final String ORDERSTATE_FINANCING_SETTLED = "2";
    public static final String ORDERSTATE_FINANCING_EXPIRED = "3";
    public static final String ORDERSTATE_FINANCING_REVOKED = "4";
    public static final String ORDERSTATE_FINANCING_MATCHING = "5";
    public static final String ORDERSTATE_FINANCING_EXIT = "6";
    
    //订单状态（出借订单）：0未支付not paid，1还款中Repayment，2已结清Settled，4已撤销Revoked，5已支付Paid，7流标Flow standard
    public static final String ORDERSTATE_LEND_NOT_PAID = "0";
    public static final String ORDERSTATE_LEND_REPAYMENT = "1";
    public static final String ORDERSTATE_LEND_SETTLED = "2";
    public static final String ORDERSTATE_LEND_REVOKED = "4";
    public static final String ORDERSTATE_LEND_PAID = "5";
    public static final String ORDERSTATE_LEND_FLOW_STANDARD = "7";
    
    //产品分类：1投标(出借)，2理财
    public static final String PRODUCTTYPE_LEND = "1";
    public static final String PRODUCTTYPE_FINANCING = "2";
    
    //是否使用历史总额：0不，1是
    public static final String USETOTALHOSTORYORDER_NO = "0";
    public static final String USETOTALHOSTORYORDER_YES = "1";
    
    //显示状态：0不显示，1显示
    public static final String DISPLAYSTATE_NO = "0";
    public static final String DISPLAYSTATE_YES = "1";
    
    //到期还款方式：0返还至账户，1打到银行卡
    public static final String THERETURNMETHOD_ACCOUNT = "0";
    public static final String THERETURNMETHOD_BANKCARD = "1";
    
    // 辅助字段
    private String lendUserName;//出借人姓名
    private Integer orderPayState;//订单支付状态
    private String rightingState;//标的状态

    public Long getLendOrderPId() {
        return lendOrderPId;
    }

    public void setLendOrderPId(Long lendOrderPId) {
        this.lendOrderPId = lendOrderPId;
    }

    public Long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(Long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public Long getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(Long customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    public Long getLendProductId() {
        return lendProductId;
    }

    public void setLendProductId(Long lendProductId) {
        this.lendProductId = lendProductId;
    }

    public Long getLendProductPublishId() {
        return lendProductPublishId;
    }

    public void setLendProductPublishId(Long lendProductPublishId) {
        this.lendProductPublishId = lendProductPublishId;
    }

    public Long getInCardId() {
        return inCardId;
    }

    public void setInCardId(Long inCardId) {
        this.inCardId = inCardId;
    }

    public BigDecimal getCurrentProfit() {
        return currentProfit;
    }

    public void setCurrentProfit(BigDecimal currentProfit) {
        this.currentProfit = currentProfit;
    }

    public BigDecimal getCurrentProfit2() {
        return currentProfit2;
    }

    public void setCurrentProfit2(BigDecimal currentProfit2) {
        this.currentProfit2 = currentProfit2;
    }

    public Long getOutCardId() {
        return outCardId;
    }

    public void setOutCardId(Long outCardId) {
        this.outCardId = outCardId;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public Long getLendUserId() {
        return lendUserId;
    }

    public void setLendUserId(Long lendUserId) {
        this.lendUserId = lendUserId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType == null ? null : productType.trim();
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    public String getLendOrderName() {
        return lendOrderName;
    }

    public void setLendOrderName(String lendOrderName) {
        this.lendOrderName = lendOrderName == null ? null : lendOrderName.trim();
    }

    public String getUnitOrderCode() {
        return unitOrderCode;
    }

    public void setUnitOrderCode(String unitOrderCode) {
        this.unitOrderCode = unitOrderCode == null ? null : unitOrderCode.trim();
    }

    public String getRenewOrderCode() {
        return renewOrderCode;
    }

    public void setRenewOrderCode(String renewOrderCode) {
        this.renewOrderCode = renewOrderCode == null ? null : renewOrderCode.trim();
    }

    public String getUseTotalHostoryOrder() {
        return useTotalHostoryOrder;
    }

    public void setUseTotalHostoryOrder(String useTotalHostoryOrder) {
        this.useTotalHostoryOrder = useTotalHostoryOrder == null ? null : useTotalHostoryOrder.trim();
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public Date getAgreementStartDate() {
        return agreementStartDate;
    }

    public void setAgreementStartDate(Date agreementStartDate) {
        this.agreementStartDate = agreementStartDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public Date getAgreementEndDate() {
        return agreementEndDate;
    }

    public void setAgreementEndDate(Date agreementEndDate) {
        this.agreementEndDate = agreementEndDate;
    }

    public Date getClosingOverDate() {
        return closingOverDate;
    }

    public void setClosingOverDate(Date closingOverDate) {
        this.closingOverDate = closingOverDate;
    }

    public BigDecimal getBuyBalance() {
        return buyBalance;
    }

    public void setBuyBalance(BigDecimal buyBalance) {
        this.buyBalance = buyBalance;
    }

    public BigDecimal getForLendBalance() {
        return forLendBalance;
    }

    public void setForLendBalance(BigDecimal forLendBalance) {
        this.forLendBalance = forLendBalance;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState == null ? null : orderState.trim();
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getDisplayState() {
        return displayState;
    }

    public void setDisplayState(String displayState) {
        this.displayState = displayState == null ? null : displayState.trim();
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Long getSalerPersonnel() {
        return salerPersonnel;
    }

    public void setSalerPersonnel(Long salerPersonnel) {
        this.salerPersonnel = salerPersonnel;
    }

    public String getSalerCode() {
        return salerCode;
    }

    public void setSalerCode(String salerCode) {
        this.salerCode = salerCode == null ? null : salerCode.trim();
    }

    public String getSalerName() {
        return salerName;
    }

    public void setSalerName(String salerName) {
        this.salerName = salerName == null ? null : salerName.trim();
    }

    public Long getRecorderPersonnel() {
        return recorderPersonnel;
    }

    public void setRecorderPersonnel(Long recorderPersonnel) {
        this.recorderPersonnel = recorderPersonnel;
    }

    public String getRecorderCode() {
        return recorderCode;
    }

    public void setRecorderCode(String recorderCode) {
        this.recorderCode = recorderCode == null ? null : recorderCode.trim();
    }

    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName == null ? null : recorderName.trim();
    }

    public BigDecimal getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(BigDecimal profitRate) {
        this.profitRate = profitRate;
    }

    public String getClosingType() {
        return closingType;
    }

    public void setClosingType(String closingType) {
        this.closingType = closingType == null ? null : closingType.trim();
    }

    public Integer getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Integer closingDate) {
        this.closingDate = closingDate;
    }

    public String getTimeLimitType() {
        return timeLimitType;
    }

    public void setTimeLimitType(String timeLimitType) {
        this.timeLimitType = timeLimitType == null ? null : timeLimitType.trim();
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getToInterestPoint() {
        return toInterestPoint;
    }

    public void setToInterestPoint(String toInterestPoint) {
        this.toInterestPoint = toInterestPoint == null ? null : toInterestPoint.trim();
    }

    public String getRenewalCycleType() {
        return renewalCycleType;
    }

    public void setRenewalCycleType(String renewalCycleType) {
        this.renewalCycleType = renewalCycleType == null ? null : renewalCycleType.trim();
    }

    public String getRenewalType() {
        return renewalType;
    }

    public void setRenewalType(String renewalType) {
        this.renewalType = renewalType == null ? null : renewalType.trim();
    }

    public BigDecimal getRenewal() {
        return renewal;
    }

    public void setRenewal(BigDecimal renewal) {
        this.renewal = renewal;
    }

    public String getTheReturnMethod() {
        return theReturnMethod;
    }

    public void setTheReturnMethod(String theReturnMethod) {
        this.theReturnMethod = theReturnMethod == null ? null : theReturnMethod.trim();
    }

    public Date getDrawCashTime() {
        return drawCashTime;
    }

    public void setDrawCashTime(Date drawCashTime) {
        this.drawCashTime = drawCashTime;
    }

    public Date getFinanciaDrawCashTime() {
        return financiaDrawCashTime;
    }

    public void setFinanciaDrawCashTime(Date financiaDrawCashTime) {
        this.financiaDrawCashTime = financiaDrawCashTime;
    }

	public String getLendUserName() {
		return lendUserName;
	}

	public void setLendUserName(String lendUserName) {
		this.lendUserName = lendUserName;
	}

	public Integer getOrderPayState() {
		return orderPayState;
	}

	public void setOrderPayState(Integer orderPayState) {
		this.orderPayState = orderPayState;
	}

	public String getRightingState() {
		return rightingState;
	}

	public void setRightingState(String rightingState) {
		this.rightingState = rightingState;
	}

	public String getIsUseVoucher() {
		return isUseVoucher;
	}

	public void setIsUseVoucher(String isUseVoucher) {
		this.isUseVoucher = StringUtils.isBlank(isUseVoucher)?"0":isUseVoucher;
	}

	public String getProfitReturnConfig() {
		return profitReturnConfig;
	}

	public void setProfitReturnConfig(String profitReturnConfig) {
		this.profitReturnConfig = profitReturnConfig;
	}
	
}