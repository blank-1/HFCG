package com.xt.cfp.core.service.redis;


import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.xt.cfp.core.util.LogUtils;
import com.xt.cfp.core.util.Property;

@Property
public class RedisCacheManger implements RedisCache {

	public JedisPool pool ;
	
	private Logger log = Logger.getLogger(RedisCacheManger.class);

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}
	
	private static Integer smsCodeTime;
	
	public static Integer getSmsCodeTime() {
        return smsCodeTime;
    }
	
	@Property(name = "register_smscode_time")
    public static void setSmsCodeTime(Integer smsCodeTime) {
		RedisCacheManger.smsCodeTime=smsCodeTime;
	}
	
	public <T> T getRedisCacheInfo(String key) {
		Jedis jedis = null;
		try {
			log.info(LogUtils.createSimpleLog("从redis服务器取值","key :"+key));
			jedis= pool.getResource();
			return (T)jedis.get(key);
		} catch (Exception e) {
			log.error(LogUtils.createSimpleLog("从redis服务器取值","key :"+key), e);
			return null;
		} finally {
			if (jedis!=null)
				pool.returnResource(jedis);
		}
	}

	public <T> boolean destroyRedisCacheInfo(String key) {
		Jedis jedis = null ;
		try {
			log.info(LogUtils.createSimpleLog("从redis服务器移除值","key :"+key));
			jedis= pool.getResource();
			jedis.del(key);
			pool.returnResource(jedis);
			return true;
		} catch (Exception e) {
			log.error(LogUtils.createSimpleLog("从redis服务器移除值","key :"+key), e);
			return false;
		} finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
	}

	public <T> boolean setRedisCacheInfo(String key, T value) {
		Jedis jedis = null ;
		try {
			log.info(LogUtils.createSimpleLog("向redis服务器插入值","key :"+key));
			jedis = pool.getResource();
			jedis.set(key, (String)value);
			pool.returnResource(jedis);
			return true;
		} catch (Exception e) {
			log.error(LogUtils.createSimpleLog("向redis服务器插入值时报错", "key :" + key),e);
			return false;
		} finally {
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
//		return false;
	}
	
	public Set<String> getAllRedisCacheInfoBySort(String key) {
		Jedis jedis = null;
		try {
			log.info(LogUtils.createSimpleLog("从redis服务器取出值","key :"+key));
			jedis = pool.getResource();
			return jedis.keys(key);
		} catch (Exception e) {
			log.error(LogUtils.createSimpleLog("从redis服务器取出值","key :"+key), e);
			return null;
		} finally {
			if (jedis!=null)
				pool.returnResource(jedis);
		}
	}

	public <T> boolean setRedisCacheInfo(String key, T value,int seconds) {
		Jedis jedis = null ;
		try {
			log.info(LogUtils.createSimpleLog("向redis服务器插入值","key :"+key));
			jedis = pool.getResource();
			jedis.setex(key,seconds, (String) value);
			pool.returnResource(jedis);
			return true;
		} catch (Exception e) {
			log.error(LogUtils.createSimpleLog("向redis服务器插入值时报错", "key :" + key),e);
			return false;
		} finally {
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
//		return false;
	}
	
	public  long setRedisCacheInfonx(String key, String value,int seconds) {
		Jedis jedis = null;
		try {
			log.info(LogUtils.createSimpleLog("向redis服务器插入值","key :"+key));
			jedis = pool.getResource();
			long l = jedis.setnx(key, value);
			jedis.expire(key, seconds);
			pool.returnResource(jedis);
			return l;
		} catch (Exception e) {
			log.error(LogUtils.createSimpleLog("向redis服务器插入值时报错", "key :" + key),e);
		} finally {
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return seconds;
	}
	
	/**
	 * 以秒为单位，返回给定 key 的剩余有效时间
	 * */
	public long getKeysValidTime(String key){
		Jedis jedis = null;
		try {
			log.info(LogUtils.createSimpleLog("从redis服务器查询key有效时间","key :"+key));
			jedis = pool.getResource() ;
			long seconds = jedis.ttl(key);
			return seconds;
		} catch (Exception e) {
			log.error(LogUtils.createSimpleLog("从redis服务器查询key有效时间时报错", "key : " + key),e);
			return -1;
		} finally {
			if (jedis!=null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 设置自增量
	 * */
	public long setIncreaseValidTime(String key,int seconds){
		Jedis jedis = null;
		try {
			log.info(LogUtils.createSimpleLog("从redis服务器查询key的自增变量","key :"+key));
			jedis = pool.getResource() ;
			String value = jedis.get(key);
			if(StringUtils.isBlank(value)){
				jedis.set(key, "0");
				jedis.expire(key, seconds);
			}
			long result = jedis.incr(key);
			return result ;
		} catch (Exception e) {
			log.error(LogUtils.createSimpleLog("从redis服务器查询key的自增变量时报错", "key : " + key),e);
			return -1;
		} finally {
			if (jedis!=null)
				pool.returnResource(jedis);
		}
	}

	public boolean testConn(){
		Jedis jedis = null;
		boolean flag=false;
		try {
			jedis = pool.getResource();
			jedis.set("testKey", "testKey");
			flag=jedis.exists("testKey");
		} catch (Exception e) {
			log.error(LogUtils.createSimpleLog("检测redis服务器是否正常testConn", "testKey"),e);
			e.printStackTrace();
		} finally {
			if (jedis!=null)
				pool.returnResource(jedis);
		}
		return flag;
	}

	public JedisPool getPool() {
		return pool;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}

	public static void main(String[] args) {

//		ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");
//		final RedisCacheManger redisCacheManger = (RedisCacheManger)applicationContext.getBean("redisCacheManager");
//		redisCacheManger.setRedisCacheInfo("key-1", "asdfg");
//		for(int i=0;i<1000;i++){
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					System.out.println(redisCacheManger.getRedisCacheInfo("key-1"));
//				}
//			}).start();
////			System.out.println(redisCacheManger.getRedisCacheInfo("12345"));
//		}

	}
}
