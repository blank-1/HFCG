package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public class WechatNoticeConstants {
	
	/**
	 * 状态（0.有效；1.无效）
	 */
	public enum WechatNoticeStateEnum implements EnumsCanDescribed {
		VALID("0", "有效"),
		UN_VALID("1", "无效"), 
		;
		
		private final String value;
		private final String desc;
		
		private WechatNoticeStateEnum(String value, String desc) {
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
		
		public char value2Char() {
			return value.charAt(0);
		}
		
		public static WechatNoticeStateEnum getWechatNoticeStateEnumByValue(String value){
			WechatNoticeStateEnum[] wechatNoticeStateEnums = WechatNoticeStateEnum.values();
	        for (WechatNoticeStateEnum wnse : wechatNoticeStateEnums){
	            if (wnse.getValue().equals(value))
	                return wnse;
	        }
	        return null;
	    }
		
	}
	
}
