package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public class UserMessage {

	public enum MessageReciverTypeEnum implements EnumsCanDescribed {

		/**
		 * 站内信接收人类型  0 所有 1 借款 2 出借  3 系统  // 4 站内信对个人 和导入用户
		 * 
		 */
		ALL("0","全部"),
		LOAN("1","借款人"),
        LEND("2","出借人"),
        SYSTEM("3","系统");

        private final String value;
        private final String desc;

        MessageReciverTypeEnum(String value, String desc) {
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
	
	public enum MessageTypeEnum implements EnumsCanDescribed {

		/**
		 * 0 系统消息 1 公告  2 站内消息
		 */
		SYSTEM("0","系统消息"),
		NOTICE("1","公告消息"),
		STATION("2","站内消息");

        private final String value;
        private final String desc;

        MessageTypeEnum(String value, String desc) {
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
