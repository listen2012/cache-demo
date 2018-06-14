package com.listen.cache.map;

import java.util.concurrent.ConcurrentMap;

import org.springframework.util.Assert;

public class ConcurrentMapCache extends AbstractBasicCache {
	private final ConcurrentMap<Object, Object> store;

	public ConcurrentMapCache(ConcurrentMap<Object, Object> store) {
		Assert.notNull(store, "cache must not be null");
		this.store = store;
	}

	@Override
	public boolean lookup(Object key) {
		if (this.store.containsKey(key)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public ValueWrapper get(Object key) {
		Object value = store.get(key);
		return toValueWrapper(value);
	}
	
	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		Object existing = this.store.putIfAbsent(key, value);
		return toValueWrapper(existing);
	}
	
	@Override
	public void put(Object key, Object value) {
		// TODO Auto-generated method stub
		store.putIfAbsent(key, value);
	}

}