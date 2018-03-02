package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 交易操作类型枚举
 * @author Windows User
 *
 */
public enum TradeOperateEnum implements EnumsCanDescribed {
	Flowage("0","划拨"),
	Thansfer("1","转账"),
	Freeze("2","冻结"),
	Initiative("3","冻结到冻结"),
	Thaw("4","解冻");
	
	private final String value;
    private final String desc;

    private TradeOperateEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    public static TradeOperateEnum value2Enum(String value) {
    	TradeOperateEnum[] values = TradeOperateEnum.values();
        for (TradeOperateEnum anEnum:values) {
            if (anEnum.getValue().equals(value)) {
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
