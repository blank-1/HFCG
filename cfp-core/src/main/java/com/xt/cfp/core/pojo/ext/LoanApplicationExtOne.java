package com.xt.cfp.core.pojo.ext;

import java.math.BigDecimal;
import java.util.Date;

import com.xt.cfp.core.pojo.LoanApplication;

public class LoanApplicationExtOne extends LoanApplication {
	
	protected String userRealName;
	protected String idCard;
	protected String mobileNo;
	protected String creditLevel;
	protected String loanTitle;
	protected long isDelay;
	protected long haveCast;
	protected int cycleCounts;//周期数
	protected int cycleValue;//还款周期值
	private BigDecimal awardRate;//奖励利率
	 

	protected String agreementStatus ;//合同状态
	
	//辅助字段
    private String mainCode;//主编号
    private BigDecimal mainLoanBalance;//主借款总金额
    private String mainState;//主状态，(0.未发标；1.发标中；2.发标完成)
    private BigDecimal mainPublishBalance;//主已发标金额
    private String otype ;//定向用户
    private Date publishPreheatTime;//发标预热时间
    private Date publishOpenTime;//发标开标时间
    private String mainAutoPublish;//自动发标（0关闭的；1开启的）
    private String displayMainState ;//展示主状态(0.未发标；1.发标中；2.发标完成),如果有未开标的主状态为1
    private String durationTime;//期限
    private Date opertionDate;

    public String getAgreementStatus() {
		return agreementStatus;
	}

	public void setAgreementStatus(String agreementStatus) {
		this.agreementStatus = agreementStatus;
	}

	public String getLoanTitle() {
        return loanTitle;
    }

    public void setLoanTitle(String loanTitle) {
        this.loanTitle = loanTitle;
    }

    public long getIsDelay() {
		return isDelay;
	}
	public void setIsDelay(long isDelay) {
		this.isDelay = isDelay;
	}
	public long getHaveCast() {
		return haveCast;
	}
	public void setHaveCast(long haveCast) {
		this.haveCast = haveCast;
	}
	public String getUserRealName() {
		return userRealName;
	}
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getCreditLevel() {
		return creditLevel;
	}
	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}

	public int getCycleCounts() {
		return cycleCounts;
	}

	public void setCycleCounts(int cycleCounts) {
		this.cycleCounts = cycleCounts;
	}

	public int getCycleValue() {
		return cycleValue;
	}

	public void setCycleValue(int cycleValue) {
		this.cycleValue = cycleValue;
	}

	public String getMainCode() {
		return mainCode;
	}

	public void setMainCode(String mainCode) {
		this.mainCode = mainCode;
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
		this.mainState = mainState;
	}

	public BigDecimal getMainPublishBalance() {
		return mainPublishBalance;
	}

	public void setMainPublishBalance(BigDecimal mainPublishBalance) {
		this.mainPublishBalance = mainPublishBalance;
	}

	public String getOtype() {
		return otype;
	}

	public void setOtype(String otype) {
		this.otype = otype;
	}

	public Date getPublishPreheatTime() {
		return publishPreheatTime;
	}

	public void setPublishPreheatTime(Date publishPreheatTime) {
		this.publishPreheatTime = publishPreheatTime;
	}

	public Date getPublishOpenTime() {
		return publishOpenTime;
	}

	public void setPublishOpenTime(Date publishOpenTime) {
		this.publishOpenTime = publishOpenTime;
	}

	public BigDecimal getAwardRate() {
		return awardRate;
	}

	public void setAwardRate(BigDecimal awardRate) {
		this.awardRate = awardRate;
	}

	public String getMainAutoPublish() {
		return mainAutoPublish;
	}

	public void setMainAutoPublish(String mainAutoPublish) {
		this.mainAutoPublish = mainAutoPublish;
	}

	public String getDisplayMainState() {
		return displayMainState;
	}

	public void setDisplayMainState(String displayMainState) {
		this.displayMainState = displayMainState;
	}

	public String getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(String durationTime) {
		this.durationTime = durationTime;
	}

	public Date getOpertionDate() {
		return opertionDate;
	}

	public void setOpertionDate(Date opertionDate) {
		this.opertionDate = opertionDate;
	}

}
