package com.xt.cfp.core.constants;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by luqinglin on 2015/6/16.
 */
public enum WithDrawType implements EnumsCanDescribed {

    SOURCE_WITHDRAW("0","渠道提现", AccountConstants.AccountChangedTypeEnum.BOND_SOURCE),

    ;


    private final String value;
    private final String desc;
    private final AccountConstants.AccountChangedTypeEnum accountChangedType;

    WithDrawType(String value, String desc, AccountConstants.AccountChangedTypeEnum accountChangedType) {
        this.value = value;
        this.desc = desc;
        this.accountChangedType = accountChangedType;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getValue() {
        return value;
    }

    public AccountConstants.AccountChangedTypeEnum getAccountChangedType() {
        return accountChangedType;
    }

}
