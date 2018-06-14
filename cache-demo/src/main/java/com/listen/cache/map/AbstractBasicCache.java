package com.listen.cache.map;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

public abstract class AbstractBasicCache implements Cache {
	private Cache cache;

	@Value("${CACHE_NULLVALUE}")
	private Boolean defaultHit;
	
	public boolean lookup(Object key) {
		return defaultHit == null ? false : defaultHit;
	}
	
	@Override
	public ValueWrapper get(Object key) {
		Object value = cache.get(key);
		return toValueWrapper(value);
	}

	@Override
	public void put(Object key, Object value) {
		// TODO Auto-generated method stub
//		cache.putIfAbsent(key, value);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getNativeCache() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void evict(Object key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	protected Cache.ValueWrapper toValueWrapper(Object storeValue) {
		return (storeValue != null ? new SimpleValueWrapper(storeValue) : null);
	}
}