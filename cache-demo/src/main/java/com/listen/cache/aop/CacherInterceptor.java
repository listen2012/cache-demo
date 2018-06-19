package com.listen.cache.aop;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.interceptor.CacheOperationInvoker;

@SuppressWarnings("serial")
public class CacherInterceptor extends CacherAbstractSupport implements
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
			return execute(invocation, invocation.getThis(), method,
					invocation.getArguments());
		} catch (CacheOperationInvoker.ThrowableWrapper th) {
			throw th.getOriginal();
		}
	}
}
