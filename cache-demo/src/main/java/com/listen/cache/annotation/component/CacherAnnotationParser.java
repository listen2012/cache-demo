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

package com.listen.cache.annotation.component;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.core.annotation.AnnotatedElementUtils;

import com.listen.cache.annotation.CacheRemover;
import com.listen.cache.annotation.Cacher;
import com.listen.cache.annotation.cacheroperation.CacheRemoverOperation;
import com.listen.cache.annotation.cacheroperation.CacherOperation;

@SuppressWarnings("serial")
public class CacherAnnotationParser implements Serializable {

	public Collection<CacherOperation> parseCacheAnnotations(Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<CacheOperation> parseCacheAnnotations(Method method) {
		// TODO Auto-generated method stub
		return parseCacherAnnotations(method);
	}

	// protected Collection<CacherOperation> parseCacherAnnotation(Method
	// method){
	// Collection<CacherOperation> co = new ArrayList<CacherOperation>(1);
	// co.add(new CacherOperation(null));
	// return co;
	// }

	protected Collection<CacheOperation> parseCacherAnnotations(
			AnnotatedElement ae) {
		Collection<CacheOperation> ops = new ArrayList<CacheOperation>(1);

		Collection<Cacher> cachers = AnnotatedElementUtils
				.getAllMergedAnnotations(ae, Cacher.class);
		if (!cachers.isEmpty()) {
			for (Cacher cacher : cachers) {
				ops.add(parseCacherAnnotation(ae, cacher));
			}
		}

		Collection<CacheRemover> cacheRemovers = AnnotatedElementUtils
				.getAllMergedAnnotations(ae, CacheRemover.class);
		if (!cacheRemovers.isEmpty()) {
			for (CacheRemover cacheRemover : cacheRemovers) {
				ops.add(parseCacheRemoverAnnotation(ae, cacheRemover));
			}
		}
		return ops;
	}

	CacherOperation parseCacherAnnotation(AnnotatedElement ae, Cacher cacher) {
		CacherOperation.Builder builder = new CacherOperation.Builder();
		builder.setKey(cacher.key());
		CacherOperation op = new CacherOperation(builder);
		return op;
	}

	CacheRemoverOperation parseCacheRemoverAnnotation(AnnotatedElement ae, CacheRemover cacheRemover) {
		CacheRemoverOperation.Builder builder = new CacheRemoverOperation.Builder();
		builder.setKey(cacheRemover.key());
		CacheRemoverOperation op = new CacheRemoverOperation(builder);
		return op;
	}

}
