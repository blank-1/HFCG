package com.xt.cfp.core.util;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Renyulin on 14-3-28 下午4:57.
 */
public class InterestCalculation {
    public static int daysInYear = 365;
    public final static int MONTH = 12;

    /**
     * 期限类型：1为天，2为月
     */
    public static final char TIMELIMITTYPE_MONTH = '2';
    public static final char TIMELIMITTYPE_DAY = '1';

    /**
     * 还款周期类型：1为按月，2为到期，3为按天
     */
    public static final char REPAYMENTCYCLE_MONTH = '1';
    public static final char REPAYMENTCYCLE_ONCE = '2';
    public static final char REPAYMENTCYCLE_DAY = '3';

    /**
     * 还款方法：1为等额本息，2为等额本金
     */
    public static final char REPAYMENTMETHOD_AVERAGECAPITALPLUSINTEREST = '1';
    public static final char REPAYMENTMETHOD_AVERAGECAPITAL = '2';

    /**
     * 还款方式：1为周期还本息，2为周期还利息,到期还本金，3为周期还本金,到期还利息
     */
    public static final char REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_AND_INTEREST = '1';
    public static final char REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL = '2';
    public static final char REPAYMENT_TYPE_CYCLE_OF_PRINCIPAL_INTEREST_DUE = '3';

    /**
     * 按天数，到期还款
     *
     * @param cash
     * @param rate
     * @return
     */
    public static Map<Integer, Map<String, BigDecimal>> getDaysInfoOnce(BigDecimal cash, BigDecimal rate, int days) {
        Map map = new HashMap();
        BigDecimal dayInterest = cash.multiply(rate).divide(new BigDecimal(String.valueOf(getHowManyDayInAYear())), 18, BigDecimal.ROUND_UP);
        Map dayMap = new HashMap();
        BigDecimal interest = dayInterest.multiply(new BigDecimal(String.valueOf(days)));
        dayMap.put("calital", cash);
        dayMap.put("interest", interest);
        dayMap.put("balance", cash.add(interest));

        map.put(1, dayMap);
        return map;
    }

    /**
     * 按月，到期还款
     *
     * @param cash
     * @param rate
     * @return
     */
    public static Map<Integer, Map<String, BigDecimal>> getMonthInfoOnce(BigDecimal cash, BigDecimal rate, int months) {
        Map map = new HashMap();
        BigDecimal monthInterest = cash.multiply(rate).divide(new BigDecimal("12"), 18, BigDecimal.ROUND_UP);
        Map monthMap = new HashMap();
        BigDecimal interest = monthInterest.multiply(new BigDecimal(String.valueOf(months)));
        monthMap.put("calital", cash);
        monthMap.put("interest", interest);
        monthMap.put("balance", cash.add(interest));

        map.put(1, monthMap);
        return map;

    }

    /**
     * 按月，到期 还本金，按月还利息
     *
     * @param cash
     * @param rate
     * @param months
     * @return
     */
    public static Map<Integer, Map<String, BigDecimal>> getMonthInfoOnceCalital(BigDecimal cash, BigDecimal rate, int months) {
        Map map = new HashMap();
        BigDecimal monthInterest = cash.multiply(rate).divide(new BigDecimal("12"), 18, BigDecimal.ROUND_UP);
        for (int monthIndex = 0; monthIndex < months; monthIndex++) {
            Map monthMap = new HashMap();
            BigDecimal calital = BigDecimal.ZERO;
            if (monthIndex == months - 1) {
                calital = cash;
            }
            monthMap.put("calital", calital);
            monthMap.put("interest", monthInterest);
            monthMap.put("balance", calital.add(monthInterest));
            map.put((monthIndex + 1), monthMap);
        }
        return map;
    }

