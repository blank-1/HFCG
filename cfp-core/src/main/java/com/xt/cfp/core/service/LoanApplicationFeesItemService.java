package com.xt.cfp.core.service;

import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.pojo.LoanApplicationFeesItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ren yulin on 15-6-24.
 */
public interface LoanApplicationFeesItemService {
    /**
     * 计算费用
     * @param applicationFeesItem
     * @param allCalital
     * @param allInterest
     * @param currentCalital
     * @param currentInterest
     * @param allProfit
     * @param currentProfit
     * @return
     */
    public BigDecimal calculateLoanApplicationFeesBalance(LoanApplicationFeesItem applicationFeesItem, BigDecimal allCalital, BigDecimal allInterest,
                                                          BigDecimal currentCalital, BigDecimal currentInterest, BigDecimal allProfit, BigDecimal currentProfit);

    public List<LoanApplicationFeesItem> getByLoanApplicationId(long loanApplicationId);

    public List<LoanApplicationFeesItem> getByLoanApplicationIdAndFeePoint(long loanApplicationId,FeesPointEnum feesPointEnum);

    /**
     * 添加借款申请费用
     */
    LoanApplicationFeesItem addLoanApplicationFeesItem(LoanApplicationFeesItem loanApplicationFeesItem);
    
    /**
     * 修改借款申请费用
     */
    LoanApplicationFeesItem updateLoanApplicationFeesItem(LoanApplicationFeesItem loanApplicationFeesItem);
    
    /**
     * 根据ID加载一条数据
     * @param loanApplicationFeesItemId 借款申请费用ID
     */
    LoanApplicationFeesItem getLoanApplicationFeesItemById(Long loanApplicationFeesItemId);

    void insert(LoanApplicationFeesItem applicationFeesItem);
}
