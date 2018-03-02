package com.xt.cfp.message;

import com.xt.cfp.core.service.message.*;
import com.xt.cfp.core.service.message.MessageChannel;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by lenovo on 2015/8/17.
 */
@Component
public class MessageListener implements InitializingBean {

    @Autowired
    private RedisCacheManger redisCacheManger;

    @Override
    public void afterPropertiesSet() throws Exception {

        //渠道数组
        com.xt.cfp.core.service.message.MessageChannel[] values = com.xt.cfp.core.service.message.MessageChannel.values();
        for (final MessageChannel mc:values){
            //渠道数组
           new Thread(new Runnable() {
                @Override
                public void run() {
                    JedisPool pool = redisCacheManger.getPool();
                    Jedis jedis = pool.getResource();
                    //订阅所有渠道的消息
                    MessageProcesser mp = new MessageProcesser();
                    jedis.subscribe(mp,mc.getValue());
                }
            }).start();
        }

    }
}
