package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.util.JsonUtil;
import com.xt.cfp.core.util.LoanCalculateUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yulei on 2015/9/22.
 */
public class LoanCalculateUtilsTest {

//    private BigDecimal annualRate;
//    private BigDecimal rate1;
//    private int period1;
//    private BigDecimal amount;
//    private LoanCalculateUtils.CycleType cycleType;
//    private Integer cycleValue;
//    private Integer periodNum;
//    private Date startDate;
//    private boolean ROUND_CEILING;
//
//
//    @Before
//    public void init() {
//        annualRate = new BigDecimal("0.12");
//        amount = new BigDecimal("10000");
//        cycleType = LoanCalculateUtils.CycleType.MONTH;
//        cycleValue = 1;
//        periodNum = 12;
//        startDate = new Date();
//        ROUND_CEILING = true;
//
//        rate1 = new BigDecimal("0.1");
//        period1 = 3;
//
//    }
//
//    @Test
//    public void testGetMonthRate() {
//        System.out.println("testGetMonthRate");
//        System.out.println(LoanCalculateUtils.getMonthRate(annualRate, BigDecimal.ROUND_CEILING).toPlainString());
//        assert true;
//    }
//
//    @Test
//    public void testGetPeriodRate() {
//        System.out.println("testGetPeriodRate");
//        System.out.println(LoanCalculateUtils.getPeriodRate(rate1, period1, BigDecimal.ROUND_CEILING).toPlainString());
//        assert true;
//    }
//
//    @Test
//    public void testGetPlanForAverageCapital() {
//        System.out.println("测试等额本金算法");
//        List<LoanCalculateUtils.RepaymentDetail> planForAverageCapital = LoanCalculateUtils.getPlanForAverageCapital(amount, cycleType, cycleValue, periodNum, annualRate, startDate, ROUND_CEILING);
//        System.out.println(JsonUtil.getGson(false).toJson(planForAverageCapital));
//        BigDecimal amount =BigDecimal.ZERO;
//        BigDecimal i =BigDecimal.ZERO;
//        BigDecimal p =BigDecimal.ZERO;
//        for (LoanCalculateUtils.RepaymentDetail repaymentDetail : planForAverageCapital) {
//            amount = amount.add(repaymentDetail.getAmount());
//            i = i.add(repaymentDetail.getInterest());
//            p = p.add(repaymentDetail.getCapital());
//        }
//
//        System.out.println("总数：" + amount.toPlainString() + "___利息：" + i.toPlainString() + "____本金：" + p.toPlainString());
//    }
//
//    @Test
//    public void testGetPlanForAverageInterest() {
//        System.out.println("测试等额本息算法");
//        List<LoanCalculateUtils.RepaymentDetail> planForAverageCapital = LoanCalculateUtils.getPlanForAverageInterest(amount, cycleType, cycleValue, periodNum, annualRate, startDate, ROUND_CEILING);
//        System.out.println(JsonUtil.getGson(false).toJson(planForAverageCapital));
//        BigDecimal amount =BigDecimal.ZERO;
//        BigDecimal i =BigDecimal.ZERO;
//        BigDecimal p =BigDecimal.ZERO;
//        for (LoanCalculateUtils.RepaymentDetail repaymentDetail : planForAverageCapital) {
//            amount = amount.add(repaymentDetail.getAmount());
//            i = i.add(repaymentDetail.getInterest());
//            p = p.add(repaymentDetail.getCapital());
//        }
//
//        System.out.println("总数：" + amount.toPlainString() + "___利息：" + i.toPlainString() + "____本金：" + p.toPlainString());
//    }
}
