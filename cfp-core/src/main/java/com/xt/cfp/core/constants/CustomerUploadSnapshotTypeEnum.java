package com.xt.cfp.core.constants;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.UserAccountErrorCode;
import com.xt.cfp.core.enums.EnumsCanDescribed;

import java.util.List;

/**
 * Created by ren yulin on 15-6-30.
 */
public enum CustomerUploadSnapshotTypeEnum implements EnumsCanDescribed {
    IDCARD("0","身份证", "身份认证"),
    PAY_WARTER("1","工资流水", "工资流水"),
    PERSON_CREDIT_REPORT("2","个人信用报告", "信用报告"),
    MERRIED_CERTIFICATE("3","结婚证", "结婚证"),
    SOCIAL_SCREESHOT  ("4","社保截图", "社保截图"),
    INCOME_PROOF("5","收入证明", "收入证明"),
    HOUSER_CERTIFICATE("6","房产证","房产认证"),
    EARTH_CERTIFICATE("7","土地证", "土地证"),
    HEAD_ICON("8","头像", "头像"),
    BID_INTRDUCE("9","标的说明", "标的说明"),
    CONTRACT_AGGREMENT("10","合同协议", "合同协议"),
    CAR_IMAGE("11","车辆照片", "车辆照片"),
    CERTIFICATE_ABOUT("12","证件相关", "证件相关"),

    ;


    private final String value;
    private final String desc;
    private final String descVo;


    private CustomerUploadSnapshotTypeEnum(String value, String desc, String descVo) {
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

    public static CustomerUploadSnapshotTypeEnum getCustomerUploadSnapshotTypeByValue(String value){
        CustomerUploadSnapshotTypeEnum[] aoes = CustomerUploadSnapshotTypeEnum.values();
        for (CustomerUploadSnapshotTypeEnum aoe :aoes){
            if(aoe.getValue().equals(value))
                return aoe;
        }
        return null;
    }
}
