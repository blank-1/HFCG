package com.xt.cfp.core.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间段
 * User: yulei
 * Date: 13-10-31
 * Time: 下午1:27
 */
public final class TimeInterval {

    public Date startTime;

    public Date endTime;

    public TimeInterval(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private TimeInterval() {
    }

    public boolean isBetween(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(startTime);
        long startM = instance.getTimeInMillis();
        instance.setTime(endTime);
        long endM = instance.getTimeInMillis();
        instance.setTime(date);
        long dateM = instance.getTimeInMillis();
        if (startM<=dateM&&dateM<=endM){
            return true;
        }
        return false;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