    /**
     * 按指定天数为周期，到期还本金，周期还利息
     *
     * @param cash
     * @param rate
     * @param cycleValue
     * @param days
     * @return
     */
    public static Map<Integer, Map<String, BigDecimal>> getDaysInfoOnceCalital(BigDecimal cash, BigDecimal rate, int days, int cycleValue) {
        Map map = new HashMap();
        BigDecimal dayInterest = cash.multiply(rate).divide(new BigDecimal(String.valueOf(daysInYear)), 18, BigDecimal.ROUND_UP);

        BigDecimal interest = dayInterest.multiply(new BigDecimal(String.valueOf(cycleValue)));
        //总期数
        int cycles = 0;
        if (days % cycleValue == 0) {
            cycles = days / cycleValue;
        } else {
            cycles = days / cycleValue + 1;
        }
        for (int cycleIndex = 0; cycleIndex < cycles; cycleIndex++) {
            Map dayMap = new HashMap();
            BigDecimal calital = BigDecimal.ZERO;
            if (cycleIndex == cycles - 1) {
                calital = cash;
            }
            dayMap.put("calital", calital);
            dayMap.put("interest", interest);
            dayMap.put("balance", calital.add(interest));
            map.put((cycleIndex + 1), dayMap);
        }
        return map;
    }

    /**
     * 等额本金方式计算每月还款额
     *
     * @param cash
     * @param rate
     * @param months
     * @return
     */
    public static Map<Integer, Map<String, BigDecimal>> getMonthInfoByAverageCapital(BigDecimal cash, BigDecimal rate, int months) {
        Map map = new HashMap();
        //每月本金
        BigDecimal monthCaplital = cash.divide(new BigDecimal(String.valueOf(months)), 18, BigDecimal.ROUND_CEILING);
        for (int i = 0; i < months; i++) {
            Map monthMap = new HashMap();
            BigDecimal monthInterest = cash.multiply(rate).divide(new BigDecimal("12"), 18, BigDecimal.ROUND_CEILING);
            monthMap.put("balance", monthInterest.add(monthCaplital));//当月还款总额
            monthMap.put("interest", monthInterest);//当月利息
            monthMap.put("calital", monthCaplital);//当月本金
            cash = cash.subtract(monthCaplital);
            map.put(i + 1, monthMap);
        }
        return map;

    }

    /**
     * 等额本金方式计算所有月的利息之和
     *
     * @param cash
     * @param rate
     * @param months
     * @return
     */
    public static BigDecimal getAllMonthInterestByAverageCapital(BigDecimal cash, BigDecimal rate, int months) {
        BigDecimal allInterest = BigDecimal.ZERO;
        BigDecimal monthCaplital = cash.divide(new BigDecimal(String.valueOf(months)), 18, BigDecimal.ROUND_CEILING);
        for (int i = 0; i < months; i++) {
            Map monthMap = new HashMap();
            BigDecimal monthInterest = cash.multiply(rate).divide(new BigDecimal("12"), 18, BigDecimal.ROUND_CEILING);
            allInterest = allInterest.add(monthInterest);
        }
        return allInterest;
    }

