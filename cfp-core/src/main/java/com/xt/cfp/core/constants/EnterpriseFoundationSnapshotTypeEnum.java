package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 企业保理附件类型
 */
public enum EnterpriseFoundationSnapshotTypeEnum implements EnumsCanDescribed {
	RELATED_CONTRACT("0","相关合同","相关合同"),
	;

	private final String value;
    private final String desc;
    private final String descVo;

    private EnterpriseFoundationSnapshotTypeEnum(String value, String desc, String descVo) {
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

    public static EnterpriseFoundationSnapshotTypeEnum getEnterpriseFoundationSnapshotTypeByValue(String value){
    	EnterpriseFoundationSnapshotTypeEnum[] aoes = EnterpriseFoundationSnapshotTypeEnum.values();
        for (EnterpriseFoundationSnapshotTypeEnum aoe :aoes){
            if(aoe.getValue().equals(value))
                return aoe;
        }
        return null;
    }

}
