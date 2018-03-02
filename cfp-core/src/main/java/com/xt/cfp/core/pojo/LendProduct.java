package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出借产品
 */
public class LendProduct {
	protected long lendProductId;
	protected String productType;
	protected String productName;
	protected String productCode;
	protected BigDecimal profitRate;
	protected int closingDate;
	protected String closingType;
	protected BigDecimal startsAt;
	protected BigDecimal upAt;
	protected String toInterestPoint;
	protected String renewalCycleType;
	protected String renewalType;
	protected BigDecimal renewal;
	protected String versionCode;
	protected String productDesc;
	protected Date createTime;
	protected Date lastMdfTime;
	protected String timeLimitType;
	protected int timeLimit;
	protected String productState;
	protected String guaranteeType;
	protected BigDecimal profitRateMax;


	/**
	 * 产品类型：1为债权类出借产品，2为省心计划出借产品
	 */
	public static final String PRODUCTTYPE_RIGHTS = "1";
	public static final String PRODUCTTYPE_FINANCING = "2";

	/**
	 * 续费起投额：1为大于等于起投额，2为大于等于递增金额，3为大于等于自定义金额
	 */
	public static final String RENEWALBALANCETYPE_MORETHANSTARTSAT = "1";
	public static final String RENEWALBALANCETYPE_MORETHANUPAT = "2";
	public static final String RENEWALBALANCETYPE_MORETHANDEFINE = "3";

	/**
	 * 期限类型为天时，返息周期长度：周按7天算，月按30天算，季度是90天算，半年按180天，一年按365天
	 */
	public static final int TIMELIMITTYPE_DAY_WEEK = 7;
	public static final int TIMELIMITTYPE_DAY_MONTH = 30;
	public static final int TIMELIMITTYPE_DAY_QUARTER = 90;
	public static final int TIMELIMITTYPE_DAY_HALFYEAR = 180;
	public static final int TIMELIMITTYPE_DAY_YEAR = 365;
	public static final int TIMELIMITTYPE_MONTH_YEAR = 12;

	/**
	 * 产品状态：0为有效，1为失效
	 */
	public static final String PRODUCTSTATE_VALID = "0";
	public static final String PRODUCTSTATE_INVALID = "1";

	/**
	 * 续费周期 : 1为不允许
	 */
	public static final String RENEWALCYCLETYPE_NOT_ALLOW = "1";

	/**
	 * 返息周期：1为周，2为月，3为季度，4为半年，5为一年，6为退出时
	 */
	public static final String TOINTERESTPOINT_WEEK = "1";
	public static final String TOINTERESTPOINT_MONTH = "2";
	public static final String TOINTERESTPOINT_QUARTER = "3";
	public static final String TOINTERESTPOINT_HALFYEAR = "4";
	public static final String TOINTERESTPOINT_YEAR = "5";
	public static final String TOINTERESTPOINT_BEQUIT = "6";

	/**
	 * 保障类型：1本息保障、2本金保障、3无
	 */
	public static final String GUARANTEE_TYPE_INTEREST = "1";
	public static final String GUARANTEE_TYPE_PRINCIPAL = "2";
	public static final String GUARANTEE_TYPE_NONE = "3";

	public String getProductState() {
		return productState;
	}

	public void setProductState(String productState) {
		this.productState = productState;
	}

	public String getTimeLimitType() {
		return timeLimitType;
	}

	public void setTimeLimitType(String timeLimitType) {
		this.timeLimitType = timeLimitType;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getClosingType() {
		return closingType;
	}

	public void setClosingType(String closingType) {
		this.closingType = closingType;
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

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public long getLendProductId() {
		return lendProductId;
	}

	public void setLendProductId(long lendProductId) {
		this.lendProductId = lendProductId;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public void setToInterestPoint(String toInterestPoint) {
		this.toInterestPoint = toInterestPoint;
	}

	public void setRenewalCycleType(String renewalCycleType) {
		this.renewalCycleType = renewalCycleType;
	}

	public void setRenewalType(String renewalType) {
		this.renewalType = renewalType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(BigDecimal profitRate) {
		this.profitRate = profitRate;
	}

	public int getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(int closingDate) {
		this.closingDate = closingDate;
	}

	public BigDecimal getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(BigDecimal startsAt) {
		this.startsAt = startsAt;
	}

	public BigDecimal getUpAt() {
		return upAt;
	}

	public void setUpAt(BigDecimal upAt) {
		this.upAt = upAt;
	}

	public BigDecimal getRenewal() {
		return renewal;
	}

	public void setRenewal(BigDecimal renewal) {
		this.renewal = renewal;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getToInterestPoint() {
		return toInterestPoint;
	}

	public String getRenewalCycleType() {
		return renewalCycleType;
	}

	public String getRenewalType() {
		return renewalType;
	}

	public String getGuaranteeType() {
		return guaranteeType;
	}

	public void setGuaranteeType(String guaranteeType) {
		this.guaranteeType = guaranteeType;
	}
	
	public BigDecimal getProfitRateMax() {
		return profitRateMax;
	}

	public void setProfitRateMax(BigDecimal profitRateMax) {
		this.profitRateMax = profitRateMax;
	}
}
