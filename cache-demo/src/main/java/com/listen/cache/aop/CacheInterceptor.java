package com.listen.cache.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.listen.cache.annotation.Cacher;
import com.listen.cache.map.CacheMapHandler;

@Aspect
@Component
public class CacheInterceptor {

	@Autowired
	private CacheMapHandler cacheHandler;

	@Pointcut("@annotation(com.listen.cache.annotation.Cacher)")
	public void cachePutPointcut() {
	};

	@Around("cachePutPointcut()")
	public void cachePut(ProceedingJoinPoint pjp) {
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		Cacher cache = method.getAnnotation(Cacher.class);
		Object[] args = pjp.getArgs();
		try {
			Object value = pjp.proceed(args);
			cacheHandler.put(cache.key(), value);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