    /**
     * 等额本金
     * 按指定天数及每个周期的天数，计算每个周期的应还金额（本金及利息）
     *
     * @param cash
     * @param rate
     * @param days
     * @param cycleValue
     * @return
     */
    public static Map<Integer, Map<String, BigDecimal>> getDaysInfoByAverageCapital(BigDecimal cash, BigDecimal rate, int days, int cycleValue) {
        if (days == 0 || cycleValue == 0 || cycleValue > days) {
            return null;
        }
        //总期数
        int cycles = 0;
        if (days % cycleValue == 0) {
            cycles = days / cycleValue;
        } else {
            cycles = days / cycleValue + 1;
        }
        //计算每期本金()
        BigDecimal theDayCalital = cash.divide(new BigDecimal(String.valueOf(days)), 18, BigDecimal.ROUND_UP);
        Map<Integer, Map<String, BigDecimal>> cycleMap = new HashMap<Integer, Map<String, BigDecimal>>();

        int dayIndex = 0;
        for (int i = 0; i < cycles; i++) {
            Map cycleInfo = new HashMap();
            BigDecimal cycleInterest = BigDecimal.ZERO;
            BigDecimal cycleCalital = BigDecimal.ZERO;
            BigDecimal dayInterest = cash.multiply(rate).divide(new BigDecimal(String.valueOf(daysInYear)), 18, BigDecimal.ROUND_UP);
            for (int d = 0; d < cycleValue; d++) {
                if (dayIndex < days) {
                    cycleInterest = cycleInterest.add(dayInterest);
                    cycleCalital = cycleCalital.add(theDayCalital);
                }
                dayIndex++;
            }
            cycleInfo.put("interest", cycleInterest);
            cycleInfo.put("calital", cycleCalital);
            cycleInfo.put("balance", cycleCalital.add(cycleInterest));
            cash = cash.subtract(cycleCalital);
            cycleMap.put(i + 1, cycleInfo);
        }

        return cycleMap;

    }

//    public static Map<Integer, BigDecimal> getDailyInterestByAverageCapital(BigDecimal cash, BigDecimal rate, List<Date> repaymentDates) {
//
//        //计算每期本金()
//        BigDecimal theDayCalital = cash.divide(new BigDecimal(repaymentDates.size()), 18, BigDecimal.ROUND_CEILING);
//
//        Map<Integer, Map<String, BigDecimal>> cycleMap = new HashMap<Integer, Map<String, BigDecimal>>();
//
//        int dayIndex = 0;
//        for (int i = 0; i < repaymentDates.size(); i++) {
//            Map cycleInfo = new HashMap();
//            BigDecimal cycleInterest = BigDecimal.ZERO;
//            BigDecimal cycleCalital = BigDecimal.ZERO;
//            BigDecimal dayInterest = cash.multiply(rate).divide(new BigDecimal(daysInYear), 18, BigDecimal.ROUND_CEILING);
//            for (int d = 0; d < cycleValue; d++) {
//                if (dayIndex < days) {
//                    cycleInterest = cycleInterest.add(dayInterest);
//                    cycleCalital = cycleCalital.add(theDayCalital);
//                }
//                dayIndex++;
//            }
//            cycleInfo.put("interest", cycleInterest);
//            cycleInfo.put("calital", cycleCalital);
//            cycleInfo.put("balance", cycleCalital.add(cycleInterest));
//            cash = cash.subtract(cycleCalital);
//            cycleMap.put(i + 1, cycleInfo);
//        }
//
//        return cycleMap;
//
//    }

    /**
     * 等额本金方式计算每个周期的利息总额
     *
     * @param cash
     * @param rate
     * @param days
     * @param cycleValue
     * @return
     */
    public static BigDecimal getAllCycleInterestByAverageCapital(BigDecimal cash, BigDecimal rate, int days, int cycleValue) {
        BigDecimal allInterest = BigDecimal.ZERO;
        //总期数
        int cycles = 0;
        if (days % cycleValue == 0) {
            cycles = days / cycleValue;
        } else {
            cycles = days / cycleValue + 1;
        }
        //计算每期本金()
        BigDecimal theDayCalital = cash.divide(new BigDecimal(String.valueOf(days)), 6, BigDecimal.ROUND_CEILING);

        Map<Integer, Map<String, BigDecimal>> cycleMap = new HashMap<Integer, Map<String, BigDecimal>>();

        int dayIndex = 0;
        for (int i = 0; i < cycles; i++) {
            Map cycleInfo = new HashMap();
            BigDecimal cycleInterest = BigDecimal.ZERO;
            BigDecimal cycleCalital = BigDecimal.ZERO;
            BigDecimal dayInterest = cash.multiply(rate).divide(new BigDecimal(String.valueOf(daysInYear)), 18, BigDecimal.ROUND_CEILING);
            for (int d = 0; d < cycleValue; d++) {
                if (dayIndex < days) {
                    cycleInterest = cycleInterest.add(dayInterest);
                    cycleCalital = cycleCalital.add(theDayCalital);
                }
                dayIndex++;
            }
            allInterest = allInterest.add(cycleInterest);
            cash = cash.subtract(cycleCalital);
            cycleMap.put(i + 1, cycleInfo);
        }
        return allInterest;

    }

