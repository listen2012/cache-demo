package com.listen.cache.aop;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.cache.interceptor.CacheOperationInvoker;

import com.listen.cache.annotation.cacheroperation.CacherOperation;
import com.listen.cache.annotation.cacheroperation.CacherOperationContext;

@SuppressWarnings("serial")
public class CacherInterceptor extends CacheAspectSupport implements
		MethodInterceptor, Serializable {
	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();

		// CacheOperationInvoker aopAllianceInvoker = new
		// CacheOperationInvoker() {
		// @Override
		// public Object invoke() {
		// try {
		// return invocation.proceed();
		// }
		// catch (Throwable ex) {
		// throw new ThrowableWrapper(ex);
		// }
		// }
		// };

		try {
			System.out
					.println("-------- enter CacherInterceptor -------------");
			return execute(invocation, invocation.getThis(), method,
					invocation.getArguments());
		} catch (CacheOperationInvoker.ThrowableWrapper th) {
			throw th.getOriginal();
		}
	}

	private Object execute(final MethodInvocation invoker, Object target,
			Method method, Object[] args) throws Throwable {
		Collection<CacheOperation> ops = getCacheOperationSource()
				.getCacheOperations(method, target.getClass());

		// assemble operation, cachemanager
		CacherOperationContext context = new CacherOperationContext(ops);
		Object key = generateKey(CacherOperation.class, context);
		// cachehit
		Object value = null;
//		if (tryTofindInCache(key, context)) {
			value = findInCache(key, context);
			if(value != null)
			return value;
//		}
		value = invoker.proceed();
		// update cache
		getCacheManager();
		return value;
	}

	private Object generateKey(Class<? extends CacheOperation> clazz,
			CacherOperationContext context) {
		CacheOperation cacherop = context.get(clazz);
		return cacherop.getKey();
	}

	private boolean tryTofindInCache(Object key, CacherOperationContext context) {
		return context.getCache().lookup(key);
	}

	private Object findInCache(Object key, CacherOperationContext context) {
		Object value = context.getCache().get(key);
		return value;
	}

}
