package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 借款产品
 */
public class LoanProduct {

    private Long loanProductId;//借款产品ID
    private Long adminId;//员工ID
    private Long lendProductId;//出借产品ID(暂时不用)
    private String productCode;//产品编码
    private String productName;//产品名称
    private BigDecimal annualRate;//年利率
    private char repaymentMethod;//还款方法
    private char repaymentCycle;//还款周期
    private int cycleValue;//还款周期值
    private int cycleCounts;//周期数
    private char repaymentType;//还款方式
    private char dueTimeType;//期限类型
    private int dueTime;//期限时长
    private String productDesc;//产品描述
    private Date startDate;//有效开始日期
    private Date endDate;//有效结束日期
    private char productState;//产品状态
    private String versionCode;//版本号
    private Date createTime;//录入时间
    private Date lastMdfTime;//最后修改时间

    /**
     * 辅助字段
     */
    private String loanSearchState;
    private String searchDueTime;
    
    /**
     * 产品状态:0为有效，1为无效
     */

    public static final char PUBLISHSTATE_VALID = '0';
    public static final char PUBLISHSTATE_INVALID = '1';

    /**
     * 期限类型：1为天，2为月
     */
    public static final char DUETIMETYPE_DAY = '1';
    public static final char DUETIMETYPE_MONTH = '2';
    
    public String getDueTimeTypeStr(char dueTimeType){
    	if(this.DUETIMETYPE_DAY == dueTimeType){
    		return "天";
    	}else if (this.DUETIMETYPE_MONTH == dueTimeType) {
			return "月";
		}else {
			return String.valueOf(dueTimeType);
		}
    }

    /**
     * 还款周期类型：1为按月，2为到期，3为按天
     */
    public static final char REPAYMENTCYCLE_MONTH = '1';
    public static final char REPAYMENTCYCLE_ONCE = '2';
    public static final char REPAYMENTCYCLE_DAY = '3';

    /**
     * 还款方法：1为等额本息，2为等额本金
     */
    public static final char REPAYMENTMETHOD_AVERAGECAPITALPLUSINTEREST='1';
    public static final char REPAYMENTMETHOD_AVERAGECAPITAL='2';
    
    /**
     * 还款方式：1为周期还本息，2为周期还利息,到期还本金，3为周期还本金,到期还利息
     */
    public static final char REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST = '1';
    public static final char REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL = '2';
    public static final char REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_INTEREST_DUE = '3';
    
    public String getRepaymentTypeStr(char repaymentType){
    	if(this.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST == repaymentType){
    		return "周期还本息";
    	}else if (this.REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL == repaymentType) {
			return "周期还利息,到期还本金";
		}else if (this.REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_INTEREST_DUE == repaymentType) {
			return "周期还本金,到期还利息";
		}else {
			return String.valueOf(repaymentType);
		}
    }
    
    public Long getLoanProductId() {
        return loanProductId;
    }

    public void setLoanProductId(Long loanProductId) {
        this.loanProductId = loanProductId;
    }
    
    public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Long getLendProductId() {
        return lendProductId;
    }

    public void setLendProductId(Long lendProductId) {
        this.lendProductId = lendProductId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(BigDecimal annualRate) {
        this.annualRate = annualRate;
    }

    public char getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(char repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public char getRepaymentCycle() {
        return repaymentCycle;
    }

    public void setRepaymentCycle(char repaymentCycle) {
        this.repaymentCycle = repaymentCycle;
    }

    public int getCycleValue() {
        return cycleValue;
    }

    public void setCycleValue(int cycleValue) {
        this.cycleValue = cycleValue;
    }

    public int getCycleCounts() {
        return cycleCounts;
    }

    public void setCycleCounts(int cycleCounts) {
        this.cycleCounts = cycleCounts;
    }

    public char getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(char repaymentType) {
        this.repaymentType = repaymentType;
    }

    public char getDueTimeType() {
        return dueTimeType;
    }

    public void setDueTimeType(char dueTimeType) {
        this.dueTimeType = dueTimeType;
    }

    public int getDueTime() {
        return dueTime;
    }

    public void setDueTime(int dueTime) {
        this.dueTime = dueTime;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public char getProductState() {
        return productState;
    }

    public void setProductState(char productState) {
        this.productState = productState;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastMdfTime() {
        return lastMdfTime;
    }

    public void setLastMdfTime(Date lastMdfTime) {
        this.lastMdfTime = lastMdfTime;
    }

    public String getLoanSearchState() {
        return loanSearchState;
    }

    public void setLoanSearchState(String loanSearchState) {
        this.loanSearchState = loanSearchState;
    }

    public String getSearchDueTime() {
        return searchDueTime;
    }

    public void setSearchDueTime(String searchDueTime) {
        this.searchDueTime = searchDueTime;
    }


}
