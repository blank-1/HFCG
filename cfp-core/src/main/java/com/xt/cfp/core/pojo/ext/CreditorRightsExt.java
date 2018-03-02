package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.RepaymentRecord;
import com.xt.cfp.core.pojo.RightsRepaymentDetail;

import java.util.ArrayList;
import java.util.List;

public class CreditorRightsExt {
	private String creditorRightsName;
	private String loanLoginName;
	private String buyPrice;
	private String waitTotalpayMent;
	private String factBalance;
	private String currentPayDate;
	private String rightsState;
	private String buyDate;
	private String expectProfit;
	private String creditorRightsId;
	private String cycleCount;
	private String annualRate;
	private String repayMentMethod;
	private String expireDays;
	private String loanType;
	private String index;
	
	
	
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getLoanType() {
		return loanType;
	}
	private String  turn ;//是否是 转让
	//还款计划
	List<RightsRepaymentDetail> details =new ArrayList<RightsRepaymentDetail>();
	//还款记录
	private  RepaymentRecord repaymentRecord;
	
	
	public RepaymentRecord getRepaymentRecord() {
		return repaymentRecord;
	}
	public void setRepaymentRecord(RepaymentRecord repaymentRecord) {
		this.repaymentRecord = repaymentRecord;
	}
	public List<RightsRepaymentDetail> getDetails() {
		return details;
	}
	public void setDetails(List<RightsRepaymentDetail> details) {
		this.details = details;
	}
	public String getTurn() {
		return turn;
	}
	public void setTurn(String turn) {
		this.turn = turn;
	}
	//来源
	private char fromWhere;
	//合同生效时间
	private int agreementDays;
	//申请id
	private Long creditorRightsApplyId;
	
	
	private String awardPoint;
	private String awardRate;
	
	
	 
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
	public Long getCreditorRightsApplyId() {
		return creditorRightsApplyId;
	}
	public void setCreditorRightsApplyId(Long creditorRightsApplyId) {
		this.creditorRightsApplyId = creditorRightsApplyId;
	}
	public int getAgreementDays() {
		return agreementDays;
	}
	public void setAgreementDays(int agreementDays) {
		this.agreementDays = agreementDays;
	}
	public char getFromWhere() {
		return fromWhere;
	}
	public void setFromWhere(char fromWhere) {
		this.fromWhere = fromWhere;
	}
	public String getCreditorRightsName() {
		return creditorRightsName;
	}
	public void setCreditorRightsName(String creditorRightsName) {
		this.creditorRightsName = creditorRightsName;
	}
	public String getLoanLoginName() {
		return loanLoginName;
	}
	public void setLoanLoginName(String loanLoginName) {
		this.loanLoginName = loanLoginName;
	}
	public String getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}
	public String getWaitTotalpayMent() {
		return waitTotalpayMent;
	}
	public void setWaitTotalpayMent(String waitTotalpayMent) {
		this.waitTotalpayMent = waitTotalpayMent;
	}
	public String getFactBalance() {
		return factBalance;
	}
	public void setFactBalance(String factBalance) {
		this.factBalance = factBalance;
	}
	public String getCurrentPayDate() {
		return currentPayDate;
	}
	public void setCurrentPayDate(String currentPayDate) {
		this.currentPayDate = currentPayDate;
	}
	public String getRightsState() {
		return rightsState;
	}
	public void setRightsState(String rightsState) {
		this.rightsState = rightsState;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public String getExpectProfit() {
		return expectProfit;
	}
	public void setExpectProfit(String expectProfit) {
		this.expectProfit = expectProfit;
	}
	public String getCreditorRightsId() {
		return creditorRightsId;
	}
	public void setCreditorRightsId(String creditorRightsId) {
		this.creditorRightsId = creditorRightsId;
	}
	public String getCycleCount() {
		return cycleCount;
	}
	public void setCycleCount(String cycleCount) {
		this.cycleCount = cycleCount;
	}
	public String getAnnualRate() {
		return annualRate;
	}
	public void setAnnualRate(String annualRate) {
		this.annualRate = annualRate;
	}
	public String getRepayMentMethod() {
		return repayMentMethod;
	}
	public void setRepayMentMethod(String repayMentMethod) {
		this.repayMentMethod = repayMentMethod;
	}
	public String getExpireDays() {
		return expireDays;
	}
	public void setExpireDays(String expireDays) {
		this.expireDays = expireDays;
	}

	 
}
