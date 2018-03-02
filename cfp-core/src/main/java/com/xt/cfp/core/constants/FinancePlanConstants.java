package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-6-30.
 */
public class FinancePlanConstants {

    public enum MatchTimesEnum implements EnumsCanDescribed {
        FIRST_MATCH("0","首次匹配"),
        UN_FIRST_MATCH("1","非首次匹配"),
        ;

        private final String value;
        private final String desc;

        MatchTimesEnum(String value, String desc) {
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
