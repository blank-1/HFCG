package com.xt.cfp.core.util;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import jodd.util.StringUtil;

public class MongoClientFactory {

    private static MongoClientFactory instance = null;
    private MongoClient mongoClient;

    private MongoClientFactory() {
        initMongo();
        //authenticate();
    }

    public static MongoClientFactory getInstance() {
        if (instance == null) {
            synchronized (MongoClientFactory.class) {
                if (instance == null) {
                    instance = new MongoClientFactory();
                }
            }
        }
        return instance;

    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public DB getDB() {
        String database = ConfigUtil.getString("MONGO.DATABASE");
//		authenticate();
        return mongoClient.getDB(database);
    }

    private void initMongo() {
        int port = ConfigUtil.getInt("MONGO.PORT", 27017);
        String ip = ConfigUtil.getString("MONGO.IP", "localhost");

        try {
            ServerAddress sa = new ServerAddress(ip, port);
            mongoClient = new MongoClient(sa);

            MongoOptions options = mongoClient.getMongoOptions();
            options.autoConnectRetry = true;
            options.connectionsPerHost = 50;
            options.maxWaitTime = 5000;
            options.socketTimeout = 2000;
            options.connectTimeout = 15000;
            options.threadsAllowedToBlockForConnectionMultiplier = 4;
        } catch (Exception e) {
            throw new SystemException(SystemErrorCode.PROGRAM_ERROR_START).set("mongo", "mongo连接异常");
        }

    }

    private void authenticate() {
        String database = ConfigUtil.getString("MONGO.DATABASE");
        String userName = ConfigUtil.getString("MONGO.USER_NAME");
        String passward = ConfigUtil.getString("MONGO.PASSWARD");

        if (StringUtil.isEmpty(database) || StringUtil.isEmpty(userName) || StringUtil.isEmpty(passward)) {
            throw new SystemException(SystemErrorCode.PROGRAM_ERROR_START).set("mongo", "mongo参数未设置");
        }
        DB db = mongoClient.getDB(database);
        if (!db.isAuthenticated()) {
            boolean auth = db.authenticate(userName, passward.toCharArray());
            if (!auth) {
                throw new SystemException(SystemErrorCode.PROGRAM_ERROR_START).set("mongo", "mongo认证失败");
            }
        }
    }

}
