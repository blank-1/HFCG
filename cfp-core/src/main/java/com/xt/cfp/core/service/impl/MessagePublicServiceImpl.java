package com.xt.cfp.core.service.impl;

import com.xt.cfp.core.service.MessagePublicService;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by lenovo on 2015/8/17.
 */
@Service
public class MessagePublicServiceImpl implements MessagePublicService {

    @Autowired
    private RedisCacheManger redisCacheManger;

    @Override
    public void publish(Message message,String channel) {
        try {
            message.setChannel(channel);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(message);
            String msg1 = baos.toString("ISO-8859-1");//指定字符集将字节流解码成字符串，否则在订阅时，转换会有问题。
            JedisPool pool = redisCacheManger.getPool();
            Jedis jedis = pool.getResource();
            //发布消息
            jedis.publish(channel,msg1);
            //资源返回
            pool.returnResource(jedis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
