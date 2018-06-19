package com.listen.springcache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
// @Cacheable(cacheNames = "test")
public class CachePutTest {
	final static String KEY = "test";

	@Cacheable(cacheNames="test", key = "#a1")
	public String put(String value, String key) throws Exception {
		System.out.println("cache put test ...");
		return value + 1;
	}
}
