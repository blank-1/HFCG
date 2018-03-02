package com.xt.cfp.core.event.pojo;

import java.util.Date;

/**
 * Created by Renyulin on 14-6-13 上午11:10.
 */
public class EventTriggerInfo {
    private long eventTriggerInfoId;
    private long eventId;
    private String triggerType;
    private String triggerObjId;
    private Date happenTime;
    private char happenResult;
    private String eventName;//只是为了显示用，表中无此字段
    private String triggerObjCode;//只是为了显示用，表中无此字段

    public static char HAPPENRESULT_SUCCESS = '1';
    public static char HAPPENRESULT_FAILURE = '0';

    public String getTriggerObjCode() {
        return triggerObjCode;
    }

    public void setTriggerObjCode(String triggerObjCode) {
        this.triggerObjCode = triggerObjCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public long getEventTriggerInfoId() {
        return eventTriggerInfoId;
    }

    public void setEventTriggerInfoId(long eventTriggerInfoId) {
        this.eventTriggerInfoId = eventTriggerInfoId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerObjId() {
        return triggerObjId;
    }

    public void setTriggerObjId(String triggerObjId) {
        this.triggerObjId = triggerObjId;
    }

    public Date getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(Date happenTime) {
        this.happenTime = happenTime;
    }

    public char getHappenResult() {
        return happenResult;
    }

    public void setHappenResult(char happenResult) {
        this.happenResult = happenResult;
    }
}
