package com.xt.cfp.core.service;


import com.xt.cfp.core.pojo.DefaultInterestDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-4-21 上午11:25.
 */
public interface DefaultInterestDetailService {
    public List<DefaultInterestDetail> getInterestDetailsByLoanAppAndState(long repaymentPlanId, char repaymentState);

    public void modify(DefaultInterestDetail defaultInterestDetail);
    public void insert(DefaultInterestDetail defaultInterestDetail);
    
    public List<DefaultInterestDetail> findBy(Map parameters) throws Exception;

    /**
     * 获取用户的待还罚息
     * @param userId
     * @return
     */
    public BigDecimal getDefaultInterestByUserId(Long userId);

    /**
     * 根据还款计划id获得罚息总额
     * @param repaymentId
     * @return
     */
    public BigDecimal getDefaultInterestByRepaymentPlanId(Long repaymentId);
    /**
     * 根据还款计划id获得已还罚息
     * @param repaymentId
     * @return
     */
    public BigDecimal getDefaultInterestPaidByRepaymentPlanId(Long repaymentId);
}
