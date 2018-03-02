package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.xt.cfp.core.pojo.CreditorRights;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;

public class CreditorRightsExtVo extends CreditorRights{

	private int dueTime;//时限
	private char dueTimeType;//时限值
	private BigDecimal  factBalance = BigDecimal.ZERO;//已回款总额
	private BigDecimal  expectProfit = BigDecimal.ZERO;//预计收益
	private BigDecimal  waitTotalpayMent = BigDecimal.ZERO;//待回款总额
	private BigDecimal  newWaitTotalpayMent = BigDecimal.ZERO;
	private BigDecimal  shouldBalance = BigDecimal.ZERO;
	private BigDecimal  annualRate = BigDecimal.ZERO;//年利率
	private BigDecimal  buyBalance = BigDecimal.ZERO;//出借金额
	private BigDecimal  surplusCalital = BigDecimal.ZERO;//剩余本金
	private String awardPoint;//奖励发放时机
	private String awardRate;//奖励利率
	private boolean rightsBtn;// 是否显示债权转让按钮
	private Long creditorRightsApplyId;//债权转让申请ID
	private String zr;//区别是否转让
	private String rightsStateStr;
	private String fromWhereStr;
	private String rateValue2;
	private String rateValue;
	private Object[] ylist;
	
	//加息标记 0-无，1-有
	private String rateFlag ;
	// 加息类型（0.奖励；1.加息券）
	private String rateType ;
	private BigDecimal  newRateValue ;
	//奖励信息
	private List<RateLendOrderVO> rateList ;
	
	private String loanApplicationCode;
	private String loanApplicationName;
	private String lenderLoginName;
	private String lenderRealName;
	private String loanLoginName;
	private String loanRealName;
	
	private String creditorRightsName;//债权名称
	private Date buyDate;//投标时间
	private String timeLimitType;//期限类型
	private Integer timeLimit;//期限值
	//债券还款明细
	private List<RightsRepaymentDetail> rightsRepaymentDetailList;
	//借款申请信息
	private LoanApplicationListVO loanApplicationListVO;
	private String loanType;//标的类型
	private String orderCode;//订单号
	private String rightsStateDesc;//债权状态值
	private String turnStateDesc;//转让状态值
	private String busStatus;//债权转让表中的交易状态
	private String repaymentType;//还款方式
	private Long loanProductId;//借款产品ID
	private BigDecimal waitBackBalance;//待收回款
	
	public String getRateValue2() {
		return rateValue2;
	}
	public void setRateValue2(String rateValue2) {
		this.rateValue2 = rateValue2;
	}
	public String getRateValue() {
		return rateValue;
	}
	public void setRateValue(String rateValue) {
		this.rateValue = rateValue;
	}
	public String getZr() {
		return zr;
	}
	public void setZr(String zr) {
		this.zr = zr;
	}
	
	public BigDecimal getAnnualRate() {
		return annualRate;
	}
	public void setAnnualRate(BigDecimal annualRate) {
		this.annualRate = annualRate;
	}
	public BigDecimal getBuyBalance() {
		return buyBalance;
	}
	public void setBuyBalance(BigDecimal buyBalance) {
		this.buyBalance = buyBalance;
	}
	public BigDecimal getNewWaitTotalpayMent() {
		return newWaitTotalpayMent;
	}
	public void setNewWaitTotalpayMent(BigDecimal newWaitTotalpayMent) {
		this.newWaitTotalpayMent = newWaitTotalpayMent;
	}
	public BigDecimal getShouldBalance() {
		return shouldBalance;
	}
	public void setShouldBalance(BigDecimal shouldBalance) {
		this.shouldBalance = shouldBalance;
	}

	public String getLoanApplicationCode() {
		return loanApplicationCode;
	}
	public void setLoanApplicationCode(String loanApplicationCode) {
		this.loanApplicationCode = loanApplicationCode;
	}
	public String getLoanApplicationName() {
		return loanApplicationName;
	}
	public void setLoanApplicationName(String loanApplicationName) {
		this.loanApplicationName = loanApplicationName;
	}

	public LoanApplicationListVO getLoanApplicationListVO() {
		return loanApplicationListVO;
	}

	public void setLoanApplicationListVO(LoanApplicationListVO loanApplicationListVO) {
		this.loanApplicationListVO = loanApplicationListVO;
	}

	public String getTimeLimitType() {
		return timeLimitType;
	}

	public String getLoanLoginName() {
		return loanLoginName;
	}

	public void setLoanLoginName(String loanLoginName) {
		this.loanLoginName = loanLoginName;
	}

	public void setTimeLimitType(String timeLimitType) {
		this.timeLimitType = timeLimitType;
	}

	public Integer getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	public BigDecimal getExpectProfit() {
		return this.expectProfit;
	}
	public void setExpectProfit(BigDecimal expectProfit) {
		this.expectProfit = expectProfit;
	}

