/*
 * Copyright 2002-2016 the original author or authors.
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

package com.listen.cache.annotation.cacheroperation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.interceptor.CacheOperation;

import com.listen.cache.map.AbstractBasicCache;
import com.listen.cache.map.ConcurrentMapCache;

public class CacherOperationContext {
	private LinkedHashMap<Class<? extends CacheOperation>, CacheOperation> opMap;

	private AbstractBasicCache cache;

	public CacherOperationContext(Collection<CacheOperation> ops) {
		this.opMap = new LinkedHashMap<Class<? extends CacheOperation>, CacheOperation>(
				32);
		this.cache = generateCache();
		for (CacheOperation op : ops) {
			add(op.getClass(), op);
		}
	}

	public void add(Class<? extends CacheOperation> clazz,
			CacheOperation context) {
		this.opMap.put(clazz, context);
	}

	public CacheOperation get(Class<? extends CacheOperation> clazz) {
		return this.opMap.get(clazz);
	}

	public AbstractBasicCache getCache() {
		return this.cache;
	}

	private AbstractBasicCache generateCache() {
		return new ConcurrentMapCache(
				new ConcurrentHashMap<Object, Object>(256));
	}
}
