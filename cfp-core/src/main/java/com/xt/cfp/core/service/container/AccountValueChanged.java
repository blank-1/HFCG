package com.xt.cfp.core.service.container;

import com.xt.cfp.core.annotation.AllowNull;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulei on 2015/6/11.
 */
public class AccountValueChanged {

    protected Long accId;
    protected BigDecimal changeValue;
    protected BigDecimal changeValue2;
    protected String changeType;
    protected String busType;
    protected String fromType;
    protected String isVisbled;
    protected Long fromId;
    protected String ownerType;
    protected Long ownerId;
    protected Date changeTime;
    protected String desc;
    @AllowNull
    private CallBack callBack;

    public AccountValueChanged(Long accId, BigDecimal changeValue, BigDecimal changeValue2, String changeType, String busType, String fromType, String isVisbled, Long fromId, String ownerType, Long ownerId, Date changeTime, String desc, boolean needCheck) {
        this.accId = accId;
        this.changeValue = changeValue;
        this.changeValue2 = changeValue2;
        this.changeType = changeType;
        this.busType = busType;
        this.fromType = fromType;
        this.isVisbled = isVisbled;
        this.fromId = fromId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.changeTime = changeTime;
        this.desc = desc;
        this.needCheck = needCheck;
    }
    public AccountValueChanged(Long accId, BigDecimal changeValue, BigDecimal changeValue2, String changeType, String busType, String fromType, String isVisbled, Long fromId, String ownerType, Long ownerId, Date changeTime, String desc, boolean needCheck,CallBack callBack) {
        this.accId = accId;
        this.changeValue = changeValue;
        this.changeValue2 = changeValue2;
        this.changeType = changeType;
        this.busType = busType;
        this.fromType = fromType;
        this.isVisbled = isVisbled;
        this.fromId = fromId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.changeTime = changeTime;
        this.desc = desc;
        this.needCheck = needCheck;
        this.callBack = callBack;
    }

    protected boolean needCheck; //是否需要检查支出后，价值是否为负

    public boolean isNeedCheck() {
        return needCheck;
    }

    public void setNeedCheck(boolean needCheck) {
        this.needCheck = needCheck;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigDecimal getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(BigDecimal changeValue) {
        this.changeValue = changeValue;
    }

    public BigDecimal getChangeValue2() {
        return changeValue2;
    }

    public void setChangeValue2(BigDecimal changeValue2) {
        this.changeValue2 = changeValue2;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Long getAccId() {
        return accId;
    }

    public void setAccId(Long accId) {
        this.accId = accId;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getIsVisbled() {
        return isVisbled;
    }

    public void setIsVisbled(String isVisbled) {
        this.isVisbled = isVisbled;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
