package com.xt.cfp.core.service.message;

import java.io.Serializable;

/**
 * Created by lenovo on 2015/8/17.
 */
public class Message implements Serializable {

    /**
     * 消息channel
     */
    private String channel;

    /**
     * 消息体
     */
    private MessageBody messageBody;

    public Message(MessageBody messageBody) {
        this.messageBody = messageBody;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(MessageBody messageBody) {
        this.messageBody = messageBody;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
