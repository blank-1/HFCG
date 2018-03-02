package com.xt.cfp.core.util;

import com.xt.cfp.core.constants.LendOrderReceiveStateEnum;
import com.xt.cfp.core.constants.LendProductClosingType;
import com.xt.cfp.core.constants.LendProductInterestReturnType;
import com.xt.cfp.core.constants.LendProductTimeLimitType;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendOrderReceive;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by yulei on 2015/8/7 0007.
 */
public final class OrderCalculateUtils {


    /**
     * 计算并设定理财订单的合同结束日期，前提是理财订单的合同开始时间不为空
     * @param financeOrder
     */
    public final static Date calAndSetAgreementEndDate(LendOrder financeOrder) {
        ValidationUtil.checkRequiredField(financeOrder, "agreementStartDate", "timeLimitType", "timeLimit");

        if (financeOrder.getTimeLimitType().equals(LendProductTimeLimitType.TIMELIMITTYPE_DAY.getValue())) {
            financeOrder.setAgreementEndDate(DateUtil.addDate(financeOrder.getAgreementStartDate(), Calendar.DATE, financeOrder.getTimeLimit()));
        } else if (financeOrder.getTimeLimitType().equals(LendProductTimeLimitType.TIMELIMITTYPE_MONTH.getValue())) {
            financeOrder.setAgreementEndDate(DateUtil.addDate(financeOrder.getAgreementEndDate(), Calendar.MONTH, financeOrder.getTimeLimit()));
        }

        return financeOrder.getAgreementEndDate();
    }

    /**
     * 计算并设定理财订单的封闭到期日期，前提是理财订单的合同开始时间不为空
     * @param financeOrder
     * @return
     */
    public final static Date calAndSetClosingOverDate(LendOrder financeOrder) {
        ValidationUtil.checkRequiredField(financeOrder, "agreementStartDate", "closingType", "closingDate");

        if (financeOrder.getClosingType().equals(LendProductClosingType.CLOSINGTYPE_DAY.getValue())) {
            financeOrder.setClosingOverDate(DateUtil.addDate(financeOrder.getAgreementStartDate(), Calendar.DATE, financeOrder.getClosingDate()));
        } else if (financeOrder.getClosingType().equals(LendProductClosingType.CLOSINGTYPE_MONTH.getValue())) {
            financeOrder.setClosingOverDate(DateUtil.addDate(financeOrder.getAgreementStartDate(), Calendar.MONTH, financeOrder.getClosingDate()));
        }

        return financeOrder.getClosingOverDate();
    }

    private static Map<Integer, Map<String, BigDecimal>> getMonthInfoByAverageCapitalPlusInterest(BigDecimal cash, BigDecimal rate, int months) {
        Map map = new HashMap();

        BigDecimal monthValue = getMonthAverageCapitalPlusInterest(cash, rate, months);
        BigDecimal monthRate = rate.divide(BigDecimal.valueOf(12), 18, BigDecimal.ROUND_DOWN);
        for (int month = 0; month < months; month++) {
            //计算本月利息
            BigDecimal interest = cash.multiply(monthRate);
            //每月还款额减每个月的利息，即该月的本金
            BigDecimal theMonthCash = monthValue.subtract(interest);

            Map monthMap = new HashMap();
            monthMap.put("balance", monthValue);//当月还款总额
            monthMap.put("interest", interest);//当月利息
            monthMap.put("calital", theMonthCash);//当月本金
            map.put(month + 1, monthMap);
            cash = cash.subtract(theMonthCash);
        }
        return map;
    }

    private static BigDecimal getMonthAverageCapitalPlusInterest(BigDecimal cash, BigDecimal rate, int months) {
        BigDecimal trate, monthRepay;
        BigDecimal monthRate = rate.divide(BigDecimal.valueOf(12), 18, BigDecimal.ROUND_DOWN);
        trate = monthRate.add(BigDecimal.valueOf(1));
        for (int i = 0; i < months - 1; i++) {
            trate = trate.multiply(monthRate.add(BigDecimal.valueOf(1)));
        }
        BigDecimal divisor = cash.multiply(trate).multiply(monthRate);
        BigDecimal dividend = trate.subtract(BigDecimal.valueOf(1));
        monthRepay = divisor.divide(dividend, 18, BigDecimal.ROUND_DOWN);
        return monthRepay;
    }

