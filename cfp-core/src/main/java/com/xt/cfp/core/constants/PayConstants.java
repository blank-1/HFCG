package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by yulei on 2015/7/7.
 */
public class PayConstants {

    /**
     * 支付订单状态
     */
    public enum OrderStatus implements EnumsCanDescribed {
        UNPAY("0", "未支付"),
        SUCCESS("1", "支付成功"),
        FAIL("2", "支付失败"),
        PAYING("3", "支付中"),
        ;

        private String desc;
        private String value;

        OrderStatus(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public String getDesc() {
            return this.desc;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }

    /**
     * 业务处理状态
     */
    public enum ProcessStatus implements EnumsCanDescribed {
        UN_PROCESS("0", "未处理"),
        SUCCESS("1", "处理成功"),
        FAIL("2", "处理失败"),
        PROCESSING("3", "处理中"),
        ;

        private String desc;
        private String value;

        ProcessStatus(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public String getDesc() {
            return this.desc;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }

    /**
     * 订单费用组成类型、订单费用明细类型
     */
    public enum AmountType implements EnumsCanDescribed {
        ACCOUNT("0", "账户余额"),
        RECHARGE("1", "充值金额"),
        VOUCHERS("2", "财富券"),
        OFFLINE("3", "线下支付"),
        FINANCEACCOUNT("4", "省心计划账户余额"),
        ;

        private String value;
        private String desc;


        AmountType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public String getDesc() {
            return this.desc;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }

    /**
     * 业务类型枚举
     */
    public enum BusTypeEnum implements EnumsCanDescribed {
        BUY_FINANCE("0000", "购买省心计划", AccountConstants.BusinessTypeEnum.FEESTYPE_BUYFINANCEFREEZEN, AccountConstants.BusinessTypeEnum.FEESTYPE_BUYFINANCEUNFREEZEN),
        BID_LOAN("0001", "投标借款", AccountConstants.BusinessTypeEnum.FEESTYPE_BIDLOANFREEZEN, AccountConstants.BusinessTypeEnum.FEESTYPE_BIDLOAN_TRANSFER),
        LOAN_INCOM("0002", "借款充值", AccountConstants.BusinessTypeEnum.FEESTYPE_RECHARGE, AccountConstants.BusinessTypeEnum.FEESTYPE_RECHARGE),
        BUY_RIGHTS("0003", "购买债权", AccountConstants.BusinessTypeEnum.FEESTYPE_BUYRIGHTSFREEZEN, AccountConstants.BusinessTypeEnum.FEESTYPE_BUYRIGHTSUNFREEZEN),
        REPAYMENT_FINANCE("0004", "省心计划回款利息", AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEDEFAULTINTEREST_FROZEN, AccountConstants.BusinessTypeEnum.FEESTYPE_INCOMEDEFAULTINTEREST),
        ;

        private String desc;
        private String value;
        private AccountConstants.BusinessTypeEnum accFrozenBusTypeEnum;
        private AccountConstants.BusinessTypeEnum accUnFrozenBusTypeEnum;

        BusTypeEnum(String value, String desc, AccountConstants.BusinessTypeEnum accFrozenBusTypeEnum, AccountConstants.BusinessTypeEnum accUnFrozenBusTypeEnum) {
            this.value = value;
            this.desc = desc;
            this.accFrozenBusTypeEnum = accFrozenBusTypeEnum;
            this.accUnFrozenBusTypeEnum = accUnFrozenBusTypeEnum;
        }

        @Override
        public String getDesc() {
            return this.desc;
        }

        @Override
        public String getValue() {
            return this.value;
        }

        public AccountConstants.BusinessTypeEnum getAccFrozenBusTypeEnum() {
            return accFrozenBusTypeEnum;
        }

        public AccountConstants.BusinessTypeEnum getAccUnFrozenBusTypeEnum() {
            return accUnFrozenBusTypeEnum;
        }

        public static BusTypeEnum cValueOf(String value) {
            for (BusTypeEnum busTypeEnum : BusTypeEnum.values()) {
                if (busTypeEnum.getValue().equals(value)) {
                    return busTypeEnum;
                }
            }

            return null;
        }
    }


    /**
     * 支付第三方渠道
     */
    public enum PayChannel implements EnumsCanDescribed {
        YB("0", "易宝"),
        LL("1", "连连"),
        HF("2", "恒丰"),
        ;

        private String desc;
        private String value;

        PayChannel(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public String getDesc() {
            return this.desc;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }


    /**
     * 卡所属渠道
     */
    public enum CardChannel implements EnumsCanDescribed {
        YB("0", "易宝"),
        LL("1", "连连"),
        HF("2", "恒丰"),
        ;

        private String desc;
        private String value;

        CardChannel(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public String getDesc() {
            return this.desc;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }
    public enum CallBackType implements EnumsCanDescribed {
    	PAY("0", "支付"),
    	WITH_DRAW("1", "提现"),
        ;

    	private String desc;
        private String value;

        CallBackType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @Override
        public String getDesc() {
            return this.desc;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }
}
