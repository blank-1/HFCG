package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权信息
 */
public class CreditorRights {
    private long creditorRightsId;//债权ID[主键]
    private long lendOrderId;//出借产品订单ID[外键]
    private long loanApplicationId;//借款申请ID[外键]
    private long loanAccountId;//借款放款账户ID[外键]
    private long lendAccountId;//出借资金账户ID[外键]
    
    private long lendUserId;// 出借人用户ID[外键]
    private long loanUserId;// 借款人用户ID[外键]
    
    private char lenderType;//出借人类型
    private String creditorRightsCode;//债权编码
    private Date lendTime;//出借时间
    private Date agreementStartDate;//合同生效日期
    private Date completeTime;//债权结清时间
    private Date firstRepaymentDate;//首期还款日
    private Date lastRepaymentDate;//还款截止日

    private Date createTime;//生成时间
    private Date applyRollOutTime;//提交转出时间

    private BigDecimal rightsWorth = BigDecimal.ZERO;//债权价值
    private BigDecimal lendPrice = BigDecimal.ZERO;//债权价格
    private BigDecimal buyPrice = BigDecimal.ZERO;//购买金额
    private BigDecimal annualRate = BigDecimal.ZERO;//利率
    private int repaymentCycle;//还款周期
    private char rightsState;//债权状态
    private char transferState;//放款状态
    private char fromWhere;//债权来源
    private long channelType;//来源渠道
    private char isDelay;//本期逾期状态
    private char displayState;//显示状态

    private String currentCycle;//当前期号
    private Date currentPayDate;//当前还款日
    
    private long repaymentAccountId;//借款还款账户ID
    private char turnState;//债权转让状态(0.未申请,1.申请转出;2.已转让)

    // 辅助字段
    private String loanCustomerName;//借款人姓名
    private char loanCustomerCerType;//借款人证件类型
    private String loanCustomerCerCode;//借款人证件号
    private String loanSystemLoginName;//借款人和信贷账户
    private String lendSystemLoginName;//出借人和信贷账户
    private String lendCustomerName;//出借人姓名
    private char lendCustomerCerType;//出借人证件类型
    private String lendCustomerCerCode;//出借人证件号
    
    private String sum_shouldCapital2;//出借金额 =sum(应还本金2)
    private String sum_factBalance;//已收回款 =sum(实还总额)
    private String sum_shouldBalance2_factBalance;//待收回款 =sum(应还总额2-实际总额)
    private String shouldBalance2;//总计应收回款 =sum(应还总额2)
    
    private String loanApplicationName;//借款名称
    private String originalUserId;//原始债权人
    
    /**
     * 债权来源：0为购买，1为转让
     */
    public static final char FROMWHERE_BUY = '0';
    public static final char FROMWHERE_TURNIN = '1';

    /**
     * 债权状态：0为未放款，1已放款
     */
    public static final char TRANSFERSTATE_WAITING = '0';
    public static final char TRANSFERSTATE_HAVEN = '1';

    /**
     * 本期是否逾期还款，1为逾期，0为未逾期
     */
    public static final char ISDELAY_YES = '1';
    public static final char ISDELAY_NO = '0';

    /**
     * 显示状态：0为隐藏，1为显示
     */
    public static final char DISPLAYSTATE_DISPLAY = '1';
    public static final char DISPLAYSTATE_HIDDEN = '0';

    /**
     * 出借人类型：1为出借人，2为渠道账户
     */
    public static final char LENDERTYPE_LENDER = '1';
    public static final char LENDERTYPE_CHANNEL = '2';
    
    /**
     * 债权转让状态(1.回款中；2.回款预期；3.已结清；4.转让中；5.已转让)
     */
    public static final char TURNSTATE_BACKING = '1';
    public static final char TURNSTATE_COMPARED_EXPECTED = '2';
    public static final char TURNSTATE_SETTLED = '3';
    public static final char TURNSTATE_TRANSFER = '4';
    public static final char TURNSTATE_TRANSFERRED = '5';

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public char getLenderType() {
        return lenderType;
    }

