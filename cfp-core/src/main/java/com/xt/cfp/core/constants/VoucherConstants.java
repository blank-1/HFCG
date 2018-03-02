package com.xt.cfp.core.constants;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by yulei on 2015/7/7.
 */
public class VoucherConstants {

    public enum VoucherTypeEnum implements EnumsCanDescribed {
        FIXED("0","固定金额"),
        UN_FIXED("1","非固定金额"),
        ;

        private final String value;
        private final String desc;

        VoucherTypeEnum(String value, String desc) {
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

        public static VoucherTypeEnum getVoucherTypeEnumByValue(String value){
            VoucherTypeEnum[] aoes = VoucherTypeEnum.values();
            for (VoucherTypeEnum aoe :aoes){
                if(aoe.getValue().equals(value))
                    return aoe;
            }
            return null;
        }
    }

    public enum CardinalTypeEnum implements EnumsCanDescribed {
        LOAN("0","投标金额"),
        FINANCE("1","省心计划金额"),
        ;

        private final String value;
        private final String desc;

        CardinalTypeEnum(String value, String desc) {
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

        public static CardinalTypeEnum getCardinalTypeEnumEnumByValue(String value){
            CardinalTypeEnum[] aoes = CardinalTypeEnum.values();
            for (CardinalTypeEnum aoe :aoes){
                if(aoe.getValue().equals(value))
                    return aoe;
            }
            return null;
        }
    }
     public enum UsageScenario implements EnumsCanDescribed {
            WHOLE("0","全业务"),
            LOAN("1","投标使用"),
            FINANCE("2","购买省心计划使用"),
            WITHDRAW("3","提现专用"),
            ;

            private final String value;
            private final String desc;

            UsageScenario(String value, String desc) {
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

         public static UsageScenario getUsageScenarioEnumByValue(String value){
             UsageScenario[] aoes = UsageScenario.values();
             for (UsageScenario aoe :aoes){
                 if(aoe.getValue().equals(value))
                     return aoe;
             }
             return null;
         }
     }

    public enum VoucherProductStatus implements EnumsCanDescribed {
            UN_USAGE("0","停用"),
            USAGE("1","有效"),
            ;

            private final String value;
            private final String desc;

            VoucherProductStatus(String value, String desc) {
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

         public static VoucherProductStatus getVoucherProductStatusByValue(String value){
             VoucherProductStatus[] aoes = VoucherProductStatus.values();
             for (VoucherProductStatus aoe :aoes){
                 if(aoe.getValue().equals(value))
                     return aoe;
             }
             return null;
         }
    }

    public enum SourceType implements EnumsCanDescribed {
        PLATFORM_AWARD("0","平台奖励"),
        BIDING_AWARD("2","投标奖励"),
        OTHER("1","其他"),
        ;

        private final String value;
        private final String desc;

        SourceType(String value, String desc) {
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

        public static SourceType getSourceTypeByValue(String value){
            SourceType[] aoes = SourceType.values();
            for (SourceType aoe :aoes){
                if(aoe.getValue().equals(value))
                    return aoe;
            }
            return null;
        }
    }

    public enum VoucherFrontStatus implements EnumsCanDescribed {
        CAN_USAGE("0","可用"),
        CAN_NOT_USAGE("1","不可用"),

        ;

        private final String value;
        private final String desc;

        VoucherFrontStatus(String value, String desc) {
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

        public static VoucherFrontStatus getVoucherFrontStatusByValue(String value){
            VoucherFrontStatus[] aoes = VoucherFrontStatus.values();
            for (VoucherFrontStatus aoe :aoes){
                if(aoe.getValue().equals(value))
                    return aoe;
            }
            return null;
        }
    }

    public enum VoucherUseStatus implements EnumsCanDescribed {
        USAGE("0","使用"),
        FREEZE("1","冻结"),
        OVER_TIME("2","过期"),
        BACK("3","返还"),
        BING("4","绑定")
        ;


        private final String value;
        private final String desc;

        VoucherUseStatus(String value, String desc) {
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

        public static VoucherUseStatus getVoucherUseStatusByValue(String value){
            VoucherUseStatus[] aoes = VoucherUseStatus.values();
            for (VoucherUseStatus aoe :aoes){
                if(aoe.getValue().equals(value))
                    return aoe;
            }
            return null;
        }
    }

    public enum VoucherStatus implements EnumsCanDescribed {
        UN_USAGE("0","未使用"),
        USAGE("1","已使用"),
        FREEZE("2","冻结"),
        OVER_TIME("3","过期"),
        ;

        private final String value;
        private final String desc;

        VoucherStatus(String value, String desc) {
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

        public static VoucherStatus getVoucherStatusByValue(String value){
            VoucherStatus[] aoes = VoucherStatus.values();
            for (VoucherStatus aoe :aoes){
                if(aoe.getValue().equals(value))
                    return aoe;
            }
            return null;
        }
    }

}
