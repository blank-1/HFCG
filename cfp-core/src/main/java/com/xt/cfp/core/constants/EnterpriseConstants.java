package com.xt.cfp.core.constants;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by yulei on 2015/7/7.
 */
public class EnterpriseConstants {

    public enum CoLtdStatusEnum implements EnumsCanDescribed {

        NORMAL("0","正常"),
        UN_USAGE("1","禁用"),;

        private final String value;
        private final String desc;

        CoLtdStatusEnum(String value, String desc) {
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
