package com.xt.cfp.core.util;

import com.xt.cfp.core.constants.LendOrderReceiveStateEnum;
import com.xt.cfp.core.constants.LendProductClosingType;
import com.xt.cfp.core.constants.LendProductInterestReturnType;
import com.xt.cfp.core.constants.LendProductTimeLimitType;
import com.xt.cfp.core.pojo.LendOrder;
import com.xt.cfp.core.pojo.LendOrderReceive;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by yulei on 2015/8/7 0007.
 */
public final class FinanceCalculateUtils {


    /**
     * 计算并设定省心计划的合同结束日期，前提是省心计划的合同开始时间不为空
     * @param financeOrder
     */
    public final static Date calAndSetAgreementEndDate(LendOrder financeOrder) {
        ValidationUtil.checkRequiredField(financeOrder, "agreementStartDate", "timeLimitType", "timeLimit");

        if (financeOrder.getTimeLimitType().equals(LendProductTimeLimitType.TIMELIMITTYPE_DAY.getValue())) {
            financeOrder.setAgreementEndDate(DateUtil.addDate(financeOrder.getAgreementStartDate(), Calendar.DATE, financeOrder.getTimeLimit()));
        } else if (financeOrder.getTimeLimitType().equals(LendProductTimeLimitType.TIMELIMITTYPE_MONTH.getValue())) {
            financeOrder.setAgreementEndDate(DateUtil.addDate(financeOrder.getAgreementStartDate(), Calendar.MONTH, financeOrder.getTimeLimit()));
        }

        return financeOrder.getAgreementEndDate();
    }

    /**
     * 计算并设定省心计划的封闭到期日期，前提是省心计划的合同开始时间不为空
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

    /**
     * 根据年利率获取月利率
     * @param annualRate
     * @param roundType
     * @return
     */
    public static BigDecimal getMonthRate(BigDecimal annualRate, int roundType) {
        return annualRate.divide(new BigDecimal("12"), 18, roundType);
    }

    /**
     * 根据年利率获取月利率和差额利率（年利率减去月利率之和）
     * @param annualRate
     * @param roundType
     * @return
     */
    public static BigDecimal[] getMonthAndModRate(BigDecimal annualRate, int roundType) {
        BigDecimal[] result = new BigDecimal[2];
        result[0] = annualRate.divide(new BigDecimal("12"), 18, roundType);
        result[1] = annualRate.subtract(result[0].multiply(new BigDecimal("12")));
        return result;
    }

    /**
     * 根据一个利率和一个分期数获取周期利率
     * @param rate
     * @param periodNum
     * @param roundType
     * @return
     */
    public static BigDecimal getPeriodRate(BigDecimal rate, int periodNum, int roundType) {
        return rate.divide(new BigDecimal(String.valueOf(periodNum)), 18, roundType);
    }


    /**
     * 根据一个利率和一个分期数获取周期利率和差额利率（总利率减去分期利率之和）
     * @param rate
     * @param periodNum
     * @param roundType
     * @return
     */
    public static BigDecimal[] getPeriodAndModRate(BigDecimal rate, int periodNum, int roundType) {
        BigDecimal[] result = new BigDecimal[2];
        result[0] = rate.divide(new BigDecimal(String.valueOf(periodNum)), 18, roundType);
        result[1] = rate.subtract(result[0].multiply(new BigDecimal(String.valueOf(periodNum))));
        return result;
    }

    /**
     * 获取日利率
     * @param annualRate
     * @return
     */
    public static BigDecimal getDayRate(BigDecimal annualRate, int roundType) {
        return annualRate.divide(new BigDecimal("365"), 18, roundType);
    }

    /**
     * 获取日利息
     * @param annualRate
     * @param moneyAmount
     * @param roundType
     * @param precision
     * @return
     */
    public static BigDecimal getDayInterest(BigDecimal annualRate, BigDecimal moneyAmount, int roundType, int precision) {
        BigDecimal dayRate = getDayRate(annualRate, roundType);
        return dayRate.multiply(moneyAmount, new MathContext(precision, RoundingMode.valueOf(roundType)));
    }

