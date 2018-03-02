package com.xt.cfp.core.service;


import com.xt.cfp.core.service.message.Message;

/**
 * Created by lenovo on 2015/8/17.
 */
public interface MessagePublicService {

    /**
     * 发布消息
     * @param message
     * @param channel 发布渠道
     */
    void publish(Message message,String channel);
}
