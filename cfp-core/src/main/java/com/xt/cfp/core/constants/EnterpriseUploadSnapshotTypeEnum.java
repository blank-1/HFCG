package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * 企业上传快照类型
 */
public enum EnterpriseUploadSnapshotTypeEnum implements EnumsCanDescribed {
    LEGAL_IDENTITY_CARD("0","法人身份证","法人身份证"),
    LEGAL_PERSONAL_CREDIT("1","法人个人征信","法人个人征信"),
    TAX_REGISTRATION_CERTIFICATE("2","税务登记证","税务登记证"),
    BUSINESS_LICENSE("3","营业执照","营业执照"),
    ORGANIZATION_CODE_CERTIFICATE("4","组织机构代码证","组织机构代码证"),
    OPENING_PERMIT("5","开户许可证","开户许可证"),
    THE_CAPITAL_VERIFICATION_REPORT("6","验资报告","验资报告"),
    BUSINESS_PREMISES_LEASE_CONTRACT("7","经营场所租凭合同","经营场所租凭合同"),
    NEARLY_THREE_YEARS_OF_FINANCIAL_STATEMENTS("8","近三年的财务报表","近三年的财务报表"),
    ;

    private final String value;
    private final String desc;
    private final String descVo;

    private EnterpriseUploadSnapshotTypeEnum(String value, String desc, String descVo) {
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

    public static EnterpriseUploadSnapshotTypeEnum getCustomerUploadSnapshotTypeByValue(String value){
    	EnterpriseUploadSnapshotTypeEnum[] aoes = EnterpriseUploadSnapshotTypeEnum.values();
        for (EnterpriseUploadSnapshotTypeEnum aoe :aoes){
            if(aoe.getValue().equals(value))
                return aoe;
        }
        return null;
    }

}
