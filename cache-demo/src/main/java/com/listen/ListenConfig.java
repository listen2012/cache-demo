package com.listen;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableCacher
@EnableCaching
public class ListenConfig extends CachingConfigurerSupport{
//	@Bean 
    @Override
    public CacheManager cacheManager() {
        // configure and return CacheManager instance
//		return new com.listen.cache.map.ConcurrentMapCacheManager();
		return new org.springframework.cache.concurrent.ConcurrentMapCacheManager();
    }
}