    /**
     * 获取月利率
     * @param rate
     * @return
     */
    private static BigDecimal getMonthRate(BigDecimal rate) {
        return rate.divide(new BigDecimal("12"), 18, BigDecimal.ROUND_DOWN);
    }

    /**
     * 获取自定义周期利率
     * @param rate
     * @param cycleNum
     * @return
     */
    private static BigDecimal getCycleRate(BigDecimal rate, int cycleNum) {
        return rate.divide(new BigDecimal(String.valueOf(cycleNum)), 18, BigDecimal.ROUND_DOWN);
    }

    /**
     * 获取日利息
     * @param rate
     * @return
     */
    private static BigDecimal getDayRate(BigDecimal rate) {
        return rate.divide(new BigDecimal("365"), 18, BigDecimal.ROUND_DOWN);
    }

    public final static List<LendOrderReceive> generateReceiveList(LendOrder financeOrder) {
        List<LendOrderReceive> lendOrderReceives = new ArrayList<LendOrderReceive>();
        String toInterestPoint = financeOrder.getToInterestPoint();
        int days = DateUtil.daysBetweenDates(financeOrder.getAgreementEndDate(), financeOrder.getAgreementStartDate());
        BigDecimal dayRate = getDayRate(financeOrder.getProfitRate());

        //到期还本付息
        if (toInterestPoint.equals(LendProductInterestReturnType.EXIT.getValue())) {
            LendOrderReceive lendOrderReceive = new LendOrderReceive();
            lendOrderReceive.setLendOrderId(financeOrder.getLendOrderId());
            lendOrderReceive.setSectionCode(1);
            lendOrderReceive.setShouldCapital(financeOrder.getBuyBalance());
            lendOrderReceive.setShouldCapital2(financeOrder.getBuyBalance().setScale(2, BigDecimal.ROUND_DOWN));
            lendOrderReceive.setShouldInterest(financeOrder.getBuyBalance().multiply(dayRate).multiply(new BigDecimal(String.valueOf(days))));
            lendOrderReceive.setShouldInterest2(financeOrder.getBuyBalance().multiply(dayRate).multiply(new BigDecimal(String.valueOf(days))).setScale(2, BigDecimal.ROUND_DOWN));
            lendOrderReceive.setReceiveDate(financeOrder.getAgreementEndDate());
            lendOrderReceive.setFactCapital(BigDecimal.ZERO);
            lendOrderReceive.setFactInterest(BigDecimal.ZERO);
            lendOrderReceive.setReceiveState(LendOrderReceiveStateEnum.UNRECEIVE.getValue().charAt(0));
            lendOrderReceive.setReceiveBalance(BigDecimal.ZERO);
            lendOrderReceives.add(lendOrderReceive);
        }

        //按周还息，到期还本
        if (toInterestPoint.equals(LendProductInterestReturnType.WEEK.getValue())) {
            ArrayList<LendOrderReceive> receives = getLendOrderReceivesForFixCycle(financeOrder, 7);
            lendOrderReceives.addAll(receives);
        }

        //按月还息，到期还本
        if (toInterestPoint.equals(LendProductInterestReturnType.MONTH.getValue())) {
            ArrayList<LendOrderReceive> receives = getLendOrderReceivesForFixCycle(financeOrder, 30);
            lendOrderReceives.addAll(receives);
        }

        //按季度还息，到期还本
        if (toInterestPoint.equals(LendProductInterestReturnType.SEASON.getValue())) {
            ArrayList<LendOrderReceive> receives = getLendOrderReceivesForFixCycle(financeOrder, 90);
            lendOrderReceives.addAll(receives);
        }

        //按半年还息，到期还本
        if (toInterestPoint.equals(LendProductInterestReturnType.HALF_YEAR.getValue())) {
            ArrayList<LendOrderReceive> receives = getLendOrderReceivesForFixCycle(financeOrder, 180);
            lendOrderReceives.addAll(receives);
        }

        //按年还息，到期还本
        if (toInterestPoint.equals(LendProductInterestReturnType.YEAR.getValue())) {
            ArrayList<LendOrderReceive> receives = getLendOrderReceivesForFixCycle(financeOrder, 365);
            lendOrderReceives.addAll(receives);
        }

        return lendOrderReceives;
    }

