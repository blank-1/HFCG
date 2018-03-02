package com.xt.cfp.core.service.financePlan;

import com.xt.cfp.core.constants.FinancePlanConstants;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.service.matchrules.MatchCreditorVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lenovo on 2015/12/8.
 */
public interface FinancePlanService {

    /**
     * 自动匹配省心计划
     * @throws Exception
     */
    void creditorRightsAutoMatch();
    /**
     * 批量匹配
     */
    public void match(List<LendOrder> lendOrderList) throws Exception;
    /**
     * 单个匹配
     */
    public void match(LendOrder lendOrder) throws Exception;

    /**
     * 周期返息
     */
    public void cycleCoupon();

    /**
     * 退出省心计划
     * @param lendOrderList
     */
    public void quitPlan(List<LendOrder> lendOrderList);
    
    /**
     * 省心计划到期结算 LendOrder
     * */
	void financePaymentBalance(LendOrder lendOrder);
    /**
     * 省心计划到期结算 List<LendOrder>
     * */
	void financePaymentBalance(List<LendOrder> lendOrderList);
    /**
     * 省心计划到期结算 timer
     * */	
	void financePaymentBalanceTask();

}
