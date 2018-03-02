package com.xt.cfp.core.service.message;

import redis.clients.jedis.JedisPubSub;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Created by lenovo on 2015/8/17.
 */
public class MessageProcesser extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        //消息处理
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(
                    message.getBytes("ISO-8859-1"));//此处指定字符集将字符串编码成字节数组，此处的字符集需要与发布时的字符集保持一致
            ObjectInputStream ois = new ObjectInputStream(bis);
            Message msg = (Message) ois.readObject();
            //消息处理
            msg.getMessageBody().handle();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }
}
