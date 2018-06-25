package com.listen.springcache;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.listen.SpringBootDemo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootDemo.class)
public class SpringCacheTest {

	@Autowired
	private CachePutTest putTest;

//	@Autowired
//	private CacheMapHandler handler;

	@Test
	public void test() {
		try {
			List value = putTest.put("put a string", "keyListTest");
			System.out.println("test get value : ------ " + value.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void check() throws Exception {
//		Object value = handler.get(CachePutTest.KEY);
		@SuppressWarnings({ "unused", "rawtypes" })
		Class put = putTest.getClass();
//		String key = putTest.getClass().getAnnotation(Cacheable.class).cacheNames()[0];
//		System.out.println("key : ------ " + key);
//		System.out.println("cacheManager : ------ " + manager);

		List value = (List)putTest.put("333", "keyListTest");
		System.out.println("check get value : ------ " + value.toString());
	}
}
