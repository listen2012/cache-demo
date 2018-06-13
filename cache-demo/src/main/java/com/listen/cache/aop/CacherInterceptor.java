package com.listen.cache.aop;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.cache.interceptor.CacheOperationInvoker;

import com.listen.cache.annotation.Cacher;
import com.listen.cache.annotation.cacheroperation.CacherOperation;
import com.listen.cache.annotation.cacheroperation.CacherOperationContext;
import com.listen.cache.map.ConcurrentMapCache;

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

		CacherOperationContext context = new CacherOperationContext();
		//assemble opation, cachemanager
		
		for (CacheOperation op : ops) {
			opMap.put(op.getClass(), op);	
		}
		
		// cachehit
		CacheOperation cacherOp = opMap.get(Cacher.class);
		String cacherKey = cacherOp.getKey();
		Object value = cacheMap.get(key);
		if (value != null) {
			return value;
		}
		value = invoker.proceed();
		cacheMap.put(key, value);
		return value;

	}
	
	private final LinkedHashMap<Class, CacheOperation> opMap = new LinkedHashMap<Class, CacheOperation>();
}
