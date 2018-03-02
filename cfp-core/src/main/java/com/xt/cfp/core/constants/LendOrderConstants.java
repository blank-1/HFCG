package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by yulei on 2015/7/9.
 */
public final class LendOrderConstants {

    public enum FinanceOrderStatusEnum implements EnumsCanDescribed {
        UNPAY("0","待支付"),
        REPAYMENTING("1","理财中"),
        CLEAR("2","已结清"),
        OUT_TIME("3","已过期"),
        REVOCATION("4","已撤销"),//未使用
        HASPAID("5","已支付"),
        QUITING("6","授权期结束"),
        ;


        private final String value;
        private final String desc;

        FinanceOrderStatusEnum(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public enum LoanOrderStatusEnum implements EnumsCanDescribed {
        UNPAY("0","待支付"),
        REPAYMENTING("1","还款中"),
        CLEAR("2","已结清"),
        OUT_TIME("3","已过期"),
        REVOCATION("4","已撤销"),
        PAID("5","已支付"),
        FAIL("7","流标"),
        ;


        private final String value;
        private final String desc;

        LoanOrderStatusEnum(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public enum RightsOrderStatusEnum implements EnumsCanDescribed {
        UNPAY("0","待支付"),
        REPAYMENTING("1","还款中"),
        CLEAR("2","已结清"),
        OUT_TIME("3","已过期"),
        REVOCATION("4","已撤销"),
        PAID("5","已支付"),
        QUITING("6","退出中"),
        ;


        private final String value;
        private final String desc;

        RightsOrderStatusEnum(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        @Override
        public String getValue() {
            return value;
        }
    }
    public enum FinanceProfitReturnEnum implements EnumsCanDescribed {

        TO_FINANCE_ACCOUNT("0","收益复利投资"),
        TO_CASH_ACCOUNT("1","收益提取至可用余额"),
        ;


        private final String value;
        private final String desc;

        FinanceProfitReturnEnum(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        @Override
        public String getValue() {
            return value;
        }
    }
}
