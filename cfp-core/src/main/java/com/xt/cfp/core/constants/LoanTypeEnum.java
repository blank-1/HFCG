package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by lenovo on 2015/6/18.
 */
public enum LoanTypeEnum implements EnumsCanDescribed {
    LOANTYPE_CREDIT("0","信贷"),
    LOANTYPE_HOUSE("1","房贷"),
    LOANTYPE_ENTERPRISE_CAR("2","企业车贷"),
    LOANTYPE_ENTERPRISE_CREDIT("3","企业信贷"),
    LOANTYPE_ENTERPRISE_FACTORING("4","企业保理"),
    LOANTYPE_ENTERPRISE_FOUNDATION("5","基金"),
    LOANTYPE_ENTERPRISE_PLEDGE("6","企业标"),
    LOANTYPE_DIRECT_HOUSE("7","个人房产直投"),
    LOANTYPE_CREDIT_CAR_PEOPLE("8","个人信用车贷"),
    LOANTYPE_CASH_LOAN("9","现金贷"),
    ;


    private final String value;
    private final String desc;

    private LoanTypeEnum(String value, String desc) {
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

    public static LoanTypeEnum getLoanTypeEnumByCode(String code){
        LoanTypeEnum[] loanTypeEnums = LoanTypeEnum.values();
        for (LoanTypeEnum lte :loanTypeEnums){
            if (lte.getValue().equals(code))
                return lte;
        }
        return null;
    }
}
