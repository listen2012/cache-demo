package com.listen.cacher;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.listen.SpringBootDemo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootDemo.class)
public class CacherTest {

	@Autowired
	private CacherPutTest putTest;

	@Test
	public void test() {
		try {
			String value = putTest.put("put a string", "key_test_1");
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

		String value = (String)putTest.put("get a string", "key_test_2");
		System.out.println("check get value : ------ " + value.toString());
	}
}
