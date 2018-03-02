package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 交易业务类型枚举类
 * @author HuYongkui
 *
 */
public enum TradeTypeEnum implements EnumsCanDescribed {
	UserRegist("0","个人用户开户"),
	CorpRegist("1","法人用户开户"),
	Tender("2","投标"),
	Loss("3","流标"),
	Full("4","满标放款"),
	Thansfer("5","转账"),
	Repayment("6","还款"),
	Other("8","其他");
	
	private final String value;
    private final String desc;

    private TradeTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    public static TradeTypeEnum value2Enum(String value) {
    	TradeTypeEnum[] values = TradeTypeEnum.values();
        for (TradeTypeEnum anEnum:values) {
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
