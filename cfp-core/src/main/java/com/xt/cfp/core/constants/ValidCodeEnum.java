package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public enum ValidCodeEnum implements EnumsCanDescribed {
	INVITE_FRIEND("0","邀请好友"),
	CHANGE_MOBILE("1","修改手机"),
	REGISTER_USER("2","用户注册"),
	RESET_TRANSACTION_PWD("3","重设交易密码"),
	FIND_PWD("4","找回密码"),
	WITHDRAW_MSG("5","提现短信"),
	VALID_MSG("6","模拟登录短信"),
	;
	
	private final String value;
    private final String desc;
	
    private ValidCodeEnum(String value, String desc){
    	this.value = value;
        this.desc = desc;
    }
    
    public static ValidCodeEnum getValidCodeEnumByType(Integer type){
    	switch(type){
    		case 0:
    			return ValidCodeEnum.INVITE_FRIEND;
    		case 1:
    			return ValidCodeEnum.CHANGE_MOBILE;
    		case 2:
    			return ValidCodeEnum.REGISTER_USER;
    		case 3:
    			return ValidCodeEnum.RESET_TRANSACTION_PWD;
    		case 4:
    			return ValidCodeEnum.FIND_PWD;
    		case 5:
    			return ValidCodeEnum.WITHDRAW_MSG;
    	}
		return null;
    	
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
