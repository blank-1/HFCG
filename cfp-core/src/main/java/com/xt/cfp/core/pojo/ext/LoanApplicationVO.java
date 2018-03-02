package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.constants.LoanApplicationStateEnum;
import com.xt.cfp.core.constants.LoanTypeEnum;
import com.xt.cfp.core.pojo.LoanApplication;

import java.math.BigDecimal;

/**
 * Created by ren yulin on 15-7-3.
 */
public class LoanApplicationVO extends LoanApplication {
    private BigDecimal rightsBalance;

    private String loginName;

    private String realName;

    private String title;

    private String loanTypeStr;

    private String loanlimit;

    private String currentRepaymentCount;

    private BigDecimal shouldCapital = BigDecimal.ZERO;
    private BigDecimal shouldInterest = BigDecimal.ZERO;
    private BigDecimal shouldFee = BigDecimal.ZERO;
    private BigDecimal shouldFaxi = BigDecimal.ZERO;
    private BigDecimal shouldAll = BigDecimal.ZERO;

    private String bondUserLoginName;
    private String bondUserRealName;
    private String statusStr;

    public String getBondUserLoginName() {
        return bondUserLoginName;
    }

    public void setBondUserLoginName(String bondUserLoginName) {
        this.bondUserLoginName = bondUserLoginName;
    }

    public String getBondUserRealName() {
        return bondUserRealName;
    }

    public void setBondUserRealName(String bondUserRealName) {
        this.bondUserRealName = bondUserRealName;
    }

    public String getStatusStr() {
        LoanApplicationStateEnum byValue = LoanApplicationStateEnum.getByValue(this.getApplicationState());
        if (byValue!=null)
            return byValue.getDesc();
        return null;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public BigDecimal getShouldAll() {
        return shouldAll;
    }

    public void setShouldAll(BigDecimal shouldAll) {
        this.shouldAll = shouldAll;
    }

    public BigDecimal getShouldCapital() {
        return shouldCapital;
    }

    public void setShouldCapital(BigDecimal shouldCapital) {
        this.shouldCapital = shouldCapital;
    }

    public BigDecimal getShouldInterest() {
        return shouldInterest;
    }

    public void setShouldInterest(BigDecimal shouldInterest) {
        this.shouldInterest = shouldInterest;
    }

    public BigDecimal getShouldFee() {
        return shouldFee;
    }

    public void setShouldFee(BigDecimal shouldFee) {
        this.shouldFee = shouldFee;
    }

    public BigDecimal getShouldFaxi() {
        return shouldFaxi;
    }

    public void setShouldFaxi(BigDecimal shouldFaxi) {
        this.shouldFaxi = shouldFaxi;
    }

    public String getCurrentRepaymentCount() {
        return currentRepaymentCount;
    }

    public void setCurrentRepaymentCount(String currentRepaymentCount) {
        this.currentRepaymentCount = currentRepaymentCount;
    }

    public String getLoanTypeStr() {
        LoanTypeEnum lt = LoanTypeEnum.getLoanTypeEnumByCode(this.getLoanType());
        if (lt!=null)
            return lt.getDesc();
        return null;
    }

    public void setLoanTypeStr(String loanTypeStr) {
        this.loanTypeStr = loanTypeStr;
    }

    public String getLoanlimit() {
        return loanlimit;
    }

    public void setLoanlimit(String loanlimit) {
        this.loanlimit = loanlimit;
    }

    public BigDecimal getRightsBalance() {
        return rightsBalance;
    }

    public void setRightsBalance(BigDecimal rightsBalance) {
        this.rightsBalance = rightsBalance;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
