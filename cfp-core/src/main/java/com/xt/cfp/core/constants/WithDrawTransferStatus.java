package com.xt.cfp.core.constants;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.enums.EnumsCanDescribed;

/**
 * Created by luqinglin on 2015/6/16.
 */
public enum WithDrawTransferStatus implements EnumsCanDescribed {

    UN_TRANSFER("0","未打款"),
    TRANSFER_UNCOMMIT("1","未提交"),
    TRANSFER_ING("2","处理中"),
    TRANSFER_SUCCESS("3","打款成功"),
    TRANSFER_FAILE("4","打款失败"),
    ;


    private final String value;
    private final String desc;

    WithDrawTransferStatus(String value, String desc) {
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

    public static WithDrawTransferStatus getAccountOperateByDesc(String desc){
        WithDrawTransferStatus[] aoes = WithDrawTransferStatus.values();
        for (WithDrawTransferStatus aoe :aoes){
            if(aoe.getDesc().equals(desc))
                return aoe;
        }
        throw new SystemException(UserAccountErrorCode.UNSUPPORT_OPERATE_TYPE).set("desc", desc);
    }
}
