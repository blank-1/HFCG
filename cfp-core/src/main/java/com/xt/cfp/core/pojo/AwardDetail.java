package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ren yulin on 15-7-14.
 */
public class AwardDetail {
    private long awardDetailId;
    private long rightsRepaymentDetailId;
    private long lendOrderId;
    private long loanApplicationId;
    private BigDecimal awardBalance;
    private Date awardTime;
    private String awardPoint;
    private long userAccountId;
    private String awardType ;

    public String getAwardPoint() {
        return awardPoint;
    }

    public void setAwardPoint(String awardPoint) {
        this.awardPoint = awardPoint;
    }

    public long getAwardDetailId() {
        return awardDetailId;
    }

    public void setAwardDetailId(long awardDetailId) {
        this.awardDetailId = awardDetailId;
    }

    public long getRightsRepaymentDetailId() {
        return rightsRepaymentDetailId;
    }

    public void setRightsRepaymentDetailId(long rightsRepaymentDetailId) {
        this.rightsRepaymentDetailId = rightsRepaymentDetailId;
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

    public BigDecimal getAwardBalance() {
        return awardBalance;
    }

    public void setAwardBalance(BigDecimal awardBalance) {
        this.awardBalance = awardBalance;
    }

    public Date getAwardTime() {
        return awardTime;
    }

    public void setAwardTime(Date awardTime) {
        this.awardTime = awardTime;
    }

    public long getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(long userAccountId) {
        this.userAccountId = userAccountId;
    }

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType(String awardType) {
		this.awardType = awardType;
	}
    
}
