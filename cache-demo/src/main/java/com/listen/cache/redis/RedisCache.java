/*
 * Copyright 2011-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.listen.cache.redis;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ClassUtils;

import com.listen.cache.map.AbstractBasicCache;

/**
 * Cache implementation on top of Redis.
 *
 * @author Costin Leau
 * @author Christoph Strobl
 * @author Thomas Darimont
 * @author Mark Paluch
 */
@SuppressWarnings("unchecked")
public class RedisCache extends AbstractBasicCache {

	@SuppressWarnings("rawtypes")
	private final RedisOperations redisOperations;

	public RedisCache(
			RedisOperations<? extends Object, ? extends Object> redisOperations,
			long expiration) {
		this(redisOperations, expiration, false);
	}

	/**
	 * Constructs a new {@link RedisCache} instance.
	 *
	 * @param name
	 *            cache name
	 * @param prefix
	 *            must not be {@literal null} or empty.
	 * @param redisOperations
	 * @param expiration
	 * @param allowNullValues
	 * @since 1.8
	 */
	public RedisCache(
			RedisOperations<? extends Object, ? extends Object> redisOperations,
			long expiration, boolean allowNullValues) {

		RedisSerializer<?> serializer = redisOperations.getValueSerializer() != null ? redisOperations
				.getValueSerializer()
				: (RedisSerializer<?>) new JdkSerializationRedisSerializer();

		// this.cacheMetadata.setDefaultExpiration(expiration);
		this.redisOperations = redisOperations;
		// this.cacheValueAccessor = new CacheValueAccessor(serializer);

		if (allowNullValues) {

			if (redisOperations.getValueSerializer() instanceof StringRedisSerializer
					|| redisOperations.getValueSerializer() instanceof GenericToStringSerializer
					|| redisOperations.getValueSerializer() instanceof JacksonJsonRedisSerializer
					|| redisOperations.getValueSerializer() instanceof Jackson2JsonRedisSerializer) {
				throw new IllegalArgumentException(
						String.format(
								"Redis does not allow keys with null value 炉\\_(銉�)_/炉. "
										+ "The chosen %s does not support generic type handling and therefore cannot be used with allowNullValues enabled. "
										+ "Please use a different RedisSerializer or disable null value support.",
								ClassUtils.getShortName(redisOperations
										.getValueSerializer().getClass())));
			}
		}
	}
	
	@Override
	public boolean lookup(Object key) {
		if (redisOperations.hasKey(key)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public ValueWrapper get(Object key) {
		Object value = redisOperations.opsForValue().get(key);
		return toValueWrapper(value);
	}
	
	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		Object existing = redisOperations.opsForValue().setIfAbsent(key, value);
		return toValueWrapper(existing);
	}
	
	@Override
	public void put(Object key, Object value) {
		redisOperations.opsForValue().set(key, value);
	}

}
