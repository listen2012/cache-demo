package com.listen.cache.redis;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisOperations;

public class RedisCacheManager implements CacheManager {
	private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>(
			16);

	private static final String DEFAULT_NAME = "defaultName";

	@SuppressWarnings("rawtypes")
	private final RedisOperations redisOperations;

	private long defaultExpiration = 0;
	 
	public RedisCacheManager(@SuppressWarnings("rawtypes") RedisOperations redisOperations){
		this.redisOperations = redisOperations;
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = null;
		if (!cacheMap.isEmpty()) {
			return cacheMap.get(DEFAULT_NAME);
		} else {
			cache = createCache();
			cacheMap.putIfAbsent(DEFAULT_NAME, cache);
		}
		return cache;
	}
	
	@SuppressWarnings("rawtypes")
	protected RedisOperations getRedisOperations() {
		return redisOperations;
	}

	@Override
	public Collection<String> getCacheNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected Cache createCache() {
		return new RedisCache(redisOperations, defaultExpiration);
	}

}