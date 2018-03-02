package com.xt.cfp.app.vo;

public class APPMyTurnCreditRightListVO {
	private String creditorRightsId;//ID
	private String loanApplicationTitle;//借款标题
	private String cycleCount;//借款期限
	private String buyPrice;//转出资金
	private String expectProfit;//实际收益
	private String rightsState;//状态
	private String rightsStateDisplay;//状态中文
	private String fromWhere;//来源
	private boolean rightShow;//该债权转让记录是否显示
	private String creditorRightsApplyId;//申请债券转让ID
	
	public String getCreditorRightsId() {
		return creditorRightsId;
	}
	public void setCreditorRightsId(String creditorRightsId) {
		this.creditorRightsId = creditorRightsId;
	}
	public String getLoanApplicationTitle() {
		return loanApplicationTitle;
	}
	public void setLoanApplicationTitle(String loanApplicationTitle) {
		this.loanApplicationTitle = loanApplicationTitle;
	}
	public String getCycleCount() {
		return cycleCount;
	}
	public void setCycleCount(String cycleCount) {
		this.cycleCount = cycleCount;
	}
	public String getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}
	public String getExpectProfit() {
		return expectProfit;
	}
	public void setExpectProfit(String expectProfit) {
		this.expectProfit = expectProfit;
	}
	public String getRightsState() {
		return rightsState;
	}
	public void setRightsState(String rightsState) {
		this.rightsState = rightsState;
	}
	public String getRightsStateDisplay() {
		return rightsStateDisplay;
	}
	public void setRightsStateDisplay(String rightsStateDisplay) {
		this.rightsStateDisplay = rightsStateDisplay;
	}
	public String getFromWhere() {
		return fromWhere;
	}
	public void setFromWhere(String fromWhere) {
		this.fromWhere = fromWhere;
	}
	public boolean isRightShow() {
		return rightShow;
	}
	public void setRightShow(boolean rightShow) {
		this.rightShow = rightShow;
	}
	public String getCreditorRightsApplyId() {
		return creditorRightsApplyId;
	}
	public void setCreditorRightsApplyId(String creditorRightsApplyId) {
		this.creditorRightsApplyId = creditorRightsApplyId;
	}
	
}
