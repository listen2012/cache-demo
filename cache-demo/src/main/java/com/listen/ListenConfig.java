package com.listen;

import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.listen.cache.annotation.EnableCacher;

@Configuration
@EnableCacher
// @EnableCaching
public class ListenConfig extends CachingConfigurerSupport {
	
	@Autowired
	private RedisTemplate<Object, Object> template;

	@Bean
	public CacheManager cacheManager() {
		// configure and return CacheManager instance
//		 return new com.listen.cache.map.ConcurrentMapCacheManager();
		return new com.listen.cache.redis.RedisCacheManager(template);
//		return new com.listen.cache.redis.RedisCacheManager(redisTemplate);
	}

	/**
	 * Standard Redis configuration.
	 */
	@Configuration
	protected static class JedisConfig {

		// public static final String JEDIS_PREFIX = "jedis";

		@Bean(name = "jedisPool")
		@Autowired
		public JedisPool jedisPool(
				@Qualifier("jedisPoolConfig") JedisPoolConfig config,
				@Value("${spring.jedis.pool.host}") String host,
				@Value("${spring.jedis.pool.port}") int port,
				@Value("${spring.jedis.pool.timeout}") int timeout,
				@Value("${spring.jedis.pool.password}") String password) {
			return new JedisPool(config, host, port, timeout, password);
		}

		@Bean(name = "jedisPoolConfig")
		public JedisPoolConfig jedisPoolConfig(
				@Value("${spring.jedis.pool.config.maxTotal}") int maxTotal,
				@Value("${spring.jedis.pool.config.maxIdle}") int maxIdle,
				@Value("${spring.jedis.pool.config.maxWaitMillis}") int maxWaitMillis) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(maxTotal);
			config.setMaxIdle(maxIdle);
			config.setMaxWaitMillis(maxWaitMillis);
			return config;
		}
	}

	@Configuration
	protected static class RedisConfiguration {

		@Bean
		public RedisTemplate<Object, Object> redisTemplate(
				RedisConnectionFactory redisConnectionFactory)
				throws UnknownHostException {
			RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
			template.setConnectionFactory(redisConnectionFactory);
			template.setKeySerializer(new StringRedisSerializer());
			template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
			return template;
		}

		@Bean
		public StringRedisTemplate stringRedisTemplate(
				RedisConnectionFactory redisConnectionFactory)
				throws UnknownHostException {
			StringRedisTemplate template = new StringRedisTemplate();
			template.setConnectionFactory(redisConnectionFactory);
			return template;
		}

	}

}