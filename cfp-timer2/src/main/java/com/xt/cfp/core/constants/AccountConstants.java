package com.xt.cfp.core.constants;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by yulei on 2015/7/7.
 */
public class AccountConstants {

    public enum AccountChangedTypeEnum implements EnumsCanDescribed {
        //    LEND("LendOrder","出借订单"),
//    LOAN("LoanApplication","借款申请"),
//    SYSTEM("UserAccount","平台账户"),
//    BOND_SOURCE("BondSource","渠道账户"),
//    RECHARGE("SourceRecharge","充值单"),
//    WITHDRAW("WithDraw","提现单"),
        LEND("0","出借订单"),
        LOAN("1","借款申请"),
        SYSTEM("2","平台账户"),
        BOND_SOURCE("3","渠道账户"),
        RECHARGE("4","充值单"),
        WITHDRAW("5","提现单"),
        GUARANTEE_COMPANY("6","担保公司账户"),
        PAYORDER("7", "支付订单"),
        PLATFORM_USER("8","平台用户"),
        CASH_ACCOUNT("9","资金账户"),
        DATA_TRANS("10", "数据迁移"),
        TIMER("11", "系统定时器"),
        ENTERPRISE_SOURCE("12", "企业账户"),
        CORRECTION_PRECISION("13", "修正精度"),
        ;

        private final String value;
        private final String desc;

