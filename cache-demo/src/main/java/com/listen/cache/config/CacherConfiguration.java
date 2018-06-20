package com.listen.cache.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.listen.cache.annotation.EnableCacher;
import com.listen.cache.annotation.component.CacherOperationSource;
import com.listen.cache.annotation.component.ProxyCacheOperationSource;
import com.listen.cache.aop.CacherInterceptor;
import com.listen.cache.aop.CacherOperationSourceAdvisor;
import com.listen.cache.aop.CacherResolver;

@Configuration
public class CacherConfiguration extends AbstractCachingConfiguration{
	private final static String CACHER_BEAN = "cahcerBean";
	
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableCaching = AnnotationAttributes.fromMap(
				importMetadata.getAnnotationAttributes(EnableCacher.class.getName(), false));
		if (this.enableCaching == null) {
			throw new IllegalArgumentException(
					"@EnableCacher is not present on importing class " + importMetadata.getClassName());
		}
	}
	
	@Bean(name = CacherConfiguration.CACHER_BEAN)
	public CacherOperationSourceAdvisor cacheAdvisor() {
		CacherOperationSourceAdvisor advisor =
				new CacherOperationSourceAdvisor();
		advisor.setCacheOperationSource(cacherOperationSource());
		advisor.setAdvice(cacherInterceptor());
//		advisor.setOrder(this.enableCaching.<Integer>getNumber("order"));
		return advisor;
	}

	@Bean
	public ProxyCacheOperationSource cacherOperationSource() {
		return new CacherOperationSource();
	}

	@Bean
	public CacherInterceptor cacherInterceptor() {
		CacherInterceptor interceptor = new CacherInterceptor();
		interceptor.setCacheOperationSources(cacherOperationSource());
		if (this.cacheResolver != null) {
			interceptor.setCacherResolver((CacherResolver) this.cacheResolver);
		}
		else if (this.cacheManager != null) {
			interceptor.setCacheManager(this.cacheManager);
		}
		if (this.keyGenerator != null) {
//			interceptor.setKeyGenerator(this.keyGenerator);
		}
		if (this.errorHandler != null) {
			interceptor.setErrorHandler(this.errorHandler);
		}
		return interceptor;
	}
}