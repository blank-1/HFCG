package com.xt.cfp.core.pojo.ext;

/**
 * Created by yulei on 2015/7/9.
 */
public class PayResult {

    private boolean payResult;
    private String failDesc;
    private Long payId;
    private Long lendOrderId;
    private Long loanAppId;
    private boolean processResult;

    public PayResult(Long payId) {
        this.payId = payId;
        this.payResult = false;
        this.processResult = false;
    }

    public Long getLendOrderId() {
		return lendOrderId;
	}

	public void setLendOrderId(Long lendOrderId) {
		this.lendOrderId = lendOrderId;
	}

	public boolean isPayResult() {
        return payResult;
    }

    public void setPayResult(boolean payResult) {
        this.payResult = payResult;
    }

    public String getFailDesc() {
        return failDesc;
    }

    public void setFailDesc(String failDesc) {
        this.failDesc = failDesc;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public boolean isProcessResult() {
        return processResult;
    }

    public void setProcessResult(boolean processResult) {
        this.processResult = processResult;
    }

    public Long getLoanAppId() {
        return loanAppId;
    }

    public void setLoanAppId(Long loanAppId) {
        this.loanAppId = loanAppId;
    }
}
