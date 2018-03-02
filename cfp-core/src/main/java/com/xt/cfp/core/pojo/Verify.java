package com.xt.cfp.core.pojo;

import java.util.Date;

public class Verify {
    private Long id;

    private Long loanApplicationId;

    private Long userId;

    private String result;

    private String verifySuggestion;

    private Date verifyTime;

    private String verifyType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    public String getVerifySuggestion() {
        return verifySuggestion;
    }

    public void setVerifySuggestion(String verifySuggestion) {
        this.verifySuggestion = verifySuggestion == null ? null : verifySuggestion.trim();
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType == null ? null : verifyType.trim();
    }
}