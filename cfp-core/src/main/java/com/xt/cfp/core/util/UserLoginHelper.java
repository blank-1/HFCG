package com.xt.cfp.core.util;

import com.xt.cfp.core.service.redis.RedisCacheManger;

/**
 * Created by john on 2016/12/2.
 */
public class UserLoginHelper {

    private static final String CACHE_KEY = "WECHAT_LOGIN_SESSION";

    public static void saveUserJsession(RedisCacheManger redisCacheManger, String jsession, String userId) {
        try {
            redisCacheManger.setRedisCacheInfo(CACHE_KEY + "_" + userId, jsession);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUserJsession(RedisCacheManger redisCacheManger, String userId) {
        return redisCacheManger.getRedisCacheInfo(CACHE_KEY + "_" + userId);
    }
}
