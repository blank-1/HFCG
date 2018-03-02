package com.xt.cfp.core.event;

/**
 * Created by Renyulin on 14-6-11 下午6:51.
 */
public class EventHandle {


    public static  void fireEvent(Event event) throws Exception {
        event.fire();
    }

}
