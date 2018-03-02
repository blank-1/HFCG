package com.xt.cfp.core.constants;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by ren yulin on 15-7-10.
 */
public enum CRMatchRuleEnum implements EnumsCanDescribed {
    MAXLIMIT("1","期限最大化排序"),//根据省心计划期限，找到比剩余期限小且最接近剩余期限的，再次比剩余期限大，且最接近剩余期限的排序方式
    ;


    private final String value;
    private final String desc;

    private CRMatchRuleEnum(String value, String desc) {
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

    public static CRMatchRuleEnum getEnumByValue(String value) {
        CRMatchRuleEnum[] values = CRMatchRuleEnum.values();
        CRMatchRuleEnum theEnum = null;
        for (CRMatchRuleEnum enums:values) {
            if (enums.getValue().equals(value)) {
                theEnum = enums;
            }
        }
        if (theEnum == null) {
            throw new SystemException(UserAccountErrorCode.UNSUPPORT_OPERATE_TYPE).set("value", value);
        }
        return theEnum;
    }
}