    public static void main(String[] args) {
        LendOrder fo = new LendOrder();
        fo.setBuyBalance(new BigDecimal("10000"));
        Date date = new Date();
        Date endDate = DateUtil.addDate(date, Calendar.DATE, 180);
        fo.setAgreementStartDate(date);
        fo.setAgreementEndDate(endDate);
        fo.setLendOrderId(99l);
        fo.setProfitRate(new BigDecimal("0.12"));
        BigDecimal dayRate = getDayRate(fo.getProfitRate());
        ArrayList<LendOrderReceive> lendOrderReceivesForFixCycle = getLendOrderReceivesForFixCycle(fo, 30);
        System.out.println(JsonUtil.getGson(false).toJson(lendOrderReceivesForFixCycle));
        System.out.println("---------------------------------------------------------------------------\r\n\r\n");
//        ArrayList<LendOrderReceive> lendOrderReceivesForFixCycle1 = getLendOrderReceivesForFixCycle(fo, 365, dayRate, 7);
//        System.out.println(lendOrderReceivesForFixCycle1);
//        System.out.println("---------------------------------------------------------------------------\r\n\r\n");
        ArrayList<LendOrderReceive> lendOrderReceivesForFixCycle2 = getLendOrderReceivesForFixCycle(fo, 90);
        System.out.println(JsonUtil.getGson(false).toJson(lendOrderReceivesForFixCycle2));
        System.out.println("---------------------------------------------------------------------------\r\n\r\n");
        ArrayList<LendOrderReceive> lendOrderReceivesForFixCycle3 = getLendOrderReceivesForFixCycle(fo, 180);
        System.out.println(JsonUtil.getGson(false).toJson(lendOrderReceivesForFixCycle3));
        System.out.println("---------------------------------------------------------------------------\r\n\r\n");
    }

