package com.listen.cache;

import org.springframework.stereotype.Component;

import com.listen.cache.annotation.Cacher;

@Component
//@Cacheable(cacheNames = "test")
public class CachePutTest {
	final static String KEY = "test";

	@Cacher(key = "test")
	public String put(String value, String key) throws Exception {
		System.out.println("cache put test ...");
		return value + 1;
	}
}
