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

package com.listen.cache.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.AbstractCacheInvoker;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.Assert;

import com.listen.cache.annotation.cacheroperation.AbstractCacheOperation;
import com.listen.cache.annotation.component.ProxyCacheOperationSource;
import com.listen.cache.annotation.expression.CacherOperationExpressionEvaluator;
import com.listen.cache.map.AbstractBasicCache;

/**
 * Base class for caching aspects, such as the {@link CacheInterceptor} or an
 * AspectJ aspect.
 *
 * <p>
 * This enables the underlying Spring caching infrastructure to be used easily
 * to implement an aspect for any aspect system.
 *
 * <p>
 * Subclasses are responsible for calling methods in this class in the correct
 * order.
 *
 * <p>
 * Uses the <b>Strategy</b> design pattern. A {@link CacheResolver}
 * implementation will resolve the actual cache(s) to use, and a
 * {@link CacheOperationSource} is used for determining caching operations.
 *
 * <p>
 * A cache aspect is serializable if its {@code CacheResolver} and
 * {@code CacheOperationSource} are serializable.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @author Chris Beams
 * @author Phillip Webb
 * @author Sam Brannen
 * @author Stephane Nicoll
 * @since 3.1
 */
public abstract class CacherAbstractSupport extends AbstractCacheInvoker
		implements BeanFactoryAware, InitializingBean,
		SmartInitializingSingleton {

	private ProxyCacheOperationSource cacheOperationSource;

	private KeyGenerator keyGenerator = new SimpleKeyGenerator();

	private CacherResolver cacherResolver;

	private BeanFactory beanFactory;

	private final CacherOperationExpressionEvaluator evaluator = new CacherOperationExpressionEvaluator();

	public ProxyCacheOperationSource getCacheOperationSource() {
		return this.cacheOperationSource;
	}

	protected Object execute(final MethodInvocation invoker, Object target,
			Method method, Object[] args) throws Throwable {
		Collection<AbstractCacheOperation> ops = getCacheOperationSource()
				.getCacheOperations(method, target.getClass());

		// assemble operation, cachemanager
		CacherOperationContext context = new CacherOperationContext(ops,
				invoker, target, target.getClass(), method, args);
		// Object key = context.generateKey(CacherOperation.class, context);

		// cachehit
		Object value = null;
		// if (tryTofindInCache(key, context)) {
		// value = findInCache(key, context);
		// if (value != null)
		// return value;
		// }
		// value = invoker.proceed();
		// // update cache
		// context.getCache().put(key, value);
		try {
			for (AbstractCacheOperation op : context.opMap.values()) {
				value = op.operate(context);
			}
			// context.opMap.forEach((k, v) -> {
			// value = v.operate(context);
			// });
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
		return value;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacherResolver = new SimpleCacherResolver(cacheManager);
	}

	public void setCacheOperationSources(
			ProxyCacheOperationSource... cacheOperationSources) {
		Assert.notEmpty(cacheOperationSources,
				"At least 1 CacheOperationSource needs to be specified");
		// this.cacheOperationSource = (cacheOperationSources.length > 1 ?
		// new CompositeCacheOperationSource(cacheOperationSources) :
		// cacheOperationSources[0]);
		this.cacheOperationSource = cacheOperationSources[0];
	}

	public void setCacherResolver(CacherResolver cacherResolver) {
		this.cacherResolver = cacherResolver;
	}

	/**
	 * Return the default {@link CacheResolver} that this cache aspect delegates
	 * to.
	 */
	public CacherResolver getCacherResolver() {
		return this.cacherResolver;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterSingletonsInstantiated() {
		// TODO Auto-generated method stub

	}

	protected AbstractBasicCache getCache(CacherOperationContext context,
			CacherResolver cacherResolver) {
		AbstractBasicCache cache = cacherResolver.resolveCache(context);
		return cache;
	}

	public class CacherOperationContext implements
			CacheOperationInvocationContext<AbstractCacheOperation> {
		private final LinkedHashMap<Class<? extends AbstractCacheOperation>, AbstractCacheOperation> opMap;

		private AbstractBasicCache cache;

		private Object value;

		private final MethodInvocation invoker;

		private Method method;

		private Object target;

		private Class<?> targetClass;

		private Object[] args;

		private final AnnotatedElementKey methodCacheKey;

		private List<AbstractBasicCache> caches = new ArrayList<AbstractBasicCache>();

		public CacherOperationContext(Collection<AbstractCacheOperation> ops,
				final MethodInvocation invoker, Object target,
				Class<?> targetClass, Method method, Object[] args) {
			this.opMap = new LinkedHashMap<Class<? extends AbstractCacheOperation>, AbstractCacheOperation>(
					32);
			this.cache = CacherAbstractSupport.this.getCache(this,
					cacherResolver);
			caches.add(this.cache);
			for (AbstractCacheOperation op : ops) {
				add(op.getClass(), op);
			}

			this.invoker = invoker;
			this.method = method;
			this.target = target;
			this.targetClass = targetClass;
			this.args = args;
			this.methodCacheKey = new AnnotatedElementKey(this.method,
					this.targetClass);
			generateKey(this.opMap);
		}

		public void add(Class<? extends AbstractCacheOperation> class1,
				AbstractCacheOperation op) {
			this.opMap.put(class1, op);
		}

		public AbstractCacheOperation getOperationByClass(
				Class<? extends AbstractCacheOperation> clazz) {
			return this.opMap.get(clazz);
		}

		public AbstractBasicCache getCache() {
			return this.cache;
		}

		public final MethodInvocation getInvoker() {
			return this.invoker;
		}

		private void generateKey(
				LinkedHashMap<Class<? extends AbstractCacheOperation>, AbstractCacheOperation> opMap) {
			EvaluationContext evaluationContext = createEvaluationContext(CacherOperationExpressionEvaluator.NO_RESULT);
			this.opMap.forEach((k, v) -> {
				Object key = evaluator.key(v.getKey(), this.methodCacheKey,
						evaluationContext);
				v.setCodedKey(key);
			});
			// AbstractCacheOperation cacherop = context.get(clazz);
			// return evaluator.key(cacherop.getKey(), this.methodCacheKey,
			// evaluationContext);
			// return cacherop.getKey();
		}

		private EvaluationContext createEvaluationContext(Object result) {
			return evaluator.createEvaluationContext(caches, this.method,
					this.args, this.target, this.targetClass, result,
					beanFactory);
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		@Override
		public AbstractCacheOperation getOperation() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getTarget() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Method getMethod() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object[] getArgs() {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
