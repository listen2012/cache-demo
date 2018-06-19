package com.listen.cache.map;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public class ConcurrentMapCacheManager implements CacheManager {
	private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>(16);

	private static final String DEFAULT_NAME = "defaultName";
	
	@Override
	public Cache getCache(String name) {
		Cache cache = null;
		if(!cacheMap.isEmpty()){
			return cacheMap.get(DEFAULT_NAME);
		}else{
			cache = createConcurrentMapCache(DEFAULT_NAME);
			cacheMap.putIfAbsent(DEFAULT_NAME, cache);
		}
		return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected Cache createConcurrentMapCache(String name) {
		return new ConcurrentMapCache(new ConcurrentHashMap<Object, Object>(256));

	}


}