	public BigDecimal getWaitTotalpayMent() {
		return waitTotalpayMent;
	}

	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	public String getCreditorRightsName() {
		return creditorRightsName;
	}

	public void setCreditorRightsName(String creditorRightsName) {
		this.creditorRightsName = creditorRightsName;
	}

	public List<RightsRepaymentDetail> getRightsRepaymentDetailList() {
		return rightsRepaymentDetailList;
	}

	public void setRightsRepaymentDetailList(List<RightsRepaymentDetail> rightsRepaymentDetailList) {
		this.rightsRepaymentDetailList = rightsRepaymentDetailList;
	}

	public void setWaitTotalpayMent(BigDecimal waitTotalpayMent) {
		this.waitTotalpayMent = waitTotalpayMent;
	}

	public String getLenderLoginName() {
		return lenderLoginName;
	}

	public void setLenderLoginName(String lenderLoginName) {
		this.lenderLoginName = lenderLoginName;
	}

	public String getLenderRealName() {
		return lenderRealName;
	}

	public void setLenderRealName(String lenderRealName) {
		this.lenderRealName = lenderRealName;
	}

	public int getDueTime() {
		return dueTime;
	}
	public void setDueTime(int dueTime) {
		this.dueTime = dueTime;
	}
	public char getDueTimeType() {
		return dueTimeType;
	}
	public void setDueTimeType(char dueTimeType) {
		this.dueTimeType = dueTimeType;
	}
	public BigDecimal getFactBalance() {
		return factBalance;
	}
	public void setFactBalance(BigDecimal factBalance) {
		this.factBalance = factBalance;
	}
	public String getAwardPoint() {
		return awardPoint;
	}
	public void setAwardPoint(String awardPoint) {
		this.awardPoint = awardPoint;
	}
	public String getAwardRate() {
		return awardRate;
	}
	public void setAwardRate(String awardRate) {
		this.awardRate = awardRate;
	}
	public BigDecimal getSurplusCalital() {
		return surplusCalital;
	}
	public void setSurplusCalital(BigDecimal surplusCalital) {
		this.surplusCalital = surplusCalital;
	}
	public boolean isRightsBtn() {
		return rightsBtn;
	}
	public void setRightsBtn(boolean rightsBtn) {
		this.rightsBtn = rightsBtn;
	}
	public Long getCreditorRightsApplyId() {
		return creditorRightsApplyId;
	}
	public void setCreditorRightsApplyId(Long creditorRightsApplyId) {
		this.creditorRightsApplyId = creditorRightsApplyId;
	}
	public String getRateFlag() {
		return rateFlag;
	}
	public void setRateFlag(String rateFlag) {
		this.rateFlag = rateFlag;
	}
	public String getRateType() {
		return rateType;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
	public BigDecimal getNewRateValue() {
		return newRateValue;
	}
	public void setNewRateValue(BigDecimal newRateValue) {
		this.newRateValue = newRateValue;
	}
	public List<RateLendOrderVO> getRateList() {
		return rateList;
	}
	public void setRateList(List<RateLendOrderVO> rateList) {
		this.rateList = rateList;
	}
	public String getRightsStateStr() {
		return rightsStateStr;
	}
	public void setRightsStateStr(String rightsStateStr) {
		this.rightsStateStr = rightsStateStr;
	}
	public String getFromWhereStr() {
		return fromWhereStr;
	}
	public void setFromWhereStr(String fromWhereStr) {
		this.fromWhereStr = fromWhereStr;
	}
	public Object[] getYlist() {
		return ylist;
	}
	public void setYlist(Object[] ylist) {
		this.ylist = ylist;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getLoanRealName() {
		return loanRealName;
	}
	public void setLoanRealName(String loanRealName) {
		this.loanRealName = loanRealName;
	}
	public String getRightsStateDesc() {
		return rightsStateDesc;
	}
	public void setRightsStateDesc(String rightsStateDesc) {
		this.rightsStateDesc = rightsStateDesc;
	}
	public String getTurnStateDesc() {
		return turnStateDesc;
	}
	public void setTurnStateDesc(String turnStateDesc) {
		this.turnStateDesc = turnStateDesc;
	}
	public String getBusStatus() {
		return busStatus;
	}
	public void setBusStatus(String busStatus) {
		this.busStatus = busStatus;
	}
	public String getRepaymentType() {
		return repaymentType;
	}
	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}
	public Long getLoanProductId() {
		return loanProductId;
	}
	public void setLoanProductId(Long loanProductId) {
		this.loanProductId = loanProductId;
	}
	public BigDecimal getWaitBackBalance() {
		return waitBackBalance;
	}
	public void setWaitBackBalance(BigDecimal waitBackBalance) {
		this.waitBackBalance = waitBackBalance;
	}
	
}
