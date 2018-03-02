package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 用户注册相关rediskey
 * */
public enum UserRegisterRedisKeyEnum implements EnumsCanDescribed {
	
	USER_REGISTER_COUNT_KEY("registerMessageCount","注册短信次数键值"),
	USER_REGISTER_MSG_KEY("register_message_","注册短信确认键值"),
	USER_REGISTER_IMG_KEY("register_imgcode_","获取图片验证码键值"),
	;	 
	
    private final String desc;
    private final String value;
    
    
    UserRegisterRedisKeyEnum(String value , String desc){
    	this.value = value ; 
    	this.desc = desc ;
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
