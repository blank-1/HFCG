package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public enum CommonRedisKeyEnum implements EnumsCanDescribed  {
	POS_RECHARGE("POS_RECHARGE_","POS刷卡key"),
	;	 
	
    private final String desc;
    private final String value;
    
    
    CommonRedisKeyEnum(String value , String desc){
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
