package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.RepaymentPlan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ren yulin on 15-7-20.
 */
public class RepaymentPlanVO extends RepaymentPlan {

	private BigDecimal shouldFees;
    private BigDecimal factFees;
    private BigDecimal shouldDefaultInterest;
    private BigDecimal faceDefaultInterest;
    private String loanApplicationCode;
    private String loanApplicationName;
    private String userRealName;
    private String loginName;
    private String loanTitle;
    private int sectionCode;
    
    private Date nextRepaymentDay;//下一次还款日期
    private Integer sumSectionCode;//总的还款期数
    private Integer nowSectionCode;//当前所在还款期数

    public int getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(int sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getLoanTitle() {
        return loanTitle;
    }

    public void setLoanTitle(String loanTitle) {
        this.loanTitle = loanTitle;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public BigDecimal getShouldFees() {
        return shouldFees;
    }

    public void setShouldFees(BigDecimal shouldFees) {
        this.shouldFees = shouldFees;
    }

    public BigDecimal getFactFees() {
        return factFees;
    }

    public void setFactFees(BigDecimal factFees) {
        this.factFees = factFees;
    }

    public BigDecimal getShouldDefaultInterest() {
        return shouldDefaultInterest;
    }

    public void setShouldDefaultInterest(BigDecimal shouldDefaultInterest) {
        this.shouldDefaultInterest = shouldDefaultInterest;
    }

    public BigDecimal getFaceDefaultInterest() {
        return faceDefaultInterest;
    }

    public void setFaceDefaultInterest(BigDecimal faceDefaultInterest) {
        this.faceDefaultInterest = faceDefaultInterest;
    }

	public Date getNextRepaymentDay() {
		return nextRepaymentDay;
	}

	public void setNextRepaymentDay(Date nextRepaymentDay) {
		this.nextRepaymentDay = nextRepaymentDay;
	}

	public Integer getSumSectionCode() {
		return sumSectionCode;
	}

	public void setSumSectionCode(Integer sumSectionCode) {
		this.sumSectionCode = sumSectionCode;
	}

	public Integer getNowSectionCode() {
		return nowSectionCode;
	}

	public void setNowSectionCode(Integer nowSectionCode) {
		this.nowSectionCode = nowSectionCode;
	}
    
}
