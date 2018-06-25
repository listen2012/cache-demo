package com.listen.springcache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

@Component
// @Cacheable(cacheNames = "test")
public class CachePutTest {
	final static String KEY = "test";

	@CachePut(cacheNames="test", key = "#a1")
	public List put(String value, String key) throws Exception {
		System.out.println("cache put test ...");
		List<String> list = new ArrayList<String>();
		list.add("d");
		list.add("e");
		list.add("f");
		return list;
	}
}
