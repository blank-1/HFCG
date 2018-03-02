package com.xt.cfp.core.event.pojo;

/**
 * Created by Renyulin on 14-6-13 上午11:08.
 */
public class EventInfo {
    private long eventId;
    private String eventName;
    private char eventType;
    private String implClass;

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public char getEventType() {
        return eventType;
    }

    public void setEventType(char eventType) {
        this.eventType = eventType;
    }

    public String getImplClass() {
        return implClass;
    }

    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }
}
