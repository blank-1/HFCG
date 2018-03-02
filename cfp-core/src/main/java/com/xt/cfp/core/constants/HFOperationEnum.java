package com.xt.cfp.core.constants;

import com.xt.cfp.core.enums.EnumsCanDescribed;

import java.util.Objects;

public enum HFOperationEnum implements EnumsCanDescribed {

    WITHDRAW("0", "提现"),
    RECHARGE("1", "充值"),

    FROZEN_PERSON_TO_PERSON("2", "冻结个人"),
    FROZEN_PERSON_TO_COMPANY("3", "冻结企业用户"),

    ALLOCATION_PERSON_TO_PERSON("5", "划拨个人到个人"),
    ALLOCATION_PERSON_TO_COMPANY("6", "划拨个人到企业"),
    ALLOCATION_COMPANY_TO_PERSON("7", "划拨企业到个人"),
    ALLOCATION_COMPANY_TO_COMPANY("701", "划拨企业到企业"),

    TRANSFER_COMPANY_TO_MERCHANT("4", "转账企业到商户"),
    TRANSFER_PERSON_TO_COMPANY("8", "转账个人到商户"),
    TRANSFER_MERCHANT_TO_PERSON("9", "转账商户到个人"),
    TRANSFER_COMPANY_TO_COMPANY("10", "转账商户到企业"),


    FROZEN_TO_FROZEN_PERSON_TO_PERSON("11", "冻结到冻结个人到个人"),
    FROZEN_TO_FROZEN_PERSON_TO_COMPANY("12", "冻结到冻结个人到企业"),
    FROZEN_TO_FROZEN_COMPANY_TO_PERSON("13", "冻结到冻结企业到个人"),

    ALLOCATION_TO_FROZEN_PERSON_TO_PERSON("14", "划拨预冻结个人到个人"),
    ALLOCATION_TO_FROZEN_PERSON_TO_COMPANY("15", "划拨预冻结个人到企业"),
    ALLOCATION_TO_FROZEN_COMPANY_TO_PERSON("16", "划拨预冻结企业到个人"),

    TRANSFER_TO_FROZEN_MERCHANT_TO_PERSON("17", "转账预冻结商户到个人"),
    TRANSFER_TO_FROZEN_MERCHANT_TO_COMPANY("18", "转账预冻结商户到企业"),

    UNFROZEN("20", "解冻");

    private final String value;
    private final String desc;

    HFOperationEnum(String value, String desc) {
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

    public static HFOperationEnum typeOf(Integer code) {
        if (code == null) {
            return null;
        }
        for (HFOperationEnum item : HFOperationEnum.values()) {
            if (Objects.equals(code.toString(), item.getValue())) {
                return item;
            }
        }
        return null;
    }
}
