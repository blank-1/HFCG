package com.xt.cfp.core.pojo;

/**
 * Created by ren yulin on 15-8-11.
 */
public class LoanWithdrawRelations {
    private long relationsId;
    private long loanApplicationId;
    private long withdrawId;


    public long getRelationsId() {
        return relationsId;
    }

    public void setRelationsId(long relationsId) {
        this.relationsId = relationsId;
    }

    public long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public long getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(long withdrawId) {
        this.withdrawId = withdrawId;
    }
}
