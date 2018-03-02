package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 响应状态枚举类
 * @author HuYongkui
 *
 */
public enum ResponseStatusEnum implements EnumsCanDescribed {
	Unresponsive("0","未响应"),
	Success("1","成功"),
	Failue("2","失败");
	
	private final String value;
    private final String desc;

    private ResponseStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    public static ResponseStatusEnum value2Enum(String value) {
    	ResponseStatusEnum[] values = ResponseStatusEnum.values();
        for (ResponseStatusEnum anEnum:values) {
            if (anEnum.getValue() == (value)) {
                return anEnum;
            }
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