    /**
     * todo 等额本金,按月还款时，计算每日利息
     *
     * @param cash
     * @param rate
     * @param months
     * @param theMonth
     * @param firstDate
     * @return
     */
    public static Map<Date, BigDecimal> getMonthEveryDayInterestByAverageCapital(BigDecimal cash, BigDecimal rate, int months, int theMonth, Date firstDate) {
        Map<Date, BigDecimal> daysMap = new HashMap<Date, BigDecimal>();
        BigDecimal monthRate = rate.divide(new BigDecimal("12"), 18, BigDecimal.ROUND_DOWN);
        //每月本金
//        BigDecimal monthCaplital = cash.divide(new BigDecimal(months), 18, BigDecimal.ROUND_CEILING);
//        for (int i = 0; i < months; i++) {
//
//        }
        return daysMap;
    }

    /**
     * todo 等额本金，按周期 还款时，计算每日利息
     *
     * @param cash
     * @param rate
     * @param days
     * @param cycleValue
     * @param firstDate
     * @return
     */
    public static Map<Date, BigDecimal> getDayEveryDayInterestByAverageCapital(BigDecimal cash, BigDecimal rate, int days, int cycleValue, Date firstDate) {
        Map<Date, BigDecimal> daysMap = new HashMap<Date, BigDecimal>();
        return daysMap;
    }