    public static ArrayList<LendOrderReceive> getLendOrderReceivesForFixCycle(LendOrder financeOrder, int cycleNums) {
        ArrayList<LendOrderReceive> receives = new ArrayList<LendOrderReceive>();
        int days = DateUtil.daysBetweenDates(financeOrder.getAgreementEndDate(), financeOrder.getAgreementStartDate());
        BigDecimal dayRate = getDayRate(financeOrder.getProfitRate());
        BigDecimal daysB = new BigDecimal(String.valueOf(days));
        BigDecimal cycleDays = new BigDecimal(String.valueOf(cycleNums));
        BigDecimal[] bigDecimals = daysB.divideAndRemainder(cycleDays);
        BigDecimal cycleInterest = dayRate.multiply(cycleDays).multiply(financeOrder.getBuyBalance()).setScale(2, BigDecimal.ROUND_DOWN);
        BigDecimal dayInterest = dayRate.multiply(financeOrder.getBuyBalance()).setScale(2, BigDecimal.ROUND_DOWN);
        Date currentEndingDate = financeOrder.getAgreementStartDate();
        for (int i = 1; i <= bigDecimals[0].intValue(); i++) {
            currentEndingDate = DateUtil.addDate(currentEndingDate, Calendar.DATE, cycleNums);
            LendOrderReceive lendOrderReceive = new LendOrderReceive();
            lendOrderReceive.setLendOrderId(financeOrder.getLendOrderId());
            lendOrderReceive.setSectionCode(i);
            lendOrderReceive.setShouldCapital(BigDecimal.ZERO);
            lendOrderReceive.setShouldCapital2(BigDecimal.ZERO);
            lendOrderReceive.setShouldInterest(cycleInterest);
            lendOrderReceive.setShouldInterest2(cycleInterest);
            lendOrderReceive.setReceiveDate(currentEndingDate);
            lendOrderReceive.setFactCapital(BigDecimal.ZERO);
            lendOrderReceive.setFactInterest(BigDecimal.ZERO);
            lendOrderReceive.setReceiveState(LendOrderReceiveStateEnum.UNRECEIVE.getValue().charAt(0));
            lendOrderReceive.setReceiveBalance(BigDecimal.ZERO);
            if ((bigDecimals[1] == null || bigDecimals[1].compareTo(BigDecimal.ZERO) == 0) && i == bigDecimals[0].intValue()) {
                lendOrderReceive.setShouldCapital(financeOrder.getBuyBalance());
                lendOrderReceive.setShouldCapital2(financeOrder.getBuyBalance());
            }
            receives.add(lendOrderReceive);
        }

        if (bigDecimals[1] != null && bigDecimals[1].compareTo(BigDecimal.ZERO) > 0) {
            currentEndingDate = DateUtil.addDate(currentEndingDate, Calendar.DATE, bigDecimals[1].intValue());
            LendOrderReceive lendOrderReceive = new LendOrderReceive();
            lendOrderReceive.setLendOrderId(financeOrder.getLendOrderId());
            lendOrderReceive.setSectionCode(bigDecimals[0].intValue() + 1);
            lendOrderReceive.setShouldCapital(financeOrder.getBuyBalance());
            lendOrderReceive.setShouldCapital2(financeOrder.getBuyBalance());
            lendOrderReceive.setShouldInterest(dayInterest.multiply(bigDecimals[1]).setScale(2, BigDecimal.ROUND_DOWN));
            lendOrderReceive.setShouldInterest2(dayInterest.multiply(bigDecimals[1]).setScale(2, BigDecimal.ROUND_DOWN));
            lendOrderReceive.setReceiveDate(currentEndingDate);
            lendOrderReceive.setFactCapital(BigDecimal.ZERO);
            lendOrderReceive.setFactInterest(BigDecimal.ZERO);
            lendOrderReceive.setReceiveState(LendOrderReceiveStateEnum.UNRECEIVE.getValue().charAt(0));
            lendOrderReceive.setReceiveBalance(BigDecimal.ZERO);
            receives.add(lendOrderReceive);
        }
        return receives;
    }

    //等额本金还款计划
    public void getPlanForAverageCapital(BigDecimal amount, CycleType cycleType, Integer cycleValue, BigDecimal annualRate, Date startDate) {

        if (cycleType == CycleType.MONTH) {

        }
    }

    public enum RepaymentMethod {
        //等额本息
        AVERAGE_INTEREST_CAPITAL,
        //等额本金
        AVERAGE_CAPITAL,
        ;
    }

    public enum CycleType {
        //按月
        MONTH,
        //按日
        DAY,
        //按周
        WEEK,
        //按季度
        SEASON,
        //按半年
        HALF_YEAR,
        ;
    }

    public static class RepaymentDetail {
        private BigDecimal capital;
        private BigDecimal interest;
        private BigDecimal penalty;
        private BigDecimal cycleRate;
        private int period;
        private Date start;
        private Date end;

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }

        public BigDecimal getCapital() {
            return capital;
        }

        public void setCapital(BigDecimal capital) {
            this.capital = capital;
        }

        public BigDecimal getInterest() {
            return interest;
        }

        public void setInterest(BigDecimal interest) {
            this.interest = interest;
        }

        public BigDecimal getPenalty() {
            return penalty;
        }

        public void setPenalty(BigDecimal penalty) {
            this.penalty = penalty;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public BigDecimal getCycleRate() {
            return cycleRate;
        }

        public void setCycleRate(BigDecimal cycleRate) {
            this.cycleRate = cycleRate;
        }
    }
}
