package com.xt.cfp.core.constants;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by wangxudong on 2015/12/23.
 */
public class WechatActConstants {

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

    public enum IsShareEnum implements EnumsCanDescribed {

        NOT_SHARE("1","未分享"),
        IS_SHARE("0","已分享"),;

        private final String value;
        private final String desc;

        IsShareEnum(String value, String desc) {
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
    
    public enum PrizeTypeEnum implements EnumsCanDescribed {

    	VIRTUAL("0","虚拟奖品"),
    	ENTITY("1","实物奖品"),;

        private final String value;
        private final String desc;

        PrizeTypeEnum(String value, String desc) {
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
