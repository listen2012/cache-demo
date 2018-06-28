package com.listen.cacher;

import org.springframework.stereotype.Component;

import com.listen.cache.annotation.Cacher;

@Component
public class CacherPutTest {
	final static String KEY = "test";

	@Cacher(key = "#p1")
	public String put(String value, String key) throws Exception {
		System.out.println("cache put test ...");
		return value + 1;
	}
}
