package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class MainLoanPublish {
    private Long mainLoanApplicationId;
    private Long companyId;
    private String loanTitle;
    private String guaranteeChannel;
    private String creditLevel;
    private BigDecimal awardRate;
    private String awardPoint;
    private String guaranteeType;
    private String desc;
    private String overduePayPoint;
    private String publishTarget;
    private Date preheatTime;
    private Date openTime;
    private Date publishTime;
    private Integer bidingDays;
    private BigDecimal maxBuyBalance;
    private String authInfos;
    private Long hourseAddress;
    private Double hourseSize;
    private Double assessValue;
    private Double marketValue;
    private String hourseDesc;
    private String loanUseageDesc;//借款用途描述
    private String oType ;//定向用户
    
    //辅助字段
    private String thisPublishTitle;//本次发标标题
    private String thisPublishBalance;//本次发标金额
    private String publishRule;//发布规则

    public Long getMainLoanApplicationId() {
        return mainLoanApplicationId;
    }

    public void setMainLoanApplicationId(Long mainLoanApplicationId) {
        this.mainLoanApplicationId = mainLoanApplicationId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getLoanTitle() {
        return loanTitle;
    }

    public void setLoanTitle(String loanTitle) {
        this.loanTitle = loanTitle == null ? null : loanTitle.trim();
    }

    public String getGuaranteeChannel() {
        return guaranteeChannel;
    }

    public void setGuaranteeChannel(String guaranteeChannel) {
        this.guaranteeChannel = guaranteeChannel == null ? null : guaranteeChannel.trim();
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel == null ? null : creditLevel.trim();
    }

    public BigDecimal getAwardRate() {
        return awardRate;
    }

    public void setAwardRate(BigDecimal awardRate) {
        this.awardRate = awardRate;
    }

    public String getAwardPoint() {
        return awardPoint;
    }

    public void setAwardPoint(String awardPoint) {
        this.awardPoint = awardPoint == null ? null : awardPoint.trim();
    }

    public String getGuaranteeType() {
        return guaranteeType;
    }

    public void setGuaranteeType(String guaranteeType) {
        this.guaranteeType = guaranteeType == null ? null : guaranteeType.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public String getOverduePayPoint() {
        return overduePayPoint;
    }

    public void setOverduePayPoint(String overduePayPoint) {
        this.overduePayPoint = overduePayPoint == null ? null : overduePayPoint.trim();
    }

    public String getPublishTarget() {
        return publishTarget;
    }

    public void setPublishTarget(String publishTarget) {
        this.publishTarget = publishTarget == null ? null : publishTarget.trim();
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

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getBidingDays() {
        return bidingDays;
    }

    public void setBidingDays(Integer bidingDays) {
        this.bidingDays = bidingDays;
    }

    public BigDecimal getMaxBuyBalance() {
        return maxBuyBalance;
    }

    public void setMaxBuyBalance(BigDecimal maxBuyBalance) {
        this.maxBuyBalance = maxBuyBalance;
    }

    public String getAuthInfos() {
        return authInfos;
    }

    public void setAuthInfos(String authInfos) {
        this.authInfos = authInfos == null ? null : authInfos.trim();
    }

    public Long getHourseAddress() {
        return hourseAddress;
    }

    public void setHourseAddress(Long hourseAddress) {
        this.hourseAddress = hourseAddress;
    }

    public Double getHourseSize() {
        return hourseSize;
    }

    public void setHourseSize(Double hourseSize) {
        this.hourseSize = hourseSize;
    }

    public Double getAssessValue() {
        return assessValue;
    }

    public void setAssessValue(Double assessValue) {
        this.assessValue = assessValue;
    }

    public Double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
    }

    public String getHourseDesc() {
        return hourseDesc;
    }

    public void setHourseDesc(String hourseDesc) {
        this.hourseDesc = hourseDesc == null ? null : hourseDesc.trim();
    }
    
	public String getLoanUseageDesc() {
		return loanUseageDesc;
	}

	public void setLoanUseageDesc(String loanUseageDesc) {
		this.loanUseageDesc = loanUseageDesc;
	}

	public String getThisPublishTitle() {
		return thisPublishTitle;
	}

	public void setThisPublishTitle(String thisPublishTitle) {
		this.thisPublishTitle = thisPublishTitle;
	}

	public String getThisPublishBalance() {
		return thisPublishBalance;
	}

	public void setThisPublishBalance(String thisPublishBalance) {
		this.thisPublishBalance = thisPublishBalance;
	}

	public String getoType() {
		return oType;
	}

	public void setoType(String oType) {
		this.oType = oType;
	}
	
	public String getOType() {
		return oType;
	}

	public void setOType(String oType) {
		this.oType = oType;
	}

	public String getPublishRule() {
		return publishRule;
	}

	public void setPublishRule(String publishRule) {
		this.publishRule = publishRule;
	}
	
}