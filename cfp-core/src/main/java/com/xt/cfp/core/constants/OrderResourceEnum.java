package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public enum OrderResourceEnum implements EnumsCanDescribed{
	
    MAPPING_TYPE_LEND("0","LENDORDER"),
    MAPPING_TYPE_RECHARGE("1","RECHARGEORDER"),
    MAPPING_TYPE_WITHDRAW("2","WITHDRAWORDER"),
    ;
	
	private final String value;
    private final String desc;
    
    private OrderResourceEnum(String value, String desc) {
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
