package com.xt.cfp.core.service;

import com.xt.cfp.core.pojo.LoanFeesDetail;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by ren yulin on 15-6-25.
 */
public interface LoanFeesDetailService  {
    void insert(LoanFeesDetail loanFeesDetail);

    List<LoanFeesDetail> getFeesItemBy(Map feesItemsParameters);

    void update(Map loanFeesDetailMap);


    /**
     * 添加借款申请费用明细
     */
    LoanFeesDetail addLoanFeesDetail(LoanFeesDetail loanFeesDetail);
    
    /**
     * 修改借款申请费用明细
     */
    LoanFeesDetail updateLoanFeesDetail(LoanFeesDetail loanFeesDetail);
    
    /**
     * 根据ID记载一条数据
     * @param loanFeesDetailId 借款申请费用明细ID
     */
    LoanFeesDetail getLoanFeesDetailById(Long loanFeesDetailId);

    List<LoanFeesDetail> getLoanFeesDetailByLoanId(long loanAppliationId);

    /**
     * 获取用户待缴费用（净资产）
     * @param userId
     * @return
     */
    BigDecimal getLoanFeeNoPaied(Long userId);
    
    /**
     * 获取用户待缴费用（后台账户预览）
     * @param userId
     * @return
     */
    BigDecimal getUserLoanFeeNoPaied(Long userId);
    
}