    /**
     * 等额本息算法，计算每期的本金、利息及应还金额
     *
     * @param cash
     * @param rate
     * @param months
     * @return
     */
    public static Map<Integer, Map<String, BigDecimal>> getMonthInfoByAverageCapitalPlusInterest(BigDecimal cash, BigDecimal rate, int months) {
        if (months <= 0) {
            return null;
        }
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

    /**
     * 等额本息算法，计算所有月的利息总额
     *
     * @param cash
     * @param rate
     * @param months
     * @return
     */
    public static BigDecimal getAllMonthInterestByAverageCapitalPlusInterest(BigDecimal cash, BigDecimal rate, int months) {
        BigDecimal allInterest = BigDecimal.ZERO;
        BigDecimal monthValue = getMonthAverageCapitalPlusInterest(cash, rate, months);
        for (int month = 0; month < months; month++) {
            //计算本月利息
            BigDecimal interest = cash.multiply(rate).divide(new BigDecimal(String.valueOf(months)), 18, BigDecimal.ROUND_CEILING);
            //每月还款额减每个月的利息，即该月的本金
            BigDecimal theMonthCash = monthValue.subtract(interest);

            allInterest = allInterest.add(interest);
            cash = cash.subtract(theMonthCash);
        }
        return allInterest;
    }

    /**
     * 等额本息
     * 按指定天数及每个周期的天数，计算每个周期的应还金额（本金及利息）
     *
     * @param cash
     * @param rate
     * @param days
     * @param cycleValue
     * @return
     */
    public static Map<Integer, Map<String, BigDecimal>> getDaysInfoByCapitalPlusInterest(BigDecimal cash, BigDecimal rate, int days, int cycleValue) {
        if (days == 0 || cycleValue == 0 || cycleValue > days) {
            return null;
        }
        //总期数
        int cycles = 0;
        if (days % cycleValue == 0) {
            cycles = days / cycleValue;
        } else {
            cycles = days / cycleValue + 1;
        }
        Map map = new HashMap();
        BigDecimal dayRate = rate.divide(new BigDecimal(String.valueOf(daysInYear)), 18, BigDecimal.ROUND_CEILING);
        BigDecimal cycleRate = dayRate.multiply(new BigDecimal(String.valueOf(cycleValue)));

        BigDecimal cycleBalance = getDayAverageCapitalPlusInterest(cash, rate, days, cycleValue);
        for (int c = 0; c < cycles; c++) {
            BigDecimal cycleInterest = cash.multiply(cycleRate);
            BigDecimal cycleCalital = cycleBalance.subtract(cycleInterest);
            Map monthMap = new HashMap();
            monthMap.put("balance", cycleBalance);//本期还款总额
            monthMap.put("interest", cycleInterest);//本期还款利息
            monthMap.put("calital", cycleCalital);//本期本金
            map.put(c + 1, monthMap);
            cash = cash.subtract(cycleCalital);
        }
        return map;
    }

    /**
     * 等额本息算法，计算所有期的利息总额
     *
     * @param cash
     * @param rate
     * @return
     */
    public static BigDecimal getAllCycleInterestByAverageCapitalPlusInterest(BigDecimal cash, BigDecimal rate, int days, int cycleValue) {
        BigDecimal allInterest = BigDecimal.ZERO;
        //总期数
        int cycles = 0;
        if (days % cycleValue == 0) {
            cycles = days / cycleValue;
        } else {
            cycles = days / cycleValue + 1;
        }
        Map map = new HashMap();
        BigDecimal dayRate = rate.divide(new BigDecimal(String.valueOf(daysInYear)), 18, BigDecimal.ROUND_CEILING);
        BigDecimal cycleRate = dayRate.multiply(new BigDecimal(String.valueOf(cycleValue)));

        BigDecimal cycleBalance = getDayAverageCapitalPlusInterest(cash, rate, days, cycleValue);
        for (int c = 0; c < cycles; c++) {
            BigDecimal cycleInterest = cash.multiply(cycleRate);
            BigDecimal cycleCalital = cycleBalance.subtract(cycleInterest);
            allInterest = allInterest.add(cycleInterest);
            cash = cash.subtract(cycleCalital);
        }
        return allInterest;
    }

    /**
     * 等额本息算法，计算每期还款额
     * 按指定天数及每个周期的天数
     *
     * @param cash
     * @param rate
     * @param days
     * @param cycleValue
     * @return
     */
    public static BigDecimal getDayAverageCapitalPlusInterest(BigDecimal cash, BigDecimal rate, int days, int cycleValue) {
        BigDecimal dayRate = rate.divide(new BigDecimal(String.valueOf(daysInYear)), 18, BigDecimal.ROUND_CEILING);
        BigDecimal cycleRate = dayRate.multiply(new BigDecimal(String.valueOf(cycleValue)));
        BigDecimal trate = cycleRate.add(BigDecimal.ONE);
        //总期数
        int cycles = 0;
        if (days % cycleValue == 0) {
            cycles = days / cycleValue;
        } else {
            cycles = days / cycleValue + 1;
        }
        for (int i = 0; i < cycles - 1; i++) {
            trate = trate.multiply(cycleRate.add(BigDecimal.ONE));
        }
        BigDecimal divisor = cash.multiply(trate).multiply(cycleRate);
        BigDecimal dividend = trate.subtract(BigDecimal.ONE);
        BigDecimal cycleRepay = divisor.divide(dividend, 8, BigDecimal.ROUND_CEILING);
        return cycleRepay;
    }


    /**
     * 等额本息算法计算月还款额
     * 返回月还款额的精度为18位
     *
     * @return
     */
    public static BigDecimal getMonthAverageCapitalPlusInterest(BigDecimal cash, BigDecimal rate, int months) {
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
     * 等额本息算法，计算月利息
     *
     * @param cash     借款总额
     * @param rate     年利率
     * @param months   借款期限（月数）
     * @param theMonth 指定月份
     * @return
     */
    public static BigDecimal getMonthInterestAverageCapitalPlusInterest(BigDecimal cash, BigDecimal rate, int months, int theMonth) {
        BigDecimal monthValue = getMonthAverageCapitalPlusInterest(cash, rate, months);
        BigDecimal interest = BigDecimal.ZERO;
        for (int i = 0; i < theMonth; i++) {
            //从第0个月开始计算每个月的利息
            interest = cash.multiply(rate).divide(new BigDecimal(String.valueOf(months)), 6, BigDecimal.ROUND_CEILING);
            //每月还款额减每个月的利息，即该月的本金
            BigDecimal theMonthCash = monthValue.subtract(interest);
            cash = cash.subtract(theMonthCash);
        }
        return interest;
    }

    /**
     * todo 等额本息,按月还款时，计算每日利息
     *
     * @param cash
     * @param rate
     * @param months
     * @param theMonth
     * @param firstDate
     * @return
     */
    public static Map<Date, BigDecimal> getMonthEveryDayInterestAverageCapitalPlusInterest(BigDecimal cash, BigDecimal rate, int months, int theMonth, Date firstDate) {
        Map<Date, BigDecimal> daysMap = new HashMap<Date, BigDecimal>();
        BigDecimal monthRate = rate.divide(new BigDecimal("12"), 18, BigDecimal.ROUND_DOWN);

        return daysMap;
    }

    /**
     * todo 等额本息，按周期 还款时，计算每日利息
     *
     * @param cash
     * @param rate
     * @param days
     * @param cycleValue
     * @param firstDate
     * @return
     */
    public static Map<Date, BigDecimal> getDayEveryDayInterestAverageCapitalPlusInterest(BigDecimal cash, BigDecimal rate, int days, int cycleValue, Date firstDate) {
        Map<Date, BigDecimal> daysMap = new HashMap<Date, BigDecimal>();
        return daysMap;
    }


    public static void main(String[] args) {
//        System.out.println(getDayAverageCapitalPlusInterest(new BigDecimal("120000"), new BigDecimal(0.068), 60, 30));
//        Map<Integer, Map<String, BigDecimal>> result = getMonthInfoByAverageCapitalPlusInterest(BigDecimal.valueOf(10000), BigDecimal.valueOf(0.12), 12);
//        Map<Integer, Map<String, BigDecimal>> result = getDaysInfoByAverageCapital(new BigDecimal("1000000"), new BigDecimal(0.20), 40, 10);
        try {
            Map<Integer, Map<String, BigDecimal>> result =getCalitalAndInterest(new BigDecimal("2500"),new BigDecimal("12"),'2','1','1','1',11,2);

        Iterator iterator = result.keySet().iterator();
        BigDecimal allBalance = BigDecimal.ZERO;
        BigDecimal allInterest = BigDecimal.ZERO;
        BigDecimal allCalital = BigDecimal.ZERO;
        while (iterator.hasNext()) {
            Integer theMonth = (Integer) iterator.next();
            Map<String, BigDecimal> map = result.get(theMonth);
            System.out.println("第" + theMonth.intValue() + "期：" + map.get("balance") + ",利息：" + map.get("interest").toPlainString() + ",本金：" + map.get("calital"));
            allBalance = allBalance.add(map.get("balance"));
            allInterest = allInterest.add(map.get("interest"));
            allCalital = allCalital.add(map.get("calital"));
        }
        System.out.println("应还总额：" + allBalance + ",利息总额：" + allInterest + ",本金总额" + allCalital);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(getAllCycleInterestByAverageCapitalPlusInterest(new BigDecimal("10000"), new BigDecimal(0.15), 300, 60));
//        BigDecimal bigDecimal = BigDecimal.ZERO;
////        bigDecimal = bigDecimal.setScale(18);
//        BigDecimal multiplicand = new BigDecimal(0.5);
//        multiplicand = multiplicand.setScale(18);
//        try {
//            BigDecimal multiply = bigDecimal.multiply(multiplicand);
//            System.out.println("m=" + multiply);
//            System.out.println(BigDecimalUtil.compareTo(multiply, BigDecimal.ZERO, true, 2) == 0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    //todo 计算罚息的接口
    //todo 计算费用金额的接口
    //todo 计算罚金的接口

    public static Map<Integer, Map<String, BigDecimal>> getCalitalAndInterest(BigDecimal balance, BigDecimal annualRate, char dueTimeType, char repaymentMethod, char repaymentType,
                                                                              char repaymentCycle, Integer dueTime, Integer cycleValue) throws Exception {
        BigDecimal rate = annualRate.divide(new BigDecimal("100"), 6, BigDecimal.ROUND_CEILING);
        Map<Integer, Map<String, BigDecimal>> mapping = new HashMap<Integer, Map<String, BigDecimal>>();
        if (repaymentMethod == REPAYMENTMETHOD_AVERAGECAPITALPLUSINTEREST) {
            if (dueTimeType == TIMELIMITTYPE_MONTH)
                if (repaymentCycle == REPAYMENTCYCLE_ONCE)
                    mapping = InterestCalculation.getMonthInfoOnce(balance, rate, dueTime);
                else if (repaymentType == REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL)
                    mapping = InterestCalculation.getMonthInfoOnceCalital(balance, rate, dueTime);
                else
                    mapping = InterestCalculation.getMonthInfoByAverageCapitalPlusInterest(balance, rate, dueTime);
            else if (repaymentCycle == REPAYMENTCYCLE_ONCE)
                mapping = InterestCalculation.getDaysInfoOnce(balance, rate, dueTime);
            else if (repaymentType == REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL)
                mapping = InterestCalculation.getDaysInfoOnceCalital(balance, rate, dueTime, cycleValue);
            else
                mapping = InterestCalculation.getDaysInfoByCapitalPlusInterest(balance, rate, dueTime, cycleValue);
        } else {
            if (dueTimeType == TIMELIMITTYPE_MONTH)
                if (repaymentCycle == REPAYMENTCYCLE_ONCE)
                    mapping = InterestCalculation.getMonthInfoOnce(balance, rate, dueTime);
                else if (repaymentType == REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL)
                    mapping = InterestCalculation.getMonthInfoOnceCalital(balance, rate, dueTime);
                else
                    mapping = InterestCalculation.getMonthInfoByAverageCapital(balance, rate, dueTime);
            else if (repaymentCycle == REPAYMENTCYCLE_ONCE)
                mapping = InterestCalculation.getDaysInfoOnce(balance, rate, dueTime);
            else if (repaymentType == REPAYMENT_TYPE_CYCLE_OF_INTEREST_DUE_PRINCIPAL)
                mapping = InterestCalculation.getDaysInfoOnceCalital(balance, rate, dueTime, cycleValue);
            else
                mapping = InterestCalculation.getDaysInfoByAverageCapital(balance, rate, dueTime, cycleValue);
        }
        return mapping;
    }

    /**
     * repaymentMethod  还款方法：1为等额本息，2为等额本金
     * repaymentType 还款方式：1为周期还本息，2为周期还利息,到期还本金，3为周期还本金,到期还利息
     * repaymentCycle 还款周期类型：1为按月，2为到期，3为按天
     * cycleValue 周期值
     * dueTime  期数值
     * */
    public static BigDecimal getAllInterest(BigDecimal balance, BigDecimal annualRate, char dueTimeType, char repaymentMethod, char repaymentType, char repaymentCycle, Integer dueTime, Integer cycleValue) throws Exception {
        BigDecimal allInterest = BigDecimal.ZERO;
        Map<Integer, Map<String, BigDecimal>> mapping = new HashMap<Integer, Map<String, BigDecimal>>();
        mapping = InterestCalculation.getCalitalAndInterest(balance, annualRate, dueTimeType, repaymentMethod, repaymentType, repaymentCycle, dueTime, cycleValue);
        for (Integer code : mapping.keySet()) {
            allInterest = allInterest.add(mapping.get(code).get("interest"));
        }
        return allInterest;
    }

    /**
     * 获取预期收益
     *
     * @param balance
     * @param profitRate
     * @param timeLimitType
     * @param timeLimitValue
     * @return
     */
    public static BigDecimal getExpectedInteresting(BigDecimal balance, BigDecimal profitRate, char timeLimitType, int timeLimitValue) {
        BigDecimal interest = BigDecimal.ZERO;
        if (null == balance || null == profitRate)
            return interest;

        profitRate = profitRate.divide(new BigDecimal("100"), 18, BigDecimal.ROUND_CEILING);
        if (timeLimitType == TIMELIMITTYPE_MONTH) {
            profitRate = profitRate.divide(new BigDecimal(String.valueOf(MONTH)), 18, BigDecimal.ROUND_CEILING);
        } else if (timeLimitType == TIMELIMITTYPE_DAY) {
            profitRate = profitRate.divide(new BigDecimal(String.valueOf(daysInYear)), 18, BigDecimal.ROUND_CEILING);
        }
        interest = balance.multiply(profitRate).multiply(new BigDecimal(String.valueOf(timeLimitValue)));
        return interest;
    }

    /**
     * 计算提现手续费
     *
     * @param withDrawAmount
     * @return
     */
    public static BigDecimal getWithDrawFee(BigDecimal withDrawAmount) {
        return new BigDecimal("3");
//        return BigDecimal.ZERO;
    }

    /**
     * 计算一年的天数
     *
     * @param
     * @return
     */
    public static int getHowManyDayInAYear() {
        return GregorianCalendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR );
    }

}
