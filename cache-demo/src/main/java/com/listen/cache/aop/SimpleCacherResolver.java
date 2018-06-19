/*
 * Copyright 2002-2014 the original author or authors.
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

package com.listen.cache.aop;
import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.BasicOperation;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

import com.listen.cache.map.AbstractBasicCache;

/**
 * A simple {@link CacheResolver} that resolves the {@link Cache} instance(s)
 * based on a configurable {@link CacheManager} and the name of the
 * cache(s) as provided by {@link BasicOperation#getCacheNames() getCacheNames()}
 *
 * @author Stephane Nicoll
 * @since 4.1
 * @see BasicOperation#getCacheNames()
 */
public class SimpleCacherResolver extends AbstractCacherResolver {

	public SimpleCacherResolver() {
	}

	public SimpleCacherResolver(CacheManager cacheManager) {
		super(cacheManager);
	}

	@Override
	protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
		return context.getOperation().getCacheNames();
	}

	@Override
	public Collection<? extends AbstractBasicCache> resolveCaches(
			CacheOperationInvocationContext<?> context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractBasicCache resolveCache(
			CacheOperationInvocationContext<?> context) {
		// TODO Auto-generated method stub
		return (AbstractBasicCache) getCacheManager().getCache(null);
	}

}