        AccountChangedTypeEnum(String value, String desc) {
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

    public enum AccountOperateEnum implements EnumsCanDescribed {

        INCOM("1","收入"),
        PAY("2","支出"),
        PAY_FROZEN("3","冻结资金支出"),
        FREEZE("4","冻结"),
        UNFREEZE("5","解冻"),
        ;


        private final String value;
        private final String desc;

        AccountOperateEnum(String value, String desc) {
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

        public static AccountOperateEnum getAccountOperateByValue(String value){
            AccountOperateEnum [] aoes = AccountOperateEnum.values();
            for (AccountOperateEnum aoe :aoes){
                if(aoe.getValue().equals(value))
                    return aoe;
            }
            throw new SystemException(UserAccountErrorCode.UNSUPPORT_OPERATE_TYPE).set("value", value);
        }
    }

    public enum AccountStatusEnum implements EnumsCanDescribed {
        NORMAL("0", "正常"),
        ABNORMAL("1", "异常"),
        FROZEN("2", "冻结");

        private final String value;
        private final String desc;

        AccountStatusEnum(String value, String desc) {
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

    public enum AccountTypeEnum implements EnumsCanDescribed {
        BORROW_ACCOUNT("1","借款人账户"),
        LENDER_ACCOUNT("2","出借人账户"),
        PLATFORM_ACCOUNT("4","平台收支账户"),
        ORDER_ACCOUNT("5","订单账户"),
        PLATFORM_PAYMENT("6", "平台逾期垫付资金账户"),
        PLATFORM_RISK("7", "平台风险准备金账户"),
        PLATFORM_OPERATING("8", "平台运营账户"),
        ;


        private final String value;
        private final String desc;

        AccountTypeEnum(String value, String desc) {
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

    public enum BusinessTypeEnum implements EnumsCanDescribed {

        FEESTYPE_RECHARGE("1", "充值"),
        FEESTYPE_WITHDRAWCASH("2", "提现"),
        FEESTYPE_BUYRIGHTS("3", "购买债权"),
        FEESTYPE_FINANCING("4", "购买理财"),
        FEESTYPE_MANAGER("5", "管理费"),
        FEESTYPE_EXIST("6", "退出费"),
        FEESTYPE_WORKFLOWFEES("7", "平台费"),
        FEESTYPE_DELAYFEES("8", "逾期费"),
        FEESTYPE_TOBUYFREEZEN("9", "投标冻结"),
        FEESTYPE_WITHDRAWCASHFREEZEN("10", "提现冻结"),
        FEESTYPE_BALANCECHANGEINTO("11", "资金转入"),
        FEESTYPE_BALANCECHANGEOUT("12", "资金转出"),
        FEESTYPE_EARLYWITHDRAWALPENALTY("13", "提前退出违约金"),
        FEESTYPE_AUTHENTICATIONFEE("14", "身份验证费用"),
        FEESTYPE_WITHDRAWCASHFEE("15", "提现手续费"),
        FEESTYPE_SYSTEMTRANSFER("16", "平台垫付"),
        FEESTYPE_REPAYMENT("17", "还款"),
        FEESTYPE_INCOMEOVERFLOW("18", "溢出费用收入"),
        FEESTYPE_PAYOVERFLOW("19", "溢出费用支出"),
        FEESTYPE_INCOMEDEFAULTINTEREST("20", "回款收入利息"),
        FEESTYPE_INCOMEREPAYMENT("21", "回款收入本金"),
        FEESTYPE_CONTRACTMATURITYOUT("22", "合同到期转出"),
        FEESTYPE_CONTRACTMATURITYTRANSFER("23", "合同到期转入"),
        FEESTYPE_REPAYMENTREEZEN("24", "还款冻结"),
        FEESTYPE_INCOMEDEFAULTINTEREST_FROZEN("25", "回款利息冻结"),
        FEESTYPE_WITHDRAWCASH_FAILURE("26", "提现失败"),
        FEESTYPE_RETURNPREPAY("27", "垫付利息回款"),
        FEESTYPE_UNFROZEN_SECURITY("28", "解冻保证金"),
        FEESTYPE_UNFROZEN_PREPAY_INTEREST("29", "解冻预付利息"),
        FEESTYPE_SECURITY_INTO("30", "保证金收入"),
        FEESTYPE_SECURITY_OUT("31", "保证金支出"),
        FEESTYPE_ACCURACYERROR("32", "精度误差"),
        FEESTYPE_EARLYWITHDRAWALPENALTY_INCOME("33", "提前退出违约金"),
        FEESTYPE_HEDEG("34", "账户对冲"),
        FEESTYPE_WITHDRAWCASHUNFREEZEN("35", "提现解冻"),
        FEESTYPE_BUYFINANCEFREEZEN("36", "购买理财冻结"),
        FEESTYPE_BUYFINANCEUNFREEZEN("37", "购买理财失败解冻"),
        FEESTYPE_BUYFINANCE_TRANSFER("38", "购买理财"),
        FEESTYPE_PAYMENTDEFAULTINTEREST("39", "借款申请罚息"),
        FEESTYPE_BIDLOANFREEZEN("40", "投标借款冻结"),
        FEESTYPE_BIDLOAN_TRANSFER("41", "投标借款"),
        FEESTYPE_PAY_AWARD("42", "平台支出奖励"),
        FEESTYPE_INCOME_AWARD("43", "出借人收入奖励"),
        FEESTYPE_SYS_DATATRANS_INCOME("44", "平台数据迁移转账"),
        FEESTYPE_OFFLINE_RECHARGE("45", "线下充值"),
        FEESTYPE_FAIL_LOAN("46", "流标"),
        VOUCHER_PLATFORM_PAY("47", "平台财富券支出"),
        VOUCHER_INCOME("48", "用户财富券变现充值"),
        VOUCHER_PAY("49", "用户财富券返现支出"),
        VOUCHER_PLATFORM_BACK_INCOME("50", "平台财富券返现充值"),
        CORRECTION_PRECISION("51", "精度修复"),
        FEESTYPE_INCOME_DELAYDEFAULTINTEREST("52", "回款收入罚息"),
        LENDER_FEESTYPE_SYSTEMTRANSFER("53", "出借人垫付"),
        ;


        private final String value;
        private final String desc;

        BusinessTypeEnum(String value, String desc) {
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

        public static BusinessTypeEnum getBusinessTypeByValue(String value){
            BusinessTypeEnum[] aoes = BusinessTypeEnum.values();
            for (BusinessTypeEnum aoe :aoes){
                if(aoe.getValue().equals(value))
                    return aoe;
            }
            throw new SystemException(UserAccountErrorCode.UNSUPPORT_BUSINESS_TYPE).set("value", value);
        }
    }

    public enum VisiableEnum  implements EnumsCanDescribed {
        DISPLAY("1","显示"),
        HIDDEN("0","隐藏"),
        ;

        private final String value;
        private final String desc;

        VisiableEnum(String value, String desc) {
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

    public enum OwnerTypeEnum implements EnumsCanDescribed {
        LOAN("0", "债权"),
        USER("1", "用户"),
        ORDER("2", "订单"),
        SYS_ACC("3", "平台账户"),
        CASH_ACC("4", "资金账户"),
        CORRECTION_PRECISION("5", "修正精度"),
        ;

        private String desc;
        private String value;

        OwnerTypeEnum(String value, String desc) {
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
