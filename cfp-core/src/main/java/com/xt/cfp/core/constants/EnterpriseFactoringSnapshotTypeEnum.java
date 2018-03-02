package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 企业保理附件类型
 */
public enum EnterpriseFactoringSnapshotTypeEnum implements EnumsCanDescribed {
	RELATED_CONTRACT("0","相关合同","相关合同"),
	DUE_DILIGENCE_REPORT("1","尽职调查报告","尽职调查报告"),
	FACTOR_OF_FACTORING("2","保理风控要件","保理风控要件"),
	OTHER("3","其它","其它"),
	;

	private final String value;
    private final String desc;
    private final String descVo;

    private EnterpriseFactoringSnapshotTypeEnum(String value, String desc, String descVo) {
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

    public static EnterpriseFactoringSnapshotTypeEnum getEnterpriseFactoringSnapshotTypeByValue(String value){
    	EnterpriseFactoringSnapshotTypeEnum[] aoes = EnterpriseFactoringSnapshotTypeEnum.values();
        for (EnterpriseFactoringSnapshotTypeEnum aoe :aoes){
            if(aoe.getValue().equals(value))
                return aoe;
        }
        return null;
    }

}
