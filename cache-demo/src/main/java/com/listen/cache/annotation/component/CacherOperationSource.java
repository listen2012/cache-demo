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

package com.listen.cache.annotation.component;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.cache.annotation.CacheAnnotationParser;
import org.springframework.cache.interceptor.CacheOperation;

import com.listen.cache.annotation.cacheroperation.AbstractCacheOperation;

@SuppressWarnings("serial")
public class CacherOperationSource extends AbstractFallbackCacherOperationSource implements Serializable {

	private final boolean publicMethodsOnly;

	private final Set<CacherAnnotationParser> cacherParsers;


	/**
	 * Create a default AnnotationCacheOperationSource, supporting public methods
	 * that carry the {@code Cacheable} and {@code CacheEvict} annotations.
	 */
	public CacherOperationSource() {
		this(true);
	}

	/**
	 * Create a default {@code AnnotationCacheOperationSource}, supporting public methods
	 * that carry the {@code Cacheable} and {@code CacheEvict} annotations.
	 * @param publicMethodsOnly whether to support only annotated public methods
	 * typically for use with proxy-based AOP), or protected/private methods as well
	 * (typically used with AspectJ class weaving)
	 */
	public CacherOperationSource(boolean publicMethodsOnly) {
		this.publicMethodsOnly = publicMethodsOnly;
		this.cacherParsers = new LinkedHashSet<CacherAnnotationParser>(1);
		this.cacherParsers.add(new CacherAnnotationParser());
	}

	@Override
	protected Collection<AbstractCacheOperation> findCacheOperations(final Method method) {
		return determineCacherOperations(new CacherOperationProvider() {
			@Override
			public Collection<AbstractCacheOperation> getCacherOperations(
					CacherAnnotationParser parser) {
				// TODO Auto-generated method stub
				return parser.parseCacheAnnotations(method);
			}
		});
	}

	@Override
	protected Collection<AbstractCacheOperation> findCacheOperations(Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Collection<AbstractCacheOperation> determineCacherOperations(CacherOperationProvider provider) {
		Collection<AbstractCacheOperation> ops = null;
		for (CacherAnnotationParser annotationParser : this.cacherParsers) {
			Collection<AbstractCacheOperation> annOps = provider.getCacherOperations(annotationParser);
			if (annOps != null) {
				if (ops == null) {
					ops = new ArrayList<AbstractCacheOperation>();
				}
				ops.addAll(annOps);
			}
		}
		return ops;
	}
	
	/**
	 * Callback interface providing {@link CacheOperation} instance(s) based on
	 * a given {@link CacheAnnotationParser}.
	 */
	protected interface CacherOperationProvider {

		/**
		 * Return the {@link CacheOperation} instance(s) provided by the specified parser.
		 * @param parser the parser to use
		 * @return the cache operations, or {@code null} if none found
		 */
		Collection<AbstractCacheOperation> getCacherOperations(CacherAnnotationParser parser);
	}



}