    public void setLenderType(char lenderType) {
        this.lenderType = lenderType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getApplyRollOutTime() {
        return applyRollOutTime;
    }

    public void setApplyRollOutTime(Date applyRollOutTime) {
        this.applyRollOutTime = applyRollOutTime;
    }

    public String getLoanSystemLoginName() {
        return loanSystemLoginName;
    }

    public void setLoanSystemLoginName(String loanSystemLoginName) {
        this.loanSystemLoginName = loanSystemLoginName;
    }

    public String getLendSystemLoginName() {
        return lendSystemLoginName;
    }

    public void setLendSystemLoginName(String lendSystemLoginName) {
        this.lendSystemLoginName = lendSystemLoginName;
    }

    public String getLoanCustomerName() {
        return loanCustomerName;
    }

    public void setLoanCustomerName(String loanCustomerName) {
        this.loanCustomerName = loanCustomerName;
    }

    public char getLoanCustomerCerType() {
        return loanCustomerCerType;
    }

    public void setLoanCustomerCerType(char loanCustomerCerType) {
        this.loanCustomerCerType = loanCustomerCerType;
    }

    public String getLoanCustomerCerCode() {
        return loanCustomerCerCode;
    }

    public void setLoanCustomerCerCode(String loanCustomerCerCode) {
        this.loanCustomerCerCode = loanCustomerCerCode;
    }

    public String getLendCustomerName() {
        return lendCustomerName;
    }

    public void setLendCustomerName(String lendCustomerName) {
        this.lendCustomerName = lendCustomerName;
    }

    public char getLendCustomerCerType() {
        return lendCustomerCerType;
    }

    public void setLendCustomerCerType(char lendCustomerCerType) {
        this.lendCustomerCerType = lendCustomerCerType;
    }

    public String getLendCustomerCerCode() {
        return lendCustomerCerCode;
    }

    public void setLendCustomerCerCode(String lendCustomerCerCode) {
        this.lendCustomerCerCode = lendCustomerCerCode;
    }

    public long getCreditorRightsId() {
        return creditorRightsId;
    }

    public void setCreditorRightsId(long creditorRightsId) {
        this.creditorRightsId = creditorRightsId;
    }

    public long getLendOrderId() {
        return lendOrderId;
    }

    public void setLendOrderId(long lendOrderId) {
        this.lendOrderId = lendOrderId;
    }

    public long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public long getLoanAccountId() {
        return loanAccountId;
    }

    public void setLoanAccountId(long loanAccountId) {
        this.loanAccountId = loanAccountId;
    }

    public long getLendAccountId() {
        return lendAccountId;
    }

    public void setLendAccountId(long lendAccountId) {
        this.lendAccountId = lendAccountId;
    }

    public String getCreditorRightsCode() {
        return creditorRightsCode;
    }

    public void setCreditorRightsCode(String creditorRightsCode) {
        this.creditorRightsCode = creditorRightsCode;
    }

    public Date getLendTime() {
        return lendTime;
    }

    public void setLendTime(Date lendTime) {
        this.lendTime = lendTime;
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

    public BigDecimal getRightsWorth() {
        return rightsWorth;
    }

    public void setRightsWorth(BigDecimal rightsWorth) {
        this.rightsWorth = rightsWorth;
    }

    public BigDecimal getLendPrice() {
        return lendPrice;
    }

    public void setLendPrice(BigDecimal lendPrice) {
        this.lendPrice = lendPrice;
    }

    public BigDecimal getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(BigDecimal annualRate) {
        this.annualRate = annualRate;
    }

    public int getRepaymentCycle() {
        return repaymentCycle;
    }

    public void setRepaymentCycle(int repaymentCycle) {
        this.repaymentCycle = repaymentCycle;
    }

    public char getRightsState() {
        return rightsState;
    }

    public void setRightsState(char rightsState) {
        this.rightsState = rightsState;
    }

    public char getTransferState() {
        return transferState;
    }

    public void setTransferState(char transferState) {
        this.transferState = transferState;
    }

    public char getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(char fromWhere) {
        this.fromWhere = fromWhere;
    }

    public long getChannelType() {
        return channelType;
    }

    public void setChannelType(long channelType) {
        this.channelType = channelType;
    }

    public char getIsDelay() {
        return isDelay;
    }

    public void setIsDelay(char isDelay) {
        this.isDelay = isDelay;
    }

    public char getDisplayState() {
        return displayState;
    }

    public void setDisplayState(char displayState) {
        this.displayState = displayState;
    }

	public long getRepaymentAccountId() {
		return repaymentAccountId;
	}

	public void setRepaymentAccountId(long repaymentAccountId) {
		this.repaymentAccountId = repaymentAccountId;
	}

	public char getTurnState() {
		return turnState;
	}

	public void setTurnState(char turnState) {
		this.turnState = turnState;
	}

	public String getSum_shouldCapital2() {
		return sum_shouldCapital2;
	}

	public void setSum_shouldCapital2(String sum_shouldCapital2) {
		this.sum_shouldCapital2 = sum_shouldCapital2;
	}

	public String getSum_factBalance() {
		return sum_factBalance;
	}

	public void setSum_factBalance(String sum_factBalance) {
		this.sum_factBalance = sum_factBalance;
	}

	public String getSum_shouldBalance2_factBalance() {
		return sum_shouldBalance2_factBalance;
	}

	public void setSum_shouldBalance2_factBalance(
			String sum_shouldBalance2_factBalance) {
		this.sum_shouldBalance2_factBalance = sum_shouldBalance2_factBalance;
	}

	public String getShouldBalance2() {
		return shouldBalance2;
	}

	public void setShouldBalance2(String shouldBalance2) {
		this.shouldBalance2 = shouldBalance2;
	}

	public String getLoanApplicationName() {
		return loanApplicationName;
	}

	public void setLoanApplicationName(String loanApplicationName) {
		this.loanApplicationName = loanApplicationName;
	}

	public String getOriginalUserId() {
		return originalUserId;
	}

	public void setOriginalUserId(String originalUserId) {
		this.originalUserId = originalUserId;
	}

	public long getLendUserId() {
		return lendUserId;
	}

	public void setLendUserId(long lendUserId) {
		this.lendUserId = lendUserId;
	}

	public long getLoanUserId() {
		return loanUserId;
	}

	public void setLoanUserId(long loanUserId) {
		this.loanUserId = loanUserId;
	}

    public String getCurrentCycle() {
        return currentCycle;
    }

    public void setCurrentCycle(String currentCycle) {
        this.currentCycle = currentCycle;
    }

    public Date getCurrentPayDate() {
        return currentPayDate;
    }

    public void setCurrentPayDate(Date currentPayDate) {
        this.currentPayDate = currentPayDate;
    }
}
