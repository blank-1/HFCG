package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 企业车贷附件类型
 */
public enum EnterpriseCarLoanSnapshotTypeEnum implements EnumsCanDescribed {
	CONTRACT_AGREEMENT("0","合同协议","合同协议"),
	PROJECT_DATA("1","项目资料","项目资料"),
	PLATFORM_INTERNAL_VEHICLE_MONITORING("2","平台内部车辆监管单","平台内部车辆监管单"),
	VEHICLE_REPLACEMENT("3","车辆置换书","车辆置换书"),
	FIELD_ADJUSTMENT("4","实地尽调","实地尽调"),
	;

	private final String value;
    private final String desc;
    private final String descVo;

    private EnterpriseCarLoanSnapshotTypeEnum(String value, String desc, String descVo) {
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

    public static EnterpriseCarLoanSnapshotTypeEnum getEnterpriseCarLoanSnapshotTypeByValue(String value){
    	EnterpriseCarLoanSnapshotTypeEnum[] aoes = EnterpriseCarLoanSnapshotTypeEnum.values();
        for (EnterpriseCarLoanSnapshotTypeEnum aoe :aoes){
            if(aoe.getValue().equals(value))
                return aoe;
        }
        return null;
    }

}
