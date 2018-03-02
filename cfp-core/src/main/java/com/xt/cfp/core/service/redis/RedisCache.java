package com.xt.cfp.core.service.redis;

public interface RedisCache {

	public <T> T getRedisCacheInfo(String key);

	public <T> boolean setRedisCacheInfo(String key, T value);
	
}