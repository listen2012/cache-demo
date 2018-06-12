package com.listen.cache.map;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class CacheMapHandler {
	private final static ConcurrentHashMap<String, Object> cacheMap = new ConcurrentHashMap<String, Object>();

	public void put(String key, Object value) {
		cacheMap.putIfAbsent(key, value);
	}
	
	public Object get(String key){
		return cacheMap.get(key);
	}
}