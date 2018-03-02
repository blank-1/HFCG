package com.xt.cfp.core.constants;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by lenovo on 2015/6/18.
 */
public enum UserType implements EnumsCanDescribed {
    COMMON("0","普通用户",AccountConstants.AccountChangedTypeEnum.PLATFORM_USER),
    MEDIATOR("1","居间人",AccountConstants.AccountChangedTypeEnum.PLATFORM_USER),
    CHANNEL("2","渠道用户", AccountConstants.AccountChangedTypeEnum.BOND_SOURCE),
    SYSTEM("3","系统平台用户", AccountConstants.AccountChangedTypeEnum.SYSTEM),
    GUARANTEE_COMPANY("4","担保公司用户", AccountConstants.AccountChangedTypeEnum.GUARANTEE_COMPANY),
    LINE("5","线下用户",AccountConstants.AccountChangedTypeEnum.PLATFORM_USER),
    ENTERPRISE("6","企业虚拟用户",AccountConstants.AccountChangedTypeEnum.PLATFORM_USER),
    ENTERPRISE_USER("7","企业人员用户",AccountConstants.AccountChangedTypeEnum.PLATFORM_USER),
    ;

    private final String value;
    private final String desc;
    private final AccountConstants.AccountChangedTypeEnum accountChangedType;

    private UserType(String value, String desc, AccountConstants.AccountChangedTypeEnum accountChangedType1) {
        this.value = value;
        this.desc = desc;
        this.accountChangedType = accountChangedType1;
    }

    public AccountConstants.AccountChangedTypeEnum getAccountChangedType() {
        return accountChangedType;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static UserType getUserTypeByValue(String value){
        UserType[] aoes = UserType.values();
        for (UserType aoe :aoes){
            if(aoe.getValue().equals(value))
                return aoe;
        }
        throw new SystemException(UserErrorCode.USERTYPE_NOT_EXIST).set("value", value);
    }
}