    public final static List<LendOrderReceive> generateReceiveList(LendOrder financeOrder) {
        List<LendOrderReceive> lendOrderReceives = new ArrayList<LendOrderReceive>();
        String toInterestPoint = financeOrder.getToInterestPoint();
        int days = DateUtil.daysBetweenDates(financeOrder.getAgreementEndDate(), financeOrder.getAgreementStartDate());
        BigDecimal dayRate = getDayRate(financeOrder.getProfitRate(), BigDecimal.ROUND_DOWN);

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

//    public static void main(String[] args) {
//        LendOrder fo = new LendOrder();
//        fo.setBuyBalance(new BigDecimal("10000"));
//        Date date = new Date();
//        Date endDate = DateUtil.addDate(date, Calendar.DATE, 180);
//        fo.setAgreementStartDate(date);
//        fo.setAgreementEndDate(endDate);
//        fo.setLendOrderId(99l);
//        fo.setProfitRate(new BigDecimal("0.12"));
//        BigDecimal dayRate = getDayRate(fo.getProfitRate());
//        ArrayList<LendOrderReceive> lendOrderReceivesForFixCycle = getLendOrderReceivesForFixCycle(fo, 30);
//        System.out.println(JsonUtil.getGson(false).toJson(lendOrderReceivesForFixCycle));
//        System.out.println("---------------------------------------------------------------------------\r\n\r\n");
////        ArrayList<LendOrderReceive> lendOrderReceivesForFixCycle1 = getLendOrderReceivesForFixCycle(fo, 365, dayRate, 7);
////        System.out.println(lendOrderReceivesForFixCycle1);
////        System.out.println("---------------------------------------------------------------------------\r\n\r\n");
//        ArrayList<LendOrderReceive> lendOrderReceivesForFixCycle2 = getLendOrderReceivesForFixCycle(fo, 90);
//        System.out.println(JsonUtil.getGson(false).toJson(lendOrderReceivesForFixCycle2));
//        System.out.println("---------------------------------------------------------------------------\r\n\r\n");
//        ArrayList<LendOrderReceive> lendOrderReceivesForFixCycle3 = getLendOrderReceivesForFixCycle(fo, 180);
//        System.out.println(JsonUtil.getGson(false).toJson(lendOrderReceivesForFixCycle3));
//        System.out.println("---------------------------------------------------------------------------\r\n\r\n");
//    }

    public static ArrayList<LendOrderReceive> getLendOrderReceivesForFixCycle(LendOrder financeOrder, int cycleNums) {
        ArrayList<LendOrderReceive> receives = new ArrayList<LendOrderReceive>();
        int days = DateUtil.daysBetweenDates(financeOrder.getAgreementEndDate(), financeOrder.getAgreementStartDate());
        BigDecimal dayRate = getDayRate(financeOrder.getProfitRate(), BigDecimal.ROUND_DOWN);
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

    /**
     * 等额本息回款计划
     * @param amount
     * @param cycleType
     * @param cycleValue
     * @param periodNum
     * @param annualRate
     * @param startDate
     * @param ROUND_CEILING
     * @return
     */
    public static List<RepaymentDetail> getPlanForAverageInterest(BigDecimal amount, CycleType cycleType, Integer cycleValue, Integer periodNum, BigDecimal annualRate, Date startDate, boolean ROUND_CEILING) {
        int roundType = BigDecimal.ROUND_DOWN;
        if (ROUND_CEILING)
            roundType = BigDecimal.ROUND_CEILING;

        List<RepaymentDetail> result = getPlanForAverageInterest(cycleType, amount, cycleValue, periodNum, annualRate, startDate, roundType);
        return result;
    }

    /**
     * 获取等额本息的单个分期总还款额
     * @param cycleType
     * @param amount
     * @param cycleValue
     * @param annualRate
     * @param periodNum
     * @param roundType
     * @return
     */
    private static BigDecimal getPeriodRepaymentAmountForAverageInterest(CycleType cycleType, BigDecimal amount, Integer cycleValue, BigDecimal annualRate, int periodNum, int roundType) {
        BigDecimal trate, cycleRate = null, periodRate;
        if (cycleType == CycleType.MONTH) {
            cycleRate = getMonthRate(annualRate, roundType);
        }
        if (cycleType == CycleType.DAY) {
            cycleRate = getDayRate(annualRate, roundType);
        }
        periodRate = cycleRate.multiply(new BigDecimal(cycleValue.toString()));
        trate = periodRate.add(BigDecimal.valueOf(1));
        for (int i = 1; i < periodNum; i++) {
            trate = trate.multiply(periodRate.add(BigDecimal.valueOf(1)));
        }
        BigDecimal divisor = amount.multiply(trate).multiply(periodRate);
        BigDecimal dividend = trate.subtract(BigDecimal.valueOf(1));
        return divisor.divide(dividend, 18, roundType);
    }

    /**
     * 获取等额本息的还款计划，可以根据不同的周期类型、周期值、分期数来进行计算
     * @param cycleType
     * @param amount
     * @param cycleValue
     * @param periodNum
     * @param annualRate
     * @param startDate
     * @param roundType
     * @return
     */
    private static List<RepaymentDetail> getPlanForAverageInterest(CycleType cycleType, BigDecimal amount, Integer cycleValue, Integer periodNum, BigDecimal annualRate, Date startDate, int roundType) {
        List<RepaymentDetail> result = new ArrayList<RepaymentDetail>();
        BigDecimal cycleRate = null;
        int dateType = -1;

        if (cycleType == CycleType.MONTH) {
            cycleRate = getMonthRate(annualRate, roundType);
            dateType = Calendar.MONTH;
        }

        if (cycleType == CycleType.DAY) {
            cycleRate = getDayRate(annualRate, roundType);
            dateType = Calendar.DATE;
        }

        BigDecimal periodRepaymentAmount = getPeriodRepaymentAmountForAverageInterest(cycleType, amount, cycleValue, annualRate, periodNum, roundType);
        BigDecimal periodRate = cycleRate.multiply(new BigDecimal(cycleValue.toString()));
        BigDecimal amountTemp = new BigDecimal(amount.toPlainString());
        Date startDateEveryPeriod = startDate;
        Date endDateEveryPeriod = DateUtil.addDate(DateUtil.addDate(startDateEveryPeriod, dateType, cycleValue), Calendar.DATE, -1);
        for (int i = 1; i <= periodNum; i++) {
            RepaymentDetail repaymentDetail = new RepaymentDetail();
            BigDecimal periodInterest = amountTemp.multiply(periodRate).stripTrailingZeros();
            if (i < periodNum)
                repaymentDetail.setCapital(periodRepaymentAmount.subtract(periodInterest).stripTrailingZeros());
            else
                repaymentDetail.setCapital(amountTemp.stripTrailingZeros());
            repaymentDetail.setStart(startDateEveryPeriod);
            repaymentDetail.setEnd(endDateEveryPeriod);
            repaymentDetail.setInterest(periodInterest.stripTrailingZeros());
            repaymentDetail.setAmount(repaymentDetail.getCapital().add(repaymentDetail.getInterest()).stripTrailingZeros());
            repaymentDetail.setPenalty(BigDecimal.ZERO);
            repaymentDetail.setPeriod(i);
            repaymentDetail.setPeriodRate(periodRate.stripTrailingZeros());
            if (i < periodNum)
                amountTemp = amountTemp.subtract(repaymentDetail.getCapital().stripTrailingZeros());
            else
                amountTemp = BigDecimal.ZERO;
            repaymentDetail.setSurplusCapital(amountTemp.stripTrailingZeros());
            startDateEveryPeriod = DateUtil.addDate(endDateEveryPeriod, Calendar.DATE, 1);
            endDateEveryPeriod = DateUtil.addDate(DateUtil.addDate(startDateEveryPeriod, dateType, cycleValue), Calendar.DATE, -1);
            result.add(repaymentDetail);
        }

        return result;
    }



    /**
     * 等额本金回款计划
     * @param amount
     * @param cycleType
     * @param cycleValue
     * @param periodNum
     * @param annualRate
     * @param startDate
     * @param ROUND_CEILING
     * @return
     */
    public static List<RepaymentDetail> getPlanForAverageCapital(BigDecimal amount, CycleType cycleType, Integer cycleValue, Integer periodNum, BigDecimal annualRate, Date startDate, boolean ROUND_CEILING) {
        int roundType = BigDecimal.ROUND_DOWN;
        if (ROUND_CEILING)
            roundType = BigDecimal.ROUND_CEILING;

        List<RepaymentDetail> result = getPlanForAverageCapital(cycleType, amount, cycleValue, periodNum, annualRate, startDate, roundType);
        return result;
    }

    /**
     * 获取等额本金的还款计划，可以根据不同的周期类型、周期值、分期数来进行计算
     * @param cycleType
     * @param amount
     * @param cycleValue
     * @param periodNum
     * @param annualRate
     * @param startDate
     * @param roundType
     * @return
     */
    private static List<RepaymentDetail> getPlanForAverageCapital(CycleType cycleType, BigDecimal amount, Integer cycleValue, Integer periodNum, BigDecimal annualRate, Date startDate, int roundType) {
        List<RepaymentDetail> result = new ArrayList<RepaymentDetail>();
        BigDecimal cycleRate = null;
        int dateType = -1;
        RoundingMode roundingMode = null;
        if (roundType == BigDecimal.ROUND_DOWN)
            roundingMode = RoundingMode.DOWN;
        if (roundType == BigDecimal.ROUND_CEILING)
            roundingMode = RoundingMode.CEILING;

        if (cycleType == CycleType.MONTH) {
            cycleRate = getMonthRate(annualRate, roundType);
            dateType = Calendar.MONTH;
        }

        if (cycleType == CycleType.DAY) {
            cycleRate = getDayRate(annualRate, roundType);
            dateType = Calendar.DATE;
        }

        BigDecimal periodRate = cycleRate.multiply(new BigDecimal(cycleValue.toString()));
        BigDecimal amountTemp = new BigDecimal(amount.toPlainString());
        BigDecimal cyclePrincipal = amount.divide(new BigDecimal(cycleValue.toString()).multiply(new BigDecimal(periodNum.toString())), 18, roundType);
        BigDecimal periodPrincipal = cyclePrincipal.multiply(new BigDecimal(cycleValue.toString()));
        Date startDateEveryPeriod = startDate;
        Date endDateEveryPeriod = DateUtil.addDate(DateUtil.addDate(startDateEveryPeriod, dateType, cycleValue), Calendar.DATE, -1);
        for (int i = 1; i <= periodNum; i++) {
            RepaymentDetail repaymentDetail = new RepaymentDetail();
            if (i < periodNum)
                repaymentDetail.setCapital(periodPrincipal);
            else
                repaymentDetail.setCapital(amountTemp);
            repaymentDetail.setStart(startDateEveryPeriod);
            repaymentDetail.setEnd(endDateEveryPeriod);
            repaymentDetail.setInterest(amountTemp.multiply(periodRate, new MathContext(18, roundingMode)));
            repaymentDetail.setAmount(repaymentDetail.getCapital().add(repaymentDetail.getInterest()));
            repaymentDetail.setPenalty(BigDecimal.ZERO);
            repaymentDetail.setPeriod(i);
            repaymentDetail.setPeriodRate(periodRate);
            if (i < periodNum)
                amountTemp = amountTemp.subtract(periodPrincipal);
            else
                amountTemp = BigDecimal.ZERO;
            repaymentDetail.setSurplusCapital(amountTemp);
            startDateEveryPeriod = DateUtil.addDate(endDateEveryPeriod, Calendar.DATE, 1);
            endDateEveryPeriod = DateUtil.addDate(DateUtil.addDate(startDateEveryPeriod, dateType, cycleValue), Calendar.DATE, -1);
            result.add(repaymentDetail);
        }

        return result;
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
//        WEEK,
        //按季度
//        SEASON,
        //按半年
//        HALF_YEAR,
        ;
    }

    /**
     * 用于封装回款计划
     */
    public static class RepaymentDetail {
        private BigDecimal capital;
        private BigDecimal surplusCapital;
        private BigDecimal interest;
        private BigDecimal penalty;
        private BigDecimal amount;
        private BigDecimal periodRate;
        private int period;
        private Date start;
        private Date end;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

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

        public BigDecimal getSurplusCapital() {
            return surplusCapital;
        }

        public void setSurplusCapital(BigDecimal surplusCapital) {
            this.surplusCapital = surplusCapital;
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

        public BigDecimal getPeriodRate() {
            return periodRate;
        }

        public void setPeriodRate(BigDecimal periodRate) {
            this.periodRate = periodRate;
        }
    }
}
