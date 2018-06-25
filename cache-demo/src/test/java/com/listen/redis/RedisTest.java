package com.listen.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.listen.SpringBootDemo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootDemo.class)
public class RedisTest {

	@Autowired
	private RedisTemplate<Object, Object> template;

	@Test
	public void test() {
		template.opsForValue().set("nosql", "redis");
		Assert.assertEquals("redis", template.opsForValue().get("nosql"));
	}

}
