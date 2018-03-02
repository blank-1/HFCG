package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 現金貸-附件类型
 */
public enum CashCreditTypeEnum implements EnumsCanDescribed {
	LEGAL_IDENTITY_CARD("0","债权清单","债权清单"),
	OTHER("1","其它","其它"),
	;

	private final String value;
    private final String desc;
    private final String descVo;

    private CashCreditTypeEnum(String value, String desc, String descVo) {
        this.value = value;
        this.desc = desc;
        this.descVo = descVo;
    }

    public String getDescVo() {
        return descVo;
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

    public static CashCreditTypeEnum byValue(String value){
    	CashCreditTypeEnum[] aoes = CashCreditTypeEnum.values();
        for (CashCreditTypeEnum aoe :aoes){
            if(aoe.getValue().equals(value))
                return aoe;
        }
        return null;
    }

}
