package com.xt.cfp.core.constants;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by admin on 15/12/15.
 */
public final class CreditorRightsConstants {

    /**
     * 债权转让状态
     */
    public static enum CreditorRightsTurnStateEnum implements EnumsCanDescribed{
        TRANSFERRING("1", "转让中"),
        TRANSFERRED("2", "已转出")
        ;

        private String value;
        private String desc;

        CreditorRightsTurnStateEnum(String value, String desc) {
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

        public char value2Char() {
            return this.value.toCharArray()[0];
        }
    }

    /**
     * 债权状态
     */
    public static enum CreditorRightsStateEnum implements EnumsCanDescribed {
        EFFECTIVE("0","已生效"),
//        REPAYING("1","还款中"),
        TURNOUT("2","已转出"),
        COMPLETE("3","已结清"),
        APPLYTURNOUT("5","申请转出"),
        TURNOUT_SYSTEMPREPAY("6","已转出(平台垫付)"),
        EARLYCOMPLETE("7","提前结清"),
        TRANSFERING("8","转让中"),
        LOCKED("9","已锁定"),
        ;


        private final String value;
        private final String desc;

        CreditorRightsStateEnum(String value, String desc) {
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

        public  char value2Char() {
            return value.charAt(0);
        }

        public static CreditorRightsStateEnum getCreditorRightsStateEnumByValue(String value){
            CreditorRightsStateEnum[] crseArray = CreditorRightsStateEnum.values();
            for (CreditorRightsStateEnum crse : crseArray){
                if(crse.getValue().equals(value))
                    return crse;
            }
            throw new SystemException(UserAccountErrorCode.UNSUPPORT_OPERATE_TYPE).set("value", value);
        }

        /**
         * 债权还款明细还清状态
         */
        public enum RightsRepaymentDetailIsPayOffEnum implements EnumsCanDescribed {
            ISPAYOFF_YES("1", "已还清"),
            ISPAYOFF_NO("0", "未还清"),
            ;

            private final String value;
            private final String desc;

            RightsRepaymentDetailIsPayOffEnum(String value, String desc) {
                this.value = value;
                this.desc = desc;
            }

            public  char value2Char() {
                return value.charAt(0);
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public String getDesc() {
                return desc;
            }
        }

        /**
         * 债权明细状态
         */
        public enum RightsRepaymentDetailStateEnum implements EnumsCanDescribed {
            UNCOMPLETE("0","未还款"),
            PART("1","部分还款"),
            COMPLETE("2","已还清"),
            DEFAULT("3","逾期"),
            BEFORE_COMPLETE("4","提前还款"),
            TURNOUT("5","已转出"),
            SYSTEMPREPAY_INTEREST("6","平台垫付利息"),
            DELETED("7","已删除"),
            ;
            private final String value;
            private final String desc;

            RightsRepaymentDetailStateEnum(String value, String desc) {
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
            public  char value2Char() {
                return value.charAt(0);
            }

            public static RightsRepaymentDetailStateEnum getRightsRepaymentDetailStateEnumByValue(String value){
                RightsRepaymentDetailStateEnum[] rrdseArray = RightsRepaymentDetailStateEnum.values();
                for (RightsRepaymentDetailStateEnum rrdse : rrdseArray){
                    if(rrdse.getValue().equals(value))
                        return rrdse;
                }
                throw new SystemException(UserAccountErrorCode.UNSUPPORT_OPERATE_TYPE).set("value", value);
            }
        }

        /**
         * 债权转让申请类型
         */
        public enum CreditorRightsTransferAppType implements EnumsCanDescribed {
            MANUAL("0", "用户手动转让"),
            FINANCE_EXIT("1", "退出省心计划转让")
            ;

            private String value;
            private String desc;

            CreditorRightsTransferAppType(String value, String desc) {
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
         * 债权转让申请状态
         */
        public enum CreditorRightsTransferAppStatus implements EnumsCanDescribed {
            TRANSFERRING("0", "转让中"),
            SUCCESS("1", "转让成功"),
            OVERDUE("2", "过期"),
            CANCEL("3", "撤销"),
            ;


            private String value;
            private String desc;

            CreditorRightsTransferAppStatus(String value, String desc) {
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
            
            public static CreditorRightsTransferAppStatus getCreditorRightsTransferAppStatusByValue(String value){
            	CreditorRightsTransferAppStatus[] crseArray = CreditorRightsTransferAppStatus.values();
                for (CreditorRightsTransferAppStatus crse : crseArray){
                    if(crse.getValue().equals(value))
                        return crse;
                }
                throw new SystemException(UserAccountErrorCode.UNSUPPORT_OPERATE_TYPE).set("value", value);
            }
        }

        /**
         * 债权转让明细状态
         */
        public enum CreditorRightsTransferDetailStatus implements EnumsCanDescribed {
            BUYING("0", "支付中"),
            SUCCESS("1", "支付成功"),
            FAIL("2", "支付失败"),
            ;


            private String value;
            private String desc;

            CreditorRightsTransferDetailStatus(String value, String desc) {
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
}

