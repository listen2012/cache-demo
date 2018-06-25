package com.listen.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.listen.SpringBootDemo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootDemo.class)
public class RedisGetTest {

	@Autowired
	private StringRedisTemplate stringTemplate;

	@Test
	public void test() {
		Assert.assertEquals("keyTest", stringTemplate.opsForValue().get("keyTest"));
	}

}
