/*
 * Copyright 2002-2017 the original author or authors.
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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.cache.interceptor.BasicOperation;
import org.springframework.util.Assert;

import com.listen.cache.aop.CacherAbstractSupport.CacherOperationContext;

/**
 * Base class for cache operations.
 *
 * @author Costin Leau
 * @author Stephane Nicoll
 * @author Marcin Kamionowski
 * @since 3.1
 */
public abstract class AbstractCacheOperation implements BasicOperation {

	private final String name;

	private final Set<String> cacheNames;

	private final String key;

	private final String keyGenerator;

	private final String cacheManager;

	private final String cacheResolver;

	private final String condition;

	private final String toString;
	
	private Object codedKey;

	/**
	 * @since 4.3
	 */
	protected AbstractCacheOperation(Builder b) {
		this.name = b.name;
		this.cacheNames = b.cacheNames;
		this.key = b.key;
		this.keyGenerator = b.keyGenerator;
		this.cacheManager = b.cacheManager;
		this.cacheResolver = b.cacheResolver;
		this.condition = b.condition;
		this.toString = b.getOperationDescription().toString();
	}

	public String getName() {
		return this.name;
	}

	@Override
	public Set<String> getCacheNames() {
		return this.cacheNames;
	}

	public String getKey() {
		return this.key;
	}

	public String getKeyGenerator() {
		return this.keyGenerator;
	}

	public String getCacheManager() {
		return this.cacheManager;
	}

	public String getCacheResolver() {
		return this.cacheResolver;
	}

	public String getCondition() {
		return this.condition;
	}

	/**
	 * This implementation compares the {@code toString()} results.
	 * 
	 * @see #toString()
	 */
	@Override
	public boolean equals(Object other) {
		return (other instanceof AbstractCacheOperation && toString().equals(
				other.toString()));
	}

	/**
	 * This implementation returns {@code toString()}'s hash code.
	 * 
	 * @see #toString()
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * Return an identifying description for this cache operation.
	 * <p>
	 * Returned value is produced by calling
	 * {@link Builder#getOperationDescription()} during object construction.
	 * This method is used in {@link #hashCode} and {@link #equals}.
	 * 
	 * @see Builder#getOperationDescription()
	 */
	@Override
	public final String toString() {
		return this.toString;
	}

	public Object operate(CacherOperationContext context) throws Throwable {
		Object value = null;

		if (checkBeforeInvoke(context)) {
			try {
				value = context.getInvoker().proceed();
				context.setValue(value);
			} catch (Throwable e) {
				e.printStackTrace();
				throw new Throwable(e);
			}
			refreshCache(context);
		} else {
			value = executeWithoutInvoke(context);
		}
//		executeAfterInvoke(context);
		return value;
	}

	protected boolean checkBeforeInvoke(CacherOperationContext context) {
		return true;
	}

	protected Object executeWithoutInvoke(CacherOperationContext context) {
		return null;
	}
	
	protected void refreshCache(CacherOperationContext context){
		context.getCache().put(
				context.getOperationByClass(CacherOperation.class)
						.getCodedKey(), context.getValue());
	}

	protected void executeAfterInvoke(CacherOperationContext context) {}

//	protected boolean tryTofindInCache(
//			CacherOperationContext context) {
//		return context.getCache().lookup(key);
//	}
//
//	protected Object findInCache(Object key, CacherOperationContext context) {
//		Object value = context.getCache().get(key).get();
//		return value;
//	}

	public Object getCodedKey() {
		return codedKey;
	}

	public void setCodedKey(Object codedKey) {
		this.codedKey = codedKey;
	}

	/**
	 * @since 4.3
	 */
	public abstract static class Builder {

		private String name = "";

		private Set<String> cacheNames = Collections.emptySet();

		private String key = "";

		private String keyGenerator = "";

		private String cacheManager = "";

		private String cacheResolver = "";

		private String condition = "";

		public void setName(String name) {
			Assert.hasText(name, "Name must not be empty");
			this.name = name;
		}

		public void setCacheName(String cacheName) {
			Assert.hasText(cacheName, "Cache name must not be empty");
			this.cacheNames = Collections.singleton(cacheName);
		}

		public void setCacheNames(String... cacheNames) {
			this.cacheNames = new LinkedHashSet<String>(cacheNames.length);
			for (String cacheName : cacheNames) {
				Assert.hasText(cacheName,
						"Cache name must be non-empty if specified");
				this.cacheNames.add(cacheName);
			}
		}

		public Set<String> getCacheNames() {
			return this.cacheNames;
		}

		public void setKey(String key) {
			Assert.notNull(key, "Key must not be null");
			this.key = key;
		}

		public String getKey() {
			return this.key;
		}

		public String getKeyGenerator() {
			return this.keyGenerator;
		}

		public String getCacheManager() {
			return this.cacheManager;
		}

		public String getCacheResolver() {
			return this.cacheResolver;
		}

		public void setKeyGenerator(String keyGenerator) {
			Assert.notNull(keyGenerator, "KeyGenerator name must not be null");
			this.keyGenerator = keyGenerator;
		}

		public void setCacheManager(String cacheManager) {
			Assert.notNull(cacheManager, "CacheManager name must not be null");
			this.cacheManager = cacheManager;
		}

		public void setCacheResolver(String cacheResolver) {
			Assert.notNull(cacheResolver, "CacheResolver name must not be null");
			this.cacheResolver = cacheResolver;
		}

		public void setCondition(String condition) {
			Assert.notNull(condition, "Condition must not be null");
			this.condition = condition;
		}

		/**
		 * Return an identifying description for this caching operation.
		 * <p>
		 * Available to subclasses, for inclusion in their {@code toString()}
		 * result.
		 */
		protected StringBuilder getOperationDescription() {
			StringBuilder result = new StringBuilder(getClass().getSimpleName());
			result.append("[").append(this.name);
			result.append("] caches=").append(this.cacheNames);
			result.append(" | key='").append(this.key);
			result.append("' | keyGenerator='").append(this.keyGenerator);
			result.append("' | cacheManager='").append(this.cacheManager);
			result.append("' | cacheResolver='").append(this.cacheResolver);
			result.append("' | condition='").append(this.condition).append("'");
			return result;
		}

		public abstract AbstractCacheOperation build();
	}

}
