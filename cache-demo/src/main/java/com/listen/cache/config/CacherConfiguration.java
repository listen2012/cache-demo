package com.listen.cache.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.listen.cache.annotation.EnableCacher;
import com.listen.cache.annotation.component.CacherOperationSource;
import com.listen.cache.aop.CacherInterceptor;

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
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public BeanFactoryCacheOperationSourceAdvisor cacheAdvisor() {
		BeanFactoryCacheOperationSourceAdvisor advisor =
				new BeanFactoryCacheOperationSourceAdvisor();
		advisor.setCacheOperationSource(cacherOperationSource());
		advisor.setAdvice(cacherInterceptor());
//		advisor.setOrder(this.enableCaching.<Integer>getNumber("order"));
		return advisor;
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public CacheOperationSource cacherOperationSource() {
		return new CacherOperationSource();
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public CacherInterceptor cacherInterceptor() {
		CacherInterceptor interceptor = new CacherInterceptor();
		interceptor.setCacheOperationSources(cacherOperationSource());
		if (this.cacheResolver != null) {
			interceptor.setCacheResolver(this.cacheResolver);
		}
		else if (this.cacheManager != null) {
			interceptor.setCacheManager(this.cacheManager);
		}
		if (this.keyGenerator != null) {
			interceptor.setKeyGenerator(this.keyGenerator);
		}
		if (this.errorHandler != null) {
			interceptor.setErrorHandler(this.errorHandler);
		}
		return interceptor;
	}
}