package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 企业信贷附件类型
 */
public enum EnterpriseCreditSnapshotTypeEnum implements EnumsCanDescribed {
	LEGAL_IDENTITY_CARD("0","法人身份证","法人身份证"),
	LEGAL_PERSON_CREDIT_REPORT("1","法人征信报告","法人征信报告"),
	JUDICIAL_INQUIRY("2","司法查询","司法查询"),
	CONTRACT_AGREEMENT("3","合同协议","合同协议"),
	PROJECT_DATA("4","项目资料","项目资料"),
	FIELD_ADJUSTMENT("5","实地尽调","实地尽调"),
	OTHER("6","其它","其它"),
	;

	private final String value;
    private final String desc;
    private final String descVo;

    private EnterpriseCreditSnapshotTypeEnum(String value, String desc, String descVo) {
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

    public static EnterpriseCreditSnapshotTypeEnum getEnterpriseCreditSnapshotTypeByValue(String value){
    	EnterpriseCreditSnapshotTypeEnum[] aoes = EnterpriseCreditSnapshotTypeEnum.values();
        for (EnterpriseCreditSnapshotTypeEnum aoe :aoes){
            if(aoe.getValue().equals(value))
                return aoe;
        }
        return null;
    }

}
